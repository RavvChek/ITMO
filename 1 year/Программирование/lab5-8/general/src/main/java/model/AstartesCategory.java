package model;

import java.io.Serializable;

public enum AstartesCategory implements Serializable {
    SCOUT("scout"),
    ASSAULT("assault"),
    SUPPRESSOR("suppressor"),
    APOTHECARY("apothecary");
    private String name;

    AstartesCategory(String name) {
        this.name = name;
    }
}
