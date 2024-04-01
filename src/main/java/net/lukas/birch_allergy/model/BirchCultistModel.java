package net.lukas.birch_allergy.model;

import net.lukas.birch_allergy.BirchAllergy;
import net.lukas.birch_allergy.entity.BirchCultistEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class BirchCultistModel extends DefaultedEntityGeoModel<BirchCultistEntity> {
    public BirchCultistModel() {
        super(new ResourceLocation(BirchAllergy.MOD_ID, "birch_cultist"));
    }

    @Override
    public RenderType getRenderType(BirchCultistEntity entity, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(entity));
    }
}
