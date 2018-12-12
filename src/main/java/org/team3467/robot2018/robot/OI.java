package org.team3467.robot2018.robot;

import org.team3467.robot2018.robot.autonomous.AutoRightSwitch_Front;
import org.team3467.robot2018.robot.control.ButtonBox;
import org.team3467.robot2018.robot.control.ButtonBoxButton;
import org.team3467.robot2018.robot.control.XboxController;
import org.team3467.robot2018.robot.control.XboxControllerButton;
import org.team3467.robot2018.robot.control.triggers.DPadDown;
import org.team3467.robot2018.robot.control.triggers.DPadUp;
import org.team3467.robot2018.subsystems.ArmLift.ArmLift;
import org.team3467.robot2018.subsystems.ArmLift.ArmLiftCalibrate;
import org.team3467.robot2018.subsystems.ArmLift.ArmLiftTest;
import org.team3467.robot2018.subsystems.ArmLift.ArmLiftTransition;
import org.team3467.robot2018.subsystems.ArmLift.ManualArm;
import org.team3467.robot2018.subsystems.ArmLift.ManualLift;
import org.team3467.robot2018.subsystems.ArmLift.ReportStats;
import org.team3467.robot2018.subsystems.ArmLift.UpdateStats;
import org.team3467.robot2018.subsystems.ArmLift.ZeroEncoders;
import org.team3467.robot2018.subsystems.DriveBase.DriveBot;
import org.team3467.robot2018.subsystems.DriveBase.DriveStraight;
import org.team3467.robot2018.subsystems.DriveBase.DriveTurn;
import org.team3467.robot2018.subsystems.DriveBase.ResetDriveEncoders;
import org.team3467.robot2018.subsystems.IMU.ZeroGyro;
import org.team3467.robot2018.subsystems.Pneumatics.CloseHands;
import org.team3467.robot2018.subsystems.Pneumatics.GearShiftToggle;
import org.team3467.robot2018.subsystems.Pneumatics.OpenHands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class OI {

	// Driving with XBox Controller is preferred, but a Logitech GamePad will also work in a pinch, using the same code
	XboxController driveController;
	XboxController operatorController;
	
	// Custom Button Box controls ArmLift positions
	ButtonBox bBox;
	
	// Static subsystem reference
	private static OI oiInstance = new OI();

	public static OI getInstance() {
		return OI.oiInstance;
	}
	
	// OI class constructor
	protected OI() {

		 driveController = new XboxController(0);
		 operatorController = new XboxController(1);
		 bBox = new ButtonBox(2);
	}
	
	public double getControllerLeftX() {
		return driveController.getX(XboxController.Hand.kLeft);
	}
	
	public double getControllerLeftY() {
		return driveController.getY(XboxController.Hand.kLeft);
	}
	
	public double getControllerRightX() {
		return driveController.getX(XboxController.Hand.kRight);
	}
	
	public double getControllerRightY() {
		return driveController.getY(XboxController.Hand.kRight);
	}
	
	public double getControllerLeftTrigger() {
		return driveController.getTriggerAxis(XboxController.Hand.kLeft);		
	}
	
	public double getControllerRightTrigger() {
		return driveController.getTriggerAxis(XboxController.Hand.kRight);		
	}
	
	public double getOperatorLeftX() {
		return operatorController.getX(XboxController.Hand.kLeft);
	}
	
	public double getOperatorLeftY() {
		return operatorController.getY(XboxController.Hand.kLeft);
	}
	
	public double getOperatorRightX() {
		return operatorController.getX(XboxController.Hand.kRight);
	}
	
	public double getOperatorRightY() {
		return operatorController.getY(XboxController.Hand.kRight);
	}
	
	public double getOperatorLeftTrigger() {
		return operatorController.getTriggerAxis(XboxController.Hand.kLeft);		
	}
	
	public double getOperatorRightTrigger() {
		return operatorController.getTriggerAxis(XboxController.Hand.kRight);		
	}
	
	public void BindCommands() {
		
		/*
		 *
		 * Drive Controller
		 * 
		 */
		// Bind any Drive Controller buttons here
		
		// DPad will change drive control mode
		new DPadUp(driveController).whenActive(new DriveBot(DriveBot.driveMode_Tank, false));
		new DPadDown(driveController).whenActive(new DriveBot(DriveBot.driveMode_Rocket, false));
		
		// The "A" Button will shift any drive mode to Precision mode
		new XboxControllerButton(driveController, XboxController.Button.kA)
			.whenActive(new DriveBot(DriveBot.driveMode_Rocket, true));

		// The "B" Button will temporarily change the drive speed
		new XboxControllerButton(driveController, XboxController.Button.kB).whenActive(new GearShiftToggle());
		new XboxControllerButton(driveController, XboxController.Button.kB).whenInactive(new GearShiftToggle());

		// The "Y" Button will toggle the drive speed
		new XboxControllerButton(driveController, XboxController.Button.kY).whenActive(new GearShiftToggle());

		// The "X" button activates "turn in place" while held down
		new XboxControllerButton(driveController, XboxController.Button.kX)
			.whileActive(new DriveBot(DriveBot.driveMode_RocketSpin, false));


		/*
		 * 
		 * Operator Controller
		 * 
		 */
		// Bind any Operator Controller buttons here
		
		// Pressing stick buttons puts the ArmLift into Manual mode
		new XboxControllerButton(operatorController, XboxController.Button.kStickRight).whenActive(new ManualArm());
		new XboxControllerButton(operatorController, XboxController.Button.kStickLeft).whenActive(new ManualLift());
		
		// Open Hands
		new XboxControllerButton(operatorController, XboxController.Button.kBumperLeft).whenPressed(new OpenHands());
		new XboxControllerButton(operatorController, XboxController.Button.kBumperLeft).whenReleased(new CloseHands());


		/*
		 * 
		 * Button Box Controller
		 * 
		 */
		// Bind any Button Box buttons here

		new ButtonBoxButton(bBox, ButtonBox.Button.k3).whenPressed(new ArmLiftTransition(ArmLift.eArmLiftState.OnFloor));
		new ButtonBoxButton(bBox, ButtonBox.Button.k4).whenPressed(new ArmLiftTransition(ArmLift.eArmLiftState.OnStep));
		new ButtonBoxButton(bBox, ButtonBox.Button.k2).whenPressed(new ArmLiftTransition(ArmLift.eArmLiftState.Level2Cube));
		new ButtonBoxButton(bBox, ButtonBox.Button.k1).whenPressed(new ArmLiftTransition(ArmLift.eArmLiftState.SwitchFront));
		new ButtonBoxButton(bBox, ButtonBox.Button.k5).whenPressed(new ArmLiftTransition(ArmLift.eArmLiftState.HighLow));
		new ButtonBoxButton(bBox, ButtonBox.Button.k6).whenPressed(new ArmLiftTransition(ArmLift.eArmLiftState.ScaleHigh));
		new ButtonBoxButton(bBox, ButtonBox.Button.k8).whenPressed(new ArmLiftTransition(ArmLift.eArmLiftState.ScaleLow));
		new ButtonBoxButton(bBox, ButtonBox.Button.k9).whenPressed(new ArmLiftTransition(ArmLift.eArmLiftState.SwitchBack));
		new ButtonBoxButton(bBox, ButtonBox.Button.k7).whenPressed(new ArmLiftTransition(ArmLift.eArmLiftState.OnTrunk));
		new ButtonBoxButton(bBox, ButtonBox.Button.k10).whenPressed(new OpenHands());
		new ButtonBoxButton(bBox, ButtonBox.Button.k10).whenReleased(new CloseHands());
		//new ButtonBoxButton(bBox, ButtonBox.Button.k11).whenPressed(new ArmLiftTransition(ArmLift.eArmLiftState.OnFloor));


		// Test Buttons for Smartdash
		SmartDashboard.putData("Zero Drive Encoders", new ResetDriveEncoders());
		SmartDashboard.putData("Drive Turn 45", new DriveTurn(45.0, 0.4));
		SmartDashboard.putData("Drive Turn -45", new DriveTurn(-45.0, 0.4));
		SmartDashboard.putData("Drive Turn 90", new DriveTurn(90.0, 0.4));
		SmartDashboard.putData("Drive Turn -90", new DriveTurn(-90.0, 0.4));
		SmartDashboard.putData("Drive Straight", new DriveStraight(30000));
		SmartDashboard.putData("Do Left Switch", new AutoRightSwitch_Front());
		
		SmartDashboard.putData("Zero Gyro", new ZeroGyro());

		SmartDashboard.putData("Zero ArmLift Encoders", new ZeroEncoders());
		SmartDashboard.putData("Report ArmLift Stats", new ReportStats());
		SmartDashboard.putData("Calibrate ArmLift", new ArmLiftCalibrate());
		SmartDashboard.putData("Test ArmLift using SDB Setpoints", new ArmLiftTest());
		SmartDashboard.putData("Test ArmLift using Coded Setpoints", new ArmLiftTest(-3600, 3600));
		SmartDashboard.putData("Update from Dash", new UpdateStats());
		
		SmartDashboard.putData("Move to Floor", new ArmLiftTransition(ArmLift.eArmLiftState.OnFloor));
		SmartDashboard.putData("Move to Step", new ArmLiftTransition(ArmLift.eArmLiftState.OnStep));
		SmartDashboard.putData("Move to 2nd Lvl Cube", new ArmLiftTransition(ArmLift.eArmLiftState.Level2Cube));
		SmartDashboard.putData("Move to Front Switch", new ArmLiftTransition(ArmLift.eArmLiftState.SwitchFront));
		SmartDashboard.putData("Move to High", new ArmLiftTransition(ArmLift.eArmLiftState.HighLow));
		SmartDashboard.putData("Move to Scale High", new ArmLiftTransition(ArmLift.eArmLiftState.ScaleHigh));
		SmartDashboard.putData("Move to Scale Low", new ArmLiftTransition(ArmLift.eArmLiftState.ScaleLow));
		SmartDashboard.putData("Move to Back Switch", new ArmLiftTransition(ArmLift.eArmLiftState.SwitchBack));
		SmartDashboard.putData("Move to Trunk", new ArmLiftTransition(ArmLift.eArmLiftState.OnTrunk));

	
	}
}

