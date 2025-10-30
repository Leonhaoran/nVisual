/*
                        环境依赖
"C:\Users\Leon\Desktop\opencv\build\java\opencv-480.jar"
"C:\Users\Leon\Desktop\opencv\build\java\x64\opencv_java480.dll"
 */

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Main {
    public static void main(String[] args) {
        System.load("C:\\Users\\Leon\\Desktop\\opencv\\build\\java\\x64\\opencv_java480.dll");

        Mat mat = new Mat(200, 200, CvType.CV_8UC3, new Scalar(255, 0, 0));
        HighGui.imshow("mat", mat);
        HighGui.waitKey();
    }
}