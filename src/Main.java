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

//        Mat mat = new Mat(4, 4, CvType.CV_8UC1);
//        mat.put(0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
//        System.out.println("mat = " + mat);
//        System.out.println("mat.dump() = \n" + mat.dump());
//        Mat reshape = mat.reshape(3);
//        System.out.println("reshape = " + reshape);
//        System.out.println("reshape.dump() = \n" + reshape.dump());


        Mat imread = Imgcodecs.imread("C:\\Users\\Leon\\Desktop\\nVisual\\test_small.png");
        HighGui.imshow("imread", imread);
        Mat t = imread.t();
        HighGui.imshow("t", t);
        Mat reshape = imread.reshape(1);
        HighGui.imshow("reshape", reshape);
        HighGui.waitKey();

    }
}