// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import frc.robot.Constants.Intake;
import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
//import edu.wpi.first.wpilibj.DigitalInput;

public class IntakeSubsystem extends SubsystemBase {
  private SparkMax m_deploy = new SparkMax(Intake.canIDDeploy, MotorType.kBrushless);
  private SparkMax m_intake = new SparkMax(Intake.canIDIntake, MotorType.kBrushless);
  private RelativeEncoder m_intake_encoder = m_intake.getEncoder();
  public static final double homingDebounceTime = 0.25;
  public static final double homingVelocityThreshold = 0.1;
  private Debouncer homingDebouncer;
  private boolean homed;
  //private RelativeEncoder m_deploy_encoder = m_deploy.getEncoder();
  ///private DigitalInput forwardCheck = new DigitalInput(0);
  //private DigitalInput backwardCheck = new DigitalInput(1);

  private ShuffleboardTab tab = Shuffleboard.getTab("Testing Variables");
  private GenericEntry deployCurrent = tab.add("Deploy Current", 0).getEntry();
  private GenericEntry deployVoltage = tab.add("Deploy Voltage", 0).getEntry();

  private boolean in = true;
  /** Creates a new Intake. */
  public IntakeSubsystem() {
  }


  public void toggleIn() {
    in = !in;
  }
  public void deploy(double power) {

    // if (in && !backwardCheck.get()) {
    //   //m_deploy.set(-power);
    // }
    
    // else if (!in && !forwardCheck.get()) {
    //   //m_deploy.set(power);
    // }
    m_deploy.set(power);
  }

  public void intake(double power) {
    m_intake.set(power);
  }

  public void reverseIntake(double power) {
    m_intake.set(-power);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    deployCurrent.setDouble(m_deploy.getOutputCurrent());
    deployVoltage.setDouble(m_deploy.get());
  }

  //ts is actually so cooked ong. Yes i am too lazy to change the varuiable names
   public Command runIntake(double power) {
        return startRun(
            () -> {
                homingDebouncer = new Debouncer(homingDebounceTime);
                homingDebouncer.calculate(false);
                intake(power);
            },
            () -> {
                homed = homingDebouncer.calculate(Math.abs(m_intake_encoder.getVelocity()) <= homingVelocityThreshold);
            }
        )
        .until(() -> homed)
        .andThen(() -> intake(0));
    }
}
