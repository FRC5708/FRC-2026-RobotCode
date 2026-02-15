// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class ShootSubsystem extends SubsystemBase {
  private SparkMax m_shootLeft = new SparkMax(26, MotorType.kBrushless);
  private SparkMax m_shootRight = new SparkMax(27, MotorType.kBrushless);
  private RelativeEncoder m_encoderLeft = m_shootLeft.getEncoder();
  private RelativeEncoder m_encoderRight = m_shootRight.getEncoder();
  
  private SparkMax m_stageLeft = new SparkMax(28, MotorType.kBrushless);
  private SparkMax m_stageRight = new SparkMax(29, MotorType.kBrushless);

  private SparkMax m_hood = new SparkMax(30, MotorType.kBrushless);

  private ShuffleboardTab tab = Shuffleboard.getTab("Testing Variables");
  private GenericEntry speed = 
    tab.add("Speed of Shooter",0.1)
      .getEntry();

  //private SparkMax m_hood = new SparkMax(30, MotorType.kBrushless);
  /** Creates a new Intake. */
  public ShootSubsystem() {

  }

  private double velocityToProperSpeed(double velocity, double properSpeed, double thresh) {
    velocity /= 5676; // Hopefully converts from RPM to percent
    double returnSpeed = properSpeed; 
    if (velocity < thresh && properSpeed > thresh) {
        returnSpeed += properSpeed-velocity;
    }
    return returnSpeed;
  }

  public void shoot(double power) {
    double thresh = 0.5;
    stage(-.4);
    //m_shootLeft.set(1);
    //m_shootRight.set(-1);
    m_shootLeft.set(velocityToProperSpeed(m_encoderLeft.getVelocity(),-power, thresh));
    m_shootRight.set(velocityToProperSpeed(m_encoderRight.getVelocity(),power, thresh));
  }

  public void testShoot() {
    double thresh = 0.5;
    double power = speed.getDouble(0.1);
    m_shootLeft.set(velocityToProperSpeed(m_encoderLeft.getVelocity(),-power, thresh));
    m_shootRight.set(velocityToProperSpeed(m_encoderLeft.getVelocity(),power, thresh));
  }

  public void testAllShootSystems() {
    double thresh = 0.5;
    stage(-.4);
    double power = speed.getDouble(0.1);
    m_shootLeft.set(velocityToProperSpeed(m_encoderLeft.getVelocity(),-power, thresh));
    m_shootRight.set(velocityToProperSpeed(m_encoderLeft.getVelocity(),power, thresh));
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
    // This method will be called once per scheduler run
  }
}
