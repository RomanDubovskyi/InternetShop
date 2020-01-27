package mate.academy.internetshop.controller;

import mate.academy.internetshop.annotations.Inject;
import mate.academy.internetshop.exceptions.DataProcessingException;
import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.service.BucketService;
import mate.academy.internetshop.service.ItemService;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AddItemToBucketController extends HttpServlet {
    private static Logger logger = Logger.getLogger(AddItemController.class);
    @Inject
    private static BucketService bucketService;
    @Inject
    private static ItemService itemService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long userId = (Long) req.getSession().getAttribute("user_id");
        String itemId = req.getParameter("item_id");
        try {
            Bucket newBucket = bucketService.getByOwnerId(userId);
            Item item = itemService.get(Long.valueOf(itemId));
            bucketService.addItem(newBucket, item);
        } catch (DataProcessingException e) {
            logger.error(e);
            req.setAttribute("error_massage", e);
            req.getRequestDispatcher("/WEB-INF/views/daraProcessingError.jsp").forward(req, resp);
        }
        resp.sendRedirect(req.getContextPath() + "/servlet/getAllItems");
    }
}
