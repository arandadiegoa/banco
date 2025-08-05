package TP_Banco.ui;

import TP_Banco.dao.UserDao;
import TP_Banco.dao.dto.UserDto;
import TP_Banco.exception.ErrorConexionDB;

import javax.swing.*;
import java.awt.*;


/**
 * Ventana de registro de nuevos usuarios en el sistema bancario.
 * Permite ingresar nombre, email y contraseña.
 * Si el usuario no existe, se crea con rol "cliente" por defecto.
 */

public class RegistroUsuarioFrame extends JFrame {

    private final UserDao userDao;

    /**
     * Constructor que inicializa el formulario de registro.
     * @param userDao DAO para operaciones sobre la tabla de usuarios.
     */
    public RegistroUsuarioFrame(UserDao userDao){
        this.userDao = userDao;

        setTitle("Registro de usuario");
        setSize(350, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();

        setVisible(true);

    }

    /**
     * Inicializa los componentes de la interfaz gráfica.
     */
    private void initUI(){
        JPanel panel = new JPanel(new GridLayout(5,2,10,10));

        JLabel nameLabel = new JLabel("Nombre:");
        JTextField nameField = new JTextField();

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();

        JLabel passLabel = new JLabel("Contraseña:");
        JPasswordField passField = new JPasswordField();

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnCancelar = new JButton("Cancelar");

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(new JLabel()); //Espacio vacío
        panel.add(new JLabel()); //Espacio vacío
        panel.add(btnRegistrar);
        panel.add(btnCancelar);

        add(panel);

        // Acción del botón Registrar
        btnRegistrar.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String pass = new String(passField.getPassword());

            if(name.isEmpty() || email.isEmpty() || pass.isEmpty()){
                JOptionPane.showMessageDialog(this,
                        "Complete todos los campos",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

            if(userDao.userExists(email)){
                JOptionPane.showMessageDialog(this,
                        "El email ya esta registrado",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

            UserDto newUser = new UserDto(name, email, pass, "cliente");
            try {
                boolean creado = userDao.create(newUser);
                if(creado){
                    JOptionPane.showMessageDialog(this,
                            "Usuario creado con exito!");
                    dispose();
                }else {
                    JOptionPane.showMessageDialog(this,
                            "Error al registrar al usuario",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            } catch (ErrorConexionDB ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Error de conexión con la base de datos",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        });

        // Acción del botón Cancelar
        btnCancelar.addActionListener(e -> dispose());
    }

}
