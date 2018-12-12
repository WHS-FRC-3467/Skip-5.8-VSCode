
package org.team3467.robot2018.robot;

import org.team3467.robot2018.subsystems.FieldCamera.FieldCamera;
import org.team3467.robot2018.subsystems.IMU.IMU;
import org.team3467.robot2018.subsystems.ArmLift.ArmLift;
import org.team3467.robot2018.subsystems.DriveBase.DriveBase;
import org.team3467.robot2018.subsystems.Pneumatics.Pneumatics;
import org.team3467.robot2018.robot.autonomous.AutoRightSwitch_Front;
import org.team3467.robot2018.robot.autonomous.AutoRightSwitch_Middle;
import org.team3467.robot2018.robot.autonomous.AutoRightSwitch_Side;
import org.team3467.robot2018.robot.autonomous.AutoDriveForward;
import org.team3467.robot2018.robot.autonomous.AutoDriveForwardAngle;
import org.team3467.robot2018.robot.autonomous.AutoLeftSwitch_Front;
import org.team3467.robot2018.robot.autonomous.AutoLeftSwitch_Middle;
import org.team3467.robot2018.robot.autonomous.AutoLeftSwitch_Side;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import openrio.powerup.MatchData;


/**
 * This is the top-level robot class.
 * 
 * The VM is configured to automatically run this class, and to call the
 * methods corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */


public class Robot extends TimedRobot {

    Command autonomousCommand;
    SendableChooser<String> chooser;

	public static DriveBase driveBase;
	public static ArmLift armLift; 
	public static Pneumatics pneumatics;
	public static FieldCamera fieldCamera;
	public static IMU imu;
	public static OI oi;
	
	public MatchData.OwnedSide ourSwitch;
	public MatchData.OwnedSide ourScale;
	public MatchData.OwnedSide theirSwitch;
	public boolean haveValidFieldData = false;
    	
    /**
     * This method is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
     
		// Start the FieldCamera
		fieldCamera = new FieldCamera();
    	
    	// Initialize all the robot subsystems
		driveBase = DriveBase.getInstance();
		armLift = ArmLift.getInstance();
		pneumatics = Pneumatics.getInstance();
		imu = IMU.getInstance();
		
		// Zero the gyro here
		imu.zeroAngle();
		
		oi = OI.getInstance();
		oi.BindCommands();

		// Init Match Data
		haveValidFieldData = false;
		ourSwitch = MatchData.OwnedSide.UNKNOWN;
		ourScale = MatchData.OwnedSide.UNKNOWN;
		theirSwitch = MatchData.OwnedSide.UNKNOWN;

		// Put an Autonomous mode chooser on the Smart Dashboard
 		chooser = new SendableChooser<String>();

		chooser.addDefault("Drive Forward (Default)", "DriveForward");
        chooser.addObject("Middle Switch", "MiddleSwitch");
        chooser.addObject("Left Switch from Front", "LeftSwitchFront");
        chooser.addObject("Left Switch from Side", "LeftSwitchSide");
        chooser.addObject("Right Switch from Front", "RightSwitchFront");
        chooser.addObject("Right Switch from Side", "RightSwitchSide");
        chooser.addObject("Do Nothing", "AutoDoNothing");
        SmartDashboard.putData("Autonomous Mode", chooser);
        
    }

    
	/**
     * This method is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 *
	 * Note the following order of control states during a match:
	 * 
	 * Disable - state prior to match start
	 * Autonomous Enable - Autonomous period
	 * Disable - end of Autonomous period
	 * Disable - end of Autonomous period, prior to start of Teleop period
	 * Teleop Enable - Teleop period
	 * Disable - end of Teleop/match end
	 * 
	 * Because of this, the Disabled state may be entered more than once per match,
	 * and so you should be careful about what happens in disabledInit(). 
     */
    public void disabledInit(){

    }
	
    /**
     * This method is called periodically while the robot is disabled
     */
	public void disabledPeriodic() {
		
		// Let the Scheduler do its business, even when disabled
		Scheduler.getInstance().run();
	}

	/**
     * This method is called once when the robot enters Autonomous mode.
     * You can also use it to set/reset any subsystem information you want before autonomous starts.
     */
    public void autonomousInit() {
    }
    
