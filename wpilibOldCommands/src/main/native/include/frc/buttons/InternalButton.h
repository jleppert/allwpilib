// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

#pragma once

#include "frc/buttons/Button.h"

namespace frc {

/**
 * This Button is intended to be used within a program. The programmer can
 * manually set its value. Also includes a setting for whether or not it should
 * invert its value.
 *
 * This class is provided by the OldCommands VendorDep
 */
class InternalButton : public Button {
 public:
  InternalButton() = default;
  explicit InternalButton(bool inverted);
  ~InternalButton() override = default;

  InternalButton(InternalButton&&) = default;
  InternalButton& operator=(InternalButton&&) = default;

  void SetInverted(bool inverted);
  void SetPressed(bool pressed);

  bool Get() override;

 private:
  bool m_pressed = false;
  bool m_inverted = false;
};

}  // namespace frc
