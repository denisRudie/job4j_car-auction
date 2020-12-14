package ru.job4j.servlet;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class Filter implements javax.servlet.Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        String uri = req.getRequestURI();
        HttpSession session = req.getSession(false);

        boolean loggedIn = session != null && session.getAttribute("user") != null;
        boolean isLoginPage = uri .endsWith("/login") || uri.endsWith("registration.html");

        if (loggedIn || isLoginPage) {
            chain.doFilter(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }

    @Override
    public void destroy() {

    }
}
