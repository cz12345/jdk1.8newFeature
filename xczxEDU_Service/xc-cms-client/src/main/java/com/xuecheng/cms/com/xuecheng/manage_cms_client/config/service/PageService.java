package com.xuecheng.cms.com.xuecheng.manage_cms_client.config.service;


import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.cms.com.xuecheng.manage_cms_client.config.dao.CmsPageRepository;
import com.xuecheng.cms.com.xuecheng.manage_cms_client.config.dao.CmsSiteRepository;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.ExceptionCast;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
public class PageService {
    @Autowired
    private CmsPageRepository cmsPageRepository;
    @Autowired
    private CmsSiteRepository cmsSiteRepository;
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Qualifier
    GridFSBucket gridFSBucket;

    /*将静态化的文件取出保存到服务器目录 实现页面发布
     * 这样保存到服务器目录，前端一刷新，就是刚刚静态化并且发布的页面，通过nginx访问该页面
     * */
    public void savePageToServerPath(String pageId) {
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        CmsPage cmsPage = optional.get();
        //获得站点
        CmsSite cmsSite = findSite(cmsPage.getSiteId());
        //物理路径  =  站点路劲+页面路劲
        String sitePhysicalPath = cmsSite.getSitePhysicalPath();
        String pagePhysicalPath = cmsPage.getPagePhysicalPath();
        String pageName = cmsPage.getPageName();
        String path = sitePhysicalPath + pagePhysicalPath + pageName;
        //从Monodb获取静态文件输入流
        InputStream inputStream = getFileById(cmsPage.getHtmlFileId());
        //定义文件输出流 将文件写入到path路径上
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(path);
            try {
                IOUtils.copy(inputStream, fileOutputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private InputStream getFileById(String fileId) {
        //获取文件对象
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
        //获取下载流
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        //定义GridFsResource
        GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
        try {
            //获取该文件的下载流 即读入流
            return gridFsResource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private CmsSite findSite(String siteId) {
        Optional<CmsSite> byId = cmsSiteRepository.findById(siteId);
        if (byId.isPresent()) {
            return byId.get();
        }
        return null;
    }

}
