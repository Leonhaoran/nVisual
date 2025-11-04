import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        System.load("C:\\Users\\Leon\\Desktop\\opencv\\build\\java\\x64\\opencv_java480.dll");

//        Mat image = Imgcodecs.imread("C:\\Users\\Leon\\Desktop\\nVisual\\test_micro.png");
//
//        // 转换为灰度图
//        Mat gray = new Mat();
//        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);
//
////        HighGui.imshow("gray", gray);
//
//        // 图像反向二值化，去除噪点
//        Mat threshold = new Mat();
//        Imgproc.threshold(gray, threshold, 0, 255, Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);
////        HighGui.imshow("threshold", threshold);
//
//        // 像素点加粗，让矩形闭合
//        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
//        Mat dst = new Mat();
//        Imgproc.morphologyEx(threshold, dst, Imgproc.MORPH_GRADIENT, element);
////        HighGui.imshow("dst", dst);
//
//        // 使用Canny算法检测图像的边缘
//        Mat edges = new Mat();
//        Imgproc.Canny(dst, edges, 50, 150);
//
//        // 找轮廓
//        List<MatOfPoint> contours = new LinkedList<>();
//        Mat hierarchy = new Mat();
//        Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
//
//        int total = 0;
//        for (MatOfPoint contour : contours) {
//            Rect boundRect = Imgproc.boundingRect(contour);
////            System.out.println(Math.abs((double) boundRect.width / boundRect.height - 2.0));
//            if (Math.abs((double) boundRect.width / boundRect.height - 2.0) < 0.05){
//                total++;
//                System.out.println(total);
//                System.out.println("boundRect.tl() = " + boundRect.tl());
//                System.out.println("boundRect.br() = " + boundRect.br());
//                System.out.println();
//            }
//
//            Imgproc.rectangle(image, boundRect.tl(), boundRect.br(), new Scalar(0, 0, 255), 2);
//        }
//
//        Imgcodecs.imwrite("result.png", image);
//        HighGui.imshow("result", image);
//        HighGui.waitKey();

        Mat imread = Imgcodecs.imread("test_micro.png", Imgcodecs.IMREAD_GRAYSCALE);
        JFrame frame = HighGui.createJFrame("Canny", HighGui.WINDOW_AUTOSIZE);

        JLabel imageLabel = new JLabel();
        Image image = HighGui.toBufferedImage(imread);
        imageLabel.setIcon(new ImageIcon(image));
        frame.add(imageLabel, BorderLayout.CENTER);

        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS));
        final double[] threshold1 = {0};
        final double[] threshold2 = {0};

        Mat edges = new Mat();
        JLabel label = new JLabel("threshold1:");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        JSlider slider = new JSlider(0, 255, 0);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setMinorTickSpacing(5);
        slider.setMajorTickSpacing(51);

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                threshold1[0] = source.getValue();
                Imgproc.Canny(imread, edges, threshold1[0], threshold2[0], 3);
                Image image1 = HighGui.toBufferedImage(edges);
                label.setText("threshold1: " + threshold1[0]);
                imageLabel.setIcon(new ImageIcon(image1));
                frame.repaint();
            }
        });
        JLabel label2 = new JLabel("threshold2:");
        label2.setAlignmentX(Component.CENTER_ALIGNMENT);
        JSlider slider2 = new JSlider(0, 255, 0);
        slider2.setPaintTicks(true);
        slider2.setPaintLabels(true);
        slider2.setMinorTickSpacing(5);
        slider2.setMajorTickSpacing(51);

        slider2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                threshold2[0] = source.getValue();
                Imgproc.Canny(imread, edges, threshold1[0], threshold2[0], 3);
                Image image1 = HighGui.toBufferedImage(edges);
                label2.setText("threshold2: " + threshold2[0]);
                imageLabel.setIcon(new ImageIcon(image1));
                frame.repaint();
            }
        });
        sliderPanel.add(label);
        sliderPanel.add(slider);
        sliderPanel.add(label2);
        sliderPanel.add(slider2);

        // 将滑块面板添加到 JFrame
        frame.add(sliderPanel, BorderLayout.EAST);

        // 设置 JFrame 可见
        frame.pack();
        frame.setVisible(true);

    }
}