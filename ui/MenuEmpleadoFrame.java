package TP_Banco.ui;

import TP_Banco.dao.CuentaDaoImpl;
import TP_Banco.dao.MovimientoDao;

import javax.swing.*;
import java.awt.*;


/**
 * Esta clase representa la ventana principal del menú de un empleado del banco.
 * Permite acceder a funcionalidades exclusivas del rol empleado como:
 * <ul>
 *     <li>Ver todas las transacciones registradas en el sistema.</li>
 *     <li>Gestionar las cuentas de los usuarios (activar, bloquear o cerrar).</li>
 * </ul>
 */

public class MenuEmpleadoFrame extends JFrame {
    private final CuentaDaoImpl cuentaDao;
    private final MovimientoDao movimientoDao;
    private final int userId;

    /**
     * Constructor de la ventana del menú de empleado.
     *
     * @param cuentaDao instancia del DAO para operaciones sobre cuentas
     * @param movimientoDao instancia del DAO para operaciones sobre movimientos
     * @param userId ID del usuario que ha iniciado sesión
     */
    public MenuEmpleadoFrame(CuentaDaoImpl cuentaDao, MovimientoDao movimientoDao,  int userId) {
        this.cuentaDao = cuentaDao;
        this.movimientoDao = movimientoDao;
        this.userId = userId;

        setTitle("Menú Empleado - Banco Boedo SL");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        initUI();
        setVisible(true);
    }

    /**
     * Inicializa la interfaz gráfica con los botones de acción del menú:
     * <ul>
     *     <li>Ver transacciones</li>
     *     <li>Gestionar cuentas de usuarios</li>
     *     <li>Salir del sistema</li>
     * </ul>
     */
    private void initUI() {
        JPanel panel = new JPanel(new GridLayout(4, 1,10,10));

        JButton btnVerTransacciones = new JButton("Ver Transacciones Registradas");
        JButton btnGestionarCuentas = new JButton("Gestionar Cuentas de Usuario");
        JButton btnSalir = new JButton("Salir");

        panel.add(new JLabel("Seleccione una opción: ", SwingConstants.CENTER));
        panel.add(btnVerTransacciones);
        panel.add(btnGestionarCuentas);
        panel.add(btnSalir);

        add(panel);

        btnVerTransacciones.addActionListener(e -> movimientoDao.verTransacciones());

        btnGestionarCuentas.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog(this, "Ingrese el Id del usuario a gestionar: ");
            try{
                int id = Integer.parseInt(idStr);
                cuentaDao.gestionarCuentasUsuariosDesdeGUI(this,id);
            }catch (Exception ex){
                JOptionPane.showMessageDialog(this, "ID inválido.");
            }

        });

        btnSalir.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Gracias por usar el sistema. Hasta luego!");
            dispose();
        });

        }

    }
