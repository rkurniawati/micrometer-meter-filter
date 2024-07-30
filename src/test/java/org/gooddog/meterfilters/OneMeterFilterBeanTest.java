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
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import({MeterGenerator.class, MeterRegistryConfig.class})
@ActiveProfiles({"deny-abe"})
@Slf4j
class OneMeterFilterBeanTest {

    @Autowired
    private MeterRegistry meterRegistry;

    @TestConfiguration
    public static class MeterConfiguration {
        @Bean
        // uncomment this to make abe.meter appear and the test pass
        // @Order(value = Ordered.HIGHEST_PRECEDENCE)
        public MeterFilter acceptAE_DenyFZ() {
            return AtoZFilter.builder()
                    .accept(Set.of(new Range('a', 'e')))
                    .deny(Set.of(new Range('f', 'z')))
                    .build();
        }
    }

    @Test
    void testAcceptDenyMeterFilter() {
        // generate a meter with id "abe.meter"
        meterRegistry.counter("abe.meter");

        // assert that the meterRegistry contains only meters whose names starting with [a-e]
        // and no meters with names starting with [f-z]
        List<String> acceptedMeters = meterRegistry.getMeters().stream().map(m -> m.getId().getName()).sorted().toList();
        log.info("Accepted meters: {}", acceptedMeters);
        assertThat(acceptedMeters).contains("abe.meter");
    }
}
