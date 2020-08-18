package org.dcache.hsm;

import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class GroupIml implements Group {

    private final Set<Request> requests = new HashSet<>();
    private final String name;
    private Instant lastUpdate = Instant.now();

    public GroupIml(String name) {
        this.name = name;
    }

    @Override
    public void add(Request request) {
        requests.add(request);
        lastUpdate = Instant.now();
    }

    @Override
    public Collection<Request> requests() {
        return Set.copyOf(requests);
    }

    @Override
    public int size() {
        return requests.size();
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Instant lastUpdate() {
        return lastUpdate;
    }

    @Override
    public int accumulatedCapacity() {
        return 200;
    }
}
