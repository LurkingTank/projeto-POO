package br.edu.fatec.clinica.excecoes;

public class HorarioIndisponivelException extends Exception {

    public HorarioIndisponivelException(String mensagem) {
        super(mensagem);
    }
}