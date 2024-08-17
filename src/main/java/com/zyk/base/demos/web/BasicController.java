/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zyk.base.demos.web;

import com.zyk.base.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
@Controller
public class BasicController {

    // http://127.0.0.1:8080/hello?name=zyk&phone=123&nickname=zxg
    @RequestMapping("/hello")
    @ResponseBody
    public String hello(@RequestParam(name = "name", defaultValue = "unknown user") String name, String phone, @RequestParam(name = "nickname", defaultValue = "noAlias", required = false) String alias) {
        System.out.println(name);
        System.out.println(phone);
        System.out.println(alias);
        return "Hello " + name;
    }

    // http://127.0.0.1:8080/user?name=zyk&phone=123&alias=zxg
    @RequestMapping("/user-test")
    @ResponseBody
    public User user(User user) {
        System.out.println(user.getName());
        System.out.println(user.getPhone());

        return user;
    }

    // http://127.0.0.1:8080/save_user?name=newName&age=11
    @RequestMapping("/save_user")
    @ResponseBody
    public String saveUser(User u) {
        return "user will save: name=" + u.getName() + ", phone=" + u.getPhone();
    }

    // http://127.0.0.1:8080/html
    @RequestMapping("/html")
    public String html(){
        return "index.html";
    }

    @ModelAttribute
    public void parseUser(@RequestParam(name = "name", defaultValue = "unknown user") String name
            , @RequestParam(name = "age", defaultValue = "12") Integer age, User user) {
        user.setName("zhangsan");
        user.setPhone("123123123");
    }

    @Value("${upload-dir}")
    private String UPLOAD_DIR;

    @PostMapping("/upload")
    @ResponseBody
    public String upload(String name, MultipartFile file, HttpServletRequest request) throws IOException {
        System.out.println("name is " + name);
        System.out.println(file.getOriginalFilename());

        saveFile(file, StandardCopyOption.REPLACE_EXISTING);

        return "success";
    }

    // 保存文件到指定目录，UPLOAD_DIR 配置为 '/upload-file/'，则是保存到classpath所在的根目录下，即"E:/upload-file/"
    private void saveFile(MultipartFile file, StandardCopyOption sco) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        //判断路径是否存在
        Path destPath = Paths.get(UPLOAD_DIR);
        if(!Files.exists(destPath)){
            Files.createDirectories(destPath);
        }
        //构造文件名称
        Path destFile = destPath.resolve(fileName);

        System.out.println(destPath.getFileName());

        Files.copy(file.getInputStream(), destFile, sco);
    }

    // 保存文件到指定目录，UPLOAD_DIR 需要配置为绝对路径，如：E:/upload-file/
    private void saveFile(MultipartFile file) throws IOException {

        File destPath = new File(UPLOAD_DIR);
        if(!destPath.exists()){
            Boolean mkdir = destPath.mkdir();
            System.out.println("Create the new directory: " + destPath.getAbsolutePath());
        }

        File destFile = new File(UPLOAD_DIR + file.getOriginalFilename());
        System.out.println(destFile.getAbsolutePath());
        file.transferTo(destFile);
    }

    // 保存文件到容器的相对目录下，UPLOAD_DIR 配置为 /upload-file/
    private void saveFile(MultipartFile file, HttpServletRequest request) throws IOException {

        String uploadDir = request.getServletContext().getRealPath(UPLOAD_DIR);

        File destPath = new File(uploadDir);
        if(!destPath.exists()){
            Boolean mkdir = destPath.mkdir();
            System.out.println("Create the new directory: " + destPath.getAbsolutePath());
        }

        File destFile = new File(uploadDir + file.getOriginalFilename());
        System.out.println(destFile.getAbsolutePath());
        file.transferTo(destFile);
    }



}
