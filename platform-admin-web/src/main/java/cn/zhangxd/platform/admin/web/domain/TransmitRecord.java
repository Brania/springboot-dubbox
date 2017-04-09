/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.domain;

import cn.zhangxd.platform.admin.web.util.IdEntity;
import javax.persistence.*;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 上午10:58
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description: 在档案接收时，可设置档案存放地点和保管人
 */
@Entity
@Table(name = "xz_transmit_record")
public class TransmitRecord extends IdEntity {

    /**
     * 档案转接类型
     */
    private TransmitEventType transmitEventType;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 转接院系机构
     */
    private Depart depart;

    /**
     * 档案主体
     */
    private Student student;

    /**
     * 转接人（系统判断转接人字段为空则该记录待操作）
     */
    private String transitUserId;

    @ManyToOne
    @JoinColumn(name = "event_type_id", referencedColumnName = "id")
    public TransmitEventType getTransmitEventType() {
        return transmitEventType;
    }

    public void setTransmitEventType(TransmitEventType transmitEventType) {
        this.transmitEventType = transmitEventType;
    }

    @Column(name = "remarks")
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @ManyToOne
    @JoinColumn(name = "depart_id", referencedColumnName = "id")
    public Depart getDepart() {
        return depart;
    }

    public void setDepart(Depart depart) {
        this.depart = depart;
    }

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Column(name = "transit_user_id")
    public String getTransitUserId() {
        return transitUserId;
    }

    public void setTransitUserId(String transitUserId) {
        this.transitUserId = transitUserId;
    }
}
