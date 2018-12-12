package org.team3467.robot2018.subsystems.ArmLift	;

import org.team3467.robot2018.robot.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;


/**
 *
 */
public class ZeroEncoders extends InstantCommand {

	public ZeroEncoders() {
	}
	
    protected void execute() {
    	Robot.armLift.zeroEncoders();
    	Robot.armLift.reportEncoders();
    }
    
}
