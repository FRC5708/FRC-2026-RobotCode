package frc.robot.commands;

import static edu.wpi.first.units.Units.RPM;
import static edu.wpi.first.units.Units.Radians;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.controller.PIDController;
//import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
//import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.util.Units;
//import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.Auto.RotationK;
import frc.robot.ballistics.BallisticsCalculator;
//import frc.robot.ballistics.BallisticsCommon;
//import frc.robot.ballistics.BallisticsLookupTable;
import frc.robot.ballistics.ShotSolution;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IndexSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShootSubsystem;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.commands.DriveHeadingLocked;
import frc.robot.FieldConstants;
import frc.robot.Constants;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
//import frc.robot.utils.GeometryUtils;

//TODO: remove timed delays, replace with empirical measurement of encoders
//This command uses an internal FSM for control
//Lowkirkuinely ts FSM logic would probably be better as a superstructure class than a command, or could be replaced by a bool instead of a state enum
public class ShootAutoDistance extends Command {
    private static enum CommandState {
        TRANSITIONING, // The robot is transitioning into the firing state, hood angle is transitioning, robot is rotating, indexing into stage. When all measurements are within the threshold, transitions into firing
        FIRING // The robot is firing, all subsystems are still targeting their respective setpoints, stager is indexing into shooter
    }
    private CommandState state = CommandState.TRANSITIONING;
    private ShotSolution solution;
    private final ShootSubsystem m_shoot; 
    private final IndexSubsystem m_index;
    private final IntakeSubsystem m_intake;
    private final DriveSubsystem m_drive;

    //TODO: Tune these, Move to constants file
    private static final double robotRotationThreshRads = Units.degreesToRadians(5); //TODO: Maybe make dynamic???
    private static final double hoodAngleThreshRads = Units.degreesToRadians(1);
    private static final double flywheelSpeedThreshRPM = 30;

    private Translation2d target;

    private final DoubleSupplier xSpeedSupplier;
    private final DoubleSupplier ySpeedSupplier;
    private final PIDController rotationController = new PIDController(RotationK.kP,RotationK.kI,RotationK.kD);

    public ShootAutoDistance(DoubleSupplier xSpeedSupplier, DoubleSupplier ySpeedSupplier, ShootSubsystem shoot, IndexSubsystem index, IntakeSubsystem intake, DriveSubsystem drive) {
        m_shoot = shoot;
        m_index = index;
        m_intake = intake;
        m_drive = drive;
        this.xSpeedSupplier = xSpeedSupplier;
        this.ySpeedSupplier = ySpeedSupplier;
        addRequirements(m_shoot,m_intake,m_index);
    }

    @Override
    public void initialize() {
        target = DriverStation.getAlliance().isPresent()
        ? DriverStation.getAlliance().map(
          (Alliance alliance) -> alliance == Alliance.Blue
            ? FieldConstants.Hub.topCenterPoint.toTranslation2d()
            : FieldConstants.Hub.redTopCenterPoint.toTranslation2d()
        ).get()
        : FieldConstants.Hub.topCenterPoint.toTranslation2d();
    }

    @Override
    public void execute() {
        // TODO: compensate for lag in ballistics calculations
        solution = BallisticsCalculator.calculateStationarySolution(m_drive.getPose().getTranslation(), target);
        switch (state) {
            case TRANSITIONING:
                //TODO: Fix units
                // Note, setShootAngle sets the exit angle of the ball leaving the shooter, which is the complement of the hood angle
                m_shoot.hood(solution.shootAngleRads());
                m_shoot.stage(0.4);
                m_shoot.shoot(solution.flywheelSpeedRPM(),RPM);
                m_index.run(0.6);
                //m_intake.intake(.2);
                //executeAutoalign(solution.robotAngleRads());
                if (inThreshold()) {
                    state = CommandState.FIRING;
                }
                break;
            case FIRING:
                m_shoot.hood(solution.shootAngleRads());
                m_shoot.stage(-1);
                m_index.run(-0.6);
                m_shoot.shoot(solution.flywheelSpeedRPM(),RPM);
                m_intake.intake(.2);
                //executeAutoalign(solution.robotAngleRads());
                System.out.println(solution.shootAngleRads());
                if (!inThreshold()) {
                    state = CommandState.TRANSITIONING;
                }
                break;
        }

    }

    private boolean inThreshold() {
        return true;
            //Math.abs(m_shoot.getShootAngle().in(Radians) - solution.shootAngleRads()) < hoodAngleThreshRads &&
            //Math.abs(solution.robotAngleRads().getRadians() - m_drive.getPose().getRotation().getRadians()) < robotRotationThreshRads &&
            //Math.abs(solution.flywheelSpeedRPM() - m_shoot.getRightShooterVelocityUnitSafe().abs(RPM)) < flywheelSpeedThreshRPM;
    }

    // private void executeAutoalign(Rotation2d angle) {
    //     var speeds = ChassisSpeeds.fromFieldRelativeSpeeds(
    //         xSpeedSupplier.getAsDouble() * m_drive.getSwerveDrive().getMaximumChassisVelocity(),
    //         ySpeedSupplier.getAsDouble() * m_drive.getSwerveDrive().getMaximumChassisVelocity(),
    //         rotationController.calculate(
    //             m_drive.getPose().getRotation().getRadians(),
    //             angle.getRadians()
    //         ),
    //         m_drive.getPose().getRotation()
    //     );
        
    //     m_drive.driveRobotRelative(speeds);
    // }

    @Override
    public void end(boolean interrupted) {
        m_shoot.shoot(0,RPM);
        m_shoot.stage(0);
        m_index.run(0);
        m_intake.intake(0);
        m_shoot.hoodDown(0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
