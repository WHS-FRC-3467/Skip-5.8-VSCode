package org.team3467.robot2018.robot.autonomous;

import org.team3467.robot2018.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class AutoDriveForTime_Backwards extends Command {

	public AutoDriveForTime_Backwards(double time) {
        requires(Robot.driveBase);
        setTimeout(time);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.driveBase.resetEncoders();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	// This drives BACKWARDS
    	Robot.driveBase.driveTank(-0.5, -0.5);
    	Robot.driveBase.reportEncoders();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut();
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
