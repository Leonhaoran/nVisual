import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

// 机柜：600*1200 600*1100 600*1000 800*1200 800*1100 800*1000

public class Main {
    public static void main(String[] args) {
        System.load("C:\\Users\\Leon\\Desktop\\opencv4.12.0\\build\\java\\x64\\opencv_java4120.dll");
        Mat image = Imgcodecs.imread("C:\\Users\\Leon\\Desktop\\nVisual\\test2.png");
        // 方法1：模板匹配
        // 直接从原图中截取模板，检测效果不精确，会因为截图影响检测结果
        List<Mat> mats = new ArrayList<>();

        Mat templateImage1 = Imgcodecs.imread("C:\\Users\\Leon\\Desktop\\nVisual\\template2.png");
        Mat templateImage2 = Imgcodecs.imread("C:\\Users\\Leon\\Desktop\\nVisual\\template2_2.png");
        Mat templateImage3 = Imgcodecs.imread("C:\\Users\\Leon\\Desktop\\nVisual\\template2_3.png");
        // TODO 可以优先检测较大的模板，可能效果会更好，有待验证
//        mats.add(templateImage1);
//        mats.add(templateImage2);
        mats.add(templateImage3);

        List<Scalar> scalars = new ArrayList<>();
        scalars.add(new Scalar(255, 0, 0));
        scalars.add(new Scalar(0, 255, 0));
        scalars.add(new Scalar(0, 0, 255));

        Template template = new Template(image, mats, scalars);
        template.match();


        // 方法2：Canny边缘检测
//        Canny canny = new Canny(image, 500, 500, 100);
//        canny.match();

        File imageFile = new File("result.png");
        try {
            Desktop.getDesktop().open(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

