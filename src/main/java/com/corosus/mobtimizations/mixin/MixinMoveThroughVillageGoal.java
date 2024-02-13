package com.corosus.mobtimizations.mixin;

import com.corosus.mobtimizations.Mobtimizations;
import com.corosus.mobtimizations.config.ConfigFeatures;
import net.minecraft.world.entity.ai.goal.MoveThroughVillageGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MoveThroughVillageGoal.class)
public abstract class MixinMoveThroughVillageGoal {

    @Inject(method = "canUse",
            at = @At(value = "HEAD"), cancellable = true)
    public void canUse(CallbackInfoReturnable<Boolean> cir) {
        if (!Mobtimizations.canVillageRaid()) {
            Mobtimizations.incCancel();
            cir.setReturnValue(false);
        }
    }
}
