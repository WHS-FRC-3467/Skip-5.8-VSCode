
package org.team3467.robot2018.subsystems.ArmLift;

import org.team3467.robot2018.robot.OI;
import org.team3467.robot2018.robot.Robot;
import org.team3467.robot2018.subsystems.ArmLift.ArmLift.eArmLiftState;

import edu.wpi.first.wpilibj.command.Command;

/**
 *  Drive the lift and arm rotation manually
 */
public class ManualArm extends Command {

	private static final double MAX_SPEED_FACTOR = 0.35;
	
	public ManualArm() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.armLift);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    	Robot.armLift.setState(eArmLiftState.Unknown);
    
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double speed = OI.getInstance().getOperatorRightX() * MAX_SPEED_FACTOR;
		if ((speed > -.1) && (speed < .1)) {
			speed = 0.0;
		}
		Robot.armLift.moveArm(speed);
		Robot.armLift.reportEncoders();
		Robot.armLift.reportLimits();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
