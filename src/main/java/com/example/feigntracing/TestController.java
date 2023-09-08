package com.example.feigntracing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactivefeign.webclient.WebReactiveFeign;
import reactor.core.publisher.Mono;



@RestController
@RequestMapping("/")
public class TestController {

  private Logger logger = LoggerFactory.getLogger(TestController.class);

  private final FeignTestApi client = WebReactiveFeign.<FeignTestApi>builder()
    .target(FeignTestApi.class, "http://localhost:50000");


  @GetMapping
  public Mono<String> init() {
    logger.info("start request");
    return client.somePostRequest("foo")
    .mapNotNull(String::toUpperCase);
  }
}
