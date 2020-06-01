package com.example.examenandroidgp.Activities.FourthActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.examenandroidgp.Adapters.ListViewEmpleadosAdapter;
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

    private ListViewEmpleadosAdapter adapter;
    private Context context;
    private FourthActivityViewModel fourthActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);
        ButterKnife.bind(this);
        context = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fourthActivityViewModel = new ViewModelProvider(this).get(FourthActivityViewModel.class);
        Log.i("ViewModel", fourthActivityViewModel+"");

        Observer<ArrayList<Empleado>> observerEmpleados = new Observer<ArrayList<Empleado>>() {
            @Override
            public void onChanged(ArrayList<Empleado> empleadosInObserver) {
                Log.i("EmpleadosObserver",empleadosInObserver.size()+"");
                if (adapter == null){
                    adapter = new ListViewEmpleadosAdapter(context, new ArrayList<Empleado>(), R.layout.list_view_item);
                    listViewEmpleados.setAdapter(adapter);
                }
                if (empleadosInObserver.isEmpty()){
                    btnCreateEmpleados.setVisibility(View.VISIBLE);
                    btnDeleteEmpleados.setVisibility(View.GONE);
                    Toast.makeText(context, "Entra delete", Toast.LENGTH_SHORT).show();
                    adapter.clear();
                }else{
                    btnCreateEmpleados.setVisibility(View.GONE);
                    btnDeleteEmpleados.setVisibility(View.VISIBLE);
                    Toast.makeText(context, "Entra add", Toast.LENGTH_SHORT).show();
                    adapter.addAll(empleadosInObserver);
                }
                adapter.notifyDataSetChanged();
            }
        };
        fourthActivityViewModel.getMutableArrayEmpleados().observe(this, observerEmpleados);

        if (fourthActivityViewModel.getEmpleadosCount() > 0){
            fourthActivityViewModel.setEmpleados(fourthActivityViewModel.getEmpleados());
        }
    }

    @OnClick(R.id.btnCreateEmpleados)
    public void createEmpleados(){
        fourthActivityViewModel.createEmpleados();
    }

    @OnClick(R.id.btnDeleteEmpleados)
    public void deleteEmpleados(){
        fourthActivityViewModel.deleteEmpleados();
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
