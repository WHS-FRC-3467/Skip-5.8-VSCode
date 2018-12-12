package org.team3467.robot2018.subsystems.IMU;

import org.team3467.robot2018.robot.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;


/**
 *
 */
public class ZeroGyro extends InstantCommand {

	public ZeroGyro() {
	}
	
    protected void execute() {
    	Robot.imu.zeroAngle();
    }
    
}
