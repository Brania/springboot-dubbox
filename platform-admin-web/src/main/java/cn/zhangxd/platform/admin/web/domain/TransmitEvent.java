/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.domain;

import cn.zhangxd.platform.admin.web.util.IdEntity;
import com.google.common.collect.Sets;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 上午10:37
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * 转接事由
 */
@Entity
@Table(name = "xz_transmit_event")
public class TransmitEvent extends IdEntity {

    private String name;
    private Boolean enabled;
    private Set<TransmitEventType> eventTypes = Sets.newHashSet();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @OneToMany(mappedBy = "transmitEvent")
    public Set<TransmitEventType> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(Set<TransmitEventType> eventTypes) {
        this.eventTypes = eventTypes;
    }
}
