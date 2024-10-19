package net.lukas.birch_allergy;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
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
                            ResourceKey<MobEffect> birchAllergyKey = ResourceKey.create(Registries.MOB_EFFECT, ResourceLocation.fromNamespaceAndPath(BirchAllergy.MOD_ID, "birch_allergy"));
                            Holder<MobEffect> birchAllergyEffect = BuiltInRegistries.MOB_EFFECT.getHolderOrThrow(birchAllergyKey);
                            if(player.getEffect(birchAllergyEffect) == null) {
                                player.addEffect(new MobEffectInstance(birchAllergyEffect, 400, 1));
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
