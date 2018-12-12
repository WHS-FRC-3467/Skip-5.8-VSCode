package org.team3467.robot2018.subsystems.DriveBase;


import edu.wpi.first.wpilibj.command.Subsystem;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.team3467.robot2018.robot.RobotMap;
import org.team3467.robot2018.subsystems.DriveBase.DriveBot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;

public class DriveBase extends Subsystem {
	
	// WPI_TalonSRX objects and RobotDrive object
	private WPI_TalonSRX 			leftTalon1, rightTalon1, leftTalon2, rightTalon2, leftTalon3, rightTalon3;
	private static DifferentialDrive m_drive;
	private ControlMode 			m_talonControlMode;
	private int						m_driveMode;

	// Static subsystem reference
	private static DriveBase dBInstance = new DriveBase();

	public static DriveBase getInstance() {
		return DriveBase.dBInstance;
	}
	
	//DriveBase class constructor
	protected DriveBase() {

		// Three motors per side -> three speed controllers per side
		leftTalon1 = new WPI_TalonSRX(RobotMap.driveBase_leftTalon1);
		leftTalon2 = new WPI_TalonSRX(RobotMap.driveBase_leftTalon2);
		leftTalon3 = new WPI_TalonSRX(RobotMap.driveBase_leftTalon3);
		rightTalon1 = new WPI_TalonSRX(RobotMap.driveBase_rightTalon1);
		rightTalon2 = new WPI_TalonSRX(RobotMap.driveBase_rightTalon2);
		rightTalon3 = new WPI_TalonSRX(RobotMap.driveBase_rightTalon3);
		
		// Slave the extra Talons on each side
		leftTalon1.follow(leftTalon2);
		leftTalon3.follow(leftTalon2);
		rightTalon1.follow(rightTalon2);
		rightTalon3.follow(rightTalon2);
		
		// Flip any sensors?
		leftTalon2.setSensorPhase(true);
		
		// Invert all motors (until we figure out why controls are backward)
 		leftTalon1.setInverted(true);
		leftTalon2.setInverted(true);
		leftTalon3.setInverted(true);
		rightTalon1.setInverted(true);
		rightTalon2.setInverted(true);
		rightTalon3.setInverted(true);

		/* set the peak and nominal outputs, 1.0 means full */
 		leftTalon1.configNominalOutputForward(0, 0); leftTalon1.configNominalOutputReverse(0, 0);
		leftTalon1.configPeakOutputForward(1.0, 0); leftTalon1.configPeakOutputReverse(-1.0, 0);
 
		leftTalon2.configNominalOutputForward(0, 0); leftTalon2.configNominalOutputReverse(0, 0);
		leftTalon2.configPeakOutputForward(1.0, 0); leftTalon2.configPeakOutputReverse(-1.0, 0);
 
		leftTalon3.configNominalOutputForward(0, 0); leftTalon3.configNominalOutputReverse(0, 0);
		leftTalon3.configPeakOutputForward(1.0, 0); leftTalon3.configPeakOutputReverse(-1.0, 0);
 
		rightTalon1.configNominalOutputForward(0, 0); rightTalon1.configNominalOutputReverse(0, 0);
		rightTalon1.configPeakOutputForward(1.0, 0); rightTalon1.configPeakOutputReverse(-1.0, 0);
 
		rightTalon2.configNominalOutputForward(0, 0); rightTalon2.configNominalOutputReverse(0, 0);
		rightTalon2.configPeakOutputForward(1.0, 0); rightTalon2.configPeakOutputReverse(-1.0, 0);
 
		rightTalon3.configNominalOutputForward(0, 0); rightTalon3.configNominalOutputReverse(0, 0);
		rightTalon3.configPeakOutputForward(1.0, 0); rightTalon3.configPeakOutputReverse(-1.0, 0);
 
		// Turn off Brake mode
		setTalonBrakes(false);
		
		// Set default control Modes for Master Talons
		setControlMode(ControlMode.PercentOutput);
		
 		// Set encoders as feedback device
		leftTalon2.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
		rightTalon2.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 10);
		
