package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
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
                .splineToLinearHeading(new Pose2d(2*12, 0, Math.toRadians(90)), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(0, -2*12, 0), 0)

                .splineToLinearHeading(new Pose2d(2*12, 0, Math.toRadians(90)), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(0, -2*12, 0), 0)

                .splineToLinearHeading(new Pose2d(2*12, 0, Math.toRadians(90)), Math.toRadians(90))
                .splineToLinearHeading(new Pose2d(0, -2*12, 0), 0)

                .splineToSplineHeading(new Pose2d(4*12, 2*12, Math.toRadians(180)), Math.toRadians(180))

                .build();

        trajectories.add(traj);
    }

    @Override
    public void runOpMode() {
        configure();
        waitForStart();

        if (opModeIsActive()) {
            for (TrajectorySequence traj : trajectories) {
                // Moving Robot
                drive.followTrajectorySequenceAsync(traj);

                // Updating Robot Position
                while (opModeIsActive() && drive.isBusy()) {
                    drive.update();
                    telemetry.addData("Robot Position", drive.getPoseEstimate());
                    telemetry.update();
                }

                // Printing Deviation in Position Once Completed
                telemetry.addData("Expected End Position", traj.end());
                telemetry.addData("Current Position", drive.getPoseEstimate());
                telemetry.update();
            }
        }
    }
}