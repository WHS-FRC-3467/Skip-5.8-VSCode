
package org.team3467.robot2018.subsystems.ArmLift;

import org.team3467.robot2018.robot.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *  
 */
public class UpdateStats extends InstantCommand {

	public UpdateStats() {
		requires(Robot.armLift);
		this.setInterruptible(true);
	}
	
	protected void execute() {
		Robot.armLift.updateTalonStats();
	}

}
