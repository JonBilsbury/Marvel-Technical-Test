package com.marvel.demo.services;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@Import({TranslateService.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TranslateServiceIT {

	@Autowired
	TranslateService translateService;

	WireMockServer wireMockServer;

	@BeforeEach
	void init()
	{
		wireMockServer = new WireMockServer(8090);
		wireMockServer.start();
	}

	@AfterEach
	void teardown()
	{
		wireMockServer.stop();
	}

	@Test
	void testTranslateString()
	{
		wireMockServer.stubFor(post(urlPathEqualTo("/translate/v2"))
				.willReturn(aResponse().withHeader("Content-Type", "application/json")
						.withStatus(200)
						.withBodyFile("wiremock/TranslateResponse.json")));

		Mono<String> source = translateService.translateString("Born with super-human senses and the power to heal from almost any wound, Wolverine was captured by a secret Canadian organization and given an unbreakable skeleton and claws. Treated like an animal, it took years for him to control himself. Now, he's a premiere member of both the X-Men and the Avengers.", "de");

		StepVerifier.create(source)
				.assertNext(result -> {
					assertNotNull(result);
					assertEquals("Geboren mit übermenschlichen Sinnen und der Kraft, fast jede Wunde zu heilen, wurde Wolverine von einer kanadischen Geheimorganisation gefangen genommen und erhielt ein unzerbrechliches Skelett und Klauen. Wie ein Tier behandelt, brauchte er Jahre, um sich zu beherrschen. Jetzt ist er ein Premierenmitglied der X-Men und der Avengers.", result);
				})
				.verifyComplete();
	}
}