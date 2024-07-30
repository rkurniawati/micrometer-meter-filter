package org.gooddog.meterfilters;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.gooddog.meterfilters.filter.AtoZFilter;
import org.gooddog.meterfilters.filter.Range;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@Import({MeterGenerator.class, MeterRegistryConfig.class, TwoMeterFilterWithCustomizerConfiguration.class})
@ActiveProfiles({"properties-filter-accept-all", "two-meter-filter-with-customizer"})
@Slf4j
public class TwoMeterFilterWithCustomizerTest {

    @Autowired
    private MeterRegistry meterRegistry;

    @Test
    void testAcceptDenyMeterFilter() {
        // track if we've seen meters that are accepted
        AcceptedMeterTracker acceptedMeterTracker = AcceptedMeterTracker.createAcceptedMeterTracker()
                .addAcceptedRanges(Set.of(new Range('a', 'e'), new Range('i', 'o'), new Range('e', 'h')));

        // since the meters are neutral for [m-q] and we accept all meters in application.properties, they should be present
        acceptedMeterTracker.addAcceptedRange(new Range('p', 'q'));

        // assert that the meterRegistry contains only meters whose names start with [f-h] or [x-z] or [i-l]
        // and no meters whose names start with [a-e] or [r-v]
        meterRegistry.getMeters().stream().map(m -> m.getId().getName()).sorted().forEach(name -> {
            char c = name.charAt(0);
            if ((c >= 'a' && c <= 'e') || (c >= 'i' && c <= 'o') || (c >= 'e' && c <= 'h')) {
                acceptedMeterTracker.markAcceptedRange(c);
                return;
            }
            if (c >= 'w' && c <= 'z' || c >= 'r' && c <= 'v') {
                fail("Meter with name " + name + " should not be present");
            }

            // we are neutral on these metric, so they will be accepted
            acceptedMeterTracker.markAcceptedRange(c);
        });

        if (!acceptedMeterTracker.isAllAcceptedMetersSeen()) {
            log.info("Accepted meters not seen: {}", acceptedMeterTracker.rangesWithoutAcceptedMeters());
            fail("Not all accepted meters are seen");
        }
    }

}
