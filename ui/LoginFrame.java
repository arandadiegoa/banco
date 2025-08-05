package TP_Banco.ui;

import TP_Banco.dao.CuentaDaoImpl;
import TP_Banco.dao.MovimientoDao;
import TP_Banco.dao.dto.LoginResult;

import javax.swing.*;
import java.awt.*;

/**
 * Interfaz gráfica para el inicio de sesión en el sistema bancario.
 *
 * <p>Permite al usuario ingresar su correo electrónico y contraseña.
 * Dependiendo del rol del usuario (empleado o usuario común),
 * redirige a la ventana correspondiente del menú principal.</p>
 */

public class LoginFrame extends JFrame {

    private final CuentaDaoImpl cuentaDao;
    private final MovimientoDao movimientoDao;

    /**
     * Constructor de la ventana de login.
     *
     * @param cuentaDao DAO para operaciones sobre cuentas.
     * @param movimientoDao DAO para operaciones sobre movimientos.
     */
    public LoginFrame(CuentaDaoImpl cuentaDao, MovimientoDao movimientoDao){
        this.cuentaDao = cuentaDao;
        this.movimientoDao = movimientoDao;

        setTitle("Banco Boedo - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 130);
        setLocationRelativeTo(null);

        initUi();

        setVisible(true);
    }

    /**
     * Inicializa los componentes gráficos de la ventana.
     * Configura el formulario de ingreso y el manejo del evento de login.
     */
    private void initUi(){
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

        JLabel emailLabel = new JLabel("Email: ");
        JTextField emailField = new JTextField();

        JLabel passLabel = new JLabel("Contraseña: ");
        JPasswordField passField = new JPasswordField();

        JButton loginBtn = new JButton("Iniciar sesión");

        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(new JLabel()); //Espacio vacío
        panel.add(loginBtn);

        add(panel);

        loginBtn.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passField.getPassword());

            LoginResult result =  cuentaDao.login(email, password);
            if(!result.isSuccess()){
                JOptionPane.showMessageDialog(this, result.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }else {
                JOptionPane.showMessageDialog(this, "Login exitoso como " + result.getRol());

                dispose(); //Cerramos la ventana de login

                if("empleado".equalsIgnoreCase(result.getRol())){
                    new MenuEmpleadoFrame(cuentaDao, movimientoDao, result.getUserId());
                }else{
                    new MenuUsuarioFrame(cuentaDao, movimientoDao, result.getUserId());
                }
            }
        });

    }

}
