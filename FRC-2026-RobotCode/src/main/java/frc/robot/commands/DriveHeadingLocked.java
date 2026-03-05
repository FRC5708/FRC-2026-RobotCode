package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.Constants.Auto.*;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ShootSubsystem;
import frc.robot.utils.GeometryUtils;
import frc.robot.Constants.Shooter;;

// Drives holonomic while aligning the robot with a pose. It controls rotation with PID while allowing the user full translational control
public class DriveHeadingLocked extends Command {
    private final Pose2d targetPose;
    private final DoubleSupplier xSpeedSupplier;
    private final DoubleSupplier ySpeedSupplier;
    private final PIDController rotationController = new PIDController(RotationK.kP,RotationK.kI,RotationK.kD);
    private final DriveSubsystem m_drive;
    public DriveHeadingLocked(Pose2d targetPose, DoubleSupplier xSpeedSupplier, DoubleSupplier ySpeedSupplier, DriveSubsystem drive) {
        this.targetPose = targetPose;
        this.xSpeedSupplier = xSpeedSupplier;
        this.ySpeedSupplier = ySpeedSupplier;
        m_drive = drive;
        //m_shooter = shoot;
    }

    @Override
    public void initialize() {
        addRequirements(m_drive);
    }

    @Override
    public void execute() {
        var difference = GeometryUtils.angleToPose(m_drive.getPose(),targetPose).getRadians();
        //System.out.println(control);
        var speeds = ChassisSpeeds.fromFieldRelativeSpeeds(
            xSpeedSupplier.getAsDouble() * m_drive.getSwerveDrive().getMaximumChassisVelocity(),
            ySpeedSupplier.getAsDouble() * m_drive.getSwerveDrive().getMaximumChassisVelocity(),
            rotationController.calculate(difference,0),
            m_drive.getPose().getRotation()
        );
        
        m_drive.driveRobotRelative(speeds);
    }
}

