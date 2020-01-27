package mate.academy.internetshop.controller;

import mate.academy.internetshop.annotations.Inject;
import mate.academy.internetshop.exceptions.DataProcessingException;
import mate.academy.internetshop.service.OrderService;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteOrderController extends HttpServlet {
    private static Logger logger = Logger.getLogger(AddItemController.class);
    @Inject
    private static OrderService orderService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String orderId = req.getParameter("order_id");
        try {
            orderService.deleteById(Long.valueOf(orderId));
        } catch (DataProcessingException e) {
            logger.error(e);
            req.setAttribute("error_massage", e);
            req.getRequestDispatcher("/WEB-INF/views/daraProcessingError.jsp").forward(req, resp);
        }
        resp.sendRedirect(req.getContextPath() + "/servlet/getAllOrders");
    }
}
