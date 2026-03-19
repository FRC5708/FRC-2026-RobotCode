package frc.robot.ballistics;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.math.util.Units;
import frc.robot.Constants.Shooter;

public class BallisticsLookupTable {
    public static final InterpolatingDoubleTreeMap distanceToHoodAngleMap = new InterpolatingDoubleTreeMap(); //meters to radians
    public static final InterpolatingDoubleTreeMap distanceToFlywheelSpeedMap = new InterpolatingDoubleTreeMap(); //meters to RPM
    // TODO: Remove
    public static final InterpolatingDoubleTreeMap exitSpeedToRPMMap = new InterpolatingDoubleTreeMap(); // MPS to RPM, converts ideal exit speed (m/s) to actual flywheel RPM, to compensate for flywheel dynamics (slippage, momentum loss, etc)
    
    public static final InterpolatingDoubleTreeMap distanceToTOFMap = new InterpolatingDoubleTreeMap(); // meters to seconds
    public static final InterpolatingDoubleTreeMap velocityToDistanceMap = new InterpolatingDoubleTreeMap(); // essentially a reverse of the TOF map. m/s to m
    static {
        // Dummy
        distanceToHoodAngleMap.put(Units.feetToMeters(1 + 4), BallisticsCommon.motorRotsToShootRads(0.45));
        distanceToHoodAngleMap.put(Units.feetToMeters(2 + 4), BallisticsCommon.motorRotsToShootRads(0.55));
        distanceToHoodAngleMap.put(Units.feetToMeters(3 + 4), BallisticsCommon.motorRotsToShootRads(0.65));
        distanceToHoodAngleMap.put(Units.feetToMeters(4 + 4), BallisticsCommon.motorRotsToShootRads(0.78));
        distanceToHoodAngleMap.put(Units.feetToMeters(5 + 4), BallisticsCommon.motorRotsToShootRads(1.1));
        distanceToHoodAngleMap.put(Units.feetToMeters(6 + 4), BallisticsCommon.motorRotsToShootRads(1.22));
        distanceToHoodAngleMap.put(Units.feetToMeters(7 + 4), BallisticsCommon.motorRotsToShootRads(1.4));
        distanceToHoodAngleMap.put(Units.feetToMeters(8 + 4), BallisticsCommon.motorRotsToShootRads(1.5));
        distanceToHoodAngleMap.put(Units.feetToMeters(9 + 4), BallisticsCommon.motorRotsToShootRads(1.6));
        distanceToHoodAngleMap.put(Units.feetToMeters(10 + 4), BallisticsCommon.motorRotsToShootRads(1.7));
        distanceToHoodAngleMap.put(Units.feetToMeters(11 + 4), BallisticsCommon.motorRotsToShootRads(1.8));

        // Dummy
        distanceToFlywheelSpeedMap.put(Units.feetToMeters(1 + 4),(double)(50 * 60));
        distanceToFlywheelSpeedMap.put(Units.feetToMeters(6 + 4),(double)(50 * 60));
        distanceToFlywheelSpeedMap.put(Units.feetToMeters(7 + 4),(double)(55 * 60));
        distanceToFlywheelSpeedMap.put(Units.feetToMeters(8 + 4),(double)(57 * 60));
        distanceToFlywheelSpeedMap.put(Units.feetToMeters(11 + 4),(double)(57 * 60));


        // Dummy, assumes perfect mechanics for now
        exitSpeedToRPMMap.put(0.0,0.0);
        exitSpeedToRPMMap.put(100.0,BallisticsCommon.calculateIdealRPM(100));


    }
}