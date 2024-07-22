package org.gooddog.meterfilters.filter;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Range {
    private final char start;
    private final char end; // inclusive

    public Range(char start, char end) {
        this.start = start;
        this.end = end;
    }

    public boolean contains(char c) {
        return c >= start && c <= end;
    }

    @Override
    public String toString() {
        return "[" + start + "," + end + "]";
    }
}
