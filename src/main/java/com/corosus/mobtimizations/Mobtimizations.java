package com.corosus.mobtimizations;

import com.corosus.coroutil.util.CU;
import com.corosus.mobtimizations.config.ConfigFeatures;
import com.corosus.mobtimizations.config.ConfigFeaturesCustomization;
import com.corosus.modconfig.ConfigMod;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.slf4j.Logger;

import java.io.File;

public class Mobtimizations
{
    public static final String MODID = "mobtimizations";
    public static final Logger LOGGER = LogUtils.getLogger();

    /*public static String lastWanderTime = "coro_lastWanderTime";
    public static String lastPlayerScanTime = "coro_lastPlayerScanTime";
    public static String playerInRange = "coro_playerInRange";*/
    /*public static int playerRangeCutoff = 12;
    public static int playerScanDelay = 20*2;*/

    public static boolean modActive = true;
    public static boolean testSpawningActive = false;
    private static int cancels = 0;

    public Mobtimizations()
    {
        new File("./config/" + MODID).mkdirs();
        ConfigMod.addConfigFile(MODID, new ConfigFeatures());
        ConfigMod.addConfigFile(MODID, new ConfigFeaturesCustomization());
    }

    public static int getCancels() {
        return cancels;
    }

    public static void incCancel() {
        cancels++;
        if (cancels == Integer.MAX_VALUE) cancels = 0;
    }

    public static boolean rollPercentChance(float percent) {
        if (percent == 0) return false;
        return CU.rand().nextFloat() <= (percent/100F);
    }

