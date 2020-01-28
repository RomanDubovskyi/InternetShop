package mate.academy.internetshop.controller;

import mate.academy.internetshop.annotations.Inject;
import mate.academy.internetshop.exceptions.AuthenticationException;
import mate.academy.internetshop.exceptions.DataProcessingException;
import mate.academy.internetshop.model.User;
import mate.academy.internetshop.service.UserService;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

public class LoginController extends HttpServlet {
    private static Logger logger = Logger.getLogger(LoginController.class);
    @Inject
    private static UserService userService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("psw");
        try {
            User user = userService.login(login, password);
            HttpSession session = req.getSession(true);
            session.setAttribute("user_id", user.getUserId());
            resp.sendRedirect(req.getContextPath() + "/servlet/main_menu");
        } catch (AuthenticationException e) {
            req.setAttribute("error_massage", "Incorrect login or password!");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        } catch (DataProcessingException e) {
            logger.error(e);
            req.setAttribute("error_massage", e);
            req.getRequestDispatcher("/WEB-INF/views/daraProcessingError.jsp").forward(req, resp);
        }
    }
}
