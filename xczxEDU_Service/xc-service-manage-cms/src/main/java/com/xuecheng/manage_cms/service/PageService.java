package com.xuecheng.manage_cms.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
public class PageService {
    @Autowired
    private CmsPageRepository cmsPageRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private GridFSBucket gridFSBucket;


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
        if (cmsPage == null) {
            /*非法参数异常*/
            throw new HttpMessageNotReadableException("非法参数");
        }

        //在添加页面时，先校验该页面是否已经存在了

        String pageName = cmsPage.getPageName();
        String siteId = cmsPage.getSiteId();
        String pageWebPath = cmsPage.getPageWebPath();
        CmsPage cmsPageAlerady = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(pageName, siteId, pageWebPath);
        /*说明页面已经存在了，抛出自定义异常*/
        if (cmsPageAlerady != null) {
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }

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
            //更新dataUrl
            cmsPageNew.setDataUrl(cmsPage.getDataUrl());
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
        if (byId != null) {
            cmsPageRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }


    /*页面静态化模板*/
    public String getPageHtml(String id) {
        //根据页面ID获取模型数据
        CmsPageResult cmsPageResult = this.findById(id);
        if (cmsPageResult == null) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        Map model = getModel(cmsPageResult);

        if (model == null) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        //获取页面模板
        String temPlateContent = getTemplate(cmsPageResult);
        //执行页面静态化
        String html = generateHtml(temPlateContent, model);
        if(StringUtils.isEmpty(html)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        return html;
    }

    /*使用在内存生成模板方式来执行页面静态化，不需要在磁盘生成一个模板，这是最常用的方法*/
    /*就是根据一个段字符串，在程序执行时，在内存先生成一个模板，然后填充数据模型，返回静态化的页面（也就是html代码）*/
    private String generateHtml(String temPlateContent, Map model) {
        try {
            //生成配置类
            Configuration configuration = new Configuration(Configuration.getVersion());
            //模板加载器，根据temPlateContent生成模板文件
            StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
            stringTemplateLoader.putTemplate("template", temPlateContent);
            configuration.setTemplateLoader(stringTemplateLoader);
            //获取模板文件
            Template template = configuration.getTemplate("template");
            //填充数据模型，执行页面静态化
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            return html;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    private String getTemplate(CmsPageResult cmsPageResult) {
        CmsPage cmsPage = cmsPageResult.getCmsPage();
        String templateId = cmsPage.getTemplateId();
        if (StringUtils.isEmpty(templateId)) {
            //页面模板为空
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(templateId);
        if (optional.isPresent()) {
            CmsTemplate cmsTemplate = optional.get();
            //获取模板文件的ID
            String templateFileId = cmsTemplate.getTemplateFileId();
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
            //打开下载流对象
            GridFSDownloadStream gridFSDownloadStream =
                    gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            //创建GridFsResource
            GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
            try {
                String s = IOUtils.toString(gridFsResource.getInputStream(), "UTF-8");
                return s;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        //根据模板ID获取模板实体
        return null;

    }


    private Map getModel(CmsPageResult cmsPageResult) {

        //先获取页面实体
        CmsPage cmsPage = cmsPageResult.getCmsPage();
        if (cmsPage == null) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        else {
            //再获取页面的dataUrl
            String dataUrl = cmsPage.getDataUrl();
            if (StringUtils.isEmpty(dataUrl)) {
                ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
            }
            else {
                //使用restTemplate再次请求，来获取数据模型
                ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
                Map body = forEntity.getBody();
                return body;
            }
        }
        return null;
    }

}
