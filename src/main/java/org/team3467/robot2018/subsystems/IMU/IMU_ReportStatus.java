package org.team3467.robot2018.subsystems.IMU;

import org.team3467.robot2018.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class IMU_ReportStatus extends Command {

	private int counter;
	
	public IMU_ReportStatus() {
		requires(Robot.imu);
		this.setInterruptible(true);
	}
	
	protected void initialize() {
		counter = 0;
	}

	protected void execute() {
		if (counter < 50) {
			counter++;
		}
		else {
			Robot.imu.displayIMUUpdate();
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
