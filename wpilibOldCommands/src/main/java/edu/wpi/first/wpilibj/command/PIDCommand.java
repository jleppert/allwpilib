// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package edu.wpi.first.wpilibj.command;

import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

/**
 * This class defines a {@link Command} which interacts heavily with a PID loop.
 *
 * <p>It provides some convenience methods to run an internal {@link PIDController} . It will also
 * start and stop said {@link PIDController} when the {@link PIDCommand} is first initialized and
 * ended/interrupted.
 *
 * <p>This class is provided by the OldCommands VendorDep
 */
public abstract class PIDCommand extends Command {
  /** The internal {@link PIDController}. */
  private final PIDController m_controller;
  /** An output which calls {@link PIDCommand#usePIDOutput(double)}. */
  private final PIDOutput m_output = this::usePIDOutput;
  /** A source which calls {@link PIDCommand#returnPIDInput()}. */
  private final PIDSource m_source =
      new PIDSource() {
        @Override
        public void setPIDSourceType(PIDSourceType pidSource) {}

        @Override
        public PIDSourceType getPIDSourceType() {
          return PIDSourceType.kDisplacement;
        }

        @Override
        public double pidGet() {
          return returnPIDInput();
        }
      };

  /**
   * Instantiates a {@link PIDCommand} that will use the given p, i and d values.
   *
   * @param name the name of the command
   * @param p the proportional value
   * @param i the integral value
   * @param d the derivative value
   */
  public PIDCommand(String name, double p, double i, double d) {
    super(name);
    m_controller = new PIDController(p, i, d, m_source, m_output);
  }

  /**
   * Instantiates a {@link PIDCommand} that will use the given p, i and d values. It will also space
   * the time between PID loop calculations to be equal to the given period.
   *
   * @param name the name
   * @param p the proportional value
   * @param i the integral value
   * @param d the derivative value
   * @param period the time (in seconds) between calculations
   */
  public PIDCommand(String name, double p, double i, double d, double period) {
    super(name);
    m_controller = new PIDController(p, i, d, m_source, m_output, period);
  }

  /**
   * Instantiates a {@link PIDCommand} that will use the given p, i and d values. It will use the
   * class name as its name.
   *
   * @param p the proportional value
   * @param i the integral value
   * @param d the derivative value
   */
  public PIDCommand(double p, double i, double d) {
    m_controller = new PIDController(p, i, d, m_source, m_output);
  }

  /**
   * Instantiates a {@link PIDCommand} that will use the given p, i and d values. It will use the
   * class name as its name. It will also space the time between PID loop calculations to be equal
   * to the given period.
   *
   * @param p the proportional value
   * @param i the integral value
   * @param d the derivative value
   * @param period the time (in seconds) between calculations
   */
  public PIDCommand(double p, double i, double d, double period) {
    m_controller = new PIDController(p, i, d, m_source, m_output, period);
  }

  /**
   * Instantiates a {@link PIDCommand} that will use the given p, i and d values.
   *
   * @param name the name of the command
   * @param p the proportional value
   * @param i the integral value
   * @param d the derivative value
   * @param subsystem the subsystem that this command requires
   */
  public PIDCommand(String name, double p, double i, double d, Subsystem subsystem) {
    super(name, subsystem);
    m_controller = new PIDController(p, i, d, m_source, m_output);
  }

  /**
   * Instantiates a {@link PIDCommand} that will use the given p, i and d values. It will also space
   * the time between PID loop calculations to be equal to the given period.
   *
   * @param name the name
   * @param p the proportional value
   * @param i the integral value
   * @param d the derivative value
   * @param period the time (in seconds) between calculations
   * @param subsystem the subsystem that this command requires
   */
  public PIDCommand(String name, double p, double i, double d, double period, Subsystem subsystem) {
    super(name, subsystem);
    m_controller = new PIDController(p, i, d, m_source, m_output, period);
  }

