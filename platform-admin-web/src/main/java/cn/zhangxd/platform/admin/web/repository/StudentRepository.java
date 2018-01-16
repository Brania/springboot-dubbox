/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.repository;

import cn.zhangxd.platform.admin.web.domain.Depart;
import cn.zhangxd.platform.admin.web.domain.Student;
import cn.zhangxd.platform.admin.web.domain.common.ArchiveStat;
import cn.zhangxd.platform.admin.web.enums.TransmitEnum;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午8:09
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
public interface StudentRepository extends PagingAndSortingRepository<Student, Long>, JpaSpecificationExecutor<Student> {

    Student findByExamineeNo(String examineeNo);

    Long countByExamineeNo(String examineeNo);

    Student findByStudentNo(String sno);

    Long countByStudentNo(String no);

    List<Student> findByIdIn(Collection<Long> ids);

    List<Student> findByStatusIn(Collection<TransmitEnum> status);

    List<Student> findByDepartAndStatusIn(Depart depart, Collection<TransmitEnum> status);

    List<Student> findByRollDepartAndStatus(Depart depart, TransmitEnum status);

    /**
     * 统计院系学生人数
     *
     * @param depart
     * @return
     */
    Long countByDepart(Depart depart);

    Student findByIdCard(String idCard);

    Iterable<Student> findByNameLike(String name);

    Iterable<Student> findByStudentNoLike(String sno);

    @Query("select s from Student s where s.depart.code = ?1")
    List<Student> findStudentListBySearch(String departName);

    @Query("select s.depart.code as code,count(s.id) as totalAmount from Student s where s.entranceYear=?1 group by s.depart.code")
    List<Map<String, Object>> statStudentsGroupByDepart(Integer year);


}



