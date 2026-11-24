package br.edu.fatec.clinica.modelo;

import br.edu.fatec.clinica.interfaces.Prontuario;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ConsultaPet implements Comparable<ConsultaPet>, Prontuario {

    public static final DateTimeFormatter FORMATO_DATA_HORA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private int id;
    private String nomePet;
    private String especie;
    private String tutor;
    private LocalDateTime dataHora;
    private String status; // AGENDADA, ATENDIDA, CANCELADA
    private String observacoesClinicas;

    public ConsultaPet(int id, String nomePet, String especie, String tutor, LocalDateTime dataHora) {
        this.id = id;
        this.nomePet = nomePet;
        this.especie = especie;
        this.tutor = tutor;
        this.dataHora = dataHora;
        this.status = "AGENDADA";
        this.observacoesClinicas = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomePet() {
        return nomePet;
    }

    public void setNomePet(String nomePet) {
        this.nomePet = nomePet;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getTutor() {
        return tutor;
    }

    public void setTutor(String tutor) {
        this.tutor = tutor;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservacoesClinicas() {
        return observacoesClinicas;
    }

    public void setObservacoesClinicas(String observacoesClinicas) {
        this.observacoesClinicas = observacoesClinicas;
    }

    @Override
    public int compareTo(ConsultaPet outra) {
        int cmpData = this.dataHora.compareTo(outra.dataHora);
        if (cmpData != 0) {
            return cmpData;
        }
        return Integer.compare(this.id, outra.id);
    }

    @Override
    public String emitirReceituario() {
        String obs = (observacoesClinicas == null || observacoesClinicas.isBlank())
                ? "Sem observações clínicas registradas até o momento."
                : observacoesClinicas;

        return formatarCabecalhoReceita(nomePet, tutor)
                + "Data/Hora: " + dataHora.format(FORMATO_DATA_HORA) + "\n"
                + "Status: " + status + "\n"
                + "Prescrição / Observações: " + obs + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConsultaPet that)) return false;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("#%d | %s (%s) | Tutor: %s | %s | Status: %s",
                id,
                nomePet,
                especie,
                tutor,
                dataHora.format(FORMATO_DATA_HORA),
                status);
    }
}