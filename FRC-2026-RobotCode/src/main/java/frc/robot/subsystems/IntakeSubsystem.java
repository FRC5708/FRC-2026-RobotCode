// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

public class IntakeSubsystem extends SubsystemBase {
  private SparkMax m_deploy = new SparkMax(10, MotorType.kBrushless);
  private SparkMax m_intake = new SparkMax(11, MotorType.kBrushless);
  /** Creates a new Intake. */
  public IntakeSubsystem() {

  }

  public void deploy(double power) {
    m_deploy.set(power);
  }
  public void intake(double power) {
    m_intake.set(power);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
