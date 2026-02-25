# To Do

* Figure out preliminary driver controls and commands. The temporary/test controls are not for competition!
* PID closed-loop control of shooter (TalonFXS)
* PID closed-loop control of stage (SparkMax)
* Calibrate shooting based on distance to HUB -- lots and lots of test shots and data collection
* Detect indexing jam (fuel jammed against stage stalls indexer)
* Configure pathplanner and verify with a simple movement auto path

# Driver Controls

* Teleop Driving
  * Left - Field relative movement (LeftX, LeftY)
  * Right - Angular orientation (RightX)
* Hopper (Intake Position)
  * Y held - Extend Intake
  * X held - Retract Intake
* Intake (Gather)
  * ?
  * Pov Down held - Reverses the direction of fuel to put out of the robot
  * B held - Gather from floor
* Shoot
  * ?
  * Trigger Left - Fast (high power) test shot
  * Trigger Right - Slow (low power) test shot
  * Bumper Left - Hood up
  * Bumper Right - Hood down

# Commands Robot Will Perform

## Drive

## Expand hopper (Extend intake, Prepare to gather)

## Gather from floor

1. Start intake
1. Index to stage (when jammed, stop indexing)
1. wait until driver stops
1. Stop intake

## Score Fuel

Needs: Distance to HUB (using either vision or predefined field location)

1. Reverse stage and Index from stage for 0.5 sec to avoid contact with stage wheel
1. Spin up shooter with setpoint
1. Adjust hood
1. Spin up stage with setpoint (same setpoint? 80% of shooter setpoint?)
1. Index to stage
1. wait until hopper empty or driver stops (automaticlly collapse hopper?)
1. Stop index, Stop stage, Stop shooter, Fully retract hood

Future Improvement: Auto-align to face HUB using vision

## Collapse hopper (Retract/Protect intake, Prepare to shoot)

Possibly run intake at low power while collapsing to shuffle fuel in the hopper. This will shuffle the balls without launching them out of the hopper.

## Yeet Fuel (Passing between zones)

## Utility Tasks

### Clear shooter jam

### Clear intake jam

### Retract hood

### Reset field orientation (Reset NavX gyro)
