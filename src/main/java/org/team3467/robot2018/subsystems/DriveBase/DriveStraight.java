package org.team3467.robot2018.subsystems.DriveBase;

import org.team3467.robot2018.robot.Robot;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Drive the given distance straight (negative values go backwards).
 * Uses a local PID controller to run a simple PID loop that is only
 * enabled while this command is running. The input is the averaged
 * values of the left and right encoders.
 */
public class DriveStraight extends Command {

	private static final double TOLERANCE = 50;
	
	// Working instance variables
	// We reinitialize these in initialize() just to make sure
	private PIDController m_pid;
	private double m_pastDistance = 0.0;
	private int m_count = 0;
//	private double m_initialHeading = 0.0;

	// Instance variables that may be set by one or more constructors
	private double m_maxSpeed = 0.6;
	private double m_distance = 0.0;
	private boolean m_manualCurve = false;
	private double m_curveValue = 0.0;
	private double KP = 2.0;
	private double KI = 0.0;
	private double KD = 0.0;
	
	boolean m_init = false;

	public DriveStraight(double distance, double maxSpeed, double kp, double ki, double kd) {
        
    	requires(Robot.driveBase);
    	KP = kp; KI = ki; KD = kd;
    	m_maxSpeed = maxSpeed;
    	m_distance = distance;
    }

    public DriveStraight(double distance, double maxSpeed) {
    
    	requires(Robot.driveBase);
    	m_maxSpeed = maxSpeed;
    	m_distance = distance;
    }
	
	public DriveStraight(double distance) {
    	requires(Robot.driveBase);
    	m_distance = distance;
	}

	public DriveStraight(double distance, boolean curve, double curveValue) {
		requires(Robot.driveBase);
		
		m_distance = distance;
		m_manualCurve = curve;
		m_curveValue = curveValue;
	}
	
	public DriveStraight(double distance, double maxSpeed, double timeOut) {
		requires(Robot.driveBase);
		m_distance = distance;
		m_maxSpeed = maxSpeed;
		setTimeout(timeOut);
	}
	
	private void buildController() {
		
		m_pid = new PIDController(KP, KI, KD,
	                new PIDSource() {
	                    PIDSourceType m_sourceType = PIDSourceType.kDisplacement;
	
	                    public double pidGet() {
	                        return Robot.driveBase.getDistance();
	                    }
	
	                    public void setPIDSourceType(PIDSourceType pidSource) {
	                      m_sourceType = pidSource;
	                    }
	
	                    public PIDSourceType getPIDSourceType() {
	                        return m_sourceType;
	                    }
	                },
	                new PIDOutput() {
	                	
	                	public void pidWrite(double d) {
	            			if (m_manualCurve) {
	            				Robot.driveBase.drive(d, m_curveValue, false);
	            			}
	            			else
	            			{
	            				// I think this is the correct approach, but need to fully test it again
	            				//double headDeviation = Robot.imu.getCurrentAngle() - m_initialHeading;
	            				
	            				// Drive with the magnitude returned by the PID calculation, 
		                		// and curve the opposite way from the current yaw deviation
	            				// (which conveniently has a sign  opposite of what the drive() method expects!)
		                		// (Divide currentAngle by some factor so as to normalize to -1.0 / + 1.0)
	            				Robot.driveBase.drive(d, (Robot.imu.getCurrentAngle()/240.), false);
	            				// Robot.driveBase.drive(d, (headDeviation/240.), false);
	            			}
	            		}
	                }
               );
		
        m_pid.setAbsoluteTolerance(TOLERANCE);
        m_pid.setOutputRange((m_maxSpeed * -1.0), m_maxSpeed);
        m_pid.setSetpoint(m_distance);
    }

	//If the robot has hit a wall, SAY SOMETHING!
	public boolean hasStalled() {
		if (Robot.driveBase.getDistance() - m_pastDistance <= 1) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
    // Called just before this Command runs the first time
    protected void initialize() {
    	// Get everything in a safe starting state.
    	m_pastDistance = 0.0;
    	m_count = 0;
        Robot.driveBase.resetEncoders();
        
        // Note the initial robot heading
        //m_initialHeading = Robot.imu.getCurrentAngle();
        Robot.imu.zeroAngle();
        
        buildController();
    	m_pid.reset();
        m_pid.enable();
 	
        m_init = true;
    }
    
    
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	if (!m_init) {
    		initialize();
    		m_init = true;
    	}
    	
    	Robot.driveBase.reportEncoders();
    	
    	if (hasStalled()) {
    		m_count++;
    	}
    	
    	m_pastDistance = Robot.driveBase.getDistance();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	
    	boolean retVal = false;
    	double error = m_pid.getError();
    	
    	if (m_count >= 50) {
    		// Robot is definitely stalled - return now
    		retVal = true;
    	}
    	else {
       		retVal = ((error >= 0 && error <= TOLERANCE) || (error < 0 && error >= (-1.0)*TOLERANCE));
    	}

    	if (retVal == true) {
    		// I suspect that sometimes end() doesn't get called or gets called late,
    		// allowing the PID loop to run too long, so just take care of business here
	    	m_pid.disable();
	        Robot.driveBase.drive(0, 0, false);
    	}
    	return retVal;
    
    }

    // Called once after isFinished returns true
    protected void end() {
    	// Stop PID and the wheels
    	m_init = false;
    	m_pid.disable();
    	m_pid.free();
        Robot.driveBase.drive(0, 0, false);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}

