package uk.gemwire.moretotems;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.gemwire.moretotems.event.ModEvents;
import uk.gemwire.moretotems.item.TotemItem;

/**
 * Credit to SameButDifferent for the original MobTotems mod.
 *
 * Rewritten & Maintained by Curle
 */
@Mod(MoreTotems.MOD_ID)
public class MoreTotems {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "moretotems";

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MoreTotems.MOD_ID);

    public static final RegistryObject<Item> TOTEM_OF_ESCAPING = ITEMS.register("totem_of_escaping", TotemItem::new);

    public static final ItemGroup GROUP = new ItemGroup(MoreTotems.MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Items.TOTEM_OF_UNDYING);
        }
    };

    public MoreTotems() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(bus);
        MinecraftForge.EVENT_BUS.register(new ModEvents());
        MinecraftForge.EVENT_BUS.register(this);
    }
}
