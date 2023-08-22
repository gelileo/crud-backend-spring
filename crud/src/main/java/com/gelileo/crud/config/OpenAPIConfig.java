package com.gelileo.crud.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
      info = @Info(
              contact = @Contact(
                      name  = "Kun",
                      email = "mail2kun@gmail.com"
              ),
              description = "OpenAPI documentation for Spring Crud",
              title = "OpenAPI specification - crud",
              version = "v1.0",
              license = @License(
                      name = "Apache"
              ),
              termsOfService = "Terms of Service"
      ),
      servers = {
              @Server(
                      description = "Local",
                      url = "http://localhost:8081"
              ),
              @Server(
                      description = "Production",
                      url = ""
              )

      }

)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenAPIConfig {
}
