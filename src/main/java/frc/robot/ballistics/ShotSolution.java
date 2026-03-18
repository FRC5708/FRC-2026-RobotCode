package frc.robot.ballistics;

import edu.wpi.first.math.geometry.Rotation2d;

public record ShotSolution (
    double shootAngleRads,
    double flywheelSpeedRPM,
    Rotation2d robotAngleRads
) {}
