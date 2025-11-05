import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.LinkedList;
import java.util.List;

// 机柜：600*1200 600*1100 600*1000 800*1200 800*1100 800*1000
// width / height = 1.83
public class Main {
    public static void main(String[] args) {
        System.load("C:\\Users\\Leon\\Desktop\\opencv\\build\\java\\x64\\opencv_java480.dll");

        Mat image = Imgcodecs.imread("C:\\Users\\Leon\\Desktop\\nVisual\\test_middle.png");

        int tileWidth = 1000;
        int tileHeight = 1000;
        int imageWidth = image.cols();
        int imageHeight = image.rows();

        Mat result = image.clone();
        int total = 0;

        for (int x = 0; x < imageWidth; x += tileWidth) {
            for (int y = 0; y < imageHeight; y += tileHeight) {
                int w = Math.min(tileWidth, imageWidth - x);
                int h = Math.min(tileHeight, imageHeight - y);

                Rect roi = new Rect(x, y, w, h);
                Mat tile = new Mat(image, roi);

                // 转换为灰度图
                Mat gray = new Mat();
                Imgproc.cvtColor(tile, gray, Imgproc.COLOR_BGR2GRAY);

                // 二值化
                Mat threshold = new Mat();
                Imgproc.threshold(gray, threshold, 0, 255, Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);

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
                    if (Math.abs(width / height - 1.83) < 0.5 && Math.abs(width - 68) < 3 && Math.abs(height - 37) < 3) {
                        total++;
                        System.out.println(total);
                        System.out.println("Red");
                        System.out.println("width = " + width);
                        System.out.println("height = " + height);
                        System.out.println("boundRect.tl() = " + boundRect.tl());
                        System.out.println("boundRect.br() = " + boundRect.br());
                        System.out.println();
                        Imgproc.rectangle(result, new Point(boundRect.tl().x + x, boundRect.tl().y + y), new Point(boundRect.br().x + x, boundRect.br().y + y), new Scalar(0, 0, 255), 2);
                    } else if (Math.abs(width / height - 1.36) < 0.5 && Math.abs(width - 67) < 3 && Math.abs(height - 49) < 3) {
                        total++;
                        System.out.println(total);
                        System.out.println("Blue");
                        System.out.println("width = " + width);
                        System.out.println("height = " + height);
                        System.out.println("boundRect.tl() = " + boundRect.tl());
                        System.out.println("boundRect.br() = " + boundRect.br());
                        System.out.println();
                        Imgproc.rectangle(result, new Point(boundRect.tl().x + x, boundRect.tl().y + y), new Point(boundRect.br().x + x, boundRect.br().y + y), new Scalar(255, 0, 0), 2);
                    }
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
        HighGui.imshow("result", result);
        HighGui.waitKey();
    }
}


