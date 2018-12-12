package org.team3467.robot2018.robot.control.triggers;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.*;

/**
 * A custom button that is triggered when two buttons on a GenericHID are
 * simultaneously pressed.
 */
public class SingleButtonandIgnoreOtherButton extends Trigger {
	private GenericHID dev;
	private int button1, button2;
	boolean triggerstate;
	
	public SingleButtonandIgnoreOtherButton(GenericHID dev, int button1, int button2) {
		this.dev = dev;
		this.button1 = button1;
		this.button2 = button2;

	}	
    
    public boolean get() {
    	if((dev.getRawButton(button1) == true) && (dev.getRawButton(button2) == false)){
    		triggerstate = true;
    	}
    	else{
    		triggerstate = false;
    	}
        return triggerstate;
    }
}
