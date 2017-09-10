/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 . All rights reserved.
 *          刀光剑影不是我门派，
 *          天空海阔自有我风采。
 *          双手一推非黑也非白，
 *          不好也不坏，没有胜又何来败。
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.common;

import cn.zhangxd.platform.admin.web.domain.Student;

import java.util.Comparator;

/**
 * 导出排序规则:学院 -> 专业 -> 班级 -> 学号 -> 档案号
 *
 * @author 陈辉[of2547]
 *         company qianmi.com
 *         Date    2017/9/10
 */
public class StudentComparator implements Comparator<Student> {


    @Override
    public int compare(Student o1, Student o2) {


        int capResult = 0;


        if (o1.getDepart() != null && o2.getDepart() != null) {


            int departCap = o1.getDepart().getCode().compareToIgnoreCase(o2.getDepart().getCode());
            if (departCap != 0) {

                if (o1.getMajor() != null && o2.getMajor() != null) {

                    int majorCap = o1.getMajor().getCode().compareToIgnoreCase(o2.getMajor().getCode());
                    if (majorCap != 0) {


                        if (o1.getAdClass() != null && o2.getAdClass() != null) {
                            int adClassCap = o1.getAdClass().getCode().compareToIgnoreCase(o2.getAdClass().getCode());
                            if (adClassCap != 0) {

                                if (o1.getStudentNo().compareTo(o2.getStudentNo()) == 0) {
                                    return o1.getArchiveNo().compareTo(o2.getArchiveNo());

                                } else {
                                    return o1.getStudentNo().compareTo(o2.getStudentNo());
                                }


                            } else {
                                capResult = adClassCap;
                            }
                        }

                    } else {
                        capResult = majorCap;
                    }
                }


            } else {
                capResult = departCap;
            }

        }


        return capResult;
    }


}
