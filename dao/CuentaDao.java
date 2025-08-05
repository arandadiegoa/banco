package TP_Banco.dao;

import TP_Banco.dao.dto.CuentaDto;

import javax.swing.*;
import java.util.List;

/**
 * Interfaz que define las operaciones relacionadas con la gestión de cuentas bancarias.
 *
 * <p>Incluye métodos para crear cuentas, consultar saldo, realizar operaciones (depósitos, retiros, transferencias),
 * y visualizar movimientos y cuentas desde la interfaz gráfica.</p>
 */

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
