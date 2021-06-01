package io.github.apace100.apoli.power;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.function.Consumer;

public class ModifyJumpPower extends ValueModifyingPower {

    private final Consumer<Entity> entityAction;

    public ModifyJumpPower(PowerType<?> type, LivingEntity entity, Consumer<Entity> entityAction) {
        super(type, entity);
        this.entityAction = entityAction;
    }

    public void executeAction() {
        if(entityAction != null) {
            entityAction.accept(entity);
        }
    }
}
