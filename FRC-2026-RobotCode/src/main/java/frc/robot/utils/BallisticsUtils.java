package frc.robot.utils;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;

public class BallisticsUtils {
    // Calculates shooter speed in rad/s from distance to hub in m, dummy method
    public static double getSpeed(double distance) {
        return 1.0;
    }
    // Calculates hood angle in radians from distance to hub in m, dummy method
    public static double getAngle(double distance) {
        return 0.2;
    }

    public static ShotSolution calculateFixedShot(Pose2d shooterPose, Pose2d targetPose, ChassisSpeeds speeds) {
        
    }
}
