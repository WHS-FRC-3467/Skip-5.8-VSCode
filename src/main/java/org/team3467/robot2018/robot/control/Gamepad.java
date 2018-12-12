package org.team3467.robot2018.robot.control;

import edu.wpi.first.wpilibj.GenericHID;

/**
 * Class for using the Logitech F310 gamepad. DO NOT USE WITH DUAL ACTION!!
 *
 */
public class Gamepad extends GenericHID {
	public static int xButton = 3;
	public static int aButton = 1;
	public static int bButton = 2;
	public static int yButton = 4;
	public static int leftBumper = 5;
	public static int rightBumper = 6;
	public static int backButton = 7;
	public static int startButton = 8;
	public static int leftStickPress = 9;
	public static int rightStickPress = 10;
	
	public static int leftStick_xAxis = 0;
	public static int leftStick_yAxis = 1;
	public static int leftTrigger_Axis = 2;
	public static int rightTrigger_Axis = 3;
	public static int rightStick_xAxis = 4;
	public static int rightStick_yAxis = 5;
	
	public Gamepad(int port) {
		super(port);
	}
	
	public double getLeftStickX() {
		return getRawAxis(leftStick_xAxis);
	}
	
	public double getLeftStickY() {
		return getRawAxis(leftStick_yAxis);
	}
	
	public boolean getLeftAxisButton() {
		return getRawButton(leftStickPress);
	}
	
	public double getRightStickX() {
		return getRawAxis(rightStick_xAxis);
	}
	
	public double getRightStickY() {
		return getRawAxis(rightStick_yAxis);
	}
	
	public boolean getRightAxisButton() {
		return getRawButton(rightStickPress);
	}
	
	public boolean getLeftBumper() {
		return getRawButton(leftBumper);
	}
	
	public boolean getRightBumper() {
		return getRawButton(rightBumper);
	}
	
	public double getLeftTrigger()
	{
		return getRawAxis(leftTrigger_Axis);
	}
	
	public double getRightTrigger()
	{
		return getRawAxis(rightTrigger_Axis);
	}
	
	public boolean getLeftTriggerButton() {
		boolean leftTrigger = false;
		if(getRawAxis(leftTrigger_Axis) > .8 ){
			leftTrigger = true;
		}
		else{
			leftTrigger = false;
		}
		return leftTrigger;
	}
	
	public boolean getRightTriggerButton() {
		boolean rightTrigger = false;
		if(getRawAxis(rightTrigger_Axis) > .8 ){
			rightTrigger = true;
		}
		else{
			rightTrigger = false;
		}
		return rightTrigger;
	}
	
	public boolean getXButton() {
		return getRawButton(xButton);
	}
	
	public boolean getAButton() {
		return getRawButton(aButton);
	}
	
	public boolean getBButton() {
		return getRawButton(bButton);
	}
	
	public boolean getYButton() {
		return getRawButton(yButton);
	}
	
	public boolean getStartButton() {
		return getRawButton(startButton);
	}
	
	public boolean getBackButton() {
		return getRawButton(backButton);
	}
	
	public boolean getDpadUp() {
		return(getPOV(0) == 0 ? true : false);
	}
	
	public boolean getDpadRight() {
		return(getPOV(0) == 90 ? true : false);
	}
	
	public boolean getDpadDown() {
		return(getPOV(0) == 180 ? true : false);
	}

	public boolean getDpadLeft() {
		return(getPOV(0) == 270 ? true : false);
	}

	public double getX(Hand hand) {
		if (hand.equals(Hand.kLeft)) {
			return getRawAxis(leftStick_xAxis);
		} else {
			return getRawAxis(rightStick_xAxis);
		}
	}

	public double getY(Hand hand) {
	  if (hand.equals(Hand.kLeft)) {
	    return getRawAxis(leftStick_yAxis);
	  } else {
	    return getRawAxis(rightStick_yAxis);
	  }
	}
}
