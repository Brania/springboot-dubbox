package cn.zhangxd.platform.admin.web.controller;

import cn.zhangxd.platform.admin.web.domain.Transform;
import cn.zhangxd.platform.admin.web.service.TransformService;
import cn.zhangxd.platform.system.api.entity.SysUser;
import cn.zhangxd.platform.system.api.service.ISystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 陈辉[of2547]
 *         company qianmi.com
 *         Date    2018/3/11
 */
@RestController
@RequestMapping(value = "/transform")
@Slf4j
public class TransformController {

    @Autowired
    private ISystemService systemService;

    @Autowired
    private TransformService transformService;

    /**
     * 查询当前登录用户是否存在档案转接消息通知
     *
     * @return
     */
    @GetMapping(value = "/notify")
    public ResponseEntity<Boolean> notifyMessage() {
        return ResponseEntity.ok(transformService.findNotifyByLogin());
    }

    @GetMapping(value = "/listAll")
    public List<SysUser> listAll() {

        List<Transform> undoTransform = transformService.listAllUndo();
        List<SysUser> users = systemService.queryPreTransformUsers();
        users = users.stream().map(sysUser -> {
            for (Transform t : undoTransform) {
                if (sysUser.getId().equals(t.getToMemberId())) {
                    sysUser.setBeTransform(Boolean.TRUE);
                    break;
                }
            }
            return sysUser;
        }).collect(Collectors.toList());
        return users;
    }


    /**
     * 发起档案交接
     *
     * @param memberId
     * @return
     */
    @PostMapping(value = "/confirm")
    public ResponseEntity<Boolean> handleTransformEvent(@RequestParam String memberId) {
        return ResponseEntity.ok(transformService.addTransformEvent(memberId));
    }


}
