import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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

    public void pre() {
        Imgproc.cvtColor(targetImage, targetImage, Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(targetImage, targetImage, 0, 255, Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);

        Imgproc.cvtColor(templateImage, templateImage, Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(templateImage, templateImage, 0, 255, Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);
    }


    public HashSet<Rect> match() {

        // 已匹配的点集和矩形
        HashSet<Point> points = new HashSet<>();
        HashSet<Rect> matchedRects = new HashSet<>();
        Imgproc.matchTemplate(targetImage, templateImage, similarity, Imgproc.TM_CCOEFF_NORMED);

        for (int y = 0; y < similarity.rows(); y++) {
            for (int x = 0; x < similarity.cols(); x++) {
                if (similarity.get(y, x)[0] > confidence && !isDuplicate(x, y, points, inaccuracy)) {
                    Point matchLoc = new Point(x, y);
                    points.add(matchLoc);
                    matchedRects.add(new Rect(matchLoc, new Size(templateImage.width(), templateImage.height())));
                }
            }
        }


        return matchedRects;
    }

//    public Mat draw(HashSet<Rect> matchedRects1, HashSet<Rect> matchedRects2, HashSet<Rect> matchedRects3, Mat display) {
//        for (Rect rect : matchedRects1) {
//            Imgproc.rectangle(display, rect.tl(), rect.br(), scalar, 2);
//        }
//        return display;
//    }

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
