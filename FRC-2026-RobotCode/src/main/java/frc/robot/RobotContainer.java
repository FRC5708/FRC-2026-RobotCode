// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.Operator;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShootSubsystem;
import frc.robot.subsystems.IndexSubsystem;

import java.io.IOException;
import org.json.simple.parser.ParseException;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.CreepMode;
import frc.robot.commands.Deploy;
import frc.robot.commands.Intake;
import frc.robot.commands.OnlyIndexFromStage;
import frc.robot.commands.OnlyIndexToStage;
import frc.robot.commands.ReverseIntake;
import frc.robot.commands.Shoot;
import frc.robot.commands.SplitStage;
import frc.robot.commands.HoodUp;
import frc.robot.commands.HoodDown;
import frc.robot.commands.HoodSetpointChange;
import frc.robot.commands.Stage;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
@Logged
public class RobotContainer {
  CommandXboxController m_driverController = new CommandXboxController(Operator.kDriverControllerPort);
  DriveSubsystem m_drive;
  IntakeSubsystem m_intake;
  ShootSubsystem m_shoot;
  IndexSubsystem m_index;

  
  // Replace with CommandPS4Controller or CommandJoystick if needed

  /** The container for the robot. Contains subsystems, OI devices, and commands. 
     * @throws org.json.simple.parser.ParseException */
    public RobotContainer() throws IOException, ParseException{
    m_intake = new IntakeSubsystem();
    m_drive = new DriveSubsystem();
    m_shoot = new ShootSubsystem();
    m_index = new IndexSubsystem();

    // Configure the trigger bindings
    //NamedCommands.registerCommand("Creep Mode", new creepMode(m_drive));
  // The robot's subsystems and commands are defined here...
    m_drive.setDefaultCommand(
           m_drive.driveCommand(m_driverController::getLeftX, m_driverController::getLeftY,
               m_driverController::getRightX));
    // m_intake.setDefaultCommand(new Deploy(m_intake,0.0));
    configureBindings();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    
    //For testing/debugging commands
    //m_driverController.y().whileTrue(new Stage(m_shoot, 1));
    //m_driverController.x().whileTrue(new SplitStage(m_shoot, .5, false));
    //m_driverController.y().whileTrue(new SplitStage(m_shoot, -.5, false));
    //m_driverController.a().whileTrue(new SplitStage(m_shoot, .5, true));
    //m_driverController.b().whileTrue(new SplitStage(m_shoot, -.5, true));
    //m_driverController.rightTrigger().whileTrue(new OnlyIndexToStage(m_index));
    //m_driverController.leftTrigger().whileTrue(new OnlyIndexFromStage(m_index));
    
    //For the comp
    //Intake controls
    m_driverController.leftTrigger().whileTrue(new Intake(m_intake, m_index, 0.55));
    m_driverController.y().whileTrue(new ReverseIntake(m_intake, m_index, 0.1));

    //Deploy the funnel controls
    //m_driverController.a().whileTrue(new Deploy(m_intake, 1.0));
    //m_driverController.x().whileTrue(new Deploy(m_intake, -.85));

    //Shoot controls
    m_driverController.rightTrigger().whileTrue(new Shoot(m_shoot, m_index, m_intake));

    //Hood controls
    m_driverController.rightBumper().whileTrue(new HoodSetpointChange(m_shoot, 1));
    m_driverController.leftBumper().whileTrue(new HoodSetpointChange(m_shoot, -1));
    m_driverController.a().whileTrue(new HoodUp(m_shoot, .1));
    m_driverController.b().whileTrue(new HoodDown(m_shoot, .1));

    
    //Creep mode + driving is with joysticks(look above)
    m_driverController.leftStick().toggleOnTrue(new CreepMode(m_drive));
    m_driverController.rightStick().toggleOnTrue(new CreepMode(m_drive));
    m_driverController.start().onTrue(m_drive.zeroGyro());
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  // public DriveSubsystem getDriveSubsystem() {
  //   return m_drive;
  // }
}
