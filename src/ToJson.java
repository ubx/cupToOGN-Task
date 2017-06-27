import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by andreas on 27.06.17.
 */
public class ToJson {

    public static void output(List<Task> tasks, String ognfile) throws IOException {
        JSONObject jsonObj = new JSONObject();
        for (Task task : tasks) {
            String taskName = task.getName();
            List<WaypointWithObsZone> taskWaypoints = task.getWaypoints();
            JSONArray legs = new JSONArray();
            int wpNum = 0;
            int starLine = 0;
            int finishLine = 0;
            for (WaypointWithObsZone taskWaypoint : taskWaypoints) {
                legs.put((new JSONArray()).put(taskWaypoint.getLat()).put(taskWaypoint.getLon()));
                if (wpNum < taskWaypoints.size()) {
                    if (taskWaypoint.getObsZone() == null) {
                        legs.put((new JSONArray()).put(500));
                    } else {
                        final int r1 = taskWaypoint.getObsZone().getR1();
                        if (wpNum == 0 & taskWaypoint.getObsZone().isStartLine()) {
                            starLine = r1 * 2;
                        } else if (wpNum == taskWaypoints.size() - 1 & taskWaypoint.getObsZone().isFinishLine()) {
                            finishLine = r1 * 2;
                        } else {
                            legs.put((new JSONArray()).put(r1));
                        }
                    }
                }
                wpNum++;
            }
            JSONObject jsonObj2 = new JSONObject()
                    .put("legs", legs)
                    .put("name", taskName)
                    .put("color", Colors.getNextColor());
            if (starLine > 0) {
                jsonObj2.put("startline", starLine);
            }
            if (finishLine > 0) {
                jsonObj2.put("finishline", finishLine);
            }
            jsonObj.append("tasks", jsonObj2);
        }

        if (ognfile == null) {
            System.out.println(jsonObj.toString(2));
        } else {
            FileWriter file = new FileWriter(ognfile);
            //file.write(jsonObj.toString(2));
            jsonObj.write(file);
            file.close();
        }
    }

}
