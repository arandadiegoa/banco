package TP_Banco.ui;

import TP_Banco.dao.CuentaDaoImpl;
import TP_Banco.dao.MovimientoDao;

import javax.swing.*;
import java.awt.*;

public class MenuEmpleadoFrame extends JFrame {
    private final CuentaDaoImpl cuentaDao;
    private final MovimientoDao movimientoDao;
    private final int userId;
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
