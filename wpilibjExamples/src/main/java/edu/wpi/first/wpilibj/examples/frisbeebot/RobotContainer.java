// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package edu.wpi.first.wpilibj.examples.frisbeebot;

import static edu.wpi.first.wpilibj.XboxController.Button;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.examples.frisbeebot.Constants.AutoConstants;
import edu.wpi.first.wpilibj.examples.frisbeebot.Constants.OIConstants;
import edu.wpi.first.wpilibj.examples.frisbeebot.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj.examples.frisbeebot.subsystems.ShooterSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems
  private final DriveSubsystem m_robotDrive = new DriveSubsystem();
  private final ShooterSubsystem m_shooter = new ShooterSubsystem();

  // A simple autonomous routine that shoots the loaded frisbees
  private final Command m_autoCommand =
      // Start the command by spinning up the shooter...
      new InstantCommand(m_shooter::enable, m_shooter)
          .andThen(
              // Wait until the shooter is at speed before feeding the frisbees
              new WaitUntilCommand(m_shooter::atSetpoint),
              // Start running the feeder
              new InstantCommand(m_shooter::runFeeder, m_shooter),
              // Shoot for the specified time
              new WaitCommand(AutoConstants.kAutoShootTimeSeconds))
          // Add a timeout (will end the command if, for instance, the shooter never gets up to
          // speed)
          .withTimeout(AutoConstants.kAutoTimeoutSeconds)
          // When the command ends, turn off the shooter and the feeder
          .andThen(
              () -> {
                m_shooter.disable();
                m_shooter.stopFeeder();
              });

  // The driver's controller
  XboxController m_driverController = new XboxController(OIConstants.kDriverControllerPort);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();

    // Configure default commands
    // Set the default drive command to split-stick arcade drive
    m_robotDrive.setDefaultCommand(
        // A split-stick arcade command, with forward/backward controlled by the left
        // hand, and turning controlled by the right.
        new RunCommand(
            () ->
                m_robotDrive.arcadeDrive(
                    -m_driverController.getLeftY(), m_driverController.getRightX()),
            m_robotDrive));
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link edu.wpi.first.wpilibj.GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    // Spin up the shooter when the 'A' button is pressed
    new JoystickButton(m_driverController, Button.kA.value)
        .whenPressed(new InstantCommand(m_shooter::enable, m_shooter));

    // Turn off the shooter when the 'B' button is pressed
    new JoystickButton(m_driverController, Button.kB.value)
        .whenPressed(new InstantCommand(m_shooter::disable, m_shooter));

    // Run the feeder when the 'X' button is held, but only if the shooter is at speed
    new JoystickButton(m_driverController, Button.kX.value)
        .whenPressed(
            new ConditionalCommand(
                // Run the feeder
                new InstantCommand(m_shooter::runFeeder, m_shooter),
                // Do nothing
                new InstantCommand(),
                // Determine which of the above to do based on whether the shooter has reached the
                // desired speed
                m_shooter::atSetpoint))
        .whenReleased(new InstantCommand(m_shooter::stopFeeder, m_shooter));

    // Drive at half speed when the bumper is held
    new JoystickButton(m_driverController, Button.kRightBumper.value)
        .whenPressed(() -> m_robotDrive.setMaxOutput(0.5))
        .whenReleased(() -> m_robotDrive.setMaxOutput(1));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return m_autoCommand;
  }
}
