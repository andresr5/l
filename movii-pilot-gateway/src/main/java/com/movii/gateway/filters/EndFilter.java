package com.movii.gateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class EndFilter implements GlobalFilter, Ordered {

	@Override
	public int getOrder() {
		return 1;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		HttpHeaders headers = exchange.getRequest().getHeaders();
		System.out.println("End Filter");
		System.out.println(headers.keySet());
		System.out.println(headers.size());
		exchange.getResponse().getHeaders().set("Header-filtrofinal", "oimate");
		
		System.out.println(exchange.getResponse().getHeaders().keySet());
		System.out.println(exchange.getResponse().getHeaders().size());
		System.out.println(exchange.getResponse().getStatusCode());
		exchange.getResponse().setStatusCode(exchange.getResponse().getStatusCode()); 
		
		ServerHttpResponse response = exchange.getResponse();
		
		response.setStatusCode(HttpStatus.UNAUTHORIZED);
		//response.setComplete();
		//exchange.getPrincipal().subscribe(a->System.out.println(a));
		return chain.filter(exchange);
	}

}
