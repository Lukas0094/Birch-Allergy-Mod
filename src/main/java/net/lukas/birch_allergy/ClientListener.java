package net.lukas.birch_allergy;

import net.lukas.birch_allergy.client.renderer.BirchCultistRenderer;
import net.lukas.birch_allergy.client.renderer.DemonBirchRenderer;
import net.lukas.birch_allergy.entity.EntityRegistry;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import software.bernie.example.registry.BlockRegistry;

@Mod.EventBusSubscriber(modid = BirchAllergy.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientListener {

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.DEMON_BIRCH.get(), DemonBirchRenderer::new);
        event.registerEntityRenderer(EntityRegistry.BIRCH_CULTIST.get(), BirchCultistRenderer::new);
    }

    @SubscribeEvent
    public static void registerRenderers(final FMLClientSetupEvent event) {

    }
}