package TP_Banco.dao;

import TP_Banco.dao.dto.CuentaDto;

public interface CuentaDao {
    void crearCuenta(CuentaDto cuenta);
    void verSaldo();

    double depositar(int userId, double newSaldo);
    double retirar(int userId, double newSaldo);
}
