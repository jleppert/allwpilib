// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

#pragma once

#include <wpi/SmallVector.h>
#include <wpi/span.h>

#include "frc2/command/CommandBase.h"
#include "frc2/command/CommandHelper.h"
#include "frc2/command/SetUtilities.h"

namespace frc2 {
/**
 * Schedules the given commands when this command is initialized, and ends when
 * all the commands are no longer scheduled.  Useful for forking off from
 * CommandGroups.  If this command is interrupted, it will cancel all of the
 * commands.
 *
 * This class is provided by the NewCommands VendorDep
 */
class ProxyScheduleCommand
    : public CommandHelper<CommandBase, ProxyScheduleCommand> {
 public:
  /**
   * Creates a new ProxyScheduleCommand that schedules the given commands when
   * initialized, and ends when they are all no longer scheduled.
   *
   * @param toSchedule the commands to schedule
   */
  explicit ProxyScheduleCommand(wpi::span<Command* const> toSchedule);

  explicit ProxyScheduleCommand(Command* toSchedule);

  ProxyScheduleCommand(ProxyScheduleCommand&& other) = default;

  ProxyScheduleCommand(const ProxyScheduleCommand& other) = default;

  void Initialize() override;

  void End(bool interrupted) override;

  void Execute() override;

  bool IsFinished() override;

 private:
  wpi::SmallVector<Command*, 4> m_toSchedule;
  bool m_finished{false};
};
}  // namespace frc2
