package cn.zhangxd.platform.admin.web.controller;

import cn.zhangxd.platform.admin.web.common.controller.BaseController;
import cn.zhangxd.platform.admin.web.service.DashboardService;
import cn.zhangxd.platform.admin.web.util.Constants;
import cn.zhangxd.platform.common.web.annotations.Action;
import cn.zhangxd.platform.common.web.annotations.License;
import cn.zhangxd.platform.common.web.util.LicenseUtils;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    @License(action = Action.Skip)
    public Map<String, Object> get(@RequestParam(required = false, defaultValue = Constants.DEFAULT_STAT_YEAR) Integer statYear) {

        Map<String, Object> statMap = Maps.newHashMap();
        log.info("统计年份={}", statYear);
        // 汇总
        statMap.putAll(dashboardService.countArchiveAmount(statYear));
        // 待转入档案列表
        statMap.put("rollStudents", dashboardService.findTransmitTodoList());
        // 院系统计
        statMap.put("statDeparts", dashboardService.statArchiveAmountByDepart(statYear));
        // 统计授权天数
        statMap.put("period", LicenseUtils.check());
        // 统计业务年份
        statMap.put("statYear", statYear);

        return statMap;
    }

}
