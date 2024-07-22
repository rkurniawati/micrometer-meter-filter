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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@Import({MeterGenerator.class, MeterRegistryConfig.class, TwoMeterConfiguration.class})
@ActiveProfiles("properties-filter-accept-all")
@Slf4j
public class TwoAcceptDenyMeterFiltersTest {

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private AtoZFilter acceptAE_DenyFZ;

    @Autowired
    private AtoZFilter acceptGI_DenyLO;

    @Test
    void testAcceptDenyMeterFilter() {
        // track if we've seen meters that are accepted
        AcceptedMeterTracker acceptedMeterTracker = AcceptedMeterTracker.createAcceptedMeterTracker()
                .addAcceptedRanges(acceptAE_DenyFZ.getAccept())
                .addAcceptedRanges(acceptGI_DenyLO.getAccept());

        // since the meters are neutral for [m-q] and we accept all meters in application.properties, they should be present
        acceptedMeterTracker.addAcceptedRange(new Range('m', 'q'));

        // assert that the meterRegistry contains only meters whose names start with [f-h] or [x-z] or [i-l]
        // and no meters whose names start with [a-e] or [r-v]
        meterRegistry.getMeters().stream().map(m -> m.getId().getName()).sorted().forEach(name -> {
            char c = name.charAt(0);
            if ((c >= 'f' && c <= 'h') || (c >= 'x' && c <= 'z') || (c >= 'i' && c <= 'l')) {
                acceptedMeterTracker.markAcceptedRange(c);
                return;
            }
            if (c >= 'a' && c <= 'e' || c >= 'r' && c <= 'v') {
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
