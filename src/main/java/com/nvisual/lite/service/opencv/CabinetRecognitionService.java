package com.nvisual.lite.service.opencv;
import com.nvisual.lite.entity.opencv.CabinetPosition;
import com.nvisual.lite.entity.opencv.CabinetRecognitionResult;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class CabinetRecognitionService {

    static {
        // 加载OpenCV本地库
        nu.pattern.OpenCV.loadLocally();
    }

    /**
     * 识别机柜位置
     * @param roomImagePath 机房图纸文件路径
     * @param templateImagePath 机柜模板文件路径
     * @param confidenceThreshold 置信度阈值，默认0.5
     * @return 识别结果
     */
    public CabinetRecognitionResult recognizeCabinets(String roomImagePath,
                                                      String templateImagePath,
                                                      Double confidenceThreshold) {
        try {
            // 验证文件存在性
            if (!new File(roomImagePath).exists()) {
                return new CabinetRecognitionResult(false, "机房图纸文件不存在: " + roomImagePath, null);
            }
            if (!new File(templateImagePath).exists()) {
                return new CabinetRecognitionResult(false, "机柜模板文件不存在: " + templateImagePath, null);
            }

            // 加载图片
            Mat targetImage = Imgcodecs.imread(roomImagePath);
            Mat templateImage = Imgcodecs.imread(templateImagePath);

            if (targetImage.empty()) {
                return new CabinetRecognitionResult(false, "无法加载机房图纸: " + roomImagePath, null);
            }
            if (templateImage.empty()) {
                return new CabinetRecognitionResult(false, "无法加载机柜模板: " + templateImagePath, null);
            }

            // 创建模板匹配实例
            TemplateMatcher matcher = new TemplateMatcher(targetImage, templateImage);

            // 设置置信度阈值
            if (confidenceThreshold != null) {
                matcher.setConfidence(confidenceThreshold);
            }

            // 执行预处理
            matcher.preprocess();

            // 进行匹配
            HashSet<Rect> matchedRects = matcher.match();

            // 转换为返回结果
            List<CabinetPosition> positions = new ArrayList<>();
            for (Rect rect : matchedRects) {
                positions.add(new CabinetPosition(
                        rect.x,
                        rect.y,
                        rect.width,
                        rect.height,
                        0.8 // 这里可以根据实际匹配度设置，原代码中没有保存每个匹配的置信度
                ));
            }

            // 释放资源
            targetImage.release();
            templateImage.release();

            return new CabinetRecognitionResult(true,
                    String.format("成功识别到 %d 个机柜", positions.size()),
                    positions);

        } catch (Exception e) {
            return new CabinetRecognitionResult(false,
                    "识别过程中发生错误: " + e.getMessage(), null);
        }
    }

    /**
     * 重载方法，使用默认置信度阈值
     */
    public CabinetRecognitionResult recognizeCabinets(String roomImagePath, String templateImagePath) {
        return recognizeCabinets(roomImagePath, templateImagePath, null);
    }
}