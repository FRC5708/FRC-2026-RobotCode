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
    addRequirements(m_shoot);
  }

  @Override
  public void initialize() {
    m_timer.reset();
    m_timer.start(); 
  }

  @Override
  public void execute() {
    double hoodPos;
    double speed;
    double hoodAdjust = m_shoot.getHoodAdjust();
    double speedAdjust = m_shoot.getShootAdjust();
  
    switch (m_shoot.getDistanceChoice()) {
      case 0:
        // aganist the HUB
        hoodPos = 0.25;
        speed = 50;
        break;
      case 5:
        // from 6 feet out
        hoodPos = 1.1;
        speed = 45;
        break;
      case 6:
        // from 6 feet out
        hoodPos = 1.1;
        speed = 50;
        break;
      case 7:
        // against the side field wall and 7ft
        hoodPos = 1.4;
        speed = 50;
        break;
      case 10:
        // against the side field wall and ~10ft
        hoodPos = 1.4;
        speed = 55.5;
        break;
      case 15:
        // passing between zones
        hoodPos = 3.0;
        speed = 75;
        break;
      default:
        hoodPos = 0;
        speed = 0;
        break;
    }

    m_shoot.hood(hoodPos * hoodAdjust);
    m_shoot.shoot(speed * speedAdjust);

    if (m_timer.hasElapsed(Shooter.shootWindUp)){
      m_shoot.stage(-1);
      m_index.indexToStage(true);
      // temporarily removed due to contact with hot dog rollers
      //m_intake.intake(.2);
    }
    else {
      m_shoot.stage(.4);
      m_index.indexFromStage(true);
    }
  }

  @Override
  public void end(boolean interrupted) {
    m_shoot.shoot(0);
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
