import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;

/*
环境依赖：
opencv      4.12.0
sdk         corretto-17
 */

public class Main {

    public static void main(String[] args) {
        System.load("/usr/local/share/java/opencv4/libopencv_java4120.so");
//        System.load("C:\\Users\\Leon\\Desktop\\opencv4.12.0\\build\\java\\x64\\opencv_java4120.dll");


        // 读取待检测图片
        Mat image = Imgcodecs.imread("/home/nvisual/nVisual/test/test.png");
//        Mat image = Imgcodecs.imread("C:\\Users\\Leon\\Desktop\\nVisual\\test\\test.png");

        // 模板图片（应该允许有多个模板图片）
        Mat template1 = Imgcodecs.imread("/home/nvisual/nVisual/test/template.png");
        Mat template2 = Imgcodecs.imread("/home/nvisual/nVisual/test/template_2.png");
        Mat template3 = Imgcodecs.imread("/home/nvisual/nVisual/test/template_3.png");
//        Mat template1 = Imgcodecs.imread("C:\\Users\\Leon\\Desktop\\nVisual\\test\\template.png");
//        Mat template2 = Imgcodecs.imread("C:\\Users\\Leon\\Desktop\\nVisual\\test\\template_2.png");
//        Mat template3 = Imgcodecs.imread("C:\\Users\\Leon\\Desktop\\nVisual\\test\\template_3.png");
        // 对模板图片进行预处理
        TemplateMatch templateMatch1 = new TemplateMatch(image.clone(), template1);
        templateMatch1.pre();
        TemplateMatch templateMatch2 = new TemplateMatch(image.clone(), template2);
        templateMatch2.pre();
        TemplateMatch templateMatch3 = new TemplateMatch(image.clone(), template3);
        templateMatch3.pre();

        templateMatch1.setConfidence(0.5);
        templateMatch2.setConfidence(0.5);
        templateMatch3.setConfidence(0.5);

        HashSet<Rect> hashSet1 = templateMatch1.match();
        HashSet<Rect> hashSet2 = templateMatch2.match();
        HashSet<Rect> hashSet3 = templateMatch3.match();

        System.out.println(hashSet1.size());
        System.out.println(hashSet2.size());
        System.out.println(hashSet3.size());

    }

}
