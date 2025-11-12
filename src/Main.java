import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.load("C:\\Users\\Leon\\Desktop\\opencv4.12.0\\build\\java\\x64\\opencv_java4120.dll");

        // 读取图片
        Mat image = Imgcodecs.imread("C:\\Users\\Leon\\Desktop\\nVisual\\test.png");
        BufferedImage bufferedImage = (BufferedImage) HighGui.toBufferedImage(image);

        // 主窗口
        JFrame frame = new JFrame("图片放缩与平移 + 参数调节");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // 图片显示面板（自定义类）
        ImageZoomPanel panel = new ImageZoomPanel(bufferedImage);
        frame.add(panel, BorderLayout.CENTER);

        // ======= 右侧滑块区域 =======
        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.Y_AXIS)); // 垂直排列
        sliderPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        sliderPanel.setPreferredSize(new Dimension(300, 0));

        // 创建三个滑块
        JSlider slider1 = createSlider("置信度1", 0, 100, 50);
        JSlider slider2 = createSlider("置信度2", 0, 100, 50);
        JSlider slider3 = createSlider("置信度3", 0, 100, 50);

        // 将滑块加入面板
        sliderPanel.add(slider1);
        sliderPanel.add(Box.createVerticalStrut(20)); // 间距
        sliderPanel.add(slider2);
        sliderPanel.add(Box.createVerticalStrut(20));
        sliderPanel.add(slider3);

        // 将右侧面板放在 EAST
        frame.add(sliderPanel, BorderLayout.EAST);

        // 自适应屏幕
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);

        // 模板匹配部分
        Mat target = image.clone();

        Mat template1 = Imgcodecs.imread("C:\\Users\\Leon\\Desktop\\nVisual\\template.png");
        Mat template2 = Imgcodecs.imread("C:\\Users\\Leon\\Desktop\\nVisual\\template2.png");

        List<Mat> templates = new ArrayList<>();
        templates.add(template1);
        templates.add(template2);

        List<Scalar> scalars = new ArrayList<>();
        scalars.add(new Scalar(255, 0, 0));
        scalars.add(new Scalar(0, 255, 0));

        TemplateMatch templateMatch = new TemplateMatch(target, templates, scalars);
        templateMatch.match();

        // 监听滑块事件
        slider1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();

            }
        });
    }

    /**
     * 工具方法：创建带标签的滑块
     */
    private static JSlider createSlider(String label, int min, int max, int init) {
        JLabel lbl = new JLabel(label + ": " + init, SwingConstants.CENTER);
        JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, init);
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        slider.addChangeListener(e -> {
            lbl.setText(label + ": " + ((JSlider) e.getSource()).getValue());
        });

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BorderLayout());
        wrapper.add(lbl, BorderLayout.NORTH);
        wrapper.add(slider, BorderLayout.CENTER);

        return slider;
    }
}
