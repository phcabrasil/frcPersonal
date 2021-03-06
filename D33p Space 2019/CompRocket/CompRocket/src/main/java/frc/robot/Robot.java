/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.subsystems.driveBase;
import frc.robot.subsystems.elevatorBase;
import frc.robot.subsystems.intakeBase;
import frc.robot.subsystems.limeLight;
import frc.robot.subsystems.pneumatics;
import frc.robot.subsystems.wristBase;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.*;

public class Robot extends TimedRobot {

	//Create Objects
	public static OI oi;
	public static driveBase drive;
	public static limeLight camera;
	public static elevatorBase elevator;
	public static pneumatics solenoid;
	public static wristBase wrist;
	public static intakeBase intake;
	public static ShuffleboardTab testTab;
	public static ShuffleboardTab teleopTab;
	public static Timer RobotTimer;
	public static CameraServer driverMemeCamera;
	
	//Create Commands
	Command autonomousCommand;
	Command arcadeDrive;
	Command teleopSolenoid;
	Command dashboardCommand;

	/*@SuppressWarnings("rawtypes")
	SendableChooser autoSelector;*/

	public enum RobotState {
        DISABLED, AUTONOMOUS, TELEOP
    }
	public static double Time;
    public static RobotState s_robot_state = RobotState.DISABLED;

    public static RobotState getState() {
        return s_robot_state;
    }
	public static double getTime(){
		return Time;
	}
    public static void setState(RobotState state) {
        s_robot_state = state;
    }

	@Override
	public void robotInit() {

		//create tabs
		testTab = Shuffleboard.getTab("Test Tab");
		teleopTab = Shuffleboard.getTab("Teleop Tab");

		//Define Camera
		driverMemeCamera = CameraServer.getInstance();
		driverMemeCamera.startAutomaticCapture();
		
		//Define Objects
		oi = new OI();
		drive = new driveBase();
		solenoid = new pneumatics();
		camera = new limeLight();
		elevator = new elevatorBase();
		wrist = new wristBase();
		intake = new intakeBase();
		RobotTimer = new Timer();

			
		Robot.elevator.resetEnc();
		Robot.wrist.resetEnc();
		  
		//SmartDashboard Subsystem 
		SmartDashboard.putData(drive);
		SmartDashboard.putData(camera);
		SmartDashboard.putData(solenoid);
	}

