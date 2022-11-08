package top.iseason.cmsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
@EnableCaching
@SpringBootApplication
public class CmSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(CmSystemApplication.class, args);
    }

}
