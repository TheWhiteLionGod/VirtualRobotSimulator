package org.firstinspires.ftc.teamcode.subsystems.drivetrain;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.sensor.ImuSensor;

public class FieldDrive {
    private final DcMotorEx BL, FL, FR, BR;
    private final Telemetry telemetry;

    public FieldDrive(HardwareMap hardwareMap, Telemetry telemetry) {
        BL = hardwareMap.get(DcMotorEx.class, "BL");
        FL = hardwareMap.get(DcMotorEx.class, "FL");
        FR = hardwareMap.get(DcMotorEx.class, "FR");
        BR = hardwareMap.get(DcMotorEx.class, "BR");

        BL.setDirection(DcMotorEx.Direction.REVERSE);
        FL.setDirection(DcMotorEx.Direction.REVERSE);

        BL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.telemetry = telemetry;
    }

    // Auto Commands
    public void forward(double curYaw) {
        fieldDrive(1, 0, 0, curYaw);
    }

    public void backward(double curYaw) {
        fieldDrive(-1, 0, 0, curYaw);
    }

    public void left(double curYaw) {
        fieldDrive(0, -1, 0, curYaw);
    }

    public void right(double curYaw) {
        fieldDrive(0, 1, 0, curYaw);
    }


    // Field Drive Movement
    public void fieldDrive(double forward, double strafe, double turn, double curYaw) {
        /* Adjust Joystick X/Y inputs by navX MXP yaw angle */
        double rotX = strafe * Math.cos(-curYaw) - forward * Math.sin(-curYaw);
        double rotY = strafe * Math.sin(-curYaw) + forward * Math.cos(-curYaw);
        robotDrive(rotY, rotX, turn);
    }

    // Regular Movement
    public void robotDrive(double forward, double strafe, double turn) {
        double frontLeft = forward + strafe + turn;
        double backLeft = forward - strafe + turn;
        double frontRight = forward - strafe - turn;
        double backRight = forward + strafe - turn;

        double max = Math.max(Math.abs(frontLeft), Math.max(Math.abs(backLeft),
                Math.max(Math.abs(frontRight), Math.abs(backRight))));

        if (max > 1.0) {
            frontLeft /= max;
            backLeft /= max;
            frontRight /= max;
            backRight /= max;
        }

        BL.setPower(backLeft);
        FL.setPower(frontLeft);
        FR.setPower(frontRight);
        BR.setPower(backRight);
        telemetry.addLine("Drivetrain Moving");
    }

    public void turnTo(ImuSensor imu, double targetYaw) {
        double err = imu.getYawDegrees() - targetYaw;

        while (Math.abs(err) > 1) {
            System.out.println("Error: " + err);
            err = imu.getYawDegrees() - targetYaw;
            robotDrive(0, 0, err * 0.01);
        }
    }

    public void stop() {
        BL.setPower(0);
        FL.setPower(0);
        FR.setPower(0);
        BR.setPower(0);
        telemetry.addLine("Stopped Drivetrain");
    }
}
