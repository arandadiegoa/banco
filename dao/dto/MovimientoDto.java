package TP_Banco.dao.dto;

import java.sql.Timestamp;

/**
 * Clase que representa un movimiento asociado a una cuenta bancaria.
 *
 * <p>Incluye información como el ID del movimiento, ID de la cuenta, tipo de operación,
 * monto, fecha y una descripción.</p>
 */

public class MovimientoDto {
    private int id;
    private int cuentaId; //id de la cuenta asociada
    private String tipo; //tipo de movimiento: depósito, retiro
    private double monto;
    private Timestamp fecha;
    private String description;

    //Constructor para representar un movimiento existente
    public MovimientoDto(int id, int cuentaId, String tipo, double monto, Timestamp fecha, String description) {
        this.id = id;
        this.cuentaId = cuentaId;
        this.tipo = tipo;
        this.monto = monto;
        this.fecha = fecha;
        this.description = description;
    }

    //Para insertar nuevos movimientos sin id y fecha
    public MovimientoDto(int cuentaId, String tipo, double monto, String description) {
        this.cuentaId = cuentaId;
        this.tipo = tipo;
        this.monto = monto;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCuentaId() {
        return cuentaId;
    }


    public String getTipo() {
        return tipo;
    }


    public double getMonto() {
        return monto;
    }


    public Timestamp getFecha() {
        return fecha;
    }


    public String getDescription() {
        return description;
    }

}
