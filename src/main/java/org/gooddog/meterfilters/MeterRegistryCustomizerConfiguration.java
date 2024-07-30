package org.gooddog.meterfilters;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import org.gooddog.meterfilters.filter.AtoZFilter;
import org.gooddog.meterfilters.filter.Range;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Set;

// if you enable this profile, the filter setup here will take precedence over the other filters
// setup as spring beans
@Profile("customizer")
@Configuration
public class MeterRegistryCustomizerConfiguration {
    @Bean
    public MeterRegistryCustomizer<? extends MeterRegistry> meterRegistryCustomizer() {
        return registry -> registry.config()
                .meterFilter(acceptAB())
                .meterFilter(denyKZ());
    }

    private MeterFilter denyKZ() {
        return AtoZFilter.builder()
                .deny(Set.of(new Range('k', 'z')))
                .build();
    }

    private MeterFilter acceptAB() {
        return AtoZFilter.builder()
                .accept(Set.of(new Range('a', 'b')))
                .build();
    }
}
