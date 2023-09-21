package com.example.feigntracing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactivefeign.webclient.WebReactiveFeign;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;


@RestController
@RequestMapping("/")
public class TestController {

  private Logger logger = LoggerFactory.getLogger(TestController.class);

  private WebClient.Builder builder;

  public TestController(WebClient.Builder builder) {
    this.builder = builder;
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

  @GetMapping(path = "/wc")
  public Mono<String> wc() {
    logger.info("start request");
    return builder.baseUrl("http://localhost:50000/icecream/bills/pay").build()
      .post()
      .body(BodyInserters.fromPublisher(Mono.fromCallable(() -> {
        logger.info("body request");
        //Thread.sleep(30000);
        return "foobody";
      }), String.class))
      .retrieve()
      .bodyToMono(String.class)
      .mapNotNull(String::toUpperCase);
  }

  @GetMapping(path = "/wcAsync")
  public Mono<String> wcAsync() {
    logger.info("start request");
    return builder.baseUrl("http://localhost:50000/icecream/bills/pay").build()
      .post()
      .body(BodyInserters.fromPublisher(Mono.create(MonoSink::success).then(Mono.fromCallable(() -> {
        logger.info("body request");
        //Thread.sleep(30000);
        return "foobody";
      })), String.class))
      .retrieve()
      .bodyToMono(String.class)
      .mapNotNull(String::toUpperCase);
  }
}
