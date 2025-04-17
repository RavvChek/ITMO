package org.example.part3;

public class Planet {
    double volume;

    public Planet(double volume) {
        this.volume = volume;
    }

    public class Surface {
        Material material;

        public Surface(Material material) {
            this.material = material;
        }

        public void setMaterial(Material material) {
            this.material = material;
        }

        public boolean shine() {
            if (!material.brightness) {
                return false;
            }
            if (volume / 100 * 80 <= this.material.calcVolume() * material.power_brightness) {
                return true;
            } else {
                return false;
            }
        }
    }
}
