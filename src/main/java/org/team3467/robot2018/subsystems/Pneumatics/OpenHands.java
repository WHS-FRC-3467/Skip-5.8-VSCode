package org.team3467.robot2018.subsystems.Pneumatics;

import org.team3467.robot2018.robot.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;


/**
 *
 */
public class OpenHands extends InstantCommand {

	public OpenHands() {
	}
	
    protected void execute() {
    	Robot.pneumatics.openHands();
    }
    
}
