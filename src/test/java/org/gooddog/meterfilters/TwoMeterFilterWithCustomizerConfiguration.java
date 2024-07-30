package org.gooddog.meterfilters;

import org.gooddog.meterfilters.filter.AtoZFilter;
import org.gooddog.meterfilters.filter.Range;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Set;

@Configuration
@Profile("two-meter-filter-with-customizer")
public class TwoMeterFilterWithCustomizerConfiguration {

    @Bean
    public MeterRegistryCustomizer<?> meterRegistryCustomizer() {
        // note that here we are using the acceptAE_IO_DenyWZ and acceptEH_DenyRV as method to produce
        // the AtoZFilter instances, not as beans

        return registry -> registry.config()
                .meterFilter(filter1())
                .meterFilter(filter2());
    }

    private AtoZFilter filter1() {
        return AtoZFilter.builder()
                .accept(Set.of(new Range('a', 'e'), new Range('i', 'o')))
                .deny(Set.of(new Range('w', 'z')))
                .build();
    }

    public AtoZFilter filter2() {
        return AtoZFilter.builder()
                .accept(Set.of(new Range('e', 'h')))
                .deny(Set.of(new Range('r', 'v')))
                .build();
    }
}
