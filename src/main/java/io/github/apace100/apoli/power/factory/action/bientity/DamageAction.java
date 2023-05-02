package io.github.apace100.apoli.power.factory.action.bientity;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.util.modifier.Modifier;
import io.github.apace100.apoli.util.modifier.ModifierUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.util.Pair;

import java.util.LinkedList;
import java.util.List;

public class DamageAction {

    public static void action(SerializableData.Instance data, Pair<Entity, Entity> actorAndTarget) {

        Entity target = actorAndTarget.getRight();
        Float damageAmount = data.get("amount");
        List<Modifier> modifiers = new LinkedList<>();

        data.<Modifier>ifPresent("modifier", modifiers::add);
        data.<List<Modifier>>ifPresent("modifiers", modifiers::addAll);

        if (!modifiers.isEmpty() && target instanceof LivingEntity livingTarget) {

            float maxHealth = livingTarget.getMaxHealth();
            float newDamageAmount = (float) ModifierUtil.applyModifiers(livingTarget, modifiers, maxHealth);

            if (newDamageAmount > maxHealth) damageAmount = newDamageAmount - maxHealth;
            else damageAmount = newDamageAmount;

        }

        DamageSource damageSource = data.get("source");
        EntityDamageSource entityDamageSource = new EntityDamageSource(damageSource.getName(), actorAndTarget.getLeft());

        if (damageSource.isExplosive()) entityDamageSource.setExplosive();
        if (damageSource.isProjectile()) entityDamageSource.setProjectile();
        if (damageSource.isFromFalling()) entityDamageSource.setFromFalling();
        if (damageSource.isMagic()) entityDamageSource.setUsesMagic();
        if (damageSource.isNeutral()) entityDamageSource.setNeutral();

        if (damageAmount != null) target.damage(entityDamageSource, damageAmount);

    }

    public static ActionFactory<Pair<Entity, Entity>> getFactory() {
        return new ActionFactory<>(
            Apoli.identifier("damage"),
            new SerializableData()
                .add("amount", SerializableDataTypes.FLOAT, null)
                .add("source", SerializableDataTypes.DAMAGE_SOURCE)
                .add("modifier", Modifier.DATA_TYPE, null)
                .add("modifiers", Modifier.LIST_TYPE, null),
            DamageAction::action
        );
    }
}
