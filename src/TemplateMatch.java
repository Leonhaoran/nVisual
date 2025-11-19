import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.util.Collections;
import java.util.HashSet;

/*
 targetImage 待检测图片
 templateImage 模板图片
 confidence 置信度
 inaccuracy 误差
 similarity 存放模板匹配的结果
 */
public class TemplateMatch {
    private Mat targetImage;
    private Mat templateImage;
    private double confidence;
    private double inaccuracy;
    private Mat similarity;


    public TemplateMatch(Mat targetImage, Mat templateImage) {
        this.targetImage = targetImage;
        this.templateImage = templateImage;
        this.confidence = 0.5;
        this.inaccuracy = Math.pow(Math.min(templateImage.width(), templateImage.height()), 2) * 0.9;
        this.similarity = new Mat();
    }

    // 预处理
    // 将待检测图片和模板图片先转换为灰度图，然后在进行二值化，将其转换为黑白图片，便于后续检测
    public void pre() {
        Imgproc.cvtColor(targetImage, targetImage, Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(targetImage, targetImage, 0, 255, Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);

        Mat gray = new Mat();
        Imgproc.cvtColor(templateImage, gray, Imgproc.COLOR_BGR2GRAY);
        Mat thresh = new Mat();
        Imgproc.threshold(gray, thresh, 245, 255, Imgproc.THRESH_BINARY_INV);
        Mat points = new Mat();
        Core.findNonZero(thresh, points);
        Rect rect = Imgproc.boundingRect(points);
        templateImage = new Mat(templateImage, rect);

        Imgproc.cvtColor(templateImage, templateImage, Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(templateImage, templateImage, 0, 255, Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);


    }

    // 模板匹配
    public HashSet<Rect> match() {
        // 匹配的点集（即矩形的左上角）和矩形
        HashSet<Point> points = new HashSet<>();
        HashSet<Rect> matchedRects = new HashSet<>();
        Imgproc.matchTemplate(targetImage, templateImage, similarity, Imgproc.TM_CCOEFF_NORMED);
        for (int y = 0; y < similarity.rows(); y++) {
            for (int x = 0; x < similarity.cols(); x++) {
                if (similarity.get(y, x)[0] > confidence && !isDuplicate(x, y, points, inaccuracy)) {
                    // TODO 建立一百个队列，将[a,a+1)置信度的矩阵放到第a个队列中，之后就不用重新匹配了，怀疑可行度
                    Point matchLoc = new Point(x, y);
                    points.add(matchLoc);
                    matchedRects.add(new Rect(matchLoc, new Size(templateImage.width(), templateImage.height())));
                }
            }
        }

        return matchedRects;
    }


    // 防止一个地方被多次匹配
    public boolean isDuplicate(double x, double y, HashSet<Point> points, double inaccuracy) {
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
