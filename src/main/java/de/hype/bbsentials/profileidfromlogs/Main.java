package de.hype.bbsentials.profileidfromlogs;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static de.hype.bbsentials.profileidfromlogs.Utils.*;

public class Main {
    public static void main(String[] args) {
        ProfileIDExporter exporter = new ProfileIDExporter();
        Core core = new Core(args[0], "Hype_the_Time");
        //TODO add this to the guis;
        Instant startingTime = Instant.now();
        System.out.println("Starting Log Search");
        List<String> files = searchUserLogs(getUserHome());
        System.out.println("Looking for logs took: " + (Instant.now().toEpochMilli() - startingTime.toEpochMilli()) + "ms");
        Set<String> profileIds = extractProfileIdsFromLogs(files, true);
        System.out.println("All Keys: \n" + String.join("\n", profileIds));
        System.out.println("Looking for profile ids in the logs took: " + (Instant.now().toEpochMilli() - startingTime.toEpochMilli()) + "ms");
        System.out.println("Starting profile id verification");
        List<String> profileIdList = profileIds.stream().toList();
        int listSize = profileIdList.size();
        List<String> profileStringDisplay = new ArrayList<>();
        String mcuuid = core.getMcuuid();
        for (int i = 0; i < profileIdList.size(); i++) {
            String profileId = profileIdList.get(i);
            System.out.println("Verifying Profile id: " + profileId + " → " + (i + 1) + "/" + listSize);
            Profile prof = core.getProfile(profileId);
            if (prof != null && prof.isValid(mcuuid)) {
                profileStringDisplay.add(prof.getDisplayString());
            }
        }
        System.out.println("Everything combined took: " + (Instant.now().toEpochMilli() - startingTime.toEpochMilli()) + "ms");
        System.out.println("Result: \n" + String.join("\n", profileStringDisplay));
    }
}

