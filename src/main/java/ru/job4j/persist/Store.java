package ru.job4j.persist;

import ru.job4j.model.Car;
import ru.job4j.model.CarBody;
import ru.job4j.model.Owner;
import ru.job4j.model.Transmission;

import java.util.Collection;
import java.util.Map;

public interface Store {

    Collection<Transmission> getAllTransmissions();

    Collection<CarBody> getAllCarBody();

    int addCar(Car car);

    int addOwner(Owner owner);

    Owner getOwnerByEmail(String email);

    void setCarStatusSold(int id);

    void removeCar(int id);

    CarBody getBodyById(int id);

    Transmission getTransmissionById(int id);

    int addCarPhoto();

    void updateCarPhoto(int carId, int photoId);

    Collection<Car> getCars(Map<String, String> params);

    Car getCarById(int id);
}
