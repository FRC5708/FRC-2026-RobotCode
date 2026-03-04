// Copyright 2026 Team 5708
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.Shooter;
import frc.robot.subsystems.IndexSubsystem;
import frc.robot.subsystems.ShootSubsystem;
import frc.robot.subsystems.IntakeSubsystem;

public class Shoot extends Command {
  private final ShootSubsystem m_shoot; 
  private final IndexSubsystem m_index;
  private final IntakeSubsystem m_intake;
  private final Timer m_timer = new Timer();

  public Shoot(ShootSubsystem shoot, IndexSubsystem index, IntakeSubsystem intake) {
    m_shoot = shoot;
    m_index = index;
    m_intake = intake;
  }

  @Override
  public void initialize() {
    m_timer.reset();
    m_timer.start(); 
  }

  @Override
  public void execute() {
    // TODO refactor the hood setpoint into m_shoot.shoot() so
    // that the hood position is automatically adjusted for the
    // shot distance (which controls shoot speed).
    switch (m_shoot.getHoodSetpoint()) {
      case 1:
        // against the HUB
        m_shoot.hood(0.1);
        break;
      case 2:
        // from 6 feet out
        m_shoot.hood(0.5);
        break;
      case 3:
        // against the side field wall
        m_shoot.hood(0.9);
        break;
      case 4:
        // passing between zones
        m_shoot.hood(3.0);
      default:
        break;
    }

    if (m_timer.hasElapsed(Shooter.shootWindUp)){
      m_shoot.shoot(true);
      m_shoot.stage(-1);
      m_index.indexToStage(true);
      m_intake.intake(.2);
    }
    else {
      m_shoot.shoot(true);
      m_shoot.stage(.4);
      m_index.indexFromStage(true);
    }
  }

  @Override
  public void end(boolean interrupted) {
    m_shoot.shoot(false);
    m_shoot.stage(0);
    m_index.indexToStage(false);
    m_intake.intake(0);
    m_shoot.hoodDown(0);
    m_timer.stop();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
