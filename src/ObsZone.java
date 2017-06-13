/**
 * Created by andreas on 05.06.17.
 */
public class ObsZone {

    // ObsZone=0,Style=2,R1=10000m,A1=180,Reduce=1

    private int style;
    private int r1;

    public static ObsZone createObsZone(String... items) {
        if (items.length < 5) {
            return null;
        }
        int style = Integer.decode(items[1].split("=")[1]);
        // todo -- pick only 30000 from line "ObsZone=1,Style=1,SpeedStyle=3,R1=30000m,A1=180,Reduce=1,AAT=1"
        String r1Str = items[3].split("=")[1];
        int r1 = Integer.decode(r1Str.substring(0, r1Str.length()-1));
        return new ObsZone(style, r1);
    }

    public ObsZone(int style, int r1) {
        this.style = style;
        this.r1 = r1;
    }

    public int getStyle() {
        return style;
    }

    public int getR1() {
        return r1;
    }
}
