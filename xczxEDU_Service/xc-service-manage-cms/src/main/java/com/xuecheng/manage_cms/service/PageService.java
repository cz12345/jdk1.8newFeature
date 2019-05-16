package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PageService {
    @Autowired
    private CmsPageRepository cmsPageRepository;


    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        if (page <= 0) {
            page = 1;
        }
        page = page - 1; //分页索引从下标为0开始，而页面显示应该从1开始
        Pageable pageable = PageRequest.of(page, size);
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        QueryResult<CmsPage> queryResult = new QueryResult();
        if (all != null && all.getTotalElements() > 0) {
            queryResult.setList(all.getContent());
            queryResult.setTotal(all.getTotalElements());
        }
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;
    }
}
