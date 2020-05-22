package com.example.examenandroidgp.Models;

import java.io.Serializable;

public class Empleado implements Serializable {

    private int id;
    private String nombre;
    private String fechaNacimiento;
    private String puesto;

    public Empleado(int id, String nombre, String fechaNacimiento, String puesto){
        this.id = id;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.puesto = puesto;
    }

    public Empleado(String nombre, String fechaNacimiento, String puesto){
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.puesto = puesto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }
}
