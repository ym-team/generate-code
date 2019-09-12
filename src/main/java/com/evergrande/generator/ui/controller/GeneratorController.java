package com.evergrande.generator.ui.controller;

import com.evergrande.generator.ui.bean.GeneratorInfo;
import com.evergrande.generator.ui.bean.TableResultResponse;
import com.evergrande.generator.ui.service.GeneratorService;
import com.evergrande.generator.ui.util.JsonUtil;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * @author LXH
 * @date 2018-01-30-20:12
 */
@Slf4j
@Controller
@RequestMapping("/base/generator")
public class GeneratorController {

    @Autowired
    private GeneratorService generatorService;

    @ResponseBody
    @RequestMapping("/database/list")
    public List<String> databaseList() {
        return generatorService.listDatabase();
    }

    @ResponseBody
    @RequestMapping("/page")
    public TableResultResponse<Map<String, Object>> list(@RequestParam Map<String, Object> params) {
        if (params != null) {
            if (StringUtils.isBlank((String) params.get("dbName"))) {
                params.put("dbName", "commdb");
            }
        }
        List<Map<String, Object>> result = generatorService.queryList(params);
        int total = generatorService.queryTotal(params);

        return new TableResultResponse<>(total, result);
    }

    @RequestMapping("/downloadIDL")
    public void downloadIDL(HttpServletResponse response) throws Exception {
        URI uri = Thread.currentThread().getContextClassLoader().getResource("template/TemplateIDL.java").toURI();
        File file = Paths.get(uri).toFile();
        @Cleanup InputStream in = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        in.read(data);

        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"TemplateIDL.java\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IOUtils.write(data, response.getOutputStream());
    }

    @ResponseBody
    @RequestMapping("/uploadIDL")
    public String uploadIDL(HttpServletRequest request) throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartRequest.getFiles("file").get(0);
        String id =""; //IdWorkerUtil.getStringId();
        String folderPath = this.getClass().getResource("/") + "upload/" + id;
        File uploadFolder = Paths.get(new URI(folderPath)).toFile();
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }
        File uploadFile = Paths.get(new URI(folderPath + "/" + multipartFile.getOriginalFilename())).toFile();
        uploadFile.createNewFile();
        log.info("上传的文件=>{}", uploadFile.getAbsolutePath());

        multipartFile.transferTo(uploadFile);

        return id + "_" + uploadFile.getName();
    }

    @RequestMapping("/code")
    public void code(GeneratorInfo generatorInfo, HttpServletResponse response) throws IOException {
        log.info("code generatorInfo=>{}", JsonUtil.toJsonString(generatorInfo));
        byte[] data = generatorService.generatorCode(generatorInfo);

        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"" + generatorInfo.getProjectName() + ".zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IOUtils.write(data, response.getOutputStream());
    }
}
