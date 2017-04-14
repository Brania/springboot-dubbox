/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.service.impl;

import cn.zhangxd.platform.admin.web.domain.*;
import cn.zhangxd.platform.admin.web.domain.common.LogImpExcel;
import cn.zhangxd.platform.admin.web.domain.dto.StudentXlsDto;
import cn.zhangxd.platform.admin.web.enums.NationalityEnum;
import cn.zhangxd.platform.admin.web.enums.SexEnum;
import cn.zhangxd.platform.admin.web.enums.TransmitEnum;
import cn.zhangxd.platform.admin.web.repository.*;
import cn.zhangxd.platform.admin.web.service.DictService;
import cn.zhangxd.platform.admin.web.service.StudentService;
import cn.zhangxd.platform.admin.web.util.Constants;
import cn.zhangxd.platform.admin.web.util.PaginationUtil;
import cn.zhangxd.platform.common.api.Paging;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.*;

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
    public Student save(Student student) {

        Date createOrUpdateTime = new Date();

        if (null != student.getId()) {
            student.setUpdateTime(createOrUpdateTime);
        } else {
            student.setCreateTime(createOrUpdateTime);
        }
        // TODO : 重构Student实体，对允许为空属性使用Optional容器包裹
        student.setExamineeNo(student.getExamineeNo());
        student.setAdmissionNo(student.getAdmissionNo());
        student.setStudentNo(student.getStudentNo());
        student.setName(student.getName());
        student.setSex(student.getSex());
        student.setNationality(student.getNationality());
        student.setIdCard(student.getIdCard());

        // 专业
        if (null != student.getMajor()) {
            String majorName = student.getMajor().getName().trim();
            Major major = this.majorRepository.findByName(majorName);
            if (major == null) {
                major = dictService.createMajor(majorName);
            }
            student.setMajor(major);
        }


        // 院系必填
        if (null != student.getDepart()) {
            String departName = student.getDepart().getName().trim();

            Depart depart = this.departRepository.findByName(departName);
            if (depart == null) {
                depart = dictService.createDepart(departName);

            }
            student.setDepart(depart);
        }


        // 班级
        if (null != student.getAdClass()) {
            String adClassName = student.getAdClass().getName().trim();
            AdClass adClass = this.adClassRepository.findByName(adClassName);
            if (adClass == null) {
                adClass = dictService.createAdClass(adClassName);
            }
            student.setAdClass(adClass);
        }
        student.setAdClass(student.getAdClass());
        student.setEntranceYear(student.getEntranceYear());
        student.setFamilyAddress(student.getFamilyAddress());
        student.setPostCode(student.getPostCode());
        student.setLinkPerson(student.getLinkPerson());
        student.setPrimaryPhone(student.getPrimaryPhone());
        student.setBackupPhone(student.getBackupPhone());
        student.setSources(Constants.ADD_SOURCE);
        student.setStatus(TransmitEnum.TRANSIENT);
        return studentRepository.save(student);
    }

    @Override
    public Student findOne(Long id) {
        return studentRepository.findOne(id);
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
                }
            }
        }
        return null;
    }

    @Override
    public Page<Student> getStudentPages(Map<String, Object> searchParams, Paging paging) {


        String[] sortParams = PaginationUtil.buildSortableParam(String.valueOf(searchParams.get("orderBy")));

        paging.setOrderBy(sortParams[0]);
        paging.setOrderType(sortParams[1]);


        PageRequest pageRequest = PaginationUtil.buildPageRequest(paging.getPageNum(), paging.getPageSize(), paging.getOrderBy(), paging.getOrderType());

        return studentRepository.findAll((Root<Student> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            List<Predicate> orPredicate = new ArrayList<>();

            if (null != searchParams.get("gender") && String.valueOf(searchParams.get("gender")).length() > 0) {
                predicates.add(criteriaBuilder.equal(root.get("sex"), SexEnum.valueOf(String.valueOf(searchParams.get("gender")))));
            }

            if (null != searchParams.get("depart") && String.valueOf(searchParams.get("depart")).length() > 0) {
                predicates.add(criteriaBuilder.equal(root.get("depart").get("code"), String.valueOf(searchParams.get("depart"))));
            }

            if (null != searchParams.get("sno") && String.valueOf(searchParams.get("sno")).length() > 0) {
                StringJoiner joiner = new StringJoiner("");
                joiner.add("%").add(String.valueOf(searchParams.get("sno"))).add("%");

                orPredicate.add(criteriaBuilder.equal(root.get("examineeNo"), String.valueOf(searchParams.get("sno"))));
                orPredicate.add(criteriaBuilder.equal(root.get("studentNo"), String.valueOf(searchParams.get("sno"))));
                orPredicate.add(criteriaBuilder.like(root.get("name"), joiner.toString()));
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

                Student student = studentRepository.findByExamineeNo(s.getKsh());
                if (null != student) {
                    student.setUpdateTime(createOrUpdateTime);
                } else {
                    student = new Student();
                    student.setCreateTime(createOrUpdateTime);
                }
                student.setSources(Constants.IMPORT_SOURCE);
                student.setStatus(TransmitEnum.TRANSIENT);

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

                studentRepository.save(student);
            });
        }

        return logImpExcel;
    }
}
