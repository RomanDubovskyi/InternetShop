package mate.academy.internetshop.controller;

import mate.academy.internetshop.annotations.Inject;
import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.model.User;
import mate.academy.internetshop.service.BucketService;
import mate.academy.internetshop.service.OrderService;
import mate.academy.internetshop.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CompleteOrderController extends HttpServlet {
    @Inject
    private static BucketService bucketService;
    @Inject
    private static OrderService orderService;
    @Inject
    private static UserService userService;
    private static final Long USER_ID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Bucket bucket = bucketService.getByOwnerId(USER_ID);
        User user = userService.get(USER_ID);
        orderService.completeOrder(bucket.getItems(), user);
        req.setAttribute("orders", orderService.getUserOrders(user));
        req.getRequestDispatcher("/WEB-INF/views/allOrders.jsp").forward(req, resp);
    }
}
