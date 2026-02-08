package com.corosus.mobtimizations.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;

import java.util.ArrayList;
import java.util.List;

public class MobListsConfig {

    public static List<String> blacklistMobsList = new ArrayList<>();
    public static List<String> blacklistableMobsList = new ArrayList<>();
    //public static List<String> listEnhancedMobsParsedConfig = new ArrayList<>();

    static {
        String mc = "minecraft:";
    }

    private static final Builder BUILDER = new Builder();

    public static final CategoryGeneral GENERAL = new CategoryGeneral();

    public static final class CategoryGeneral {

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> blacklistMobs;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> mobsYouCanBlacklist;
        public final ForgeConfigSpec.ConfigValue<Boolean> useBlacklistAsWhitelist;

        private CategoryGeneral() {

            BUILDER.comment("General mod settings").push("general");

            blacklistMobs = BUILDER.comment("Mobs to exclude from everything this mod does, so it doesn't change their behavior at all").defineList("blacklistMobs", blacklistMobsList,
                    it -> it instanceof String);

            mobsYouCanBlacklist = BUILDER.comment("These are all the mobs from your modpack you can choose from when adding to the blacklist, use this to find the mob you need, then add it to the array for the blacklistMobs config option.").defineList("mobsYouCanBlacklist", blacklistableMobsList,
                    it -> it instanceof String);

            useBlacklistAsWhitelist = BUILDER.comment("Set the blacklist to behave as a whitelist instead").define("useBlacklistAsWhitelist", false);

            BUILDER.pop();
        }
    }
    public static final ForgeConfigSpec CONFIG = BUILDER.build();
}
