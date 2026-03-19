package frc.robot.subsystems.vision;

import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Pose3d;
import frc.robot.subsystems.vision.VisionConstants.PhotonCamConfig;
import frc.robot.subsystems.vision.io.CameraIOPhoton;

public class PhotonCameraFactory {
    private final Supplier<Pose3d> robotPoseSupplier;
    public PhotonCameraFactory(Supplier<Pose3d> robotPoseSupplier) {
        this.robotPoseSupplier = robotPoseSupplier;
    }

    public Camera buildCameraFromConfig(PhotonCamConfig config) {
        return new Camera(new CameraIOPhoton(config,robotPoseSupplier));
    }
}
