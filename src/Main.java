import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.*;

// 机柜：600*1200 600*1100 600*1000 800*1200 800*1100 800*1000

public class Main {
    public static void main(String[] args) {
        System.load("C:\\Users\\Leon\\Desktop\\opencv4.12.0\\build\\java\\x64\\opencv_java4120.dll");

        Mat image = Imgcodecs.imread("C:\\Users\\Leon\\Desktop\\nVisual\\test.png");
        Mat templateImage = Imgcodecs.imread("C:\\Users\\Leon\\Desktop\\nVisual\\template.png");

        Template template = new Template(image, templateImage);
        List<Rect> list = template.match();
        template.displayMatches(list);

        /*

        int tileWidth = 500;
        int tileHeight = 500;
        int overlap = 100;
        int imageWidth = image.cols();
        int imageHeight = image.rows();
        HashSet<Point> points = new HashSet<>();
        Map<Point, String> red = new HashMap<>();
        Map<Point, String> blue = new HashMap<>();

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

                // 形态学操作
                Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2, 2));
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

                    // test1: 68 37
                    // test2: 238 117
                    // test3: 37 21
                    if (Math.abs(width - 68) < 3 && Math.abs(height - 37) < 3) {
                        String info = "width = " + width + "height = " + height;
                        if (!Detection.isDuplicate(tl.x, tl.y, points)) {
                            red.put(tl, info);
                            points.add(tl);
                            Imgproc.rectangle(result, tl, br, new Scalar(0, 0, 255), 2);
                        }
                    } else if (Math.abs(width - 67) < 3 && Math.abs(height - 49) < 3) {
                        String info = "width = " + width + "height = " + height;
                        if (!Detection.isDuplicate(tl.x, tl.y, points)) {
                            blue.put(tl, info);
                            points.add(tl);
                            Imgproc.rectangle(result, tl, br, new Scalar(255, 0, 0), 2);
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

//        for (Point key : red.keySet()){
//            System.out.println("key = " + key);
//            System.out.println("red.get(key) = " + red.get(key));
//        }
//
//        for (Point key : blue.keySet()){
//            System.out.println("key = " + key);
//            System.out.println("blue.get(key) = " + blue.get(key));
//        }
        System.out.println("red.size() = " + red.size());
        System.out.println("blue.size() = " + blue.size());
//        HighGui.imshow("result", result);
//        HighGui.waitKey();


         */
    }
}


