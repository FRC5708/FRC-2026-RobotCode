// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.IndexSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class RetractIntake extends Command {
  private final IntakeSubsystem m_intake; 
  private final IndexSubsystem m_index;
  private double m_power;
  private double m_startTime;
  /** Creates a new Intake. */
  public RetractIntake(IntakeSubsystem intake, IndexSubsystem index, double power) {
    // Use addRequirements
    m_intake = intake;
    m_index = index;
    m_power = power;
    addRequirements(m_intake,m_index);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_startTime = System.currentTimeMillis();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_intake.reverseIntake(m_power);
    m_index.indexFromStage(true);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_intake.reverseIntake(0);
    m_index.indexFromStage(false);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return System.currentTimeMillis() - m_startTime > 1000;
  }
}
