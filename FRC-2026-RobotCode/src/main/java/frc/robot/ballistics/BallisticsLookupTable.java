package frc.robot.ballistics;

import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;

public class BallisticsLookupTable {
    public static final InterpolatingDoubleTreeMap distanceToHoodAngleMap = new InterpolatingDoubleTreeMap(); //meters to rotations
    public static final InterpolatingDoubleTreeMap distanceToFlywheelSpeedMap = new InterpolatingDoubleTreeMap(); //meters to rotations per second

    static {
        // Dummy
        distanceToHoodAngleMap.put(0.0,0.0);
        distanceToHoodAngleMap.put(1.0,1.0); 

        // Dummy
        distanceToFlywheelSpeedMap.put(0.0,0.0);
        distanceToFlywheelSpeedMap.put(1.0,1.0);

    }
}
