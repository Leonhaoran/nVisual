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

        double[] vals = {1, 2, 3, 4, 5, 6, 7, 8};
        Rect rect = new Rect(2, 5, 3, 4);
        System.out.println("rect = " + rect);
        System.out.println("rect.tl() = " + rect.tl());
        System.out.println("rect.br() = " + rect.br());
        System.out.println("rect.contains(new Point(5,9)) = " + rect.contains(new Point(4.9, 8.9)));
    }
}