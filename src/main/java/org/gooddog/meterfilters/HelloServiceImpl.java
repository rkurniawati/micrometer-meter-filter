package org.gooddog.meterfilters;

import io.micrometer.observation.annotation.Observed;
import org.springframework.stereotype.Service;

@Service
public class HelloServiceImpl implements HelloService {
    @Override
    @Observed(name = "hello.sayHello")
    public String sayHello(String name) {
        return "Hello, " + name + "!";
    }
}
