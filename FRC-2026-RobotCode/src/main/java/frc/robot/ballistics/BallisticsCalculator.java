package frc.robot.ballistics;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;

public class BallisticsCalculator {

    public static enum BallisticsStrategy {
        SIMPLE_LOOKUP,
        SOLVE_FORCES,

    }
    // Calculates shooter speed in rad/s from distance to hub in m, dummy method
    public static double getSpeed(double distance) {
        return 1.0;
    }
    // Calculates hood angle in radians from distance to hub in m, dummy method
    public static double getAngle(double distance) {
        return 0.2;
    }

    public static ShotSolution calculateStationarySolution(Translation2d shooterPosition, Translation2d targetPosition) {
        Translation2d goalPosition = targetPosition.minus(shooterPosition);
        double distance = goalPosition.getNorm(); // in meters
        ShotExitSpeeds idealExitSpeeds = BallisticsUtils.getExitSpeeds(distance);
        Translation2d goalVector = goalPosition.div(distance).times(idealExitSpeeds.horizontal());
        return buildSolution(idealExitSpeeds, goalVector);
    }

    // Calculates a SOTF solution from vector forces, ignoring real-world dynamics. shooterSpeed is FIELD-ORIENTED
    public static ShotSolution calculateVectorSOTFSolution(Translation2d shooterPosition, Translation2d targetPosition, ChassisSpeeds shooterSpeed) {
        Translation2d speedsVector = new Translation2d(shooterSpeed.vxMetersPerSecond,shooterSpeed.vyMetersPerSecond);
        Translation2d goalPosition = targetPosition.minus(shooterPosition);

        // Calculate ideal shot (stationary)
        double distance = goalPosition.getNorm(); // in meters
        ShotExitSpeeds idealExitSpeeds = BallisticsUtils.getExitSpeeds(distance);
        Translation2d goalVector = goalPosition.div(distance).times(idealExitSpeeds.horizontal());
        
        // We compensate for the robot's motion by subtracting its speed vector from the goal vector to obtain the shot
        Translation2d shotVector = goalVector.minus(speedsVector);

        ShotExitSpeeds trueShotExitSpeeds = new ShotExitSpeeds(shotVector.getNorm(), idealExitSpeeds.vertical());

        // Convert into control
        return buildSolution(trueShotExitSpeeds, shotVector);
    }

    private static ShotSolution buildSolution(ShotExitSpeeds exitSpeeds, Translation2d shotVector) {
        return new ShotSolution(
            exitSpeeds.angle(), 
            BallisticsLookupTable.exitSpeedToRPMMap.get(exitSpeeds.total()),
            shotVector.getAngle()
        );
    }

}
