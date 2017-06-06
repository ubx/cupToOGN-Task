import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by andreas on 05.06.17.
 */
public class Task {

    private String name;
    public List<WaypointT> waypoints = new ArrayList<WaypointT>();

    static Task createTask(HashMap<String,Waypoint> waypoints, String... items) {
        Task task = new Task(items[0]);
        for (int i=1;i<items.length; i++){
            task.waypoints.add(new WaypointT(waypoints.get(items[i])));
        }
        return task;
    }

    public Task(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<WaypointT> getWaypoints() {
        return waypoints;
    }

    public void addObsZone(int num, ObsZone obsZone) {
        waypoints.get(num + 1).setObsZone(obsZone);
    }
}
