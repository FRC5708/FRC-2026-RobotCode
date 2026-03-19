package frc.robot.ballistics;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;

public class BallisticsCalculator {

    // Calculates shooter speed in rad/s from distance to hub in m, dummy method
    public static double getSpeed(double distance) {
        return 1.0;
    }
    // Calculates hood angle in radians from distance to hub in m, dummy method
    public static double getAngle(double distance) {
        return 0.2;
    }

    // Generates a stationary shot solution from the Ballistic LUTs
    public static ShotSolution calculateStationarySolution(Translation2d shooterPosition, Translation2d targetPosition) {
        Translation2d goalPosition = targetPosition.minus(shooterPosition);
        double distance = goalPosition.getNorm(); // in meters
        return new ShotSolution(
            BallisticsLookupTable.distanceToHoodAngleMap.get(distance),
            BallisticsLookupTable.distanceToFlywheelSpeedMap.get(distance),
            goalPosition.getAngle()
        );
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
        return buildSolution(distance, trueShotExitSpeeds, shotVector);
    }

    // Calculates a SOTF solution from vector forces, accounting for lag
    public static ShotSolution calculateVectorSOTFSolution(Translation2d shooterPosition, Translation2d targetPosition, ChassisSpeeds shooterSpeed, double delaySeconds) {
        return calculateVectorSOTFSolution(
            predictActualPosition(shooterPosition, shooterSpeed, delaySeconds),
            targetPosition,
            shooterSpeed
        );
    }

    private static Translation2d predictActualPosition(Translation2d shooterPosition, ChassisSpeeds shooterSpeed, double delaySeconds) {
        Translation2d speedsVector = new Translation2d(shooterSpeed.vxMetersPerSecond,shooterSpeed.vyMetersPerSecond);
        return shooterPosition.plus(speedsVector.times(delaySeconds));
    }

    private static ShotSolution buildSolution(double distance, ShotExitSpeeds exitSpeeds, Translation2d shotVector) {
        return new ShotSolution(
            exitSpeeds.angle(), 
            BallisticsLookupTable.distanceToFlywheelSpeedMap.get(getEffectiveDistance(distance, exitSpeeds.horizontal(), 10)),
            shotVector.getAngle()
        );
    }

    // Iteratively searches the TOF LUT in reverse using the newton method, 
    private static double getEffectiveDistance(double distance, double requiredVelocity, int iterations) {
        double currentDistance = distance;
        double currentTime = BallisticsLookupTable.distanceToTOFMap.get(distance);
        double currentVelocity = currentDistance / currentTime;

        for (int i = 0; i < iterations && Math.abs(currentVelocity - requiredVelocity) > 0.005; i++) {
            // estimate d/dx ((Vel(x)) - requiredVelocity) = d/dx (Vel(x)) by taking a tiny slope
            final double EPSILON = 0.001;
            double lowVel =
                (currentDistance - EPSILON) / BallisticsLookupTable.distanceToTOFMap.get(currentDistance - EPSILON);
            double highVel =
                (currentDistance + EPSILON) / BallisticsLookupTable.distanceToTOFMap.get(currentDistance + EPSILON);
            double velDeriv = (highVel - lowVel) / (EPSILON * 2);
            currentDistance -= (currentVelocity - requiredVelocity) / velDeriv;
            // update currentVelocity with f(x+1)
            currentTime = BallisticsLookupTable.distanceToTOFMap.get(currentDistance);
            currentVelocity = currentDistance / currentTime;
        }
        return currentDistance;
    }

}
