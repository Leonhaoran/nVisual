import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.load("C:\\Users\\Leon\\Desktop\\opencv\\build\\java\\x64\\opencv_java480.dll");

        Mat image = Imgcodecs.imread("C:\\Users\\Leon\\Desktop\\nVisual\\test_micro.png");

        // 转换为灰度图
        Mat gray = new Mat();
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);

//        HighGui.imshow("gray", gray);

        // 图像反向二值化，去除噪点
        Mat threshold = new Mat();
        Imgproc.threshold(gray, threshold, 0, 255, Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);
//        HighGui.imshow("threshold", threshold);

        // 像素点加粗，让矩形闭合
        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
        Mat dst = new Mat();
        Imgproc.morphologyEx(threshold, dst, Imgproc.MORPH_GRADIENT, element);
//        HighGui.imshow("dst", dst);

        // 使用Canny算法检测图像的边缘
        Mat edges = new Mat();
        Imgproc.Canny(dst, edges, 50, 150);

        // 找轮廓
        List<MatOfPoint> contours = new LinkedList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        int total = 0;
        for (MatOfPoint contour : contours) {
            Rect boundRect = Imgproc.boundingRect(contour);
//            System.out.println(Math.abs((double) boundRect.width / boundRect.height - 2.0));
            if (Math.abs((double) boundRect.width / boundRect.height - 2.0) < 0.05){
                total++;
                System.out.println(total);
                System.out.println("boundRect.tl() = " + boundRect.tl());
                System.out.println("boundRect.br() = " + boundRect.br());
                System.out.println();
            }

            Imgproc.rectangle(image, boundRect.tl(), boundRect.br(), new Scalar(0, 0, 255), 2);
        }

        Imgcodecs.imwrite("result.png", image);
        HighGui.imshow("result", image);
        HighGui.waitKey();
    }
}