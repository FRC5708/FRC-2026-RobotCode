// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.Operator;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShootSubsystem;

import java.io.IOException;
import java.text.ParseException;

import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.indexShoot;
import frc.robot.commands.creepMode;
import frc.robot.commands.Deploy;
import frc.robot.commands.DeployToggle;
import frc.robot.commands.intake;
import frc.robot.commands.shoot;
import frc.robot.commands.testDeploy;
import frc.robot.commands.hoodDown;
import frc.robot.commands.hoodUp;
import frc.robot.commands.testShoot;
import frc.robot.commands.indexer;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  CommandXboxController m_driverController = new CommandXboxController(Operator.kDriverControllerPort);
  DriveSubsystem m_drive;
  IntakeSubsystem m_intake;
  ShootSubsystem m_shoot;

  
  // Replace with CommandPS4Controller or CommandJoystick if needed

  /** The container for the robot. Contains subsystems, OI devices, and commands. 
     * @throws org.json.simple.parser.ParseException */
    public RobotContainer() throws IOException, org.json.simple.parser.ParseException{
    m_intake = new IntakeSubsystem();
    m_drive = new DriveSubsystem();
    m_shoot = new ShootSubsystem();
    // Configure the trigger bindings
    //NamedCommands.registerCommand("Creep Mode", new creepMode(m_drive));
  // The robot's subsystems and commands are defined here...
    m_drive.setDefaultCommand(
          m_drive.driveCommand(m_driverController::getLeftX, m_driverController::getLeftY,
              m_driverController::getRightX));
    m_intake.setDefaultCommand(new Deploy(m_intake,0.2));
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
    m_driverController.a().onTrue(new DeployToggle(m_intake));
    m_driverController.b().whileTrue(new intake(m_intake,-0.1));
    m_driverController.rightTrigger().whileTrue(new testDeploy(m_intake, -.3));
    m_driverController.leftTrigger().whileTrue(new testDeploy(m_intake, .3));
    //m_driverController.rightTrigger().whileTrue(new shoot(m_shoot,.3));
    //m_driverController.leftTrigger().whileTrue(new shoot(m_shoot,1.));
    m_driverController.rightBumper().whileTrue(new hoodDown(m_shoot,.3));
    m_driverController.leftBumper().whileTrue(new hoodUp(m_shoot,.3));

    m_driverController.x().whileTrue(new testShoot(m_shoot));
    
    m_driverController.leftStick().toggleOnTrue(new creepMode(m_drive));
    m_driverController.rightStick().toggleOnTrue(new creepMode(m_drive));
    m_driverController.y().whileTrue(new indexer(m_shoot, 0.5));
    
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
