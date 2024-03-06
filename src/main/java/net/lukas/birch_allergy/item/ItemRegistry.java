package net.lukas.birch_allergy.item;

import net.lukas.birch_allergy.BirchAllergy;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, BirchAllergy.MOD_ID);

    public static final RegistryObject<Item> ALLERGY_SPRAY = ITEMS.register("allergy_spray",
            () -> new AllergySpray(new Item.Properties()));

    public static final RegistryObject<Item> BIRCH_CANNON = ITEMS.register("birch_cannon",
            () -> new BirchCannon(new Item.Properties()));

    public static final RegistryObject<Item> BIRCH_ESSENCE = ITEMS.register("birch_essence",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> BIRCH_GRAPPLER = ITEMS.register("birch_grappler",
            () -> new BirchGrappler(new Item.Properties()));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
