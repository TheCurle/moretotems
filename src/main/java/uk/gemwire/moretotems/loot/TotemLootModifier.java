package uk.gemwire.moretotems.loot;


import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class TotemLootModifier extends LootModifier {
    private final Item totem;

    protected TotemLootModifier(LootItemCondition[] conditionsIn, Item totem) {
        super(conditionsIn);
        this.totem = totem;
    }

    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        generatedLoot.add(new ItemStack(totem));

        return generatedLoot;
    }

    private static class Serializer extends GlobalLootModifierSerializer<TotemLootModifier> {

        @Override
        public TotemLootModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] ailootcondition) {
            Item totem = ForgeRegistries.ITEMS.getValue(new ResourceLocation((GsonHelper.getAsString(object, "totem"))));
            return new TotemLootModifier(ailootcondition, totem);
        }

        @Override
        public JsonObject write(TotemLootModifier instance) {
            return null;
        }
    }
}
