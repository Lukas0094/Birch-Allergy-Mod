package net.lukas.birch_allergy.effect;

import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;

public class BirchAllergyEffect extends MobEffect {
    public BirchAllergyEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    @Override
    public void applyEffectTick(LivingEntity e, int amplifier) {
        int ticks = e.getPersistentData().getInt("ticks");
        if(ticks%40==0) {
            e.hurt(DamageSources.magic(), 1);
        }
        ticks++;
        e.getPersistentData().putInt("ticks", ticks);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}