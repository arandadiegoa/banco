package TP_Banco.dao;

import TP_Banco.dao.dto.MovimientoDto;

import java.util.List;

public interface MovimientoDao {
    void registrarMovimientos(MovimientoDto movimiento);
    List<MovimientoDto> obtenerMovimientosIdCuenta(int cuentaId);
}