  /**
   * Instantiates a {@link PIDCommand} that will use the given p, i and d values. It will use the
   * class name as its name.
   *
   * @param p the proportional value
   * @param i the integral value
   * @param d the derivative value
   * @param subsystem the subsystem that this command requires
   */
  public PIDCommand(double p, double i, double d, Subsystem subsystem) {
    super(subsystem);
    m_controller = new PIDController(p, i, d, m_source, m_output);
  }

  /**
   * Instantiates a {@link PIDCommand} that will use the given p, i and d values. It will use the
   * class name as its name. It will also space the time between PID loop calculations to be equal
   * to the given period.
   *
   * @param p the proportional value
   * @param i the integral value
   * @param d the derivative value
   * @param period the time (in seconds) between calculations
   * @param subsystem the subsystem that this command requires
   */
  public PIDCommand(double p, double i, double d, double period, Subsystem subsystem) {
    super(subsystem);
    m_controller = new PIDController(p, i, d, m_source, m_output, period);
  }

  /**
   * Returns the {@link PIDController} used by this {@link PIDCommand}. Use this if you would like
   * to fine tune the pid loop.
   *
   * @return the {@link PIDController} used by this {@link PIDCommand}
   */
  protected PIDController getPIDController() {
    return m_controller;
  }

  @Override
  @SuppressWarnings("MethodName")
  void _initialize() {
    m_controller.enable();
  }

  @Override
  @SuppressWarnings("MethodName")
  void _end() {
    m_controller.disable();
  }

  @Override
  @SuppressWarnings("MethodName")
  void _interrupted() {
    _end();
  }

  /**
   * Adds the given value to the setpoint. If {@link PIDCommand#setInputRange(double, double)
   * setInputRange(...)} was used, then the bounds will still be honored by this method.
   *
   * @param deltaSetpoint the change in the setpoint
   */
  public void setSetpointRelative(double deltaSetpoint) {
    setSetpoint(getSetpoint() + deltaSetpoint);
  }

  /**
   * Sets the setpoint to the given value. If {@link PIDCommand#setInputRange(double, double)
   * setInputRange(...)} was called, then the given setpoint will be trimmed to fit within the
   * range.
   *
   * @param setpoint the new setpoint
   */
  protected void setSetpoint(double setpoint) {
    m_controller.setSetpoint(setpoint);
  }

  /**
   * Returns the setpoint.
   *
   * @return the setpoint
   */
  protected double getSetpoint() {
    return m_controller.getSetpoint();
  }

  /**
   * Returns the current position.
   *
   * @return the current position
   */
  protected double getPosition() {
    return returnPIDInput();
  }

  /**
   * Sets the maximum and minimum values expected from the input and setpoint.
   *
   * @param minimumInput the minimum value expected from the input and setpoint
   * @param maximumInput the maximum value expected from the input and setpoint
   */
  protected void setInputRange(double minimumInput, double maximumInput) {
    m_controller.setInputRange(minimumInput, maximumInput);
  }

  /**
   * Returns the input for the pid loop.
   *
   * <p>It returns the input for the pid loop, so if this command was based off of a gyro, then it
   * should return the angle of the gyro.
   *
   * <p>All subclasses of {@link PIDCommand} must override this method.
   *
   * <p>This method will be called in a different thread then the {@link Scheduler} thread.
   *
   * @return the value the pid loop should use as input
   */
  protected abstract double returnPIDInput();

  /**
   * Uses the value that the pid loop calculated. The calculated value is the "output" parameter.
   * This method is a good time to set motor values, maybe something along the lines of <code>
   * driveline.tankDrive(output, -output)</code>
   *
   * <p>All subclasses of {@link PIDCommand} must override this method.
   *
   * <p>This method will be called in a different thread then the {@link Scheduler} thread.
   *
   * @param output the value the pid loop calculated
   */
  protected abstract void usePIDOutput(double output);

  @Override
  public void initSendable(SendableBuilder builder) {
    m_controller.initSendable(builder);
    super.initSendable(builder);
    builder.setSmartDashboardType("PIDCommand");
  }
}