    public static boolean canAvoidHazards(Mob mob) {
        if (!Mobtimizations.modActive) return true;
        if (ConfigFeatures.optimizationMonsterHazardAvoidingPathfollowing) {
            if (mob instanceof Monster) {
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    public static boolean canCrushEggs() {
        if (!Mobtimizations.modActive) return true;
        if (ConfigFeatures.optimizationZombieSearchAndDestroyTurtleEgg) {
            if (rollPercentChance(ConfigFeaturesCustomization.zombieSearchAndDestroyTurtleEggPercentChance)) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    public static boolean canRecomputePath() {
        if (!Mobtimizations.modActive) return true;
        if (ConfigFeatures.optimizationMobRepathfinding) {
            return false;
        }
        return true;
    }

    public static boolean canVillageRaid() {
        if (!Mobtimizations.modActive) return true;
        if (ConfigFeatures.optimizationZombieVillageRaid) {
            if (rollPercentChance(ConfigFeaturesCustomization.zombieVillageRaidPercentChance)) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    public static boolean canTarget(Mob mob) {
        if (!Mobtimizations.modActive) return true;
        if (ConfigFeatures.optimizationMobEnemyTargeting) {
            if (useReducedRates(mob)) {
                return rollPercentChance(ConfigFeaturesCustomization.mobEnemyTargetingReducedRatePercentChance);
            } else {
                return true;
            }
        }
        return true;
    }

    public static boolean canWander(Mob mob) {
        if (!Mobtimizations.modActive) return true;
        if (ConfigFeatures.optimizationMobWandering) {
            if (!rollPercentChance(ConfigFeaturesCustomization.mobWanderingPercentChance)) return false;

            float multiplier = useReducedRates(mob) ? ConfigFeaturesCustomization.mobWanderingReducedRateMultiplier : 1;
            //long lastWander = mob.getPersistentData().getLong(Mobtimizations.lastWanderTime);
            long lastWander = ((MobtimizationEntityFields)mob).getlastWanderTime();

            if (lastWander + ConfigFeaturesCustomization.mobWanderingDelay * multiplier > mob.level.getGameTime()) {
                return false;
            } else {
                //System.out.println("marked new path time: " + mob.level.getGameTime());
                //LOGGER.info("marked new path time: " + mob.level.getGameTime());
                //mob.getPersistentData().putLong(Mobtimizations.lastWanderTime, mob.level.getGameTime());
                ((MobtimizationEntityFields)mob).setlastWanderTime(mob.level.getGameTime());
            }
            return true;
        }
        return true;
    }

    public static boolean useReducedRates(Mob mob) {
        if (!ConfigFeatures.playerProximityReducedRate) return false;
        //long lastPlayerScan = data.getLong(Mobtimizations.lastPlayerScanTime);
        long lastPlayerScan = ((MobtimizationEntityFields)mob).getlastPlayerScanTime();
        if (lastPlayerScan + ConfigFeaturesCustomization.playerProximityReducedRatePlayerScanRate > mob.level.getGameTime()) {
            //TODO use a mixin to add a variable to mob instead of using nbt
            //return !data.getBoolean(playerInRange);
            return ((MobtimizationEntityFields)mob).isplayerInRange();
            //if (data.contains(playerInRange)) {
        } else {
            boolean playerInRangeBool = checkIfPlayerInRange(mob);
            //data.putBoolean(playerInRange, playerInRangeBool);
            ((MobtimizationEntityFields)mob).setplayerInRange(playerInRangeBool);
            //mob.getPersistentData().putLong(Mobtimizations.lastPlayerScanTime, mob.level.getGameTime());
            ((MobtimizationEntityFields)mob).setlastPlayerScanTime(mob.level.getGameTime());
            return !playerInRangeBool;
        }
    }

    private static boolean checkIfPlayerInRange(Mob mob) {
        for (Player player : mob.level.players()) {
            if (player.distanceToSqr(mob) < ConfigFeaturesCustomization.playerProximityReducedRateRangeCutoff * ConfigFeaturesCustomization.playerProximityReducedRateRangeCutoff) {
                return true;
            }
        }
        return false;
    }

    public static BlockPathTypes getBlockPathTypeStatic(BlockGetter p_77605_, BlockPos.MutableBlockPos p_77606_) {
        int i = p_77606_.getX();
        int j = p_77606_.getY();
        int k = p_77606_.getZ();
        BlockPathTypes blockpathtypes = WalkNodeEvaluator.getBlockPathTypeRaw(p_77605_, p_77606_);
        if (blockpathtypes == BlockPathTypes.OPEN && j >= p_77605_.getMinBuildHeight() + 1) {
            BlockPathTypes blockpathtypes1 = WalkNodeEvaluator.getBlockPathTypeRaw(p_77605_, p_77606_.set(i, j - 1, k));
            blockpathtypes = blockpathtypes1 != BlockPathTypes.WALKABLE && blockpathtypes1 != BlockPathTypes.OPEN && blockpathtypes1 != BlockPathTypes.WATER && blockpathtypes1 != BlockPathTypes.LAVA ? BlockPathTypes.WALKABLE : BlockPathTypes.OPEN;
            if (blockpathtypes1 == BlockPathTypes.DAMAGE_FIRE) {
                blockpathtypes = BlockPathTypes.DAMAGE_FIRE;
            }

            if (blockpathtypes1 == BlockPathTypes.DAMAGE_CACTUS) {
                blockpathtypes = BlockPathTypes.DAMAGE_CACTUS;
            }

            if (blockpathtypes1 == BlockPathTypes.DAMAGE_OTHER) {
                blockpathtypes = BlockPathTypes.DAMAGE_OTHER;
            }

            if (blockpathtypes1 == BlockPathTypes.STICKY_HONEY) {
                blockpathtypes = BlockPathTypes.STICKY_HONEY;
            }

            if (blockpathtypes1 == BlockPathTypes.POWDER_SNOW) {
                blockpathtypes = BlockPathTypes.DANGER_POWDER_SNOW;
            }
        }

        /*if (blockpathtypes == BlockPathTypes.WALKABLE) {
            blockpathtypes = checkNeighbourBlocks(p_77605_, p_77606_.set(i, j, k), blockpathtypes);
        }*/

        return blockpathtypes;
    }
}
