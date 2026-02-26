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

  // private RelativeEncoder m_encoderLeft = m_shootLeft.getEncoder();
  // private RelativeEncoder m_encoderRight = m_shootRight.getEncoder();
  
  private SparkMax m_stageLeft = new SparkMax(Shooter.canIDStageLeft, MotorType.kBrushless);
  private SparkMax m_stageRight = new SparkMax(Shooter.canIDStageRight, MotorType.kBrushless);

  private SparkMax m_hood = new SparkMax(Shooter.canIDHood, MotorType.kBrushless);
  private RelativeEncoder m_encoderHood = m_hood.getEncoder();


  private ShuffleboardTab tab = Shuffleboard.getTab("Testing Variables");
  private GenericEntry targetSpeed = tab.add("Target Speed",0.55).getEntry();
  private GenericEntry powerLeft = tab.add("Power going into Left", 0).getEntry();
  private GenericEntry powerRight = tab.add("Power going into Rightt", 0).getEntry();
  private GenericEntry velocityLeft = tab.add("Velocity Left", 0).getEntry();
  private GenericEntry velocityRight = tab.add("Velocity Right", 0).getEntry();
  private GenericEntry hoodPosition = tab.add("Hood Pos", 0).getEntry();
    

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

  public void shoot(double power) {
    //double thresh = 0.5;
    stage(-.5);
    double speed = targetSpeed.getDouble(0.55);
    if (0.0001 > power && power > -0.0001){
      m_shootRightPrime.setControl(voltageRequest.withOutput(0));
      // Once a motor is controlled directly, it seems like the follower
      // mode is disabled and it reverts to direct control. Maybe follower
      // should be dynamically set at the start of shooting?
      //m_shootLeftSecondary.set(0);
    }
    else {
       m_shootRightPrime.setControl(velocityRequest.withVelocity(speed));
      //m_shootRightPrime.set(speed);
      //m_shootLeftSecondary.set(1);
      //m_shootRightPrime.setControl(voltageRequest.withOutput(1));
    }
  }

  public void testShoot() {
    //double thresh = 0.5;
    double power = targetSpeed.getDouble(0.1);
    stage(.75);
    m_shootRightPrime.set(power);
    // m_shootLeft.set(velocityToProperSpeed(m_shootLeft.getVelocity(),-power, thresh));
    // m_shootRight.set(velocityToProperSpeed(m_shootRight.getVelocity(),power, thresh));
  }

  public void testAllShootSystems() {
    double thresh = 0.5;
    stage(-.4);
    double power = targetSpeed.getDouble(0.1);
    m_shootRightPrime.set(power);
  }

  public void stage(double speed) {
    m_stageLeft.set(speed);
    m_stageRight.set(speed);
  }

  

  public void hoodDown(double power) {
    m_hood.set(-power);
}

  public void hoodUp(double power){
    m_hood.set(power);
  } 

  @Override
  public void periodic() {
    double shootingPowerLeft = m_shootLeftSecondary.get();
    double shootingPowerRight = m_shootRightPrime.get();
    double activeVelocityLeft = m_shootLeftSecondary.getVelocity().getValueAsDouble();
    double activeVelocityRight = m_shootRightPrime.getVelocity().getValueAsDouble();
    double hoodPos = m_encoderHood.getPosition();

    powerLeft.setDouble(shootingPowerLeft);
    powerRight.setDouble(shootingPowerRight);
    velocityLeft.setDouble(activeVelocityLeft);
    velocityRight.setDouble(activeVelocityRight);
    hoodPosition.setDouble(hoodPos);
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
