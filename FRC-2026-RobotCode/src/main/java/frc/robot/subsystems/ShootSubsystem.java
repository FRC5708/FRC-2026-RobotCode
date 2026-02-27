// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXSConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFXS;
import com.ctre.phoenix6.signals.ExternalFeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.MotorArrangementValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.Shooter;


public class ShootSubsystem extends SubsystemBase {
  private TalonFXS m_shootLeftSecondary;
  private TalonFXS m_shootRightPrime;

  private final VoltageOut voltageRequest = new VoltageOut(0);
  private final VelocityVoltage velocityRequest = new VelocityVoltage(0);
  
  private SparkMax m_stageLeft = new SparkMax(Shooter.canIDStageLeft, MotorType.kBrushless);
  private SparkMax m_stageRight = new SparkMax(Shooter.canIDStageRight, MotorType.kBrushless);

  private RelativeEncoder m_stageEncoderLeft = m_stageLeft.getEncoder();
  private RelativeEncoder m_stageEncoderRight = m_stageRight.getEncoder();

  private SparkMax m_hood = new SparkMax(Shooter.canIDHood, MotorType.kBrushless);
  private RelativeEncoder m_encoderHood = m_hood.getEncoder();


  private ShuffleboardTab tab = Shuffleboard.getTab("Testing Variables");
  private GenericEntry targetDistance = tab.add("Target Distance",50).getEntry();
  private GenericEntry powerLeft = tab.add("Power going into Left", 0).getEntry();
  private GenericEntry powerRight = tab.add("Power going into Rightt", 0).getEntry();
  private GenericEntry velocityLeft = tab.add("Velocity Left", 0).getEntry();
  private GenericEntry velocityRight = tab.add("Velocity Right", 0).getEntry();

  private GenericEntry hoodPosition = tab.add("Hood Pos", 0).getEntry();

  private GenericEntry stagePowerLeft = tab.add("Stage Power going into Left", 0).getEntry();
  private GenericEntry stagePowerRight = tab.add("Stage Power going into Rightt", 0).getEntry();
  private GenericEntry stageVelocityLeft = tab.add("Stage Velocity Left", 0).getEntry();
  private GenericEntry stageVelocityRight = tab.add("Stage Velocity Right", 0).getEntry();
    

  //private SparkMax m_hood = new SparkMax(30, MotorType.kBrushless);
  /** Creates a new Intake. */
  public ShootSubsystem() {
    TalonFXSConfiguration primeShooterMotorConfig = new TalonFXSConfiguration();
    primeShooterMotorConfig.Commutation.MotorArrangement = MotorArrangementValue.NEO_JST;
    primeShooterMotorConfig.ExternalFeedback.ExternalFeedbackSensorSource = ExternalFeedbackSensorSourceValue.Commutation;

    primeShooterMotorConfig.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;

    primeShooterMotorConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast;
    
    //the 12 in kV is coming from us changing the power linear to apply to voltage
    //So kV should always be at .0107*12
    //the kP is best so far at .2
    //the kI is best so far at .1
    //With current weight 60 speed/target is about the upper bounds of reliable shots
    primeShooterMotorConfig.Slot0.kS = 0;
    primeShooterMotorConfig.Slot0.kV = 0.0107*12;
    primeShooterMotorConfig.Slot0.kA = 0;
    primeShooterMotorConfig.Slot0.kP = 0.3;
    primeShooterMotorConfig.Slot0.kI = 0.1;
    primeShooterMotorConfig.Slot0.kD = 0;
    
    primeShooterMotorConfig.Slot0.StaticFeedforwardSign = StaticFeedforwardSignValue.UseVelocitySign;
    
    m_shootRightPrime = new TalonFXS(Shooter.canIDShootRight);
    m_shootRightPrime.getConfigurator().apply(primeShooterMotorConfig);

    TalonFXSConfiguration secondaryShooterMotorConfig = new TalonFXSConfiguration();
    secondaryShooterMotorConfig.Commutation.MotorArrangement = MotorArrangementValue.NEO_JST;
    secondaryShooterMotorConfig.ExternalFeedback.ExternalFeedbackSensorSource = ExternalFeedbackSensorSourceValue.Commutation;

    secondaryShooterMotorConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

    secondaryShooterMotorConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast;

    m_shootLeftSecondary = new TalonFXS(Shooter.canIDShootLeft);
    m_shootLeftSecondary.getConfigurator().apply(secondaryShooterMotorConfig);
    m_shootLeftSecondary.setControl(new Follower(Shooter.canIDShootRight, MotorAlignmentValue.Opposed));
  }

  public void shoot(boolean shootGood) {
    double speed = targetDistance.getDouble(50);
    if (shootGood){
      m_shootRightPrime.setControl(velocityRequest.withVelocity(speed));
    }
    else {
      m_shootRightPrime.set(0);
    }
  }

  public void stage(double speed) {
    m_stageLeft.set(-speed);
    m_stageRight.set(-speed);
  }

  public void stageLeft(double speed) {
    m_stageLeft.set(-speed);
  }
  public void stageRight(double speed) {
    m_stageRight.set(-speed);
  }

  public void hoodDown(double power) {
    m_hood.set(-power);
}

  public void hoodUp(double power){
    m_hood.set(power);
  } 

  @Override
  public void periodic() {
    double m_shooterPowerLeft = m_shootLeftSecondary.get();
    double m_shooterPowerRight = m_shootRightPrime.get();
    double m_shooterVelocityLeft = m_shootLeftSecondary.getVelocity().getValueAsDouble();
    double m_shooterVelocityRight = m_shootRightPrime.getVelocity().getValueAsDouble();

    double m_hoodPos = m_encoderHood.getPosition();

    double m_stagePowerLeft = m_stageLeft.get();
    double m_stagePowerRight = m_stageRight.get();
    double m_stageVelocityLeft = m_stageEncoderLeft.getVelocity();
    double m_stageVelocityRight = m_stageEncoderRight.getVelocity();

    powerLeft.setDouble(m_shooterPowerLeft);
    powerRight.setDouble(m_shooterPowerRight);
    velocityLeft.setDouble(m_shooterVelocityLeft);
    velocityRight.setDouble(m_shooterVelocityRight);

    hoodPosition.setDouble(m_hoodPos);

    stagePowerLeft.setDouble(m_stagePowerLeft);
    stagePowerRight.setDouble(m_stagePowerRight);
    stageVelocityLeft.setDouble(m_stageVelocityLeft);
    stageVelocityRight.setDouble(m_stageVelocityRight);
  }

  // private double velocityToProperSpeed(StatusSignal<AngularVelocity> velocityStatusSignal, double properSpeed, double thresh) {
  //   double velocity = velocityStatusSignal.getValueAsDouble();
    
  //   //velocity /= 1300; // Hopefully converts from RPM to percent
  //   double returnSpeed = properSpeed; 
  //   if (velocity < thresh && properSpeed > thresh) {
  //       returnSpeed += properSpeed-velocity;
  //   }
  //   return returnSpeed;
  // }
}
