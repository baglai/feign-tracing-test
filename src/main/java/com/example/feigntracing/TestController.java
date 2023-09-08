package com.example.feigntracing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactivefeign.webclient.WebReactiveFeign;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/")
public class TestController {

  private Logger logger = LoggerFactory.getLogger(TestController.class);

  public TestController(WebClient.Builder builder) {
    this.client = WebReactiveFeign.<FeignTestApi>builder(builder)
      .target(FeignTestApi.class, "http://localhost:50000");
  }

  private final FeignTestApi client;


  @GetMapping
  public Mono<String> feign() {
    logger.info("start request");
    return client.somePostRequest("foo")
      .mapNotNull(String::toUpperCase);
  }
}
