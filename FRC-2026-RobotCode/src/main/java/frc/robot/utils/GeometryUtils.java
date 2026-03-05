// Copyright (c) FRC 1076 PiHi Samurai
// You may use, distribute, and modify this software under the terms of
// the license found in the root directory of this project

package frc.robot.utils;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;

public final class GeometryUtils {
    private GeometryUtils() {}

    /** rotates a Pose2d */
    public static Pose2d rotatePose(Pose2d pose, Rotation2d rot) {
        return new Pose2d(pose.getTranslation(), pose.getRotation().rotateBy(rot));
    }

    /* finds angle from one pose to another pose */
    public static Rotation2d angleToPose(Pose2d startPose, Pose2d endPose){
        Translation2d targetVector = endPose.getTranslation().minus(startPose.getTranslation());
        return targetVector.getAngle().minus(startPose.getRotation());
    }
}