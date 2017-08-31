package cn.zhangxd.platform.admin.web.controller;

import cn.zhangxd.platform.admin.web.common.controller.BaseController;
import cn.zhangxd.platform.admin.web.domain.common.License;
import cn.zhangxd.platform.admin.web.service.DashboardService;
import cn.zhangxd.platform.admin.web.util.SecurityUtils;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Dashboard controller.
 *
 * @author zhangxd
 */
@Validated
@RestController
@Slf4j
@RequestMapping("/dashboard")
public class DashboardController extends BaseController {

    @Autowired
    private DashboardService dashboardService;

    /**
     * Get map.
     *
     * @return the map
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "")
    public Map<String, Object> get() {

        Map<String, Object> statMap = Maps.newHashMap();
        // 汇总
        statMap.putAll(dashboardService.countArchiveAmount());
        // 待转入档案列表
        statMap.put("rollStudents", dashboardService.findTransmitTodoList());
        // 院系统计
        statMap.put("statDeparts", dashboardService.statArchiveAmountByDepart());
        // 统计授权天数
        statMap.put("period", SecurityUtils.calculateLicense());
        return statMap;
    }

}
