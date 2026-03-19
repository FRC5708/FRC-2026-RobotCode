package frc.robot.ballistics;

import static edu.wpi.first.units.Units.Horsepower;

import java.util.Map;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.math.util.Units;
import frc.robot.Constants.Shooter;

public class BallisticsUtils {

    // Expresses a shot as a speed vector, with x representing horizontal exit speed of the shot (m/s) and y representing vertical exit speed of the shot (m/s) given a shot distance (m)
    public static ShotExitSpeeds getExitSpeeds(double distance) {
        double horizontalExitSpeed = getHorizontalSpeed(distance);
        
        double exitAngle = BallisticsLookupTable.distanceToHoodAngleMap.get(distance);
        double verticalSpeed = (horizontalExitSpeed/Math.cos(exitAngle))/Math.sin(exitAngle);
        return new ShotExitSpeeds(horizontalExitSpeed,verticalSpeed);
    }

    // This method calculates horizontal speed of the shot, by dividing TOF by the distance. Returns m/s
    public static double getHorizontalSpeed(double distance) {
        return distance / BallisticsLookupTable.distanceToTOFMap.get(distance);
    }
}