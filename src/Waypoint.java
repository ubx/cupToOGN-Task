/**
 * Created by andreas on 05.06.17.
 */
public class Waypoint {

    private String name;
    private double lat, lon;


    public static Waypoint createWaypoint(String... items) {
        if (items.length < 7) return null;
        return new Waypoint(items[0], toDouble(items[3]), toDouble(items[4]));
    }

    public Waypoint(String name, double lat, double lon) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    static private double toDouble(String val) {
        int p = val.indexOf(".");
        String deg = val.substring(0, p - 2);
        String min = val.substring(p - 2, val.length() - 1);
        double v = Double.valueOf(deg) + (Double.valueOf(min) / 60.0d);
        if ((val.charAt(val.length()-1) == 'W') | (val.charAt(val.length()-1) == 'S')) {
            v = v * (-1.0d);
        }
        return v;
    }
}
