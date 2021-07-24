package uk.gemwire.moretotems.event;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import uk.gemwire.moretotems.MoreTotems;
import uk.gemwire.moretotems.item.TotemItem;

public class ModEvents {
    @SubscribeEvent
    public void useTotem(LivingDeathEvent event) {
        LivingEntity entity = event.getEntityLiving();
        World world = entity.level;
        if (entity instanceof PlayerEntity) {
            for (Hand hand : Hand.values()) {
                ItemStack heldItem = entity.getItemInHand(hand);
                if (heldItem.getItem() == MoreTotems.TOTEM_OF_ESCAPING.get()) {
                    TotemItem.defaultTotemBehavior(event, entity, heldItem, MoreTotems.TOTEM_OF_ESCAPING.get());
                    entity.addEffect(new EffectInstance(Effects.INVISIBILITY, 200, 0));
                    for(int i = 0; i < 16; ++i) {
                        double xRand = entity.getX() + (entity.getRandom().nextDouble() - 0.5D) * 16.0D;
                        double yRand = MathHelper.clamp(entity.getY() + (double) (entity.getRandom().nextInt(16) - 8), 0.0D, (double) (world.getHeight() - 1));
                        double zRand = entity.getZ() + (entity.getRandom().nextDouble() - 0.5D) * 16.0D;
                        entity.randomTeleport(xRand, yRand, zRand, true);
                        world.playSound((PlayerEntity) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
                        entity.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void mobDropTotem(LivingDropsEvent event) {
        LivingEntity entity = event.getEntityLiving();
        World world = entity.getCommandSenderWorld();
        if (Math.random() <= 0.05) {
            if (entity instanceof EndermanEntity)
                event.getDrops().add(new ItemEntity(world, entity.getX(), entity.getY(), entity.getZ(), new ItemStack(MoreTotems.TOTEM_OF_ESCAPING.get())));
        }
    }
}