package de.hype.bbsentials.profileidfromlogs;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Main {
    public static void main(String[] args) {
        ProfileIDExporter exporter = new ProfileIDExporter();
        while(true){
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignored) {

            }
        }
    }
}

