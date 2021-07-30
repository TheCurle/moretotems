package uk.gemwire.moretotems;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
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
import uk.gemwire.moretotems.item.CommonTotemItem;
import uk.gemwire.moretotems.loot.EntityNameCondition;

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

    public static final RegistryObject<Item> ENDER_TOTEM = ITEMS.register("ender_totem", CommonTotemItem::new);
    public static final RegistryObject<Item> CAT_TOTEM = ITEMS.register("cat_totem", CommonTotemItem::new);
    public static final RegistryObject<Item> WOLF_TOTEM = ITEMS.register("wolf_totem", CommonTotemItem::new);
    public static final RegistryObject<Item> IRON_GOLEM_TOTEM = ITEMS.register("iron_golem_totem", CommonTotemItem::new);
    public static final RegistryObject<Item> CREEPER_TOTEM = ITEMS.register("creeper_totem", CommonTotemItem::new);
    public static final RegistryObject<Item> AXOLOTL_TOTEM = ITEMS.register("axolotl_totem", CommonTotemItem::new);
    public static final RegistryObject<Item> SHULKER_TOTEM = ITEMS.register("shulker_totem", CommonTotemItem::new);

    public static final RegistryObject<Item> DYE_TOTEM = ITEMS.register("dye_totem", CommonTotemItem::new);
    public static final RegistryObject<Item> BREAD_TOTEM = ITEMS.register("bread_totem", CommonTotemItem::new);
    public static final RegistryObject<Item> WATER_TOTEM = ITEMS.register("water_totem", CommonTotemItem::new);
    public static final RegistryObject<Item> CLOCK_TOTEM = ITEMS.register("clock_totem", CommonTotemItem::new);

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

        // Register the our custom loot condition
        Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation("moretotems:name_contains"), EntityNameCondition.NAME_CONTAINS);
    }
}
