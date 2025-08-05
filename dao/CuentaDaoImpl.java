package TP_Banco.dao;

import TP_Banco.dao.dto.LoginResult;
import TP_Banco.dao.dto.MovimientoDto;
import TP_Banco.exception.ErrorConexionDB;
import TP_Banco.dao.dto.CuentaDto;
import TP_Banco.db.DataBaseConexion;


import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Implementación de la interfaz {@link CuentaDao} que gestiona las operaciones
 * sobre cuentas bancarias utilizando JDBC y componentes Swing para la interfaz gráfica.
 *
 * <p>Incluye funcionalidades como login de usuario, creación de cuentas,
 * consulta de saldo, movimientos, transferencias, retiros y depósitos.</p>
 *
 * <p>Usa {@link DataBaseConexion} para conectarse a la base de datos,
 * y delega el registro de movimientos a {@link MovimientoDaoImpl}.</p>
 *
 * @author Diego
 */

public class CuentaDaoImpl implements CuentaDao{
    private UserDao userDao;
    private final MovimientoDao movimientoDao = new MovimientoDaoImpl();
    Scanner sc = new Scanner(System.in);

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
    public CuentaDaoImpl(){}

    //Inicia sesión de un usuario validando sus credenciales..
    public LoginResult login(String email, String pass){
        try {

            int userId = userDao.findUserId(email, pass);
            if(userId == -1){
                return new LoginResult(-1, null, false, "Credenciales incorrectas");
            }

            String rol = userDao.obtenerRolPorId(userId);

            return new LoginResult(userId, rol, true, null);
        }catch (Exception e) {
            return new LoginResult(-1, null, false, "Error en el login: " + e.getMessage());
        }
    }

    // Crea una nueva cuenta bancaria para un usuario específico.
    public void crearCuenta(JFrame parent,CuentaDto cuenta) {

        try {
            Connection conn = DataBaseConexion.getInstance().getConexion();
            String sql = "INSERT INTO cuenta(saldo, user_id) VALUES(?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, cuenta.getSaldo());
            stmt.setInt(2, cuenta.getUser_id());


            int filas = stmt.executeUpdate();
            if(filas > 0){
                JOptionPane.showMessageDialog(parent,
                        "Saldo inicial: " + cuenta.getSaldo() +
                                ", asociada al usuario con ID: " + cuenta.getUser_id(),
                        "Cuenta creada",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
            stmt.close();
            conn.close();
        }catch (SQLException | ErrorConexionDB e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent,
                    "Error al crear la cuenta: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                    );
        }
    }

    //Recupera y muestra todas las cuentas asociadas a un usuario.
    public List<CuentaDto>verCuentas(JFrame parent, int userId) {

        List<CuentaDto> cuentasUsuario = new ArrayList<>(); //Guardar las cuentas del usuario
        StringBuilder mensaje = new StringBuilder();

        //Ver cuentas
        String sql = "SELECT * FROM cuenta WHERE user_id = ?";
        try {
            Connection conn = DataBaseConexion.getInstance().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int cuentaId = rs.getInt("id");
                double saldo = rs.getDouble("saldo");
                int dbUserId = rs.getInt("user_id");
                String status = rs.getString("estado");
                cuentasUsuario.add(new CuentaDto(cuentaId, saldo, dbUserId));

                mensaje.append("Cuenta nro: ").append(cuentaId)
                        .append(" - Saldo: $").append(saldo)
                        .append(" - Estado: ").append(status).append("\n");

            }
            if(cuentasUsuario.isEmpty()){
                JOptionPane.showMessageDialog(parent, "No tiene cuentas registradas.");
            }else {
                JOptionPane.showMessageDialog(parent, mensaje.toString(), "Cuentas del usuario", JOptionPane.INFORMATION_MESSAGE);
            }
            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException | ErrorConexionDB e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent,
                    "Error al recuperar cuentas: " + e.getMessage(),
                    "Error",
            JOptionPane.ERROR_MESSAGE);
        }
        return cuentasUsuario;
    }

