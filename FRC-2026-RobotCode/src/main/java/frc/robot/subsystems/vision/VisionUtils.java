package frc.robot.subsystems.vision;

import org.ejml.simple.SimpleMatrix;

import edu.wpi.first.math.Nat;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.Vector;
import edu.wpi.first.math.estimator.PoseEstimator3d;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.math.numbers.N4;

public class VisionUtils {
    private VisionUtils() {}
    public static Vector<N3> collapse3DstddevsTo2d(Vector<N4> stddevs) {
        return VecBuilder.fill(
            stddevs.get(0),
            stddevs.get(1),
            stddevs.get(3)
        );
    }
}
