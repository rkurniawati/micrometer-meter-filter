package org.gooddog.meterfilters;

import lombok.extern.slf4j.Slf4j;
import org.gooddog.meterfilters.filter.Range;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class AcceptedMeterTracker {
    // map of a range and a flag if we've seen a character in that range
    private final Map<Range, Boolean> acceptedMeters;

    public AcceptedMeterTracker() {
        this.acceptedMeters =  new HashMap<>();
    }

    public AcceptedMeterTracker addAcceptedRange(Range range) {
        acceptedMeters.put(range, false);
        return this;
    }

    public AcceptedMeterTracker addAcceptedRanges(Set<Range> ranges) {
        ranges.forEach(this::addAcceptedRange);
        return this;
    }

    public static AcceptedMeterTracker createAcceptedMeterTracker(Set<Range> acceptedRanges) {
        return new AcceptedMeterTracker().addAcceptedRanges(acceptedRanges);
    }

    public static AcceptedMeterTracker createAcceptedMeterTracker() {
        return new AcceptedMeterTracker();
    }

    // mark all ranges that contain the character as accepted
    public void markAcceptedRange(char c) {
        acceptedMeters.entrySet().stream()
                .filter(entry -> entry.getKey().contains(c))
                .forEach(entry -> acceptedMeters.put(entry.getKey(), true));
    }

    public Set<Range> rangesWithoutAcceptedMeters() {
        return acceptedMeters.entrySet().stream()
                .filter(entry -> !entry.getValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }
    public boolean isAllAcceptedMetersSeen() {
        return acceptedMeters.values().stream().allMatch(seen -> seen);
    }
}
