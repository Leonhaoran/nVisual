import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Template {
    private final Mat targetImage;
    private final List<Mat> templateImages;
    private final List<Scalar> scalars;

    public Template(Mat targetImage, List<Mat> templateImages, List<Scalar> scalars) {
        this.targetImage = targetImage;
        this.templateImages = templateImages;
        this.scalars = scalars;
    }

    public void match() {
        // 已匹配的点集和矩形
        HashSet<Point> points = new HashSet<>();
        List<List<Rect>> matchedRects = new ArrayList<>();

        for (int i = 0; i < templateImages.size(); i++) {
            matchedRects.add(new ArrayList<>());
        }

        Mat result = targetImage.clone();
        Mat similarity = new Mat();

        int iteration = 0;
        for (Mat templateImage : templateImages) {
            // 转换为灰度图
            Mat gray = new Mat();
            Imgproc.cvtColor(targetImage, gray, Imgproc.COLOR_BGR2GRAY);
            Imgproc.cvtColor(templateImage, templateImage, Imgproc.COLOR_BGR2GRAY);
            // 二值化
            Mat thresh = new Mat();
            Imgproc.threshold(gray, thresh, 0, 255, Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);
            Imgproc.threshold(templateImage, templateImage, 0, 255, Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);

            Imgproc.matchTemplate(thresh, templateImage, similarity, Imgproc.TM_CCOEFF_NORMED);

            double threshold = 0.5;
            for (int y = 0; y < similarity.rows(); y++) {
                for (int x = 0; x < similarity.cols(); x++) {
                    if (similarity.get(y, x)[0] > threshold && !isDuplicate(x, y, points)) {
                        Point matchLoc = new Point(x, y);
                        points.add(matchLoc);
                        matchedRects.get(iteration).add(new Rect(matchLoc, new Size(templateImage.width(), templateImage.height())));
                    }
                }
            }
            iteration++;
        }


        for (int i = 0; i < matchedRects.size(); i++) {
            List<Rect> list = matchedRects.get(i);
            for (Rect rect : list) {
                Imgproc.rectangle(result, rect.tl(), rect.br(), scalars.get(i), 2);
            }

        }
        Imgcodecs.imwrite("result.png", result);
    }

    public boolean isDuplicate(double x, double y, HashSet<Point> points) {
        for (Point point : points) {
            double dx = point.x - x;
            double dy = point.y - y;
            if (dx * dx + dy * dy < 200) {
                return true;
            }
        }
        return false;
    }
}
