import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TemplateMatch {
    private final Mat targetImage;
    private final List<Mat> templateImages;
    private final List<Scalar> scalars;

    public TemplateMatch(Mat targetImage, List<Mat> templateImages, List<Scalar> scalars) {
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

        Mat gray = new Mat();
        Imgproc.cvtColor(targetImage, gray, Imgproc.COLOR_BGR2GRAY);
        Mat thresh = new Mat();
        Imgproc.threshold(gray, thresh, 0, 255, Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);


        int iteration = 0;
        for (Mat templateImage : templateImages) {

            double inaccuracy = Math.min(templateImage.width(), templateImage.height());
            inaccuracy = inaccuracy * inaccuracy * 0.9;
            // 转换为灰度图
            Imgproc.cvtColor(templateImage, templateImage, Imgproc.COLOR_BGR2GRAY);
            // 二值化
            Imgproc.threshold(templateImage, templateImage, 0, 255, Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);

            Imgproc.matchTemplate(thresh, templateImage, similarity, Imgproc.TM_CCOEFF_NORMED);

            double threshold = 0.5;
            for (int y = 0; y < similarity.rows(); y++) {
                for (int x = 0; x < similarity.cols(); x++) {
                    if (similarity.get(y, x)[0] > threshold && !isDuplicate(x, y, points, inaccuracy)) {
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
}
