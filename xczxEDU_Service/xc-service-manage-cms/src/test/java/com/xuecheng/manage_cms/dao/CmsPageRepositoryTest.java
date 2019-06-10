package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {

    @Autowired
    private CmsPageRepository cmsPageRepository;


    @Test
    public void findListTest() {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page,size);

        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        System.out.println(all.getContent());

    }


//    测试自定义带条件查询和分页查询混用
    @Test
    public void testFindAll() {
        CmsPage cmsPage = new CmsPage();
        //1 精确查询，根据ID
//        cmsPage.setPageId("5a754adf6abb500ad05688d9"); //精确查询
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();//创建查询匹配器
        //记住在配置查询规则时，它是返回到ExampleMatcher对象中，所以一定要有返回值，使用该返回值传入到参数中
        exampleMatcher = exampleMatcher.withMatcher("pageAliase",
                ExampleMatcher.GenericPropertyMatchers.contains());
        //配置查询规则
        //1. 根据别名使用包含模糊查询 ExampleMatcher.GenericPropertyMatchers.contains():只要别名包含了轮播图都查询出来
        //2. 等等其他匹配方法规则
        Pageable pageable = PageRequest.of(0,100); //分页
        // 精确查询，根据别名和ID
        cmsPage.setPageAliase("轮播图");
//        cmsPage.setPageId("5cef4f447f3ace04946f1ad6");
        Example<CmsPage> example = Example.of(cmsPage,exampleMatcher);
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        System.out.println(all.getContent());
    }





}
