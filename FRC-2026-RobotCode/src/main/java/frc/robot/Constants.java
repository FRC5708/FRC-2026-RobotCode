// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import java.util.AbstractMap;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.util.Units;
import frc.robot.subsystems.vision.VisionConstants;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static class Operator {
    public static final int kDriverControllerPort = 0;
    public static final double driveDeadband = 0.05;
  }
  
  public static class Drive {
    // The max free speed of the module
    public static final double maxSpeed = 4.5;

    public static final double trackWidth = 0.5588;
    public static final double wheelBase = 0.5588;

    public static Translation2d frontLeftPosition = new Translation2d(trackWidth/2,wheelBase/2);
    public static Translation2d frontRightPosition = new Translation2d(-trackWidth/2,wheelBase/2);
    public static Translation2d backLeftPosition = new Translation2d(trackWidth/2,-wheelBase/2);
    public static Translation2d backRightPosition = new Translation2d(-trackWidth/2,-wheelBase/2);

    public static SwerveDriveKinematics driveKinematics = new SwerveDriveKinematics(
      frontLeftPosition,
      frontRightPosition,
      backLeftPosition,
      backRightPosition
    );

  }

  public static class Auto {
    public static class TranslationK {
      public static final double kP = 5.0;
      public static final double kI = 0.0;
      public static final double kD = 0.0;
    }

    public static class RotationK {
      public static final double kP = 5.0;
      public static final double kI = 0.0;
      public static final double kD = 0.0;
    }

  }
  public static class Intake {
    public static final int canIDDeploy = 11;
    public static final int canIDIntake = 12;
  }

  public static class Index {
    public static final int canIDIndex = 25;
  }

  public static class Shooter {
    public static final int canIDShootLeft = 26; // use Phoenix tuner
    public static final int canIDShootRight = 27; // use Phoenix tuner
    public static final int canIDStageLeft = 28;
    public static final int canIDStageRight = 29;
    public static final int canIDHood = 30;
    //0.020 is one tick
    public static final double shootWindUp = .3;
    public static final double flywheelRadiusMeters = Units.inchesToMeters(2); // Dummy value


    //dummy values right now
    
    public static final InterpolatingDoubleTreeMap anglesTable = InterpolatingDoubleTreeMap.ofEntries(
      new AbstractMap.SimpleEntry<>(0.0, 0.0),
      new AbstractMap.SimpleEntry<>(0.0, 0.0),
      new AbstractMap.SimpleEntry<>(0.0, 0.0),
      new AbstractMap.SimpleEntry<>(0.0, 0.0)
    );
    public static final InterpolatingDoubleTreeMap rpmTable = InterpolatingDoubleTreeMap.ofEntries(
      new AbstractMap.SimpleEntry<>(0.0, 0.0),
      new AbstractMap.SimpleEntry<>(0.0, 0.0),
      new AbstractMap.SimpleEntry<>(0.0, 0.0),
      new AbstractMap.SimpleEntry<>(0.0, 0.0)
    );

  }

  public static class FieldConstants {
    public static class PosesOfInterest {
      public static final Pose2d redHub = new Pose2d(Units.inchesToMeters(483.11),Units.inchesToMeters(135.09),new Rotation2d(0));
    }

    public static final double fieldLength = VisionConstants.kFieldLayout.getFieldLength();
    public static final double fieldWidth = VisionConstants.kFieldLayout.getFieldWidth();


    /** Hub related constants */
    public static class Hub {

    // Dimensions
      public static final double width = Units.inchesToMeters(47.0);
      public static final double height =
        Units.inchesToMeters(72.0); // includes the catcher at the top
      public static final double innerWidth = Units.inchesToMeters(41.7);
      public static final double innerHeight = Units.inchesToMeters(56.5);

      // Relevant reference points on alliance side
      public static final Translation3d topCenterPoint =
        new Translation3d(
          VisionConstants.kFieldLayout.getTagPose(26).get().getX() + width / 2.0,
            fieldWidth / 2.0,
            height);
      public static final Translation3d innerCenterPoint =
        new Translation3d(
          VisionConstants.kFieldLayout.getTagPose(26).get().getX() + width / 2.0,
            fieldWidth / 2.0,
            innerHeight);

      public static final Translation2d nearLeftCorner =
        new Translation2d(topCenterPoint.getX() - width / 2.0, fieldWidth / 2.0 + width / 2.0);
      public static final Translation2d nearRightCorner =
        new Translation2d(topCenterPoint.getX() - width / 2.0, fieldWidth / 2.0 - width / 2.0);
      public static final Translation2d farLeftCorner =
        new Translation2d(topCenterPoint.getX() + width / 2.0, fieldWidth / 2.0 + width / 2.0);
      public static final Translation2d farRightCorner =
        new Translation2d(topCenterPoint.getX() + width / 2.0, fieldWidth / 2.0 - width / 2.0);

      // Relevant reference points on the opposite side
      public static final Translation3d oppTopCenterPoint =
        new Translation3d(
          VisionConstants.kFieldLayout.getTagPose(4).get().getX() + width / 2.0,
            fieldWidth / 2.0,
            height);
      public static final Translation3d oppInnerCenterPoint =
        new Translation3d(
          VisionConstants.kFieldLayout.getTagPose(4).get().getX() + width / 2.0,
            fieldWidth / 2.0,
            innerHeight);
      public static final Translation2d oppNearLeftCorner =
        new Translation2d(oppTopCenterPoint.getX() - width / 2.0, fieldWidth / 2.0 + width / 2.0);
      public static final Translation2d oppNearRightCorner =
        new Translation2d(oppTopCenterPoint.getX() - width / 2.0, fieldWidth / 2.0 - width / 2.0);
      public static final Translation2d oppFarLeftCorner =
        new Translation2d(oppTopCenterPoint.getX() + width / 2.0, fieldWidth / 2.0 + width / 2.0);
      public static final Translation2d oppFarRightCorner =
        new Translation2d(oppTopCenterPoint.getX() + width / 2.0, fieldWidth / 2.0 - width / 2.0);

      // Hub faces
      public static final Pose2d nearFace =
        VisionConstants.kFieldLayout.getTagPose(26).get().toPose2d();
      public static final Pose2d farFace =
        VisionConstants.kFieldLayout.getTagPose(20).get().toPose2d();
      public static final Pose2d rightFace =
        VisionConstants.kFieldLayout.getTagPose(18).get().toPose2d();
      public static final Pose2d leftFace =
        VisionConstants.kFieldLayout.getTagPose(21).get().toPose2d();
    }
  }
}
