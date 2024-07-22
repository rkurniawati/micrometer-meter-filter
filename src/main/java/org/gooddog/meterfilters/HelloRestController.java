package org.gooddog.meterfilters;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Stream;

@RestController
@RequestMapping("/")
public class HelloRestController {
    private final HelloService helloService;
    private final MeterRegistry meterRegistry;

    public HelloRestController(HelloService helloService, MeterRegistry meterRegistry) {
        this.helloService = helloService;
        this.meterRegistry = meterRegistry;
    }

    @GetMapping("/hello/{name}")
    String hello(@PathVariable String name) {
        return helloService.sayHello(name);
    }

    @GetMapping("/get-meter-names")
    public Stream<String> getMeterNames() {
        return meterRegistry.getMeters().stream().sorted().map(m -> m.getId().getName());
    }
}
