package org.team3467.robot2018.robot.autonomous;

import org.team3467.robot2018.subsystems.ArmLift.ArmLift;
import org.team3467.robot2018.subsystems.ArmLift.ArmLiftTransition;
import org.team3467.robot2018.subsystems.DriveBase.DriveStraight;
import org.team3467.robot2018.subsystems.DriveBase.DriveTurn;
import org.team3467.robot2018.subsystems.DriveBase.SetBrakeMode;
import org.team3467.robot2018.subsystems.Pneumatics.CloseHands;
import org.team3467.robot2018.subsystems.Pneumatics.OpenHands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *  Starting in the middle position, drive to right side of Switch and drop in Cube
 */

public class AutoRightSwitch_Middle extends CommandGroup {

    public  AutoRightSwitch_Middle() {

    	// Brakes off
    	addSequential(new SetBrakeMode(false));

    	// Drive forward just a bit (to get off of DS wall)
    	addSequential(new DriveStraight(1500, 0.4));

    	// Turn 45 degrees toward right side of switch
    	addSequential(new DriveTurn(-45.0, 0.35));
    	
    	// Drive to switch
    	addSequential(new DriveStraight(78000, 0.5));
    	
    	// Turn 45 degrees back toward switch
    	addSequential(new DriveTurn(45.0, 0.35));
    	
    	// Raise arm
    	addSequential(new ArmLiftTransition(ArmLift.eArmLiftState.SwitchFront));

    	// Drive to switch (make sure we are against it)
    	addSequential(new DriveStraight(6000, 0.4));
    	
    	// Wait to settle
    	addSequential(new WaitCommand(1.0));    	
    	
    	// Open hands, wait, then close
    	addSequential(new OpenHands());
    	addSequential(new WaitCommand(2.0));
    	addSequential(new CloseHands());
    	
    	
    }
}
