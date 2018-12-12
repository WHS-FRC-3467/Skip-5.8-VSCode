package org.team3467.robot2018.subsystems.DriveBase;

import org.team3467.robot2018.robot.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;


/**
 *
 */
public class SetBrakeMode extends InstantCommand {

	boolean brakesOn;
	
    public SetBrakeMode(boolean setBrake) {
        requires(Robot.driveBase);
        brakesOn = setBrake;
    }

    protected void execute() {
    	Robot.driveBase.setTalonBrakes(brakesOn);
    }

}
