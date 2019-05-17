package com.movii.gateway.util.validators;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class FieldsValidator {
	
	
	MultiValueMap<String, String> mapaValores = new LinkedMultiValueMap<String, String>();
	
	public String validateHeaders(HttpHeaders httpHeaders){
		
		return "";
	}
	
}
