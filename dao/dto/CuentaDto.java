package TP_Banco.dao.dto;

/**
 * Clase que representa una cuenta bancaria asociada a un usuario.
 *
 * <p>Contiene el ID de la cuenta, el saldo, el ID del usuario propietario y el estado de la cuenta.</p>
 */

public class CuentaDto {
    private int id;
    private double saldo;
    private int user_id; //usuario al que pertenece la cuenta

    private String status;

    //Constructor con todos los campos
    public CuentaDto(int id, double saldo, int user_id) {
        this.id = id;
        this.saldo = saldo;
        this.user_id = user_id;
    }

    // Constructor sin ID (puede usarse al crear una cuenta nueva).
    public CuentaDto(double saldo, int user_id) {
        this.saldo = saldo;
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getSaldo() {
        return saldo;
    }

    public int getUser_id() {
        return user_id;
    }


}
