package ru.job4j.persist;

import ru.job4j.model.*;

import java.util.Collection;

public interface Store {

    Collection<Car> getAllCars();

    Collection<Car> getAllCarsByUser(Owner owner);

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

    Car getCarById(int id);

    void updateCarPhoto(int carId, int photoId);
}
