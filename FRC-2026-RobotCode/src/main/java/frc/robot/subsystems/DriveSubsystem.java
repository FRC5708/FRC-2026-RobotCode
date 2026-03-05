// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static edu.wpi.first.units.Units.Rotation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.DoubleSupplier;

import org.json.simple.parser.ParseException;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.Drive;
import frc.robot.Constants.FieldConstants;
import frc.robot.Constants.Operator;
import frc.robot.Constants.Auto.RotationK;
import frc.robot.Constants.Auto.TranslationK;
import frc.robot.Constants.FieldConstants.PosesOfInterest;
import frc.robot.subsystems.vision.Camera;
import frc.robot.subsystems.vision.VisionConstants;
import frc.robot.subsystems.vision.VisionUtils;
import frc.robot.subsystems.vision.io.CameraIOPhoton;
import swervelib.SwerveDrive;
import swervelib.parser.SwerveParser;

@Logged
public class DriveSubsystem extends SubsystemBase {

  private SwerveDrive swerveDrive;

  SwerveDriveOdometry m_odometry;
  ArrayList<Camera> cameras = new ArrayList<>();
  Field2d m_field = new Field2d();
  double creepMode;

  private ShuffleboardTab tab = Shuffleboard.getTab("Testing Variables");
  private GenericEntry targetDistance = tab.add("Distance from Pose",0).getEntry();

  public DriveSubsystem() throws IOException, ParseException {
    File swerveJsons = new File(Filesystem.getDeployDirectory(), "swerve");

    // All other subsystem initialization
    // Load the RobotConfig from the GUI settings. You should probably
    // store this in your Constants file
    RobotConfig config = RobotConfig.fromGUISettings();

    cameras.add(new Camera(new CameraIOPhoton(VisionConstants.PhotonCamConfig.RED_CAMERA,() -> new Pose3d(getPose()),() -> getPose().getRotation())));
    cameras.add(new Camera(new CameraIOPhoton(VisionConstants.PhotonCamConfig.BLUE_CAMERA,() -> new Pose3d(getPose()),() -> getPose().getRotation())));

    // Makes the swerve drive with Json files
    swerveDrive = new SwerveParser(swerveJsons).createSwerveDrive(Drive.maxSpeed);

    // Sets creep mode to base value
    creepMode = 1.5;

    // Configure AutoBuilder last
    AutoBuilder.configure(
        this::getPose, // Robot pose supplier
        this::resetPose, // Method to reset odometry (will be called if your auto has a starting pose)
        this::getRobotRelativeSpeeds, // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE
        (speeds, feedforwards) -> driveRobotRelative(speeds), // Method that will drive the robot given ROBOT RELATIVE
                                                              // ChassisSpeeds. Also optionally outputs individual
                                                              // module feedforwards
        new PPHolonomicDriveController( // PPHolonomicController is the built in path following controller for holonomic
                                        // drive trains
            new PIDConstants(TranslationK.kP, TranslationK.kI, TranslationK.kD), // Translation PID constants
            new PIDConstants(RotationK.kP, RotationK.kI, RotationK.kD) // Rotation PID constants
        ),
        config,
        () -> {
          // Boolean supplier that controls when the path will be mirrored for the red
          // alliancecontroller
          // This will flip the path being followed to the red side of the field.
          // THE ORIGIN WILL REMAIN ON THE BLUE SIDE

          var alliance = DriverStation.getAlliance();
          if (alliance.isPresent()) {
            return alliance.get() == DriverStation.Alliance.Red;
          }
          return false;
        },
        this // Reference to this subsystem to set requirements
    );
  }

  public Command driveCommand(DoubleSupplier translationX, DoubleSupplier translationY,
      DoubleSupplier angularRotationX) {
    return run(() -> {
      // Make the robot move
      double x = -MathUtil.applyDeadband(translationX.getAsDouble(), Operator.driveDeadband);
      double y = -MathUtil.applyDeadband(translationY.getAsDouble(), Operator.driveDeadband);;
      double r = Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
      r = Math.pow(r,3);
      x = x*r;
      y = y*r;
      double angle = -MathUtil.applyDeadband(angularRotationX.getAsDouble(), Operator.driveDeadband);
      angle = Math.pow(angle, 3);
      swerveDrive.drive(new Translation2d(y * swerveDrive.getMaximumChassisVelocity() / creepMode,
          x * swerveDrive.getMaximumChassisVelocity() / creepMode),
          angle * swerveDrive.getMaximumChassisAngularVelocity() / creepMode,
          true,
          false);
    });
  };

  public SwerveDrive getSwerveDrive() {
    return swerveDrive;
  }

  // TODO: jank asf, remove
  public double distanceFromHub() {
    return getPose().getTranslation().minus(FieldConstants.PosesOfInterest.blueHub.getTranslation()).getNorm() + Units.feetToMeters(2);
  }


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    //Pose2d pose = getPose();
    for (var camera : cameras) {
      camera.periodic();
      var observations = camera.getPoseObservations();
      if (observations.size() > 0) {
        // FIXME This generates too much log spam. It must be removed for competition.
        //System.out.println("Observations: " + observations.size());
        for (var observation : observations) {
          swerveDrive.addVisionMeasurement(
            observation.pose().toPose2d(),
            observation.timestampSeconds(),
            VisionUtils.collapse3DstddevsTo2d(observation.stddevs())
          );
        }
      }
    }
    m_field.setRobotPose(getPose());
    double m_targetDistance = getDistanceToPose(PosesOfInterest.redHub);
    targetDistance.setDouble(m_targetDistance);
    //Comment out the following one to reduce feedback
    //System.out.println(pose);
  }

  public void creepModeToggle() {
    if (creepMode == 1.5) {
      creepMode = 4.5;
    } else {
      creepMode = 1.5;
    }
  }

  public Command zeroGyro() {
    return new InstantCommand(() -> this.swerveDrive.zeroGyro());
  }

  public Pose2d getPose() {
    return swerveDrive.getPose();
  }

  public Rotation2d getGyroRotation() {
    return this.swerveDrive.getYaw();
  }
  
  public SwerveModuleState[] getModuleStates() {
    return this.swerveDrive.getStates();
  }

  public double getDistanceToPose(Pose2d pose) {
    return getPose().getTranslation().getDistance(pose.getTranslation());
  }

  public void resetPose(Pose2d initialHolonomicPose) {
    this.swerveDrive.resetOdometry(initialHolonomicPose);
  }

  public ChassisSpeeds getRobotRelativeSpeeds() {
    return this.swerveDrive.getRobotVelocity();
  }

  public void driveRobotRelative(ChassisSpeeds velocity) {
    this.swerveDrive.drive(velocity);
  }
}
