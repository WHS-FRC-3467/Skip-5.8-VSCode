package org.team3467.robot2018.robot.autonomous;

import org.team3467.robot2018.subsystems.DriveBase.DriveStraight;
import org.team3467.robot2018.subsystems.DriveBase.SetBrakeMode;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *  Drive straight ahead for specified distance
 */

public class AutoDriveForward extends CommandGroup {

    public  AutoDriveForward() {

    	// Brakes off
    	addSequential(new SetBrakeMode(false));

    	// Drive
    	addSequential(new DriveStraight(65000, 0.6));
    	
    	// After we stop, turn brakes back on
 //   	addSequential(new SetBrakeMode(true));
    	
    }
}
