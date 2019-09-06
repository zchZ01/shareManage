package com.share.manage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * 虚拟路径生成，使得可加载本地资源
 */
@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter {
    /**
     * 在配置文件中配置的文件保存路径
     */
    @Value("${cbs.filePath}")
    private String filePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if(filePath.equals("") || filePath.equals("${cbs.filePath}")){
            String imagesPath = WebAppConfig.class.getClassLoader().getResource("").getPath();
            System.out.print("1.上传配置类imagesPath=="+imagesPath+"\n");
            if(imagesPath.indexOf(".jar")>0){
                imagesPath = imagesPath.substring(0, imagesPath.indexOf(".jar"));
            }else if(imagesPath.indexOf("classes")>0){
                imagesPath = "file:"+imagesPath.substring(0, imagesPath.indexOf("classes"));
            }
            imagesPath = imagesPath.substring(0, imagesPath.lastIndexOf("/"))+"/images/";
            System.out.print("imagesPath============="+imagesPath+"\n");
            filePath = imagesPath;
        }
        System.out.print("filePath============="+filePath+"\n");
        registry.addResourceHandler("/images/**").addResourceLocations(filePath);
        // TODO Auto-generated method stub
        System.out.print("2.上传配置类mImagesPath=="+filePath+"\n");
        super.addResourceHandlers(registry);
    }
}
