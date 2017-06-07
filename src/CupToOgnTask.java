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
        options.addRequiredOption("cup", "seeyou .cup input file",true, "the cup-file to read");
        options.addOption("ogn", true, "the ogn-file to write, if not specified: sysout");
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(200);
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
        Task lastTask = null;
        while (scanner.hasNextLine()) {
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
                        System.out.println("line skiped " + lineNumber + " : " + line);
                        break;
                    }
                    waipoints.put(wp.getName(), wp);
                    break;

                case taskheader:
                    mode = Mode.task;
                    break;

                case task:
                    if (line.startsWith("Options")) {
                        // todo - implement Options ?
                        break;
                    }
                    if (line.startsWith("ObsZone")) {
                        String[] obsItems = line.split(",");
                        ObsZone obsZone = ObsZone.createObsZone(obsItems);
                        int num = Integer.decode(obsItems[0].split("=")[1]);
                        lastTask.addObsZone(num, obsZone);
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
        JSONObject jsonObj = new JSONObject();
        for (Task task : tasks) {
            String taskName = task.getName();
            if (taskName.length() == 0) {
                taskName = "Unknown";
            }
            List<WaypointT> wps = task.getWaypoints();
            JSONArray legs = new JSONArray();
            int i = 0;
            for (WaypointT waypoint : wps) {
                legs.put((new JSONArray()).put(dfLat.format(waypoint.getLat())).put(dfLon.format(waypoint.getLon())));
                if (i > 0 & i < wps.size() - 1) {
                    legs.put((new JSONArray()).put(waypoint.getObsZone() == null ? 500 : waypoint.getObsZone().getR1()));
                }
                i++;
            }
            JSONArray jsonArr = new JSONArray()
                    .put(new JSONObject()
                            .put("legs", legs)
                            .put("name", taskName)
                            .put("color", "CC3333"));
            jsonObj.append("tasks", jsonArr);
        }

        if (ognfile == null) {
            System.out.println(jsonObj.toString(2));
        } else {
            FileWriter file = new FileWriter(ognfile);
            file.write(jsonObj.toString(2));
            //jsonObj.write(file);
            file.close();
        }


    }
}
