/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.service.impl;

import cn.zhangxd.platform.admin.web.domain.ArchiveItem;
import cn.zhangxd.platform.admin.web.domain.Student;
import cn.zhangxd.platform.admin.web.domain.StudentRelArchiveItem;
import cn.zhangxd.platform.admin.web.domain.dto.ArchiveDto;
import cn.zhangxd.platform.admin.web.repository.ArchiveItemRepository;
import cn.zhangxd.platform.admin.web.repository.StudentRelArchiveItemRepository;
import cn.zhangxd.platform.admin.web.service.ArchiveService;
import cn.zhangxd.platform.admin.web.service.StudentService;
import cn.zhangxd.platform.common.api.Paging;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午4:49
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
@Service
@Slf4j
public class ArchiveServiceImpl implements ArchiveService {

    @Autowired
    private ArchiveItemRepository archiveItemRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRelArchiveItemRepository studentRelArchiveItemRepository;


    @Override
    public ArchiveItem findById(Long id) {
        return archiveItemRepository.findOne(id);
    }

    @Override
    public Map<String, Object> attachStuArchive(Stream<String> students, Long archiveId) {

        Map<String, Object> results = Maps.newHashMap();

        Boolean success = Boolean.FALSE;
        try {
            ArchiveItem archiveItem = archiveItemRepository.findOne(archiveId);
            if (null != archiveItem) {
                students.forEach(s -> {
                    Student student = studentService.findOne(Long.parseLong(s));
                    StudentRelArchiveItem sra = this.studentRelArchiveItemRepository.findByStudentAndItem(student, archiveItem);
                    if (sra == null) {
                        sra = new StudentRelArchiveItem();
                    }
                    sra.setItem(archiveItem);
                    sra.setStudent(student);
                    sra.setCreateTime(new Date());
                    studentRelArchiveItemRepository.save(sra);
                });
                success = Boolean.TRUE;
            }
        } catch (Exception e) {
            log.error("保存学生档案项目失败：{}", e.getMessage());
        }

        results.put("success", success);
        return results;
    }

    @Override
    public Map<String, Object> deleteStuArchive(Stream<String> students, Long archiveId) {
        return null;
    }


    @Override
    public Page<ArchiveDto> reportPageList(Map<String, Object> searchParams, Paging paging) {

        Page<Student> students = studentService.getStudentPages(searchParams, paging);
        return students.map(s -> generate(s));
    }


    private ArchiveDto generate(Student student) {

        ArchiveDto dto = new ArchiveDto();
        dto.setId(student.getId());
        dto.setAdmissionNo(student.getAdmissionNo());
        dto.setExamineeNo(student.getExamineeNo());
        dto.setStudentNo(student.getStudentNo());
        dto.setDepart(student.getDepart());
        dto.setEntranceYear(student.getEntranceYear());
        dto.setName(student.getName());
        dto.setTotalArchiveCount(archiveItemRepository.countByForcedAndEnabled(Boolean.TRUE, Boolean.TRUE));
        dto.setFilledCount(studentRelArchiveItemRepository.countByStudent(student));

        return dto;
    }

    @Override
    public ArchiveItem save(ArchiveItem archiveItem) {

        if (null != archiveItem.getId()) {
            // 记录修改时间
        } else {
            archiveItem = generate(archiveItem);
        }
        return archiveItemRepository.save(archiveItem);
    }

    @Override
    public Map<String, Object> deleteArchiveItem(Long id) {

        Boolean success = Boolean.TRUE;
        Map<String, Object> results = Maps.newHashMap();
        try {
            archiveItemRepository.delete(id);
        } catch (Exception e) {
            success = Boolean.FALSE;
            log.error("删除档案项目失败：{}", e.getMessage());
        }
        results.put("success", success);
        return results;
    }

    @Override
    public Iterable<ArchiveItem> findAll(Sort sort) {

        return archiveItemRepository.findAll(sort);
    }


    private ArchiveItem generate(ArchiveItem archiveItem) {

        ArchiveItem item = new ArchiveItem();
        item.setEnabled(archiveItem.getEnabled());
        item.setForced(archiveItem.getForced());
        item.setName(archiveItem.getName());
        item.setRemarks(archiveItem.getRemarks());
        item.setSort(archiveItem.getSort());
        return item;
    }
}
