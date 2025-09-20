package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Disabled
@TeleOp(name = "Robot", group = "FTC2025")
public abstract class Robot extends LinearOpMode {
    public SampleMecanumDrive drive;
    public LinearOpMode game = null;

    @Override
    public void runOpMode() {
        configure();
        waitForStart();
        run();
    }

    public boolean canRun() {
        if (game == null) {
            return false;
        }
        return game.opModeIsActive();
    }

    abstract public void configure();
    abstract public void run();
}