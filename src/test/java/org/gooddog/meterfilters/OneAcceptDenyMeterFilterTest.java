package org.gooddog.meterfilters;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import lombok.extern.slf4j.Slf4j;
import org.gooddog.meterfilters.filter.AtoZFilter;
import org.gooddog.meterfilters.filter.Range;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.Set;

@SpringBootTest
@Import({MeterGenerator.class, MeterRegistryConfig.class})
@Slf4j
class OneAcceptDenyMeterFilterTest {

    @Autowired
    private MeterRegistry meterRegistry;

    @TestConfiguration
    public static class MeterConfiguration {
        @Bean
        public MeterFilter acceptAE_DenyFZ() {
            return AtoZFilter.builder()
                    .accept(Set.of(new Range('a', 'e')))
                    .deny(Set.of(new Range('f', 'z')))
                    .build();
        }
    }

    @Test
    void testAcceptDenyMeterFilter() {
        // assert that the meterRegistry contains only meters whose names starting with [a-e]
        // and no meters with names starting with [f-z]
        meterRegistry.getMeters().stream().map(m -> m.getId().getName()).forEach(name -> {
            char c = name.charAt(0);
            if (c >= 'a' && c <= 'e') {
                return;
            }
            throw new AssertionError("Meter with name " + name + " should not be present");
        });
    }
}
