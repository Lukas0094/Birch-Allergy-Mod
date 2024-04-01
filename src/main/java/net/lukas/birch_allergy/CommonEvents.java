package net.lukas.birch_allergy;

import net.lukas.birch_allergy.entity.EntityRegistry;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BirchAllergy.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonEvents {
    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        AttributeSupplier.Builder genericAttributes = PathfinderMob.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 64)
                .add(Attributes.MAX_HEALTH, 1000);

        event.put(EntityRegistry.DEMON_BIRCH.get(), genericAttributes.build());

        AttributeSupplier.Builder genericCultistAttributes = PathfinderMob.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 64)
                .add(Attributes.MAX_HEALTH, 100);

        event.put(EntityRegistry.BIRCH_CULTIST.get(), genericCultistAttributes.build());
    }
}
