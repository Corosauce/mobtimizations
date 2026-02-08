package com.corosus.mobtimizations.mixin;

import com.corosus.mobtimizations.Mobtimizations;
import com.corosus.mobtimizations.config.ConfigFeatures;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PathNavigation.class)
public abstract class MixinRecomputePath2 {

    @Shadow
    protected Mob mob;

    @Inject(method = "recomputePath",
            at = @At(value = "HEAD"), cancellable = true)
    public void recomputePath(CallbackInfo ci) {
        if (!Mobtimizations.canRecomputePath(mob)) {
            Mobtimizations.incCancel();
            ci.cancel();
        }
    }
}
