package test.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@PropertySource(value = {"classpath:application.properties", "classpath:build.properties"}, encoding = "UTF-8")
@ComponentScan("com.test")
@EnableAsync
@EnableScheduling
public class AppConfig {


}
