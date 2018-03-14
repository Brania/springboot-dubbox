package cn.zhangxd.platform.admin.web.controller;

import cn.zhangxd.platform.admin.web.domain.Depart;
import cn.zhangxd.platform.admin.web.domain.Transform;
import cn.zhangxd.platform.admin.web.domain.dto.TransformDto;
import cn.zhangxd.platform.admin.web.service.DictService;
import cn.zhangxd.platform.admin.web.service.StudentService;
import cn.zhangxd.platform.admin.web.service.TransformService;
import cn.zhangxd.platform.system.api.entity.SysUser;
import cn.zhangxd.platform.system.api.service.ISystemService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
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

    @Autowired
    private StudentService studentService;

    @Autowired
    private DictService dictService;

    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 查询当前登录用户是否存在档案转接消息通知
     *
     * @return
     */
    @GetMapping(value = "/check/notify")
    public ResponseEntity<Object> notifyMessage() {
        Transform transform = transformService.findNotifyByLogin();
        Optional<TransformDto> transformOpt = Optional.empty();
        if (null != transform) {
            Depart depart = dictService.findDepartByCode(transform.getDeptCode());
            TransformDto dto = TransformDto.builder()
                    .id(String.valueOf(transform.getId()))
                    .deptName(transform.getDeptName())
                    .count(String.valueOf(studentService.countArchiveByDepart(depart)))
                    .createTime(sf.format(transform.getCreateTime()))
                    .build();
            transformOpt = Optional.of(dto);
        }

        return transformOpt.isPresent() ? ResponseEntity.ok(JSON.toJSONString(transformOpt.get())) : ResponseEntity.ok("");
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
    @PostMapping(value = "/start")
    public ResponseEntity<Boolean> startTransformEvent(@RequestParam String memberId) {
        return ResponseEntity.ok(transformService.addTransformEvent(memberId));
    }

    /**
     * 完成档案交接
     *
     * @return
     */
    @GetMapping(value = "/finish/notify/{id}")
    public ResponseEntity<Boolean> finishTransformEvent(@PathVariable String id) {
        Boolean success = Boolean.FALSE;
        try {
            success = transformService.finishTransformEvent(id);
        } catch (Exception e) {
            log.error("接收档案交接失败: {}", e.getMessage());
        }
        return ResponseEntity.ok(success);
    }


}
