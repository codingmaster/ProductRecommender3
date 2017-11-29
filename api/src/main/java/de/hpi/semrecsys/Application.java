package de.hpi.semrecsys;

import de.hpi.semrecsys.repository.BaseRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = {BaseRepository.class, ProductApi.class})
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public HibernateJpaSessionFactoryBean sessionFactory() {
        return new HibernateJpaSessionFactoryBean();
    }

}
