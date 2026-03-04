package frc.robot.ballistics;

public record ShotExitSpeeds(double horizontal,double vertical){
    // Calculates total speed using the pythagorean theorem
    public double total() {
        return Math.sqrt(Math.pow(vertical, 2) + Math.pow(horizontal,2));
    }

    public double angle() {
        return Math.atan2(vertical,horizontal);
    }
}
