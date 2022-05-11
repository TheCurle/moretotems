package uk.gemwire.moretotems.item;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.loading.FMLLoader;
import uk.gemwire.moretotems.ClientStuff;
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
        entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
        entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));

        if (FMLLoader.getDist() == Dist.CLIENT)
            ClientStuff.particleStuff(entity);

        entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
        entity.playSound(SoundEvents.TOTEM_USE, 1.0F, 1.0F);

        if (FMLLoader.getDist() == Dist.CLIENT)
            ClientStuff.totemActivated(animationItem);
    }
}
