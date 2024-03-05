package net.lukas.birch_allergy.item;

import net.lukas.birch_allergy.BirchAllergy;
import net.lukas.birch_allergy.effect.BirchAllergyEffect;
import net.lukas.birch_allergy.effect.ModEffects;
import net.lukas.birch_allergy.sound.SoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.List;

public class BirchCannon extends Item {
    public BirchCannon(Item.Properties p) {
        super(p.stacksTo(1));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        player.startUsingItem(hand);
        level.playSound(null, player.getOnPos(), SoundRegistry.CHARGE_UP.get(), SoundSource.MASTER);
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int timeTillEnd) {
        if(timeTillEnd == 5) {
            releaseUsing(stack, level, entity, 5);
        }
        if(!level.isClientSide()) {
            Vec3 lookVector = entity.getLookAngle();
            Vec3 eyePosition = entity.getEyePosition();
            Vec3 particlePos = lookVector.add(eyePosition).add(0, -0.42, 0);
            ServerLevel serverLevel = (ServerLevel) level;
            for(ServerPlayer player : serverLevel.players()) {
                serverLevel.sendParticles(player, new DustParticleOptions(new Vector3f(0.2F, 0.9F, 0.05F), 1.5F), true, particlePos.x(), particlePos.y(), particlePos.z(), 1, 0.05, 0.05, 0.05, 0.04);
                serverLevel.sendParticles(player, ParticleTypes.SNEEZE, true, particlePos.x(), particlePos.y(), particlePos.z(), 2, 0, 0, 0, 0.1);
            }
        }
    }

    public int getUseDuration(@Nullable ItemStack stack) {
        return 200;
    }

    public int getUseDuration() {
        return getUseDuration(null);
    }