   /**
    * This method is called periodically during autonomous
    * Here it is used to start the Auto command that was selected using the SmartDashboard. 
    */
   public void autonomousPeriodic() {
       
 
     	// Look for match data for up to 5 seconds
	   double matchTime;
    	if (!haveValidFieldData && (matchTime = DriverStation.getInstance().getMatchTime()) > 10.0)
    	{
    		
        	// Get the 2018 Match Data to display on the Dashboard
        	ourSwitch = MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_NEAR);
        	ourScale = MatchData.getOwnedSide(MatchData.GameFeature.SCALE);
        	theirSwitch = MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_FAR);
        	
        	String md = "MatchTime: " + Double.toString(matchTime) +
        				" SW:" + OS2String(ourSwitch) +
        				" SC:" + OS2String(ourScale) +
        				" OP:" + OS2String(theirSwitch);
        	SmartDashboard.putString("Match Data ", md);
        	
        	if (ourSwitch != MatchData.OwnedSide.UNKNOWN)
        	{
        		haveValidFieldData = true;
        		// Choose auto commands based on SmartDashbord Chooser control
        		autonomousCommand = processChooser();
       	        // schedule the autonomous command
                if (autonomousCommand != null) 	autonomousCommand.start();
            
        	}
        	else
        	{
        		// Still no valid field data; return to try again
        		return;
        	}
    	}

    	// Once we get here, autonomous command (if any) will have been started, so tell Scheduler to run it
    	Scheduler.getInstance().run();

   }    	

   /*
    * Turn the String specified by the Chooser into an actual Command (or CommandGroup)
    */
   private Command processChooser() {
    		
	   Command autoCommand = null;
	   
	   String chooserCommand = chooser.getSelected();

	   if((chooserCommand == null) || (chooserCommand.equals("AutoDoNothing")))
       {
    	   autoCommand = null;
       }
       else 

        {
        	switch(chooserCommand)
        	{
        	case "DriveForward":
                autoCommand = new AutoDriveForward();
                break;

        	case "MiddleSwitch":
            	if (ourSwitch == MatchData.OwnedSide.LEFT) {
            		autoCommand = new AutoLeftSwitch_Middle();
            	}
            	else if (ourSwitch == MatchData.OwnedSide.RIGHT) {
            		autoCommand = new AutoRightSwitch_Middle();
                }
            	else
            		// Should never have to do this
            		autoCommand = new AutoDriveForwardAngle();
        		break;

        	case "LeftSwitchFront":
 
            	if (ourSwitch == MatchData.OwnedSide.LEFT) {
            		autoCommand = new AutoLeftSwitch_Front();
            	}
            	else
            		autoCommand = new AutoDriveForward();
            	break;
            	
        	case "LeftSwitchSide":

        		if (ourSwitch == MatchData.OwnedSide.LEFT) {
            		autoCommand = new AutoLeftSwitch_Side();
            	}
            	else
            		autoCommand = new AutoDriveForward();
        		break;
            		
        	case "RightSwitchFront":
            	if (ourSwitch == MatchData.OwnedSide.RIGHT) {
            		autoCommand = new AutoRightSwitch_Front();
            	}
            	else
            		autoCommand = new AutoDriveForward();
            	break;
            	
        	case "RightSwitchSide":
            	if (ourSwitch == MatchData.OwnedSide.RIGHT) {
            		autoCommand = new AutoRightSwitch_Side();
            	}
            	else
            		autoCommand = new AutoDriveForward();
        		break;
        	}		
        }
       
        SmartDashboard.putString("Chosen Auto", chooserCommand);
       	return autoCommand;
    }


    private String OS2String(MatchData.OwnedSide os) {
    	String retStr = "";
    	switch (os) {
	    	case LEFT:   retStr = "LEFT";   break;
	    	case RIGHT:   retStr = "RIGHT";   break;
	    	case UNKNOWN:   retStr = "UNKN";   break;
    	}
    	return retStr;
    }

    /**
     * This method is called once when the robot enters Teleoperated mode.
     * Here it is used to stop the Auto command, which will otherwise keep running until it ends.  
     * You can also use it to set/reset any subsystem information you want before teleop starts.
     */
    public void teleopInit() {
		
    	// This makes sure that the autonomous stops running when teleop starts running.
    	// If you want the autonomous to continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();
    }

    /**
     * This method is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
    }
    
    /**
     * This method is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.updateValues();
    }
}
