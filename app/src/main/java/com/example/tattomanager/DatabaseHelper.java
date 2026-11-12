package com.example.tattomanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Gestor de base de datos SQLite para la aplicacion TattoManagger
 **/
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tattomanager.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_COTIZACIONES = "cotizaciones";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CLIENTE = "cliente";
    public static final String COLUMN_ESTILO = "estilo";
    public static final String COLUMN_TAMANO = "tamano";
    public static final String COLUMN_FECHA_HORA = "fecha_hora";
    public static final String COLUMN_TOTAL = "total";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_COTIZACIONES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CLIENTE + " TEXT, " +
                COLUMN_ESTILO + " TEXT, " +
                COLUMN_TAMANO + " TEXT, " +
                COLUMN_FECHA_HORA + " TEXT, " +
                COLUMN_TOTAL + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COTIZACIONES);
        onCreate(db);
    }

    /**
     * Guarda una cotización en la base de datos usando ContentValues.
     *
     * @param cliente   Nombre del cliente
     * @param estilo    Estilo de tatuaje
     * @param tamano    Tamaño en cm
     * @param fechaHora Fecha agendada
     * @param total     Monto total
     * @return true si se guardó con éxito
     **/
    public boolean guardarCotizacion(String cliente, String estilo, String tamano, String fechaHora, String total) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CLIENTE, cliente);
        values.put(COLUMN_ESTILO, estilo);
        values.put(COLUMN_TAMANO, tamano);
        values.put(COLUMN_FECHA_HORA, fechaHora);
        values.put(COLUMN_TOTAL, total);

        long resultado = db.insert(TABLE_COTIZACIONES, null, values);
        db.close();
        return resultado != -1;
    }

    /**
     * obtiene todas las cotizaciones de la base de datos
     *
     * @return cursor con los resultados
     **/
    public Cursor obtenerTodasLasCotizaciones() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_COTIZACIONES + " ORDER BY " + COLUMN_ID + " DESC", null);
    }

    /**
     * actualiza una cotizacion por su ID mediante consulta parametrizada
     *
     * @param id ID del registro a actualizar
     * @return true si se actualizó el registro
     **/
    public boolean actualizarCotizacion(int id, String cliente, String estilo, String tamano, String fechaHora, String total) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CLIENTE, cliente);
        values.put(COLUMN_ESTILO, estilo);
        values.put(COLUMN_TAMANO, tamano);
        values.put(COLUMN_FECHA_HORA, fechaHora);
        values.put(COLUMN_TOTAL, total);
        int filasAfectadas = db.update(TABLE_COTIZACIONES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return filasAfectadas > 0;
    }

    /**
     * elimina una cotizacion por su ID
     *
     * @param id ID del registro a eliminar
     * @return true si se eliminó el registro
     **/
    public boolean eliminarCotizacion(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int filasEliminadas = db.delete(TABLE_COTIZACIONES, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return filasEliminadas > 0;
    }
}