package ca.fxco.unsigner;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Unsigner implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("unsigner");

    @Override
    public void onInitialize() {
        LOGGER.info("Unsigner has been loaded, all chat messages will be unsigned!");
    }
}
