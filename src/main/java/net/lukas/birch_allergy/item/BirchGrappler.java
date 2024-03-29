package net.lukas.birch_allergy.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class BirchGrappler extends Item {
    public BirchGrappler(Item.Properties p) {
        super(p.stacksTo(1));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        player.startUsingItem(hand);
        if(!level.isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) level;
            raycastStart(serverLevel, player, 2);
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int timeTillEnd) {
        if(timeTillEnd == 5) {
            releaseUsing(stack, level, entity, 5);
        }
        CompoundTag tag = entity.getPersistentData();
        if(!level.isClientSide() && tag.getBoolean("work")) {
            int x = tag.getInt("grapplerX");
            int y = tag.getInt("grapplerY");
            int z = tag.getInt("grapplerZ");
            Vec3 grapplePos = new Vec3(x, y, z);
            Vec3 playerPos = entity.position();
            Vec3 unitNormalVector = playerPos.vectorTo(grapplePos).normalize();
            Vec3 velocityVector = entity.getDeltaMovement();
            double force = -unitNormalVector.dot(velocityVector);
            entity.setDeltaMovement(entity.getDeltaMovement().add(unitNormalVector.scale(force)).scale(1.14));
            double speed = entity.getDeltaMovement().length();
            if(speed > 5) {
                Vec3 movement = entity.getDeltaMovement();
                entity.setDeltaMovement(movement.scale(0.85));
            }
            particleTrail((ServerLevel) level, playerPos.add(0, 1.38, 0).add(entity.getLookAngle()), grapplePos);
            sounds((ServerLevel) level, playerPos, (float) velocityVector.length());
            entity.hurtMarked = true;
            entity.fallDistance = 0;
        }
    }

    public int getUseDuration(@Nullable ItemStack stack) {
        return 400;
    }

    public int getUseDuration() {
        return getUseDuration(null);
    }

    @Override
    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity, int timeTillEnd) {

    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int count) {

    }

    public void particleTrail(ServerLevel level, Vec3 startPos, Vec3 endPos) {
        double n = startPos.subtract(endPos).horizontalDistance();
        double d = endPos.y-startPos.y;
        double a = 2*d/(n*(n+1));
        double x = startPos.x();
        double y = startPos.y();
        double z = startPos.z();
        double xStep = (endPos.x()-startPos.x())/n;
        double zStep = (endPos.z()-startPos.z())/n;
        for(int i = 1; i<=n; i++) {
            for(ServerPlayer player : level.players()) {
                level.sendParticles(player, ParticleTypes.SNEEZE, true, x, y, z, 1, 0.6, 0.6, 0.6, 0.2);
                level.sendParticles(player, new DustParticleOptions(new Vector3f(0.2F, 0.9F, 0.05F), 2), true, x, y, z, 1, 0.6, 0.6, 0.6, 0.04);
            }
            x+=xStep;
            y+=a*i;
            z+=zStep;
        }
    }

    public void sounds(ServerLevel level, Vec3 pos, float speed) {
        float volume = 0.3F*speed;
        level.playSound(null, BlockPos.containing(pos), SoundEvents.GENERIC_EXPLODE, SoundSource.MASTER, volume, 1.5F);
        level.playSound(null, BlockPos.containing(pos), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.MASTER, volume, 1.5F);
    }

    public void raycastStart(ServerLevel level, LivingEntity entity, int intensity) {
        float dx = -Mth.sin(entity.getYRot() * (0.017453292f)) * Mth.cos(entity.getXRot() * (0.017453292f));
        float dy = -Mth.sin((entity.getXRot()) * (0.017453292f));
        float dz = Mth.cos(entity.getYRot() * (0.017453292f)) * Mth.cos(entity.getXRot() * (0.017453292f));
        BlockPos pos = raycast(level, dx, dy, dz, entity, intensity);
        if(pos != null) {
            entity.getPersistentData().putInt("grapplerX", pos.getX());
            entity.getPersistentData().putInt("grapplerY", pos.getY());
            entity.getPersistentData().putInt("grapplerZ", pos.getZ());
            entity.getPersistentData().putBoolean("work", true);
        }
        else {
            entity.getPersistentData().putBoolean("work", false);
        }
    }

    public BlockPos raycast(ServerLevel level, float dx, float dy, float dz, LivingEntity entity, int intensity) {
        double step = 1.4;
        for(int i = 0; i<getRange()*step; i++) {
            Vec3 particlePos = new Vec3(dx*i/step+entity.getX(), dy*i/step+entity.getY()+1.38, dz*i/step+entity.getZ());
            BlockPos position = BlockPos.containing(particlePos);
            if(!level.getBlockState(position).isAir()) return position;
        }
        return null;
    }

    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.BOW;
    }

    public int getRange() {
        return 192;
    }
}
