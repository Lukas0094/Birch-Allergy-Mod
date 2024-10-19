package net.lukas.birch_allergy.effect;

import net.lukas.birch_allergy.BirchAllergy;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class AllergySprayEffect extends MobEffect {
    public AllergySprayEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity e, int amplifier) {
        ResourceKey<MobEffect> birchAllergyKey = ResourceKey.create(Registries.MOB_EFFECT, ResourceLocation.fromNamespaceAndPath(BirchAllergy.MOD_ID, "birch_allergy"));
        Holder<MobEffect> birchAllergyEffect = BuiltInRegistries.MOB_EFFECT.getHolderOrThrow(birchAllergyKey);
        e.removeEffect(birchAllergyEffect);
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amp) {
        return true;
    }
}
