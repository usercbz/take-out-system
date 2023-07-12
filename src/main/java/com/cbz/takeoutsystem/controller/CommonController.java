package com.cbz.takeoutsystem.controller;

import com.cbz.takeoutsystem.utils.Result;
import com.cbz.takeoutsystem.utils.StrUtils;
import com.cbz.takeoutsystem.utils.SystemConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("common")
public class CommonController {

    /**
     * 文件上传
     *
     * @param file 图片文件
     * @return 处理结果
     */
    @PostMapping("upload")
    public Result<String> upload(MultipartFile file) {
//        log.info(file.toString());
        if (file == null) {
            return Result.error("上传失败");
        }

        String filename = file.getOriginalFilename();
        String suffix = file.getOriginalFilename().substring(filename.lastIndexOf("."));

        String uuid = StrUtils.createUUID(true);
        filename = uuid + suffix;
        try {
            file.transferTo(new File(SystemConst.imgFileDir, filename));
            return Result.success(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.error("上传失败！");
    }

    @GetMapping("download")
    public Result<Object> download(String name, HttpServletResponse response) {

        FileInputStream fileInputStream = null;
        ServletOutputStream outputStream = null;
        try {
            fileInputStream = new FileInputStream(new File(SystemConst.imgFileDir, name));

            outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");

            byte[] bytes = new byte[1024];
            int len;
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                assert outputStream != null;
                outputStream.close();
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
