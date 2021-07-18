package samebutdifferent.mobtotems;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import samebutdifferent.mobtotems.event.ModEvents;
import samebutdifferent.mobtotems.init.ModItems;

@Mod(MobTotems.MOD_ID)
public class MobTotems {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "mobtotems";

    public static final ItemGroup GROUP = new ItemGroup(MobTotems.MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Items.TOTEM_OF_UNDYING);
        }
    };

    public MobTotems() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.ITEMS.register(bus);
        MinecraftForge.EVENT_BUS.register(new ModEvents());

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
    }
}
