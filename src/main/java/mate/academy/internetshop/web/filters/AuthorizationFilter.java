package mate.academy.internetshop.web.filters;

import static mate.academy.internetshop.model.Role.RoleName.ADMIN;
import static mate.academy.internetshop.model.Role.RoleName.USER;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mate.academy.internetshop.annotations.Inject;
import mate.academy.internetshop.exceptions.DataProcessingException;
import mate.academy.internetshop.model.Role;
import mate.academy.internetshop.model.User;
import mate.academy.internetshop.service.UserService;
import org.apache.log4j.Logger;

public class AuthorizationFilter implements Filter {
    private static final Logger LOGGER = Logger.getLogger(AuthorizationFilter.class);
    @Inject
    private static UserService userService;
    private Map<String, Role.RoleName> protectedUrlsAdmin = new HashMap<>();
    private Map<String, Role.RoleName> protectedUrlsUser = new HashMap<>();

    @Override
    public void init(FilterConfig filterConfig) {
        protectedUrlsAdmin.put("/servlet/getAllUsers", ADMIN);
        protectedUrlsAdmin.put("/servlet/addItem", ADMIN);
        protectedUrlsAdmin.put("/servlet/deleteFromItems", ADMIN);

        protectedUrlsUser.put("/servlet/addItemToBucket", USER);
        protectedUrlsUser.put("/servlet/deleteItemFromBucket", USER);
        protectedUrlsUser.put("/servlet/completeOrder", USER);
        protectedUrlsUser.put("/servlet/getBucket", USER);
        protectedUrlsUser.put("/servlet/getAllOrders", USER);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        String requestedUrl = req.getServletPath();
        Role.RoleName roleNameAdmin = protectedUrlsAdmin.get(requestedUrl);
        Role.RoleName roleNameUser = protectedUrlsUser.get(requestedUrl);
        if (roleNameUser == null && roleNameAdmin == null) {
            processAuthorized(chain, req, resp);
            return;
        }
        Long userId = (Long) req.getSession().getAttribute("user_id");
        User user = null;
        try {
            user = userService.get(userId);
        } catch (DataProcessingException e) {
            LOGGER.error(e);
            req.setAttribute("error_massage", e);
            req.getRequestDispatcher("/WEB-INF/views/daraProcessingError.jsp").forward(req, resp);
        }
        if (verifyRole(user, roleNameAdmin) || verifyRole(user, roleNameUser)) {
            processAuthorized(chain, req, resp);
        } else {
            processDenied(req, resp);
        }
    }

    private boolean verifyRole(User user, Role.RoleName roleName) {
        return user.getRoles().stream()
                .anyMatch(r -> r.getRoleName().equals(roleName));
    }

    private void processDenied(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/accessDenied.jsp").forward(req, resp);
    }

    private void processAuthorized(FilterChain chain, HttpServletRequest req,
                                   HttpServletResponse resp) throws IOException, ServletException {
        chain.doFilter(req, resp);
    }

    @Override
    public void destroy() {
    }
}
