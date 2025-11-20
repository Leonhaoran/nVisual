package com.nvisual.lite.service.opencv;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.HashSet;

public class TemplateMatcher {
    private Mat targetImage;
    private Mat templateImage;
    private double confidence;
    private double inaccuracy;
    private Mat similarity;

    public TemplateMatcher(Mat targetImage, Mat templateImage) {
        this.targetImage = targetImage;
        this.templateImage = templateImage;
        this.confidence = 0.5;
        this.inaccuracy = Math.pow(Math.min(templateImage.width(), templateImage.height()), 2) * 0.9;
        this.similarity = new Mat();
    }

    /**
     * 预处理图片
     */
    public void preprocess() {
        // 转换目标图像为灰度图并二值化
        if (targetImage.channels() > 1) {
            Imgproc.cvtColor(targetImage, targetImage, Imgproc.COLOR_BGR2GRAY);
        }
        Imgproc.threshold(targetImage, targetImage, 0, 255,
                Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);

        // 处理模板图像
        Mat grayTemplate = new Mat();
        if (templateImage.channels() > 1) {
            Imgproc.cvtColor(templateImage, grayTemplate, Imgproc.COLOR_BGR2GRAY);
        } else {
            templateImage.copyTo(grayTemplate);
        }

        Mat thresh = new Mat();
        Imgproc.threshold(grayTemplate, thresh, 245, 255, Imgproc.THRESH_BINARY_INV);

        // 找到非零区域并裁剪
        Mat points = new Mat();
        Core.findNonZero(thresh, points);
        if (points.rows() > 0) {
            Rect rect = Imgproc.boundingRect(points);
            templateImage = new Mat(templateImage, rect);
        }

        // 最终处理模板图像
        if (templateImage.channels() > 1) {
            Imgproc.cvtColor(templateImage, templateImage, Imgproc.COLOR_BGR2GRAY);
        }
        Imgproc.threshold(templateImage, templateImage, 0, 255,
                Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);

        // 释放临时Mat对象
        grayTemplate.release();
        thresh.release();
        points.release();
    }

    /**
     * 执行模板匹配
     * @return 匹配到的矩形区域集合
     */
    public HashSet<Rect> match() {
        HashSet<Point> points = new HashSet<>();
        HashSet<Rect> matchedRects = new HashSet<>();

        // 执行模板匹配
        Imgproc.matchTemplate(targetImage, templateImage, similarity, Imgproc.TM_CCOEFF_NORMED);

        // 遍历匹配结果
        for (int y = 0; y < similarity.rows(); y++) {
            for (int x = 0; x < similarity.cols(); x++) {
                double matchValue = similarity.get(y, x)[0];
                if (matchValue > confidence && !isDuplicate(x, y, points, inaccuracy)) {
                    Point matchLoc = new Point(x, y);
                    points.add(matchLoc);
                    matchedRects.add(new Rect(matchLoc,
                            new Size(templateImage.width(), templateImage.height())));
                }
            }
        }

        // 释放相似度矩阵
        similarity.release();

        return matchedRects;
    }

    /**
     * 检查是否重复匹配
     */
    private boolean isDuplicate(double x, double y, HashSet<Point> points, double inaccuracy) {
        for (Point point : points) {
            double dx = point.x - x;
            double dy = point.y - y;
            if (dx * dx + dy * dy < inaccuracy) {
                return true;
            }
        }
        return false;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }
}