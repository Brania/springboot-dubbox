/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.service.impl;

import cn.zhangxd.platform.admin.web.domain.ArchiveClassify;
import cn.zhangxd.platform.admin.web.domain.ArchiveItem;
import cn.zhangxd.platform.admin.web.domain.Student;
import cn.zhangxd.platform.admin.web.domain.StudentRelArchiveItem;
import cn.zhangxd.platform.admin.web.domain.dto.ArchiveClassifyDto;
import cn.zhangxd.platform.admin.web.domain.dto.ArchiveDto;
import cn.zhangxd.platform.admin.web.domain.dto.ArchiveItemDto;
import cn.zhangxd.platform.admin.web.repository.ArchiveClassifyRepository;
import cn.zhangxd.platform.admin.web.repository.ArchiveItemRepository;
import cn.zhangxd.platform.admin.web.repository.StudentRelArchiveItemRepository;
import cn.zhangxd.platform.admin.web.service.ArchiveService;
import cn.zhangxd.platform.admin.web.service.StudentService;
import cn.zhangxd.platform.common.api.Paging;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
    private ArchiveClassifyRepository archiveClassifyRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRelArchiveItemRepository studentRelArchiveItemRepository;


    @Override
    public ArchiveClassify save(ArchiveClassify archiveClassify) {
        if (null != archiveClassify.getId()) {
            archiveClassify.setCreateTime(new Date());
        }
        return archiveClassifyRepository.save(archiveClassify);
    }

    @Override
    public ArchiveClassify findClassifyById(Long id) {
        return archiveClassifyRepository.findOne(id);
    }

    @Override
    public ArchiveItemDto findById(Long id) {
        return generate(archiveItemRepository.findOne(id));
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
    public ArchiveItemDto save(ArchiveItemDto dto) {
        ArchiveItem archiveItem;
        if (null != dto.getId()) {
            // 记录修改时间
            archiveItem = archiveItemRepository.findOne(dto.getId());
            archiveItem.setClassify(archiveClassifyRepository.findOne(dto.getClassifyId()));
        } else {
            archiveItem = create(dto);
        }
        return generate(archiveItemRepository.save(archiveItem));
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
    public List<ArchiveItemDto> findAll(Sort sort) {
        List<ArchiveItemDto> list = Lists.newArrayList();
        Long defaultValue = 0l;
        for (ArchiveItem archiveItem : archiveItemRepository.listAll()) {
            ArchiveItemDto archiveItemDto = generate(archiveItem);
            if (archiveItemDto.getClassifyId().compareTo(defaultValue) != 0) {
                archiveItemDto.setRowSpan(archiveItemRepository.countByClassify(archiveItem.getClassify()).intValue());
            }
            defaultValue = archiveItemDto.getClassifyId();
            list.add(archiveItemDto);
        }

        return list;
    }


    private ArchiveItemDto generate(ArchiveItem archiveItem) {
        ArchiveItemDto dto = new ArchiveItemDto();
        dto.setId(archiveItem.getId());

        dto.setClassifyId(archiveItem.getClassify().getId());
        dto.setClassifyName(archiveItem.getClassify().getName());
        dto.setEnabled(archiveItem.getEnabled());
        dto.setForced(archiveItem.getForced());
        dto.setName(archiveItem.getName());
        dto.setRemarks(archiveItem.getRemarks());
        dto.setSort(archiveItem.getSort());

        return dto;
    }


    @Override
    public List<ArchiveClassifyDto> findAllClassify(Sort sort) {

        List<ArchiveClassifyDto> list = Lists.newArrayList();
        archiveClassifyRepository.findAll(sort).forEach(archiveClassify -> {
            ArchiveClassifyDto dto = new ArchiveClassifyDto();

            dto.setId(archiveClassify.getId());
            dto.setName(archiveClassify.getName());
            dto.setItems(archiveClassify.getItems().stream().map(classify -> generate(classify)).collect(Collectors.toList()));
            list.add(dto);
        });


        return list;
    }

    private ArchiveItem create(ArchiveItemDto dto) {

        ArchiveItem item = new ArchiveItem();
        item.setEnabled(dto.getEnabled());
        item.setForced(dto.getForced());
        item.setName(dto.getName());
        item.setRemarks(dto.getRemarks());
        item.setSort(dto.getSort());
        item.setCreateTime(new Date());
        item.setClassify(archiveClassifyRepository.findOne(dto.getClassifyId()));
        return item;
    }
}
