package Curdled.features;

import androidx.annotation.NonNull;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import dev.frozenmilk.dairy.core.Feature;
import dev.frozenmilk.dairy.core.FeatureRegistrar;
import dev.frozenmilk.dairy.core.dependency.Dependency;
import dev.frozenmilk.dairy.core.dependency.annotation.SingleAnnotation;
import dev.frozenmilk.dairy.core.wrapper.Wrapper;
import dev.frozenmilk.util.cell.LateInitCell;
import kotlin.annotation.MustBeDocumented;

public class IMUInterleaving implements Feature {
	private final LateInitCell<Attach> attachCell = new LateInitCell<>();
	
	private final Dependency<?> dependency =
			new SingleAnnotation<>(Attach.class)
					.onResolve((attach) -> { })
					.onResolve(attachCell);
	
	@NonNull
	@Override
	public Dependency<?> getDependency() { return dependency; }
	
	private Attach getAttach() { return attachCell.get(); }
    private IMUInterleaving() { FeatureRegistrar.registerFeature(this); }
	
	private static final IMUInterleaving INSTANCE = new IMUInterleaving();
	private static Telemetry telemetry;
	private static List<LynxModule> allHubs;
	public YawPitchRollAngles imuRead;

	private void getBulkRead(){ allHubs.get(0).getBulkData(); }

	private void imuShit1(){
		//do the first part of the imu shit
	}
	
	private void imuShit2(){
		//do the second part of the imu shit
	}

	private void getIMUBulkInterleaved(){
		imuShit1();
		getBulkRead();
		imuShit2();
	}

	@Override
	public void preUserInitHook(@NotNull Wrapper opMode) {}

	@Override
	public void postUserInitHook(@NotNull Wrapper opMode) {
		telemetry = opMode.getOpMode().telemetry;
		HardwareMap hardwareMap = opMode.getOpMode().hardwareMap;
		allHubs = hardwareMap.getAll(LynxModule.class);

		for (LynxModule hub : allHubs) { hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL); }
	}

	@Override
	public void preUserInitLoopHook(@NotNull Wrapper opMode) { getIMUBulkInterleaved(); }

	@Override
	public void postUserInitLoopHook(@NotNull Wrapper opMode) { }

	@Override
	public void preUserStartHook(@NotNull Wrapper opMode) { getIMUBulkInterleaved(); }

	@Override
	public void postUserStartHook(@NotNull Wrapper opMode) { }

	@Override
	public void preUserLoopHook(@NotNull Wrapper opMode) { getIMUBulkInterleaved(); }

	@Override
	public void postUserLoopHook(@NotNull Wrapper opMode) { }
	
	@Override
	public void preUserStopHook(@NotNull Wrapper opMode) { getIMUBulkInterleaved(); }
	
	@Override
	public void postUserStopHook(@NotNull Wrapper opMode) { attachCell.invalidate(); }
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@MustBeDocumented
	@Inherited
	public @interface Attach { }
}