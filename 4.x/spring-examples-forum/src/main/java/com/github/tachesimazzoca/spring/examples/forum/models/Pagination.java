package com.github.tachesimazzoca.spring.examples.forum.models;

import java.util.Collections;

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

    public interface SelectFunction<T> {
        public Iterable<T> apply(int theOffset, int theLimit);
    }

    public interface CountFunction {
        public Long apply();
    }

    public static <A> Pagination<A> paginate(int anOffset, int aLimit,
                                  CountFunction countF, SelectFunction<A> selectF) {
        Long theCount = countF.apply();
        if (null == theCount || 0L == theCount) {
            return new Pagination<A>(Collections.emptyList(), 0, aLimit, 0);
        }

        int first = anOffset;
        if (first >= theCount) {
            if (theCount > 0)
                first = (int) ((theCount - 1) / aLimit) * aLimit;
            else
                first = 0;
        }
        return new Pagination<A>(selectF.apply(first, aLimit), first, aLimit, theCount);
    }
}
