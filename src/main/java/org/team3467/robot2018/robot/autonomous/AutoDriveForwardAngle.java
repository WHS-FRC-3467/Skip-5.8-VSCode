package org.team3467.robot2018.robot.autonomous;

import org.team3467.robot2018.subsystems.DriveBase.DriveStraight;
import org.team3467.robot2018.subsystems.DriveBase.DriveTurn;
import org.team3467.robot2018.subsystems.DriveBase.SetBrakeMode;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *  Drive at an angle to the left side to simply cross the starting line when positioned in the middle of the switch 
 */

public class AutoDriveForwardAngle extends CommandGroup {

    public  AutoDriveForwardAngle() {

    	// Brakes off
    	addSequential(new SetBrakeMode(false));

    	// Drive forward just a bit (to get off of DS wall)
    	addSequential(new DriveStraight(1500, 0.4));

    	// Turn 45 degrees toward left side of switch
    	addSequential(new DriveTurn(45.0, 0.35));
    	
    	// Drive to switch
    	addSequential(new DriveStraight(78000, 0.5));
    	
    	// Turn 45 degrees back toward switch
    	addSequential(new DriveTurn(-45.0, 0.35));
    	
    	// Drive to switch (make sure we are against it)
    	addSequential(new DriveStraight(6000, 0.4));
    	
    }
}
