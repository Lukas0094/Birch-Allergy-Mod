package net.lukas.birch_allergy.effect;

import net.lukas.birch_allergy.BirchAllergy;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS
            = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, BirchAllergy.MOD_ID);

    public static final RegistryObject<MobEffect> BIRCH_ALLERGY = MOB_EFFECTS.register("birch_allergy",
            () -> new BirchAllergyEffect(MobEffectCategory.HARMFUL, 5342281));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}