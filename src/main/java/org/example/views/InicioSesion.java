package org.example.views;

import org.example.dao.JdbcUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Pantalla de inicio de sesión
 */
public class InicioSesion extends JFrame {
    private JTextField campoUsuario;
    private JPasswordField campoContraseña;
    private JButton botonLogin;
    private static Connection connection;

    /**
     * Constructor donde se crean los distintos elementos de la pantalla, y sus respectivas
     * funcionalidades
     */
    public InicioSesion() {
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel usuarioLabel = new JLabel("Usuario:");
        usuarioLabel.setBounds(10, 20, 80, 25);
        panel.add(usuarioLabel);

        campoUsuario = new JTextField(20);
        campoUsuario.setBounds(100, 20, 165, 25);
        panel.add(campoUsuario);

        JLabel contraseñaLabel = new JLabel("Contraseña:");
        contraseñaLabel.setBounds(10, 50, 80, 25);
        panel.add(contraseñaLabel);

        campoContraseña = new JPasswordField(20);
        campoContraseña.setBounds(100, 50, 165, 25);
        panel.add(campoContraseña);

        botonLogin = new JButton("Iniciar sesión");
        botonLogin.setBounds(10, 80, 150, 25);
        panel.add(botonLogin);

        botonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                comprobarUsuario();
            }
        });

        JButton botonCerrar = new JButton("Cerrar");
        botonCerrar.setBounds(150, 80, 120, 25);
        panel.add(botonCerrar);

        botonCerrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        add(panel);
    }

    /**
     * Método que comprueba si el nombre de usuario y la contraseña ingresados coinciden con los de
     * la base de datos, y si son correctos muestra un mensaje y te dirige a la pantalla principal.
     * En caso de que no sean correctos muestra un mensaje
     */
    private void comprobarUsuario() {
        String nombreUsuario = campoUsuario.getText();
        String contraseña = new String(campoContraseña.getPassword());

        try (Connection conn = JdbcUtils.getConnection()) {
            String sql = "SELECT * FROM Usuarios WHERE nombre_usuario = ? AND contraseña = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nombreUsuario);
            ps.setString(2, contraseña);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int idUsuario = rs.getInt("id");
                JOptionPane.showMessageDialog(this, "Login exitoso");
                Principal p = new Principal(idUsuario);
                p.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
