package ru.job4j.model;

import org.apache.taglibs.standard.lang.jstl.UnaryMinusOperator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "car")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String brand;
    private String model;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;
    @ManyToOne
    @JoinColumn(name = "transmission_id")
    private Transmission transmission;
    @ManyToOne
    @JoinColumn(name = "car_body_id")
    private CarBody carBody;
    private boolean sold;
    private float price;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "photo_id")
    private CarPhoto photo;

    public Car() {
        this.created = new Date();
        this.sold = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Transmission getTransmission() {
        return transmission;
    }

    public void setTransmission(Transmission transmission) {
        this.transmission = transmission;
    }

    public CarBody getCarBody() {
        return carBody;
    }

    public void setCarBody(CarBody carBody) {
        this.carBody = carBody;
    }

    public boolean isSold() {
        return sold;
    }

    public void setSold() {
        this.sold = true;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public CarPhoto getPhoto() {
        return photo;
    }

    public void setPhoto(CarPhoto photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", created=" + created +
                ", owner=" + owner +
                ", transmission=" + transmission +
                ", carBody=" + carBody +
                ", sold=" + sold +
                ", price=" + price +
                ", photo=" + photo +
                '}';
    }
}
