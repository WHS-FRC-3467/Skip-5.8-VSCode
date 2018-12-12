package org.team3467.robot2018.robot.control.triggers;

import org.team3467.robot2018.robot.control.Gamepad;

import edu.wpi.first.wpilibj.buttons.Trigger;

/**
 *
 */
public class GamepadLeftTrigger extends Trigger {
    
	Gamepad m_gamepad;
	public GamepadLeftTrigger(Gamepad gamepad) {
		this.m_gamepad = gamepad;
	}	

    public boolean get() {
    		boolean leftTrigger = false;
    		if(m_gamepad.getRawAxis(Gamepad.leftTrigger_Axis) > .8 ){
    			leftTrigger = true;
    		}
    		else{
    			leftTrigger = false;
    		}
    		return leftTrigger;
    }
}
