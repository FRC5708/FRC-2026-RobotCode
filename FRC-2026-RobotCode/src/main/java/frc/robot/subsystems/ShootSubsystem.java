// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXSConfiguration;
import com.ctre.phoenix6.hardware.TalonFXS;
import com.ctre.phoenix6.signals.ExternalFeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.MotorArrangementValue;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class ShootSubsystem extends SubsystemBase {
  private TalonFXS m_shootLeft = new TalonFXS(26);
  private TalonFXS m_shootRight = new TalonFXS(27);
  // private RelativeEncoder m_encoderLeft = m_shootLeft.getEncoder();
  // private RelativeEncoder m_encoderRight = m_shootRight.getEncoder();
  
  private SparkMax m_stageLeft = new SparkMax(28, MotorType.kBrushless);
  private SparkMax m_stageRight = new SparkMax(29, MotorType.kBrushless);

  private SparkMax m_hood = new SparkMax(30, MotorType.kBrushless);
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
    TalonFXSConfiguration conf = new TalonFXSConfiguration();
    conf.Commutation.MotorArrangement = MotorArrangementValue.NEO_JST;
    conf.ExternalFeedback.ExternalFeedbackSensorSource = ExternalFeedbackSensorSourceValue.Commutation;
    m_shootLeft.getConfigurator().apply(conf);
    m_shootRight.getConfigurator().apply(conf);
  }

  private double velocityToProperSpeed(StatusSignal<AngularVelocity> velocityStatusSignal, double properSpeed, double thresh) {
    double velocity = velocityStatusSignal.getValueAsDouble();
    
    //velocity /= 1300; // Hopefully converts from RPM to percent
    double returnSpeed = properSpeed; 
    if (velocity < thresh && properSpeed > thresh) {
        returnSpeed += properSpeed-velocity;
    }
    return returnSpeed;
  }

  public void shoot(double power) {
    //double thresh = 0.5;
    stage(-.4);
    m_shootLeft.set(-power);
    m_shootRight.set(power);
    //m_shootLeft.set(velocityToProperSpeed(m_shootLeft.getVelocity(),-power, thresh));
    //m_shootRight.set(velocityToProperSpeed(m_shootRight.getVelocity(),power, thresh));
  }

  public void testShoot() {
    //double thresh = 0.5;
    double power = targetSpeed.getDouble(0.1);
    stage(-.4);
    m_shootLeft.set(-power);
    m_shootRight.set(power);
    // m_shootLeft.set(velocityToProperSpeed(m_shootLeft.getVelocity(),-power, thresh));
    // m_shootRight.set(velocityToProperSpeed(m_shootRight.getVelocity(),power, thresh));
  }

  public void testAllShootSystems() {
    double thresh = 0.5;
    stage(-.4);
    double power = targetSpeed.getDouble(0.1);
    m_shootLeft.set(velocityToProperSpeed(m_shootLeft.getVelocity(),-power, thresh));
    m_shootRight.set(velocityToProperSpeed(m_shootRight.getVelocity(),power, thresh));
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
    double shootingPowerLeft = m_shootLeft.get();
    double shootingPowerRight = m_shootRight.get();
    double activeVelocityLeft = m_shootLeft.getVelocity().getValueAsDouble();
    double activeVelocityRight = m_shootRight.getVelocity().getValueAsDouble();
    double hoodPos = m_encoderHood.getPosition();

    powerLeft.setDouble(shootingPowerLeft);
    powerRight.setDouble(shootingPowerRight);
    velocityLeft.setDouble(activeVelocityLeft);
    velocityRight.setDouble(activeVelocityRight);
    hoodPosition.setDouble(hoodPos);
  }
}
