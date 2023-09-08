package com.example.feigntracing;

import feign.RequestLine;
import reactor.core.publisher.Mono;

public interface FeignTestApi {

  @RequestLine("POST /icecream/bills/pay")
  Mono<String> somePostRequest(String bill);
}
