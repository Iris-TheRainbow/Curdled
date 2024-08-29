package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import Curdled.features.LoopTimes;
import Curdled.features.BulkReads;

@Autonomous
@BulkReads.Attach
@LoopTimes.Attach
public class test extends OpMode {
    private DcMotorEx motor;

    @Override
    public void init(){
        motor = hardwareMap.get(DcMotorEx.class, "motor");
    }

    @Override
    public void loop() {
        telemetry.addData("Motor Pos", motor.getCurrentPosition());
        motor.setPower(Math.sin((double) System.nanoTime() / 100));
    }
}
