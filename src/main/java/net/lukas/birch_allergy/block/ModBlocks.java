package net.lukas.birch_allergy.block;

import net.lukas.birch_allergy.BirchAllergy;
import net.lukas.birch_allergy.block.custom.BirchEssenceBucketBlock;
import net.lukas.birch_allergy.block.custom.DeadVillagerOakSignBlock;
import net.lukas.birch_allergy.block.custom.DeadVillagerOakSignTridentBlock;
import net.lukas.birch_allergy.item.ItemRegistry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, BirchAllergy.MOD_ID);

    public static final RegistryObject<Block> BIRCH_ESSENCE_BUCKET = registerBlock("birch_essence_bucket",
            () -> new BirchEssenceBucketBlock(BlockBehaviour.Properties.of()
                    .strength(0.2f).noOcclusion().forceSolidOn()));

    public static final RegistryObject<Block> DEAD_VILLAGER_OAK_SIGN = registerBlock("dead_villager_oak_sign",
            () -> new DeadVillagerOakSignBlock(BlockBehaviour.Properties.of()
                    .strength(0.2f).noOcclusion().forceSolidOn()));

    public static final RegistryObject<Block> DEAD_VILLAGER_OAK_SIGN_TRIDENT = registerBlock("dead_villager_oak_sign_trident",
            () -> new DeadVillagerOakSignTridentBlock(BlockBehaviour.Properties.of()
                    .strength(0.2f).noOcclusion().forceSolidOn()));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends  Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ItemRegistry.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
