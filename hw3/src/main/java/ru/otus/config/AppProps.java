package ru.otus.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;

@ConfigurationProperties(prefix = "questions")
public record AppProps(String path, Integer threshold, Locale locale) { }
