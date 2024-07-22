package org.gooddog.meterfilters.filter;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.config.MeterFilterReply;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.Set;

@Builder
@Getter
public class AtoZFilter implements MeterFilter {

    private boolean setOfRangesContains(Set<Range> set, char c) {
        return set.stream().anyMatch(range -> range.contains(c));
    }

    private final Set<Range> accept;
    private final Set<Range> deny;

    public AtoZFilter(Set<Range> accept, Set<Range> deny) {
        this.accept = accept;
        this.deny = deny;
    }

    @Override
    public @NonNull MeterFilterReply accept(Meter.Id id) {
        // decide based on the lower case of the first character of the meter id
        char c = id.getName().charAt(0);
        if (setOfRangesContains(deny, c)) {
            return MeterFilterReply.DENY;
        } else if (setOfRangesContains(accept, c)) {
            return MeterFilterReply.ACCEPT;
        }
        return MeterFilterReply.NEUTRAL;
    }

    @Override
    public String toString() {
        return "AtoZFilter{accept=" + accept + ", deny=" + deny + "}";
    }
}
