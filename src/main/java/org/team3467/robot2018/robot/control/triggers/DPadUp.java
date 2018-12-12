package org.team3467.robot2018.robot.control.triggers;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.*;

/**
 * Gamepad DPadUp support
 */
public class DPadUp extends Trigger {
	private GenericHID dev;
	
	public DPadUp(GenericHID dev) {
		this.dev = dev;
	}	

	public boolean get() {
        return (dev.getPOV(0) == 0);
    }
}
