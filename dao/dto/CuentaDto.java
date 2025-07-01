package TP_Banco.dao.dto;

public class CuentaDto {
    private int id;
    private double saldo;
    private int user_id;

    public CuentaDto(int id, double saldo, int user_id) {
        this.id = id;
        this.saldo = saldo;
        this.user_id = user_id;
    }

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

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
