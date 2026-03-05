package frc.robot.ballistics;

import edu.wpi.first.math.util.Units;
import frc.robot.Constants.Shooter;

public class BallisticsCommon {
    // Converts a flywheel RPM to linear Meters per second, given by the relationship linear velocity = angular velocity * radius
    public static double flywheelRPMtoMPS(double flywheelRPM) {
        return Units.rotationsPerMinuteToRadiansPerSecond(flywheelRPM) * Shooter.flywheelRadiusMeters;
    }

    // converts a linear velocity back into RPM, assuming no dynamics
    public static double calculateIdealRPM(double mps) {
        return Units.radiansPerSecondToRotationsPerMinute(mps/Shooter.flywheelRadiusMeters);
    }
}