import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class ImageZoomPanel extends JPanel {
    private BufferedImage image;
    private double scale = 1.0; // 当前缩放比例
    private int translateX = 0, translateY = 0; // 平移偏移量
    private int lastX, lastY; // 上一次鼠标位置

    public ImageZoomPanel(BufferedImage img) {
        this.image = img;

        // 鼠标滚轮缩放
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double delta = 0.1f * e.getPreciseWheelRotation();
                scale -= delta;
                scale = Math.max(0.1, Math.min(scale, 10)); // 限制缩放范围
                repaint();
            }
        });

        // 鼠标拖拽平移
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastX = e.getX();
                lastY = e.getY();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int dx = e.getX() - lastX;
                int dy = e.getY() - lastY;
                translateX += dx;
                translateY += dy;
                lastX = e.getX();
                lastY = e.getY();
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.translate(translateX, translateY);
            g2d.scale(scale, scale);
            g2d.drawImage(image, 0, 0, this);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (image != null) {
            return new Dimension((int) (image.getWidth() * scale), (int) (image.getHeight() * scale));
        }
        return super.getPreferredSize();
    }

}
