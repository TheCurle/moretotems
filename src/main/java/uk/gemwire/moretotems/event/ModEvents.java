package uk.gemwire.moretotems.event;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import uk.gemwire.moretotems.MoreTotems;
import uk.gemwire.moretotems.item.TotemItem;

public class ModEvents {
    @SubscribeEvent
    public void useTotem(LivingDeathEvent event) {
        LivingEntity entity = event.getEntityLiving();
        Level world = entity.level;
        if (entity instanceof Player) {
            for (InteractionHand hand : InteractionHand.values()) {
                ItemStack heldItem = entity.getItemInHand(hand);

                if (heldItem.getItem() == MoreTotems.TOTEM_OF_ESCAPING.get()) {
                    TotemItem.defaultTotemBehavior(event, entity, heldItem, MoreTotems.TOTEM_OF_ESCAPING.get());
                    entity.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 200, 0));
                    for(int i = 0; i < 16; ++i) {
                        double xRand = entity.getX() + (entity.getRandom().nextDouble() - 0.5D) * 16.0D;
                        double yRand = Mth.clamp(entity.getY() + (double) (entity.getRandom().nextInt(16) - 8), 0.0D, (double) (world.getHeight() - 1));
                        double zRand = entity.getZ() + (entity.getRandom().nextDouble() - 0.5D) * 16.0D;
                        entity.randomTeleport(xRand, yRand, zRand, true);
                        world.playSound((Player) null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
                        entity.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
                    }
                }
            }
        }
    }

    // TODO: Replace with a GLM
    @SubscribeEvent
    public void mobDropTotem(LivingDropsEvent event) {
        LivingEntity entity = event.getEntityLiving();
        Level world = entity.getCommandSenderWorld();
        if (Math.random() <= 0.05) {
            if (entity instanceof EnderMan)
                event.getDrops().add(new ItemEntity(world, entity.getX(), entity.getY(), entity.getZ(), new ItemStack(MoreTotems.TOTEM_OF_ESCAPING.get())));
        }
    }
}