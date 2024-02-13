package com.corosus.mobtimizations.loader.forge;

import com.corosus.coroutil.util.CU;
import com.corosus.mobtimizations.CommandMisc;
import com.corosus.mobtimizations.Mobtimizations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Mobtimizations.MODID)
public class MobtimizationsForge extends Mobtimizations {

    public MobtimizationsForge() {
        super();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.register(Mobtimizations.class);
    }

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        CommandMisc.register(event.getDispatcher());
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    private void loadComplete(final FMLLoadCompleteEvent event)
    {

    }

    @SubscribeEvent
    public void worldTick(TickEvent.WorldTickEvent event) {
        if (Mobtimizations.testSpawningActive) {
            Level level = event.world;
            int huskOrZombieCount = 0;
            if (event.phase == TickEvent.Phase.END && level.dimension() == Level.OVERWORLD && level.getGameTime() % 100 == 0 && level instanceof ServerLevel serverLevel) {
                for (Entity entity : serverLevel.getAllEntities()) {
                    if (entity instanceof Zombie) {
                        huskOrZombieCount++;
                    }
                }

                int spawnMax = 1000;
                int spawnRange = 100;
                int spawnCount = 0;
                if (huskOrZombieCount < spawnMax) {
                    for (int i = 0; i < spawnMax - huskOrZombieCount; i++) {

                        int playerCount = level.players().size();
                        Player player = level.players().get(CU.rand().nextInt(playerCount));
                        int x = Mth.floor(player.position().x + ((CU.rand().nextFloat() * spawnRange) - (CU.rand().nextFloat() * spawnRange)));
                        int z = Mth.floor(player.position().z + ((CU.rand().nextFloat() * spawnRange) - (CU.rand().nextFloat() * spawnRange)));
                        int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);

                        Husk mob = new Husk(EntityType.HUSK, level);
                        mob.setPos(x, y, z);
                        ((Mob)mob).finalizeSpawn(serverLevel, level.getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.SPAWNER, (SpawnGroupData)null, (CompoundTag)null);
                        level.addFreshEntity(mob);
                        spawnCount++;
                    }
                }
                System.out.println("spawned " + spawnCount + " husks");
            }

        }
    }
}
