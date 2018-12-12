package org.team3467.robot2018.subsystems.Pneumatics;

import org.team3467.robot2018.robot.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;


/**
 *
 */
public class GearShiftToggle extends InstantCommand {

	public GearShiftToggle() {
	}
	
    protected void execute() {
    	Robot.pneumatics.shiftGears();
    }

}
