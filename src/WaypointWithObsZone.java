/**
 * Created by andreas on 05.06.17.
 */
public class WaypointWithObsZone extends Waypoint {

    private ObsZone obsZone;

    public WaypointWithObsZone(Waypoint waypoint) {
        super(waypoint.getName(), waypoint.getLat(), waypoint.getLon());
    }

    public ObsZone getObsZone() {
        return obsZone;
    }

    public void setObsZone(ObsZone obsZone) {
        this.obsZone = obsZone;
    }
}
