package cn.zhangxd.platform.admin.web.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 陈辉[of2547]
 *         company qianmi.com
 *         Date    2018/3/14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransformDto implements Serializable {
    private static final long serialVersionUID = 1950132500490884579L;

    private String id;
    private String deptName;
    private String count;
    private String createTime;
}
