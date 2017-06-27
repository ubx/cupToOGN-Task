import org.apache.commons.cli.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;


/**
 * Created by andreas on 05.06.17.
 */
public class CupToOgnTask {

    private static final String COMPARE_DUPLICATE_POSITION = "compare";
    private static final String CUP = "cup";
    private static final String OGN = "ogn";

    private enum Mode {waypointheader, waypoint, taskheader, task}

    private static String cupfile, ognfile;

    public static void main(String args[]) throws IOException {

        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addRequiredOption("cup", "seeyou cup input file", true, "the cup-file to read");
        options.addOption("ogn", true, "the ogn-file (json-encoded) to write, if not specified: sysout");
        options.addOption(COMPARE_DUPLICATE_POSITION, false, "check if there are waypoins with the same position");
        CommandLine cmd = null;

        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(200);
        try {
            cmd = parser.parse(options, args);
            cupfile = cmd.getOptionValue(CUP);
            ognfile = cmd.getOptionValue(OGN);
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
            if (line.length() == 0) continue; // skip empty line
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
                    if (line.startsWith("ObsZone") && lastTask != null) {
                        String[] obsItems = line.split(",");
                        ObsZone obsZone = ObsZone.createObsZone(obsItems);
                        lastTask.addObsZone(Integer.decode(obsItems[0].split("=")[1]), obsZone);
                        break;
                    }
                    lastTask = Task.createTask(waipoints, line.split("\",\""));
                    if (lastTask != null) tasks.add(lastTask);
                    break;
            }
            lineNumber++;
        }

        if (cmd.hasOption(COMPARE_DUPLICATE_POSITION)) {
            WaypointUtils.checkForDuplicate(waipoints);
            exit(0);
        }

        // to json
        ToJson.output(tasks, ognfile);

    }

}
