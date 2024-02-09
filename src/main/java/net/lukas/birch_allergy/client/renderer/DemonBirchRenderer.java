package net.lukas.birch_allergy.client.renderer;

import net.lukas.birch_allergy.entity.DemonBirchEntity;
import net.lukas.birch_allergy.model.DemonBirchModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DemonBirchRenderer extends GeoEntityRenderer<DemonBirchEntity> {
    public DemonBirchRenderer(EntityRendererProvider.Context context) {
        super(context, new DemonBirchModel());
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull DemonBirchEntity entity) {
        return new ResourceLocation("birch_allergy:textures/entity/demon_birch.png");
    }
}