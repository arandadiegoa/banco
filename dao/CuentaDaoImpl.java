package TP_Banco.dao;

import TP_Banco.dao.dto.MovimientoDto;
import TP_Banco.exception.InvalidIngressException;
import TP_Banco.exception.ErrorConexionDB;
import TP_Banco.dao.dto.CuentaDto;
import TP_Banco.db.DataBaseConexion;
import TP_Banco.utils.Validator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CuentaDaoImpl implements CuentaDao{
    private UserDao userDao;
    private final MovimientoDao movimientoDao = new MovimientoDaoImpl();
    Scanner sc = new Scanner(System.in);

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
    public CuentaDaoImpl(){}

    @Override
    public void login() {
        String email, pass;

        do {
            System.out.println("Ingrese su email: ");
            email = sc.nextLine();
            System.out.println("Ingrese su pass: ");
            pass = sc.nextLine();
            try {
                if (!Validator.isEmailFormatValid(email) && !Validator.isPasswordRequirementsValid(pass)) {
                    throw new InvalidIngressException("No cumple los parametros establecidos");
                }
            } catch (InvalidIngressException e) {
                System.out.println("Error: " + e.getMessage());
            }

        } while (!Validator.isEmailFormatValid(email) && !Validator.isPasswordRequirementsValid(pass));


        int userId = userDao.searchUsers(email, pass);

        if(userId == -1){
            System.out.println("No se pudo autenticar ni registrar al usuario");
            return;
        }

        String rol = userDao.obtenerRolPorId(userId);
        if("empleado".equalsIgnoreCase(rol)){

            //Funcionalidad
            System.out.println("""
                Menu de opciones
                1) Transacciones registradas
                2) Habilitar / DesHabilitar cuenta
                3) Salir
                Ingrese la opción deseada: 1, 2, 3
            """);

            while (true) {
                int nro = sc.nextInt();
                switch (nro) {
                    case 1 -> {
                        System.out.println("Transacciones registradas");
                        movimientoDao.verTransacciones();

                    }
                    case 2 -> {
                        System.out.println("Habilitar / DesHabilitar cuenta");
                        System.out.println("Ingrese el id del usuario a gestionar");
                        int idUser = sc.nextInt();
                        sc.nextLine();
                        gestionarCuentasUsuarios(idUser);
                    }
                    case 3 -> {
                        System.out.println("Gracias por usar el sistema, hasta la próxima!!");
                        return;
                    }
                    default -> System.out.println("Opción inválida, intente nuevamente");
                }
                System.out.println("Ingrese una opción para continuar");
            }
        }else{
            //Funcionalidad
            System.out.println("""
                Menu de opciones
                1) Crear cuenta
                2) Ver saldo
                3) Depositar efectivo
                4) Retirar efectivo
                5) Transferir
                6) Ver momimientos
                7) Salir
                Ingrese la opción deseada: 1, 2, 3, 4, 5, 6 o 7
            """);

            while (true) {
                int nro = sc.nextInt();
                switch (nro) {
                    case 1 -> {
                        System.out.println("Crear cuenta");
                        System.out.println("Ingrese el monto en pesos");
                        double saldo = sc.nextDouble();
                        crearCuenta(new CuentaDto(saldo, userId));
                    }
                    case 2 -> {
                        System.out.println("Ver saldo");
                        verSaldo(userId);
                    }
                    case 3 -> {
                        System.out.println("Depositar efectivo");
                        depositar(userId);
                        verSaldo(userId);

                    }
                    case 4 -> {
                        System.out.println("Retirar efectivo");
                        retirar(userId);
                        verSaldo(userId);
                    }
                    case 5 -> {
                        System.out.println("Transferencias");
                        transferir(userId);
                    }
                    case 6 -> {
                        System.out.println("Ver movimientos");
                        verMovimientosUserId(userId);
                    }
                    case 7 -> {
                        System.out.println("Gracias por usar el sistema, hasta la próxima!!");
                        return;
                    }
                    default -> System.out.println("Opción inválida, intente nuevamente");
                }
                System.out.println("Ingrese una opción para continuar");
            }
        }

    }

    @Override
    public void crearCuenta(CuentaDto cuenta) {

        try {
            Connection conn = DataBaseConexion.getInstance().getConexion();
            String sql = "INSERT INTO cuenta(saldo, user_id) VALUES(?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, cuenta.getSaldo());
            stmt.setInt(2, cuenta.getUser_id());


            int filas = stmt.executeUpdate();
            if(filas > 0){
                System.out.println("Saldo inicial: " + cuenta.getSaldo() + ", asociada al usuario con ID: " + cuenta.getUser_id());
            }
            stmt.close();
            conn.close();
        }catch (SQLException | ErrorConexionDB e){
            e.printStackTrace();
        }
    }

    @Override
    public List<CuentaDto>verCuentas(int userId) {

        List<CuentaDto> cuentasUsuario = new ArrayList<>(); //Guardar las cuentas del usuario
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
                System.out.println("Cuentas disponibles: ");
                if(saldo > 0){
                    System.out.println("Cuenta nro: " + cuentaId + " tiene un saldo disponible de " + saldo + " pesos");
                    System.out.println("Estado: " + status);
                }else{
                    System.out.println("Saldo insuficiente");
                }
            }

        } catch (SQLException | ErrorConexionDB e) {
            e.printStackTrace();
        }
        return cuentasUsuario;
    }

    @Override
    public void verMovimientosUserId(int userId) {
        List<CuentaDto> cuentas = verCuentas(userId);

        if(cuentas.isEmpty()){
            System.out.println("No tiene cuentas registradas");
            return;
        }

        for (CuentaDto cuenta : cuentas){
            int cuentaId = cuenta.getId();

            List<MovimientoDto> movimientos = movimientoDao.obtenerMovimientosIdCuenta(cuentaId);
            System.out.println("Mivimientos de la cuenta nro: " + cuentaId);

            if(movimientos.isEmpty()){
                System.out.println("No tiene movimientos registrados");
            }else{
                for(MovimientoDto mov : movimientos){
                    System.out.println("  [" + mov.getFecha() + "] " +
                            mov.getTipo() + " - $" + mov.getMonto() +
                            " (" + mov.getDescription() + ")");
                }
            }
        }
    }

    public void gestionarCuentasUsuarios(int userId){
        //Ver cuentas
        List<CuentaDto>cuentaDtos = verCuentas(userId);
        int cuentaOrigen;
        boolean cuentaValida;

        try{

            Connection conn = DataBaseConexion.getInstance().getConexion();
            if(cuentaDtos.size() > 1){
                do {
                    cuentaValida = false;
                    System.out.println("Ingrese el id de la cuenta origen");
                    cuentaOrigen = sc.nextInt();
                    sc.nextLine();

                    for (CuentaDto cuenta : cuentaDtos) {

                        System.out.println("Comparando con cuenta ID: " + cuenta.getId());

                        if (cuenta.getId() == cuentaOrigen) {
                            cuentaValida = true;
                            System.out.println("  ¡Cuenta encontrada y validada!");
                            break;
                        }
                    }
                    if (!cuentaValida) {
                        System.out.println("El numero de cuenta ingresado no corresponde. Vuelva a ingresarlo ");
                    }

                }while (!cuentaValida);

                System.out.println(
                        "Ingrese la opcion deseada: \n" +
                        "1) Habilitar cuenta \n" +
                            "2) Deshabilitar cuenta \n" +
                                "3)Borrar cuenta"
                );
                int opcionSeleccionada = sc.nextInt();
                sc.nextLine();

                if(opcionSeleccionada == 1){
                    // Habilitar cuenta
                    String habilitarCuentaSql = "UPDATE cuenta SET estado = ? + WHERE id= ?";
                    PreparedStatement stmt= conn.prepareStatement(habilitarCuentaSql);
                    stmt.setString(1, "activa");
                    stmt.setInt(2, cuentaOrigen);
                    stmt.executeUpdate();
                }else if(opcionSeleccionada == 2){
                    // Deshabilitar cuenta
                    String habilitarCuentaSql = "UPDATE cuenta SET estado = ? + WHERE id= ?";
                    PreparedStatement stmt= conn.prepareStatement(habilitarCuentaSql);
                    stmt.setString(1, "bloqueada");
                    stmt.setInt(2, cuentaOrigen);
                    stmt.executeUpdate();
                }else{
                    //Cerrar cuenta
                    String habilitarCuentaSql = "UPDATE cuenta SET estado = ? + WHERE id= ?";
                    PreparedStatement stmt= conn.prepareStatement(habilitarCuentaSql);
                    stmt.setString(1, "cerrada");
                    stmt.setInt(2, cuentaOrigen);
                    stmt.executeUpdate();
                }
            } else{
                System.out.println(
                        "Ingrese la opcion deseada: \n" +
                                "1) Habilitar cuenta \n" +
                                "2) Deshabilitar cuenta \n" +
                                "3) Cerrar cuenta"
                );

                int cuentaId = cuentaDtos.get(0).getId();
                int opcionSeleccionada = sc.nextInt();
                sc.nextLine();


                String[] estados = new String[3];
                estados[0] = "activa";
                estados[1] = "bloqueada";
                estados[2] = "cerrada";


                if(opcionSeleccionada == 1){
                    // Habilitar cuenta
                    String habilitarCuentaSql = "UPDATE cuenta SET estado = ?  WHERE id= ?";
                    PreparedStatement stmt= conn.prepareStatement(habilitarCuentaSql);
                    stmt.setString(1, estados[0]);
                    stmt.setInt(2, cuentaId);
                    stmt.executeUpdate();
                    System.out.println("La cuenta se encuentra: " + estados[0]);

                }else if(opcionSeleccionada == 2){
                    // Deshabilitar cuenta
                    String habilitarCuentaSql = "UPDATE cuenta SET estado = ?  WHERE id= ?";
                    PreparedStatement stmt= conn.prepareStatement(habilitarCuentaSql);
                    stmt.setString(1, estados[1]);
                    stmt.setInt(2, cuentaId);
                    stmt.executeUpdate();
                    System.out.println("La cuenta se encuentra: " + estados[1]);

                }else{
                    //Cerrar cuenta
                    String habilitarCuentaSql = "UPDATE cuenta SET estado = ?  WHERE id= ?";
                    PreparedStatement stmt= conn.prepareStatement(habilitarCuentaSql);
                    stmt.setString(1, estados[2]);
                    stmt.setInt(2, cuentaId);
                    stmt.executeUpdate();
                    System.out.println("La cuenta se encuentra: " + estados[2]);
                }

            }
        }catch (SQLException | ErrorConexionDB e){
            e.printStackTrace();
        }
    }

    @Override
    public void verSaldo(int userId) {

        String sql= "SELECT * FROM cuenta WHERE user_id = ?";

        try {
            Connection conn = DataBaseConexion.getInstance().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            boolean tieneDatos = false;
                while (rs.next()) {
                    int cuentaId = rs.getInt("id");
                    double saldo = rs.getDouble("saldo");
                    String status = rs.getString("estado");
                    tieneDatos = true;
                    System.out.println("Cuenta nro: " + cuentaId + " tiene un saldo disponible de " + saldo + " pesos");
                    System.out.println("Estado: " + status);
                }

                if(!tieneDatos){
                    //Si no tiene cuenta
                    System.out.println("Debe crear cuenta y realizar un deposito");
                }


             //Libero recursos
             rs.close();

        }catch (SQLException | ErrorConexionDB e){
            e.printStackTrace();
        }
    }

    @Override
    public double depositar(int userId) {
        //Ver cuentas
        List<CuentaDto>cuentaDtos = verCuentas(userId);
        int cuentaOrigen;
        boolean cuentaValida;

        try{

            Connection conn = DataBaseConexion.getInstance().getConexion();
            if(cuentaDtos.size() > 1){
                do {
                    cuentaValida = false;
                    System.out.println("Ingrese el id de la cuenta origen");
                    cuentaOrigen = sc.nextInt();
                    sc.nextLine();

                    for (CuentaDto cuenta : cuentaDtos) {

                        System.out.println("Comparando con cuenta ID: " + cuenta.getId());

                        if (cuenta.getId() == cuentaOrigen) {
                            cuentaValida = true;
                            System.out.println("  ¡Cuenta encontrada y validada!");
                            break;
                        }
                    }
                    if (!cuentaValida) {
                        System.out.println("El numero de cuenta ingresado no corresponde. Vuelva a ingresarlo ");
                    }

                }while (!cuentaValida);

                System.out.println("Ingrese el monto a depositar");
                double dinero = sc.nextDouble();
                sc.nextLine();

                // Depositar
                String sqlDepositarDinero = "UPDATE cuenta SET saldo = saldo + ? WHERE id= ?";
                PreparedStatement stmtSumar = conn.prepareStatement(sqlDepositarDinero);
                stmtSumar.setDouble(1, dinero);
                stmtSumar.setInt(2, cuentaOrigen);
                stmtSumar.executeUpdate();

                //Registro de movimientos
                MovimientoDto movimiento = new MovimientoDto(
                        cuentaOrigen,
                        "Deposito",
                        dinero,
                        "Deposito realizado en cuenta"
                );

                movimientoDao.registrarMovimientos(movimiento);

            }else {

                System.out.println("Ingrese el monto a depositar");
                double dinero = sc.nextDouble();
                sc.nextLine();

                int cuentaId = cuentaDtos.get(0).getId();

                        //Depositar
                        String sqlDepositarDinero = "UPDATE cuenta SET saldo = saldo + ? WHERE id= ?";
                        PreparedStatement stmtSumar = conn.prepareStatement(sqlDepositarDinero);
                        stmtSumar.setDouble(1, dinero);
                        stmtSumar.setInt(2, cuentaId);
                        stmtSumar.executeUpdate();

                        //Registro de movimientos
                        MovimientoDto movimiento = new MovimientoDto(
                                cuentaId,
                            "Deposito",
                                dinero,
                                "Deposito realizado en cuenta"
                        );

                    movimientoDao.registrarMovimientos(movimiento);
                System.out.println("Saldo depositado correctamente: " + dinero + " pesos.");
                conn.close();
            }

        }catch (SQLException | ErrorConexionDB e){
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public double retirar(int userId) {
        //Ver cuentas
        List<CuentaDto>cuentaDtos = verCuentas(userId);
        int cuentaOrigen;
        boolean cuentaValida;

        try{

            Connection conn = DataBaseConexion.getInstance().getConexion();
                if(cuentaDtos.size() > 1){
                    do {
                        cuentaValida = false;
                        System.out.println("Ingrese el id de la cuenta origen");
                        cuentaOrigen = sc.nextInt();
                        sc.nextLine();

                        for (CuentaDto cuenta : cuentaDtos) {

                            System.out.println("  Comparando con cuenta ID: " + cuenta.getId());

                            if (cuenta.getId() == cuentaOrigen) {
                                cuentaValida = true;
                                System.out.println("¡Cuenta encontrada y validada!");
                                break;
                            }
                        }
                        if (!cuentaValida){
                            System.out.println("El numero de cuenta ingresado no corresponde. Vuelva a ingresarlo ");
                        }

                    }while (!cuentaValida);

                    //Validar saldo cuenta origen

                    String validarSaldoSql = "SELECT * FROM cuenta WHERE id= ? AND user_id = ?";
                    PreparedStatement stmtValidar = conn.prepareStatement(validarSaldoSql);
                    stmtValidar.setInt(1, cuentaOrigen);
                    stmtValidar.setInt(2, userId);
                    ResultSet rsVerificar = stmtValidar.executeQuery();

                    if(rsVerificar.next()) {
                        double saldoCuentaOrigen = rsVerificar.getDouble("saldo");

                        if (saldoCuentaOrigen > 0) {
                            System.out.println("Ingrese el monto a retirar");
                            double dinero = sc.nextDouble();

                            if(saldoCuentaOrigen >= dinero){
                                //Retirar
                                String sqlRetirarDinero = "UPDATE cuenta SET saldo = saldo - ? WHERE id= ?";
                                PreparedStatement stmtRestar = conn.prepareStatement(sqlRetirarDinero);
                                stmtRestar.setDouble(1, dinero);
                                stmtRestar.setInt(2, cuentaOrigen);
                                stmtRestar.executeUpdate();

                                //Registro de movimientos
                                MovimientoDto movimiento = new MovimientoDto(
                                        cuentaOrigen,
                                        "Retiro",
                                        dinero,
                                        "Retiro realizado en cuenta"
                                );

                                movimientoDao.registrarMovimientos(movimiento);

                                System.out.println("Saldo retirado correctamente: " + dinero + " pesos.");
                            }

                        } else {
                            System.out.println("No se pudo realizar la operación, saldo insuficiente");
                        }
                    }else{
                        System.out.println("No se encontro la cuenta");
                    }
                }else {
                    //Validar saldo cuenta origen

                    String validarSaldoSql = "SELECT * FROM cuenta WHERE user_id = ?";
                    PreparedStatement stmtValidar = conn.prepareStatement(validarSaldoSql);
                    stmtValidar.setInt(1, userId);
                    ResultSet rsVerificar = stmtValidar.executeQuery();

                    int cuentaId = cuentaDtos.get(0).getId();

                    if(rsVerificar.next()) {
                        double saldoCuentaOrigen = rsVerificar.getDouble("saldo");
                        if(saldoCuentaOrigen > 0){
                            System.out.println("Ingrese el monto a retirar");
                            double dinero = sc.nextDouble();
                            sc.nextLine();
                            if(saldoCuentaOrigen >= dinero){
                                //Retirar
                                String sqlRetirarDinero = "UPDATE cuenta SET saldo = saldo - ? WHERE id= ?";
                                PreparedStatement stmtRestar = conn.prepareStatement(sqlRetirarDinero);
                                stmtRestar.setDouble(1, dinero);
                                stmtRestar.setInt(2, cuentaId);
                                stmtRestar.executeUpdate();

                                //Registro de movimientos
                                MovimientoDto movimiento = new MovimientoDto(
                                        cuentaId,
                                        "Retiro",
                                        dinero,
                                        "Retiro realizado en cuenta"
                                );

                                movimientoDao.registrarMovimientos(movimiento);

                                System.out.println("Saldo retirado correctamente: " + dinero + " pesos.");
                            }
                        }
                    }
                    conn.close();
                }


        }catch (SQLException | ErrorConexionDB e){
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public double transferir(int userId) {

        //Ver cuentas
        String sql= "SELECT * FROM cuenta WHERE user_id = ?";
        try {
            Connection conn = DataBaseConexion.getInstance().getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();


                List<Integer>idCuentasUsuario = new ArrayList<>(); //Guardar las cuentas del usuario
                while (rs.next()){
                    int cuentaId = rs.getInt("id");
                    double saldo = rs.getDouble("saldo");
                    idCuentasUsuario.add(cuentaId);

                    System.out.println("Cuenta nro: " + cuentaId + " tiene un saldo disponible de " + saldo + " pesos");
                }

                int cuentaOrigen;
                int cuentaDestino;

                if(idCuentasUsuario.size() > 1){
                    do {
                        System.out.println("Ingrese el id de la cuenta origen");
                        cuentaOrigen = sc.nextInt();

                        if(!idCuentasUsuario.contains(cuentaOrigen)){
                            System.out.println("El nro de cuenta ingresado no corresponde al usuario");
                            System.out.println("Ingrese nuevamente el nro de cuenta");
                        }
                    }while (!idCuentasUsuario.contains(cuentaOrigen));

                    do {
                        System.out.println("Ingrese el id de la cuenta destino");
                        cuentaDestino = sc.nextInt();

                        if(!idCuentasUsuario.contains(cuentaDestino)) {
                            System.out.println("El nro de cuenta ingresado no corresponde al usuario");
                            System.out.println("Ingrese nuevamente el nro de cuenta");
                        } else if (cuentaDestino == cuentaOrigen) {
                            System.out.println("No se puede realizar esta operacion con la misma cuenta");
                        }

                    }while (!idCuentasUsuario.contains(cuentaDestino) || cuentaDestino == cuentaOrigen);

                    System.out.println("Ingrese el monto a transferir");
                    double dinero = sc.nextDouble();


                    //Validar saldo cuenta origen
                    String validarSaldoSql = "SELECT * FROM cuenta WHERE id= ? AND user_id = ?";
                    PreparedStatement stmtValidar = conn.prepareStatement(validarSaldoSql);
                    stmtValidar.setInt(1, cuentaOrigen);
                    stmtValidar.setInt(2, userId);
                    ResultSet rsVerificar = stmtValidar.executeQuery();

                    if(rsVerificar.next()){
                        double saldoCuentaOrigen = rsVerificar.getDouble("saldo");

                        if(saldoCuentaOrigen >= dinero){

                            if (conn.isClosed()) {
                                conn = DataBaseConexion.getInstance().getConexion();
                            }

                            //Retirar
                            String sqlRetirarDinero = "UPDATE cuenta SET saldo = saldo - ? WHERE id= ?";
                            PreparedStatement stmtRestar = conn.prepareStatement(sqlRetirarDinero);
                            stmtRestar.setDouble(1, dinero);
                            stmtRestar.setInt(2, cuentaOrigen);
                            stmtRestar.executeUpdate();

                            // En la cuenta origen
                            movimientoDao.registrarMovimientos(
                                    new MovimientoDto(
                                            cuentaOrigen,
                                            "TRANSFERENCIA_ENVIADA",
                                            dinero,
                                            "Transferencia a cuenta " + cuentaDestino
                                    )
                            );

                            if (conn.isClosed()) {
                                conn = DataBaseConexion.getInstance().getConexion();
                            }

                            //Depositar
                            String sqlDepositarDinero = "UPDATE cuenta SET saldo = saldo + ? WHERE id= ?";
                            PreparedStatement stmtSumar = conn.prepareStatement(sqlDepositarDinero);
                            stmtSumar.setDouble(1, dinero);
                            stmtSumar.setInt(2, cuentaDestino);
                            stmtSumar.executeUpdate();

                            // En la cuenta destino
                            movimientoDao.registrarMovimientos(
                                    new MovimientoDto(
                                            cuentaDestino,
                                            "TRANSFERENCIA_ENVIADA",
                                            dinero,
                                            "Transferencia a cuenta " + cuentaOrigen
                                    )
                            );

                            System.out.println("Transferencia exitosa de: " + dinero + " de cuenta " + cuentaOrigen
                                    + " a cuenta " + cuentaDestino);
                        } else {
                            System.out.println("Solo tiene una cuenta asociada");
                        }
                    }

                }else {
                    System.out.println("Solo tiene una cuenta, no puede transferir");
                }

        }catch (SQLException | ErrorConexionDB e){
            e.printStackTrace();
        }

        return 0;
    }

}
