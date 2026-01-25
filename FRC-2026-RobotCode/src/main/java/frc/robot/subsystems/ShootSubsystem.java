// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;


public class ShootSubsystem extends SubsystemBase {
  private SparkMax m_shootLeft = new SparkMax(20, MotorType.kBrushless);
  private SparkMax m_shootRight = new SparkMax(21, MotorType.kBrushless);
  
  private SparkMax m_indexOne = new SparkMax(23, MotorType.kBrushless);
  private SparkMax m_indexTwo = new SparkMax(24, MotorType.kBrushless);

  private ShuffleboardTab tab = Shuffleboard.getTab("Test Shoot");
  private GenericEntry speed = 
    tab.add("Speed",0.1)
      .getEntry();

  private SparkMax m_hood = new SparkMax(22, MotorType.kBrushless);
  /** Creates a new Intake. */
  public ShootSubsystem() {

  }

  public void shoot(double power) {
    m_shootLeft.set(power);
    m_shootRight.set(power);
  }

  public void testShoot() {
    double power = speed.getDouble(0.1);
    m_shootLeft.set(power);
    m_shootRight.set(power);
  }

  public void indexer(double speed) {
    m_indexOne.set(speed);
    m_indexTwo.set(speed);
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
