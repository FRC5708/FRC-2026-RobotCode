// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.Shooter;
import frc.robot.subsystems.IndexSubsystem;
import frc.robot.subsystems.ShootSubsystem;
import frc.robot.subsystems.IntakeSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class Shoot extends Command {
  private final ShootSubsystem m_shoot; 
  private final IndexSubsystem m_index;
  private final IntakeSubsystem m_intake;
  private final Timer m_timer = new Timer();

  /** Creates a new Intake. */
  public Shoot(ShootSubsystem shoot, IndexSubsystem index, IntakeSubsystem intake) {
    // Use addRequirements
    m_shoot = shoot;
    m_index = index;
    m_intake = intake;
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
    if (m_timer.hasElapsed(Shooter.shootWindUp)){
      m_shoot.shoot(true);
      m_shoot.stage(1);
      m_index.indexToStage(true);
      m_intake.intake(.2);
    }
    else {
      m_shoot.shoot(true);
      m_shoot.stage(-.4);
      m_index.indexFromStage(true);
    }
  }

  // Called once the command ends or is intrupted.
  @Override
  public void end(boolean interrupted) {
    m_shoot.shoot(false);
    m_shoot.stage(0);
    m_index.indexToStage(false);
    m_intake.intake(0);
    m_timer.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
