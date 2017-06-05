import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


/**
 * Created by andreas on 05.06.17.
 */
public class CupToOgnTask {

    private static DecimalFormat dfLat = new DecimalFormat("##.00000");
    private static DecimalFormat dfLon = new DecimalFormat("###.00000");


    private enum Mode {waypointheader, waypoint, taskheader, task}


    public static void main(String args[]) throws FileNotFoundException {

        //Creating Scanner instnace to read File in Java
        Scanner scanner = new Scanner(new File("testdata/test.cup"));

        Mode mode = Mode.waypointheader;

        HashMap<String, Waypoint> waipoints = new HashMap<String, Waypoint>();
        List<Task> tasks = new ArrayList<Task>();

        //Reading each line of file using Scanner class
        int lineNumber = 1;
        while (scanner.hasNextLine()) {
            Task lastTask;
            String line = scanner.nextLine();
            if (line.contains("-----Related Tasks-----")) {
                mode = Mode.taskheader;
            }
            switch (mode) {
                case waypointheader:
                    mode = Mode.waypoint;
                    break;

                case waypoint:
                    System.out.println("Waypoint line " + lineNumber + " : " + line);
                    String[] items = line.split(",");
                    Waypoint wp = Waypoint.createWaypoint(items);
                    if (wp == null) {
                        System.out.println("line skiped" + lineNumber + " : " + line);
                        break;
                    }
                    waipoints.put(wp.getName(), wp);
                    break;

                case taskheader:
                    mode = Mode.task;
                    break;

                case task:
                    System.out.println("Task line " + lineNumber + " : " + line);
                    if (line.startsWith("Options") | line.startsWith("ObsZone")) {
                        // todo - implement Options and ObsZone
                        break;
                    }
                    String[] items2 = line.split(",");
                    lastTask = Task.createTask(waipoints, items2);
                    tasks.add(lastTask);
                    System.out.println(tasks);
                    break;
            }
            lineNumber++;
        }
        System.out.println(tasks);

        // to json
        /*
        {"tasks": [
	        {"name": "Mixed-18m",
	         "color": "CC3333",
	         "legs": [
			          [46.807, 6.630],
			          [46.563, 6.559],[500],
			          [46.953, 7.262],[500],
			          [46.537, 6.333],[500],
			          [46.976, 6.810],[500],
			          [46.728, 6.569],[500],
			          [46.758, 6.609],[500]
		             ]
	        }
                  ]
         }
         */

        for (Task task : tasks) {
            System.out.println(task.getName());
            List<WaypointT> waypoints = task.getWaypoints();
            JSONArray legs = new JSONArray();

            for (Waypoint waypoint : waypoints) {
                JSONArray leg = new JSONArray();
                leg.put(dfLat.format(waypoint.getLat())).put(dfLon.format(waypoint.getLon()));
                legs.put(leg);
            }

            JSONObject obj = (new JSONObject())
                    .put("task", (new JSONArray())
                            .put(new JSONObject()
                                    .put("legs", legs)
                                    .put("name", "xxxxx")
                                    .put("color", "CC3333")));

            System.out.println(obj);
        }

    }
}
