package Curdled.features;

import androidx.annotation.NonNull;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
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

public class LoopTimes implements Feature {
    private final LateInitCell<Attach> attachCell = new LateInitCell<>();

    private final Dependency<?> dependency =
            new SingleAnnotation<>(Attach.class)
                    .onResolve((attach) -> { })
                    .onResolve(attachCell);

    @NonNull
    @Override
    public Dependency<?> getDependency() { return dependency; }

    private Attach getAttach() { return attachCell.get(); }
    private LoopTimes() { FeatureRegistrar.registerFeature(this); }

    private static final LoopTimes INSTANCE = new LoopTimes();
    private static Telemetry telemetry;
    private final long startTime = System.nanoTime();
    private long lastTime = startTime;
    private int loops = 0;


    private void time(){
        long currentTime = System.nanoTime();
        double instantLoopTime = .000001 * (currentTime - lastTime);
        double instantHz = 1 / (instantLoopTime / 1000);
        double averageLoopTime = (.000001 * (currentTime - startTime)) / loops;
        double averageHz = loops / (averageLoopTime / 1000);

        telemetry.addData("Instantaneous Loop Time", instantLoopTime);
        telemetry.addData("Instantaneous Loop Hz", instantHz);
        telemetry.addData("Average Loop Time", averageLoopTime);
        telemetry.addData("Average Loop Hz", averageHz);

        lastTime = currentTime;
        loops += 1;

    }

    @Override
    public void preUserInitHook(@NotNull Wrapper opMode) { }

    @Override
    public void postUserInitHook(@NotNull Wrapper opMode) { time(); }

    @Override
    public void preUserInitLoopHook(@NotNull Wrapper opMode) { }

    @Override
    public void postUserInitLoopHook(@NotNull Wrapper opMode) { time(); }

    @Override
    public void preUserStartHook(@NotNull Wrapper opMode) { }

    @Override
    public void postUserStartHook(@NotNull Wrapper opMode) { time(); }

    @Override
    public void preUserLoopHook(@NotNull Wrapper opMode) { }

    @Override
    public void postUserLoopHook(@NotNull Wrapper opMode) { time(); }

    @Override
    public void preUserStopHook(@NotNull Wrapper opMode) { }

    @Override
    public void postUserStopHook(@NotNull Wrapper opMode) { attachCell.invalidate(); }
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @MustBeDocumented
    @Inherited
    public @interface Attach { }
}