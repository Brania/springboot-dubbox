/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.service;

import cn.zhangxd.platform.admin.web.domain.Depart;
import cn.zhangxd.platform.admin.web.domain.Student;
import cn.zhangxd.platform.admin.web.domain.StudentRelArchiveItem;
import cn.zhangxd.platform.admin.web.domain.common.LogImpExcel;
import cn.zhangxd.platform.admin.web.domain.dto.StudentXlsDto;
import cn.zhangxd.platform.common.api.Paging;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午8:11
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
public interface StudentService {

    Iterable<Student> findAll();


    Student save(Student student);


    Student findOne(Long id);

    Boolean deleteById(Long id);

    /**
     * 分页查询学生列表
     *
     * @param searchParams
     * @param paging
     * @return
     */
    Page<Student> getStudentPages(final Map<String, Object> searchParams, Paging paging);

    /**
     * 根据多条件查询单个学生实体
     *
     * @param searchParam
     * @return
     */
    Student getStudentInfo(String searchParam);

    /**
     * 根据查询条件批量转接办理（暂时仅支持学院）
     *
     * @param searchParams
     * @return
     */
    List<Student> findStudentBySearchParam(Map<String, Object> searchParams);

    /**
     * 导入学生名单
     *
     * @param list
     * @return
     */
    LogImpExcel importStudent(List<StudentXlsDto> list);


    Long countByDepart(Depart depart);

    /**
     * 查找学生档案项目列表
     *
     * @param student
     * @return
     */
    List<StudentRelArchiveItem> findArchiveItemByStudent(Student student);

    /**
     * 删除学生档案项目
     * @param student
     * @param itemId
     * @return
     */
    Map<String, Object> deleteStudentAttachById(Student student, Long itemId);


}
