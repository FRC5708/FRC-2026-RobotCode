package frc.robot.subsystems.vision;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.subsystems.vision.VisionConstants.PhotonCamConfig;
import frc.robot.subsystems.vision.io.CameraIO;
import frc.robot.subsystems.vision.io.CameraIOPhoton;
import frc.robot.utils.VirtualSubsystem;

// TODO: Add photon camera sim
public class VisionSubsystem extends VirtualSubsystem {
    private final ArrayList<Camera> cameras = new ArrayList<>();
    private final ArrayList<Consumer<CameraIO.AprilTagPoseObservation>> resultConsumers = new ArrayList<>();

    public void addCameras(Collection<Camera> cameras) {
        cameras.addAll(cameras);
    }

    public void addCameras(Camera... cameras) {
        addCameras(List.of(cameras));
    }

    public void addResultConsumers(Collection<Consumer<CameraIO.AprilTagPoseObservation>> consumers) {
        resultConsumers.addAll(consumers);
    }

    @SafeVarargs
    public final void addResultConsumers(Consumer<CameraIO.AprilTagPoseObservation>... consumers) {
        addResultConsumers(List.of(consumers));
    }

    @Override
    public void periodic() {
        for (var camera : cameras) {
            camera.periodic();
            var observations = camera.getPoseObservations();
            for (var observation : observations) {
                for (var resultConsumer : resultConsumers) {
                    resultConsumer.accept(observation);
                }
            }
        }
    }

    @Override
    public void simulationPeriodic() {}


}
