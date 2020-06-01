package com.example.examenandroidgp.Activities.FourthActivity;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.examenandroidgp.Helpers.EmpleadosSQLiteHelper;
import com.example.examenandroidgp.Models.Empleado;
import com.example.examenandroidgp.Activities.FourthActivity.FourthActivityUseCase;

import java.util.ArrayList;

public class FourthActivityViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<Empleado>> empleados;
    private EmpleadosSQLiteHelper db;

    public FourthActivityViewModel(@NonNull Application application) {
        super(application);
        db = new EmpleadosSQLiteHelper(getApplication());
        empleados = new MutableLiveData<>();
    }

    public void setEmpleados(ArrayList<Empleado> empleados){
        this.empleados.setValue(empleados);
    }

    public MutableLiveData<ArrayList<Empleado>> getMutableArrayEmpleados(){
        return empleados;
    }

    public int getEmpleadosCount(){
        return db.getEmpleadosCount();
    }

    public void deleteEmpleados(){
        for (Empleado empleado: db.getAllEmpleados()) {
            db.deleteEmpleado(empleado);
        }
        Log.i("empleadosFromDelete", db.getAllEmpleados().size()+"");
        empleados.setValue(db.getAllEmpleados());
    }

    public void createEmpleados(){
        ArrayList<Empleado> empleados = FourthActivityUseCase.getStaticEmployes();
        for (Empleado empleado: empleados) {
            db.addEmpleado(empleado);
        }
        this.empleados.setValue(empleados);
    }

    public ArrayList<Empleado> getEmpleados(){
        return db.getAllEmpleados();
    }

}
