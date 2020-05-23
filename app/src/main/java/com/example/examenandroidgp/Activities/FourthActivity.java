package com.example.examenandroidgp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.examenandroidgp.Adapters.ListViewEmpleadosAdapter;
import com.example.examenandroidgp.Helpers.EmpleadosSQLiteHelper;
import com.example.examenandroidgp.Models.Empleado;
import com.example.examenandroidgp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FourthActivity extends AppCompatActivity {

    @BindView(R.id.listViewEmpleados)
    ListView listViewEmpleados;
    @BindView(R.id.btnCreateEmpleados)
    Button btnCreateEmpleados;
    @BindView(R.id.btnDeleteEmpleados)
    Button btnDeleteEmpleados;

    private EmpleadosSQLiteHelper db;
    private ListViewEmpleadosAdapter adapter;
    private ArrayList<Empleado> empleados;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);
        ButterKnife.bind(this);
        context = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new EmpleadosSQLiteHelper(context);
        if (db.getEmpleadosCount() > 0){
            setListViewEmpleados();
        }
    }

    @OnClick(R.id.btnCreateEmpleados)
    public void createEmpleados(){
        db.addEmpleado(new Empleado("Miguel Cervantes", "08-Dic-1990", "Desarrollador"));
        db.addEmpleado(new Empleado("Juan Morales", "03-Jul-1990", "Desarrollador"));
        db.addEmpleado(new Empleado("Roberto Méndez", "14-Oct-1990", "Desarrollador"));
        db.addEmpleado(new Empleado("Miguel Cuevas", "08-Dic-1990", "Desarrollador"));

        if (adapter != null){
            empleados = db.getAllEmpleados();
            adapter.notifyDataSetChanged();
        }else{
            setListViewEmpleados();
        }
    }

    @OnClick(R.id.btnDeleteEmpleados)
    public void deleteEmpleados(){
        for (int i = 0; i < empleados.size(); i ++){
            db.deleteEmpleado(empleados.get(i));
        }
        empleados.removeAll(empleados);
        adapter.notifyDataSetChanged();
        btnCreateEmpleados.setVisibility(View.VISIBLE);
        btnDeleteEmpleados.setVisibility(View.GONE);
    }

    private void setListViewEmpleados(){
        empleados = db.getAllEmpleados();
        adapter = new ListViewEmpleadosAdapter(context, empleados, R.layout.list_view_item);
        listViewEmpleados.setAdapter(adapter);

        adapter.notifyDataSetChanged();
        btnCreateEmpleados.setVisibility(View.GONE);
        btnDeleteEmpleados.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
