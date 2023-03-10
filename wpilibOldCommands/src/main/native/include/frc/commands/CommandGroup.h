// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

#pragma once

#include <string_view>
#include <vector>

#include <units/time.h>

#include "frc/commands/Command.h"
#include "frc/commands/CommandGroupEntry.h"

namespace frc {

/**
 * A CommandGroup is a list of commands which are executed in sequence.
 *
 * Commands in a CommandGroup are added using the AddSequential() method and are
 * called sequentially. CommandGroups are themselves Commands and can be given
 * to other CommandGroups.
 *
 * CommandGroups will carry all of the requirements of their Command
 * subcommands. Additional requirements can be specified by calling Requires()
 * normally in the constructor.
 *
 * CommandGroups can also execute commands in parallel, simply by adding them
 * using AddParallel().
 *
 * This class is provided by the OldCommands VendorDep
 *
 * @see Command
 * @see Subsystem
 */
class CommandGroup : public Command {
 public:
  CommandGroup() = default;

  /**
   * Creates a new CommandGroup with the given name.
   *
   * @param name The name for this command group
   */
  explicit CommandGroup(std::string_view name);

  ~CommandGroup() override = default;

  CommandGroup(CommandGroup&&) = default;
  CommandGroup& operator=(CommandGroup&&) = default;

  /**
   * Adds a new Command to the group. The Command will be started after all the
   * previously added Commands.
   *
   * Note that any requirements the given Command has will be added to the
   * group. For this reason, a Command's requirements can not be changed after
   * being added to a group.
   *
   * It is recommended that this method be called in the constructor.
   *
   * @param command The Command to be added
   */
  void AddSequential(Command* command);

  /**
   * Adds a new Command to the group with a given timeout. The Command will be
   * started after all the previously added commands.
   *
   * Once the Command is started, it will be run until it finishes or the time
   * expires, whichever is sooner.  Note that the given Command will have no
   * knowledge that it is on a timer.
   *
   * Note that any requirements the given Command has will be added to the
   * group. For this reason, a Command's requirements can not be changed after
   * being added to a group.
   *
   * It is recommended that this method be called in the constructor.
   *
   * @param command The Command to be added
   * @param timeout The timeout
   */
  void AddSequential(Command* command, units::second_t timeout);

  /**
   * Adds a new child Command to the group. The Command will be started after
   * all the previously added Commands.
   *
   * Instead of waiting for the child to finish, a CommandGroup will have it run
   * at the same time as the subsequent Commands. The child will run until
   * either it finishes, a new child with conflicting requirements is started,
   * or the main sequence runs a Command with conflicting requirements. In the
   * latter two cases, the child will be canceled even if it says it can't be
   * interrupted.
   *
   * Note that any requirements the given Command has will be added to the
   * group. For this reason, a Command's requirements can not be changed after
   * being added to a group.
   *
   * It is recommended that this method be called in the constructor.
   *
   * @param command The command to be added
   */
  void AddParallel(Command* command);

  /**
   * Adds a new child Command to the group with the given timeout. The Command
   * will be started after all the previously added Commands.
   *
   * Once the Command is started, it will run until it finishes, is interrupted,
   * or the time expires, whichever is sooner. Note that the given Command will
   * have no knowledge that it is on a timer.
   *
   * Instead of waiting for the child to finish, a CommandGroup will have it run
   * at the same time as the subsequent Commands. The child will run until
   * either it finishes, the timeout expires, a new child with conflicting
   * requirements is started, or the main sequence runs a Command with
   * conflicting requirements. In the latter two cases, the child will be
   * canceled even if it says it can't be interrupted.
   *
   * Note that any requirements the given Command has will be added to the
   * group. For this reason, a Command's requirements can not be changed after
   * being added to a group.
   *
   * It is recommended that this method be called in the constructor.
   *
   * @param command The command to be added
   * @param timeout The timeout
   */
  void AddParallel(Command* command, units::second_t timeout);

  bool IsInterruptible() const;

  int GetSize() const;

 protected:
  /**
   * Can be overridden by teams.
   */
  void Initialize() override;

  /**
   * Can be overridden by teams.
   */
  void Execute() override;

  /**
   * Can be overridden by teams.
   */
  bool IsFinished() override;

  /**
   * Can be overridden by teams.
   */
  void End() override;

  /**
   * Can be overridden by teams.
   */
  void Interrupted() override;

  void _Initialize() override;
  void _Execute() override;
  void _End() override;
  void _Interrupted() override;

 private:
  void CancelConflicts(Command* command);

  // The commands in this group (stored in entries)
  std::vector<CommandGroupEntry> m_commands;

  // The active children in this group (stored in entries)
  std::vector<CommandGroupEntry*> m_children;

  // The current command, -1 signifies that none have been run
  int m_currentCommandIndex = -1;
};

}  // namespace frc