    @Override
    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity, int timeTillEnd) {
        if(level.isClientSide()) return;
        ServerLevel serverLevel = (ServerLevel) level;
        ClientboundStopSoundPacket clientboundstopsoundpacket = new ClientboundStopSoundPacket(new ResourceLocation(BirchAllergy.MOD_ID, "charge_up"), SoundSource.MASTER);
        for(ServerPlayer serverplayer : serverLevel.players()) {
            serverplayer.connection.send(clientboundstopsoundpacket);
        }
        level.playSound(null, entity.getOnPos(), SoundRegistry.GUNFIRE.get(), SoundSource.MASTER, Math.max((getUseDuration()-timeTillEnd)/50F, 0.2F), 1.0F);
        raycastStart(serverLevel, entity, getUseDuration()-timeTillEnd);
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int count) {
        Level level = entity.level();
        if(level.isClientSide()) return;
        ServerLevel serverLevel = (ServerLevel) level;
        ClientboundStopSoundPacket clientboundstopsoundpacket = new ClientboundStopSoundPacket(new ResourceLocation(BirchAllergy.MOD_ID, "charge_up"), SoundSource.MASTER);
        for(ServerPlayer serverplayer : serverLevel.players()) {
            serverplayer.connection.send(clientboundstopsoundpacket);
        }
    }

    public void raycastStart(ServerLevel level, LivingEntity entity, int intensity) {
        float dx = -Mth.sin(entity.getYRot() * (0.017453292f)) * Mth.cos(entity.getXRot() * (0.017453292f));
        float dy = -Mth.sin((entity.getXRot()) * (0.017453292f));
        float dz = Mth.cos(entity.getYRot() * (0.017453292f)) * Mth.cos(entity.getXRot() * (0.017453292f));
        BlockPos damagePos = raycast(level, dx, dy, dz, entity, intensity);
        if(damagePos != null) explosion(level, damagePos, intensity);
    }

    public BlockPos raycast(ServerLevel level, float dx, float dy, float dz, LivingEntity entity, int intensity) {
        double step = 1.4;
        for(int i = 0; i<getRange()*step; i++) {
            Vec3 particlePos = new Vec3(dx*i/step+entity.getX(), dy*i/step+entity.getY()+1.38, dz*i/step+entity.getZ());
            BlockPos position = BlockPos.containing(particlePos);
            if(!level.getBlockState(position).isAir()) return position;
            List<Entity> entities = getEntitiesInBox(level, position, entity);
            for(Entity e : entities) {
                if(!(e instanceof ItemEntity)) return position;
            }
            int particles = (int) ((float) intensity / 20F);
            for(ServerPlayer player : level.players()) {
                level.sendParticles(player, ParticleTypes.SNEEZE, true, particlePos.x(), particlePos.y(), particlePos.z(), Math.min(intensity, 20), 0, 0, 0, 0.04);
                if(intensity > 45) {
                    level.sendParticles(player, new DustParticleOptions(new Vector3f(0.2F, 0.9F, 0.05F), (float) intensity / 45F), true, particlePos.x(), particlePos.y(), particlePos.z(), particles, 0, 0, 0, 0.04);
                }
            }
        }
        return null;
    }

    public void explosion(ServerLevel level, BlockPos pos, int intensity) {
        int strength = (int) (Math.sqrt(intensity)*1.5);
        for(int i = -strength; i<=strength; i++) {
            for(int k = -strength; k<=strength; k++) {
                int dyMax = (int) Math.sqrt(strength*strength-i*i-k*k);
                for(int j = -dyMax; j<=dyMax; j++) {
                    BlockPos position = pos.offset(i, j, k);
                    for(ServerPlayer player : level.players()) {
                        level.sendParticles(player, ParticleTypes.EXPLOSION, true, position.getX(), position.getY(), position.getZ(), 1, 0, 0, 0, 0.1);
                        level.sendParticles(player, ParticleTypes.SNEEZE, true, position.getX(), position.getY(), position.getZ(), 1, 0, 0, 0, 1);
                    }
                    if((j == -dyMax || j == dyMax) && Math.random() < 0.1 && !level.getBlockState(position).isAir()) {
                        if(Math.random() < 0.5) {
                            level.setBlockAndUpdate(position, Blocks.BIRCH_LOG.defaultBlockState());
                        } else {
                            level.setBlockAndUpdate(position, Blocks.BIRCH_LEAVES.defaultBlockState());
                        }
                        continue;
                    }
                    breakBlockFast(level, position);
                }
            }
        }
        int strengthSquared = strength*strength;
        for(Entity entity : getEntitiesInBox(level, pos, null, strength)) {
            if(entity instanceof LivingEntity e && entity.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < strengthSquared) {
                e.addEffect(new MobEffectInstance(ModEffects.BIRCH_ALLERGY.get(), 2400, 4));
                e.hurt(BirchAllergyEffect.getDamageSource(level), intensity);
            }
        }
        for(Entity entity : getEntitiesInBox(level, pos, null, strength*3)) {
            if(entity instanceof LivingEntity e && entity.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < strengthSquared*9) {
                e.addEffect(new MobEffectInstance(ModEffects.BIRCH_ALLERGY.get(), 600, 3));
                e.hurt(BirchAllergyEffect.getDamageSource(level), intensity/3F);
            }
        }
    }

    public static void breakBlockFast(ServerLevel level, BlockPos pos) {
        if(level.isOutsideBuildHeight(pos.getY())) return;
        SectionPos sectionPos = SectionPos.of(pos);
        LevelChunkSection section = level.getChunk(sectionPos.x(), sectionPos.z()).getSection(level.getSectionIndexFromSectionY(sectionPos.y()));
        byte x = (byte) (pos.getX() & 15);
        byte y = (byte) (pos.getY() & 15);
        byte z = (byte) (pos.getZ() & 15);
        breakBlock(section, x, y, z);
        level.getChunkSource().blockChanged(pos);
    }

    private static void breakBlock(LevelChunkSection section, byte x, byte y, byte z) {
        section.getStates().set(x, y, z, Blocks.AIR.defaultBlockState());
    }

    public static List<Entity> getEntitiesInBox(Level level, BlockPos position, @Nullable Entity entity) {
        return getEntitiesInBox(level, position, entity, 1);
    }

    public static List<Entity> getEntitiesInBox(Level level, BlockPos position, @Nullable Entity entity, int size) {
        return level.getEntities(entity, new AABB(position.getX()-size, position.getY()-size, position.getZ()-size, position.getX()+size, position.getY()+size, position.getZ()+size));
    }

    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.BOW;
    }

    public int getRange() {
        return 192;
    }
}
