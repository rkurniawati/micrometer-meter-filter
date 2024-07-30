package org.gooddog.meterfilters;

import io.micrometer.core.instrument.config.MeterFilter;
import org.gooddog.meterfilters.filter.AtoZFilter;
import org.gooddog.meterfilters.filter.Range;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.Set;

@Profile("beans")
@Configuration
public class MeterFilterConfiguration {

    // The set of four MeterFilter beans below will be applied in the order of their Order values,
    // with Spring Boot's default MeterFilters in between:
    //   - front1
    //   - front2
    //   - Spring Boot's default MeterFilters
    //   - back2
    //   - back1

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public MeterFilter front1() {
        return AtoZFilter.builder()
                .accept(Set.of(new Range('a', 'c')))
                .deny(Set.of(new Range('y', 'z')))
                .build();
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE+1)
    public MeterFilter front2() {
        return AtoZFilter.builder()
                .accept(Set.of(new Range('b', 'd')))
                .deny(Set.of(new Range('w', 'x')))
                .build();
    }

    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE)
    public MeterFilter back1() {
        return AtoZFilter.builder()
                .accept(Set.of(new Range('a', 'e')))
                .deny(Set.of(new Range('r', 't')))
                .build();
    }

    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE-1)
    public MeterFilter back2() {
        return AtoZFilter.builder()
                .accept(Set.of(new Range('f', 'g')))
                .deny(Set.of(new Range('u', 'v')))
                .build();
    }
}
