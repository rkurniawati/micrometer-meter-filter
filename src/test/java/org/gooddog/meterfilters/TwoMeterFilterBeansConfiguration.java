package org.gooddog.meterfilters;

import io.micrometer.core.instrument.config.MeterFilter;
import org.gooddog.meterfilters.filter.AtoZFilter;
import org.gooddog.meterfilters.filter.Range;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.util.Set;

@Configuration
@Profile("two-meter-filter-beans")
public class TwoMeterFilterBeansConfiguration {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public MeterFilter filter1() {
        return AtoZFilter.builder()
                .accept(Set.of(new Range('a', 'e'), new Range('i', 'o')))
                .deny(Set.of(new Range('w', 'z')))
                .build();
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 1) // next highest precedence
    public MeterFilter filter2() {
        return AtoZFilter.builder()
                .accept(Set.of(new Range('e', 'h')))
                .deny(Set.of(new Range('r', 'v')))
                .build();
    }
}
