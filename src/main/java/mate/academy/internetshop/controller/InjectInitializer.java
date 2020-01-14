package mate.academy.internetshop.controller;

import mate.academy.internetshop.annotations.Injector;
import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class InjectInitializer implements ServletContextListener {
    private static final Logger LOGGER = Logger.getLogger(InjectInitializer.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            Injector.injectDependency();
        } catch (IllegalAccessException e) {
            LOGGER.error("Couldn't inject all dependency properly", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
