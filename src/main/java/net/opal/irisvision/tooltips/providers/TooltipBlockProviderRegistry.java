package net.opal.irisvision.tooltips.providers;

import net.opal.irisvision.api.IBlockTooltipProvider;
import net.opal.irisvision.tooltips.modded.MeltingSupportTooltipProvider;
import net.opal.irisvision.tooltips.providers.fluid.FluidTooltipProvider;
import net.opal.irisvision.tooltips.providers.generic.*;
import net.opal.irisvision.tooltips.providers.specific.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TooltipBlockProviderRegistry {

    // On utilise CopyOnWriteArrayList pour que les autres mods puissent
    // s'enregistrer en toute sécurité même pendant le chargement
    private static final List<IBlockTooltipProvider> PROVIDERS = new CopyOnWriteArrayList<>();

    static {
        // --- Enregistrement des modules Vanillas ---
        register(new CopperTooltipProvider());
        register(new BeehiveTooltipProvider());
        register(new BeaconTooltipProvider());
        register(new SignTooltipProvider());
        register(new RedstoneTooltipProvider());
        register(new EnderFrameTooltipProvider());
        register(new AgricultureTooltipProvider());
        register(new EnchantmentTooltipProvider());
        register(new ChiseledBookshelfTooltipProvider());
        register(new InteractionTooltipProvider());
        register(new NoteblockTooltipProvider());
        register(new FluidTooltipProvider());
        register(new JukeboxTooltipProvider());
        register(new EnderChestTooltipProvider());
        register(new InventoryTooltipProvider());
        register(new HopperTooltipProvider());
        register(new LecternTooltipProvider());
        register(new DecoratedPotTooltipProvider());
        register(new CommandBlockTooltipProvider());
        register(new StructureBlockTooltipProvider());
        register(new PlayerHeadTooltipProvider());
        register(new SpawnerTooltipProvider());
        register(new JigsawTooltipProvider());
        register(new ComposterTooltipProvider());

        // --- Enregistrement des modules Moddés (Tes propres ajouts) ---
        register(new MeltingSupportTooltipProvider());
    }

    /**
     * Méthode publique pour que n'importe quel moddeur puisse ajouter son provider.
     * Exemple : IrisRegistries.registerTooltipProvider(new MyModTooltip());
     */
    public static void register(IBlockTooltipProvider provider) {
        if (provider != null) {
            PROVIDERS.add(provider);
        }
    }

    public static List<IBlockTooltipProvider> getProviders() {
        return PROVIDERS;
    }
}
