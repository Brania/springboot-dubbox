package cn.zhangxd.platform.admin.web.repository;

import cn.zhangxd.platform.admin.web.domain.Transform;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author 陈辉[of2547]
 *         company qianmi.com
 *         Date    2018/3/11
 */
public interface TransformRepository extends PagingAndSortingRepository<Transform, Long>, JpaSpecificationExecutor<Transform> {

    @Query("select t from Transform t where t.done='0' ")
    List<Transform> findProcessTransform();

    Transform findByToMemberIdAndDone(String memberId, Boolean done);
}
