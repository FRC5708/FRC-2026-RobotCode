// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import frc.robot.Constants.Intake;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.Constants.Intake;

public class IntakeSubsystem extends SubsystemBase {
  private SparkMax m_deploy = new SparkMax(Intake.canIDDeploy, MotorType.kBrushless);
  private SparkMax m_intake = new SparkMax(Intake.canIDIntake, MotorType.kBrushless);
  //private RelativeEncoder m_intake_encoder = m_intake.getEncoder();
  //private RelativeEncoder m_deploy_encoder = m_deploy.getEncoder();
  private double stoppedSpeed = 0.1;
  private DigitalInput forwardCheck = new DigitalInput(0);
  private DigitalInput backwardCheck = new DigitalInput(1);

  private boolean in = true;
  /** Creates a new Intake. */
  public IntakeSubsystem() {

  }

  public void testDeploy(double power) {
    m_deploy.set(power);
  }

  public void toggleIn() {
    in = !in;
  }
  public void deploy(double power) {

    if (in && !backwardCheck.get()) {
      //m_deploy.set(-power);
    }
    
    else if (!in && !forwardCheck.get()) {
      //m_deploy.set(power);
    }
  
    //else m_deploy.set(0.0);

    // System.out.println(m_deploy_encoder.getVelocity());

    // if (in) {
    //   if (m_deploy_encoder.getVelocity() < stoppedSpeed ){
    //     m_deploy.set(-power);
    //   }
    //   else {
    //     m_deploy.set(0);
    //   }
    // }
    // if (!in){
    //   if (m_deploy_encoder.getVelocity() < stoppedSpeed ){
    //     m_deploy.set(power);
      
    //   }
    //   else {
    //     m_deploy.set(0);
    //   }
    // }
    
  }
  public void intake(double power) {
    m_intake.set(-power);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