		// Instantiate DifferentialDrive
		m_drive = new DifferentialDrive(leftTalon2, rightTalon2);
		
		// DifferentialDrive Parameters
		m_drive.setSafetyEnabled(true);
		m_drive.setExpiration(1.0);
		m_drive.setMaxOutput(1.0);
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new DriveBot(DriveBot.driveMode_Rocket, false));
    }
    
	public WPI_TalonSRX getLeftTalon() {
		return leftTalon2;
	}
	
	public WPI_TalonSRX getRightTalon() {
		return rightTalon2;
	}
	
	/* Get the Talon that is controlling the IMU */
	public TalonSRX getIMUTalon() {
		return ((TalonSRX) leftTalon3);
	}
	
	//Use standard Tank Drive method
	public void driveTank (double leftSpeed, double rightSpeed) {
		m_drive.tankDrive(leftSpeed, rightSpeed, false);
	}

	// Use single-stick Arcade Drive method
	public void driveArcade(double move, double rotate) {
		m_drive.arcadeDrive(move, rotate, false);
	}

	// Use DifferentialDrive curvatureDrive() method
	public void drive(double outputMagnitude, double curve, boolean spin) {
		m_drive.curvatureDrive(outputMagnitude, curve, spin);
	}

	/**
	 * @param controlMode Set the control mode of the left and
	 * right master WPI_TalonSRXs
	 */
	public void setControlMode(ControlMode controlMode) {
		leftTalon2.set(controlMode, 0.0);
		rightTalon2.set(controlMode, 0.0);
		
		// Save control mode so we will know if we have to set it back later
		m_talonControlMode = controlMode;
	}
	
	/**
	 * @return The current WPI_TalonSRX control mode
	 */
	public String getTalonControlMode() {
		if (m_talonControlMode == ControlMode.PercentOutput) {
			return "PercentVbus";
		}
		else if(m_talonControlMode == ControlMode.Position) {
			return "Position";
		}
		else
			return "Problem";
	}
	
	/**
	 * Sets the drive control mode
	 * @param driveMode - values defined in DriveBot
	 */
	public void setDriveControlMode(int driveMode) {
		m_driveMode = driveMode;
	}
	
	/**
	 * Gets the drive control mode
	 * @return driveMode - values defined in DriveBot
	 */
	public int getDriveControlMode() {
		return m_driveMode;
	}
	
	
	/**
	 * Sets the brake mode for ALL WPI_TalonSRXs
	 * @param setBrake Enable brake mode?
	 */
	public void setTalonBrakes(boolean setBrake) {

		NeutralMode nm = setBrake ? NeutralMode.Brake : NeutralMode.Coast;
		
		leftTalon1.setNeutralMode(nm);
		rightTalon1.setNeutralMode(nm);
		leftTalon2.setNeutralMode(nm);
		rightTalon2.setNeutralMode(nm);
		leftTalon3.setNeutralMode(nm);
		rightTalon3.setNeutralMode(nm);
		
		SmartDashboard.putBoolean("Talon Brakes", setBrake);
	}
	
	
 	// @return Average of the encoder values from the left and right encoders
	public double getDistance() {
		return (leftTalon2.getSelectedSensorPosition(0) + 
//				(rightTalon2.getSelectedSensorPosition(0) * -1.0))/2;
				rightTalon2.getSelectedSensorPosition(0) ) / 2;
	}

	public void reportEncoders() {
		SmartDashboard.putNumber("Left Encoder", leftTalon2.getSelectedSensorPosition(0));
//		SmartDashboard.putNumber("Right Encoder", rightTalon2.getSelectedSensorPosition(0) * -1.0);			
		SmartDashboard.putNumber("Right Encoder", rightTalon2.getSelectedSensorPosition(0));			
	}

	public void resetEncoders() {
		leftTalon2.setSelectedSensorPosition(0, 0, 0);
		rightTalon2.setSelectedSensorPosition(0, 0, 0);
	}

}
