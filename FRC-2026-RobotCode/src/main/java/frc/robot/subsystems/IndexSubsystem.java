// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import frc.robot.Constants.Index;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IndexSubsystem extends SubsystemBase {
  private SparkMax m_index = new SparkMax(Index.canIDIndex, MotorType.kBrushless);
  //private RelativeEncoder m_encoderIndex = m_index.getEncoder();

  private ShuffleboardTab tab = Shuffleboard.getTab("Testing Variables");
  private GenericEntry speed = tab.add("Speed of Index", 0.6).getEntry();

  //private SparkMax m_hood = new SparkMax(30, MotorType.kBrushless);
  /** Creates a new Intake. */
  public IndexSubsystem() {

  }

  public void indexToStage (boolean On){
    if (On){
    double power = speed.getDouble(0.6);
    m_index.set(-power);
    }
    else {
      m_index.set(0);
    }
  }

  public void indexFromStage (boolean On){
    if (On){
      double power = speed.getDouble(0.6);
      m_index.set(power);
    }
    else {
      m_index.set(0);
    }
  }



  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
