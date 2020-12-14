package ru.job4j.servlet;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.model.Owner;
import ru.job4j.persist.HbmAuction;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class AuthServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(AuthServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("login.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try (BufferedReader read = req.getReader()) {
            StringBuilder fullLine = new StringBuilder();
            String oneLine;
            while ((oneLine = read.readLine()) != null) {
                fullLine.append(oneLine);
            }
            JSONObject json = (JSONObject) new JSONParser().parse(fullLine.toString());
            String action = (String) json.get("action");
            String login = (String) json.get("email");
            String pwd = (String) json.get("pwd");
            String name = (String) json.get("name");

            if (action.equals("new")) {
                Owner owner = new Owner();
                owner.setName(name);
                owner.setEmail(login);
                owner.setPassword(pwd);
                HbmAuction.instOf().addOwner(owner);
                resp.setStatus(HttpServletResponse.SC_OK);
            } else if (action.equals("login")) {
                Owner existing = HbmAuction.instOf().getOwnerByEmail(login);
                if (existing != null && existing.getPassword().equals(pwd)) {
                    req.getSession().setAttribute("user", existing);
                    resp.setStatus(HttpServletResponse.SC_OK);
                } else {
                    resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }
        } catch (IOException | ParseException e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
