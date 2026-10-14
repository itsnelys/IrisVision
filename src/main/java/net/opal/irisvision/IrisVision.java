package net.opal.irisvision;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.opal.irisvision.client.ClientEventRegistration;
import net.opal.irisvision.network.NetworkHandler;
import net.opal.irisvision.network.ServerDataSender;

/** Tooltip and recipe-management module built on Iris's HUD and options base. */
@Mod(IrisVision.MODID)
public final class IrisVision {
    public static final String MODID = "irisvision";

    public IrisVision(IEventBus modEventBus) {
        modEventBus.register(NetworkHandler.class);
        NeoForge.EVENT_BUS.register(ServerDataSender.class);
        if (FMLEnvironment.dist == Dist.CLIENT) ClientEventRegistration.register(modEventBus);
    }
}
