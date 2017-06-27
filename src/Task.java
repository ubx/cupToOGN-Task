import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by andreas on 05.06.17.
 */
public class Task {

    private String name;
    public List<WaypointWithObsZone> waypoints = new ArrayList<WaypointWithObsZone>();

    static Task createTask(HashMap<String, Waypoint> waypoints, String... items) {
        Task task = new Task(items[0].length() == 0 ? createTaskName(items) : items[0].replaceAll("\"",""));
        for (int i = 1; i < items.length; i++) {
            String item = items[i].replaceAll("\"","");
            if (!item.equals("???")) {
                Waypoint waypoint = waypoints.get(item);
                if (waypoint == null) {
                    return null;
                }
                task.waypoints.add(new WaypointWithObsZone(waypoint));
            }
        }
        return task;
    }

    public Task(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<WaypointWithObsZone> getWaypoints() {
        return waypoints;
    }

    public void addObsZone(int num, ObsZone obsZone) {
        if (num < waypoints.size()) {
            waypoints.get(num).setObsZone(obsZone);
        }
    }

    private static String createTaskName(String... items) {
        StringBuffer tn = new StringBuffer();
        for (int i = 2; i < items.length - 1; i++) {
            tn.append(items[i] + "-");
        }
        if (tn.length() == 0) return "Unknown";
        tn.setLength(tn.length() - 1);
        return tn.toString().replaceAll("\"", "");
    }
}
