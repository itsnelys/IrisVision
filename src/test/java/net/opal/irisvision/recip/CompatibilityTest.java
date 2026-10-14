package net.opal.irisvision.recip;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.opal.irisvision.api.compat.IrisCompatibility;
import net.opal.irisvision.api.compat.RecipeIntegration;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CompatibilityTest {
    private RecipeIntegration adapter(String name) {
        return new RecipeIntegration() {
            public ResourceLocation id() { return ResourceLocation.fromNamespaceAndPath("test", name); }
            public Component title() { return Component.literal(name); }
            public boolean supportsScreen(AbstractContainerScreen<?> screen) { return false; }
        };
    }

    @Test void catalogOnlyDefaultsDenyTransfers() {
        var integration = adapter("catalog");
        assertFalse(integration.supportsRecipe(null, null));
        assertFalse(integration.canTransfer(null, null));
        assertFalse(integration.transfer(null, null, true));
        assertEquals("compat:test:catalog", IrisCompatibility.settingKey(integration));
    }

    @Test void rejectsDuplicateIdsAndReturnsImmutableSnapshot() {
        var integration = adapter("unique");
        IrisCompatibility.register(integration);
        assertThrows(IllegalArgumentException.class, () -> IrisCompatibility.register(adapter("unique")));
        assertThrows(UnsupportedOperationException.class, () -> IrisCompatibility.integrations().clear());
    }

    @Test void faultyCallbackFailsClosedForTheSession() {
        var integration = adapter("faulty");
        assertFalse(IrisCompatibility.isFailed(integration));
        assertFalse(IrisCompatibility.call(integration, () -> { throw new IllegalStateException("test failure"); }, false));
        assertTrue(IrisCompatibility.isFailed(integration));
        assertFalse(IrisCompatibility.call(integration, () -> true, false));
        assertTrue(IrisCompatibility.call(adapter("healthy"), () -> true, false));
    }
}
