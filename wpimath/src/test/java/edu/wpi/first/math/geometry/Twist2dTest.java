// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package edu.wpi.first.math.geometry;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class Twist2dTest {
  private static final double kEpsilon = 1E-9;

  @Test
  void testStraightLineTwist() {
    var straight = new Twist2d(5.0, 0.0, 0.0);
    var straightPose = new Pose2d().exp(straight);

    assertAll(
        () -> assertEquals(straightPose.getX(), 5.0, kEpsilon),
        () -> assertEquals(straightPose.getY(), 0.0, kEpsilon),
        () -> assertEquals(straightPose.getRotation().getRadians(), 0.0, kEpsilon));
  }

  @Test
  void testQuarterCirleTwist() {
    var quarterCircle = new Twist2d(5.0 / 2.0 * Math.PI, 0, Math.PI / 2.0);
    var quarterCirclePose = new Pose2d().exp(quarterCircle);

    assertAll(
        () -> assertEquals(quarterCirclePose.getX(), 5.0, kEpsilon),
        () -> assertEquals(quarterCirclePose.getY(), 5.0, kEpsilon),
        () -> assertEquals(quarterCirclePose.getRotation().getDegrees(), 90.0, kEpsilon));
  }

  @Test
  void testDiagonalNoDtheta() {
    var diagonal = new Twist2d(2.0, 2.0, 0.0);
    var diagonalPose = new Pose2d().exp(diagonal);

    assertAll(
        () -> assertEquals(diagonalPose.getX(), 2.0, kEpsilon),
        () -> assertEquals(diagonalPose.getY(), 2.0, kEpsilon),
        () -> assertEquals(diagonalPose.getRotation().getDegrees(), 0.0, kEpsilon));
  }

  @Test
  void testEquality() {
    var one = new Twist2d(5, 1, 3);
    var two = new Twist2d(5, 1, 3);
    assertEquals(one, two);
  }

  @Test
  void testInequality() {
    var one = new Twist2d(5, 1, 3);
    var two = new Twist2d(5, 1.2, 3);
    assertNotEquals(one, two);
  }

  @Test
  void testPose2dLog() {
    final var start = new Pose2d();
    final var end = new Pose2d(5.0, 5.0, Rotation2d.fromDegrees(90.0));

    final var twist = start.log(end);

    assertAll(
        () -> assertEquals(twist.dx, 5.0 / 2.0 * Math.PI, kEpsilon),
        () -> assertEquals(twist.dy, 0.0, kEpsilon),
        () -> assertEquals(twist.dtheta, Math.PI / 2.0, kEpsilon));
  }
}
