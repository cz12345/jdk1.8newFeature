package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsConfigRespositoryTest  {

    @Autowired
    private CmsConfigRepository cmsConfigRepository;


    @Test
    public void test1() {
        Optional<CmsConfig> Optional = cmsConfigRepository.findById("5a791725dd573c3574ee333f");
    }
}
