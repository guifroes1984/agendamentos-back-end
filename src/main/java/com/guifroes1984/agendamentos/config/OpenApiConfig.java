package com.guifroes1984.agendamentos.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(
	info = @Info(
		title = "API Agendamento de Manicures",
		version = "1.0",
		description = "API REST para gerenciamento de clientes e agendamentos"
	)
)
public class OpenApiConfig {
}