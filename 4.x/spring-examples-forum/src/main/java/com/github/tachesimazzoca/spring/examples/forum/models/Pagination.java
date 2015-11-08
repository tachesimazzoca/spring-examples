package com.github.tachesimazzoca.spring.examples.forum.models;

public class Pagination<T> {
    private final Iterable<T> results;
    private final int offset;
    private final int limit;
    private final long count;

    public Pagination(Iterable<T> results, int offset, int limit, long count) {
        this.results = results;
        this.offset = offset;
        this.limit = limit;
        this.count = count;
    }

    public Iterable<T> getResults() {
        return results;
    }

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

    public long getCount() {
        return count;
    }
}
