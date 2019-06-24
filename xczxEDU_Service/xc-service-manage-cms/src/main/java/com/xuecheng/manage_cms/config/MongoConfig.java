package com.xuecheng.manage_cms.config;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//有关Mongo的配置类
@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.database}")
    String db;


    //mongoDb中的GridFSBucket对象，主要用于读取数据使用,是用来打开一个文件下载流
    //需要使用到参数MongoClient，如果spring容器中只有该参数实例的一个对象，MongoClient对象在spring容器中只有一个实例
    //那么再取参数时，就会取这一个参数实例，但是如果有多个实例，那么必须告知具体去哪一个实例，多个一般根据参数名（ID）来取对应的实例
    @Bean
    public GridFSBucket getGridFSBucket(MongoClient mongoClient) {
        //拿到连接的数据库
        MongoDatabase database = mongoClient.getDatabase(db);
        //创建GridFSBucket对象
        GridFSBucket gridFSBucket = GridFSBuckets.create(database);
        return gridFSBucket;
    }
}
