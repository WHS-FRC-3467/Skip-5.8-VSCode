package org.team3467.robot2018.robot.control.triggers;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.*;

/**
 * Gamepad DPadUp support
 */
public class DPadLeft extends Trigger {
	private GenericHID dev;
	
	public DPadLeft(GenericHID dev) {
		this.dev = dev;
	}	

	public boolean get() {
        return (dev.getPOV(0) == 270);
    }
}
