package ajmas74.experimental.geo;

import java.awt.geom.Point2D;

public class Landmark {
    long        id;
    Point2D     location;
    String      name;
    Bounds3D    bounds;
    String      previewImageUrl;
    
    public String toString() 
    {
        return name;
    }
}
