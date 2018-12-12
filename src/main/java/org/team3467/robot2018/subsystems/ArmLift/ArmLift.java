
package org.team3467.robot2018.subsystems.ArmLift;

import org.team3467.robot2018.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class ArmLift extends Subsystem {
    

	// This is the current positional state of the ArmLift subsystem
	public enum eArmLiftState {
		OnFloor(0, "On Floor"),
		OnStep(1, "On Step"),  // Stow / Start Position: Less prone to gravity!
		Level2Cube(2, "2nd Level Cube"),
		SwitchFront(3, "Front Switch / 3rd Level Cube"),
		HighLow(4, "High/Low"),	// Lift High, Arm Low - standard intermediate position
		ScaleHigh(5, "Scale: High"),
		ScaleLow(6, "Scale: Low"),
		SwitchBack(7, "Back Switch"),
		OnTrunk(8, "On Trunk"),
		Unknown(9, "UNKNOWN STATE"); // When in this state, move to HiLo before doing anything else
		
		private final int value;
		private final String name;
		
		private eArmLiftState(int value, String name) {
			this.value = value;
			this.name = name;
		}
		
		public int getVal() {
			return this.value;
		}
		
		public String getName() {
			return this.name();
		}
	}
	
	// TalonSRX objects
	private ArmLiftCANTalon  m_leftTalon, m_rightTalon;

	// Limit switches
	public DigitalInput m_LSLiftTop, m_LSLiftBottom, m_LSArmFront, m_LSArmBack;
	
	// Current ArmLift State
	private eArmLiftState currentState;
	
	// Static subsystem reference
	private static ArmLift armInstance = new ArmLift();

	public static ArmLift getInstance() {
		return ArmLift.armInstance;
	}

	//DriveBase class constructor
	protected ArmLift() {

		// Limit Switches
		m_LSLiftTop = new DigitalInput(RobotMap.liftTopLimitSwitch);
		m_LSLiftBottom = new DigitalInput(RobotMap.liftBottomLimitSwitch);
		m_LSArmFront = new DigitalInput(RobotMap.armFrontLimitSwitch);
		m_LSArmBack = new DigitalInput(RobotMap.armBackLimitSwitch);
		
		// Setup both Talons inside a management wrapper class
		m_leftTalon = new ArmLiftCANTalon("ArmLift Left", RobotMap.liftarm_leftTalon, 0);
		m_rightTalon = new ArmLiftCANTalon("ArmLift Right", RobotMap.liftarm_rightTalon, 0);
		
/*
  		// Config CAN limit switches
 		m_leftTalon.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector,
				LimitSwitchNormal.NormallyOpen, 0);
		m_leftTalon.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector,
				LimitSwitchNormal.NormallyOpen, 0);
*/
		// Flip Motor Directions
		m_leftTalon.setInverted(true);
		
		// Flip sensors so they count positive in the positive control direction
		m_leftTalon.setSensorPhase(true);
		m_rightTalon.setSensorPhase(true);
		
		// Stop all Talon motion (for now)
		m_leftTalon.set(ControlMode.PercentOutput, 0.0);
		m_rightTalon.set(ControlMode.PercentOutput, 0.0);
		
		// Assuming this is Starting Position; if not, then need to change it
		setState(eArmLiftState.OnStep);
	}
	

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new ReportStats());
    }
    
    public void reportTalonStats() {
    	m_leftTalon.reportMotionToDashboard();
    	m_rightTalon.reportMotionToDashboard();
    }
    
    public void updateTalonStats() {
    	m_leftTalon.updateStats();
    	m_rightTalon.updateStats();
    }
    
    public int getLeftEncoder() {
    	return m_leftTalon.getSelectedSensorPosition(0);
    }
    
    public int getRightEncoder() {
    	return m_rightTalon.getSelectedSensorPosition(0);	
    }
    
    public void zeroEncoders() {
    	m_leftTalon.setSelectedSensorPosition(0, 0, 0);
    	m_rightTalon.setSelectedSensorPosition(0, 0, 0);
    }
    
    public void reportEncoders() {
		SmartDashboard.putNumber("ArmLift Left Encoder", getLeftEncoder());
		SmartDashboard.putNumber("ArmLift Right Encoder", getRightEncoder());
    }
    
    /*
     * Individual Motor Control     
     */
    public void moveLeftMotor(double speed) {
		m_leftTalon.set(ControlMode.PercentOutput, speed);
    }
    
    public void moveRightMotor(double speed) {
		m_rightTalon.set(ControlMode.PercentOutput, speed);
    }

    public void stopMotors() {
		m_leftTalon.set(ControlMode.PercentOutput, 0.0);
		m_rightTalon.set(ControlMode.PercentOutput, 0.0);
    }
    
    public void setSetpointsFromDashboard() {
    	m_leftTalon.updateSetpointFromDashboard();
    	m_rightTalon.updateSetpointFromDashboard();
    }

    /*
     * Closed Loop Motion Control
     */
    public void moveMagically (int left_setPoint, int right_setPoint) {

		// WARNING: Does NOT check limit switches!
    	m_leftTalon.runMotionMagic(left_setPoint);
		m_rightTalon.runMotionMagic(right_setPoint);
    }
    
    public void moveMagically () {

		// WARNING: Does NOT check limit switches!
    	m_leftTalon.runMotionMagic();
		m_rightTalon.runMotionMagic();
    }
    
    
    public boolean onTarget(boolean closeEnough) {
    	
    	int leftError = m_leftTalon.getClosedLoopError(0);
    	int rightError = m_rightTalon.getClosedLoopError(0);
    	int leftAllowable = m_leftTalon.getTolerance();
    	int rightAllowable = m_rightTalon.getTolerance();
    	
    	if (closeEnough) {
    		leftAllowable += 20;
    		rightAllowable += 20;
    	}
    		
		return (((leftError >= 0 && leftError <= leftAllowable) ||
			(leftError < 0 && leftError >= (-1.0) * leftAllowable)) &&
			((rightError >= 0 && rightError <= rightAllowable) ||
			(rightError < 0 && rightError >= (-1.0) * rightAllowable))
			);

    }
    
    /*
	 * Manual Lift Control
	 */
	public void moveLift(double speed) {

		speed = checkLiftLimits(speed);
		
		m_leftTalon.set(ControlMode.PercentOutput, -speed);
		m_rightTalon.set(ControlMode.PercentOutput, speed);
		
	}

	/*
	 * Manual Arm Control
	 */
	public void moveArm(double speed) {

		speed = checkArmLimits(speed);
		
		if (speed > 0.0) {
			m_leftTalon.set(ControlMode.PercentOutput, speed * .70);
			m_rightTalon.set(ControlMode.PercentOutput, speed);
		} else {
			m_leftTalon.set(ControlMode.PercentOutput, speed);
			m_rightTalon.set(ControlMode.PercentOutput, speed * .70);
		}
	}
	
	
	// Check for Lift limit switches
	public double checkLiftLimits(double speed) {
		if ((speed > 0.0 && isSwitchHit(RobotMap.liftTopLimitSwitch)) ||
				(speed < 0.0 && isSwitchHit(RobotMap.liftBottomLimitSwitch)))
		{
			return 0.0;
		}
		else
		{
			return speed;
		}
	}
	
	// Check for Arm limit switches
	public double checkArmLimits(double speed) {
		if ((speed > 0.0 && isSwitchHit(RobotMap.armFrontLimitSwitch))
//				|| (speed < 0.0 && isSwitchHit(RobotMap.armBackLimitSwitch))
			)
		{
			return 0.0;
		}
		else
		{
			return speed;
		}
	}

	/*
	 * Check all connected limits
	 */
	public boolean isAnySwitchHit() {
		if ((!m_LSLiftTop.get()) || (!m_LSLiftBottom.get()) || (!m_LSArmFront.get())) //|| !m_LSArmBack.get()
			return true;
		else
			return false;
	}
	
	/*
	 * Report on specified limit switch (true = open; false = pressed)
	 */
	public boolean isSwitchHit(int switchnum) {
		
		boolean retVal = false;
		
		switch(switchnum) {
		case RobotMap.liftTopLimitSwitch:
			retVal = !m_LSLiftTop.get();
			break;
		case RobotMap.liftBottomLimitSwitch:
			retVal = !m_LSLiftBottom.get();
			break;
		case RobotMap.armFrontLimitSwitch:
			retVal = !m_LSArmFront.get();
			break;
		case RobotMap.armBackLimitSwitch:
			retVal = !m_LSArmBack.get();
			break;
		}
		
		return retVal;
	}
	
	public void reportLimits() {
		SmartDashboard.putBoolean("Upper Arm Limit", m_LSLiftTop.get() );
		SmartDashboard.putBoolean("Lower Arm Limit", m_LSLiftBottom.get() );
		SmartDashboard.putBoolean("Front Arm Limit", m_LSArmFront.get() );
		SmartDashboard.putBoolean("Back Arm Limit", m_LSArmBack.get() );
	}
	
    public eArmLiftState getState() {
    	return currentState;
    }
    
    public void setState(eArmLiftState newState) {
    	currentState = newState;
    }
    
	
}

