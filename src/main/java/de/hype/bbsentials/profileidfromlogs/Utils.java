package de.hype.bbsentials.profileidfromlogs;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

public class Utils{
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
        }
        else {
            System.out.println("Opening a browser is not supported on this platform.");
        }
    }

    private static Set<String> getIdsFromFile(String filePath) {
        Set<String> ids = new HashSet<>();
        if (filePath.endsWith(".log.gz")) {
            try (FileInputStream fileInputStream = new FileInputStream(filePath);
                 GZIPInputStream gzipInputStream = new GZIPInputStream(fileInputStream);
                 InputStreamReader inputStreamReader = new InputStreamReader(gzipInputStream);
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    String profileId = extractProfileIdFromLine(line);
                    if (profileId != null) {
                        ids.add(profileId);
                    }
                }
                if (ids.isEmpty()) return ids;
                System.out.println("File complete: " + filePath + " | Keys: " + String.join(";", ids));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    String profileId = extractProfileIdFromLine(line);
                    if (profileId != null) {
                        ids.add(profileId);
                    }
                }
                if (ids.isEmpty()) return ids;
                System.out.println("File complete: " + filePath + " | Keys: " + String.join(";", ids));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ids;
    }

    private static String extractProfileIdFromLine(String line) {
        Pattern pattern = Pattern.compile("Profile ID: ([\\w-]+)");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static Set<String> extractProfileIdsFromLogs(java.util.List<String> filePaths, boolean parallel) {
        Set<String> profileIds;
        if (parallel) {
            profileIds = Collections.synchronizedSet(new HashSet<>());
            filePaths.parallelStream().forEach(path -> profileIds.addAll(getIdsFromFile(path)));
        }
        else {
            profileIds = new HashSet<>();
        }
        //TODO implement this
//        List<Future<Set<String>>> futures = new ArrayList<>();
//        AtomicInteger activeThreads = new AtomicInteger(10);
//
//        for (String filePath : filePaths) {
//            while (heapMemoryUsage.getMax()*)
//            Future<Set<String>> future = executorService.submit(() -> {
//
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

    public static File getUserHome() {
        return new File(System.getProperty("user.home"));
    }

    public static java.util.List<String> searchUserLogs(File folder) {
        java.util.List<String> files = searchLogsRecursive(folder);
        System.out.println("Found a total of " + files.size() + " Potential logs");
        return files;
    }

    private static java.util.List<String> searchLogsRecursive(File directory) {
        List<String> filesList = new ArrayList<>();
        try {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        filesList.addAll(searchLogsRecursive(file));
                    }
                    else {
                        try {
                            String fileName = file.getName();
                            if (fileName.endsWith(".log.gz")) {
                                filesList.add(file.getAbsolutePath());
                            }
                            else if (fileName.endsWith(".log")) {
                                filesList.add(file.getAbsolutePath());
                            }
                        } catch (SecurityException e) {
                            // Ignore exception related to insufficient privileges
                            // or any other security issues
                        } catch (Exception e) {
                            // Handle other exceptions if necessary
                        }
                    }
                }
            }
        } catch (SecurityException e) {
            // Ignore exception related to insufficient privileges
            // or any other security issues
        } catch (Exception e) {
            // Handle other exceptions if necessary
        }
        return filesList;
    }
}
