package uk.gemwire.moretotems.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SpongeBlock;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.server.ServerLifecycleHooks;
import uk.gemwire.moretotems.MoreTotems;
import uk.gemwire.moretotems.item.CommonTotemItem;

import java.util.Objects;

public class ModEvents {

    @SubscribeEvent
    public void useTotem(LivingDeathEvent event) {
        LivingEntity living = event.getEntityLiving();
        Level world = living.level;
        if (living instanceof Player player) {

            for (InteractionHand hand : InteractionHand.values()) {
                ItemStack heldItem = player.getItemInHand(hand);

                // Behavior is based on the registry path of the otherwise identical items.
                // TODO: move this into a special lookup class
                switch (heldItem.getItem().getRegistryName().getPath()) {
                    case "ender_totem" -> {
                        // Save the player from death.
                        // Give them invisibility for 10 seconds
                        // Teleport them to a random nearby location.
                        // Do this 16 times to mask their true location.
                        CommonTotemItem.defaultTotemBehavior(event, player, heldItem, MoreTotems.ENDER_TOTEM.get(), true);
                        player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 200, 0));
                        for (int i = 0; i < 16; ++i) {
                            double xRand = player.getX() + (player.getRandom().nextDouble() - 0.5D) * 16.0D;
                            double yRand = Mth.clamp(player.getY() + (double) (player.getRandom().nextInt(16) - 8), 0.0D, world.getHeight() - 1);
                            double zRand = player.getZ() + (player.getRandom().nextDouble() - 0.5D) * 16.0D;
                            player.randomTeleport(xRand, yRand, zRand, true);
                            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
                            player.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
                        }
                    }
                    case "cat_totem" -> {
                        // Save the player from death.
                        // One in nine times, do not delete the item.
                        boolean flag = world.getRandom().nextInt(9) == 0;
                        CommonTotemItem.defaultTotemBehavior(event, player, heldItem, MoreTotems.CAT_TOTEM.get(), flag);
                    }
                    case "wolf_totem" -> {
                        // Save the player from death.
                        // Give them speed 2 for 10 seconds.
                        CommonTotemItem.defaultTotemBehavior(event, player, heldItem, MoreTotems.WOLF_TOTEM.get(), true);
                        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 2));
                    }
                    case "iron_golem_totem" -> {
                        // Save the player from death.
                        // Give them strength 2 and resistance 2 for 10 seconds.
                        CommonTotemItem.defaultTotemBehavior(event, player, heldItem, MoreTotems.IRON_GOLEM_TOTEM.get(), true);
                        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 2));
                        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 2));
                    }
                    case "creeper_totem" -> {
                        // Save the player from death.
                        // Explode the nearby vicinity.
                        CommonTotemItem.defaultTotemBehavior(event, player, heldItem, MoreTotems.CREEPER_TOTEM.get(), true);
                        world.explode(player, player.getX(), player.getY(), player.getZ(), 4, false, Explosion.BlockInteraction.BREAK);
                    }
                    case "axolotl_totem" -> {
                        // Save the player from death.
                        // Refill their air.
                        // Give them a minute of water breathing.
                        CommonTotemItem.defaultTotemBehavior(event, player, heldItem, MoreTotems.AXOLOTL_TOTEM.get(), true);
                        player.setAirSupply(player.getMaxAirSupply());
                        player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 1200, 1));
                    }
                    case "shulker_totem" -> {
                        // Save the player from death.
                        // Fling them up into the sky.
                        // Give them feather falling.
                        CommonTotemItem.defaultTotemBehavior(event, player, heldItem, MoreTotems.SHULKER_TOTEM.get(), true);
                        player.setDeltaMovement(0, 1000D, 0);
                        player.hurtMarked = true;
                        player.resetFallDistance();
                        player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 1200, 2));
                    }
                    case "dye_totem" -> {
                        // Save the player from death.
                        // Remove coloring from all dyed items in their inventory.
                        CommonTotemItem.defaultTotemBehavior(event, player, heldItem, MoreTotems.DYE_TOTEM.get(), true);

                        player.inventoryMenu.slots.forEach(slot -> {
                            // for every item in the player's inventory: check if dyeable. if true, remove dye.
                            if (slot.getItem().getItem() instanceof DyeableLeatherItem)
                                ((DyeableLeatherItem) slot.getItem().getItem()).clearColor(slot.getItem());

                            // for every item in the players' inventory: check if firework star. if true, remove colors.
                            else if (slot.getItem().getItem() instanceof FireworkStarItem) {
                                slot.getItem().getTagElement("Explosion").putIntArray("Colors", new int[]{});
                            }

                            // for every item in the player's inventory: check if flower. if true, convert to white tulip.
                            else if (slot.getItem().getItem() instanceof BlockItem block) {
                                if (block.getBlock() instanceof FlowerBlock)
                                    slot.set(new ItemStack(Items.WHITE_TULIP, slot.getItem().getCount()));
                            }
                            // TODO: firework rockets
                            // TODO: shulker boxes
                            // TODO: wool
                            // TODO: terracotta
                            // TODO: concrete
                            // TODO: glass
                            // TODO: beds
                        });

                    }
                    case "bread_totem" -> {
                        // Save the player from death.
                        // Remove all bread from their inventory
                        CommonTotemItem.defaultTotemBehavior(event, player, heldItem, MoreTotems.BREAD_TOTEM.get(), true);

                        // for every item in the player's inventory: get item namespace and path. compare against "minecraft:bread". if match, remove.
                        player.inventoryMenu.slots.forEach(slot -> {
                            if (Objects.requireNonNull(slot.getItem().getItem().getRegistryName()).getNamespace().equals("minecraft") && slot.getItem().getItem().getRegistryName().getPath().equals("bread"))
                                slot.set(ItemStack.EMPTY);
                        });
                    }
                    case "water_totem" -> {
                        // Save the player from death.
                        // Fill buckets with water.
                        // Wet sponges.
                        CommonTotemItem.defaultTotemBehavior(event, player, heldItem, MoreTotems.WATER_TOTEM.get(), true);

                        player.inventoryMenu.slots.forEach(slot -> {
                            // for every item in the player's inventory: check if bucket. check if empty. if true, fill with water.
                            if (slot.getItem().getItem() instanceof BucketItem bucket && bucket.getFluid() == Fluids.EMPTY)
                                slot.set(new ItemStack(Items.WATER_BUCKET));

                            // for every item in the player's inventory; check if sponge. check if dry. if true, make wet.
                            else if (slot.getItem().getItem() instanceof BlockItem block && block.getBlock() instanceof SpongeBlock)
                                slot.set(new ItemStack(Items.WET_SPONGE, slot.getItem().getCount()));
                        });
                    }
                    case "clock_totem" -> {
                        // Save the player from death.
                        // Move them to their respawn point.
                        // If the player doesn't have a respawn point, put them at the Overworld's spawnpoint
                        CommonTotemItem.defaultTotemBehavior(event, player, heldItem, MoreTotems.CLOCK_TOTEM.get(), true);

                        if (!world.isClientSide) {
                            if (player instanceof ServerPlayer serverPlayer) {
                                BlockPos spawnLoc = serverPlayer.getRespawnPosition();
                                ServerLevel overworld = ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD);
                                if (overworld == null)
                                    break;

                                if (spawnLoc == null)
                                    if (!(serverPlayer.getLevel().dimension() == Level.OVERWORLD))
                                        serverPlayer.teleportTo(overworld, overworld.getSharedSpawnPos().getX(), overworld.getSharedSpawnPos().getY(), overworld.getSharedSpawnPos().getZ(), overworld.getSharedSpawnAngle(), 0);
                                    else
                                        serverPlayer.setPos(overworld.getSharedSpawnPos().getX(), overworld.getSharedSpawnPos().getY(), overworld.getSharedSpawnPos().getZ());
                                else if (!(serverPlayer.getLevel().dimension() == serverPlayer.getRespawnDimension()))
                                    serverPlayer.teleportTo(ServerLifecycleHooks.getCurrentServer().getLevel(serverPlayer.getLevel().dimension()), spawnLoc.getX(), spawnLoc.getY(), spawnLoc.getZ(), 0, 0);
                                else
                                    serverPlayer.setPos(spawnLoc.getX(), spawnLoc.getY(), spawnLoc.getZ());
                            }
                        }
                    }
                }
            }
        }
    }
}