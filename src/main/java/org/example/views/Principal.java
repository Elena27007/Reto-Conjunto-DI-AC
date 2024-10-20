package org.example.views;

import org.example.dao.JdbcUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Pantalla principal con las copias del usuario que ha inciado sesión
 */
public class Principal extends JFrame {
    private int idUsuario;
    private JList<String> listaCopias;
    private DefaultListModel<String> listaModelo;
    private ArrayList<Integer> idCopias;

    /**
     * Constructor que crea los distintos elementos de la pantalla y sus respectivas funcionalidades
     * @param idUsuario
     */
    public Principal(int idUsuario) {
        this.idUsuario = idUsuario;
        setTitle("Listado de Copias de Películas");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel tituloLabel = new JLabel("Tus copias de películas:");
        tituloLabel.setBounds(10, 10, 200, 25);
        panel.add(tituloLabel);

        listaModelo = new DefaultListModel<>();
        listaCopias = new JList<>(listaModelo);
        JScrollPane scrollPane = new JScrollPane(listaCopias);
        scrollPane.setBounds(10, 40, 360, 200);
        panel.add(scrollPane);

        cargarCopias();

        JButton botonCerrarSesion = new JButton("Cerrar Sesión");
        botonCerrarSesion.setBounds(10, 260, 150, 25);
        panel.add(botonCerrarSesion);

        JButton botonCerrar = new JButton("Cerrar Aplicación");
        botonCerrar.setBounds(220, 260, 150, 25);
        panel.add(botonCerrar);

        botonCerrarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InicioSesion login = new InicioSesion();
                login.setVisible(true);
                dispose();
            }
        });

        botonCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        listaCopias.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = listaCopias.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        int idCopia = idCopias.get(index);
                        abrirDetallesCopia(idCopia);
                    }
                }
            }
        });

        add(panel);
    }

    /**
     * Método que muestra las copias del usuario y almacena los id de dichas copias en un array
     */
    private void cargarCopias() {
        idCopias = new ArrayList<>();
        try (Connection conn = JdbcUtils.getConnection()) {
            String sql = "SELECT c.id, p.titulo, c.estado, c.soporte FROM Copias c " +
                    "JOIN Peliculas p ON c.id_pelicula = p.id WHERE c.id_usuario = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, idUsuario);
            ResultSet resultSet = statement.executeQuery();

            listaModelo.clear();
            while (resultSet.next()) {
                int idCopia = resultSet.getInt("id");
                String titulo = resultSet.getString("titulo");
                String estado = resultSet.getString("estado");
                String soporte = resultSet.getString("soporte");

                String copia = "Película: " + titulo + " | Estado: " + estado + " | Soporte: " + soporte;
                listaModelo.addElement(copia);
                idCopias.add(idCopia);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar las copias de películas", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método que muestra los detalles de cada copia en otra pantalla haciendo doble clic en estas
     * @param idCopia
     */
    private void abrirDetallesCopia(int idCopia) {
        DetallesCopia dc = new DetallesCopia(idCopia);
        dc.setVisible(true);
    }
}
