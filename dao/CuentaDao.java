package TP_Banco.dao;

import TP_Banco.dao.dto.CuentaDto;

import java.util.List;

public interface CuentaDao {
    void login();
    void crearCuenta(CuentaDto cuenta);
    void verSaldo(int userId);
    double depositar(int userId);
    double retirar(int userId);
    double transferir(int userId);
    List<CuentaDto>verCuentas(int userId);
    void verMovimientosUserId(int userId);
}
