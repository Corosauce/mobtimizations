package com.corosus.mobtimizations.mixin;

import com.corosus.mobtimizations.Mobtimizations;
import com.corosus.mobtimizations.config.ConfigFeatures;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RemoveBlockGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NearestAttackableTargetGoal.class)
public abstract class MixinNearestAttackableTargetGoal {

    @Inject(method = "canUse",
            at = @At(value = "HEAD"), cancellable = true)
    public void canUse(CallbackInfoReturnable<Boolean> cir) {
        NearestAttackableTargetGoal self = (NearestAttackableTargetGoal)(Object)this;
        if (!Mobtimizations.canTarget(self.mob)) {
            Mobtimizations.incCancel();
            cir.setReturnValue(false);
        }
    }
}
