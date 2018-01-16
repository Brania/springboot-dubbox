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
import cn.zhangxd.platform.admin.web.domain.common.ArchiveStat;
import cn.zhangxd.platform.admin.web.domain.common.LogImpExcel;
import cn.zhangxd.platform.admin.web.domain.dto.StudentDetailDto;
import cn.zhangxd.platform.admin.web.domain.dto.StudentRequestForm;
import cn.zhangxd.platform.admin.web.domain.dto.StudentXlsDto;
import cn.zhangxd.platform.admin.web.enums.TransmitEnum;
import cn.zhangxd.platform.common.api.Paging;
import org.springframework.data.domain.Page;

import java.util.Collection;
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

    /**
     * 指定勾选记录导出学生条码
     *
     * @param ids
     * @return
     */
    List<Student> reportChooseStudent(Collection<Long> ids);

    /**
     * 按照查询条件导出学生条件
     *
     * @param searchParams
     * @return
     */
    List<Student> reportStudentBySearchMap(Map<String, String> searchParams);


    Student save(StudentRequestForm student);


    Student findOne(Long id);

    /**
     * 包含转接记录的学生详情
     *
     * @param id
     * @return
     */
    StudentDetailDto findStudentDetail(Long id);

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
     * 扫描条码查询单个学生实体
     *
     * @param searchParam
     * @return
     */
    Student getStudentInfoBarcode(String searchParam);

    /**
     * 查询学生档案
     *
     * @param keywords
     * @return
     */
    Student getStudentArchiveByKeywords(String keywords);

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
     *
     * @param student
     * @param itemId
     * @return
     */
    Map<String, Object> deleteStudentAttachById(Student student, Long itemId);

    /**
     * TODO:添加年份
     *
     * @param status
     * @return
     */
    List<Student> findByStatusIn(List<TransmitEnum> status);

    /**
     * 按照院系统计档案数
     *
     * @param depart
     * @return
     */
    Long countArchiveByDepart(Depart depart);

    /**
     * 统计全部档案数::TODO 添加年份
     *
     * @return
     */
    Long countTotalArchive();


    List<Student> findByDepartAndStatusIn(Depart depart, List<TransmitEnum> status);

    List<Student> findByRollDepartAndStatus(Depart depart, TransmitEnum status);

    /**
     * 统计当前年份各院系不同状态的档案数量
     *
     * @return
     */
    List<ArchiveStat> statisticsStudentsGroupByDepart(Integer acaYear);

    /**
     * 查询学生档案转接次数
     *
     * @param id
     * @return
     */
    Integer countStudentTransmitTimes(Long id);


}
