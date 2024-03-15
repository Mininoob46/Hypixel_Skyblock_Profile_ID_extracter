package de.hype.bbsentials.profileidfromlogs;

import com.google.gson.JsonObject;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.exceptions.BadStatusCodeException;
import net.hypixel.api.reactor.ReactorHttpClient;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Core {
    private final String hypixelAPIKey;
    public HypixelAPI hypixelAPI;
    private ExecutorService executorService;
    private String mcuuid;

    public Core(String hypixelAPIKey, String mcusername) {
        this.hypixelAPIKey = hypixelAPIKey;
        this.executorService = Executors.newFixedThreadPool(10);
        this.hypixelAPI = new HypixelAPI(new ReactorHttpClient(UUID.fromString(hypixelAPIKey)));
        this.mcuuid = Utils.getMcUUIDbyUsername(mcusername);
    }

    public Profile getProfile(String profileId) {
        try {
            return new Profile(hypixelAPI.getSkyBlockProfile(profileId).get().getProfile(),profileId);
        } catch (Exception e) {
            if (!(e instanceof BadStatusCodeException) && !(e instanceof NullPointerException) && !(e.getCause() instanceof BadStatusCodeException))
                e.printStackTrace();
            return null;
        }
    }

    public String getMcuuid() {
        return mcuuid;
    }
}
