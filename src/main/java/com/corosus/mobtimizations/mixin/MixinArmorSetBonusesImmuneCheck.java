package com.corosus.mobtimizations.mixin;

import com.corosus.mobtimizations.Mobtimizations;
import com.corosus.mobtimizations.config.ConfigFeatures;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "uk.co.dotcode.asb.ModUtils")
//@Mixin(ModUtils.class)
public abstract class MixinArmorSetBonusesImmuneCheck {

    @Inject(method = "isImmuneToEffect", at = @At(value = "HEAD"), cancellable = true, remap = false)
    private static void execute(MobEffectInstance effect, LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (!Mobtimizations.modActive) return;
        //StopRendering.LOGGER.error("immune 1");
        if (ConfigFeatures.mod_ArmorSetBonuses_fixServerEffectImmunityCheckingOnNonPlayers && !(entity instanceof Player)) {
            //StopRendering.LOGGER.error("immune 2");
            //force it to apply the effect if its a zombie, follows vanilla rules still
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
