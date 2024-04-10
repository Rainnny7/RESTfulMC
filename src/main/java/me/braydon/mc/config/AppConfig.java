/*
 * MIT License
 *
 * Copyright (c) 2024 Braydon (Rainnny).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package me.braydon.mc.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The configuration for the app.
 *
 * @author Braydon
 */
@Configuration @Getter
public class AppConfig {
    public static AppConfig INSTANCE;
    public static final Gson GSON = new GsonBuilder()
            .setDateFormat("MM-dd-yyyy HH:mm:ss")
            .create();

    @Value("${server.publicUrl}")
    private String serverPublicUrl;

    /**
     * The build properties of the
     * app, null if the app is not built.
     */
    private final BuildProperties buildProperties;

    @Autowired
    public AppConfig(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @PostConstruct
    public void onInitialize() {
        INSTANCE = this;
    }

    /**
     * Define the OpenAI specification for this app.
     *
     * @return the specification
     */
    @Bean @NonNull
    public OpenAPI defineOpenAPI() {
        Info info = new Info();
        info.setTitle("RESTfulMC");
        info.setVersion(buildProperties == null ? "N/A" : buildProperties.getVersion());
        info.setDescription(buildProperties == null ? "N/A" : buildProperties.get("description"));
        info.setContact(new Contact().name("Braydon (Rainnny)").url("https://rainnny.club").email("braydonrainnny@gmail.com"));
        info.setLicense(new License().name("MIT License").url("https://opensource.org/licenses/MIT"));

        return new OpenAPI()
                .info(info)
                .addServersItem(new Server().url(serverPublicUrl).description("The public server URL"));
    }
}