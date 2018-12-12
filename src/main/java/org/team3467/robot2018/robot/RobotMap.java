package org.team3467.robot2018.robot;

/**
 * The RobotMap is a mapping of the hardware port, sensor, and actuator numbers to
 * a code variable name. This provides flexibility in changing wiring, makes checking
 * the wiring easier, and significantly reduces the number of magic numbers floating around in the code.
 */
public class RobotMap {
	
    public static final int driveBase_leftTalon1 = 1;
    public static final int driveBase_leftTalon2 = 2;
    public static final int driveBase_leftTalon3 = 3;
    public static final int driveBase_rightTalon1 = 4;
    public static final int driveBase_rightTalon2 = 5;
    public static final int driveBase_rightTalon3 = 6;
    
    public static final int liftarm_leftTalon = 7;
    public static final int liftarm_rightTalon = 8;
    
    public static final int climber_Talon = 9;
    public static final int hookDeploy_Talon = 10;
    
    
    // Limit switches
	public static final int liftTopLimitSwitch = 2;
	public static final int liftBottomLimitSwitch = 1;
	public static final int armFrontLimitSwitch = 3;
	public static final int armBackLimitSwitch = 4;

    
}
