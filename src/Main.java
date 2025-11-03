/*
                        环境依赖
"C:\Users\Leon\Desktop\opencv\build\java\opencv-480.jar"
"C:\Users\Leon\Desktop\opencv\build\java\x64\opencv_java480.dll"
 */

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        System.load("C:\\Users\\Leon\\Desktop\\opencv\\build\\java\\x64\\opencv_java480.dll");

        Mat imread = Imgcodecs.imread("C:\\Users\\Leon\\Desktop\\nVisual\\test_small.png");
        Point point = new Point();
        Mat submat = imread.submat(100, 300, 100, 300);
        submat.locateROI(imread.size(), point);
        System.out.println("point = " + point);

        Imgproc.rectangle(imread, new Rect((int) point.x, (int) point.y, submat.width(), submat.height()), new Scalar(255, 0, 0));
        HighGui.imshow("submat", submat);

        HighGui.imshow("imread", imread);
        HighGui.waitKey();
    }
}