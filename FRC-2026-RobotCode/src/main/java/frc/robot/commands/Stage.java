// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ShootSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class Stage extends Command {
  private final ShootSubsystem m_shoot; 
  private final Timer m_timer = new Timer();
  private double m_power;
  /** Creates a new indexer. */
  public Stage(ShootSubsystem shoot, double power) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_shoot = shoot;
    m_power = power;
    addRequirements(m_shoot);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_timer.reset();
    m_timer.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //if (m_timer.hasElapsed(1.5)) {
      m_shoot.stageLeft(m_power);
    //}
    //else {
      //m_shoot.stageRight(m_power);
    //}
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_shoot.stageLeft(0);
    m_shoot.stageRight(0);
    m_timer.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
