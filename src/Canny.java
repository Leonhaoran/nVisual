import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.*;

public class Canny {
    private Mat image;
    private int tileWidth;
    private int tileHeight;
    private int overlap;

    public Canny(Mat image, int tileWidth, int tileHeight, int overlap) {
        this.image = image;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.overlap = overlap;
    }

    public void match() {
        int imageWidth = image.cols();
        int imageHeight = image.rows();
        HashSet<Point> points = new HashSet<>();
        Map<Point, String> red = new HashMap<>();
        Map<Point, String> blue = new HashMap<>();
        Map<Point, String> green = new HashMap<>();

        Mat result = image.clone();

        for (int x = 0; x < imageWidth; x += tileWidth) {
            for (int y = 0; y < imageHeight; y += tileHeight) {
                int w = Math.min(tileWidth + overlap, imageWidth - x);
                int h = Math.min(tileHeight + overlap, imageHeight - y);

                Rect roi = new Rect(x, y, w, h);
//                Imgproc.rectangle(result, roi, new Scalar(0, 0, 0), 5);
                Mat tile = new Mat(image, roi);

                // 转换为灰度图
                Mat gray = new Mat();
                Imgproc.cvtColor(tile, gray, Imgproc.COLOR_BGR2GRAY);

                // 二值化
                Mat threshold = new Mat();
                Imgproc.threshold(gray, threshold, 0, 255, Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);

//                HighGui.imshow("threshold", threshold);
//                HighGui.waitKey();

                // 形态学操作
                Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
                Mat dst = new Mat();
                Imgproc.morphologyEx(threshold, dst, Imgproc.MORPH_GRADIENT, element);


                // 使用Canny算法检测图像的边缘
                Mat edges = new Mat();
                Imgproc.Canny(dst, edges, 50, 100);

                // 轮廓检测
                List<MatOfPoint> contours = new LinkedList<>();
                Mat hierarchy = new Mat();
                Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

                // 机柜检测规则
                for (MatOfPoint contour : contours) {
                    Rect boundRect = Imgproc.boundingRect(contour);
                    double width = boundRect.width;
                    double height = boundRect.height;
                    Point tl = new Point(boundRect.tl().x + x, boundRect.tl().y + y);
                    Point br = new Point(boundRect.br().x + x, boundRect.br().y + y);

                    // 第一类机柜(红色)
                    // test1: 68 37
                    // test2: 216 116
                    // test2: 58 31
                    // test3: 267 155
                    if (Math.abs(width - 58) < 3 && Math.abs(height - 31) < 3) {
                        String info = "width = " + width + "height = " + height;
                        if (!isDuplicate(tl.x, tl.y, points)) {
                            red.put(tl, info);
                            points.add(tl);
                            Imgproc.rectangle(result, tl, br, new Scalar(0, 0, 255), 2);
                        }
                    }
                    // 第二类机柜(蓝色)
                    // test1: 67 49
                    // test2: 216 156
                    // test2: 58 41
                    else if (Math.abs(width - 58) < 3 && Math.abs(height - 41) < 3) {
                        String info = "width = " + width + "height = " + height;
                        if (!isDuplicate(tl.x, tl.y, points)) {
                            blue.put(tl, info);
                            points.add(tl);
                            Imgproc.rectangle(result, tl, br, new Scalar(255, 0, 0), 2);
                        }
                    }
                    // 墙体(绿色)
                    // test2: 54 54
                    else if (Math.abs(width - 54) < 3 && Math.abs(height - 54) < 3) {
                        String info = "width = " + width + "height = " + height;
                        if (!isDuplicate(tl.x, tl.y, points)) {
                            green.put(tl, info);
                            points.add(tl);
                            Imgproc.rectangle(result, tl, br, new Scalar(0, 255, 0), 2);
                        }
                    }
//                    Imgproc.rectangle(result, tl, br, new Scalar(0, 255, 0), 1);
                    contour.release();
                }
                gray.release();
                threshold.release();
                dst.release();
                edges.release();
                hierarchy.release();
                tile.release();
            }
        }


        Imgcodecs.imwrite("result.png", result);

        for (Point key : red.keySet()) {
            System.out.println("key = " + key);
            System.out.println("red.get(key) = " + red.get(key));
        }
        for (Point key : blue.keySet()) {
            System.out.println("key = " + key);
            System.out.println("blue.get(key) = " + blue.get(key));
        }
        for (Point key : green.keySet()) {
            System.out.println("key = " + key);
            System.out.println("green.get(key) = " + green.get(key));
        }


        System.out.println("red.size() = " + red.size());
        System.out.println("blue.size() = " + blue.size());
        System.out.println("green.size() = " + green.size());

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
