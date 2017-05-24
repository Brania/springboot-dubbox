/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.domain;

import cn.zhangxd.platform.admin.web.util.IdEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

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
     * 转接操作人ID
     */
    private String opUserId;

    /**
     * 转接操作人登录账号
     */
    private String opUserName;

    /**
     * 转接保管人
     */
    private String custodian;

    /**
     * 转接操作时间
     */
    //private LocalDateTime localDateTime = LocalDateTime.now();
    private Date fluctTime;


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
    @JsonBackReference
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Column(name = "op_user_id")
    public String getOpUserId() {
        return opUserId;
    }

    public void setOpUserId(String opUserId) {
        this.opUserId = opUserId;
    }

    @Column(name = "op_user_name")
    public String getOpUserName() {
        return opUserName;
    }

    public void setOpUserName(String opUserName) {
        this.opUserName = opUserName;
    }

//    @Column(name = "fluct_time")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    public LocalDateTime getLocalDateTime() {
//        return localDateTime;
//    }
//
//    public void setLocalDateTime(LocalDateTime localDateTime) {
//        this.localDateTime = localDateTime;
//    }

    @Column(name = "fluct_time")
    public Date getFluctTime() {
        return fluctTime;
    }

    public void setFluctTime(Date fluctTime) {
        this.fluctTime = fluctTime;
    }

    @Column(name = "custodian")
    public String getCustodian() {
        return custodian;
    }

    public void setCustodian(String custodian) {
        this.custodian = custodian;
    }

}
