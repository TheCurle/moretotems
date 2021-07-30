package uk.gemwire.moretotems.loot;

import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;

public class TotemLootModifier extends LootModifier {
    private final Item totem;

    protected TotemLootModifier(ILootCondition[] conditionsIn, Item totem) {
        super(conditionsIn);
        this.totem = totem;
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        generatedLoot.add(new ItemStack(totem));

        return generatedLoot;
    }

    private static class Serializer extends GlobalLootModifierSerializer<TotemLootModifier> {

        @Override
        public TotemLootModifier read(ResourceLocation location, JsonObject object, ILootCondition[] ailootcondition) {
            Item totem = ForgeRegistries.ITEMS.getValue(new ResourceLocation((JSONUtils.getAsString(object, "totem"))));
            return new TotemLootModifier(ailootcondition, totem);
        }

        @Override
        public JsonObject write(TotemLootModifier instance) {
            return null;
        }
    }
}
