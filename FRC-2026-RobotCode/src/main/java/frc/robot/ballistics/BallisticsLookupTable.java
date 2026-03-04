package frc.robot.ballistics;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.math.util.Units;
import frc.robot.Constants.Shooter;

public class BallisticsLookupTable {
    public static final InterpolatingDoubleTreeMap distanceToHoodAngleMap = new InterpolatingDoubleTreeMap(); //meters to radians
    public static final InterpolatingDoubleTreeMap distanceToExitSpeedMap = new InterpolatingDoubleTreeMap(); //meters to meters per second
    public static final InterpolatingDoubleTreeMap exitSpeedToRPMMap = new InterpolatingDoubleTreeMap(); // MPS to RPM, converts ideal exit speed (m/s) to actual flywheel RPM, to compensate for flywheel dynamics (slippage, momentum loss, etc)

    // converts a linear velocity back into RPM, assuming no dynamics
    public static double calculateIdealRPM(double mps) {
        return Units.radiansPerSecondToRotationsPerMinute(mps/Shooter.flywheelRadiusMeters);
    }

    static {
        // Dummy
        distanceToHoodAngleMap.put(0.0,0.0);
        distanceToHoodAngleMap.put(1.0,1.0); 

        // Dummy
        distanceToExitSpeedMap.put(0.0,0.0);
        distanceToExitSpeedMap.put(1.0,1.0);

        // Dummy, assumes perfect mechanics for now
        exitSpeedToRPMMap.put(0.0,0.0);
        exitSpeedToRPMMap.put(100.0,calculateIdealRPM(100));

    }
}
