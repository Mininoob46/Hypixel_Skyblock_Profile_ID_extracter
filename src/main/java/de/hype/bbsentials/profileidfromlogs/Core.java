package de.hype.bbsentials.profileidfromlogs;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reactor.ReactorHttpClient;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

public class Core{
    private ExecutorService executorService;
    public HypixelAPI hypixelAPI;
    private final String hypixelAPIKey;
    MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
    public JsonObject getProfile(String profileId) throws ExecutionException, InterruptedException {
        return hypixelAPI.getSkyBlockProfile(profileId).get().getProfile();
    }


    public static String getMcUUIDbyUsername(String username) {
        try {
            String url = "https://api.mojang.com/users/profiles/minecraft/" + username;
            if (url.endsWith("null")) {
                throw new Exception("Invalid MC_UUID / Error Occurd (null)");
            }
            URL mojangAPI = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) mojangAPI.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
                return jsonObject.get("id").getAsString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // Return the input value if conversion fails
    }

    public static void openURLInBrowser(String url) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Opening a browser is not supported on this platform.");
        }
    }


    public Core(String hypixelAPIKey) {
        this.hypixelAPIKey = hypixelAPIKey;
        this.executorService = Executors.newFixedThreadPool(10);
        this.hypixelAPI = new HypixelAPI(new ReactorHttpClient(UUID.fromString(hypixelAPIKey)));
    }

    public Set<String> extractProfileIdsFromLogs(List<String> filePaths) throws InterruptedException, ExecutionException {
        Set<String> profileIds = new HashSet<>();
        //TODO implement this
//        List<Future<Set<String>>> futures = new ArrayList<>();
//        AtomicInteger activeThreads = new AtomicInteger(10);
//
//        for (String filePath : filePaths) {
//            while (heapMemoryUsage.getMax()*)
//            Future<Set<String>> future = executorService.submit(() -> {
//                Set<String> ids = new HashSet<>();
//                try (FileInputStream fileInputStream = new FileInputStream(filePath);
//                     GZIPInputStream gzipInputStream = new GZIPInputStream(fileInputStream);
//                     InputStreamReader inputStreamReader = new InputStreamReader(gzipInputStream);
//                     BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
//                    String line;
//                    while ((line = bufferedReader.readLine()) != null) {
//                        String profileId = extractProfileIdFromLine(line);
//                        if (profileId != null) {
//                            ids.add(profileId);
//                        }
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return ids;
//            });
//            futures.add(future);
//
//            // Check queue size and to dynamically adjust thread pool size
//            if (futures.size() >= QUEUE_THRESHOLD && activeThreads.get() < MAX_THREAD_POOL_SIZE) {
//                long memoryUsage = calculateMemoryUsage();
//                if (memoryUsage < MAX_MEMORY_USAGE) {
//                    int newThreads = Math.min(MAX_THREAD_POOL_SIZE - activeThreads.get(), QUEUE_THRESHOLD);
////                    executorService.submit(future);
//                    activeThreads.addAndGet(newThreads);
//                }
//            }
//        }
//
//        for (Future<Set<String>> future : futures) {
//            profileIds.addAll(future.get());
//        }

        return profileIds;
    }

    private String extractProfileIdFromLine(String line) {
        Pattern pattern = Pattern.compile("Profile ID: (\\S+)");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    // Method to calculate memory usage
    private long calculateMemoryUsage() {
        return ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed();
    }
}