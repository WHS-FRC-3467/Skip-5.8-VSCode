package org.team3467.robot2018.subsystems.DriveBase;

import org.team3467.robot2018.robot.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class ResetDriveEncoders extends InstantCommand {

    public ResetDriveEncoders() {
        requires(Robot.driveBase);
    }

    protected void execute() {
		Robot.driveBase.resetEncoders();
    }

}
