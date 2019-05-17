package com.movii.gateway.filters;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.DefaultServerRequest;
import org.springframework.core.Ordered;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;

import com.movii.gateway.provider.RedisProvider;

import co.moviired.connector.connector.RestConnector;
import reactor.core.publisher.Mono;

@Component
public class CustomFilterAuth implements GlobalFilter, Ordered {

	RestConnector restConnector = new RestConnector("http://localhost:8080", 2000, 2000);

	
	@Autowired
	RedisProvider redisProvider;
	
	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return -200;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		
		ServerRequest serverRequest = new DefaultServerRequest(exchange);
		
		HttpHeaders headers = exchange.getRequest().getHeaders();
		String username = exchange.getRequest().getHeaders().get("username").get(0);
		String password = exchange.getRequest().getHeaders().get("password").get(0);
		String authorizationHeader = exchange.getRequest().getHeaders().get("array0").get(0);
		
		
		System.out.println("username");
		System.out.println(username);
		System.out.println("password");
		System.out.println(password);
		System.out.println("Get attributes ------------>");
		System.out.println(exchange.getAttributes());
		System.out.println("Body cast to string -------->");
		System.out.println(exchange.getRequest().getBody());
	
		String authorizationHeader = "bGVnYWN5Og==";

		MultiValueMap<String, String> mapaValores = new LinkedMultiValueMap<String, String>();
		mapaValores.put("username", new ArrayList() {
			{
				add(username);
			}
		});
		mapaValores.put("password", new ArrayList() {
			{
				add(password);
			}
		});
		mapaValores.put("grant_type", new ArrayList() {
			{
				add("password");
			}
		});
		mapaValores.put("scope", new ArrayList() {
			{
				add("read_profile");
			}
		});

		// Map<String, String> headersInMap = new HashMap<String, String>();
		// headersInMap.put("Authorization", "Basic " + authorizationHeader);
		String respuesta = "";

		// Using connectors library must be improved
		/*
		 * try { respuesta = (String) restConnector.post(
		 * "http://localhost:8080/moviired-api/supportauthentication/oauth/token",
		 * mapaValores, String.class, MediaType.APPLICATION_FORM_URLENCODED,
		 * headersInMap); } catch (Exception e) {
		 * System.out.println("error using connectors"); e.printStackTrace(); }
		 */
		// System.out.println("Respuesta using connectors --------------> ");
		// System.out.println(respuesta);
		RestTemplate template = new RestTemplate();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setBasicAuth(username, password);
		httpHeaders.set("Authorization", "Basic " + authorizationHeader);

		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		String responsez = "";
		ResponseEntity<String> respuestaz = null;
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(mapaValores, httpHeaders);
		String response = "";
		try {
			response = template.postForObject("http://localhost:8080/moviired-api/supportauthentication/oauth/token",
					request, String.class);
			HttpEntity<MultiValueMap<String, String>> request1 = new HttpEntity<>(mapaValores, httpHeaders);
			// respuestaz =
			// template.exchange("http://localhost:8080/moviired-api/supportauthentication/oauth/token",HttpMethod.POST,request,String.class);
		} catch (Exception e) {
			System.out.println("Error Calling AuthSever");
			e.getCause();
			exchange.getResponse().getHeaders().set("Headererror-----", "valorheadrerror");
			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
	        //exchange.getResponse().setComplete();
			return Mono.empty();
			//return chain.filter(exchange);
		}

		System.out.println("responsez -------------->");
		System.out.println(response);
		// System.out.println(respuestaz.getHeaders());
		// System.out.println(respuestaz.getBody());

		if (respuestaz.getBody().contains("Unauthorized")) {
			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
			return Mono.empty();
			//return chain.filter(exchange);
			// exchange.getResponse().setComplete();
		}

		/*
		 * WebClient client = WebClient.builder().baseUrl("http://localhost:8080")
		 * .defaultHeader(HttpHeaders.CONTENT_TYPE,
		 * MediaType.APPLICATION_FORM_URLENCODED_VALUE)
		 * .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic " +
		 * authorizationHeader).build();
		 */

		// Probe with delay ------------->
		/*
		 * Mono<String> clientResponse =
		 * client.post().uri("/moviired-api/supportauthentication/oauth/token")
		 * .body(BodyInserters.fromFormData(mapaValores)) .exchange().flatMap(responsex
		 * -> responsex.bodyToMono(String.class));
		 */

		// clientResponse.dela
		/*
		 * if(clientResponse.contains("Unauthorized")) {
		 * exchange.getResponse().setComplete();
		 * exchange.getResponse().getHeaders().set("UnauthorizedHeader",
		 * "unauthorized"); return chain.filter(exchange); }
		 */

		// clientResponse.doOnError(Exception.class, w->System.out.println(w));
		/*
		 * System.out.println(clientResponse); clientResponse.subscribe(then -> {
		 * System.out.println("then ------------->"); System.out.println(then);
		 * if(then.contains("Unauthorized")) {
		 * System.out.println("in Unauthorized ------------->");
		 * exchange.getResponse().setComplete();
		 * //exchange.getResponse().getHeaders().set("UnauthorizedHeader",
		 * "unauthorized"); //chain.filter(exchange);
		 * 
		 * return ; } //System.out.println(then.headers()); //Headers responseheaders =
		 * then.headers(); //Mono<String> bodyToMono = then.bodyToMono(String.class);
		 * 
		 * //Mono<String> mapped = bodyToMono.map(name->"a"). //
		 * doOnSuccess(s->{System.out.println("Exitoso --------->");System.out.println(s
		 * );}) //
		 * .doOnError(s->{System.out.println("Error --------->");System.out.println(s);}
		 * );
		 * 
		 * //bodyToMono.subscribe((body)->{
		 * //System.out.println("Body ---------------->"); //System.out.println(body);
		 * //if(body.contains("\"error\":\"Unauthorized\"")){ // throw new
		 * RuntimeException("Throw 401 Unauthorized");
		 * //System.out.println("Response--------->");
		 * //System.out.println(exchange.getResponse().getStatusCode());
		 * 
		 * //exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
		 * 
		 * //System.out.println(exchange.getResponse().getStatusCode()); //return
		 * exchange.getResponse().setComplete();
		 * 
		 * //} //},errorBody->{ // System.out.println("ErrorBody"); //
		 * System.out.println(errorBody); //});
		 * 
		 * }, error -> { System.out.println("error  ---------------->");
		 * System.out.println(error); error.printStackTrace(); });
		 */

		// String token = exchange.getRequest().getHeaders().get("username").get(0);
		// exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
		return chain.filter(exchange);
		// exchange.getResponse().setComplete();
	}

	public String getIp(HttpHeaders headers) {
		return headers.getOrigin();
	}

}
