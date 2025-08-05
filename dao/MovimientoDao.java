package TP_Banco.dao;

import TP_Banco.dao.dto.MovimientoDto;

import java.util.List;

/**
 * Interfaz que define las operaciones para gestionar los movimientos
 * bancarios del sistema.
 *
 * <p>Incluye métodos para registrar nuevos movimientos, obtener los
 * movimientos de una cuenta específica, listar todas las transacciones
 * registradas, y visualizarlas mediante una interfaz gráfica.</p>
 */
public interface MovimientoDao {
    void registrarMovimientos(MovimientoDto movimiento);
    List<MovimientoDto> obtenerMovimientosIdCuenta(int cuentaId);
    List<MovimientoDto> obtenerTransaccionesRealizadas();
    void verTransacciones();

}
