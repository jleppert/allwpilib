// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

#pragma once

#include <string_view>

#include "frc/commands/Command.h"

namespace frc {

/**
 * A ConditionalCommand is a Command that starts one of two commands.
 *
 * A ConditionalCommand uses the Condition method to determine whether it should
 * run onTrue or onFalse.
 *
 * A ConditionalCommand adds the proper Command to the Scheduler during
 * Initialize() and then IsFinished() will return true once that Command has
 * finished executing.
 *
 * If no Command is specified for onFalse, the occurrence of that condition
 * will be a no-op.
 *
 * A ConditionalCommand will require the superset of subsystems of the onTrue
 * and onFalse commands.
 *
 * This class is provided by the OldCommands VendorDep
 *
 * @see Command
 * @see Scheduler
 */
class ConditionalCommand : public Command {
 public:
  /**
   * Creates a new ConditionalCommand with given onTrue and onFalse Commands.
   *
   * @param onTrue  The Command to execute if Condition() returns true
   * @param onFalse The Command to execute if Condition() returns false
   */
  explicit ConditionalCommand(Command* onTrue, Command* onFalse = nullptr);

  /**
   * Creates a new ConditionalCommand with given onTrue and onFalse Commands.
   *
   * @param name    The name for this command group
   * @param onTrue  The Command to execute if Condition() returns true
   * @param onFalse The Command to execute if Condition() returns false
   */
  ConditionalCommand(std::string_view name, Command* onTrue,
                     Command* onFalse = nullptr);

  ~ConditionalCommand() override = default;

  ConditionalCommand(ConditionalCommand&&) = default;
  ConditionalCommand& operator=(ConditionalCommand&&) = default;

 protected:
  /**
   * The Condition to test to determine which Command to run.
   *
   * @return true if m_onTrue should be run or false if m_onFalse should be run.
   */
  virtual bool Condition() = 0;

  void _Initialize() override;
  void _Cancel() override;
  bool IsFinished() override;
  void _Interrupted() override;

 private:
  // The Command to execute if Condition() returns true
  Command* m_onTrue;

  // The Command to execute if Condition() returns false
  Command* m_onFalse;

  // Stores command chosen by condition
  Command* m_chosenCommand = nullptr;
};

}  // namespace frc
