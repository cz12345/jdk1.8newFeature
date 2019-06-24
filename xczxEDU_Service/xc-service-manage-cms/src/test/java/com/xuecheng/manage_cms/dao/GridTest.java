package com.xuecheng.manage_cms.dao;


import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.portable.InputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GridTest {


    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    GridFSBucket gridFSBucket;


    //向monodb存储一个模板文件
    @Test
    public void store() throws FileNotFoundException {
        File file = new File("C:\\work\\index_banner.ftl");
        //        gridFsTemplate
        FileInputStream inputStream = new FileInputStream(file);
        //文件存储成功会返回一个文件的ID，后续如果取文件的话，根据这个ID可以取出文件
        ObjectId objectId = gridFsTemplate.store(inputStream, "轮播图测试文件01", "");
        String fileId = objectId.toString();
        System.out.println("fileId:" + fileId);

    }


    //从mongodb取出一个文件
    @Test
    public void query() throws IOException {
        //根据文件ID查询文件，注意他是根据文件ID去查询files，和对应chunk块
        String id = "5d0af8746451cf3f946b2a58";
        //根据ID查询文件
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(id)));
        //使用gridFSBucket打开下载流对象
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);
        String s = IOUtils.toString(gridFsResource.getInputStream(), "UTF-8");
        //这个s就是文件的内容了，也就是模板文件的内容了
        //把他写入到一个文件中
        FileWriter fileWriter = new FileWriter("C:\\work\\index2.ftl");
        fileWriter.write(s);
        System.out.println("ftl_content:"+s);
        fileWriter.close();
        gridFSDownloadStream.close();
    }
}
