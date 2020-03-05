package com.gamex.application;

import com.gamex.application.controller.SynchronizationController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(value = "com.gamex.application")
public class Application {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(Application.class);
        ctx.getBean(SynchronizationController.class).initRoutes();
        ctx.registerShutdownHook();
    }
}
