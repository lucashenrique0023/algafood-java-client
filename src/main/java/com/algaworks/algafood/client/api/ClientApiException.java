package com.algaworks.algafood.client.api;

import org.springframework.web.client.RestClientResponseException;

import com.algaworks.algafood.client.model.Problem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientApiException extends RuntimeException {

	@Getter
	private Problem problem;
	
	public ClientApiException(String message, RestClientResponseException cause) {
		super(message, cause);
		
		deserializeProblem(cause);
		
	}

	private void deserializeProblem(RestClientResponseException cause) {	
		try {
			
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			mapper.registerModule(new JavaTimeModule());
			mapper.findAndRegisterModules();
			
			this.problem = mapper.readValue(cause.getResponseBodyAsString(), Problem.class);
			
		} catch (JsonProcessingException e) {
			log.warn("Nao foi possivel desserializar a resposta em um problema", e);
		}
		
	}

	
	
}