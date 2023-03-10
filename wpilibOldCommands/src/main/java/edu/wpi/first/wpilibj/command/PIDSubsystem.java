// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package edu.wpi.first.wpilibj.command;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

/**
 * This class is designed to handle the case where there is a {@link Subsystem} which uses a single
 * {@link PIDController} almost constantly (for instance, an elevator which attempts to stay at a
 * constant height).
 *
 * <p>It provides some convenience methods to run an internal {@link PIDController} . It also allows
 * access to the internal {@link PIDController} in order to give total control to the programmer.
 *
 * <p>This class is provided by the OldCommands VendorDep
 */
public abstract class PIDSubsystem extends Subsystem {
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
   * Instantiates a {@link PIDSubsystem} that will use the given p, i and d values.
   *
   * @param name the name
   * @param p the proportional value
   * @param i the integral value
   * @param d the derivative value
   */
  public PIDSubsystem(String name, double p, double i, double d) {
    super(name);
    m_controller = new PIDController(p, i, d, m_source, m_output);
    addChild("PIDController", m_controller);
  }

  /**
   * Instantiates a {@link PIDSubsystem} that will use the given p, i, d, and f values.
   *
   * @param name the name
   * @param p the proportional value
   * @param i the integral value
   * @param d the derivative value
   * @param f the feed forward value
   */
  public PIDSubsystem(String name, double p, double i, double d, double f) {
    super(name);
    m_controller = new PIDController(p, i, d, f, m_source, m_output);
    addChild("PIDController", m_controller);
  }

  /**
   * Instantiates a {@link PIDSubsystem} that will use the given p, i, d, and f values. It will also
   * space the time between PID loop calculations to be equal to the given period.
   *
   * @param name the name
   * @param p the proportional value
   * @param i the integral value
   * @param d the derivative value
   * @param f the feed forward value
   * @param period the time (in seconds) between calculations
   */
  public PIDSubsystem(String name, double p, double i, double d, double f, double period) {
    super(name);
    m_controller = new PIDController(p, i, d, f, m_source, m_output, period);
    addChild("PIDController", m_controller);
  }

  /**
   * Instantiates a {@link PIDSubsystem} that will use the given p, i and d values. It will use the
   * class name as its name.
   *
   * @param p the proportional value
   * @param i the integral value
   * @param d the derivative value
   */
  public PIDSubsystem(double p, double i, double d) {
    m_controller = new PIDController(p, i, d, m_source, m_output);
    addChild("PIDController", m_controller);
  }

  /**
   * Instantiates a {@link PIDSubsystem} that will use the given p, i, d, and f values. It will use
   * the class name as its name. It will also space the time between PID loop calculations to be
   * equal to the given period.
   *
   * @param p the proportional value
   * @param i the integral value
   * @param d the derivative value
   * @param f the feed forward coefficient
   * @param period the time (in seconds) between calculations
   */
  public PIDSubsystem(double p, double i, double d, double f, double period) {
    m_controller = new PIDController(p, i, d, f, m_source, m_output, period);
    addChild("PIDController", m_controller);
  }

  /**
   * Instantiates a {@link PIDSubsystem} that will use the given p, i and d values. It will use the
   * class name as its name. It will also space the time between PID loop calculations to be equal
   * to the given period.
   *
   * @param p the proportional value
   * @param i the integral value
   * @param d the derivative value
   * @param period the time (in seconds) between calculations
   */
  public PIDSubsystem(double p, double i, double d, double period) {
    m_controller = new PIDController(p, i, d, m_source, m_output, period);
    addChild("PIDController", m_controller);
  }

  /**
   * Returns the {@link PIDController} used by this {@link PIDSubsystem}. Use this if you would like
   * to fine tune the pid loop.
   *
   * @return the {@link PIDController} used by this {@link PIDSubsystem}
   */
  public PIDController getPIDController() {
    return m_controller;
  }

  /**
   * Adds the given value to the setpoint. If {@link PIDSubsystem#setInputRange(double, double)
   * setInputRange(...)} was used, then the bounds will still be honored by this method.
   *
   * @param deltaSetpoint the change in the setpoint
   */
  public void setSetpointRelative(double deltaSetpoint) {
    setSetpoint(getPosition() + deltaSetpoint);
  }

  /**
   * Sets the setpoint to the given value. If {@link PIDSubsystem#setInputRange(double, double)
   * setInputRange(...)} was called, then the given setpoint will be trimmed to fit within the
   * range.
   *
   * @param setpoint the new setpoint
   */
  public void setSetpoint(double setpoint) {
    m_controller.setSetpoint(setpoint);
  }

  /**
   * Returns the setpoint.
   *
   * @return the setpoint
   */
  public double getSetpoint() {
    return m_controller.getSetpoint();
  }

  /**
   * Returns the current position.
   *
   * @return the current position
   */
  public double getPosition() {
    return returnPIDInput();
  }

  /**
   * Sets the maximum and minimum values expected from the input.
   *
   * @param minimumInput the minimum value expected from the input
   * @param maximumInput the maximum value expected from the output
   */
  public void setInputRange(double minimumInput, double maximumInput) {
    m_controller.setInputRange(minimumInput, maximumInput);
  }

  /**
   * Sets the maximum and minimum values to write.
   *
   * @param minimumOutput the minimum value to write to the output
   * @param maximumOutput the maximum value to write to the output
   */
  public void setOutputRange(double minimumOutput, double maximumOutput) {
    m_controller.setOutputRange(minimumOutput, maximumOutput);
  }

  /**
   * Set the absolute error which is considered tolerable for use with OnTarget. The value is in the
   * same range as the PIDInput values.
   *
   * @param t the absolute tolerance
   */
  public void setAbsoluteTolerance(double t) {
    m_controller.setAbsoluteTolerance(t);
  }

  /**
   * Set the percentage error which is considered tolerable for use with OnTarget. (Value of 15.0 ==
   * 15 percent).
   *
   * @param p the percent tolerance
   */
  public void setPercentTolerance(double p) {
    m_controller.setPercentTolerance(p);
  }

  /**
   * Return true if the error is within the percentage of the total input range, determined by
   * setTolerance. This assumes that the maximum and minimum input were set using setInput.
   *
   * @return true if the error is less than the tolerance
   */
  public boolean onTarget() {
    return m_controller.onTarget();
  }

  /**
   * Returns the input for the pid loop.
   *
   * <p>It returns the input for the pid loop, so if this Subsystem was based off of a gyro, then it
   * should return the angle of the gyro.
   *
   * <p>All subclasses of {@link PIDSubsystem} must override this method.
   *
   * @return the value the pid loop should use as input
   */
  protected abstract double returnPIDInput();

  /**
   * Uses the value that the pid loop calculated. The calculated value is the "output" parameter.
   * This method is a good time to set motor values, maybe something along the lines of <code>
   * driveline.tankDrive(output, -output)</code>.
   *
   * <p>All subclasses of {@link PIDSubsystem} must override this method.
   *
   * @param output the value the pid loop calculated
   */
  protected abstract void usePIDOutput(double output);

  /** Enables the internal {@link PIDController}. */
  public void enable() {
    m_controller.enable();
  }

  /** Disables the internal {@link PIDController}. */
  public void disable() {
    m_controller.disable();
  }
}
