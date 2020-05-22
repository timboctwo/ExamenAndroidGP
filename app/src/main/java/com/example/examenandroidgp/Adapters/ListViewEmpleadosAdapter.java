package com.example.examenandroidgp.Adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.examenandroidgp.Models.Empleado;
import com.example.examenandroidgp.R;

import java.util.ArrayList;

public class ListViewEmpleadosAdapter extends ArrayAdapter<Empleado> {

    private Context context;
    private ArrayList<Empleado>empleados;
    private int layout;

    public ListViewEmpleadosAdapter(Context context, ArrayList<Empleado> empleados, int layout){
        super(context, layout, empleados);
        this.context = context;
        this.empleados = empleados;
        this.layout = layout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Empleado empleado = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, parent, false);
            viewHolder.textViewDatosPersonales = (TextView) convertView.findViewById(R.id.textViewDatosPersonales);
            viewHolder.textViewPuesto = (TextView) convertView.findViewById(R.id.textViewPuesto);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textViewDatosPersonales.setText(empleado.getNombre()+" - "+empleado.getFechaNacimiento());
        viewHolder.textViewPuesto.setText(empleado.getPuesto());
        // Return the completed view to render on screen
        return convertView;
    }

    private static class ViewHolder {
        private TextView textViewDatosPersonales;
        private TextView textViewPuesto;
    }
}
