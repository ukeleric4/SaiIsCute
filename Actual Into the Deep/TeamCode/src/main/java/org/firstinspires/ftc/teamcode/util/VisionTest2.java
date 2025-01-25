package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.parts.Orientation;
import org.firstinspires.ftc.teamcode.parts.PIDFSlide;
import org.firstinspires.ftc.teamcode.parts.Vision;
import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;

@TeleOp
public class VisionTest2 extends LinearOpMode {
    Vision vision;
    Orientation orientation;
    PIDFSlide slide;

    Follower follower;

    Gamepad oldGamepad1;
    Gamepad oldGamepad2;

    double newX;
    double newY;
    double newYPixels;
    double multiplier = 2.5; // edit
    Pose currentPosition;
    Pose newPosition = new Pose(0, 0, 0);

    @Override
    public void runOpMode() {
        vision = new Vision(hardwareMap, telemetry);
        slide = new PIDFSlide(hardwareMap);

        follower = new Follower(hardwareMap);
        slide.setTargetPos(400);

        waitForStart();
        while (opModeIsActive()) {
            newX = vision.getXMovement();
            newY = vision.getYMovement();
            newYPixels = vision.getYPixels();

            currentPosition = follower.getPose();

            if (gamepad1.y) {
                slide.setTargetPos(1000);
            }
            if (gamepad1.x && !oldGamepad1.a) {
                newPosition.setX(currentPosition.getX() + newX);
                newPosition.setY(slide.getCurrentPos() + newY);

                // run slide to position where block is
                slide.setTargetPos((int) (slide.getTargetPos() + newYPixels));

                // Somehow make robot go to that position
            }

            vision.updateVision();
            slide.updateSlide();
            slide.updatePower();
            follower.update();
            oldGamepad1 = gamepad1;
            oldGamepad2 = gamepad2;
            telemetry.addData("current pos:", slide.getCurrentPos());
            telemetry.addData("Change in Y Pixels:", newYPixels);
            telemetry.addData("Y movement:", newY);
            telemetry.addData("New slide position:", slide.getCurrentPos() + newY);
            telemetry.addData("Aspect Ratio:", vision.getAspectRatio());
            telemetry.update();
        }
    }
}
