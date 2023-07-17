package net.lukas.birch_allergy.effect;

import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class BirchAllergyEffect extends MobEffect {
    public BirchAllergyEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    @Override
    public void applyEffectTick(LivingEntity e, int amplifier) {
        int ticks = e.getPersistentData().getInt("allergy_ticks");
        if(ticks%40==0) {
            e.hurt(getDamageSource(e.level()), amplifier);
        }
        ticks++;
        e.getPersistentData().putInt("allergy_ticks", ticks);
        if(Math.random() < 0.01*amplifier && !e.level().isClientSide()) {
            ServerLevel level = (ServerLevel) e.level();
            level.sendParticles(ParticleTypes.SNEEZE, e.getX(), e.getY()+1.8, e.getZ(), 60, 0, 0, 0, 0.04);
            e.addEffect(new MobEffectInstance(MobEffects.POISON, 40, 3));
            e.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 120, 4));
            e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 3));
            e.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 40, 3));
            level.playSound(null, e.getOnPos(), SoundEvents.PANDA_SNEEZE, SoundSource.MASTER, 2F, 0.4F);
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    public DamageSource getDamageSource(Level level) {
        Registry<DamageType> damageTypes = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
        ResourceKey<DamageType> ALLERGY_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("birch_allergy", "allergy_damage"));
        return new DamageSource(damageTypes.getHolderOrThrow(ALLERGY_DAMAGE));
    }
}