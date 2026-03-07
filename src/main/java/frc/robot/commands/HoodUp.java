// Copyright 2026 Team 5708
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;
import frc.robot.subsystems.ShootSubsystem;
import edu.wpi.first.wpilibj2.command.Command;

public class HoodUp extends Command {
  private final ShootSubsystem m_shoot; 
  private double m_power;

  public HoodUp(ShootSubsystem hood, double power) {
    m_shoot = hood;
    m_power = power;
    addRequirements(m_shoot);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    m_shoot.hoodUp(m_power);
  }

  @Override
  public void end(boolean interrupted) {
    m_shoot.hoodUp(0);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
