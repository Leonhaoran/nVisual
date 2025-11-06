import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class Template {
    private Mat targetImage;
    private Mat templateImage;

    public Template(Mat targetImage, Mat templateImage) {
        this.targetImage = targetImage;
        this.templateImage = templateImage;
    }

    public List<Rect> match() {
        Mat result = new Mat();
        Imgproc.matchTemplate(targetImage, templateImage, result, Imgproc.TM_CCOEFF_NORMED);
        List<Rect> matchedRects = new ArrayList<>();
        double threshold = 0.8;
        for (int y = 0; y < result.rows(); y++) {
            for (int x = 0; x < result.cols(); x++) {
                if (result.get(y, x)[0] > threshold) {
                    Point matchLoc = new Point(x, y);
                    matchedRects.add(new Rect(matchLoc, new Size(templateImage.width(), templateImage.height())));
                }
            }
        }
        return matchedRects;
    }

    public void displayMatches(List<Rect> matches) {
        for (Rect rect: matches){
            Imgproc.rectangle(targetImage, rect.tl(), rect.br(), new Scalar(0, 255, 0), 2);
        }
        Imgcodecs.imwrite("result.png", targetImage);
    }
}
