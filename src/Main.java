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

//        Mat mat1 = new Mat(3, 3, CvType.CV_8UC1);
//        mat1.put(0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
//        Mat mat2 = new Mat(3, 3, CvType.CV_8UC1);
//        mat2.put(0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
//        Mat dst = new Mat();
//        Core.add(mat1, mat2, dst);
//        System.out.println("dst.dump() = \n" + dst.dump());


//        Mat diff = new Mat();
//        Core.absdiff(mat2, mat1, diff);
//        System.out.println("diff.dump() = \n" + diff.dump());

        Mat imread1 = Imgcodecs.imread("C:\\Users\\Leon\\Desktop\\dog.png");
        Mat imread2 = Imgcodecs.imread("C:\\Users\\Leon\\Desktop\\jail.jpg");
        Mat dst = new Mat();
        Imgproc.resize(imread1, imread1, imread2.size());
        System.out.println("imread1 = " + imread1);
        System.out.println("imread2 = " + imread2);
        Core.addWeighted(imread1, 0.5, imread2, 0.5, -50, dst);
        Imgcodecs.imwrite("result.png", dst);
        HighGui.imshow("dst", dst);


        HighGui.waitKey();

    }
}