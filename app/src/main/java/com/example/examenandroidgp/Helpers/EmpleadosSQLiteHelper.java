package com.example.examenandroidgp.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.examenandroidgp.Models.Empleado;

import java.util.ArrayList;
import java.util.List;

public class EmpleadosSQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ExamenDB";
    private static final String TABLE_EMPLEADOS = "empleados";
    private static final String KEY_ID = "id_empleado";
    private static final String KEY_NOMBRE = "nombre";
    private static final String KEY_FECHA_NACIMIENTO = "fecha_nacimiento";
    private static final String KEY_PUESTO = "puesto";

    public EmpleadosSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_EMPLEADOS_TABLE = "CREATE TABLE "+TABLE_EMPLEADOS+"("
                +KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                +KEY_NOMBRE+" TEXT,"
                +KEY_FECHA_NACIMIENTO+" TEXT,"
                +KEY_PUESTO+" TEXT"+")";
        db.execSQL(CREATE_EMPLEADOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_EMPLEADOS);
        onCreate(db);
    }

    public void addEmpleado(Empleado empleado) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NOMBRE, empleado.getNombre());
        values.put(KEY_FECHA_NACIMIENTO, empleado.getFechaNacimiento());
        values.put(KEY_PUESTO, empleado.getPuesto());

        db.insert(TABLE_EMPLEADOS, null, values);
        db.close();
    }

    public void deleteEmpleado(Empleado empleado) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EMPLEADOS, KEY_ID + " = ?",
                new String[] {String.valueOf(empleado.getId())});
        db.close();
    }

    public ArrayList<Empleado> getAllEmpleados() {
        ArrayList<Empleado> empleadosList = new ArrayList<Empleado>();

        String selectQuery = "SELECT * FROM " + TABLE_EMPLEADOS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Empleado empleado = new Empleado(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3));
                empleadosList.add(empleado);
            } while (cursor.moveToNext());
        }

        return empleadosList;
    }

    public int getEmpleadosCount() {
        int count = 0;
        String countQuery = "SELECT * FROM " + TABLE_EMPLEADOS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();

        return count;
    }
}
