// Copyright 2026 Team 5708
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;
import frc.robot.subsystems.ShootSubsystem;
import edu.wpi.first.wpilibj2.command.Command;

public class HoodUp extends Command {
  private final ShootSubsystem m_shooterHood; 
  private double m_power;

  public HoodUp(ShootSubsystem hood, double power) {
    m_shooterHood = hood;
    m_power = power;
    addRequirements(m_shooterHood);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    m_shooterHood.hoodUp(m_power);
  }

  @Override
  public void end(boolean interrupted) {
    m_shooterHood.hoodUp(0);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
