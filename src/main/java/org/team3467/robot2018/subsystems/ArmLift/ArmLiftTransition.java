
package org.team3467.robot2018.subsystems.ArmLift;

import org.team3467.robot2018.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class ArmLiftTransition extends Command {

	static final int				TIMEOUT_SECS = 2;	// Max time to wait for each target to be reached
	
	private ArmLift.eArmLiftState	m_targetState;	// Final state we hope to achieve

    private int						m_index = 0;
	private int						m_numStates = 0;
	private int						m_leftTarget = 0;
	private int						m_rightTarget = 0;
	private ArmLift.eArmLiftState[] m_states;
	
	private double					_timeOut;
	private double					_startTime;
	
    static int[][] stateCoordinates = {
    		
    		{-350, -350},	// OnFloor(0),  	// Power On Position is (0.0); 
    		{-5600, 7000},	// OnStep(1),		// Stow / Start Position: Less prone to gravity!
    		{-3450, 2900},	// Level2Cube(2),
    		{-11200, 10600},	// SwitchFront(3),
    		{-10550, 12100},// HighLow(4),	// Lift High, Arm Low - standard intermediate position
    		{-6500, 16400},	// ScaleHigh(5),
    		{-6100, 14000},	// ScaleLow(6),
    		{-1950, 8900},	// SwitchBack(7),
    		{-5500, 8800},	// OnTrunk(8),
    		{100, 100}		// Unknown(99); // When in this state, go to HiLo before doing anything else
    };
	
	public ArmLiftTransition(ArmLift.eArmLiftState als) {
        requires(Robot.armLift);
        this.setInterruptible(false);
        
        m_targetState = als;
    }

    // Called just before this Command runs the first time
    protected void initialize() {

    	m_index = 0;
    	
    	// get current state from ArmLift subsystem
    	ArmLift.eArmLiftState currentState = Robot.armLift.getState();
 
       	SmartDashboard.putString("ArmLift State", currentState.getName());
 
    	// Get our "states to run" array
    	m_states = transAct[currentState.getVal()][m_targetState.getVal()];
    	m_numStates = m_states.length;
    	
    	// Determine next state to transition to
    	if (m_numStates == 0) {
            // Should never get here, but if we do...
            m_targetState = ArmLift.eArmLiftState.HighLow;
    	}
    	else
    	{
        	m_targetState = m_states[m_index];    		
    	}
    	
    	SmartDashboard.putString("ArmLift Target State", m_targetState.getName());
     	
		m_leftTarget = stateCoordinates[m_targetState.getVal()][0];
		m_rightTarget = stateCoordinates[m_targetState.getVal()][1];
		
		// Setup timer for timeout
		_setTimeOut(TIMEOUT_SECS);
		_startTimer();
 
   	}

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

    	Robot.armLift.reportEncoders();
    	Robot.armLift.reportTalonStats();
    	
    	// Set setpoint for each Talon and run using MotionMagic
    	Robot.armLift.moveMagically(m_leftTarget, m_rightTarget);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {

        // Is this the last state in the list?
        if ((m_numStates - 1) > m_index) {
    		
    		// This is not the last state, so we can just get "close enough"
    		if (Robot.armLift.onTarget(true) || _isTimedOut()) {
        	
            	Robot.armLift.setState(m_targetState);
            	SmartDashboard.putString("ArmLift State", Robot.armLift.getState().getName());

            	// Get next target state
    			m_index++;
            	m_targetState = m_states[m_index];    		
	        	SmartDashboard.putString("ArmLift Target State", m_targetState.getName());
	         	
	    		m_leftTarget = stateCoordinates[m_targetState.getVal()][0];
	    		m_rightTarget = stateCoordinates[m_targetState.getVal()][1];
	    		
	    		// Reset Timer
    			_startTimer();
	    		
    		}
   			// Always return false, since we have additional states to run
    		return false;
    	
    	}
    	else
    	{
    		// This IS the last state, so we have to get closer to target
    		if (Robot.armLift.onTarget(false) || _isTimedOut()) {
    			// We're done
	    		return true;
    		}
    		else
    			return false;
    		
    	}
    }

    // Called once after isFinished returns true
    protected void end() {

    	Robot.armLift.setState(m_targetState);

    	SmartDashboard.putString("ArmLift State", Robot.armLift.getState().getName());
    	SmartDashboard.putString("ArmLift Target State", "Done");

    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {

    	// Not sure where we are, but just end normally and hope to do better next time around...
    	end();
    }

    private synchronized void _setTimeOut(double tOut) {
    	_timeOut = tOut;
    }
    
    private synchronized boolean _isTimedOut() {
        return (Timer.getFPGATimestamp() - _startTime) >= _timeOut;
    }
    
    private synchronized void _startTimer() {
    	_startTime = Timer.getFPGATimestamp();
    }
    
    static ArmLift.eArmLiftState[][][] transAct = {

// Current States
/* OnFloor(0) */ {
     // Target States
     /* OnFloor(0) */{ ArmLift.eArmLiftState.OnFloor},
     /* OnStep(1) */{ ArmLift.eArmLiftState.OnStep },
     /* Level2Cube(2) */{ ArmLift.eArmLiftState.Level2Cube },
     /* SwitchFront(3) */{ ArmLift.eArmLiftState.SwitchFront },
     /* HighLow(4) */{ ArmLift.eArmLiftState.HighLow },
     /* ScaleHigh(5) */{ ArmLift.eArmLiftState.OnFloor}, //ArmLift.eArmLiftState.HighLow, ArmLift.eArmLiftState.ScaleHigh },
     /* ScaleLow(6) */{ ArmLift.eArmLiftState.OnFloor}, //ArmLift.eArmLiftState.HighLow, ArmLift.eArmLiftState.ScaleLow },
     /* SwitchBack(7) */{ ArmLift.eArmLiftState.OnFloor}, //ArmLift.eArmLiftState.HighLow, ArmLift.eArmLiftState.ScaleLow, ArmLift.eArmLiftState.SwitchBack },
     /* OnTrunk(8) */{ ArmLift.eArmLiftState.OnFloor}, //ArmLift.eArmLiftState.HighLow, ArmLift.eArmLiftState.ScaleLow, ArmLift.eArmLiftState.OnTrunk },
     /* Unknown(9) */{ ArmLift.eArmLiftState.HighLow }
        },
/* OnStep(1) */{
     // Target States
    /* OnFloor(0) */{ ArmLift.eArmLiftState.OnStep}, //ArmLift.eArmLiftState.SwitchFront, ArmLift.eArmLiftState.OnFloor },
    /* OnStep(1) */{ ArmLift.eArmLiftState.OnStep},
    /* Level2Cube(2) */{ ArmLift.eArmLiftState.OnStep}, //ArmLift.eArmLiftState.SwitchFront, ArmLift.eArmLiftState.Level2Cube },
    /* SwitchFront(3) */{ ArmLift.eArmLiftState.SwitchFront },
    /* HighLow(4) */{ ArmLift.eArmLiftState.HighLow },
    /* ScaleHigh(5) */{ ArmLift.eArmLiftState.OnStep}, //ArmLift.eArmLiftState.HighLow, ArmLift.eArmLiftState.ScaleHigh },
    /* ScaleLow(6) */{ ArmLift.eArmLiftState.OnStep}, //ArmLift.eArmLiftState.HighLow, ArmLift.eArmLiftState.ScaleLow },
    /* SwitchBack(7) */{ ArmLift.eArmLiftState.OnStep}, //ArmLift.eArmLiftState.HighLow, ArmLift.eArmLiftState.ScaleLow, ArmLift.eArmLiftState.SwitchBack },
    /* OnTrunk(8) */{ ArmLift.eArmLiftState.OnStep}, //ArmLift.eArmLiftState.HighLow, ArmLift.eArmLiftState.ScaleLow, ArmLift.eArmLiftState.OnTrunk },
    /* Unknown(9) */{ ArmLift.eArmLiftState.HighLow }
        },
/* Level2Cube(2) */ {
     // Target States
    /* OnFloor(0) */{ ArmLift.eArmLiftState.OnFloor },
    /* OnStep(1) */{ ArmLift.eArmLiftState.OnStep },
    /* Level2Cube(2) */{ ArmLift.eArmLiftState.Level2Cube},
    /* SwitchFront(3) */{ ArmLift.eArmLiftState.SwitchFront },
    /* HighLow(4) */{ ArmLift.eArmLiftState.HighLow },
    /* ScaleHigh(5) */{ ArmLift.eArmLiftState.Level2Cube}, //ArmLift.eArmLiftState.HighLow, ArmLift.eArmLiftState.ScaleHigh },
    /* ScaleLow(6) */{ ArmLift.eArmLiftState.Level2Cube}, //ArmLift.eArmLiftState.HighLow, ArmLift.eArmLiftState.ScaleLow },
    /* SwitchBack(7) */{ ArmLift.eArmLiftState.Level2Cube}, //ArmLift.eArmLiftState.HighLow, ArmLift.eArmLiftState.ScaleLow, ArmLift.eArmLiftState.SwitchBack },
    /* OnTrunk(8) */{ ArmLift.eArmLiftState.Level2Cube}, //ArmLift.eArmLiftState.HighLow, ArmLift.eArmLiftState.ScaleLow, ArmLift.eArmLiftState.OnTrunk },
    /* Unknown(9) */{ ArmLift.eArmLiftState.HighLow }
        },
/* SwitchFront(3) */ {
     // Target States
    /* OnFloor(0) */{ ArmLift.eArmLiftState.OnFloor },
    /* OnStep(1) */{ ArmLift.eArmLiftState.OnStep },
    /* Level2Cube(2) */{ ArmLift.eArmLiftState.Level2Cube },
    /* SwitchFront(3) */{ ArmLift.eArmLiftState.SwitchFront},
    /* HighLow(4) */{ ArmLift.eArmLiftState.HighLow },
    /* ScaleHigh(5) */{ ArmLift.eArmLiftState.SwitchFront}, //ArmLift.eArmLiftState.HighLow, ArmLift.eArmLiftState.ScaleHigh },
    /* ScaleLow(6) */{ ArmLift.eArmLiftState.SwitchFront}, //ArmLift.eArmLiftState.HighLow, ArmLift.eArmLiftState.ScaleLow },
    /* SwitchBack(7) */{ ArmLift.eArmLiftState.SwitchFront}, //ArmLift.eArmLiftState.HighLow, ArmLift.eArmLiftState.ScaleLow, ArmLift.eArmLiftState.SwitchBack },
    /* OnTrunk(8) */{ ArmLift.eArmLiftState.SwitchFront}, //ArmLift.eArmLiftState.HighLow, ArmLift.eArmLiftState.ScaleLow, ArmLift.eArmLiftState.OnTrunk },
    /* Unknown(9) */{ ArmLift.eArmLiftState.HighLow }
        },
/* HighLow(4) */ {
    // Target States
   /* OnFloor(0) */{ ArmLift.eArmLiftState.HighLow}, //ArmLift.eArmLiftState.SwitchFront, ArmLift.eArmLiftState.OnFloor },
   /* OnStep(1) */{ ArmLift.eArmLiftState.HighLow}, //ArmLift.eArmLiftState.OnStep },
   /* Level2Cube(2) */{ ArmLift.eArmLiftState.HighLow}, //ArmLift.eArmLiftState.SwitchFront, ArmLift.eArmLiftState.Level2Cube },
   /* SwitchFront(3) */{ ArmLift.eArmLiftState.SwitchFront },
   /* HighLow(4) */{ ArmLift.eArmLiftState.HighLow},
   /* ScaleHigh(5) */{ ArmLift.eArmLiftState.ScaleHigh },
   /* ScaleLow(6) */{ ArmLift.eArmLiftState.ScaleLow },
   /* SwitchBack(7) */{  ArmLift.eArmLiftState.ScaleLow, ArmLift.eArmLiftState.SwitchBack },
   /* OnTrunk(8) */{ ArmLift.eArmLiftState.ScaleLow, ArmLift.eArmLiftState.OnTrunk },
   /* Unknown(9) */{ ArmLift.eArmLiftState.HighLow }
       },
/* ScaleHigh(5) */ {
    // Target States
   /* OnFloor(0) */{ ArmLift.eArmLiftState.ScaleHigh}, //ArmLift.eArmLiftState.HighLow /*, ArmLift.eArmLiftState.SwitchFront, ArmLift.eArmLiftState.OnFloor */ },
   /* OnStep(1) */{ ArmLift.eArmLiftState.ScaleHigh}, //ArmLift.eArmLiftState.HighLow /*, ArmLift.eArmLiftState.OnStep */},
   /* Level2Cube(2) */{ ArmLift.eArmLiftState.ScaleHigh}, //ArmLift.eArmLiftState.HighLow,/* ArmLift.eArmLiftState.SwitchFront, ArmLift.eArmLiftState.Level2Cube*/ },
   /* SwitchFront(3) */{ ArmLift.eArmLiftState.ScaleHigh}, //ArmLift.eArmLiftState.HighLow,/* ArmLift.eArmLiftState.SwitchFront */},
   /* HighLow(4) */{ ArmLift.eArmLiftState.HighLow },
   /* ScaleHigh(5) */{ ArmLift.eArmLiftState.ScaleHigh},
   /* ScaleLow(6) */{ ArmLift.eArmLiftState.ScaleLow },
   /* SwitchBack(7) */{ ArmLift.eArmLiftState.SwitchBack },
   /* OnTrunk(8) */{ ArmLift.eArmLiftState.OnTrunk },
   /* Unknown(9) */{ ArmLift.eArmLiftState.HighLow }
       },
/* ScaleLow(6) */ {
    // Target States
   /* OnFloor(0) */{ ArmLift.eArmLiftState.ScaleLow}, //ArmLift.eArmLiftState.HighLow /*, ArmLift.eArmLiftState.SwitchFront, ArmLift.eArmLiftState.OnFloor */},
   /* OnStep(1) */{ ArmLift.eArmLiftState.ScaleLow}, //ArmLift.eArmLiftState.HighLow, /* ArmLift.eArmLiftState.OnStep */},
   /* Level2Cube(2) */{ ArmLift.eArmLiftState.ScaleLow}, //ArmLift.eArmLiftState.HighLow, /* ArmLift.eArmLiftState.SwitchFront, ArmLift.eArmLiftState.Level2Cube */},
   /* SwitchFront(3) */{ ArmLift.eArmLiftState.ScaleLow}, //ArmLift.eArmLiftState.HighLow, /* ArmLift.eArmLiftState.SwitchFront */},
   /* HighLow(4) */{ ArmLift.eArmLiftState.HighLow },
   /* ScaleHigh(5) */{ ArmLift.eArmLiftState.ScaleHigh },
   /* ScaleLow(6) */{ ArmLift.eArmLiftState.ScaleLow},
   /* SwitchBack(7) */{ ArmLift.eArmLiftState.SwitchBack },
   /* OnTrunk(8) */{ ArmLift.eArmLiftState.OnTrunk },
   /* Unknown(9) */{ ArmLift.eArmLiftState.HighLow }
       },
/* SwitchBack(7) */ {
    // Target States
   /* OnFloor(0) */{ ArmLift.eArmLiftState.SwitchBack}, //ArmLift.eArmLiftState.HighLow /*, ArmLift.eArmLiftState.SwitchFront, ArmLift.eArmLiftState.OnFloor */},
   /* OnStep(1) */{ ArmLift.eArmLiftState.SwitchBack}, //ArmLift.eArmLiftState.HighLow /*, ArmLift.eArmLiftState.OnStep */},
   /* Level2Cube(2) */{ ArmLift.eArmLiftState.SwitchBack}, //ArmLift.eArmLiftState.HighLow /*, ArmLift.eArmLiftState.SwitchFront, ArmLift.eArmLiftState.Level2Cube */},
   /* SwitchFront(3) */{ ArmLift.eArmLiftState.SwitchBack}, //ArmLift.eArmLiftState.HighLow /*, ArmLift.eArmLiftState.SwitchFront */},
   /* HighLow(4) */{ ArmLift.eArmLiftState.ScaleLow, ArmLift.eArmLiftState.HighLow },
   /* ScaleHigh(5) */{ ArmLift.eArmLiftState.ScaleHigh },
   /* ScaleLow(6) */{ ArmLift.eArmLiftState.ScaleLow },
   /* SwitchBack(7) */{ ArmLift.eArmLiftState.SwitchBack},
   /* OnTrunk(8) */{ ArmLift.eArmLiftState.OnTrunk },
   /* Unknown(9) */{ ArmLift.eArmLiftState.HighLow }
       },
/* OnTrunk(8) */ {
    // Target States
   /* OnFloor(0) */{ ArmLift.eArmLiftState.OnTrunk}, //ArmLift.eArmLiftState.HighLow /*, ArmLift.eArmLiftState.SwitchFront, ArmLift.eArmLiftState.OnFloor */},
   /* OnStep(1) */{ ArmLift.eArmLiftState.OnTrunk}, //ArmLift.eArmLiftState.HighLow /*,  ArmLift.eArmLiftState.OnStep */},
   /* Level2Cube(2) */{ ArmLift.eArmLiftState.OnTrunk}, //ArmLift.eArmLiftState.HighLow /*,  ArmLift.eArmLiftState.SwitchFront, ArmLift.eArmLiftState.Level2Cube */},
   /* SwitchFront(3) */{ ArmLift.eArmLiftState.OnTrunk}, //ArmLift.eArmLiftState.HighLow /*,  ArmLift.eArmLiftState.SwitchFront */},
   /* HighLow(4) */{ ArmLift.eArmLiftState.ScaleLow, ArmLift.eArmLiftState.HighLow },
   /* ScaleHigh(5) */{ ArmLift.eArmLiftState.ScaleHigh },
   /* ScaleLow(6) */{ ArmLift.eArmLiftState.ScaleLow },
   /* SwitchBack(7) */{ ArmLift.eArmLiftState.SwitchBack },
   /* OnTrunk(8) */{ ArmLift.eArmLiftState.OnTrunk},
   /* Unknown(9) */{ ArmLift.eArmLiftState.HighLow }
       },
/* Unknown(9) */ {
    // Target States
   /* OnFloor(0) */{ ArmLift.eArmLiftState.HighLow },
   /* OnStep(1) */{ ArmLift.eArmLiftState.HighLow },
   /* Level2Cube(2) */{ ArmLift.eArmLiftState.HighLow },
   /* SwitchFront(3) */{ ArmLift.eArmLiftState.HighLow },
   /* HighLow(4) */{ ArmLift.eArmLiftState.HighLow },
   /* ScaleHigh(5) */{ ArmLift.eArmLiftState.HighLow },
   /* ScaleLow(6) */{ ArmLift.eArmLiftState.HighLow },
   /* SwitchBack(7) */{ ArmLift.eArmLiftState.HighLow },
   /* OnTrunk(8) */{ ArmLift.eArmLiftState.HighLow },
   /* Unknown(9) */{ }
       }
    };
	

}
