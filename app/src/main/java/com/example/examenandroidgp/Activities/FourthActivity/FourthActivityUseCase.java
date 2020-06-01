package com.example.examenandroidgp.Activities.FourthActivity;

import com.example.examenandroidgp.Models.Empleado;

import java.util.ArrayList;

public class FourthActivityUseCase {

    public static ArrayList<Empleado> getStaticEmployes(){
        ArrayList<Empleado> empleados = new ArrayList<>();
        empleados.add(new Empleado("Miguel Cervantes", "08-Dic-1990", "Desarrollador"));
        empleados.add(new Empleado("Juan Morales", "03-Jul-1990", "Desarrollador"));
        empleados.add(new Empleado("Roberto Méndez", "14-Oct-1990", "Desarrollador"));
        empleados.add(new Empleado("Miguel Cuevas", "08-Dic-1990", "Desarrollador"));
        return empleados;
    }
}
