// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import frc.robot.Constants.Intake;
import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
//import edu.wpi.first.wpilibj.DigitalInput;

@Logged
public class IntakeSubsystem extends SubsystemBase {
  private SparkMax m_deploy = new SparkMax(Intake.canIDDeploy, MotorType.kBrushless);
  private SparkMax m_intake = new SparkMax(Intake.canIDIntake, MotorType.kBrushless);
  private RelativeEncoder m_deploy_encoder = m_deploy.getEncoder();


  private ShuffleboardTab tab = Shuffleboard.getTab("Testing Variables");
  private GenericEntry deployCurrent = tab.add("Deploy Current", 0).getEntry();
  private GenericEntry deployVoltage = tab.add("Deploy Voltage", 0).getEntry();
  private GenericEntry deployVelocity = tab.add("Deploy Velocity", 0).getEntry();

  private boolean in = true;
  /** Creates a new Intake. */
  public IntakeSubsystem() {
  }

  public double getDeployPower() {
    return m_deploy.getAppliedOutput();
  }


  public void toggleIn() {
    in = !in;
  }

  //So yes I did change all of the duty cycles to power
  //I understand that you are more corret but it makes it harder to read for others
  //Duty cycle is very specfic word that could have others miss-think so please use power... like everywhere else
  public void deploy(double power) {
    m_deploy.set(power);
  }

  public double getDeployVelocity() {
    return m_deploy_encoder.getVelocity();
  }

  public void intake(double dutyCycle) {
    m_intake.set(dutyCycle);
  }

  public void reverseIntake(double dutyCycle) {
    m_intake.set(-dutyCycle);
  }

  public double getDeployCurrent() {
    return m_deploy.getOutputCurrent();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    deployCurrent.setDouble(m_deploy.getOutputCurrent());
    deployVoltage.setDouble(m_deploy.get());
    deployVelocity.setDouble(getDeployVelocity());
  }
}
