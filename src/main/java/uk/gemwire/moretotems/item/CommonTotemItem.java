package uk.gemwire.moretotems.item;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import uk.gemwire.moretotems.MoreTotems;

public class CommonTotemItem extends Item {
    public CommonTotemItem() {
        super(new Item.Properties().tab(MoreTotems.GROUP).stacksTo(1).rarity(Rarity.EPIC));
    }

    public static void defaultTotemBehavior(LivingDeathEvent event, LivingEntity entity, ItemStack heldItem, Item animationItem, boolean removeItem) {
        event.setCanceled(true);
        if (removeItem)
            heldItem.shrink(1);
        entity.setHealth(1.0F);
        entity.removeAllEffects();
        entity.addEffect(new EffectInstance(Effects.REGENERATION, 900, 1));
        entity.addEffect(new EffectInstance(Effects.ABSORPTION, 100, 1));
        Minecraft.getInstance().particleEngine.createTrackingEmitter(entity, ParticleTypes.TOTEM_OF_UNDYING, 30);
        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.TOTEM_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
        entity.playSound(SoundEvents.TOTEM_USE, 1.0F, 1.0F);
        Minecraft.getInstance().gameRenderer.displayItemActivation(new ItemStack(animationItem));
    }
}