	@Override
	public void disabledInit() {
		RobotTimer.reset();
		RobotTimer.start();
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {
		RobotTimer.reset();
		RobotTimer.start();
	}

	@Override
	public void autonomousPeriodic() {
		Time = RobotTimer.get();
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		RobotTimer.reset();
		RobotTimer.start();
	}

	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override  
	public void testPeriodic() {
		Time = RobotTimer.get();
	}
	
	public static void initTalon(TalonSRX motor, boolean invert) {
		motor.setInverted(invert);
		motor.setNeutralMode(NeutralMode.Brake);
		motor.neutralOutput();
		motor.configNominalOutputForward(0.0, 0);
		motor.configNominalOutputReverse(0.0, 0);
		motor.configClosedloopRamp(0.5, 0);
		motor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
	}
	public static void initTalonLimitSwitch(TalonSRX motor){

	}
	public static void initVictor(VictorSPX motor, boolean invert) {
		motor.setInverted(invert);
		motor.setNeutralMode(NeutralMode.Brake);
		motor.neutralOutput();
		motor.configNominalOutputForward(0.0, 0);
		motor.configNominalOutputReverse(0.0, 0);
		motor.configClosedloopRamp(0.5, 0);
	}

	public static void initMasterDriveMotor(TalonSRX motor){
		motor.setSensorPhase(true);
		//Brake Mode
		motor.setNeutralMode(NeutralMode.Brake);
		//Output Settings
		motor.configNominalOutputForward(0, Constants.kTimeoutMs);
		motor.configNominalOutputReverse(0, Constants.kTimeoutMs);
		motor.configPeakOutputForward(1, Constants.kTimeoutMs);
		motor.configPeakOutputReverse(-1, Constants.kTimeoutMs);
		//PID Gain Settings
		motor.selectProfileSlot(Constants.kDriveSlotIdx, Constants.kPIDLoopIdx);
		motor.config_kF(Constants.kDriveSlotIdx, Constants.kDriveGains.kF, Constants.kTimeoutMs);
		motor.config_kP(Constants.kDriveSlotIdx, Constants.kDriveGains.kP, Constants.kTimeoutMs);
		motor.config_kI(Constants.kDriveSlotIdx, Constants.kDriveGains.kI, Constants.kTimeoutMs);
		motor.config_kD(Constants.kDriveSlotIdx, Constants.kDriveGains.kD, Constants.kTimeoutMs);
		//Motion Magic Max Velocity and Acceleration
		motor.configMotionCruiseVelocity(15000, Constants.kTimeoutMs);
		motor.configMotionAcceleration(6000, Constants.kTimeoutMs);
		//Reset Encoder
		motor.setSelectedSensorPosition(0);
	}

	public static void initMasterElevatorMotor(TalonSRX motor){
		//Set Sensor Phase
		motor.setSensorPhase(false);
		//Brake Mode
		motor.setNeutralMode(NeutralMode.Brake);
		//Factory default hardware to prevent unexpected behavior
		motor.configFactoryDefault();
		//Set relevant frame periods to be at least as fast as periodic rate
		motor.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, Constants.kTimeoutMs);
		motor.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.kTimeoutMs);
		//Output Settings
		motor.configNominalOutputForward(0, Constants.kTimeoutMs);
		motor.configNominalOutputReverse(0, Constants.kTimeoutMs);
		motor.configPeakOutputForward(1, Constants.kTimeoutMs);
		motor.configPeakOutputReverse(-1, Constants.kTimeoutMs);
		//PID Gain Settings
		motor.selectProfileSlot(Constants.kElevatorSlotIdx, Constants.kPIDLoopIdx);
		motor.config_kF(Constants.kElevatorSlotIdx, Constants.kElevatorGains.kF, Constants.kTimeoutMs);
		motor.config_kP(Constants.kElevatorSlotIdx, Constants.kElevatorGains.kP, Constants.kTimeoutMs);
		motor.config_kI(Constants.kElevatorSlotIdx, Constants.kElevatorGains.kI, Constants.kTimeoutMs);
		motor.config_kD(Constants.kElevatorSlotIdx, Constants.kElevatorGains.kD, Constants.kTimeoutMs);
		//Motion Magic Max Velocity and Acceleration
		motor.configMotionCruiseVelocity(15000, Constants.kTimeoutMs);
		motor.configMotionAcceleration(6000, Constants.kTimeoutMs);
		//Reset Encoder
		motor.setSelectedSensorPosition(0);
	}
	public static void initMasterWristMotor(TalonSRX motor){
		//Set Sensor Phase
		motor.setSensorPhase(false);
		//Brake Mode
		motor.setNeutralMode(NeutralMode
		.Brake);
		//Factory default hardware to prevent unexpected behavior
		motor.configFactoryDefault();
		//Set relevant frame periods to be at least as fast as periodic rate
		motor.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, Constants.kTimeoutMs);
		motor.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.kTimeoutMs);
		//Output Settings
		motor.configNominalOutputForward(0, Constants.kTimeoutMs);
		motor.configNominalOutputReverse(0, Constants.kTimeoutMs);
		motor.configPeakOutputForward(0.75, Constants.kTimeoutMs);
		motor.configPeakOutputReverse(-0.75, Constants.kTimeoutMs);
		//PID Gain Settings
		motor.selectProfileSlot(Constants.kElevatorSlotIdx, Constants.kPIDLoopIdx);
		motor.config_kF(Constants.kWristSlotIdx, Constants.kWristGains.kF, Constants.kTimeoutMs);
		motor.config_kP(Constants.kWristSlotIdx, Constants.kWristGains.kP, Constants.kTimeoutMs);
		motor.config_kI(Constants.kWristSlotIdx, Constants.kWristGains.kI, Constants.kTimeoutMs);
		motor.config_kD(Constants.kWristSlotIdx, Constants.kWristGains.kD, Constants.kTimeoutMs);
		//Motion Magic Max Velocity and Acceleration
		motor.configMotionCruiseVelocity(15000, Constants.kTimeoutMs);
		motor.configMotionAcceleration(6000, Constants.kTimeoutMs);
		//Reset Encoder
		motor.setSelectedSensorPosition(0);
	}
}


