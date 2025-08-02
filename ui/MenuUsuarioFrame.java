package TP_Banco.ui;

import TP_Banco.dao.CuentaDaoImpl;
import TP_Banco.dao.MovimientoDao;
import TP_Banco.dao.dto.CuentaDto;

import javax.swing.*;
import java.awt.*;

public class MenuUsuarioFrame extends JFrame {
    private final CuentaDaoImpl cuentaDao;
    private final MovimientoDao movimientoDao;
    private final int userId;

    public MenuUsuarioFrame(CuentaDaoImpl cuentaDao, MovimientoDao movimientoDao, int userId){
        this.cuentaDao = cuentaDao;
        this.movimientoDao = movimientoDao;
        this.userId = userId;

        setTitle("Menú Usuario - Banco Boedo SL");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);

        initUI();

        setVisible(true);
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridLayout(8, 1,10,10));

        JButton btnCrearCuenta = new JButton("Crear cuenta");
        JButton btnVerSaldo = new JButton("Ver saldo");
        JButton btnDepositar = new JButton("Depositar Efectivo");
        JButton btnRetirar = new JButton("Retirar Efectivo");
        JButton btnTransferir = new JButton("Transferir");
        JButton btnVerMovimientos = new JButton("Ver movimientos");
        JButton btnSalir = new JButton("Salir");

        panel.add(new JLabel("Seleccione una opción: ", SwingConstants.CENTER));
        panel.add(btnCrearCuenta);
        panel.add(btnVerSaldo);
        panel.add(btnDepositar);
        panel.add(btnRetirar);
        panel.add(btnTransferir);
        panel.add(btnVerMovimientos);
        panel.add(btnSalir);

        add(panel);

        //Listeners
        btnCrearCuenta.addActionListener(e-> {
            String montostr = JOptionPane.showInputDialog(this, "Ingrese el saldo inicial: ");
            try {
                double monto = Double.parseDouble(montostr);
                cuentaDao.crearCuenta(this, new CuentaDto(monto, userId));
            }catch (Exception ex){
                JOptionPane.showMessageDialog(this, "Monto inválido");
            }
        });

        btnVerSaldo.addActionListener(e -> cuentaDao.verSaldo(this, userId));
        btnDepositar.addActionListener(e -> cuentaDao.depositar(this, userId));
        btnRetirar.addActionListener(e -> cuentaDao.retirar(this, userId));
        btnTransferir.addActionListener(e -> cuentaDao.transferir(this, userId));
        btnVerMovimientos.addActionListener(e -> cuentaDao.verMovimientosUserId(this, userId));

        btnSalir.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Gracias por usar el sistema. Hasta luego");
            dispose();
        });
    }
}
