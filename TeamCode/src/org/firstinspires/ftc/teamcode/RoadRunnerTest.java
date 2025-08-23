package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

import java.util.ArrayList;

@TeleOp(name = "RoadRunnerTest", group = "FTC2025")
public class RoadRunnerTest extends LinearOpMode {
    SampleMecanumDrive drive;
    ArrayList<TrajectorySequence> trajectories;
    public void configure() {
        drive = new SampleMecanumDrive(hardwareMap);
        Pose2d start_pos = new Pose2d(0, 0,0);
        drive.setPoseEstimate(start_pos);
        trajectories = new ArrayList<>();

        TrajectorySequence traj = drive.trajectorySequenceBuilder(start_pos)
                .splineToConstantHeading(new Vector2d(12, 0), 0)
                .turn(Math.toRadians(45))
                .splineToConstantHeading(new Vector2d(-5, -5), 0)
                .build();

        trajectories.add(traj);
    }

    @Override
    public void runOpMode() {
        configure();
        waitForStart();

        if (opModeIsActive()) {
            for (TrajectorySequence test_traj : trajectories) {
                drive.followTrajectorySequence(test_traj);
                System.out.println(test_traj.end());
                System.out.println(drive.getPoseEstimate());
            }
        }
    }
}