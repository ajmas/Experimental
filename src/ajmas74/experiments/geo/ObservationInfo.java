package ajmas74.experiments.geo;

import java.awt.geom.Point2D;

public class ObservationInfo {
    Point2D location;

    float horizontalOrientation;

    float verticalOrientation;

    public float getHorizontalOrientation() {
        return horizontalOrientation;
    }

    public void setHorizontalOrientation(float horizontalOrientation) {
        this.horizontalOrientation = horizontalOrientation;
    }

    public Point2D getLocation() {
        return location;
    }

    public void setLocation(Point2D location) {
        this.location = location;
    }

    public float getVerticalOrientation() {
        return verticalOrientation;
    }

    public void setVerticalOrientation(float verticalOrientation) {
        this.verticalOrientation = verticalOrientation;
    }

}
