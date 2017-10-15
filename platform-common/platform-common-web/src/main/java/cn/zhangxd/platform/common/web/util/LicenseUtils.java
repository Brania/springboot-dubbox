package cn.zhangxd.platform.common.web.util;

import cn.zhangxd.platform.common.web.annotations.LicenseSubject;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * @author 陈辉[of2547]
 *         company qianmi.com
 *         Date    2017/10/15
 */
public class LicenseUtils {


    public static LicenseSubject check() {
        LicenseSubject subject = new LicenseSubject();
        Long onlineDays = ChronoUnit.DAYS.between(subject.getBuyTime(), LocalDate.now());
        subject.setOnlineDays(onlineDays);
        Long limitDays = subject.getLicenseDays() - onlineDays;
        subject.setLimitDays(limitDays);
        if (limitDays > 0) {
            if (onlineDays + subject.getWarnDays() > subject.getLimitDays()) {
                subject.setWarning(Boolean.TRUE);
            }
        } else {
            subject.setError(Boolean.TRUE);
        }


        return subject;
    }


}
