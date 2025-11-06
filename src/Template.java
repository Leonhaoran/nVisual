import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Template {
    private Mat targetImage;
    private Mat templateImage;

    public Template(Mat targetImage, Mat templateImage) {
        this.targetImage = targetImage;
        this.templateImage = templateImage;
    }

    public void match() {
        Mat result = new Mat();
        // 转换为灰度图
        Mat gray = new Mat();
        Imgproc.cvtColor(targetImage, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(templateImage, templateImage, Imgproc.COLOR_BGR2GRAY);
        // 二值化
        Mat thresh = new Mat();
        Imgproc.threshold(gray, thresh, 0, 255, Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);
        Imgproc.threshold(templateImage, templateImage, 0, 255, Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);

        Imgproc.matchTemplate(thresh, templateImage, result, Imgproc.TM_CCOEFF_NORMED);

        List<Rect> matchedRects = new ArrayList<>();
        double threshold = 0.45;
        for (int y = 0; y < result.rows(); y++) {
            for (int x = 0; x < result.cols(); x++) {
                if (result.get(y, x)[0] > threshold ) {
                    Point matchLoc = new Point(x, y);
                    matchedRects.add(new Rect(matchLoc, new Size(templateImage.width(), templateImage.height())));
                }
            }
        }
        for (Rect rect : matchedRects) {
            Imgproc.rectangle(targetImage, rect.tl(), rect.br(), new Scalar(0, 255, 0), 2);
        }
        Imgcodecs.imwrite("result.png", targetImage);
    }

    public boolean isDuplicate(double x, double y, HashSet<Point> points) {
        for (Point point : points) {
            double dx = point.x - x;
            double dy = point.y - y;
            if (dx * dx + dy * dy < 250) {
                return true;
            }
        }
        return false;
    }
}
