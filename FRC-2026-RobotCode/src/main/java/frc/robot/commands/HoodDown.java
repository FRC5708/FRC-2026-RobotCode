// Copyright 2026 Team 5708
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;
import frc.robot.subsystems.ShootSubsystem;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;

public class HoodDown extends Command {
  private final ShootSubsystem m_shooterHood; 
  private double m_power;
  private final Timer m_timer = new Timer();

  public HoodDown(ShootSubsystem hood, double power) {
    m_shooterHood = hood;
    m_power = power;
  }

  @Override
  public void initialize() {
    m_timer.reset();
    m_timer.start(); 
  }

  @Override
  public void execute() {
    m_shooterHood.hoodDown(m_power);
  }

  @Override
  public void end(boolean interrupted) {
    if (m_timer.hasElapsed(2)){
      // When the hood is driven down for 2 or more seconds, it must
      // be at the lowest position so the hood encoder can be reset to
      // zero. It would be better to detect the motor stall, but this
      // code is simpler.
      m_shooterHood.resetHoodEncoder();
    }
    m_shooterHood.hoodDown(0);
    m_timer.stop();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
