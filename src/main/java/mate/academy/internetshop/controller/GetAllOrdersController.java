package mate.academy.internetshop.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mate.academy.internetshop.annotations.Inject;
import mate.academy.internetshop.exceptions.DataProcessingException;
import mate.academy.internetshop.model.User;
import mate.academy.internetshop.service.OrderService;
import mate.academy.internetshop.service.UserService;
import org.apache.log4j.Logger;

public class GetAllOrdersController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(GetAllOrdersController.class);
    @Inject
    private static OrderService orderService;

    @Inject
    private static UserService userService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long userId = (Long) req.getSession().getAttribute("user_id");
        try {
            User user = userService.get(userId);
            req.setAttribute("orders", orderService.getUserOrders(user));
        } catch (DataProcessingException e) {
            LOGGER.error(e);
            req.setAttribute("error_massage", e);
            req.getRequestDispatcher("/WEB-INF/views/daraProcessingError.jsp").forward(req, resp);
        }
        req.getRequestDispatcher("/WEB-INF/views/allOrders.jsp").forward(req, resp);
    }
}
