package org.firstinspires.ftc.dynabytes.auto;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.dynabytes.PositionStorage;
import org.firstinspires.ftc.dynabytes.Robot;
import org.firstinspires.ftc.dynabytes.TrajectoryStorage;
import org.firstinspires.ftc.roadrunner.drive.SampleMecanumDrive;

@Disabled
@TeleOp(name = "SampleAuto", group = "FTC2025")
public class SampleAuto extends Robot {
    @Override
    public void configure() {
        // Creating Drivetrain
        drive = new SampleMecanumDrive(hardwareMap);
        drive.setPoseEstimate(PositionStorage.start_pos);

        TrajectoryStorage.buildTrajectories(drive); // Builds Trajectory Here
    }

    public void run() {
        // Running Trajectory
        moveRobot(TrajectoryStorage.sample_traj);
    }
}