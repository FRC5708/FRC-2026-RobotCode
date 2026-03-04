// Copyright 2026 Team 5708
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.StatusSignal;
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
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.ResetMode;
import com.revrobotics.PersistMode;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.Shooter;

public class ShootSubsystem extends SubsystemBase {
  public int hoodSetPoint = 2;

  private TalonFXS m_shootLeftSecondary;
  private TalonFXS m_shootRightPrime;

  private final VoltageOut voltageRequest = new VoltageOut(0);
  private final VelocityVoltage velocityRequest = new VelocityVoltage(0);

  private final StatusSignal<AngularVelocity> leftShooterVelocity;
  private final StatusSignal<AngularVelocity> rightShooterVelocity;
  
  private SparkMax m_stageLeft = new SparkMax(Shooter.canIDStageLeft, MotorType.kBrushless);
  private SparkMax m_stageRight = new SparkMax(Shooter.canIDStageRight, MotorType.kBrushless);

  private RelativeEncoder m_stageEncoderLeft = m_stageLeft.getEncoder();
  private RelativeEncoder m_stageEncoderRight = m_stageRight.getEncoder();

  private SparkMax m_hood = new SparkMax(Shooter.canIDHood, MotorType.kBrushless);
  private RelativeEncoder m_hoodEncoder;
  private SparkClosedLoopController m_hoodPidController;

  private ShuffleboardTab tab = Shuffleboard.getTab("Testing Variables");
  private GenericEntry targetDistance = tab.add("Target Distance",50).getEntry();
  // private GenericEntry powerLeft = tab.add("Power going into Left", 0).getEntry();
  private GenericEntry powerRight = tab.add("Power going into Right", 0).getEntry();
  // private GenericEntry velocityLeft = tab.add("Velocity Left", 0).getEntry();
  private GenericEntry velocityRight = tab.add("Velocity Right", 0).getEntry();

  private GenericEntry hoodPos = tab.add("Hood Pos", 0).getEntry();
  private GenericEntry hoodPosIndex = tab.add("Hood Index", 0).getEntry();
  private GenericEntry hoodPosTarget = tab.add("Hood Target", 0).getEntry();
  private GenericEntry hoodPower = tab.add("Hood Power", 0).getEntry();


  // private GenericEntry stagePowerLeft = tab.add("Stage Power going into Left", 0).getEntry();
  // private GenericEntry stagePowerRight = tab.add("Stage Power going into Rightt", 0).getEntry();
  // private GenericEntry stageVelocityLeft = tab.add("Stage Velocity Left", 0).getEntry();
  // private GenericEntry stageVelocityRight = tab.add("Stage Velocity Right", 0).getEntry();
    

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
    rightShooterVelocity = m_shootRightPrime.getVelocity();

    TalonFXSConfiguration secondaryShooterMotorConfig = new TalonFXSConfiguration();
    secondaryShooterMotorConfig.Commutation.MotorArrangement = MotorArrangementValue.NEO_JST;
    secondaryShooterMotorConfig.ExternalFeedback.ExternalFeedbackSensorSource = ExternalFeedbackSensorSourceValue.Commutation;

    secondaryShooterMotorConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

    secondaryShooterMotorConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast;

    m_shootLeftSecondary = new TalonFXS(Shooter.canIDShootLeft);
    m_shootLeftSecondary.getConfigurator().apply(secondaryShooterMotorConfig);
    m_shootLeftSecondary.setControl(new Follower(Shooter.canIDShootRight, MotorAlignmentValue.Opposed));
    leftShooterVelocity = m_shootLeftSecondary.getVelocity();

    //PID Controls set for the hood
    m_hoodEncoder = m_hood.getEncoder();
    m_hoodPidController = m_hood.getClosedLoopController();
    SparkMaxConfig config = new SparkMaxConfig();

    config
      .smartCurrentLimit(40)
      .idleMode(IdleMode.kBrake);
    
