package uk.gemwire.moretotems;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ClientStuff {

    public static void particleStuff(LivingEntity entity) {
        Minecraft.getInstance().particleEngine.createTrackingEmitter(entity, ParticleTypes.TOTEM_OF_UNDYING, 30);
    }

    public static void totemActivated(Item animationItem) {
        Minecraft.getInstance().gameRenderer.displayItemActivation(new ItemStack(animationItem));
    }
}
