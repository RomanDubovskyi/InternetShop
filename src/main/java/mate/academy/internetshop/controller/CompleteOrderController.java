package mate.academy.internetshop.controller;

import mate.academy.internetshop.annotations.Inject;
import mate.academy.internetshop.exceptions.DataProcessingException;
import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.model.User;
import mate.academy.internetshop.service.BucketService;
import mate.academy.internetshop.service.OrderService;
import mate.academy.internetshop.service.UserService;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CompleteOrderController extends HttpServlet {
    private static Logger logger = Logger.getLogger(CompleteOrderController.class);
    @Inject
    private static BucketService bucketService;
    @Inject
    private static OrderService orderService;
    @Inject
    private static UserService userService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long userId = (Long) req.getSession().getAttribute("user_id");
        Bucket bucket = null;
        try {
            bucket = bucketService.getByOwnerId(userId);
            User user = userService.get(userId);
            orderService.completeOrder(bucket.getItems(), user);
            bucketService.clear(bucket);
            req.setAttribute("orders", orderService.getUserOrders(user));
        } catch (DataProcessingException e) {
            logger.error(e);
            req.setAttribute("error_massage", e);
            req.getRequestDispatcher("/WEB-INF/views/daraProcessingError.jsp").forward(req, resp);
        }
        req.getRequestDispatcher("/WEB-INF/views/allOrders.jsp").forward(req, resp);
    }
}
