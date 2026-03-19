package frc.robot.ballistics;

import edu.wpi.first.math.util.Units;
import frc.robot.Constants.Shooter;

// Unit conversions. Todo: refactor to use the units library or common units, ts is lowkey really bad
public class BallisticsCommon {
    // Converts a flywheel RPM to linear Meters per second, given by the relationship linear velocity = angular velocity * radius
    public static double flywheelRPMtoMPS(double flywheelRPM) {
        return Units.rotationsPerMinuteToRadiansPerSecond(flywheelRPM) * Shooter.flywheelRadiusMeters;
    }

    public static double flywheelRPStoMPS(double flywheelRPS) {
        return Units.rotationsPerMinuteToRadiansPerSecond(flywheelRPS * 60) * Shooter.flywheelRadiusMeters;
    }

    // converts a linear velocity back into RPM, assuming no dynamics
    public static double calculateIdealRPM(double mps) {
        return Units.radiansPerSecondToRotationsPerMinute(mps/Shooter.flywheelRadiusMeters);
    }

    public static double motorRotsToHoodRads(double motorRots) {
        return Units.degreesToRadians(motorRots * 10);
    }

    public static double hoodRadsToMotorRots(double hoodRads) {
        return Units.radiansToDegrees(hoodRads)/10;
    }

    public static double motorRotsToShootRads(double motorRots) {
        return Units.degreesToRadians(90) - motorRotsToHoodRads(motorRots);
    }

    // Accounts for gearing and the fact that shootRads is the complement of hoodAngle
    public static double shootRadsToMotorRots(double shootRads) {
        return hoodRadsToMotorRots(Units.degreesToRadians(90) - shootRads);
    }
}