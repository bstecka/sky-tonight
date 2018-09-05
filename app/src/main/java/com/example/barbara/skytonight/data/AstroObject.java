package com.example.barbara.skytonight.data;

public class AstroObject {
    private int id;
    private String name;
    private double rightAscension;
    private double decl;
    private Double azimuth;
    private Double altitude;

    public AstroObject() {
        this.id = -1;
        this.name = "Empty";
        this.rightAscension = -1;
        this.decl = -1;
    }

    public AstroObject(int id, String name) {
        this.id = id;
        this.name = name;
        this.rightAscension = -1;
        this.decl = -1;
    }

    public AstroObject(int id, String name, double rightAscension, double decl) {
        this.id = id;
        this.name = name;
        this.rightAscension = rightAscension;
        this.decl = decl;
    }

    public String getName(){
        return name;
    }

    public String getId(){
        return "" + id;
    }

    public void calculateAzAlt(double latitude, double longitude) {
    }

    @Override
    public String toString() {
        String str = name + " (" + id + "), RA: " + rightAscension + ", Decl: " + decl;
        if (azimuth != null && altitude != null)
            str += ", Azimuth: " + azimuth + ", Alt: " + altitude;
        return str;
    }
}
