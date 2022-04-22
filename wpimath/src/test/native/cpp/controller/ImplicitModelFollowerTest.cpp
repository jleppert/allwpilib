// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

#include <gtest/gtest.h>

#include "frc/controller/ImplicitModelFollower.h"
#include "frc/system/plant/LinearSystemId.h"

namespace frc {

TEST(ImplicitModelFollowerTest, SameModel) {
  constexpr auto dt = 5_ms;

  using Kv_t = decltype(1_V / 1_mps);
  using Ka_t = decltype(1_V / 1_mps_sq);
  auto plant = LinearSystemId::IdentifyDrivetrainSystem(Kv_t{1.0}, Ka_t{1.0},
                                                        Kv_t{1.0}, Ka_t{1.0});

  ImplicitModelFollower<2, 2> imf{plant, plant};

  Eigen::Vector<double, 2> x{0.0, 0.0};
  Eigen::Vector<double, 2> xImf{0.0, 0.0};

  // Forward
  Eigen::Vector<double, 2> u{12.0, 12.0};
  for (auto t = 0_s; t < 3_s; t += dt) {
    x = plant.CalculateX(x, u, dt);
    xImf = plant.CalculateX(xImf, imf.Calculate(xImf, u), dt);

    EXPECT_DOUBLE_EQ(x(0), xImf(0));
    EXPECT_DOUBLE_EQ(x(1), xImf(1));
  }

  // Backward
  u = Eigen::Vector<double, 2>{-12.0, -12.0};
  for (auto t = 0_s; t < 3_s; t += dt) {
    x = plant.CalculateX(x, u, dt);
    xImf = plant.CalculateX(xImf, imf.Calculate(xImf, u), dt);

    EXPECT_DOUBLE_EQ(x(0), xImf(0));
    EXPECT_DOUBLE_EQ(x(1), xImf(1));
  }

  // Rotate CCW
  u = Eigen::Vector<double, 2>{-12.0, 12.0};
  for (auto t = 0_s; t < 3_s; t += dt) {
    x = plant.CalculateX(x, u, dt);
    xImf = plant.CalculateX(xImf, imf.Calculate(xImf, u), dt);

    EXPECT_DOUBLE_EQ(x(0), xImf(0));
    EXPECT_DOUBLE_EQ(x(1), xImf(1));
  }
}

TEST(ImplicitModelFollowerTest, SlowerRefModel) {
  constexpr auto dt = 5_ms;

  using Kv_t = decltype(1_V / 1_mps);
  using Ka_t = decltype(1_V / 1_mps_sq);

  auto plant = LinearSystemId::IdentifyDrivetrainSystem(Kv_t{1.0}, Ka_t{1.0},
                                                        Kv_t{1.0}, Ka_t{1.0});

  // Linear acceleration is slower, but angular acceleration is the same
  auto plantRef = LinearSystemId::IdentifyDrivetrainSystem(
      Kv_t{1.0}, Ka_t{2.0}, Kv_t{1.0}, Ka_t{1.0});

  ImplicitModelFollower<2, 2> imf{plant, plantRef};

  Eigen::Vector<double, 2> x{0.0, 0.0};
  Eigen::Vector<double, 2> xImf{0.0, 0.0};

  // Forward
  Eigen::Vector<double, 2> u{12.0, 12.0};
  for (auto t = 0_s; t < 3_s; t += dt) {
    x = plant.CalculateX(x, u, dt);
    xImf = plant.CalculateX(xImf, imf.Calculate(xImf, u), dt);

    EXPECT_GE(x(0), xImf(0));
    EXPECT_GE(x(1), xImf(1));
  }

  // Backward
  x.setZero();
  xImf.setZero();
  u = Eigen::Vector<double, 2>{-12.0, -12.0};
  for (auto t = 0_s; t < 3_s; t += dt) {
    x = plant.CalculateX(x, u, dt);
    xImf = plant.CalculateX(xImf, imf.Calculate(xImf, u), dt);

    EXPECT_LE(x(0), xImf(0));
    EXPECT_LE(x(1), xImf(1));
  }

  // Rotate CCW
  x.setZero();
  xImf.setZero();
  u = Eigen::Vector<double, 2>{-12.0, 12.0};
  for (auto t = 0_s; t < 3_s; t += dt) {
    x = plant.CalculateX(x, u, dt);
    xImf = plant.CalculateX(xImf, imf.Calculate(xImf, u), dt);

    EXPECT_NEAR(x(0), xImf(0), 1e-5);
    EXPECT_NEAR(x(1), xImf(1), 1e-5);
  }
}

}  // namespace frc
