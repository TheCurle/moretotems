package uk.gemwire.moretotems.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.entity.Entity;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;

/**
 * A custom Loot Table condition.
 * Checks for the presence of a substring in the name of an entity.
 *
 * Like EntityHasProperty, can accept:
 *   this
 *   killer
 *   direct_killer
 *   killer_player
 * as the entity input.
 *
 * The "name" parameter is checked as a substring inisde the name of the specified entity.
 * If the specified entity is null, the check fails.
 * If the entity is not named, the check fails.
 *
 * @author Curle
 */
public class EntityNameCondition implements ILootCondition {

    public static final LootConditionType NAME_CONTAINS = new LootConditionType(new EntityNameCondition.Serializer());

    private final String nameSubstring;
    private final LootContext.EntityTarget entityTarget;

    private EntityNameCondition(LootContext.EntityTarget entity, final String substr) {
        this.nameSubstring = substr;
        this.entityTarget = entity;
    }

    @Override
    public LootConditionType getType() {
        return NAME_CONTAINS;
    }

    @Override
    public boolean test(LootContext lootContext) {
        Entity lootEntity = lootContext.getParamOrNull(this.entityTarget.getParam());
        if(lootEntity == null)
            return false;

        if(!lootEntity.hasCustomName())
            return false;

        return lootEntity.getCustomName().getContents().contains(this.nameSubstring);

    }

    public static class Serializer implements ILootSerializer<EntityNameCondition> {

        @Override
        public void serialize(JsonObject object, EntityNameCondition condition, JsonSerializationContext context) {
            object.add("string", context.serialize(condition.nameSubstring));
            object.add("entity", context.serialize(condition.entityTarget));
        }

        @Override
        public EntityNameCondition deserialize(JsonObject object, JsonDeserializationContext context) {
            LootContext.EntityTarget target = JSONUtils.getAsObject(object, "entity", context, LootContext.EntityTarget.class);
            String substr = JSONUtils.getAsString(object, "string", "null");

            return new EntityNameCondition(target, substr);
        }
    }
}
