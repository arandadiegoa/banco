package TP_Banco.dao;

import TP_Banco.dao.dto.CuentaDto;

import javax.swing.*;
import java.util.List;

public interface CuentaDao {
    void crearCuenta(JFrame parent, CuentaDto cuenta);
    void verSaldo(JFrame parent, int userId);
    double depositar(JFrame parent, int userId);
    double retirar(JFrame parent, int userId);
    double transferir(JFrame parent, int userId);
    List<CuentaDto>verCuentas(JFrame parent, int userId);
    void verMovimientosUserId(JFrame parent, int userId);
    void gestionarCuentasUsuariosDesdeGUI(JFrame parent, int userId);
}
