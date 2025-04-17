package org.example.part3;

public class Material {
    String name;
    double weight;
    int density;
    boolean brightness;
    double power_brightness;


    public Material(String name, double weight, int density, boolean brightness, double power_brightness) {
        this.name = name;
        this.density = density;
        this.weight = weight;
        this.brightness = brightness;
        this.power_brightness = power_brightness;
    }

    public double calcVolume() {
        return weight / density;
    }
}
