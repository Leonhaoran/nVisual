package com.nvisual.lite.controller;
import com.nvisual.lite.entity.opencv.CabinetRecognitionResult;
import com.nvisual.lite.service.opencv.CabinetRecognitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/wapi/v1/")
public class CabinetRecognitionController {

    @Autowired
    private CabinetRecognitionService recognitionService;

    @PostMapping("/recognize")
    public CabinetRecognitionResult recognizeCabinets(
            @RequestParam("roomImage") MultipartFile roomImage,
            @RequestParam("templateImage") MultipartFile templateImage,
            @RequestParam(value = "confidence", required = false) Double confidence) {

        try {
            // 保存上传的文件到临时目录
            String roomPath = saveMultipartFile(roomImage, "room_");
            String templatePath = saveMultipartFile(templateImage, "template_");

            // 调用识别服务
            CabinetRecognitionResult result = recognitionService.recognizeCabinets(roomPath, templatePath, confidence);

            // 删除临时文件
            new File(roomPath).delete();
            new File(templatePath).delete();

            return result;

        } catch (IOException e) {
            return new CabinetRecognitionResult(false, "文件处理错误: " + e.getMessage(), null);
        }
    }

    private String saveMultipartFile(MultipartFile file, String prefix) throws IOException {
        String tempDir = System.getProperty("java.io.tmpdir");
        String fileName = prefix + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String filePath = tempDir + File.separator + fileName;

        File dest = new File(filePath);
        file.transferTo(dest);

        return filePath;
    }
}