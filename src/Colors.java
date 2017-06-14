/**
 * Created by andreas on 14.06.17.
 */
public class Colors {

    private static int num = 0;
    private static String[] colors = {"FF0000", "000000", "0000FF", "00FF00", "008080", "FF00FF"};


    public Colors() {
    }

    public static String getNextColor() {
        return colors[num++ % colors.length];
    }
}
