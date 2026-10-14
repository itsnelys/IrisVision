package net.opal.irisvision.tooltips.providers.specific;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.opal.irisvision.api.IBlockAccessor;
import net.opal.irisvision.api.IEntityTooltipProvider;

import java.util.List;

public class EndCrystalProvider implements IEntityTooltipProvider {
    @Override
    public boolean isApplicable(Entity entity) {
        return entity instanceof EndCrystal;
    }

    @Override
    public void addTooltip(List<String> tooltip, Entity entity, IBlockAccessor accessor) {
        EndCrystal crystal = (EndCrystal) entity;

        // Cible du rayon (BeamTarget)
        BlockPos beamTarget = crystal.getBeamTarget();
    }
}