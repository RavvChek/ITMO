package model;


import exceptions.WrongValuesException;

import java.io.Serializable;
import java.util.Objects;

public class Coordinates implements Serializable {

    private float x;

    private Double y; //Значение поля должно быть больше -903, Поле не может быть null

    public Coordinates(float x, Double y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return Objects.equals(x, that.x) && Objects.equals(y, that.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y.toString() +
                '}';
    }

    public static class CoordinatesValidation {
        public static void validate(Coordinates coordinates) throws WrongValuesException {
            validateX(coordinates.getX());
            validateY(coordinates.getY());
        }

        public static void validateX(float x) throws WrongValuesException {
            if (x >= Float.MAX_VALUE) {
                throw new WrongValuesException("Поле X не может быть таким большим");
            }
        }

        public static void validateY(Double y) throws WrongValuesException {
            if (y < -903 || y == null || y.compareTo(Double.MAX_VALUE) > 0 || y.isInfinite()) {
                throw new WrongValuesException("Поле coordinates_y не может быть меньше -903 или быть null");
            }
        }
    }
}
