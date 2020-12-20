package ru.job4j.carstore;

import org.junit.Test;
import ru.job4j.model.*;
import ru.job4j.persist.HbmAuction;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HbmAuctionTest {

    @Test
    public void whenAddTransmissionThenGetById() {
        HbmAuction store = HbmAuction.instOf();
        store.addTransmission(Transmission.of("t1"));
        assertEquals("t1", store.getTransmissionById(1).getName());
    }

    @Test
    public void whenAddThreeTransmissionsThenGetAll() {
        HbmAuction store = HbmAuction.instOf();
        List<Transmission> expected = List.of(
                Transmission.of("t1"),
                Transmission.of("t2"),
                Transmission.of("t3")
        );
        expected.forEach(store::addTransmission);
        assertTrue(store.getAllTransmissions().containsAll(expected));
    }

    @Test
    public void whenAddOwnerThenGetByEmail() {
        HbmAuction store = HbmAuction.instOf();
        store.addOwner(Owner.of("o1", "o1@gmail.com", "123"));
        assertEquals("o1", store.getOwnerByEmail("o1@gmail.com").getName());
        assertEquals("123", store.getOwnerByEmail("o1@gmail.com").getPassword());
    }

    @Test
    public void whenAddThreeCarBodiesThenGetAll() {
        HbmAuction store = HbmAuction.instOf();
        List<CarBody> expected = List.of(
                CarBody.of("cb1"),
                CarBody.of("cb2"),
                CarBody.of("cb3")
        );
        expected.forEach(store::addCarBody);
        assertTrue(store.getAllCarBody().containsAll(expected));
    }

    @Test
    public void whenAddCarBodyThenGet() {
        HbmAuction store = HbmAuction.instOf();
        CarBody cb = CarBody.of("cb1");
        store.addCarBody(cb);
        assertEquals(cb.getName(), store.getBodyById(1).getName());
    }

    @Test
    public void whenUpdateCarThenGetCarById() {
        HbmAuction store = HbmAuction.instOf();
        Owner owner = Owner.of("еуые", "test@gmail.com", "123");
        store.addOwner(owner);
        Transmission transmission = Transmission.of("t1");
        store.addTransmission(transmission);
        CarBody carBody = CarBody.of("cb1");
        store.addCarBody(carBody);
        Car car = Car.of("kia", "ceed", owner, transmission, carBody, 100f, new CarPhoto());
        int carId = store.addCar(car);
        car.setId(carId);
        car.setSold();
        store.setCarStatusSold(carId);
        assertTrue(store.getCarById(carId).isSold());
    }

    @Test
    public void whenAddCarThenUpdateCarPhotoThenTryToGetCar() {
        HbmAuction store = HbmAuction.instOf();
        Owner owner = Owner.of("test2", "test2@gmail.com", "123");
        store.addOwner(owner);
        Transmission transmission = Transmission.of("t1");
        store.addTransmission(transmission);
        CarBody carBody = CarBody.of("cb1");
        store.addCarBody(carBody);
        Car car = Car.of("kia", "ceed", owner, transmission, carBody, 100f, null);
        int carId = store.addCar(car);
        car.setId(carId);

        int photoId = store.addCarPhoto();

        store.updateCarPhoto(carId, photoId);
        assertEquals(photoId, store.getCarById(carId).getPhoto().getId());
    }
}
