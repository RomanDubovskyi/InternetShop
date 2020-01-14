package mate.academy.internetshop.annotations;

import mate.academy.internetshop.controller.InjectInitializer;
import mate.academy.internetshop.dao.BucketDao;
import mate.academy.internetshop.dao.ItemDao;
import mate.academy.internetshop.dao.OrderDao;
import mate.academy.internetshop.dao.UserDao;
import mate.academy.internetshop.service.BucketService;
import mate.academy.internetshop.service.ItemService;
import mate.academy.internetshop.service.OrderService;
import mate.academy.internetshop.service.UserService;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


public class Injector {
    private static final String PROJECT_MAIN_PACKAGE = "mate.academy.internetshop";
    private static final Logger LOGGER = Logger.getLogger(InjectInitializer.class);
    private static List<Class> classes = new ArrayList<>();

    static {
        try {
            classes.addAll(getClasses(PROJECT_MAIN_PACKAGE));
        } catch (ClassNotFoundException | IOException e) {
            LOGGER.error("Couldn't get all classes!", e);
            throw new RuntimeException(e);
        }
    }

    static {
        classes.add(UserDao.class);
        classes.add(BucketDao.class);
        classes.add(ItemDao.class);
        classes.add(OrderDao.class);
        classes.add(OrderService.class);
        classes.add(BucketService.class);
        classes.add(ItemService.class);
        classes.add(UserService.class);
    }

    public static void injectDependency() throws IllegalAccessException {
        for (Class certainClass : classes) {
            for (Field field : certainClass.getDeclaredFields()) {
                if (field.getDeclaredAnnotation(Inject.class) != null) {
                    Object implementation = AnnotatedClassMap.getImplementation(field.getType());
                    if (implementation.getClass().getDeclaredAnnotation(Service.class) != null
                            || implementation.getClass().getDeclaredAnnotation(Dao.class) != null) {
                        field.setAccessible(true);
                        field.set(null, implementation);
                    }
                }
            }
        }
    }

    private static List<Class> getClasses(String packageName)
            throws IOException, ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    private static List<Class> findClasses(File directory, String packageName)
            throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    assert !file.getName().contains(".");
                    classes.addAll(findClasses(file, packageName + "." + file.getName()));
                } else if (file.getName().endsWith(".class")) {
                    classes.add(Class.forName(packageName + '.'
                            + file.getName().substring(0, file.getName().length() - 6)));
                }
            }
        }
        return classes;
    }
}
