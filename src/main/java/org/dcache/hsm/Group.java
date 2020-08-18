package org.dcache.hsm;

import java.time.Instant;
import java.util.Collection;

/**
 * Represents file group, typically 1-to-1 with tape cartridge.
 */
public interface Group {

    void add(Request request);

    Collection<Request> requests();

    int size();

    String name();

    Instant lastUpdate();

    int accumulatedCapacity();
}
