package org.team3467.robot2018.robot.control.triggers;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.*;

/**
 * A custom button that is triggered when two buttons on a GenericHID are
 * simultaneously pressed.
 */
public class DoubleButton extends Trigger {
	private GenericHID dev;
	private int button1, button2;
	
	public DoubleButton(GenericHID dev, int button1, int button2) {
		this.dev = dev;
		this.button1 = button1;
		this.button2 = button2;
	}	
    
    public boolean get() {
        return dev.getRawButton(button1) && dev.getRawButton(button2);
    }
}
