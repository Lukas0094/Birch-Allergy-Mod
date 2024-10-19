package net.lukas.birch_allergy.item;

import net.lukas.birch_allergy.BirchAllergy;
import net.lukas.birch_allergy.sound.SoundRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class AllergySpray extends Item {
    public AllergySpray(Properties p) {
        super(p.durability(69));
    }
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        level.playSound(null, player.getOnPos(), SoundRegistry.SPRAY.get(), SoundSource.MASTER, 1F, 1F);
        player.getCooldowns().addCooldown(this, 100);
        ResourceKey<MobEffect> allergySprayKey = ResourceKey.create(Registries.MOB_EFFECT, ResourceLocation.fromNamespaceAndPath(BirchAllergy.MOD_ID, "allergy_spray"));
        Holder<MobEffect> allergySprayEffect = BuiltInRegistries.MOB_EFFECT.getHolderOrThrow(allergySprayKey);
        player.addEffect(new MobEffectInstance(allergySprayEffect, 2400));
        if (!level.isClientSide()) {
            itemstack.hurtAndBreak(1, ((ServerLevel) level), ((ServerPlayer) player), item -> player.onEquippedItemBroken(item, EquipmentSlot.MAINHAND));
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }
}
