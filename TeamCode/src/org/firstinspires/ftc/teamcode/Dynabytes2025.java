package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@TeleOp(name = "Controller", group = "FTC2025")
public class Dynabytes2025 extends LinearOpMode {
    DcMotor m1, m2, m3, m4;
    IMU imu;
    double yaw_angle;
    double gear_mode = 3.0;
    boolean on_gear_switch_cooldown = false;
    double gear_switch_time;
    double MAX_GEAR = 3.0;

    public void runOpMode(){
        m1 = hardwareMap.dcMotor.get("back_left_motor");
        m2 = hardwareMap.dcMotor.get("front_left_motor");
        m3 = hardwareMap.dcMotor.get("front_right_motor");
        m4 = hardwareMap.dcMotor.get("back_right_motor");

        m1.setDirection(DcMotor.Direction.REVERSE);
        m2.setDirection(DcMotor.Direction.REVERSE);

        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP;
        RevHubOrientationOnRobot.UsbFacingDirection  usbDirection  = RevHubOrientationOnRobot.UsbFacingDirection.RIGHT;
        RevHubOrientationOnRobot orientationOnRobot = new RevHubOrientationOnRobot(logoDirection, usbDirection);

        imu = hardwareMap.get(IMU.class, "imu");
        imu.initialize(new IMU.Parameters(orientationOnRobot));

        imu.resetYaw();
        waitForStart();

        while (opModeIsActive()) {
            // START
            // Updating Yaw of Robot
            YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
            yaw_angle = orientation.getYaw(AngleUnit.DEGREES);
    
            // Switching Gears
            if (gamepad1.dpad_up) {
                changeGearMode(1);
            }
            else if (gamepad1.dpad_down) {
                changeGearMode(-1);
            }

            // Field Drive Movement
            if (gamepad1.left_stick_x != 0 || gamepad1.left_stick_y != 0) {
                fieldDriveMove(gamepad1.left_stick_x, gamepad1.left_stick_y);
            }
            else {
                Reset();
            }

            // Turning
            if (gamepad1.right_stick_x != 0) {
                Turn(gamepad1.right_stick_x);
            }
            //ENDS
        }
        Reset();
    }

    public void changeGearMode(int change_val) {
        if (on_gear_switch_cooldown) {
            if (getRuntime() - gear_switch_time >= 0.25) {
                on_gear_switch_cooldown = false;
            }
        } else {
            gear_mode = Math.min(Math.max(gear_mode + change_val, 1), MAX_GEAR);
            on_gear_switch_cooldown = true;
            gear_switch_time = getRuntime();

            telemetry.addData("Current Gear Mode", gear_mode);
            telemetry.update();
        }
    }

    public void Move(double pwrx, double pwry) {
        double gear_pwr = gear_mode / MAX_GEAR;
        m1.setPower(gear_pwr*(-pwrx-pwry));
        m3.setPower(gear_pwr*(-pwrx-pwry));

        m2.setPower(gear_pwr*(pwrx-pwry));
        m4.setPower(gear_pwr*(pwrx-pwry));
    }

    public void fieldDriveMove(double pwr_x, double pwr_y) {
        double pi = 3.1415926;

        /* Adjust Joystick X/Y inputs by navX MXP yaw angle */
        double yaw_radians = yaw_angle * pi/180;
        double temp = pwr_y * Math.cos(yaw_radians) + pwr_x * Math.sin(yaw_radians);
        pwr_x = -pwr_y * Math.sin(yaw_radians) + pwr_x * Math.cos(yaw_radians);
        pwr_y = temp;

        /* At this point, Joystick X/Y (strafe/forward) vectors have been */
        /* rotated by the gyro angle, and can be sent to drive system */
        Move(pwr_x, pwr_y);
    }

    public void Turn(double pwr) {
        double gear_pwr = gear_mode / MAX_GEAR;
        m1.setPower(gear_pwr*pwr);
        m3.setPower(gear_pwr*-pwr);

        m2.setPower(0);
        m4.setPower(0);
    }

    public void Reset() {
        m1.setPower(0);
        m2.setPower(0);
        m3.setPower(0);
        m4.setPower(0);
    }
}