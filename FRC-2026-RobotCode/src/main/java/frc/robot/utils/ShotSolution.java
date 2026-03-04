package frc.robot.utils;

public record ShotSolution (
    double launchPitchRad,
    double launchSpeedRadPerSecond,
    double robotAngleRad // Angle of the robot's shooter relative to the hub
) {}
