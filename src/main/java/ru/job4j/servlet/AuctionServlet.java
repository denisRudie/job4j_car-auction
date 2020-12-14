package ru.job4j.servlet;

import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.model.Car;
import ru.job4j.model.Owner;
import ru.job4j.persist.HbmAuction;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

public class AuctionServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(AuctionServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try (PrintWriter out = resp.getWriter()) {
            String action = req.getParameter("action");
            Owner owner = (Owner) req.getSession().getAttribute("user");

            switch (action) {
                case "my" -> {
                    resp.setCharacterEncoding("UTF-8");
                    Collection<Car> cars =
                            HbmAuction.instOf()
                                    .getAllCarsByUser(owner).stream()
                                    .sorted(Comparator.comparing(Car::getId))
                                    .collect(Collectors.toList());
                    String jsonOut = new Gson().toJson(cars);
                    out.write(jsonOut);
                    out.flush();
                }
                case "all" -> {
                    resp.setCharacterEncoding("UTF-8");
                    Collection<Car> tasks =
                            HbmAuction.instOf()
                                    .getAllCars().stream()
                                    .sorted(Comparator.comparing(Car::getId))
                                    .collect(Collectors.toList());
                    String jsonOut = new Gson().toJson(tasks);
                    out.write(jsonOut);
                    out.flush();
                }
            }
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
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
            String stringId = (String) json.get("id");

            switch (action) {
                case "delete" -> HbmAuction.instOf().removeCar(Integer.parseInt(stringId));
                case "update" -> HbmAuction.instOf().setCarStatusSold(Integer.parseInt(stringId));
            }
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (IOException | ParseException e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