    //Sets this for the NEO encoder conversionfactor
    config.encoder
      .positionConversionFactor(1)
      .velocityConversionFactor(1);

    // Set PID gains for hood position control
    // TODO Having our best shooting positions between 0.5 and 2.0 means
    // that it's difficult to avoid windup when the starting position is 0.
    // Try resetting the encoder so our positions are between 2.5 and 4.0
    // and then a more aggressive p could be used. The iMaxAccum config may
    // be useful too. (Use m_hoodPidController.getIAccum() to view windup.)
    config.closedLoop
      .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
      .outputRange(-0.1, 0.1, ClosedLoopSlot.kSlot1)
      .p(0.3, ClosedLoopSlot.kSlot1)
      .i(0.0, ClosedLoopSlot.kSlot1)
      .d(0.0, ClosedLoopSlot.kSlot1);

    config.closedLoop.feedForward
      .kS(.1, ClosedLoopSlot.kSlot1);

    m_hood.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
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

  public void hood(double setPoint){
    hoodPosTarget.setDouble(setPoint);
    m_hoodPidController.setSetpoint(setPoint, ControlType.kPosition, ClosedLoopSlot.kSlot1);
  }

  public void resetHoodEncoder(){
    m_hoodEncoder.setPosition(0);
  }

  public void changeHoodSetpoint (){
    ++hoodSetPoint;
    if (hoodSetPoint > 4) {
      hoodSetPoint = 1;
    }
  }

  @Override
  public void periodic() {
    // double m_shooterPowerLeft = m_shootLeftSecondary.get();
    double m_shooterPowerRight = m_shootRightPrime.get();
    // double m_shooterVelocityLeft = getLeftShooterVelocity();
    double m_shooterVelocityRight = getRightShooterVelocity();

    // double m_stagePowerLeft = m_stageLeft.get();
    // double m_stagePowerRight = m_stageRight.get();
    // double m_stageVelocityLeft = m_stageEncoderLeft.getVelocity();
    // double m_stageVelocityRight = m_stageEncoderRight.getVelocity();

    // powerLeft.setDouble(m_shooterPowerLeft);
    powerRight.setDouble(m_shooterPowerRight);
    // velocityLeft.setDouble(m_shooterVelocityLeft);
    velocityRight.setDouble(m_shooterVelocityRight);

    hoodPos.setDouble(m_hoodEncoder.getPosition());
    hoodPower.setDouble(m_hood.getAppliedOutput());
    hoodPosIndex.setInteger(getHoodSetpoint());

    // stagePowerLeft.setDouble(m_stagePowerLeft);
    // stagePowerRight.setDouble(m_stagePowerRight);
    // stageVelocityLeft.setDouble(m_stageVelocityLeft);
    // stageVelocityRight.setDouble(m_stageVelocityRight);
  }

  public int getHoodSetpoint (){
    return hoodSetPoint;
  }

  public double getLeftStagePower() {
    return m_stageLeft.get();
  }

  public double getRightStagePower() {
    return m_stageRight.get();
  }

  public double getLeftStageVelocity() {
    return m_stageEncoderLeft.getVelocity();
  }

  public double getRightStageVelocity() {
    return m_stageEncoderRight.getVelocity();
  }

  public double getLeftShooterPower() {
    return m_shootLeftSecondary.get();
  }

  public double getRightShooterPower() {
    return m_shootRightPrime.get();
  }


  public double getLeftShooterVelocity() {
    return leftShooterVelocity.getValueAsDouble();
  }

  public double getRightShooterVelocity() {
    return rightShooterVelocity.getValueAsDouble();
  }

  public double getHoodPos() {
    return m_hoodEncoder.getPosition();
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

  public void hoodDown(double power) {
  m_hood.set(-power);
  }

  public void hoodUp(double power){
    m_hood.set(power);
  } 

  public void stageLeft(double speed) {
    m_stageLeft.set(-speed);
  }

  public void stageRight(double speed) {
    m_stageRight.set(-speed);
  }
}
