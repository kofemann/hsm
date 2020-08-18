package org.dcache.hsm;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 */
public class Library {

    private final static int MAX = 100;

    private List<String> db = IntStream.range(0, MAX)
            .mapToObj(Integer::toString)
            .collect(Collectors.toList());



    public String volumeOf(Request request) {
        return db.get(ThreadLocalRandom.current().nextInt(MAX));
    }


}
