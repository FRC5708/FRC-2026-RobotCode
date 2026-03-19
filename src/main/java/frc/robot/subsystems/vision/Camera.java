package frc.robot.subsystems.vision;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.wpi.first.epilogue.Logged;
import frc.robot.subsystems.vision.io.*;

@Logged
public class Camera {
    private final CameraIO io;
    private List<CameraIO.AprilTagPoseObservation> poseObservationCache = new ArrayList<>();

    public Camera(CameraIO io) {
        this.io = io;
    }

    public void periodic() {
        poseObservationCache = io.getAllUnreadPoseObservations();
    }

    public List<CameraIO.AprilTagPoseObservation> getPoseObservations() {
        return Collections.unmodifiableList(poseObservationCache);
    }
}
