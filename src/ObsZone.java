/**
 * Created by andreas on 05.06.17.
 */
public class ObsZone {

    // Style = Direction. 0 - Fixed value, 1 - Symmetrical, 2 - To next point, 3 - To previous point, 4 - To start point
    // We use only 1: circle, 2: start line and 3: finish line
    private int style = -1;

    // Circle (radius) or Line (half length) in meter
    private int r1 = -1;

    public static ObsZone createObsZone(String... items) {
        if (items.length < 3) {
            return null;
        }
        if (!items[0].startsWith("ObsZone")) {
            return null;
        }

        int r1 = -1;
        int style = -1;
        // parse this: "ObsZone=1,Style=1,SpeedStyle=3,R1=30000m,A1=180,Reduce=1,AAT=1"
        for (String item : items) {
            String[] keyVal = item.split("=");
            switch (keyVal[0]) {
                case "Style": {
                    style = Integer.decode(keyVal[1]);
                    break;
                }
                case "R1": {
                    r1 = Integer.decode(keyVal[1].substring(0, keyVal[1].length() - 1));
                    break;
                }
                default: {
                    break;
                }
            }
        }
        if (style == -1 | r1 == -1) {
            return null;
        } else {
            return new ObsZone(style, r1);
        }
    }

    public ObsZone(int style, int r1) {
        this.style = style;
        this.r1 = r1;
    }

    public int getR1() {
        return r1;
    }

    public boolean isStartLine() {
        return style == 2 & r1 > 0;
    }

    public boolean isFinishLine() {
        return style == 3 & r1 > 0;
    }
}
