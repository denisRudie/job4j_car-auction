package ru.job4j.servlet;

import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.model.Car;
import ru.job4j.model.CarBody;
import ru.job4j.model.Owner;
import ru.job4j.model.Transmission;
import ru.job4j.persist.HbmAuction;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AdServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(AdServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try (PrintWriter out = resp.getWriter()) {
            Collection<CarBody> bodies = HbmAuction.instOf().getAllCarBody();
            Collection<Transmission> transmissions = HbmAuction.instOf().getAllTransmissions();

            Map<String, Collection> dictionary = new HashMap<>();
            dictionary.put("bodies", bodies);
            dictionary.put("transmissions", transmissions);

            String jsonOut = new Gson().toJson(dictionary);
            out.write(jsonOut);
            out.flush();
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try (BufferedReader read = req.getReader();
             PrintWriter out = resp.getWriter()) {
            HbmAuction store = HbmAuction.instOf();
            StringBuilder fullLine = new StringBuilder();
            String oneLine;
            while ((oneLine = read.readLine()) != null) {
                fullLine.append(oneLine);
            }
            JSONObject json = (JSONObject) new JSONParser().parse(fullLine.toString());

            Owner owner = (Owner) req.getSession().getAttribute("user");
            String brand = (String) json.get("brand");
            String model = (String) json.get("model");
            int bodyId = Integer.parseInt((String) json.get("body"));
            int transmissionId = Integer.parseInt((String) json.get("transmission"));
            float price = Float.parseFloat((String) json.get("price"));

            CarBody body = store.getBodyById(bodyId);
            Transmission transmission = store.getTransmissionById(transmissionId);

            Car car = new Car();
            car.setTransmission(transmission);
            car.setCarBody(body);
            car.setBrand(brand);
            car.setModel(model);
            car.setPrice(price);
            car.setOwner(owner);

            int carId = store.addCar(car);
            req.getSession().setAttribute("carId", carId);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (IOException | ParseException e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
