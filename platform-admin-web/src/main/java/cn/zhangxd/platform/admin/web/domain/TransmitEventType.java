/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.domain;

import cn.zhangxd.platform.admin.web.enums.TransmitEnum;
import cn.zhangxd.platform.admin.web.util.IdEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 上午10:41
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
@Entity
@Table(name = "xz_transmit_event_type")
public class TransmitEventType extends IdEntity {

    private String name;
    private TransmitEvent transmitEvent;
    private Boolean enabled;
    private Integer sort;
    private TransmitEnum nextStatus;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "event_id", referencedColumnName = "id")
    public TransmitEvent getTransmitEvent() {
        return transmitEvent;
    }

    public void setTransmitEvent(TransmitEvent transmitEvent) {
        this.transmitEvent = transmitEvent;
    }


    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "next_status")
    public TransmitEnum getNextStatus() {
        return nextStatus;
    }

    public void setNextStatus(TransmitEnum nextStatus) {
        this.nextStatus = nextStatus;
    }
}
