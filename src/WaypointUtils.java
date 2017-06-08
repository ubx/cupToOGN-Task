import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by andreas on 08.06.17.
 */
public class WaypointUtils {

    private static DecimalFormat dfLat = new DecimalFormat("##.00000");
    private static DecimalFormat dfLon = new DecimalFormat("###.00000");
    private static List<Waypoint> pWps = new ArrayList<>();

    public static void checkForDuplicate(HashMap<String, Waypoint> waipoints) {
        for (Map.Entry<String, Waypoint> entry1 : waipoints.entrySet()) {
            for (Map.Entry<String, Waypoint> entry2 : waipoints.entrySet()) {
                if (entry1 == entry2) {
                    continue;
                }
                if (pWps.contains(entry1.getValue()) | pWps.contains(entry2.getValue())) {
                    continue;
                }
                if (entry1.getValue().getLat() == entry2.getValue().getLat()
                        & entry1.getValue().getLon() == entry2.getValue().getLon()) {
                    System.out.println("Duplicate: Name=" + entry1.getKey().replaceAll("\"","") + "/" + entry2.getKey().replaceAll("\"","")
                            + ", Lat/Lon=" + dfLat.format(entry1.getValue().getLat()) + "/" + dfLon.format(entry1.getValue().getLon()));
                    pWps.add(entry1.getValue());
                }

            }

        }
    }
}

