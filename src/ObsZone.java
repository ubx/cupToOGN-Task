/**
 * Created by andreas on 05.06.17.
 */
public class ObsZone {

    // ObsZone=0,Style=2,R1=10000m,A1=180,Reduce=1

    private int num;
    private int style;
    private int r1;

    public static ObsZone createObsZone(String... items) {
        if (items.length < 5) {
            return null;
        }
        int num = Integer.getInteger(items[0].split("=")[1]);
        int style = Integer.getInteger(items[1].split("=")[1]);
        String r1Str = items[2].split("=")[1];
        int r1 = Integer.getInteger(r1Str.substring(0, r1Str.length() - 1));
        return new ObsZone(num, style, r1);
    }

    public ObsZone(int num, int style, int r1) {
        this.num = num;
        this.style = style;
        this.r1 = r1;
    }
}
