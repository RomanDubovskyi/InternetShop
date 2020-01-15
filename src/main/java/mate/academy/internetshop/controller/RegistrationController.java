package mate.academy.internetshop.controller;

import mate.academy.internetshop.annotations.Inject;
import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.model.User;
import mate.academy.internetshop.service.BucketService;
import mate.academy.internetshop.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RegistrationController extends HttpServlet {

    @Inject
    private static UserService userService;
    @Inject
    private static BucketService bucketService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User newUser = new User();
        newUser.setLogin(req.getParameter("login"));
        newUser.setName(req.getParameter("user_name"));
        newUser.setPassword(req.getParameter("psw"));
        newUser.setSurname(req.getParameter("user_surname"));
        User user = userService.create(newUser);
        HttpSession session = req.getSession(true);
        session.setAttribute("user_id", user.getUserId());
        Cookie cookie = new Cookie("MATE", user.getToken());
        resp.addCookie(cookie);
        Bucket bucket = new Bucket();
        bucket.setOwnerId(newUser.getUserId());
        bucketService.create(bucket);
        resp.sendRedirect(req.getContextPath() + "/servlet/getAllUsers");
    }
}
