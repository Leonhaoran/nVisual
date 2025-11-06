import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.*;

// 机柜：600*1200 600*1100 600*1000 800*1200 800*1100 800*1000

public class Main {
    public static void main(String[] args) {
        System.load("C:\\Users\\Leon\\Desktop\\opencv4.12.0\\build\\java\\x64\\opencv_java4120.dll");
        Mat image = Imgcodecs.imread("C:\\Users\\Leon\\Desktop\\nVisual\\test2.png");
        // 方法1：模板匹配
        // 1.1 直接从原图中截取模板，检测效果不精确，会因为截图影响检测结果
        // 1.2 手动构造一个模板，trying...
//        Mat templateImage = Imgcodecs.imread("C:\\Users\\Leon\\Desktop\\nVisual\\template.png");
//        System.out.println(templateImage);
//        System.out.println("templateImage.dump() = \n" + templateImage.dump());

//        Mat templateImage = Mat.zeros(49, 71, CvType.CV_8UC1);
//        Imgproc.rectangle(templateImage, new Point(0, 0), new Point(templateImage.width() - 1, templateImage.height() - 1), new Scalar(255, 255, 255), 1);
//        Template template = new Template(image, templateImage);
//        template.match();


        // 方法2：Canny边缘检测
        Canny canny = new Canny(image, 500, 500, 100);
        canny.match();

    }
}

