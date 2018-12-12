
package org.team3467.robot2018.subsystems.ArmLift;

import org.team3467.robot2018.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class ArmLiftTest extends Command {

	private int m_counter = 0;
	private int m_leftTarget, m_rightTarget;
	private boolean m_noArgs = false;
	
	public ArmLiftTest() {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.armLift);
        m_noArgs = true;
	}
	
	public ArmLiftTest(int leftT, int rightT) {
		
        requires(Robot.armLift);
		m_leftTarget = leftT;
		m_rightTarget = rightT;
		
	}

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	if (m_noArgs)
    		Robot.armLift.moveMagically();
    	else
    		Robot.armLift.moveMagically(m_leftTarget, m_rightTarget);
    	
    	Robot.armLift.reportLimits();
		if (m_counter < 30) {
			m_counter++;
		}
		else {
			SmartDashboard.putString("ArmLift State", Robot.armLift.getState().getName());
			Robot.armLift.reportEncoders();
			Robot.armLift.reportTalonStats();
			m_counter = 0;
		}

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return(false);
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.armLift.stopMotors();
    }
}
