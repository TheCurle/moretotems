package uk.gemwire.moretotems.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import uk.gemwire.moretotems.MoreTotems;

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
public class EntityNameCondition implements LootItemCondition {

    private final String nameSubstring;
    private final LootContext.EntityTarget entityTarget;

    private EntityNameCondition(LootContext.EntityTarget entity, final String substr) {
        this.nameSubstring = substr;
        this.entityTarget = entity;
    }

    @Override
    public LootItemConditionType getType() {
        return MoreTotems.NAME_CONTAINS_CONDITION.get();
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

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<EntityNameCondition> {

        @Override
        public void serialize(JsonObject object, EntityNameCondition condition, JsonSerializationContext context) {
            object.add("string", context.serialize(condition.nameSubstring));
            object.add("entity", context.serialize(condition.entityTarget));
        }

        @Override
        public EntityNameCondition deserialize(JsonObject object, JsonDeserializationContext context) {
            LootContext.EntityTarget target = GsonHelper.getAsObject(object, "entity", context, LootContext.EntityTarget.class);
            String substr = GsonHelper.getAsString(object, "string", "null");

            return new EntityNameCondition(target, substr);
        }
    }
}
