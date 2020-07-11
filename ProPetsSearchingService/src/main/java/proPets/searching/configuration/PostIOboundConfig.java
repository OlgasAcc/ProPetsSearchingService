package proPets.searching.configuration;

import org.springframework.cloud.stream.annotation.EnableBinding;

import proPets.searching.service.DataExchange;

@EnableBinding(DataExchange.class)
public class PostIOboundConfig {
	
}