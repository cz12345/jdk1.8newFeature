package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PageService {
    @Autowired
    private CmsPageRepository cmsPageRepository;


    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        if (page <= 0) {
            page = 1;
        }
        CmsPage cmsPage = new CmsPage();
        //创建匹配规则
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        if (StringUtils.isNotBlank(queryPageRequest.getSiteId())) {
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        if (StringUtils.isNotBlank(queryPageRequest.getPageAliase())) {
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        //创建查询匹配器，并且实体和匹配规则作为参数传入进来
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);
        page = page - 1; //分页索引从下标为0开始，而页面显示应该从1开始
        Pageable pageable = PageRequest.of(page, size);
        //根据查询匹配器，查询并且分页
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        QueryResult<CmsPage> queryResult = new QueryResult();
        if (all != null && all.getTotalElements() > 0) {
            queryResult.setList(all.getContent());
            queryResult.setTotal(all.getTotalElements());
        }
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }


    public CmsPageResult add(CmsPage cmsPage) {
        //在添加页面时，先校验该页面是否已经存在了

        String pageName = cmsPage.getPageName();
        String siteId = cmsPage.getSiteId();
        String pageWebPath = cmsPage.getPageWebPath();
        CmsPage cmsPageAlerady = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(pageName, siteId, pageWebPath);
        if (cmsPageAlerady == null) { //说明不存在该页面，可以直接新增
            cmsPage.setPageId(null); //这是为了让数据自己设置ID，防止自己设置产生相同的ID
            cmsPageRepository.save(cmsPage);
            return new CmsPageResult(CommonCode.SUCCESS, cmsPage);
        }
        return new CmsPageResult(CommonCode.FAIL, null);

    }

    //根据ID获取页面
    public CmsPageResult findById(String id) {

        Optional<CmsPage> byId = cmsPageRepository.findById(id);
        if (byId.isPresent()) { //代表查到了，
            return new CmsPageResult(CommonCode.SUCCESS, byId.get());
        }
        return null;
    }

    //修改页面
    public CmsPageResult update(String id, CmsPage cmsPage) {
        //修改页面之前先查询是否存在该页面
        CmsPageResult byId = findById(id);
        if (byId != null) {
            CmsPage cmsPageNew = byId.getCmsPage();
            //开始修改
            cmsPageNew.setTemplateId(cmsPage.getTemplateId());
            //更新所属站点
            cmsPageNew.setSiteId(cmsPage.getSiteId());
            //更新页面别名
            cmsPageNew.setPageAliase(cmsPage.getPageAliase());
            //更新页面名称
            cmsPageNew.setPageName(cmsPage.getPageName());
            //更新访问路径
            cmsPageNew.setPageWebPath(cmsPage.getPageWebPath());
            //更新物理路径
            cmsPageNew.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            //执行更新
            CmsPage save = cmsPageRepository.save(cmsPageNew);
            //只要插入成功，就会返回该插入的对象；否则返回null
            if (save != null) {
                CmsPageResult cmsPageResult = new CmsPageResult(CommonCode.SUCCESS, save);
                return cmsPageResult;
            }
        }

        return new CmsPageResult(CommonCode.FAIL, null);
    }


    /*删除页面*/
    public ResponseResult deletePageById(String id) {
        //先查询该页面ID是否存在
        CmsPageResult byId = findById(id);
        if(byId != null) {
            cmsPageRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }
}
