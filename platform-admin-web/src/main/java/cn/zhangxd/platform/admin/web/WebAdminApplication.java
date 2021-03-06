package cn.zhangxd.platform.admin.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Web admin application.
 *
 * @author zhangxd
 */
@RestController
@SpringBootApplication
@ImportResource("classpath:dubbo-consumer.xml")
@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class})
public class WebAdminApplication {

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebAdminApplication.class);

    /**
     * Hello string.
     *
     * @return the string
     */
    @RequestMapping
    public String hello() {
        return "Hello World!";
    }


    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(25);
        executor.setWaitForTasksToCompleteOnShutdown(Boolean.TRUE);
        executor.initialize();
        return executor;
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(WebAdminApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
        LOGGER.info("Web admin started!!!");
    }
}
