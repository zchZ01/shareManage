package com.share.manage.controller;

import com.share.manage.model.VedioFile;
import com.share.manage.service.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class FileController {

    @Autowired
    IFileService fileService;

    /**
     * 上传页面跳转
     * @return
     */
    @RequestMapping("/fileAddForward")
    public String fileAddForward(){
    return "fileUpload";
    }

    /**
     * 上传文件
     * @param file
     * @return
     */
    @RequestMapping("/upload")
    @ResponseBody
    public String addFile(@RequestParam("fileName") MultipartFile file){
        if(file.isEmpty()){
            System.out.println("文件不可为空");
        }
        String fileName = file.getOriginalFilename();
        fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + fileName;
        String path = "f:/fileUpload/" +fileName;
        System.out.println(fileName);
        File dest = new File(path);
        //判断文件是否已经存在
        if (dest.exists()) {
            return "文件已经存在";
        }

        //判断文件父目录是否存在
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdir();
        }
        try {
            //上传文件
            file.transferTo(dest); //保存文件
            System.out.print("保存文件路径"+path+"\n");
            VedioFile vFile = new VedioFile();
            vFile.setUrl(path);
            vFile.setPersonName("张三");
            fileService.addFile(vFile);
        } catch (IOException e) {
            return "上传失败";
        }
        return "success";
    }

    @RequestMapping("/fileShow")
    public String showFile(Model model){

        VedioFile vedioFile = fileService.showFile(2);
        String url = vedioFile.getUrl();
        if(url!=null&&url.indexOf(".jpg")!=-1){
            String[] split = url.split("/");
            System.out.println(split[split.length-1]);
            model.addAttribute("image","images/"+split[split.length-1]);
        }
        if(url!=null&&url.indexOf(".mp4")!=-1){
            String[] split = url.split("/");
            model.addAttribute("video","images/"+split[split.length-1]);
        }
        return "fileShow";
    }
}
