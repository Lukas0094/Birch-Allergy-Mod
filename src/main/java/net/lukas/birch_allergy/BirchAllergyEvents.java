package net.lukas.birch_allergy;

import net.lukas.birch_allergy.effect.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.OptionalInt;
public class BirchAllergyEvents {
    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        allergyTick(event);
    }

    public static void allergyTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level level = player.level();
        int ticks = player.getPersistentData().getInt("ticks");
        final int s = 7;
        if(ticks%40 == 0) {
            for(int x = -s; x<=s; x+=2) {
                for(int y = -s; y<=s; y+=2) {
                    for(int z = -s; z<=s; z+=2) {
                        BlockPos pos = player.getOnPos().offset(x, y, z);
                        BlockState state = level.getBlockState(pos);
                        if(state.getBlock() == Blocks.BIRCH_LEAVES && !LeavesBlock.getOptionalDistanceAt(state).equals(OptionalInt.of(7)) && !state.getValue(LeavesBlock.PERSISTENT)) {
                            if(player.getEffect(ModEffects.ALLERGY_SPRAY.get()) == null) {
                                player.addEffect(new MobEffectInstance(ModEffects.BIRCH_ALLERGY.get(), 400, 1));
                            }
                        }
                    }
                }
            }
        }
        ticks++;
        player.getPersistentData().putInt("ticks", ticks);
    }
    @SubscribeEvent
    public static void onTick(TickEvent.ServerTickEvent event) {
        final double speed = 0.82;
        for(ServerLevel level : event.getServer().getAllLevels()) {
            for(Entity entity : level.getAllEntities()) {
                if(entity instanceof AbstractMinecart) {
                    entity.setDeltaMovement(entity.getDeltaMovement().multiply(speed, 1, speed));
                }
            }
        }
    }
}
