package TP_Banco.dao;

import TP_Banco.dao.dto.CuentaDto;

public interface CuentaDao {
    void crearCuenta(CuentaDto cuenta);
    void verSaldo();
}
