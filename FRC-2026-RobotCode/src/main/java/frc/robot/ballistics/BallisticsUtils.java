package frc.robot.ballistics;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import frc.robot.Constants.Shooter;

public class BallisticsUtils {

    // Expresses a shot as a speed vector, with x representing horizontal exit speed of the shot (m/s) and y representing vertical exit speed of the shot (m/s) given a shot distance (m)
    public static ShotExitSpeeds getExitSpeeds(double distance) {
        double totalExitSpeed = BallisticsLookupTable.distanceToExitSpeedMap.get(distance);
        double exitAngle = BallisticsLookupTable.distanceToHoodAngleMap.get(distance);
        return new ShotExitSpeeds(totalExitSpeed * Math.cos(exitAngle),totalExitSpeed * Math.sin(exitAngle));
    }
}
