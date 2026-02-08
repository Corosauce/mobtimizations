package com.corosus.mobtimizations.mixin;

import com.corosus.mobtimizations.Mobtimizations;
import com.corosus.mobtimizations.config.ConfigFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PathNavigation.class)
public abstract class MixinRecomputePath {

    @Shadow
    protected Mob mob;

    @Inject(method = "shouldRecomputePath",
            at = @At(value = "HEAD"), cancellable = true)
    public void shouldRecomputePath(BlockPos p_200904_, CallbackInfoReturnable<Boolean> cir) {
        if (!Mobtimizations.canRecomputePath(mob)) {
            Mobtimizations.incCancel();
            cir.setReturnValue(false);
        }
    }
}
