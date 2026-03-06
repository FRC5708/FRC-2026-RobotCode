package frc.robot.commands;

import static edu.wpi.first.units.Units.RPM;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.Auto.RotationK;
import frc.robot.ballistics.BallisticsCalculator;
import frc.robot.ballistics.ShotSolution;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IndexSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShootSubsystem;
import frc.robot.utils.GeometryUtils;

//TODO: remove timed delays, replace with empirical measurement of encoders
//This command uses an internal FSM for control
// Lowkirkuinely ts FSM logic would probably be better as a superstructure class than a command, or could be replaced by a bool instead of a state enum
public class NewShoot extends Command {
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
    private static final double robotRotationThreshRads = 0.1;
    private static final double hoodAngleThreshRads = 0.1;
    private static final double flywheelSpeedThreshRPM = 300;

    private final Translation2d target;

    private final DoubleSupplier xSpeedSupplier;
    private final DoubleSupplier ySpeedSupplier;
    private final PIDController rotationController = new PIDController(RotationK.kP,RotationK.kI,RotationK.kD);

    public NewShoot(Translation2d target, DoubleSupplier xSpeedSupplier, DoubleSupplier ySpeedSupplier, ShootSubsystem shoot, IndexSubsystem index, IntakeSubsystem intake, DriveSubsystem drive) {
        this.target = target;
        m_shoot = shoot;
        m_index = index;
        m_intake = intake;
        m_drive = drive;
        this.xSpeedSupplier = xSpeedSupplier;
        this.ySpeedSupplier = ySpeedSupplier;
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void execute() {
        // TODO: compensate for lag in ballistics calculations
        solution = BallisticsCalculator.calculateVectorSOTFSolution(
            m_drive.getPose().getTranslation(),
            target,
            m_drive.getFieldRelativeSpeeds()
        );
        switch (state) {
            case TRANSITIONING:
                //TODO: Fix units
                m_shoot.hood(Units.radiansToRotations(solution.hoodAngleRads()));
                m_shoot.stage(-1);
                m_shoot.shootRPM(solution.flywheelSpeedRPM());
                m_index.indexToStage(true);
                m_intake.intake(.2);
                executeAutoalign(solution.robotAngleRads());
                if (inThreshold()) {
                    state = CommandState.FIRING;
                }
            case FIRING:
                m_shoot.hood(Units.radiansToRotations(solution.hoodAngleRads()));
                m_shoot.stage(.4);
                m_index.indexFromStage(true);
                m_shoot.shootRPM(solution.flywheelSpeedRPM());
                m_intake.intake(.2);
                executeAutoalign(solution.robotAngleRads());
                if (!inThreshold()) {
                    state = CommandState.TRANSITIONING;
                }
        }

    }

    private boolean inThreshold() {
        return 
            Math.abs(Units.rotationsToRadians(m_shoot.getHoodPos()) - solution.hoodAngleRads()) < hoodAngleThreshRads &&
            Math.abs(solution.robotAngleRads().getRadians() - m_drive.getPose().getRotation().getRadians()) < robotRotationThreshRads &&
            Math.abs(solution.flywheelSpeedRPM() - m_shoot.getRightShooterVelocityUnitSafe().abs(RPM)) < flywheelSpeedThreshRPM;
    }

    private void executeAutoalign(Rotation2d angle) {
        var speeds = ChassisSpeeds.fromFieldRelativeSpeeds(
            xSpeedSupplier.getAsDouble() * m_drive.getSwerveDrive().getMaximumChassisVelocity(),
            ySpeedSupplier.getAsDouble() * m_drive.getSwerveDrive().getMaximumChassisVelocity(),
            rotationController.calculate(
                m_drive.getPose().getRotation().getRadians(),
                angle.getRadians()
            ),
            m_drive.getPose().getRotation()
        );
        
        m_drive.driveRobotRelative(speeds);
    }
}
