package net.opal.irisvision.menu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.opal.iris.option.ConfigOptions;
import net.opal.iris.theme.UiTheme;

import java.util.ArrayList;
import java.util.List;

public final class IrisVisionOptionsMenu extends Screen {
    private static final int BUTTON_HEIGHT = 20;
    private static final int BASE_WIDTH = 200;
    private static final int BUTTON_GAP = 4;
    private static final int ROW_GAP = 5;
    private static final int SECTION_GAP = 12;
    private static final int CONTENT_TOP = 50;
    private static final int CONTENT_BOTTOM_PADDING = 52;

    private final Screen parent;
    private final List<PositionedWidget> contentWidgets = new ArrayList<>();
    private int scrollOffset;

    public IrisVisionOptionsMenu(Screen parent) {
        super(Component.translatable("menu.iris.module.irisvision"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        contentWidgets.clear();
        ConfigOptions config = ConfigOptions.getInstance();
        Layout layout = createLayout();
        int halfW = (layout.width - BUTTON_GAP) / 2;

        addContentWidget(Button.builder(getOverlayButtonText(config.enableBlockTooltipOverlay), b -> {
            config.enableBlockTooltipOverlay = !config.enableBlockTooltipOverlay;
            b.setMessage(getOverlayButtonText(config.enableBlockTooltipOverlay));
            config.save();
        }).tooltip(tooltip("menu.iris.tooltip.overlay")).pos(layout.left, layout.featuresButtonY).size(halfW, BUTTON_HEIGHT).build(), layout.left, layout.featuresButtonY, halfW);

        addContentWidget(new PositionSlider(layout.left + halfW + BUTTON_GAP, layout.featuresButtonY, halfW, BUTTON_HEIGHT, config), layout.left + halfW + BUTTON_GAP, layout.featuresButtonY, halfW);

        addContentWidget(Button.builder(getAdvancedButtonText(config.advancedTooltips), b -> {
            config.advancedTooltips = !config.advancedTooltips;
            b.setMessage(getAdvancedButtonText(config.advancedTooltips));
            config.save();
        }).tooltip(tooltip("menu.iris.tooltip.advanced_tooltips")).pos(layout.left, layout.featuresButtonY + layout.rowStep).size(halfW, BUTTON_HEIGHT).build(), layout.left, layout.featuresButtonY + layout.rowStep, halfW);

        addContentWidget(Button.builder(getLiquidAdvancedButtonText(config.advancedLiquidStats), b -> {
            config.advancedLiquidStats = !config.advancedLiquidStats;
            b.setMessage(getLiquidAdvancedButtonText(config.advancedLiquidStats));
            config.save();
        }).tooltip(tooltip("menu.iris.tooltip.advanced_liquids")).pos(layout.left + halfW + BUTTON_GAP, layout.featuresButtonY + layout.rowStep).size(halfW, BUTTON_HEIGHT).build(), layout.left + halfW + BUTTON_GAP, layout.featuresButtonY + layout.rowStep, halfW);

        addContentWidget(Button.builder(getCompactButtonText(config.compactMode), b -> {
            config.compactMode = !config.compactMode;
            b.setMessage(getCompactButtonText(config.compactMode));
            config.save();
        }).tooltip(tooltip("menu.iris.tooltip.compact")).pos(layout.left, layout.featuresButtonY + layout.rowStep * 2).size(layout.width, BUTTON_HEIGHT).build(), layout.left, layout.featuresButtonY + layout.rowStep * 2, layout.width);

        addProviderButtons(config, layout, halfW);
        addInventoryButtons(config, layout, halfW);

        this.addRenderableWidget(Button.builder(Component.translatable("menu.iris.return"), b -> {
            if (this.minecraft != null) this.minecraft.setScreen(parent);
        }).tooltip(tooltip("menu.iris.tooltip.return")).pos(layout.left, this.height - 30).size(layout.width, BUTTON_HEIGHT).build());

        clampScroll();
        updateContentPositions();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        UiTheme theme = UiTheme.getCurrent();
        guiGraphics.fill(0, 0, this.width, this.height, theme.gui_bgOverlay());

        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        guiGraphics.fill(0, 0, this.width, 35, theme.gui_barColor());
        guiGraphics.fill(0, 34, this.width, 35, theme.gui_lineColor());
        guiGraphics.drawCenteredString(this.font, Component.literal("§l").append(this.title), this.width / 2, 12, 0xFFFFFF);

        renderFooter(guiGraphics, theme);
        renderSectionsLayout(guiGraphics, theme);
        renderScrollbar(guiGraphics, theme);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        int previous = scrollOffset;
        scrollOffset = Mth.clamp(scrollOffset - (int) (scrollY * (BUTTON_HEIGHT + ROW_GAP)), 0, maxScroll());
        if (previous != scrollOffset) {
            updateContentPositions();
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    private void renderSectionsLayout(GuiGraphics guiGraphics, UiTheme theme) {
        Layout layout = createLayout();

        drawSection(guiGraphics, theme, "menu.irisvision.section.display", layout.left, layout.featuresTitleY - scrollOffset, layout.width);
        drawSection(guiGraphics, theme, "menu.iris.providers.section.tooltips", layout.left, layout.providersTitleY - scrollOffset, layout.width);
        drawSection(guiGraphics, theme, "menu.iris.providers.section.inventory", layout.left, layout.inventoryTitleY - scrollOffset, layout.width);
    }

    private void drawSection(GuiGraphics guiGraphics, UiTheme theme, String key, int x, int y, int width) {
        if (y + 14 < CONTENT_TOP || y > listBottom()) return;
        guiGraphics.drawString(this.font, section(key), x, y, 0xFFFFFF, true);
        int separatorY = y + 13;
        guiGraphics.fill(x, separatorY, x + width, separatorY + 1, theme.gui_separatorLine());
    }

    private void renderFooter(GuiGraphics g, UiTheme theme) {
        int footerY = this.height - 40;
        Layout layout = createLayout();
        int holeLeft = layout.left;
        int holeRight = layout.left + layout.width;

        g.fill(0, footerY, holeLeft, this.height, theme.gui_barColor());
        g.fill(0, footerY, holeLeft, footerY + 1, theme.gui_lineColor());
        g.fill(holeRight, footerY, this.width, this.height, theme.gui_barColor());
        g.fill(holeRight, footerY, this.width, footerY + 1, theme.gui_lineColor());

        g.fill(holeLeft, footerY, holeRight, this.height - 30, theme.gui_barColor());
        g.fill(holeLeft, footerY, holeRight, footerY + 1, theme.gui_lineColor());
        g.fill(holeLeft, this.height - 10, holeRight, this.height, theme.gui_barColor());
    }

    private Component getThemeButtonText(ConfigOptions config) {
        UiTheme theme = UiTheme.getCurrent();
        return Component.translatable("menu.iris.theme", colored(theme.gui_valColor(), Component.translatable(config.theme.translationKey())));
    }

    private Component getDebugButtonText(boolean on) {
        UiTheme theme = UiTheme.getCurrent();
        return Component.translatable("menu.iris.debug_chat", state(on, theme));
    }

    private Component getOverlayButtonText(boolean on) {
        UiTheme theme = UiTheme.getCurrent();
        return Component.translatable("menu.iris.overlay", state(on, theme));
    }

    private Component getCompactButtonText(boolean on) {
        UiTheme theme = UiTheme.getCurrent();
        return Component.translatable("menu.iris.compact", state(on, theme));
    }

    private Component getAdvancedButtonText(boolean on) {
        UiTheme theme = UiTheme.getCurrent();
        return Component.translatable("menu.iris.advanced_tooltips", state(on, theme));
    }

    private Component getLiquidAdvancedButtonText(boolean on) {
        UiTheme theme = UiTheme.getCurrent();
        return Component.translatable("menu.iris.advanced_liquids", state(on, theme));
    }

    private Component getEntityButtonText(boolean on) {
        UiTheme theme = UiTheme.getCurrent();
        return Component.translatable("menu.iris.entities", state(on, theme));
    }

    private Component getIndicatorGlobalButtonText(boolean on) {
        UiTheme theme = UiTheme.getCurrent();
        return Component.translatable("menu.iris.hud", state(on, theme));
    }

    private Component getIndicatorSideButtonText(ConfigOptions.IndicatorPosition position) {
        UiTheme theme = UiTheme.getCurrent();
        String key = position == ConfigOptions.IndicatorPosition.LEFT ? "menu.iris.left" : "menu.iris.right";
        return Component.translatable("menu.iris.side", colored(theme.gui_valColor(), Component.translatable(key)));
    }

    private void addProviderButtons(ConfigOptions config, Layout layout, int halfWidth) {
        addToggle("menu.iris.providers.blocks", "menu.iris.providers.tooltip.blocks", config.enableBlockProviderTooltips,
                enabled -> config.enableBlockProviderTooltips = enabled, layout.left, layout.providersButtonY, halfWidth, config);
        addToggle("menu.iris.providers.entities", "menu.iris.providers.tooltip.entities", config.enableEntityTooltip,
                enabled -> config.enableEntityTooltip = enabled, layout.left + halfWidth + BUTTON_GAP, layout.providersButtonY, halfWidth, config);
        addToggle("menu.iris.providers.drops", "menu.iris.providers.tooltip.drops", config.enableDropTooltip,
                enabled -> config.enableDropTooltip = enabled, layout.left, layout.providersButtonY + layout.rowStep, halfWidth, config);
        addToggle("menu.iris.providers.fluids", "menu.iris.providers.tooltip.fluids", config.enableFluidTooltips,
                enabled -> config.enableFluidTooltips = enabled, layout.left + halfWidth + BUTTON_GAP, layout.providersButtonY + layout.rowStep, halfWidth, config);
        addToggle("menu.iris.providers.inventories", "menu.iris.providers.tooltip.inventories", config.enableInventoryTooltips,
                enabled -> config.enableInventoryTooltips = enabled, layout.left, layout.providersButtonY + layout.rowStep * 2, layout.width, config);
    }

    private void addInventoryButtons(ConfigOptions config, Layout layout, int halfWidth) {
        addToggle("menu.iris.providers.recipes", "menu.iris.providers.tooltip.recipes", config.enableRecipeOverlay,
                enabled -> config.enableRecipeOverlay = enabled, layout.left, layout.inventoryButtonY, halfWidth, config);
        addToggle("menu.iris.inventory_search", "menu.iris.tooltip.inventory_search", config.inventorySearchHighlight,
                enabled -> config.inventorySearchHighlight = enabled, layout.left + halfWidth + BUTTON_GAP, layout.inventoryButtonY, halfWidth, config);
        addToggle("menu.iris.recipe_availability", "menu.iris.tooltip.recipe_availability", config.recipeAvailability,
                enabled -> config.recipeAvailability = enabled, layout.left, layout.inventoryButtonY + layout.rowStep, layout.width, config);
    }

    private void addToggle(String labelKey, String tooltipKey, boolean initialValue, java.util.function.Consumer<Boolean> setter,
                           int x, int y, int width, ConfigOptions config) {
        boolean[] enabled = {initialValue};
        addContentWidget(Button.builder(toggleText(labelKey, enabled[0]), button -> {
            enabled[0] = !enabled[0];
            setter.accept(enabled[0]);
            button.setMessage(toggleText(labelKey, enabled[0]));
            config.save();
        }).tooltip(tooltip(tooltipKey)).bounds(x, y, width, BUTTON_HEIGHT).build(), x, y, width);
    }

    private static Component toggleText(String key, boolean enabled) {
        return Component.translatable(key, state(enabled, UiTheme.getCurrent()));
    }

    private static Component section(String key) {
        return Component.literal("§8> §7").append(Component.translatable(key));
    }

    private static Tooltip tooltip(String key) {
        return Tooltip.create(Component.translatable(key));
    }

    private Layout createLayout() {
        int width = Math.min(BASE_WIDTH, Math.max(120, this.width - 36));
        int left = (this.width - width) / 2;
        int availableHeight = Math.max(1, this.height - CONTENT_TOP - CONTENT_BOTTOM_PADDING);
        int defaultRowStep = BUTTON_HEIGHT + ROW_GAP;
        int defaultContentHeight = 315;
        int rowStep = availableHeight < defaultContentHeight ? Math.max(BUTTON_HEIGHT + 1, (availableHeight - 45) / 8) : defaultRowStep;
        int featuresTitleY = CONTENT_TOP;
        int featuresButtonY = featuresTitleY + 15;
        int providersTitleY = featuresButtonY + rowStep * 3 + SECTION_GAP;
        int providersButtonY = providersTitleY + 15;
        int inventoryTitleY = providersButtonY + rowStep * 3 + SECTION_GAP;
        int inventoryButtonY = inventoryTitleY + 15;

        return new Layout(left, width, rowStep, featuresTitleY, featuresButtonY, providersTitleY, providersButtonY, inventoryTitleY, inventoryButtonY);
    }

    private static Component state(boolean on, UiTheme theme) {
        return colored(on ? theme.gui_onColor() : theme.gui_offColor(),
                Component.translatable(on ? "menu.iris.on" : "menu.iris.off"));
    }

    private static Component colored(String color, Component text) {
        return Component.literal(color).append(text);
    }

    private class PositionSlider extends AbstractSliderButton {
        private final ConfigOptions config;

        public PositionSlider(int x, int y, int width, int height, ConfigOptions config) {
            super(x, y, width, height, Component.empty(), config.tooltipPosition.legacyValue() / 4.0);
            this.config = config;
            setTooltip(tooltip("menu.iris.tooltip.position"));
            updateMessage();
        }

        @Override
        protected void updateMessage() {
            UiTheme theme = UiTheme.getCurrent();
            String key = switch(config.tooltipPosition) {
                case TOP_LEFT -> "menu.iris.position.top_left";
                case TOP_RIGHT -> "menu.iris.position.top_right";
                case BOTTOM_LEFT -> "menu.iris.position.bottom_left";
                case BOTTOM_RIGHT -> "menu.iris.position.bottom_right";
                case TOP_CENTER -> "menu.iris.position.top_center";
            };
            setMessage(Component.translatable("menu.iris.position", colored(theme.gui_valColor(), Component.translatable(key))));
        }

        @Override
        protected void applyValue() {
            int newValue = Mth.clamp((int)(this.value * 4.99), 0, 4);
            ConfigOptions.TooltipPosition newPosition = ConfigOptions.TooltipPosition.fromSlider(newValue);
            if (config.tooltipPosition != newPosition) {
                config.tooltipPosition = newPosition;
                config.save();
            }
        }
    }

    private <T extends AbstractWidget> T addContentWidget(T widget, int x, int y, int width) {
        contentWidgets.add(new PositionedWidget(widget, x, y, width));
        return this.addRenderableWidget(widget);
    }

    private void updateContentPositions() {
        int listBottom = listBottom();
        for (PositionedWidget positioned : contentWidgets) {
            AbstractWidget widget = positioned.widget;
            widget.setX(positioned.x);
            widget.setY(positioned.y - scrollOffset);
            widget.setWidth(positioned.width);

            boolean visible = widget.getY() >= CONTENT_TOP && widget.getY() + BUTTON_HEIGHT <= listBottom;
            widget.visible = visible;
            widget.active = visible;
        }
    }

    private void renderScrollbar(GuiGraphics guiGraphics, UiTheme theme) {
        if (maxScroll() <= 0) return;

        Layout layout = createLayout();
        int top = CONTENT_TOP;
        int bottom = listBottom();
        int barX = layout.left + layout.width + 8;
        int trackHeight = bottom - top;
        int thumbHeight = Math.max(18, (trackHeight * trackHeight) / Math.max(trackHeight, contentHeight()));
        int thumbY = top + (int) ((trackHeight - thumbHeight) * (scrollOffset / (float) maxScroll()));

        guiGraphics.fill(barX, top, barX + 2, bottom, theme.gui_separatorLine());
        guiGraphics.fill(barX - 1, thumbY, barX + 3, thumbY + thumbHeight, theme.gui_lineColor());
    }

    private void clampScroll() {
        scrollOffset = Mth.clamp(scrollOffset, 0, maxScroll());
    }

    private int maxScroll() {
        return Math.max(0, contentHeight() - (listBottom() - CONTENT_TOP));
    }

    private int contentHeight() {
        Layout layout = createLayout();
        return layout.inventoryButtonY + layout.rowStep + BUTTON_HEIGHT - CONTENT_TOP;
    }

    private int listBottom() {
        return this.height - CONTENT_BOTTOM_PADDING;
    }

    private record PositionedWidget(AbstractWidget widget, int x, int y, int width) {}

    private record Layout(
            int left,
            int width,
            int rowStep,
            int featuresTitleY,
            int featuresButtonY,
            int providersTitleY,
            int providersButtonY,
            int inventoryTitleY,
            int inventoryButtonY
    ) {}
}
