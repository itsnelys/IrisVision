package net.opal.irisvision.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.opal.irisvision.IrisVision;

public class NetworkHandler {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        // On crée le registrar pour le mod
        final PayloadRegistrar registrar = event.registrar(IrisVision.MODID).versioned("1");

        registrar.playToClient(
                BlockDataPayload.TYPE,
                BlockDataPayload.CODEC,
                ClientDataCache::handleData
        );
    }
}
