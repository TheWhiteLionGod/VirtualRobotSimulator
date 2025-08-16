package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@TeleOp(name = "RoadRunnerTest", group = "FTC2025")
public class RoadRunnerTest extends LinearOpMode {
    @Override
    public void runOpMode() {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Pose2d start_pos = new Pose2d(0, 0,0);
        drive.setPoseEstimate(start_pos);

        TrajectorySequence traj = drive.trajectorySequenceBuilder(new Pose2d(0, 0, 0))
            .splineTo(new Vector2d(-24, 0), -90)
            .splineTo(new Vector2d(24, 24), -90)
            .splineTo(new Vector2d(-24, -24), 180)
            .build();

        waitForStart();
        if (opModeIsActive()) {
            drive.followTrajectorySequence(traj);
        }
    }
}