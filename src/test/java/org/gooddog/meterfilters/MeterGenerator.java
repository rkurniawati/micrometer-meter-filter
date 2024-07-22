package org.gooddog.meterfilters;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Component
public class MeterGenerator {

    private final MeterRegistry meterRegistry;

    public MeterGenerator(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    public void generateMeter() {
        // generate meter with id "a.meter", "b.meter", "c.meter" ... "z.meter"
        // generate a meter with id
        IntStream.rangeClosed('a', 'z')
                .mapToObj(i -> String.valueOf((char) i) + ".meter")
                .forEach(meterRegistry::counter);
    }
}
