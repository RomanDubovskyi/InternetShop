package mate.academy.internetshop.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mate.academy.internetshop.annotations.Inject;
import mate.academy.internetshop.exceptions.DataProcessingException;
import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.model.Role;
import mate.academy.internetshop.model.User;
import mate.academy.internetshop.service.BucketService;
import mate.academy.internetshop.service.UserService;
import org.apache.log4j.Logger;

public class InjectDataController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(InjectDataController.class);
    @Inject
    private static UserService userService;
    @Inject
    private static BucketService bucketService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            User user = new User();
            user.setName("Jerry");
            user.setSurname("Smith");
            user.addRole(Role.of("USER"));
            user.setLogin("user");
            user.setPassword("user");
            userService.create(user);
            Bucket bucket = new Bucket();
            bucket.setOwnerId(user.getUserId());
            bucketService.create(bucket);

            User admin = new User();
            admin.setName("Rick");
            admin.setSurname("Sanchez");
            admin.addRole(Role.of("ADMIN"));
            admin.setLogin("admin");
            admin.setPassword("admin");
            userService.create(admin);
        } catch (DataProcessingException e) {
            LOGGER.error(e);
            req.setAttribute("error_massage", e);
            req.getRequestDispatcher("/WEB-INF/views/daraProcessingError.jsp").forward(req, resp);
        }

        req.getRequestDispatcher("/login").forward(req, resp);
    }
}
