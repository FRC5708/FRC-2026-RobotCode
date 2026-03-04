package frc.robot.commands;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.Auto.RotationK;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IndexSubsystem;
import frc.robot.subsystems.ShootSubsystem;

// This command will autoalign with the target pose, fix the hood, 
public class NewShootCommand extends Command {
    private final Pose2d targetPose;
    private final ShootSubsystem shooter;
    private final IndexSubsystem indexer;
    private final DriveSubsystem drive;
    private final DoubleSupplier xSpeedSupplier;
    private final DoubleSupplier ySpeedSupplier;
    private final PIDController rotationController = new PIDController(RotationK.kP,RotationK.kI,RotationK.kD);
    
    public NewShootCommand(Pose2d targetPose,ShootSubsystem shooter,IndexSubsystem indexer,DriveSubsystem drive,DoubleSupplier xSpeedSupplier,DoubleSupplier ySpeedSupplier) {
        this.targetPose = targetPose;
        this.shooter = shooter;
        this.indexer = indexer;
        this.drive = drive;
        this.xSpeedSupplier = xSpeedSupplier;
        this.ySpeedSupplier = ySpeedSupplier;
    }

    @Override
    public void initialize() {
        addRequirements(shooter,indexer,drive);
    }

    @Override
    public void execute() {
        
    }
}
