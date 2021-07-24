package uk.gemwire.moretotems.item;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import uk.gemwire.moretotems.MoreTotems;

public class TotemItem extends Item {
    public TotemItem() {
        super(new Item.Properties().tab(MoreTotems.GROUP).stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    public static void defaultTotemBehavior(LivingDeathEvent event, LivingEntity entity, ItemStack heldItem, Item animationItem) {
        event.setCanceled(true);
        heldItem.shrink(1);
        entity.setHealth(1.0F);
        entity.removeAllEffects();
        entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
        entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
        Minecraft.getInstance().particleEngine.createTrackingEmitter(entity, ParticleTypes.TOTEM_OF_UNDYING, 30);
        entity.level.playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
        entity.playSound(SoundEvents.TOTEM_USE, 1.0F, 1.0F);
        Minecraft.getInstance().gameRenderer.displayItemActivation(new ItemStack(animationItem));
    }
}
