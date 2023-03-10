// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

#pragma once

#include <memory>
#include <string>

#include <wpi/deprecated.h>
#include <wpi/mutex.h>

#include "frc/Controller.h"
#include "frc/Notifier.h"
#include "frc/PIDBase.h"
#include "frc/PIDSource.h"
#include "frc/Timer.h"

namespace frc {

class PIDOutput;

/**
 * Class implements a PID Control Loop.
 *
 * Creates a separate thread which reads the given PIDSource and takes care of
 * the integral calculations, as well as writing the given PIDOutput.
 *
 * This feedback controller runs in discrete time, so time deltas are not used
 * in the integral and derivative calculations. Therefore, the sample rate
 * affects the controller's behavior for a given set of PID constants.
 *
 * This class is provided by the OldCommands VendorDep
 *
 * @deprecated Use frc2::PIDController class instead.
 */
class PIDController : public PIDBase, public Controller {
 public:
  /**
   * Allocate a PID object with the given constants for P, I, D.
   *
   * @param p      the proportional coefficient
   * @param i      the integral coefficient
   * @param d      the derivative coefficient
   * @param source The PIDSource object that is used to get values
   * @param output The PIDOutput object that is set to the output value
   * @param period the loop time for doing calculations in seconds. This
   *               particularly affects calculations of the integral and
   *               differential terms. The default is 0.05 (50ms).
   */
  WPI_DEPRECATED("Use frc2::PIDController class instead.")
  PIDController(double p, double i, double d, PIDSource* source,
                PIDOutput* output, double period = 0.05);

  /**
   * Allocate a PID object with the given constants for P, I, D.
   *
   * @param p      the proportional coefficient
   * @param i      the integral coefficient
   * @param d      the derivative coefficient
   * @param f      the feedforward coefficient
   * @param source The PIDSource object that is used to get values
   * @param output The PIDOutput object that is set to the output value
   * @param period the loop time for doing calculations in seconds. This
   *               particularly affects calculations of the integral and
   *               differential terms. The default is 0.05 (50ms).
   */
  WPI_DEPRECATED("Use frc2::PIDController class instead.")
  PIDController(double p, double i, double d, double f, PIDSource* source,
                PIDOutput* output, double period = 0.05);

  /**
   * Allocate a PID object with the given constants for P, I, D.
   *
   * @param p      the proportional coefficient
   * @param i      the integral coefficient
   * @param d      the derivative coefficient
   * @param source The PIDSource object that is used to get values
   * @param output The PIDOutput object that is set to the output value
   * @param period the loop time for doing calculations in seconds. This
   *               particularly affects calculations of the integral and
   *               differential terms. The default is 0.05 (50ms).
   */
  WPI_DEPRECATED("Use frc2::PIDController class instead.")
  PIDController(double p, double i, double d, PIDSource& source,
                PIDOutput& output, double period = 0.05);

  /**
   * Allocate a PID object with the given constants for P, I, D.
   *
   * @param p      the proportional coefficient
   * @param i      the integral coefficient
   * @param d      the derivative coefficient
   * @param f      the feedforward coefficient
   * @param source The PIDSource object that is used to get values
   * @param output The PIDOutput object that is set to the output value
   * @param period the loop time for doing calculations in seconds. This
   *               particularly affects calculations of the integral and
   *               differential terms. The default is 0.05 (50ms).
   */
  WPI_DEPRECATED("Use frc2::PIDController class instead.")
  PIDController(double p, double i, double d, double f, PIDSource& source,
                PIDOutput& output, double period = 0.05);

  ~PIDController() override;

  /**
   * Begin running the PIDController.
   */
  void Enable() override;

  /**
   * Stop running the PIDController, this sets the output to zero before
   * stopping.
   */
  void Disable() override;

  /**
   * Set the enabled state of the PIDController.
   */
  void SetEnabled(bool enable);

  /**
   * Return true if PIDController is enabled.
   */
  bool IsEnabled() const;

  /**
   * Reset the previous error, the integral term, and disable the controller.
   */
  void Reset() override;

  void InitSendable(wpi::SendableBuilder& builder) override;

 private:
  std::unique_ptr<Notifier> m_controlLoop;
};

}  // namespace frc
