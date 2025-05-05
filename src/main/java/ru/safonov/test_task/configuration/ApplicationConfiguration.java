package ru.safonov.test_task.configuration;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import ru.safonov.test_task.security.expression.JwtProperties;
import ru.safonov.test_task.security.expression.JwtTokenFilter;
import ru.safonov.test_task.security.expression.JwtTokenProvider;

/**
 * Класс используется для настройки параметров безопасности в приложении.
 * Также для настройки необходимых бинов и Swagger Open API.
 * '@EnableWebSecurity' - включает модуль безопасности Spring Web в приложении.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableCaching
@EnableScheduling
@EnableConfigurationProperties(JwtProperties.class)
@RequiredArgsConstructor
public class ApplicationConfiguration {
    private final JwtTokenProvider tokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return rawPassword.toString().equals(encodedPassword);
            }
        };
    }

    /**
     * Создает AuthenticationManager для использования в процессе аутентификации.
     *
     * @return AuthenticationManager, используемый в приложении
     * @throws Exception, если возникает ошибка при создании AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Создает и настраивает экземпляр OpenAPI
     *
     * @return Экземпляр OpenAPI с настройками безопасности, компонентами и информацией API.
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(
                        new Components()
                                .addSecuritySchemes("bearerAuth",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                )
                .info(new Info()
                        .title("API для взаимодействия с GK-Test-Task-Service")
                        .description("Endpoints")
                        .version("0.0.1")
                        .contact(new Contact()
                                .name("Сергей")
                                .email("tchbreadman@gmail.com")
                                .url("https://t.me/Baumstruktur")));
    }

    /**
     * Создает цепочку фильтров безопасности HTTP.
     *
     * @param httpSecurity Объект конфигурации HTTP безопасности.
     * @return Цепочка фильтров безопасности HTTP.
     * @throws Exception В случае ошибок конфигурации HTTP безопасности.
     */
    @Bean
    @SneakyThrows
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> {
                    cors.configurationSource(request -> {
                        var corsConfig = new CorsConfiguration();
                        corsConfig.addAllowedOrigin("http://localhost:5173");
                        corsConfig.addAllowedMethod("*");
                        corsConfig.addAllowedHeader("*");
                        corsConfig.setAllowCredentials(true);
                        return corsConfig;
                    });
                })
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setContentType("text/plain; charset=UTF-8");
                            response.getWriter().write("Не авторизован - передайте JWT токен");
                        })
                        .accessDeniedHandler(((request, response, accessDeniedException) -> {
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            ;
                            response.setContentType("text/plain; charset=UTF-8");
                            response.getWriter().write("Доступ к данному ресурсу запрещен.");
                        })))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/swagger-ui/index.html#/").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/transfers").authenticated()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtTokenFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}