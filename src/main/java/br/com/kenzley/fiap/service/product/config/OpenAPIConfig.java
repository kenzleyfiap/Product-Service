package br.com.kenzley.fiap.service.product.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Server URL in Development environment");


        Contact contact = new Contact();
        contact.setEmail("kenzleydev@outlook.com");
        contact.setName("Luan Kenzley");


        Info info = new Info()
                .title("Product Service")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to product service.");

        return new OpenAPI().info(info).servers(List.of(devServer));
    }
}
