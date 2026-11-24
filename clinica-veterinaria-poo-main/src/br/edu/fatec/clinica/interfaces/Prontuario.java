package br.edu.fatec.clinica.interfaces;

public interface Prontuario {

    String emitirReceituario();

    default String formatarCabecalhoReceita(String nomePet, String tutor) {
        return "=== RECEITUÁRIO VETERINÁRIO ===\n"
                + "Paciente: " + nomePet + "\n"
                + "Tutor(a): " + tutor + "\n"
                + "-------------------------------\n";
    }
}