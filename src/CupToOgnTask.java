import org.apache.commons.cli.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;


/**
 * Created by andreas on 05.06.17.
 */
public class CupToOgnTask {

    private static DecimalFormat dfLat = new DecimalFormat("##.00000");
    private static DecimalFormat dfLon = new DecimalFormat("###.00000");


    private enum Mode {waypointheader, waypoint, taskheader, task}
    private static String cupfile, ognfile;

    public static void main(String args[]) throws IOException {

        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("cup", true, "the cup-file to read");
        options.addOption("ogn", true, "the ogn-file to write");
        HelpFormatter formatter = new HelpFormatter();
        try {
            CommandLine cmd = parser.parse(options, args);
            cupfile = cmd.getOptionValue("cup");
            ognfile = cmd.getOptionValue("ogn");
        } catch (ParseException e) {
            formatter.printHelp("cupToOgnTask", options);
            exit(1);
        }

        Scanner scanner = new Scanner(new File(cupfile));
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
                    if (line.startsWith("Options") | line.startsWith("ObsZone")) {
                        // todo - implement Options and ObsZone
                        break;
                    }
                    String[] items2 = line.split(",");
                    lastTask = Task.createTask(waipoints, items2);
                    tasks.add(lastTask);
                    break;
            }
            lineNumber++;
        }

        // to json
        for (Task task : tasks) {
            String taskName = task.getName();
            if (taskName.length() == 0) {
                taskName = "Unknown";
            }
            List<WaypointT> waypoints = task.getWaypoints();
            JSONArray legs = new JSONArray();

            for (Waypoint waypoint : waypoints) {
                JSONArray leg = new JSONArray();
                leg.put(dfLat.format(waypoint.getLat())).put(dfLon.format(waypoint.getLon()));
                legs.put(leg);
            }

            JSONObject obj = (new JSONObject())
                    .put("tasks", (new JSONArray())
                            .put(new JSONObject()
                                    .put("legs", legs)
                                    .put("name", taskName)
                                    .put("color", "CC3333")));

            if (ognfile == null) {
                System.out.println(obj);
            } else {
                FileWriter file = new FileWriter(ognfile);
                obj.write(file);
                file.close();
            }
        }

    }
}
