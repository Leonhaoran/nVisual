/*
                        环境依赖
"C:\Users\Leon\Desktop\opencv\build\java\opencv-480.jar"
"C:\Users\Leon\Desktop\opencv\build\java\x64\opencv_java480.dll"
 */

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

import java.awt.*;
import java.io.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.load("C:\\Users\\Leon\\Desktop\\opencv\\build\\java\\x64\\opencv_java480.dll");

        Mat mat = Imgcodecs.imread("C:\\Users\\Leon\\Desktop\\nVisual\\test.png");
        System.out.println(mat);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("output.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            for (int i = 0; i < mat.rows(); i ++){
                for (int j = 0; j < mat.cols(); j ++){
                    String string = Arrays.toString(mat.get(i,j));
                    bufferedWriter.write(string);
                    System.out.println(string);
                }
            }
            bufferedWriter.close();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }



    }
}