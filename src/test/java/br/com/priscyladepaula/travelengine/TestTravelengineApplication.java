package br.com.priscyladepaula.travelengine;

import org.springframework.boot.SpringApplication;

public class TestTravelengineApplication {

	public static void main(String[] args) {
		SpringApplication.from(TravelengineApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
