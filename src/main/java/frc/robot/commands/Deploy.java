// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.Constants.Intake;
import edu.wpi.first.wpilibj.Timer;

/* You should consider usingthe more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class Deploy extends Command {
  /** Creates a new DeployToggle. */
  private final IntakeSubsystem m_intake;
  private final double m_power;
  private boolean velocityTripped;
  private final Timer m_totalTime = new Timer();
  private final Timer m_stallTimer = new Timer();
  public Deploy(IntakeSubsystem  intake, double power) {
    m_power = power;
    m_intake = intake;
    addRequirements(m_intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_totalTime.reset();
    m_stallTimer.reset();
    m_totalTime.start();
    m_stallTimer.start();
    velocityTripped = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_intake.deploy(m_power);

    if (m_totalTime.hasElapsed(Intake.wayTooFuckingLong)){
      velocityTripped = true;
    }
    else if (m_intake.getDeployVelocity() >= Intake.velocityThreshold){
      m_stallTimer.reset();
    }
    else if (m_stallTimer.hasElapsed(Intake.maxStallTime)){
      velocityTripped = true;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_totalTime.stop();
    m_stallTimer.stop();
    m_intake.deploy(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return velocityTripped;
  }
}


// public Command runDeploy(double power) {
//         return startRun(
//             () -> {
//                 homingDebouncer = new Debouncer(homingDebounceTime);
//                 homingDebouncer.calculate(false);
//                 deploy(power);
//             },
//             () -> {
//                 homed = homingDebouncer.calculate(Math.abs(m_deploy_encoder.getVelocity()) <= homingVelocityThreshold);
//             }
//         )
//         .until(() -> homed)
//         .andThen(() -> deploy(0));
//     }