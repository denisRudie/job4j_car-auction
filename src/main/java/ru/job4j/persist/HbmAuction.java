package ru.job4j.persist;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.model.*;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class HbmAuction implements Store, AutoCloseable {
    private final static HbmAuction INST = new HbmAuction();
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    private HbmAuction() {

    }

    public static HbmAuction instOf() {
        return INST;
    }

    private <T> T tx(final Function<Session, T> command) {
        T rsl;
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            rsl = command.apply(session);
            session.getTransaction().commit();
        }
        return rsl;
    }

    private void consume(final Consumer<Session> command) {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            command.accept(session);
            session.getTransaction().commit();
        }
    }

    @Override
    public void close() {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    @Override
    public Collection<Transmission> getAllTransmissions() {
        return tx(session -> session.createQuery("from Transmission ").list());
    }

    @Override
    public Collection<CarBody> getAllCarBody() {
        return tx(session -> session.createQuery("from CarBody ").list());
    }

    @Override
    public int addCar(Car car) {
        return tx(session -> (Integer) session.save(car));
    }

    @Override
    public int addOwner(Owner owner) {
        return tx(session -> (Integer) session.save(owner));
    }

    @Override
    public Owner getOwnerByEmail(String email) {
        return tx(session -> (Owner) session.createQuery("from Owner where email = :email")
                .setParameter("email", email).uniqueResult());
    }

    @Override
    public void setCarStatusSold(int id) {
        consume(session -> {
            Car car = session.get(Car.class, id);
            car.setSold();
            session.update(car);
        });
    }

    @Override
    public void removeCar(int id) {
        consume(session -> {
            Car car = session.load(Car.class, id);
            session.remove(car);
        });
    }

    @Override
    public CarBody getBodyById(int id) {
        return tx(session -> session.get(CarBody.class, id));
    }

    @Override
    public Transmission getTransmissionById(int id) {
        return tx(session -> session.get(Transmission.class, id));
    }

    @Override
    public int addCarPhoto() {
        return tx(session -> (Integer) session.save(new CarPhoto()));
    }

    @Override
    public void updateCarPhoto(int carId, int photoId) {
        consume(session -> {
            Car car = session.get(Car.class, carId);
            CarPhoto carPhoto = new CarPhoto();
            carPhoto.setId(photoId);
            car.setPhoto(carPhoto);
            session.update(carPhoto);
        });
    }

    @Override
    public Collection<Car> getCars(Map<String, String> params) {
        StringBuilder sb = new StringBuilder("select distinct c from Car c " +
                "left join fetch c.owner " +
                "left join c.carBody " +
                "left join c.transmission " +
                "where ");
        for (Map.Entry<String, String> param : params.entrySet()) {
            sb.append("c.")
                    .append(param.getKey())
                    .append(param.getValue())
                    .append(" and ");
        }
        String request = sb.toString().substring(0, sb.toString().length() - 4);
        return tx(session -> session.createQuery(request).list());
    }

    @Override
    public Car getCarById(int id) {
        return tx(session -> session.get(Car.class, id));
    }

    public void addCarBody(CarBody carBody) {
        consume(session -> session.save(carBody));
    }

    public void addTransmission(Transmission transmission) {
        consume(session -> session.save(transmission));
    }
}
