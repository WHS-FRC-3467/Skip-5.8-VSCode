package org.team3467.robot2018.subsystems.ArmLift;

import org.team3467.robot2018.robot.Robot;
import org.team3467.robot2018.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * 
 */
public class ArmLiftCalibrate extends Command {

	private static final double DEFAULT_MAX_SPEED = 0.5;
	private boolean m_isFinished = false;
	private int m_state = 0;
	private double m_time = 0.0;
	
	
	public ArmLiftCalibrate() {
    	requires(Robot.armLift);
	}

    // Called just before this Command runs the first time
    protected void initialize() {
    	m_isFinished = false;
    	m_state = 0;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

		switch (m_state) {
		case 0:
			
			if (!Robot.armLift.isSwitchHit(RobotMap.armFrontLimitSwitch)) {
	    		// Drive arm forward until limit is hit
				Robot.armLift.moveArm(DEFAULT_MAX_SPEED / 3.0);
			}
			else
				m_state = 1;
			break;
			
		case 1:
			
			if (!Robot.armLift.isSwitchHit(RobotMap.liftBottomLimitSwitch)) {
				// Automatically drive lift down until limit is hit
				Robot.armLift.moveLift(-DEFAULT_MAX_SPEED / 2.0);
			}
			else
				m_state = 2;
				m_time = Timer.getFPGATimestamp();

			break;
			
		case 2:
			
			if ((Timer.getFPGATimestamp() - m_time) > 5.0) {
				Robot.armLift.zeroEncoders();
				m_isFinished = true;
			}
			else
			{
				m_isFinished = false;
			}
		}
    }
    
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return m_isFinished;
    	
    }

    // Called once after isFinished returns true
    protected void end() {
		Robot.armLift.stopMotors();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}