    // Muestra los movimientos de todas las cuentas del usuario.
    public void verMovimientosUserId(JFrame parent, int userId) {
        List<CuentaDto> cuentas = verCuentas(parent, userId);

        if(cuentas.isEmpty()){
            JOptionPane.showMessageDialog(parent, "No tiene cuentas registradas.");
            return;
        }

        StringBuilder mensajeTotal = new StringBuilder();

        for (CuentaDto cuenta : cuentas){
            int cuentaId = cuenta.getId();

            List<MovimientoDto> movimientos = movimientoDao.obtenerMovimientosIdCuenta(cuentaId);
            mensajeTotal.append("Movimientos de la cuenta nro: ").append(cuentaId).append("\n");

            if(movimientos.isEmpty()){
                mensajeTotal.append("No tiene movimientos registrados");
            }else{
                for(MovimientoDto mov : movimientos){
                   mensajeTotal.append(" [").append(mov.getFecha()).append("]")
                           .append(mov.getTipo()).append(mov.getMonto())
                           .append(" (").append(mov.getDescription()).append(")\n");
                }
            }
        }
        JTextArea textArea = new JTextArea(mensajeTotal.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        JOptionPane.showMessageDialog(parent, scrollPane, "Movimientos de cuenta", JOptionPane.INFORMATION_MESSAGE);
    }

    //Permite a empleado gestionar el estado de las cuentas de un usuario
    public void gestionarCuentasUsuariosDesdeGUI(JFrame parent, int userId){
        //Ver cuentas
        List<CuentaDto>cuentaDtos = verCuentas(parent, userId);

        if(cuentaDtos.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "El usuario no tiene cuentas asociadas");
            return;
        }

        //Mostrar cuentas
        String[] opciones = cuentaDtos.stream()
                .map(c -> "Cuenta #" + c.getId() + " - $" + c.getSaldo())
                .toArray(String[]::new);

        String seleccion = (String) JOptionPane.showInputDialog(
                parent,
                "Seleccione una cuenta:",
                "Gestion de cuentas",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if(seleccion == null) return;

        int cuentaId = Integer.parseInt(seleccion.split("#")[1].split(" ")[0]);

        //Elegir accion
        String[] acciones = {"Habilitar", "Bloquear", "Cerrar"};
        int opcion = JOptionPane.showOptionDialog(
                parent,
                "Seleccione una acción para la cuenta " + cuentaId,
                "Acción",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                acciones,
                acciones[0]
        );

        if(opcion < 0) return;

        String nuevoEstado = switch (opcion) {
            case 0 -> "activa";
            case 1 -> "bloqueada";
            case 2 -> "cerrada";
            default -> "";
        };

        // Conexión a la base de datos
        String sql = "UPDATE cuenta SET estado = ? WHERE id = ?";
        try (Connection conn = DataBaseConexion.getInstance().getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, cuentaId);

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                JOptionPane.showMessageDialog(parent, "La cuenta fue actualizada a estado: " + nuevoEstado);
            } else {
                JOptionPane.showMessageDialog(parent, "No se pudo actualizar la cuenta.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Error al actualizar cuenta: " + e.getMessage());
        }

    }

