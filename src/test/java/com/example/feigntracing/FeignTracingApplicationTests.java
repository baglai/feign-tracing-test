package com.example.feigntracing;


import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;


@SpringBootTest()
class FeignTracingApplicationTests {

	WireMockServer wireMockServer = new WireMockServer(options().port(50000));

	@Autowired
	private ApplicationContext context;

	@Test
	void feignTest() {
		wireMockServer.start();
		wireMockServer.stubFor(post(urlEqualTo("/icecream/bills/pay"))
			.willReturn(aResponse().withBody("Hello World!")));

		WebTestClient client = WebTestClient.bindToApplicationContext(context).build();


		client.get().uri("/").exchange().expectStatus().isOk().expectBody();

		wireMockServer.stop();
	}
}
