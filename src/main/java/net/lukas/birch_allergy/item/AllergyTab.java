package net.lukas.birch_allergy.item;

import net.lukas.birch_allergy.BirchAllergy;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class AllergyTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BirchAllergy.MOD_ID);

    public static final RegistryObject<CreativeModeTab> ALLERGY_TAB = CREATIVE_MODE_TABS.register("allergy_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ItemRegistry.ALLERGY_SPRAY.get()))
                    .title(Component.translatable("allergy_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ItemRegistry.ALLERGY_SPRAY.get());
                        pOutput.accept(ItemRegistry.BIRCH_CANNON.get());
                        pOutput.accept(ItemRegistry.BIRCH_ESSENCE.get());
                        pOutput.accept(ItemRegistry.BIRCH_GRAPPLER.get());
                    })
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
