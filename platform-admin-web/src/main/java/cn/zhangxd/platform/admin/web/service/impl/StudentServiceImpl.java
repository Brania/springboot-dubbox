/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.service.impl;

import cn.zhangxd.platform.admin.web.domain.AdClass;
import cn.zhangxd.platform.admin.web.domain.Depart;
import cn.zhangxd.platform.admin.web.domain.Major;
import cn.zhangxd.platform.admin.web.domain.Student;
import cn.zhangxd.platform.admin.web.domain.common.LogImpExcel;
import cn.zhangxd.platform.admin.web.domain.dto.StudentXlsDto;
import cn.zhangxd.platform.admin.web.enums.NationalityEnum;
import cn.zhangxd.platform.admin.web.enums.SexEnum;
import cn.zhangxd.platform.admin.web.repository.AdClassRepository;
import cn.zhangxd.platform.admin.web.repository.DepartRepository;
import cn.zhangxd.platform.admin.web.repository.MajorRepository;
import cn.zhangxd.platform.admin.web.repository.StudentRepository;
import cn.zhangxd.platform.admin.web.service.StudentService;
import cn.zhangxd.platform.admin.web.util.Constants;
import cn.zhangxd.platform.admin.web.util.sequence.Sequence;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

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
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DepartRepository departRepository;

    @Autowired
    private MajorRepository majorRepository;

    @Autowired
    private AdClassRepository adClassRepository;

    Sequence sequence = new Sequence(0, 0);


    @Override
    public Boolean save(Student student) {
        return null;
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
                        major = new Major();
                        major.setEnabled(Boolean.TRUE);
                        major.setName(s.getZy().trim());
                        StringJoiner sj = new StringJoiner("");
                        sj.add(Constants.MAJOR_CODE_PREFIX).add(sequence.nextSeq());
                        major.setCode(sj.toString());
                        major = majorRepository.save(major);
                        student.setMajor(major);
                    }
                }

                if (StringUtils.isNotBlank(s.getYx())) {
                    Depart depart = departRepository.findByName(s.getYx().trim());
                    if (null != depart) {
                        student.setDepart(depart);
                    } else {
                        depart = new Depart();
                        depart.setEnabled(Boolean.TRUE);
                        depart.setName(s.getYx().trim());
                        StringJoiner joiner = new StringJoiner("");
                        joiner.add(Constants.DEPART_CODE_PREFIX).add(sequence.nextSeq());
                        depart.setCode(joiner.toString());
                        depart = departRepository.save(depart);
                        student.setDepart(depart);
                    }
                }


                if (StringUtils.isNotBlank(s.getBj())) {
                    AdClass adClass = adClassRepository.findByName(s.getBj().trim());
                    if (null != adClass) {
                        student.setAdClass(adClass);
                    } else {
                        adClass = new AdClass();
                        adClass.setName(s.getBj().trim());
                        adClass.setCreateTime(createOrUpdateTime);
                        adClass.setEnabled(Boolean.TRUE);
                        StringJoiner stringJoiner = new StringJoiner("");
                        stringJoiner.add(Constants.ADCLASS_CODE_PREFIX).add(sequence.nextSeq());
                        adClass.setCode(stringJoiner.toString());
                        adClass = adClassRepository.save(adClass);
                        student.setAdClass(adClass);
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
