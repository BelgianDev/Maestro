package be.raftdev.maestro.client;

import be.raftdev.maestro.client.itunes.ItunesClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ApiClients {

    @Bean
    ItunesClient itunesClient() {
        // Apple iTunes weirdness. Returns [text/javascript;charset=utf-8] instead of proper JSON, spring gets confused and panics
        JacksonJsonHttpMessageConverter jsonConverter = new JacksonJsonHttpMessageConverter();
        jsonConverter.setSupportedMediaTypes(List.of(new MediaType("text", "javascript")));

        RestClient client = RestClient.builder()
                .baseUrl("https://itunes.apple.com")
                .configureMessageConverters(convBuilder -> convBuilder.addCustomConverter(jsonConverter))
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(client);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(ItunesClient.class);
    }
}
