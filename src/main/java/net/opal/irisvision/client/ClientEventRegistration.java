package net.opal.irisvision.client;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.opal.irisvision.IrisVision;
import net.opal.iris.client.ClientKeyBindings;
import net.opal.iris.menu.IrisModuleOptionsRegistry;
import net.opal.irisvision.event.onBlockBreak;
import net.opal.irisvision.menu.IrisVisionOptionsMenu;
import net.opal.irisvision.network.ClientDataCache;
import net.opal.irisvision.recip.RecipeInventoryOverlay;
import net.opal.irisvision.tooltips.TooltipManager;

public final class ClientEventRegistration {
    private ClientEventRegistration() {}

    public static void register(IEventBus modEventBus) {
        IrisModuleOptionsRegistry.register("irisvision", Component.translatable("menu.iris.module.irisvision"), IrisVisionOptionsMenu::new);
        modEventBus.addListener(ClientEventRegistration::registerGuiLayers);
        NeoForge.EVENT_BUS.register(TooltipManager.class);
        ClientKeyBindings.setPinVisibilityHandler(net.opal.irisvision.recip.PinnedRecipeHud::toggleVisibility);
        NeoForge.EVENT_BUS.register(onBlockBreak.class);
        NeoForge.EVENT_BUS.register(ClientDataCache.class);
        NeoForge.EVENT_BUS.addListener(RecipeInventoryOverlay::onScreenInit);
        NeoForge.EVENT_BUS.addListener(RecipeInventoryOverlay::onScreenRender);
        NeoForge.EVENT_BUS.addListener(RecipeInventoryOverlay::onScreenRenderPre);
        NeoForge.EVENT_BUS.addListener(RecipeInventoryOverlay::onMouseClicked);
        NeoForge.EVENT_BUS.addListener(RecipeInventoryOverlay::onMouseReleased);
        NeoForge.EVENT_BUS.addListener(RecipeInventoryOverlay::onMouseScrolled);
        NeoForge.EVENT_BUS.addListener(RecipeInventoryOverlay::onKeyPressed);
        NeoForge.EVENT_BUS.addListener(RecipeInventoryOverlay::onCharacterTyped);
        NeoForge.EVENT_BUS.addListener(RecipeInventoryOverlay::onInventoryMobEffects);
    }

    private static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(IrisVision.MODID, "pinned_recipe"),
                (gui, delta) -> net.opal.irisvision.recip.PinnedRecipeHud.render(gui));
    }
}
