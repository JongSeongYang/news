package com.someverything.news.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    private final ModelMapper customModelMapper = new ModelMapper();

    @Bean
    public ModelMapper strictModelMapper() {
        customModelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return customModelMapper;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
