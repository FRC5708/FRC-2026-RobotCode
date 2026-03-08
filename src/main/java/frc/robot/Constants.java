// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.AbstractMap;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.util.Units;


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
      public static final double kP = 1.3;
      public static final double kI = 0.000003;
      public static final double kD = 0.0003;
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
    public static final double shootWindUp = .6;
    public static final double flywheelRadiusMeters = Units.inchesToMeters(2); // Dummy value


    //dummy values right now
    
    public static final InterpolatingDoubleTreeMap hoodPosTable = InterpolatingDoubleTreeMap.ofEntries(
      new AbstractMap.SimpleEntry<>(0.25, 0.25),
      new AbstractMap.SimpleEntry<>(2.0, 1.1),
      new AbstractMap.SimpleEntry<>(2.38, 1.4),
      new AbstractMap.SimpleEntry<>(2.53, 1.4)
    );
    public static final InterpolatingDoubleTreeMap shootPowerTable = InterpolatingDoubleTreeMap.ofEntries(
      new AbstractMap.SimpleEntry<>(0.0, 50.0),
      new AbstractMap.SimpleEntry<>(2.0, 50.0),
      new AbstractMap.SimpleEntry<>(2.38, 50.0),
      new AbstractMap.SimpleEntry<>(2.53, 55.5)
    );

  }
}
