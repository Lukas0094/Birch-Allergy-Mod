package net.lukas.birch_allergy.client.renderer;

import net.lukas.birch_allergy.entity.BirchCultistEntity;
import net.lukas.birch_allergy.model.BirchCultistModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BirchCultistRenderer extends GeoEntityRenderer<BirchCultistEntity> {
    public BirchCultistRenderer(EntityRendererProvider.Context context) {
        super(context, new BirchCultistModel());
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull BirchCultistEntity entity) {
        return new ResourceLocation("birch_allergy:textures/entity/birch_cultist.png");
    }
}
