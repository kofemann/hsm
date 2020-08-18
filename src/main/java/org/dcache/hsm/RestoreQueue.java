package org.dcache.hsm;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HSM restore queue
 *
 */
public class RestoreQueue {

    /**
     * Number of tape drives concurrently available to the dCache.
     */
    // FIXME: this must be configurable
    private final int nDrives = 12;

    /**
     * maximal time allowed for request to be in a queue
     */
    // FIXME: make this configurable
    private final Duration maxQueueTime = Duration.of(2, ChronoUnit.HOURS);

    private final Map<String, Group> allRequests = new HashMap<>();

    /**
     * Maximal time before group declared closed.
     */
    public final Duration maxIdleTime = Duration.of(2, ChronoUnit.HOURS);

    /**
     * A virtual tape library
     */
    private final Library tapeLibrary = new Library();
    

    public synchronized void add(Request request) {
        String volume = getVolume(request);
        Group group = allRequests.computeIfAbsent(volume, GroupIml::new);
        group.add(request);
    }

    private String getVolume(Request request) {
        return tapeLibrary.volumeOf(request);
    }

    private synchronized void runExpiary() {

        Instant now = Instant.now();
        List<String> readyToStage = new ArrayList<>();

        for (Map.Entry<String, Group> request : allRequests.entrySet()) {

            Group group = request.getValue();
            if (group.accumulatedCapacity() == 100 && group.lastUpdate().isBefore(now.minus(maxQueueTime))) {
                readyToStage.add(request.getKey());
            }
        }

        for(String volume: readyToStage) {
            Collection<Request> requests = allRequests.remove(volume).requests();
        }

    }

}
