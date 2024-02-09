package net.lukas.birch_allergy.model;

import net.lukas.birch_allergy.BirchAllergy;
import net.lukas.birch_allergy.entity.DemonBirchEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class DemonBirchModel extends DefaultedEntityGeoModel<DemonBirchEntity> {
    public DemonBirchModel() {
        super(new ResourceLocation(BirchAllergy.MOD_ID, "demon_birch"));
    }

    @Override
    public RenderType getRenderType(DemonBirchEntity entity, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(entity));
    }
}
