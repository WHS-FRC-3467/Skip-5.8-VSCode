
package org.team3467.robot2018.subsystems.ArmLift;

import org.team3467.robot2018.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *  
 */
public class ReportStats extends Command {

	private int counter;
	
	public ReportStats() {
		requires(Robot.armLift);
		this.setInterruptible(true);
	}
	
	protected void initialize() {
		counter = 0;
	}

	protected void execute() {
		Robot.armLift.reportLimits();
		if (counter < 25) {
			counter++;
		}
		else {
			SmartDashboard.putString("ArmLift State", Robot.armLift.getState().getName());
			Robot.armLift.reportEncoders();
			Robot.armLift.reportTalonStats();
			counter = 0;
		}
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
		
	}

	protected void interrupted() {
		end();
	}
}
