package cn.zhangxd.platform.admin.web.domain;

import cn.zhangxd.platform.admin.web.util.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author 陈辉[of2547]
 *         company qianmi.com
 *         Date    2018/3/11
 */
@Entity
@Table(name = "xz_transform")
public class Transform extends IdEntity {

    private String fromMemberId;
    private String toMemberId;
    private Boolean done;
    private String fromDepart;
    private String marks;

    @Column(name = "form_member_id")
    public String getFromMemberId() {
        return fromMemberId;
    }

    public void setFromMemberId(String fromMemberId) {
        this.fromMemberId = fromMemberId;
    }

    @Column(name = "to_member_id")
    public String getToMemberId() {
        return toMemberId;
    }

    public void setToMemberId(String toMemberId) {
        this.toMemberId = toMemberId;
    }

    @Column(name = "done")
    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }


    @Column(name = "from_depart")
    public String getFromDepart() {
        return fromDepart;
    }

    public void setFromDepart(String fromDepart) {
        this.fromDepart = fromDepart;
    }

    @Column(name = "marks")
    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }
}
