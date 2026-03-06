package frc.robot.ballistics;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.math.util.Units;
import frc.robot.Constants.Shooter;

public class BallisticsLookupTable {
    public static final InterpolatingDoubleTreeMap distanceToHoodAngleMap = new InterpolatingDoubleTreeMap(); //meters to radians
    public static final InterpolatingDoubleTreeMap distanceToExitSpeedMap = new InterpolatingDoubleTreeMap(); //meters to meters per second
    public static final InterpolatingDoubleTreeMap exitSpeedToRPMMap = new InterpolatingDoubleTreeMap(); // MPS to RPM, converts ideal exit speed (m/s) to actual flywheel RPM, to compensate for flywheel dynamics (slippage, momentum loss, etc)

    static {
        // Dummy
        distanceToHoodAngleMap.put(0.0,0.0);
        distanceToHoodAngleMap.put(1.0,1.0); 

        // Dummy
        distanceToExitSpeedMap.put(2.0,16.0);
        distanceToExitSpeedMap.put(2.38,16.0);
        distanceToExitSpeedMap.put(2.53,17.7);

        // Dummy, assumes perfect mechanics for now
        exitSpeedToRPMMap.put(0.0,0.0);
        exitSpeedToRPMMap.put(100.0,BallisticsCommon.calculateIdealRPM(100));

    }
}