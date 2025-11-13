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
        System.load("C:\\Users\\Leon\\Desktop\\opencv4.12.0\\build\\java\\x64\\opencv_java4120.dll");

        // 读取图片
        Mat image = Imgcodecs.imread("C:\\Users\\Leon\\Desktop\\nVisual\\test3\\test3.png");
        BufferedImage bufferedImage = (BufferedImage) HighGui.toBufferedImage(image);
        Mat template1 = Imgcodecs.imread("C:\\Users\\Leon\\Desktop\\nVisual\\test3\\template3.png");
        Mat template2 = Imgcodecs.imread("C:\\Users\\Leon\\Desktop\\nVisual\\test3\\template3_2.png");
        Mat template3 = Imgcodecs.imread("C:\\Users\\Leon\\Desktop\\nVisual\\test3\\template3_3.png");
        Mat copy = image.clone();

        // 主窗口
        JFrame frame = new JFrame("图片显示 + 参数输入面板");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // 图片显示面板（支持缩放和平移）
        ImageZoomPanel imageZoomPanel = new ImageZoomPanel(bufferedImage);
        frame.add(imageZoomPanel, BorderLayout.CENTER);

        // ======= 右侧参数输入面板 =======
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        rightPanel.setPreferredSize(new Dimension(300, 0));

        // 对图片进行预处理
        TemplateMatch templateMatch1 = new TemplateMatch(image.clone(), template1);
        templateMatch1.pre();
        TemplateMatch templateMatch2 = new TemplateMatch(image.clone(), template2);
        templateMatch2.pre();
        TemplateMatch templateMatch3 = new TemplateMatch(image.clone(), template3);
        templateMatch3.pre();

        final HashSet<Rect>[] hashSet1 = new HashSet[]{new HashSet<>()};
        final HashSet<Rect>[] hashSet2 = new HashSet[]{new HashSet<>()};
        final HashSet<Rect>[] hashSet3 = new HashSet[]{new HashSet<>()};
        // 添加三个输入模块
        rightPanel.add(createInputPanel("置信度1", new InputCallback() {
            @Override
            public void onConfirm(String text) {
                templateMatch1.setConfidence(Integer.parseInt(text) / 100.0);
                hashSet1[0] = templateMatch1.match();

                Mat display = copy.clone();
                for (Rect rect : hashSet1[0]) {
                    Imgproc.rectangle(display, rect.tl(), rect.br(), new Scalar(255, 0, 0), 2);
                }
                for (Rect rect : hashSet2[0]) {
                    Imgproc.rectangle(display, rect.tl(), rect.br(), new Scalar(0, 255, 0), 2);
                }
                for (Rect rect : hashSet3[0]) {
                    Imgproc.rectangle(display, rect.tl(), rect.br(), new Scalar(0, 0, 255), 2);
                }


                imageZoomPanel.setImage((BufferedImage) HighGui.toBufferedImage(display));
                frame.repaint();
            }
        }));
        rightPanel.add(Box.createVerticalStrut(20));

        rightPanel.add(createInputPanel("置信度2", new InputCallback() {
            @Override
            public void onConfirm(String text) {
                templateMatch2.setConfidence(Integer.parseInt(text) / 100.0);
                hashSet2[0] = templateMatch2.match();

                Mat display = copy.clone();
                for (Rect rect : hashSet1[0]) {
                    Imgproc.rectangle(display, rect.tl(), rect.br(), new Scalar(255, 0, 0), 2);
                }
                for (Rect rect : hashSet2[0]) {
                    Imgproc.rectangle(display, rect.tl(), rect.br(), new Scalar(0, 255, 0), 2);
                }
                for (Rect rect : hashSet3[0]) {
                    Imgproc.rectangle(display, rect.tl(), rect.br(), new Scalar(0, 0, 255), 2);
                }


                imageZoomPanel.setImage((BufferedImage) HighGui.toBufferedImage(display));
                frame.repaint();
            }
        }));
        rightPanel.add(Box.createVerticalStrut(20));

        rightPanel.add(createInputPanel("置信度3", new InputCallback() {
            @Override
            public void onConfirm(String text) {
                templateMatch3.setConfidence(Integer.parseInt(text) / 100.0);
                hashSet3[0] = templateMatch3.match();

                Mat display = copy.clone();
                for (Rect rect : hashSet1[0]) {
                    Imgproc.rectangle(display, rect.tl(), rect.br(), new Scalar(255, 0, 0), 2);
                }
                for (Rect rect : hashSet2[0]) {
                    Imgproc.rectangle(display, rect.tl(), rect.br(), new Scalar(0, 255, 0), 2);
                }
                for (Rect rect : hashSet3[0]) {
                    Imgproc.rectangle(display, rect.tl(), rect.br(), new Scalar(0, 0, 255), 2);
                }


                imageZoomPanel.setImage((BufferedImage) HighGui.toBufferedImage(display));
                frame.repaint();
            }
        }));

        frame.add(rightPanel, BorderLayout.EAST);

        // 全屏显示
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screen);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    /**
     * 创建单个输入模块（带标签、文本框、确认按钮）
     */
    private static JPanel createInputPanel(final String labelText, final InputCallback callback) {
        final JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(5, 5));

        final JLabel label = new JLabel(labelText + ":");
        final JTextField textField = new JTextField();
        final JButton button = new JButton("确认");

        // 点击按钮时触发回调
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textField.getText().trim();
                callback.onConfirm(text);
            }
        });

        // 回车键触发
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textField.getText().trim();
                callback.onConfirm(text);
            }
        });

        panel.add(label, BorderLayout.NORTH);
        panel.add(textField, BorderLayout.CENTER);
        panel.add(button, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * 简单接口，用于处理确认事件
     */
    public interface InputCallback {
        void onConfirm(String text);
    }
}
