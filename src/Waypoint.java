/**
 * Created by andreas on 05.06.17.
 */
public class Waypoint {

    private String name;
    private float lat, lon;


    public static Waypoint createWaypoint(String... items) {
        if (items.length < 7) return null;
        return new Waypoint(items[0], toFloat(items[3]), toFloat(items[4]));
    }

    public Waypoint(String name, float lat, float lon) {
        this.name = name.replaceAll("\"","");
        this.lat = lat;
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public float getLat() {
        return lat;
    }

    public float getLon() {
        return lon;
    }

    static private float toFloat(String val) {
        int p = val.indexOf(".");
        String deg = val.substring(0, p - 2);
        String min = val.substring(p - 2, val.length() - 1);
        float v = Float.valueOf(deg) + (Float.valueOf(min) / 60.0f);
        if ((val.charAt(val.length()-1) == 'W') | (val.charAt(val.length()-1) == 'S')) {
            v = v * (-1.0f);
        }
        return v;
    }
}
