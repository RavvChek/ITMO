package org.example.part3;

public class Character {
    String name;
    int age;
    boolean onFeet;

    public Character(String name, int age) {
        this.name = name;
        this.age = age;
        onFeet = false;
    }

    public String getName() {
        return name;
    }

    public void standUp() {
        this.onFeet = true;
    }

    public boolean getBlinded(Planet.Surface surface) {
        if (surface.material.brightness && surface.material.power_brightness >= 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean lookAround() {
        if (onFeet) {
            System.out.println(this.name + " looking around\n");
            return true;
        } else {
            return false;
        }
    }
}
