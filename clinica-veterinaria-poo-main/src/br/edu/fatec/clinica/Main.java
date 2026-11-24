package br.edu.fatec.clinica;

import br.edu.fatec.clinica.visao.TelaClinicaVeterinaria;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaClinicaVeterinaria tela = new TelaClinicaVeterinaria();
            tela.setVisible(true);
        });
    }
}