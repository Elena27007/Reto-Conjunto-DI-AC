package org.example.views;

import org.example.dao.JdbcUtils;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Pantalla de detalles de las copias
 */
public class DetallesCopia extends JFrame {
    private int idCopia;

    /**
     * Constructor que crea los distintos elementos de la pantalla y sus respectivas funcionalidades
     * @param copyId
     */
    public DetallesCopia(int copyId) {
        this.idCopia = copyId;
        setTitle("Detalles de la Copia");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel detallesLabel = new JLabel("Detalles de la copia:");
        detallesLabel.setBounds(10, 10, 380, 25);
        panel.add(detallesLabel);

        JTextArea detallesArea = new JTextArea();
        detallesArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(detallesArea);
        scrollPane.setBounds(10, 40, 360, 200);
        panel.add(scrollPane);

        cargarDetallesCopia(detallesArea);

        add(panel);
    }

    /**
     * Método que muestra los detalles de la copia seleccionada buscando en la base de datos la
     * información sobre la película. Si no se encuentra información sobre la copia seleccionada
     * o hay un error al cargar los detalles muestra un mensaje por pantalla
     * @param detallesArea
     */
    private void cargarDetallesCopia(JTextArea detallesArea) {
        try (Connection conn = JdbcUtils.getConnection()) {
            String sql = "SELECT c.estado, c.soporte, p.titulo, p.genero, p.año, p.descripcion, p.director FROM Copias c " +
                    "JOIN Peliculas p ON c.id_pelicula = p.id WHERE c.id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idCopia);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String titulo = rs.getString("titulo");
                String genero = rs.getString("genero");
                int año = rs.getInt("año");
                String descripcion = rs.getString("descripcion");
                String director = rs.getString("director");
                String estado = rs.getString("estado");
                String soporte = rs.getString("soporte");

                String details = "Película: " + titulo + "\n" +
                        "Género: " + genero + "\n" +
                        "Año: " + año + "\n" +
                        "Descripción: " + descripcion + "\n" +
                        "Director: " + director + "\n" +
                        "Estado de la copia: " + estado + "\n" +
                        "Soporte: " + soporte;

                detallesArea.setText(details);
            } else {
                detallesArea.setText("No se encontraron detalles para la copia seleccionada.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar los detalles de la copia", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
