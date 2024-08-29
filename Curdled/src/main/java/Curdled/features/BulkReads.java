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

public class BulkReads implements Feature {
    private final LateInitCell<Attach> attachCell = new LateInitCell<>();

    private final Dependency<?> dependency =
            new SingleAnnotation<>(Attach.class)
                    .onResolve((attach) -> { })
                    .onResolve(attachCell);

    @NonNull
    @Override
    public Dependency<?> getDependency() { return dependency; }

    private Attach getAttach() { return attachCell.get(); }
    private BulkReads() { FeatureRegistrar.registerFeature(this); }

    private static final BulkReads INSTANCE = new BulkReads();
    private static Telemetry telemetry;
    private static List<LynxModule> allHubs;

    private void clearCache(){ for (LynxModule hub : allHubs) { hub.clearBulkCache(); }}


    @Override
    public void preUserInitHook(@NotNull Wrapper opMode) {}

    @Override
    public void postUserInitHook(@NotNull Wrapper opMode) {
        telemetry = opMode.getOpMode().telemetry;
        HardwareMap hardwareMap = opMode.getOpMode().hardwareMap;
        allHubs = hardwareMap.getAll(LynxModule.class);

        for (LynxModule hub : allHubs) { hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL); }
        telemetry.addLine("Bulkreads Attached");
    }

    @Override
    public void preUserInitLoopHook(@NotNull Wrapper opMode) { clearCache(); }

    @Override
    public void postUserInitLoopHook(@NotNull Wrapper opMode) { }

    @Override
    public void preUserStartHook(@NotNull Wrapper opMode) { clearCache(); }

    @Override
    public void postUserStartHook(@NotNull Wrapper opMode) { }

    @Override
    public void preUserLoopHook(@NotNull Wrapper opMode) { clearCache(); }

    @Override
    public void postUserLoopHook(@NotNull Wrapper opMode) { }

    @Override
    public void preUserStopHook(@NotNull Wrapper opMode) { clearCache(); }

    @Override
    public void postUserStopHook(@NotNull Wrapper opMode) { attachCell.invalidate(); }
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @MustBeDocumented
    @Inherited
    public @interface Attach { }
}