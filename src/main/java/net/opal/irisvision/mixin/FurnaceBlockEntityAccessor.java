package net.opal.irisvision.mixin;

import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractFurnaceBlockEntity.class)
public interface FurnaceBlockEntityAccessor {
    @Accessor("litTime")
    int iris$getLitTimeRemaining();

    @Accessor("litDuration")
    int iris$getLitTotalTime();

    @Accessor("cookingProgress")
    int iris$getCookingTimer();

    @Accessor("cookingTotalTime")
    int iris$getCookingTotalTime();
}
