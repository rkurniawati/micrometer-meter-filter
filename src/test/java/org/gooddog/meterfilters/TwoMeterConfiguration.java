package org.gooddog.meterfilters;

import org.gooddog.meterfilters.filter.AtoZFilter;
import org.gooddog.meterfilters.filter.Range;
import org.springframework.core.annotation.Order;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.util.Set;

@Configuration
public class TwoMeterConfiguration {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public AtoZFilter acceptAE_DenyFZ() {
        return AtoZFilter.builder()
                .deny(Set.of(new Range('a', 'e')))
                .accept(Set.of(new Range('f', 'h'), new Range('x', 'z')))
                .build();
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 1) // next highest precedence
    public AtoZFilter acceptGI_DenyLO() {
        return AtoZFilter.builder()
                .deny(Set.of(new Range('r', 'v')))
                .accept(Set.of(new Range('i', 'l')))
                .build();
    }
}
