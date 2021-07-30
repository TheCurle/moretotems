package uk.gemwire.moretotems.event;

import net.minecraft.block.FlowerBlock;
import net.minecraft.block.SpongeBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import uk.gemwire.moretotems.MoreTotems;
import uk.gemwire.moretotems.item.CommonTotemItem;

import java.util.Objects;

public class ModEvents {
    @SubscribeEvent
    public void useTotem(LivingDeathEvent event) {
        LivingEntity entity = event.getEntityLiving();
        World world = entity.level;
        if (entity instanceof PlayerEntity) {
            for (Hand hand : Hand.values()) {
                ItemStack heldItem = entity.getItemInHand(hand);

                // Behavior is based on the registry path of the otherwise identical items.
                // TODO: move this into a special lookup class
                switch(heldItem.getItem().getRegistryName().getPath()) {
                    case "ender_totem":
                        // Save the player from death.
                        // Give them invisibility for 10 seconds
                        // Teleport them to a random nearby location.
                        // Do this 16 times to mask their true location.
                        CommonTotemItem.defaultTotemBehavior(event, entity, heldItem, MoreTotems.ENDER_TOTEM.get(), true);
                        entity.addEffect(new EffectInstance(Effects.INVISIBILITY, 200, 0));
                        for(int i = 0; i < 16; ++i) {
                            double xRand = entity.getX() + (entity.getRandom().nextDouble() - 0.5D) * 16.0D;
                            double yRand = MathHelper.clamp(entity.getY() + (double) (entity.getRandom().nextInt(16) - 8), 0.0D, world.getHeight() - 1);
                            double zRand = entity.getZ() + (entity.getRandom().nextDouble() - 0.5D) * 16.0D;
                            entity.randomTeleport(xRand, yRand, zRand, true);
                            world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
                            entity.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
                        }
                        break;

                    case "cat_totem":
                        // Save the player from death.
                        // One in nine times, do not delete the item.
                        boolean flag = false;
                        if (world.getRandom().nextInt(9) == 0)
                            flag = true;
                        CommonTotemItem.defaultTotemBehavior(event, entity, heldItem, MoreTotems.CAT_TOTEM.get(), flag);
                        break;

                    case "wolf_totem":
                        // Save the player from death.
                        // Give them speed 2 for 10 seconds.
                        CommonTotemItem.defaultTotemBehavior(event, entity, heldItem, MoreTotems.WOLF_TOTEM.get(), true);
                        entity.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 200, 2));
                        break;

                    case "iron_golem_totem":
                        // Save the player from death.
                        // Give them strength 2 and resistance 2 for 10 seconds.
                        CommonTotemItem.defaultTotemBehavior(event, entity, heldItem, MoreTotems.IRON_GOLEM_TOTEM.get(), true);
                        entity.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, 200, 2));
                        entity.addEffect(new EffectInstance(Effects.DAMAGE_RESISTANCE, 200, 2));
                        break;

                    case "creeper_totem":
                        // Save the player from death.
                        // Explode the nearby vicinity.
                        CommonTotemItem.defaultTotemBehavior(event, entity, heldItem, MoreTotems.CREEPER_TOTEM.get(), true);
                        world.explode(entity, entity.getX(), entity.getY(), entity.getZ(), 4, false, Explosion.Mode.BREAK);
                        break;

                    case "axolotl_totem":
                        // Save the player from death.
                        // Refill their air.
                        // Give them a minute of water breathing.
                        CommonTotemItem.defaultTotemBehavior(event, entity, heldItem, MoreTotems.AXOLOTL_TOTEM.get(), true);
                        entity.setAirSupply(entity.getMaxAirSupply());
                        entity.addEffect(new EffectInstance(Effects.WATER_BREATHING, 1200, 1));
                        break;

                    case "shulker_totem":
                        // Save the player from death.
                        // Fling them up into the sky.
                        // Give them feather falling.
                        CommonTotemItem.defaultTotemBehavior(event, entity, heldItem, MoreTotems.SHULKER_TOTEM.get(), true);
                        entity.setDeltaMovement(0, 1000D, 0);
                        entity.hurtMarked = true;
                        entity.addEffect(new EffectInstance(Effects.SLOW_FALLING, 1200, 2));
                        break;

                    case "dye_totem":
                        // Save the player from death.
                        // Remove coloring from all dyed items in their inventory.
                        CommonTotemItem.defaultTotemBehavior(event, entity, heldItem, MoreTotems.DYE_TOTEM.get(), true);

                        // for every item in the player's inventory: check if dyeable. if true, remove dye.
                        ((PlayerEntity) entity).inventory.items.forEach(item -> { if (item.getItem() instanceof IDyeableArmorItem) ((IDyeableArmorItem) item.getItem()).clearColor(item); });
                        // for every item in the players' inventory: check if firework star. if true, remove colors.
                        ((PlayerEntity) entity).inventory.items.forEach(item -> { if (item.getItem() instanceof FireworkStarItem) { item.getTagElement("Explosion").putIntArray("Colors", new int[] {});} });
                        // TODO: firework rockets
                        // TODO: shulker boxes
                        // TODO: wool
                        // TODO: terracotta
                        // TODO: concrete
                        // TODO: glass
                        // TODO: beds

                        // for every item in the player's inventory: check if flower. if true, convert to white tulip.
                        ((PlayerEntity) entity).inventory.items.forEach(item -> { if (item.getItem() instanceof BlockItem && ((BlockItem) item.getItem()).getBlock() instanceof FlowerBlock) item = new ItemStack(Items.WHITE_TULIP, item.getCount()); });
                        break;

                    case "bread_totem":
                        // Save the player from death.
                        // Remove all bread from their inventory
                        CommonTotemItem.defaultTotemBehavior(event, entity, heldItem, MoreTotems.BREAD_TOTEM.get(), true);

                        // for every item in the player's inventory: get item namespace and path. compare against "minecraft:bread". if match, remove.
                        ((PlayerEntity) entity).inventory.items.forEach(item -> { if (Objects.requireNonNull(item.getItem().getRegistryName()).getNamespace().equals("minecraft") && item.getItem().getRegistryName().getPath().equals("bread")) item.setCount(0); });
                        break;

                    case "water_totem":
                        // Save the player from death.
                        // Fill buckets with water.
                        // Wet sponges.
                        CommonTotemItem.defaultTotemBehavior(event, entity, heldItem, MoreTotems.WATER_TOTEM.get(), true);
                        // for every item in the player's inventory: check if bucket. check if empty. if true, fill with water.
                        ((PlayerEntity) entity).inventory.items.forEach(item -> { if (item.getItem() instanceof BucketItem && ((BucketItem) item.getItem()).getFluid() == Fluids.EMPTY) item = new ItemStack(Items.WATER_BUCKET); });
                        // for every item in the player's inventory; check if sponge. check if dry. if true, make wet.
                        ((PlayerEntity) entity).inventory.items.forEach(item -> { if (item.getItem() instanceof BlockItem && ((BlockItem) item.getItem()).getBlock() instanceof SpongeBlock) item = new ItemStack(Items.WET_SPONGE, item.getCount()); });
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
                event.getDrops().add(new ItemEntity(world, entity.getX(), entity.getY(), entity.getZ(), new ItemStack(MoreTotems.ENDER_TOTEM.get())));
        }
    }
}