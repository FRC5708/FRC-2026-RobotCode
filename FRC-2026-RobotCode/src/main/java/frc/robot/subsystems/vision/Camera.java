package frc.robot.subsystems.vision;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import frc.robot.subsystems.vision.io.*;

public class Camera {
    private final CameraIO io;
    private final CameraIO.CameraIOInputs inputs = new CameraIO.CameraIOInputs();
    private List<CameraIO.AprilTagPoseObservation> poseObservationCache = new ArrayList<>();

    public Camera(CameraIO io) {
        this.io = io;
    }

    public void periodic() {
        io.updateInputs(inputs);
    }

    CameraIO.CameraIOInputs getInputs() {
        return inputs;
    }

    public List<CameraIO.AprilTagPoseObservation> getPoseObservations() {
        return Collections.unmodifiableList(poseObservationCache);
    }
}
