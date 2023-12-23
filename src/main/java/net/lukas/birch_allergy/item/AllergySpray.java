package net.lukas.birch_allergy.item;

import net.lukas.birch_allergy.effect.ModEffects;
import net.lukas.birch_allergy.sound.SoundRegistry;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class AllergySpray extends Item {
    public AllergySpray(Properties p) {
        super(p.defaultDurability(69));
    }
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        level.playSound(null, player.getOnPos(), SoundRegistry.SPRAY.get(), SoundSource.MASTER, 1F, 1F);
        player.getCooldowns().addCooldown(this, 100);
        player.addEffect(new MobEffectInstance(ModEffects.ALLERGY_SPRAY.get(), 2400));
        itemstack.hurtAndBreak(1, player, (l) -> {
            l.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }
}
