package de.hype.bbsentials.profileidfromlogs;

import com.google.gson.JsonObject;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.util.Locale;

public class Profile {
    JsonObject data;
    String profileId;
    Instant creationDate;

    public Profile(JsonObject data, String profileId) {
        this.data = data;
        this.profileId = profileId;
        try {
            creationDate = Instant.ofEpochMilli(data.get("created_at").getAsLong());
        } catch (Exception e) {
            creationDate = null;
        }
    }

    public boolean isMember(String mcuuid) {
        try {
            return data.getAsJsonObject("members").get(mcuuid) != null;
        } catch (NullPointerException ignored) {
            return false;
        }
    }

    public boolean isBingoProfile() {
        try {
            return data.get("game_mode").getAsString().equals("bingo");
        } catch (NullPointerException ignored) {
            return false;
        }
    }

    public boolean isValid(String mcuuid) {
        return isMember(mcuuid) && isBingoProfile();
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public int getBingoId() {
        if (creationDate == null)
            return -1;
        ZonedDateTime time = creationDate.atZone(ZoneId.of("America/Chicago"));
        return ((time.getYear() - 2022) * 12) + time.getMonthValue();
    }

    public String getBingoDatingString() {
        if (creationDate == null) return "Unknown Date. Sorry";
        ZonedDateTime time = creationDate.atZone(ZoneId.of("America/Chicago"));
        return time.getMonth().getDisplayName(TextStyle.FULL, Locale.US) + " Bingo in " + time.getYear() + " AKA Bingo #" + (getBingoId() + 1) + " → API Bingo ID " + getBingoId();
    }

    public String getDisplayString() {
        return profileId + " : " + getBingoDatingString();
    }
}
