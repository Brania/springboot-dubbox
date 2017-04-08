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

    /**
     * 分页查询学生列表
     *
     * @param searchParams
     * @param paging
     * @return
     */
    Page<Student> getStudentPages(final Map<String, Object> searchParams, Paging paging);

    /**
     * 导入学生名单
     *
     * @param list
     * @return
     */
    LogImpExcel importStudent(List<StudentXlsDto> list);


    Long countByDepart(Depart depart);


}