    //Consulta y muestra el saldo de todas las cuentas del usuario
    public void verSaldo(JFrame parent, int userId) {

        String sql= "SELECT * FROM cuenta WHERE user_id = ?";

        try {
            Connection conn = DataBaseConexion.getInstance().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            boolean tieneDatos = false;
            StringBuilder mensaje = new StringBuilder();

                while (rs.next()) {
                    int cuentaId = rs.getInt("id");
                    double saldo = rs.getDouble("saldo");
                    String status = rs.getString("estado");
                    tieneDatos = true;

                    mensaje.append("Cuenta nro: ").append(cuentaId)
                            .append(" - Saldo disponible: $").append(saldo)
                            .append(" - Estado: ").append(status).append("\n");
                }
                rs.close();
                stmt.close();
                conn.close();

                if(!tieneDatos){
                    //Si no tiene cuenta
                    JOptionPane.showMessageDialog(parent,
                            "Debe crear una cuenta y realizar un depósito.",
                                "Sin cuentas",
                                JOptionPane.INFORMATION_MESSAGE);
                }else {
                    JOptionPane.showMessageDialog(parent,
                            mensaje.toString(),
                            "Saldos de cuentas",
                            JOptionPane.INFORMATION_MESSAGE);
                }

        }catch (SQLException | ErrorConexionDB e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent,
                    "Error al consultar saldo: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    //Realiza un depósito en una cuenta del usuario
    public double depositar(JFrame parent, int userId) {

        //Ver cuentas
        List<CuentaDto>cuentaDtos = verCuentas(parent, userId);

        if(cuentaDtos.isEmpty()){
            JOptionPane.showMessageDialog(parent, "No tiene cuentas para depositar.");
            return 0;
        }

        try {

            Connection conn = DataBaseConexion.getInstance().getConexion();
            int cuentaId;

            if (cuentaDtos.size() > 1) {
                String[] opciones = cuentaDtos.stream()
                        .map(c -> "Cuenta #" + c.getId() + " - Saldo: $" + c.getSaldo())
                        .toArray(String[]::new);

                String seleccion = (String) JOptionPane.showInputDialog(
                        parent,
                        "Seleccione una cuenta para depositar:",
                        "Seleccionar cuenta",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        opciones,
                        opciones[0]
                );

                if (seleccion == null) return 0;
                cuentaId = Integer.parseInt(seleccion.split("#")[1].split(" ")[0]);
            } else {
                cuentaId = cuentaDtos.get(0).getId();
            }

            //Solicitar monto a depositar
            String montoStr = JOptionPane.showInputDialog(parent, "Ingrese el monto a depositar:");
            if (montoStr == null) return 0;

            double dinero = Double.parseDouble(montoStr);
            if (dinero <= 0) {
                JOptionPane.showMessageDialog(parent, "Monto inválido. Debe ser mayor a 0.");
            }

            //Depositar

            String sqlDepositar = "UPDATE cuenta SET saldo = saldo + ? WHERE id = ?";
            PreparedStatement stmSumar = conn.prepareStatement(sqlDepositar);
            stmSumar.setDouble(1, dinero);
            stmSumar.setInt(2, cuentaId);
            stmSumar.executeUpdate();

            //Registrar movimientos
            MovimientoDto movimiento = new MovimientoDto(
                    cuentaId,
                    "Deposito",
                    dinero,
                    "Deposito realizado en cuenta"
            );
            movimientoDao.registrarMovimientos(movimiento);

            JOptionPane.showMessageDialog(parent, "Deposito exitoso de $" +
                    dinero +
                    " en cuenta #" + cuentaId);

            conn.close();
        }catch (NumberFormatException e){
            JOptionPane.showMessageDialog(parent, "Debe ingresar un número valido.",
                    "Error de entrada", JOptionPane.ERROR_MESSAGE);
        }catch (SQLException | ErrorConexionDB e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Error al realizar depósito: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        return 0;
    }

    //Permite al usuario retirar dinero de una de sus cuentas.
    public double retirar(JFrame parent, int userId) {
        //Ver cuentas
        List<CuentaDto>cuentaDtos = verCuentas(parent, userId);

        if(cuentaDtos.isEmpty()){
            JOptionPane.showMessageDialog(parent, "No tiene cuentas registradas.");
            return 0;
        }


        try {

            Connection conn = DataBaseConexion.getInstance().getConexion();
            int cuentaId;

            //Seleccionar cuenta si hay mas de una
            if (cuentaDtos.size() > 1) {
                String[] opciones = cuentaDtos.stream()
                        .map(c -> "Cuenta #" + c.getId() + " - Saldo: $" + c.getSaldo())
                        .toArray(String[]::new);

                String seleccion = (String) JOptionPane.showInputDialog(
                        parent,
                        "Seleccione una cuenta para retirar:",
                        "Seleccione cuenta",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        opciones,
                        opciones[0]
                );

                if (seleccion == null) return 0;
                cuentaId = Integer.parseInt(seleccion.split("#")[1].split(" ")[0]);
            } else {
                cuentaId = cuentaDtos.get(0).getId();
            }

            //Validar saldo disponible

            String validarSaldoSql = "SELECT * FROM cuenta WHERE id= ? AND user_id = ?";
            PreparedStatement stmtValidar = conn.prepareStatement(validarSaldoSql);
            stmtValidar.setInt(1, cuentaId);
            stmtValidar.setInt(2, userId);
            ResultSet rsVerificar = stmtValidar.executeQuery();

            if (rsVerificar.next()) {
                double saldoActual = rsVerificar.getDouble("saldo");

                if (saldoActual <= 0) {
                    JOptionPane.showMessageDialog(parent, "Saldo insuficiente en la cuenta.");
                    return 0;
                }

                String montoStr = JOptionPane.showInputDialog(parent, "Ingrese el monto a retirar:");
                if (montoStr == null) return 0;

                double dinero = Double.parseDouble(montoStr);

                if (dinero <= 0) {
                    JOptionPane.showMessageDialog(parent, "Monto inválido. Debe ser mayor a 0.");
                    return 0;
                }

                //Ejecutar retiro

                String sqlRetirar = "UPDATE cuenta SET saldo = saldo - ? WHERE id= ?";
                PreparedStatement stmtRetirar = conn.prepareStatement(sqlRetirar);
                stmtRetirar.setDouble(1, dinero);
                stmtRetirar.setInt(2, cuentaId);
                stmtRetirar.executeUpdate();

                //Registro de movimientos
                MovimientoDto movimiento = new MovimientoDto(
                        cuentaId,
                        "Retiro",
                        dinero,
                        "Retiro realizado en cuenta"
                );

                movimientoDao.registrarMovimientos(movimiento);

                JOptionPane.showMessageDialog(parent, "Se retiraron $" + dinero +
                        "correctamente de la cuenta #" + cuentaId);

            } else {
                JOptionPane.showMessageDialog(parent, "No se encontro la cuenta");
            }
            rsVerificar.close();
            stmtValidar.close();
            conn.close();

        }catch (NumberFormatException e){
            JOptionPane.showMessageDialog(parent, "Monto inválido. Debe ser un número.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }catch (SQLException | ErrorConexionDB e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Error al retirar dinero: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return 0;
    }

    //Realiza una transferencia entre dos cuentas del mismo usuario.
    public double transferir(JFrame parent, int userId) {
        String sqlCuentas = "SELECT * FROM cuenta WHERE user_id = ?";
        try (Connection conn = DataBaseConexion.getInstance().getConexion()) {

            // 1. Obtener cuentas
            List<CuentaDto> cuentas = new ArrayList<>();
            try (PreparedStatement stmt = conn.prepareStatement(sqlCuentas)) {
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    cuentas.add(new CuentaDto(rs.getInt("id"), rs.getDouble("saldo"), userId));
                }
            }

            if (cuentas.size() < 2) {
                JOptionPane.showMessageDialog(parent, "Necesita al menos 2 cuentas para transferir.");
                return 0;
            }

            // 2. Selección cuentas origen y destino
            String[] opcionesOrigen = cuentas.stream()
                    .map(c -> "Cuenta #" + c.getId() + " - Saldo: $" + c.getSaldo())
                    .toArray(String[]::new);
            String seleccionOrigen = (String) JOptionPane.showInputDialog(parent, "Seleccione cuenta origen:",
                    "Cuenta Origen", JOptionPane.QUESTION_MESSAGE, null, opcionesOrigen, opcionesOrigen[0]);
            if (seleccionOrigen == null) return 0;
            int cuentaOrigenId = Integer.parseInt(seleccionOrigen.split("#")[1].split(" ")[0]);

            String[] opcionesDestino = cuentas.stream()
                    .filter(c -> c.getId() != cuentaOrigenId)
                    .map(c -> "Cuenta #" + c.getId() + " - Saldo: $" + c.getSaldo())
                    .toArray(String[]::new);
            String seleccionDestino = (String) JOptionPane.showInputDialog(parent, "Seleccione cuenta destino:",
                    "Cuenta Destino", JOptionPane.QUESTION_MESSAGE, null, opcionesDestino, opcionesDestino[0]);
            if (seleccionDestino == null) return 0;
            int cuentaDestinoId = Integer.parseInt(seleccionDestino.split("#")[1].split(" ")[0]);

            // 3. Monto
            String montoStr = JOptionPane.showInputDialog(parent, "Ingrese el monto a transferir:");
            if (montoStr == null) return 0;
            double dinero = Double.parseDouble(montoStr);
            if (dinero <= 0) {
                JOptionPane.showMessageDialog(parent, "Monto inválido. Debe ser mayor a 0.");
                return 0;
            }

            // 4. Validar saldo y hacer transferencia
            conn.setAutoCommit(false);
            try {
                // Validar saldo origen
                double saldoOrigen;
                String validarSql = "SELECT saldo FROM cuenta WHERE id = ? AND user_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(validarSql)) {
                    stmt.setInt(1, cuentaOrigenId);
                    stmt.setInt(2, userId);
                    ResultSet rs = stmt.executeQuery();
                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(parent, "Cuenta origen no encontrada.");
                        return 0;
                    }
                    saldoOrigen = rs.getDouble("saldo");
                }
                if (saldoOrigen < dinero) {
                    JOptionPane.showMessageDialog(parent, "Saldo insuficiente. Disponible: $" + saldoOrigen);
                    return 0;
                }

                // Retirar
                String retirarSql = "UPDATE cuenta SET saldo = saldo - ? WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(retirarSql)) {
                    stmt.setDouble(1, dinero);
                    stmt.setInt(2, cuentaOrigenId);
                    stmt.executeUpdate();
                }

                // Depositar
                String depositarSql = "UPDATE cuenta SET saldo = saldo + ? WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(depositarSql)) {
                    stmt.setDouble(1, dinero);
                    stmt.setInt(2, cuentaDestinoId);
                    stmt.executeUpdate();
                }

                conn.commit();

                JOptionPane.showMessageDialog(parent,
                        "Transferencia exitosa de $" + dinero +
                                " de cuenta #" + cuentaOrigenId + " a cuenta #" + cuentaDestinoId);

                return dinero;

            } catch (Exception e) {
                conn.rollback();
                JOptionPane.showMessageDialog(parent, "Error en la transferencia: " + e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent, "Error de conexión: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        return 0;
    }

}
