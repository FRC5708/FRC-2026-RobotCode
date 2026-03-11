// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
package frc.robot.commands.autonomous;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants.Shooter;
import frc.robot.subsystems.IndexSubsystem;
import frc.robot.subsystems.ShootSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
// Doesn't stop shooter becasuse we dont need it to. Insted it has power set to shoot when against the hub

public class ShooterStop extends Command {
  private final ShootSubsystem m_shoot; 
  private final IndexSubsystem m_index;
  private final IntakeSubsystem m_intake;
  private final Timer m_timer = new Timer();

  public ShooterStop(ShootSubsystem shoot, IndexSubsystem index, IntakeSubsystem intake) {
    m_shoot = shoot;
    m_index = index;
    m_intake = intake;
    addRequirements(m_shoot,m_index,m_intake);
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
    double hoodPos;
    double speed;
    double hoodAdjust = m_shoot.getHoodAdjust();
    double speedAdjust = m_shoot.getShootAdjust();
  
    hoodPos = 0.25;
    speed = 50;

    m_shoot.hood(hoodPos * hoodAdjust);
    m_shoot.shoot(speed * speedAdjust);

    if (m_timer.hasElapsed(Shooter.shootWindUp)){
      m_shoot.stage(-1);
      m_index.indexToStage(true);
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
    return (m_timer.hasElapsed(5.0));
  }
}
