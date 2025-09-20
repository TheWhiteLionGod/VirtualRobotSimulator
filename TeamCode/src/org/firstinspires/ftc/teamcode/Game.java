package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.auto.SampleAuto;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@TeleOp(name="Game", group="FTC2025")
public class Game extends LinearOpMode {
    SampleMecanumDrive drive;
    Robot auto;
    Dynabytes2025 teleop;

    @Override
    public void runOpMode() {
        drive = new SampleMecanumDrive(hardwareMap);
        auto = new SampleAuto();
        teleop = new Dynabytes2025();

        waitForStart();

        // Starting Autonomous
        auto.configure();

        auto.drive = drive;
        auto.game = this;

        auto.run();
        System.out.println("HELLO");

        // Starting TeleOp
        teleop.drive = drive;
        teleop.runOpMode();
    }
}
