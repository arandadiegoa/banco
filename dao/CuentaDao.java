package TP_Banco.dao;

import TP_Banco.dao.dto.CuentaDto;

public interface CuentaDao {
    void login();
    void crearCuenta(CuentaDto cuenta);
    void verSaldo(int userId);
    double depositar(int userId, double newSaldo);
    double retirar(int userId, double newSaldo);
    double transferir(int userId);
}
