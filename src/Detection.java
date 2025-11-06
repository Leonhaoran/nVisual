import org.opencv.core.Point;

import java.util.HashSet;

public class Detection {

    public static boolean isDuplicate(double x, double y, HashSet<Point> points) {
        for (Point point : points) {
            double dx = point.x - x;
            double dy = point.y - y;
            if (dx * dx + dy * dy < 250) {
                return true;
            }
        }
        return false;
    }
}
