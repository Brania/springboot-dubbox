/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.domain;

import cn.zhangxd.platform.admin.web.util.IdEntity;
import com.google.common.collect.Lists;

import javax.persistence.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午8:53
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
@Entity
@Table(name = "xz_archive_classify")
public class ArchiveClassify extends IdEntity {

    private String name;

    private List<ArchiveItem> items = Lists.newArrayList();

    @Column(name = "name", unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "classify", cascade = CascadeType.REMOVE)
    public List<ArchiveItem> getItems() {
        return items;
    }
    public void setItems(List<ArchiveItem> items) {
        this.items = items;
    }
}
