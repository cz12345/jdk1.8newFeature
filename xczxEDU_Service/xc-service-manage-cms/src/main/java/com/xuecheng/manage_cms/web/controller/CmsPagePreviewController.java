package com.xuecheng.manage_cms.web.controller;


import com.xuecheng.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class CmsPagePreviewController  {

    @Autowired
    PageService pageService;



    @RequestMapping(value = "/cms/preview/{id}",method = RequestMethod.GET)
    public void preview(HttpServletResponse response, @PathVariable("id") String id) {
        String pageHtml = pageService.getPageHtml(id);
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(pageHtml.getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
