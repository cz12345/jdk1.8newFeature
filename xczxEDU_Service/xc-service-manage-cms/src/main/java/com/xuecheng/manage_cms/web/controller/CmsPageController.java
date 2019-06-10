package com.xuecheng.manage_cms.web.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.service.PageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(value = "cms页面管理接口", description = "cms页面管理接口，提供页面增删查改")
@RestController
@RequestMapping("/cms/page")
public class CmsPageController implements CmsPageControllerApi {

    @Autowired
    private PageService pageService;

    @RequestMapping("/list/{page}/{size}")
    @Override
    @ApiOperation("cms分页查询列表")
    @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "int", paramType = "path")
    public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size") int size,
                                        QueryPageRequest queryPageRequest) {
        QueryResponseResult QueryResponseResult = pageService.findList(page, size, queryPageRequest);
        return QueryResponseResult;
    }

    @ApiOperation("cms页面增加")
    @Override
    @PostMapping("/add")
    public CmsPageResult add(@RequestBody CmsPage cmsPage) {
        return pageService.add(cmsPage);
    }

    @ApiOperation("cms页面回显")
    @GetMapping("/get/{id}")
    public CmsPageResult findById(@PathVariable("id") String id) {

        return pageService.findById(id);
    }


    @ApiOperation("cms页面修改")
    @Override
    @PutMapping("/edit/{id}")
    public CmsPageResult edit(@PathVariable("id") String id,@RequestBody CmsPage cmsPage) {
        return pageService.update(id,cmsPage);
    }

    @Override
    public ResponseResult delete(String id) {
        return pageService.deletePageById(id);
    }


}
