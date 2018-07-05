package cn.zhangxd.platform.admin.web.service.impl;

import cn.hutool.core.util.IdcardUtil;
import cn.zhangxd.platform.admin.web.domain.*;
import cn.zhangxd.platform.admin.web.domain.common.ArchiveStat;
import cn.zhangxd.platform.admin.web.domain.common.LogImpExcel;
import cn.zhangxd.platform.admin.web.domain.dto.StudentDetailDto;
import cn.zhangxd.platform.admin.web.domain.dto.StudentRequestForm;
import cn.zhangxd.platform.admin.web.domain.dto.StudentXlsDto;
import cn.zhangxd.platform.admin.web.domain.dto.TransmitRecordDto;
import cn.zhangxd.platform.admin.web.enums.NationalityEnum;
import cn.zhangxd.platform.admin.web.enums.SexEnum;
import cn.zhangxd.platform.admin.web.enums.TransmitEnum;
import cn.zhangxd.platform.admin.web.repository.*;
import cn.zhangxd.platform.admin.web.service.DictService;
import cn.zhangxd.platform.admin.web.service.StudentService;
import cn.zhangxd.platform.admin.web.util.Constants;
import cn.zhangxd.platform.admin.web.util.PaginationUtil;
import cn.zhangxd.platform.common.api.Paging;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import strman.Strman;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午8:13
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
@Service
@Slf4j
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DepartRepository departRepository;

    @Autowired
    private MajorRepository majorRepository;

    @Autowired
    private AdClassRepository adClassRepository;

    @Autowired
    private DictService dictService;

    @Autowired
    private StudentRelArchiveItemRepository studentRelArchiveItemRepository;

    @Autowired
    private ArchiveItemRepository archiveItemRepository;

    @Autowired
    private TransmitRecordRepository transmitRecordRepository;


    @Override
    public Student saveOne(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public List<Student> getStudentLikeFlag(String remarks) {
        return studentRepository.findByRemarksLike(remarks);
    }

    @Override
    public Boolean flagStudent(List<String> stuIds, String remarks) {

        Boolean flag = true;
        try {
            stuIds.forEach(sid -> {
                Student student = this.findOne(Long.parseLong(sid));
                student.setRemarks(remarks);
                studentRepository.save(student);
            });
        } catch (Exception e) {
            flag = false;
            log.error("批量标记学生档案失败: {}", e.getMessage());
        }
        return flag;
    }

    @Override
    public Student getStudentByIdCard(String idCard) {
        return studentRepository.getStudentByIdCard(idCard);
    }

    @Override
    public Integer countStudentTransmitTimes(Long id) {
        Integer times = 0;
        Student student = studentRepository.findOne(id);
        if (null != student) {
            times = transmitRecordRepository.findByStudentOrderByFluctTimeDesc(student).size();
        }
        return times;
    }


    @Override
    public Long countArchiveByDepart(Depart depart) {
        return studentRepository.countByDepart(depart);
    }

    @Override
    public Long countTotalArchive() {
        return studentRepository.count();
    }

    @Override
    public List<ArchiveStat> statisticsStudentsGroupByDepart(Integer acaYear) {

        // 默认按照当前年份统计
        List<ArchiveStat> archiveStats = Lists.newArrayList();

        List<Map<String, Object>> groupList = studentRepository.statStudentsGroupByDepart(acaYear);

        if (null != groupList && groupList.size() > 0) {
            archiveStats = groupList.stream().map(groupMap -> {
                ArchiveStat archiveStat = new ArchiveStat();
                archiveStat.setDepartCode(String.valueOf(groupMap.get("code")));
                archiveStat.setTotalAmount(Integer.parseInt(String.valueOf(groupMap.get("totalAmount"))));
                return archiveStat;
            }).collect(Collectors.toList());
        }

        return archiveStats;
    }

    @Override
    public List<Student> findByRollDepartAndStatus(Depart depart, TransmitEnum status) {
        return studentRepository.findByRollDepartAndStatus(depart, status);
    }

    @Override
    public List<Student> findByStatusIn(List<TransmitEnum> status) {
        return studentRepository.findByStatusIn(status);
    }

    @Override
    public List<Student> findByDepartAndStatusIn(Depart depart, List<TransmitEnum> status) {
        return studentRepository.findByDepartAndStatusIn(depart, status);
    }

    @Override
    public List<Student> reportStudentBySearchMap(Map<String, String> searchParams) {
        return studentRepository.findAll((Root<Student> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {

            List<Predicate> predicates = Lists.newArrayList();
            List<Predicate> orPredicate = new ArrayList<>();


            if (StringUtils.isNotBlank(searchParams.get("depart"))) {

                predicates.add(criteriaBuilder.equal(root.get("depart").get("code"), searchParams.get("depart")));
            }
            if (StringUtils.isNotBlank(searchParams.get("entranceYear"))) {
                predicates.add(criteriaBuilder.equal(root.get("entranceYear"), Integer.parseInt(searchParams.get("entranceYear"))));
            }

            if (StringUtils.isNotBlank(searchParams.get("gender"))) {
                predicates.add(criteriaBuilder.equal(root.get("sex"), SexEnum.valueOf(searchParams.get("gender"))));
            }

            String studentNo = searchParams.get("sno");
            if (StringUtils.isNotBlank(studentNo)) {
                StringJoiner joiner = new StringJoiner("");
                joiner.add("%").add(String.valueOf(studentNo)).add("%");

                orPredicate.add(criteriaBuilder.equal(root.get("examineeNo"), studentNo));
                orPredicate.add(criteriaBuilder.equal(root.get("studentNo"), studentNo));
                orPredicate.add(criteriaBuilder.like(root.get("name"), joiner.toString()));
                orPredicate.add(criteriaBuilder.like(root.get("remarks"), joiner.toString()));
                predicates.add(criteriaBuilder.or(orPredicate.toArray(new Predicate[]{})));

            }

            return predicates.size() > 0 ? PaginationUtil.buildQueryPredicate(predicates, criteriaBuilder) : null;
        });
    }

    @Override
    public List<Student> reportChooseStudent(Collection<Long> ids) {
        return studentRepository.findByIdIn(ids);
    }

    @Override
    public StudentDetailDto findStudentDetail(Long id) {

        StudentDetailDto dto = new StudentDetailDto();

        Student student = studentRepository.findOne(id);
        if (null != student) {

            dto.setStudentNo(student.getStudentNo());
            if (null != student.getAdClass()) {
                dto.setAdClass(student.getAdClass().getName());
            }

            dto.setAdmissionNo(student.getAdmissionNo());
            dto.setBackupPhone(student.getBackupPhone());
            if (null != student.getDepart()) {
                dto.setDepart(student.getDepart().getName());
            }

            dto.setName(student.getName());
            dto.setSex(student.getSex());
            dto.setEntranceYear(student.getEntranceYear());
            dto.setGraduationYear(student.getGraduationYear());
            dto.setExamineeNo(student.getExamineeNo());
            dto.setFamilyAddress(student.getFamilyAddress());
            dto.setIdCard(student.getIdCard());
            dto.setLinkPerson(student.getLinkPerson());
            dto.setNationality(student.getNationality());
            if (null != student.getMajor()) {
                dto.setMajor(student.getMajor().getName());
            }

            dto.setPostCode(student.getPostCode());
            dto.setStatus(student.getStatus());
            dto.setPrimaryPhone(student.getPrimaryPhone());

            dto.setTrackNo(student.getTrackNo());
            dto.setArchiveNo(student.getArchiveNo());
            dto.setArchiveGonePlace(student.getArchiveGonePlace());
            dto.setSourceRegion(student.getSourceRegion());
            dto.setReceiveUnit(student.getReceiveUnit());


            dto.setRecords(transmitRecordRepository.findByStudentOrderByFluctTimeDesc(student).stream().map(rec -> {
                TransmitRecordDto transmitRecordDto = new TransmitRecordDto();
                transmitRecordDto.setEventName(rec.getTransmitEventType().getTransmitEvent().getName());
                transmitRecordDto.setEventTypeName(rec.getTransmitEventType().getName());
                transmitRecordDto.setOperTime(rec.getFluctTime());
                transmitRecordDto.setRemarks(rec.getRemarks());
                if (null != rec.getFromDepart()) {
                    transmitRecordDto.setTransmitForm(rec.getFromDepart().getName());
                }
                if(null != rec.getDepart()){
                    transmitRecordDto.setTransmitTo(rec.getDepart().getName());
                }

                return transmitRecordDto;
            }).collect(Collectors.toList()));
        }
        return dto;
    }

    @Override
    public Map<String, Object> deleteStudentAttachById(Student student, Long itemId) {

        Map<String, Object> results = Maps.newHashMap();
        Boolean success = Boolean.FALSE;
        Long count = studentRelArchiveItemRepository.deleteByStudentAndItem(student, archiveItemRepository.findOne(itemId));
        if (count > 0) {
            success = Boolean.TRUE;
        }
        results.put("success", success);
        return results;
    }

    @Override
    public List<StudentRelArchiveItem> findArchiveItemByStudent(Student student) {
        return studentRelArchiveItemRepository.findByStudentOrderByItemSortAsc(student);
    }

    @Override
    public Iterable<Student> findAll() {
        return this.studentRepository.findAll();
    }


    @Override
    public List<Student> findStudentBySearchParam(Map<String, Object> searchParams) {
        String departName = String.valueOf(searchParams.get("depart"));
        return studentRepository.findStudentListBySearch(departName);
    }

    /**
     * TODO : 增加删除校验条件
     *
     * @param id
     * @return
     */
    @Override
    public Boolean deleteById(Long id) {
        Boolean flag = Boolean.TRUE;
        try {
            studentRepository.delete(id);
        } catch (Exception e) {
            flag = Boolean.FALSE;
            log.error("删除学生操作失败，传入参数ID={}，失败原因：{}", id, e.getMessage());
        }
        return flag;
    }

    @Override
    public Long countByDepart(Depart depart) {
        return studentRepository.countByDepart(depart);
    }

    @Override
    @Transactional
    public Student save(StudentRequestForm student) {


        Student saveOrUpdateStudent;

        Date createOrUpdateTime = new Date();
        if (null != student.getId()) {
            saveOrUpdateStudent = studentRepository.findOne(student.getId());
            saveOrUpdateStudent.setUpdateTime(createOrUpdateTime);
        } else {
            saveOrUpdateStudent = new Student();
            saveOrUpdateStudent.setCreateTime(createOrUpdateTime);
            saveOrUpdateStudent.setStatus(TransmitEnum.TRANSIENT);
        }
        // TODO : 重构Student实体，对允许为空属性使用Optional容器包裹
        saveOrUpdateStudent.setExamineeNo(student.getExamineeNo());
        saveOrUpdateStudent.setAdmissionNo(student.getAdmissionNo());
        saveOrUpdateStudent.setStudentNo(student.getStudentNo());
        saveOrUpdateStudent.setName(student.getName());
        saveOrUpdateStudent.setSex(student.getSex());
        saveOrUpdateStudent.setNationality(student.getNationality());
        saveOrUpdateStudent.setIdCard(student.getIdCard());

        // 专业
        if (StringUtils.isNotBlank(student.getMajor())) {
            String majorName = student.getMajor().trim();
            Major major = this.majorRepository.findByName(majorName);
            if (major == null) {
                major = dictService.createMajor(majorName);
            }
            saveOrUpdateStudent.setMajor(major);
        }


        // 院系必填
        if (StringUtils.isNotBlank(student.getDepart())) {
            String departName = student.getDepart().trim();

            Depart depart = this.departRepository.findByName(departName);
            if (depart == null) {
                depart = dictService.createDepart(departName);

            }
            saveOrUpdateStudent.setDepart(depart);
        }

        // 班级
        if (StringUtils.isNotBlank(student.getAdClass())) {
            String adClassName = student.getAdClass().trim();
            AdClass adClass = this.adClassRepository.findByName(adClassName);
            if (adClass == null) {
                adClass = dictService.createAdClass(adClassName);
            }
            saveOrUpdateStudent.setAdClass(adClass);
        }

        saveOrUpdateStudent.setTrackNo(student.getTrackNo());
        saveOrUpdateStudent.setArchiveGonePlace(student.getArchiveGonePlace());
        saveOrUpdateStudent.setArchiveNo(student.getArchiveNo());
        saveOrUpdateStudent.setSourceRegion(student.getSourceRegion());
        saveOrUpdateStudent.setReceiveUnit(student.getReceiveUnit());

        saveOrUpdateStudent.setEntranceYear(student.getEntranceYear());
        saveOrUpdateStudent.setFamilyAddress(student.getFamilyAddress());
        saveOrUpdateStudent.setPostCode(student.getPostCode());
        saveOrUpdateStudent.setLinkPerson(student.getLinkPerson());
        saveOrUpdateStudent.setPrimaryPhone(student.getPrimaryPhone());
        saveOrUpdateStudent.setBackupPhone(student.getBackupPhone());
        saveOrUpdateStudent.setSources(Constants.ADD_SOURCE);

        return studentRepository.save(saveOrUpdateStudent);
    }

    @Override
    public Student findOne(Long id) {
        return studentRepository.findOne(id);
    }


    @Override
    public Student getStudentArchiveByKeywords(String keywords) {

        Student student = this.studentRepository.findByExamineeNo(keywords);

        if (null != student) {
            return student;
        } else {
            student = this.studentRepository.findByStudentNo(keywords);
        }

        // 兼容外网学生档案查询
        // 查询条件: 身份证号码+学号
        if (student == null) {
            String idCard = StringUtils.substring(keywords, 0, keywords.length() - Constants.NJXZC_BARCODE_RULES);
            log.info("解析身份证号码:{}", idCard);
            // TODO: 身份证的合法性校验无意义
            if (IdcardUtil.isValidCard(idCard)) {
                String studentNo = keywords.substring(idCard.length());
                student = this.studentRepository.findByIdCardAndStudentNo(idCard, studentNo);
            } else {
                // 查询规则: 将字符串末尾八位作为学号字段
                student = this.getStudentInfoBarcode(keywords);
            }
        }
        return student;
    }

    /**
     * 晓庄条码规则:
     * 在校生含新生:: 入学年份 + 学校代码 + 学号
     *
     * @param searchParam
     * @return
     */
    @Override
    public Student getStudentInfoBarcode(String searchParam) {
        String studentNo = Strman.last(searchParam, Constants.NJXZC_BARCODE_RULES);
        log.info("扫描学号 = {} ", studentNo);
        return studentRepository.findByStudentNo(studentNo);
    }

    @Override
    public Student getStudentInfo(String searchParam) {

        Student student = this.studentRepository.findByExamineeNo(searchParam);
        if (null != student) {
            return student;
        } else {
            student = this.studentRepository.findByIdCard(searchParam);
            if (null != student) {
                return student;
            } else {
                Iterable<Student> students = this.studentRepository.findByNameLike(searchParam);

                if (students.iterator().hasNext()) {
                    return students.iterator().next();
                } else {
                    students = this.studentRepository.findByStudentNoLike(searchParam);
                    return students.iterator().hasNext() ? students.iterator().next() : null;
                }
            }
        }
    }

    @Override
    public Page<Student> getStudentPages(Map<String, Object> searchParams, Paging paging) {
        // 限定查询排序规则
        PageRequest pageRequest = PaginationUtil.buildPageRequest(paging.getPageNum(), paging.getPageSize());
        return studentRepository.findAll((Root<Student> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            List<Predicate> orPredicate = new ArrayList<>();
            // 性别
            if (null != searchParams.get("gender") && String.valueOf(searchParams.get("gender")).length() > 0) {
                predicates.add(criteriaBuilder.equal(root.get("sex"), SexEnum.valueOf(String.valueOf(searchParams.get("gender")))));
            }
            // 院系
            if (null != searchParams.get("depart") && String.valueOf(searchParams.get("depart")).length() > 0) {
                predicates.add(criteriaBuilder.equal(root.get("depart").get("code"), String.valueOf(searchParams.get("depart"))));
            }
            // 录取年份
            if (null != searchParams.get("entranceYear") && String.valueOf(searchParams.get("entranceYear")).length() > 0) {
                predicates.add(criteriaBuilder.equal(root.get("entranceYear"), Integer.parseInt(String.valueOf(searchParams.get("entranceYear")))));
            }
            // 档案状态
            if (null != searchParams.get("status") && String.valueOf(searchParams.get("status")).length() > 0) {
                predicates.add(criteriaBuilder.equal(root.get("status"), TransmitEnum.valueOf(String.valueOf(searchParams.get("status")))));
            }
            // 模糊匹配
            if (null != searchParams.get("sno") && String.valueOf(searchParams.get("sno")).length() > 0) {
                StringJoiner joiner = new StringJoiner("");
                joiner.add("%").add(String.valueOf(searchParams.get("sno"))).add("%");
                // 带分页条件查询
                orPredicate.add(criteriaBuilder.equal(root.get("examineeNo"), String.valueOf(searchParams.get("sno"))));
                orPredicate.add(criteriaBuilder.equal(root.get("studentNo"), String.valueOf(searchParams.get("sno"))));
                orPredicate.add(criteriaBuilder.like(root.get("name"), joiner.toString()));
                orPredicate.add(criteriaBuilder.like(root.get("remarks"), joiner.toString()));
                predicates.add(criteriaBuilder.or(orPredicate.toArray(new Predicate[]{})));
            }
            return predicates.size() > 0 ? PaginationUtil.buildQueryPredicate(predicates, criteriaBuilder) : null;
        }, pageRequest);
    }


    @Override
    @Transactional
    public LogImpExcel importStudent(List<StudentXlsDto> list) {

        LogImpExcel logImpExcel = new LogImpExcel();

        if (null != list && list.size() > 0) {

            Date createOrUpdateTime = new Date();

            list.forEach(s -> {
                Student student = new Student();
                if (StringUtils.isNotBlank(s.getKsh()) && studentRepository.countByExamineeNo(s.getKsh()) > 0) {
                    student = studentRepository.findByExamineeNo(s.getKsh());
                }

                if (StringUtils.isNotBlank(s.getXh()) && studentRepository.countByStudentNo(s.getXh()) > 0) {
                    student = studentRepository.findByStudentNo(s.getXh());
                }

                student.setUpdateTime(createOrUpdateTime);

                if (student.getId() == null) {
                    student.setCreateTime(createOrUpdateTime);
                    student.setStatus(TransmitEnum.TRANSIENT);
                    student.setSources(Constants.IMPORT_SOURCE);
                }

                if (StringUtils.isNotBlank(s.getKsh())) {
                    student.setExamineeNo(s.getKsh());
                }
                if (StringUtils.isNotBlank(s.getLqh())) {
                    student.setAdmissionNo(s.getLqh());
                }
                if (StringUtils.isNotBlank(s.getXh())) {
                    student.setStudentNo(s.getXh());
                }
                if (StringUtils.isNotBlank(s.getXm())) {
                    student.setName(s.getXm());
                }
                if (StringUtils.isNotBlank(s.getXb())) {
                    student.setSex(SexEnum.findByName(s.getXb()));
                }

                if (StringUtils.isNotBlank(s.getMz())) {
                    if (null != NationalityEnum.findByName(s.getMz())) {
                        student.setNationality(NationalityEnum.findByName(s.getMz()));
                    }
                }

                if (StringUtils.isNotBlank(s.getSfzh())) {
                    student.setIdCard(s.getSfzh().trim());
                }

                if (StringUtils.isNotBlank(s.getZy())) {
                    Major major = majorRepository.findByName(s.getZy().trim());
                    if (null != major) {
                        student.setMajor(major);
                    } else {
                        student.setMajor(dictService.createMajor(s.getZy().trim()));
                    }
                }

                if (StringUtils.isNotBlank(s.getYx())) {
                    Depart depart = departRepository.findByName(s.getYx().trim());
                    if (null != depart) {
                        student.setDepart(depart);
                    } else {
                        student.setDepart(dictService.createDepart(s.getYx().trim()));
                    }
                }


                if (StringUtils.isNotBlank(s.getBj())) {
                    AdClass adClass = adClassRepository.findByName(s.getBj().trim());
                    if (null != adClass) {
                        student.setAdClass(adClass);
                    } else {
                        student.setAdClass(dictService.createAdClass(s.getBj().trim()));
                    }
                }


                if (StringUtils.isNotBlank(s.getLqnf())) {
                    student.setEntranceYear(Integer.parseInt(s.getLqnf()));
                }

                if (StringUtils.isNotBlank(s.getTxdz())) {
                    student.setFamilyAddress(s.getTxdz());
                }

                if (StringUtils.isNotBlank(s.getYzbm())) {
                    student.setPostCode(s.getYzbm().trim());
                }

                if (StringUtils.isNotBlank(s.getLxr())) {
                    student.setLinkPerson(s.getLxr());
                }

                if (StringUtils.isNotBlank(s.getLxdh1())) {
                    student.setPrimaryPhone(s.getLxdh1());
                }

                if (StringUtils.isNotBlank(s.getLxdh2())) {
                    student.setBackupPhone(s.getLxdh2());
                }

                // 离校档案补充属性

                if (StringUtils.isNotBlank(s.getDah())) {
                    student.setArchiveNo(s.getDah());
                }

                if (StringUtils.isNotBlank(s.getSydq())) {
                    student.setSourceRegion(s.getSydq());
                }

                if (StringUtils.isNotBlank(s.getDaqx())) {
                    student.setArchiveGonePlace(s.getDaqx());
                }
                if (StringUtils.isNotBlank(s.getJsdw())) {
                    student.setReceiveUnit(s.getJsdw());
                }

                if (StringUtils.isNotBlank(s.getYdh())) {
                    student.setTrackNo(s.getYdh());
                }

                if (StringUtils.isNoneBlank(s.getRemarks())) {
                    student.setRemarks(s.getRemarks());
                }

                studentRepository.save(student);
            });
        }

        return logImpExcel;
    }
}
