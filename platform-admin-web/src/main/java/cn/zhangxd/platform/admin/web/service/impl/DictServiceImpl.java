/*
 * Author:  ch-hui
 *
 * Copyright (c) 2017 Nanjing Hodoo Information Technology Co.,Ltd. All rights reserved.
 *
 * Email:   ch000.hui@gmail.com
 */

package cn.zhangxd.platform.admin.web.service.impl;

import cn.zhangxd.platform.admin.web.domain.AdClass;
import cn.zhangxd.platform.admin.web.domain.Depart;
import cn.zhangxd.platform.admin.web.domain.Major;
import cn.zhangxd.platform.admin.web.enums.SexEnum;
import cn.zhangxd.platform.admin.web.repository.AdClassRepository;
import cn.zhangxd.platform.admin.web.repository.DepartRepository;
import cn.zhangxd.platform.admin.web.repository.MajorRepository;
import cn.zhangxd.platform.admin.web.service.DictService;
import cn.zhangxd.platform.admin.web.util.Constants;
import cn.zhangxd.platform.admin.web.util.PaginationUtil;
import cn.zhangxd.platform.admin.web.util.sequence.Sequence;
import cn.zhangxd.platform.common.api.Paging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Created with IntelliJ IDEA
 * User: ch-hui
 * Date: ${Date}
 * Time: 下午10:36
 * <p>
 * "潜居抱道，已待其时" -《素书》
 * <p>
 * Description:
 */
@Service
@Slf4j
public class DictServiceImpl implements DictService {

    @Autowired
    private DepartRepository departRepository;

    @Autowired
    private AdClassRepository adClassRepository;

    @Autowired
    private MajorRepository majorRepository;

    Sequence sequence = new Sequence(0, 0);


    @Override
    public Depart findDepartById(Long id) {
        return departRepository.findOne(id);
    }

    @Override
    public Depart persistDepart(Depart depart) {
        if(null != depart.getId()){
            // 记录修改时间
        }else{
            depart = createDepart(depart.getName());
        }
        return departRepository.save(depart);
    }

    @Override
    public Boolean deleteDepartById(Long id) {
        Boolean flag = Boolean.TRUE;
        try {
            departRepository.delete(id);
        } catch (Exception e) {
            flag = Boolean.FALSE;
            log.error("删除院系操作失败，传入参数ID={}，失败原因：{}", id, e.getMessage());
        }
        return flag;
    }

    @Override
    public Page<Depart> getDepartPages(Map<String, Object> searchParams, Paging paging) {

        String[] sortParams = PaginationUtil.buildSortableParam(String.valueOf(searchParams.get("orderBy")));

        paging.setOrderBy(sortParams[0]);
        paging.setOrderType(sortParams[1]);

        PageRequest pageRequest = PaginationUtil.buildPageRequest(paging.getPageNum(), paging.getPageSize(), paging.getOrderBy(), paging.getOrderType());

        return departRepository.findAll(new Specification<Depart>() {
            @Override
            public Predicate toPredicate(Root<Depart> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = null;
                if (null != searchParams.get("departName") && String.valueOf(searchParams.get("departName")).length() > 0) {
                    StringJoiner sj = new StringJoiner("");
                    sj.add("%").add(String.valueOf(searchParams.get("departName")).trim()).add("%");
                    predicate = criteriaBuilder.like(root.get("name"), sj.toString());
                }

                return predicate;
            }
        }, pageRequest);

    }


    @Override
    public Iterable<Depart> findAll(Sort sort) {
        return departRepository.findAll();
    }


    @Override
    public AdClass createAdClass(String name) {
        AdClass adClass = new AdClass();
        adClass.setEnabled(Boolean.TRUE);
        adClass.setCreateTime(new Date());
        adClass.setName(name);
        StringJoiner sj = new StringJoiner("");
        sj.add(Constants.ADCLASS_CODE_PREFIX).add(sequence.nextSeq());
        adClass.setCode(sj.toString());
        return adClassRepository.save(adClass);
    }

    @Override
    public Depart createDepart(String name) {
        Depart depart = new Depart();
        depart.setEnabled(Boolean.TRUE);
        depart.setCreateTime(new Date());
        depart.setName(name);
        StringJoiner sj = new StringJoiner("");
        sj.add(Constants.DEPART_CODE_PREFIX).add(sequence.nextSeq());
        depart.setCode(sj.toString());
        return departRepository.save(depart);
    }

    @Override
    public Major createMajor(String name) {
        Major major = new Major();
        major.setEnabled(Boolean.TRUE);
        major.setCreateTime(new Date());
        major.setName(name);
        StringJoiner sj = new StringJoiner("");
        sj.add(Constants.MAJOR_CODE_PREFIX).add(sequence.nextSeq());
        major.setCode(sj.toString());
        return majorRepository.save(major);
    }
}
