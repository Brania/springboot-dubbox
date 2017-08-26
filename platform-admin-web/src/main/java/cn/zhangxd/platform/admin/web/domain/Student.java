/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.domain;

import cn.zhangxd.platform.admin.web.enums.NationalityEnum;
import cn.zhangxd.platform.admin.web.enums.SexEnum;
import cn.zhangxd.platform.admin.web.enums.TransmitEnum;
import cn.zhangxd.platform.admin.web.util.IdEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.google.common.collect.Sets;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午2:56
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */

@Entity
@Table(name = "xz_student")
public class Student extends IdEntity {

    /**
     * 考生号
     */
    private String examineeNo;
    /**
     * 录取号
     */
    private String admissionNo;
    /**
     * 学号
     */
    private String studentNo;

    /**
     * 姓名
     */
    private String name;
    /**
     * 性别
     */
    private SexEnum sex;
    /**
     * 民族
     */
    private NationalityEnum nationality;
    /**
     * 身份证号码
     */
    private String idCard;
    /**
     * 专业
     */
    private Major major;
    /**
     * 院系（正式）
     */
    private Depart depart;
    /**
     * 异动院系（转出写入:转入覆盖正式院系）
     */
    private Depart rollDepart;
    /**
     * 班级
     */
    private AdClass adClass;
    /**
     * 录取年份
     */
    private Integer entranceYear;
    /**
     * 家庭地址->档案邮寄通信地址
     */
    private String familyAddress;
    /**
     * 邮编
     */
    private String postCode;
    /**
     * 联系人->收件人
     */
    private String linkPerson;
    /**
     * 联系电话->收件人联系电话1
     */
    private String primaryPhone;
    /**
     * 备用联系电话->收件人联系电话2
     */
    private String backupPhone;
    /**
     * 数据来源：IMPORT/ADD
     */
    private String sources;

    /**
     * 档案号-add
     */
    private String archiveNo;
    /**
     * 生源地区-add
     */
    private String sourceRegion;
    /**
     * 档案去向-add
     */
    private String archiveGonePlace;
    /**
     * 档案接收单位-add
     */
    private String receiveUnit;
    /**
     * 运单号-add
     */
    private String trackNo;


    /**
     * 更新时间
     */
    private Date updateTime;

    private Set<TransmitRecord> records = Sets.newHashSet();

    private TransmitEnum status;

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "examinee_no", unique = true)
    public String getExamineeNo() {
        return examineeNo;
    }

    public void setExamineeNo(String examineeNo) {
        this.examineeNo = examineeNo;
    }

    @Column(name = "admission_no", unique = true)
    public String getAdmissionNo() {
        return admissionNo;
    }

    public void setAdmissionNo(String admissionNo) {
        this.admissionNo = admissionNo;
    }

    @Column(name = "student_no", unique = true)
    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "sex")
    public SexEnum getSex() {
        return sex;
    }

    public void setSex(SexEnum sex) {
        this.sex = sex;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "nationality")
    public NationalityEnum getNationality() {
        return nationality;
    }

    public void setNationality(NationalityEnum nationality) {
        this.nationality = nationality;
    }

    @Column(name = "id_card")
    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    @ManyToOne
    @JoinColumn(name = "major_id", referencedColumnName = "id")
    public Major getMajor() {
        return major;
    }

    public void setMajor(Major major) {
        this.major = major;
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
    @JoinColumn(name = "roll_depart_id", referencedColumnName = "id")
    public Depart getRollDepart() {
        return rollDepart;
    }

    public void setRollDepart(Depart rollDepart) {
        this.rollDepart = rollDepart;
    }

    @Column(name = "entrance_year")
    public Integer getEntranceYear() {
        return entranceYear;
    }

    public void setEntranceYear(Integer entranceYear) {
        this.entranceYear = entranceYear;
    }

    @Column(name = "family_address")
    public String getFamilyAddress() {
        return familyAddress;
    }

    public void setFamilyAddress(String familyAddress) {
        this.familyAddress = familyAddress;
    }

    @Column(name = "post_code")
    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    @Column(name = "link_person")
    public String getLinkPerson() {
        return linkPerson;
    }

    public void setLinkPerson(String linkPerson) {
        this.linkPerson = linkPerson;
    }

    @Column(name = "primary_phone")
    public String getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    @Column(name = "backup_phone")
    public String getBackupPhone() {
        return backupPhone;
    }

    public void setBackupPhone(String backupPhone) {
        this.backupPhone = backupPhone;
    }

    @ManyToOne
    @JoinColumn(name = "adclass_id", referencedColumnName = "id")
    public AdClass getAdClass() {
        return adClass;
    }

    public void setAdClass(AdClass adClass) {
        this.adClass = adClass;
    }

    @Column(name = "sources")
    public String getSources() {
        return sources;
    }

    public void setSources(String sources) {
        this.sources = sources;
    }


    @Enumerated(EnumType.STRING)
    @NotNull(message = "档案状态不能为空")
    @Column(name = "status")
    public TransmitEnum getStatus() {
        return status;
    }

    public void setStatus(TransmitEnum status) {
        this.status = status;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @OneToMany(mappedBy = "student")
    @JsonBackReference
    public Set<TransmitRecord> getRecords() {
        return records;
    }

    public void setRecords(Set<TransmitRecord> records) {
        this.records = records;
    }


    @Column(name = "archive_no")
    public String getArchiveNo() {
        return archiveNo;
    }

    public void setArchiveNo(String archiveNo) {
        this.archiveNo = archiveNo;
    }

    @Column(name = "source_region")
    public String getSourceRegion() {
        return sourceRegion;
    }

    public void setSourceRegion(String sourceRegion) {
        this.sourceRegion = sourceRegion;
    }

    @Column(name = "archive_gone_place")
    public String getArchiveGonePlace() {
        return archiveGonePlace;
    }

    public void setArchiveGonePlace(String archiveGonePlace) {
        this.archiveGonePlace = archiveGonePlace;
    }

    @Column(name = "receive_unit")
    public String getReceiveUnit() {
        return receiveUnit;
    }

    public void setReceiveUnit(String receiveUnit) {
        this.receiveUnit = receiveUnit;
    }

    @Column(name = "track_no")
    public String getTrackNo() {
        return trackNo;
    }

    public void setTrackNo(String trackNo) {
        this.trackNo = trackNo;
    }


/**
 * 转接记录按照时间降序排序,第一条记录作为状态值
 *
 * @return
 */
//    public TransmitRecord findLastestRecord() {
//        Stream<TransmitRecord> stream = records.stream().sorted((v1, v2) -> v2.getLocalDateTime().isAfter(v1.getLocalDateTime()) ? 0 : 1);
//        return stream.findFirst().get();
//    }
}
