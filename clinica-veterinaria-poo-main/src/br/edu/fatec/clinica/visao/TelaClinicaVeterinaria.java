package br.edu.fatec.clinica.visao;

import br.edu.fatec.clinica.excecoes.HorarioIndisponivelException;
import br.edu.fatec.clinica.modelo.ConsultaPet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TelaClinicaVeterinaria extends JFrame implements ActionListener, WindowListener {

    private final List<ConsultaPet> consultas = new ArrayList<>();

    private final JTextField txtId = new JTextField(8);
    private final JTextField txtNomePet = new JTextField(14);
    private final JTextField txtEspecie = new JTextField(12);
    private final JTextField txtTutor = new JTextField(14);
    private final JTextField txtDataHora = new JTextField(14);
    private final JTextField txtObservacoes = new JTextField(24);
    private final JTextField txtBusca = new JTextField(16);

    private final JButton btnCadastrar = new JButton("Cadastrar");
    private final JButton btnPesquisar = new JButton("Pesquisar");
    private final JButton btnAtender = new JButton("Dar Baixa / Atender");
    private final JButton btnCancelar = new JButton("Cancelar");
    private final JButton btnOrdenar = new JButton("Ordenar");
    private final JButton btnSair = new JButton("Sair");

    private final JTextArea areaRelatorio = new JTextArea(14, 65);

    public TelaClinicaVeterinaria() {
        super("Clínica Veterinária - Controle de Consultas");
        configurarJanela();
        montarLayout();
        registrarEventos();
        atualizarAreaRelatorio("Sistema iniciado. Nenhuma consulta cadastrada ainda.");
    }

    private void configurarJanela() {
        setSize(820, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    private void montarLayout() {
        JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel painelFormulario = new JPanel(new GridLayout(3, 4, 8, 8));
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Dados da Consulta"));

        painelFormulario.add(new JLabel("ID (número):"));
        painelFormulario.add(txtId);

        painelFormulario.add(new JLabel("Nome do Pet:"));
        painelFormulario.add(txtNomePet);

        painelFormulario.add(new JLabel("Espécie:"));
        painelFormulario.add(txtEspecie);

        painelFormulario.add(new JLabel("Tutor(a):"));
        painelFormulario.add(txtTutor);

        painelFormulario.add(new JLabel("Data/Hora (dd/MM/yyyy HH:mm):"));
        painelFormulario.add(txtDataHora);

        painelFormulario.add(new JLabel("Prescrição / Obs.:"));
        painelFormulario.add(txtObservacoes);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnAtender);
        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnOrdenar);
        painelBotoes.add(btnSair);

        JPanel painelTopo = new JPanel(new BorderLayout(5, 5));
        painelTopo.add(painelFormulario, BorderLayout.CENTER);
        painelTopo.add(painelBotoes, BorderLayout.SOUTH);

        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        painelBusca.setBorder(BorderFactory.createTitledBorder("Pesquisa Rápida (ID, Pet ou Tutor)"));
        painelBusca.add(new JLabel("Filtro:"));
        painelBusca.add(txtBusca);
        painelBusca.add(btnPesquisar);

        areaRelatorio.setEditable(false);
        areaRelatorio.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(areaRelatorio);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Agenda / Prontuários em Memória"));

        JPanel painelCentro = new JPanel(new BorderLayout(5, 5));
        painelCentro.add(painelBusca, BorderLayout.NORTH);
        painelCentro.add(scrollPane, BorderLayout.CENTER);

        painelPrincipal.add(painelTopo, BorderLayout.NORTH);
        painelPrincipal.add(painelCentro, BorderLayout.CENTER);

        add(painelPrincipal);
    }

    private void registrarEventos() {
        btnCadastrar.addActionListener(this);
        btnPesquisar.addActionListener(this);
        btnAtender.addActionListener(this);
        btnCancelar.addActionListener(this);
        btnOrdenar.addActionListener(this);
        btnSair.addActionListener(this);
        addWindowListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object origem = e.getSource();

        if (origem == btnCadastrar) {
            cadastrarConsulta();
        } else if (origem == btnPesquisar) {
            pesquisarConsultas();
        } else if (origem == btnAtender) {
            darBaixaConsulta();
        } else if (origem == btnCancelar) {
            cancelarConsulta();
        } else if (origem == btnOrdenar) {
            ordenarConsultas();
        } else if (origem == btnSair) {
            confirmarSaida();
        }
    }

    private void cadastrarConsulta() {
        try {
            int id = lerIdObrigatorio();
            String nomePet = txtNomePet.getText().trim();
            String especie = txtEspecie.getText().trim();
            String tutor = txtTutor.getText().trim();
            String dataHoraTexto = txtDataHora.getText().trim();

            if (nomePet.isEmpty() || especie.isEmpty() || tutor.isEmpty() || dataHoraTexto.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Preencha Nome do Pet, Espécie, Tutor(a) e Data/Hora.",
                        "Campos obrigatórios",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            LocalDateTime dataHora = LocalDateTime.parse(dataHoraTexto, ConsultaPet.FORMATO_DATA_HORA);
            validarDisponibilidade(id, dataHora);

            ConsultaPet novaConsulta = new ConsultaPet(id, nomePet, especie, tutor, dataHora);
            if (!txtObservacoes.getText().trim().isEmpty()) {
                novaConsulta.setObservacoesClinicas(txtObservacoes.getText().trim());
            }

            consultas.add(novaConsulta);
            Collections.sort(consultas);
            limparCamposCadastro();

            JOptionPane.showMessageDialog(
                    this,
                    "Consulta cadastrada com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE
            );
            exibirTodasConsultas("Consultas agendadas (ordenadas por data/hora):");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "O campo ID deve conter um número inteiro válido.",
                    "Erro de formato numérico",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Data/Hora inválida. Use o formato dd/MM/yyyy HH:mm (ex.: 25/11/2026 14:30).",
                    "Data/Hora inválida",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (HorarioIndisponivelException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Horário indisponível",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void validarDisponibilidade(int id, LocalDateTime dataHora) throws HorarioIndisponivelException {
        for (ConsultaPet c : consultas) {
            if (c.getId() == id) {
                throw new HorarioIndisponivelException(
                        "Já existe uma consulta cadastrada com o ID #" + id + "."
                );
            }
            boolean ativa = !"CANCELADA".equalsIgnoreCase(c.getStatus());
            if (ativa && c.getDataHora().equals(dataHora)) {
                throw new HorarioIndisponivelException(
                        "Já existe consulta marcada para " + dataHora.format(ConsultaPet.FORMATO_DATA_HORA)
                                + " (Pet: " + c.getNomePet() + ")."
                );
            }
        }
    }

    private void darBaixaConsulta() {
        try {
            int id = lerIdObrigatorio();
            ConsultaPet encontrada = buscarPorId(id);

            if (encontrada == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Nenhuma consulta encontrada com o ID #" + id + ".",
                        "Não encontrada",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            if ("CANCELADA".equalsIgnoreCase(encontrada.getStatus())) {
                JOptionPane.showMessageDialog(
                        this,
                        "Não é possível dar baixa em uma consulta cancelada.",
                        "Operação inválida",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            String obs = txtObservacoes.getText().trim();
            if (!obs.isEmpty()) {
                encontrada.setObservacoesClinicas(obs);
            }
            encontrada.setStatus("ATENDIDA");

            JOptionPane.showMessageDialog(
                    this,
                    encontrada.emitirReceituario(),
                    "Atendimento Concluído - Receituário",
                    JOptionPane.INFORMATION_MESSAGE
            );
            exibirTodasConsultas("Consulta #" + id + " marcada como ATENDIDA:");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Informe um ID numérico válido no campo ID para dar baixa.",
                    "Erro de formato numérico",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cancelarConsulta() {
        try {
            int id = lerIdObrigatorio();
            ConsultaPet encontrada = buscarPorId(id);

            if (encontrada == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Nenhuma consulta encontrada com o ID #" + id + ".",
                        "Não encontrada",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            if ("CANCELADA".equalsIgnoreCase(encontrada.getStatus())) {
                JOptionPane.showMessageDialog(
                        this,
                        "Esta consulta já está cancelada.",
                        "Aviso",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            encontrada.setStatus("CANCELADA");
            JOptionPane.showMessageDialog(
                    this,
                    "Consulta #" + id + " cancelada.",
                    "Cancelamento",
                    JOptionPane.INFORMATION_MESSAGE
            );
            exibirTodasConsultas("Consulta #" + id + " cancelada:");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Informe um ID numérico válido no campo ID para cancelar.",
                    "Erro de formato numérico",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void pesquisarConsultas() {
        String termo = txtBusca.getText().trim().toLowerCase();
        if (termo.isEmpty()) {
            exibirTodasConsultas("Todas as consultas em memória:");
            return;
        }

        StringBuilder sb = new StringBuilder("Resultados da pesquisa por \"" + txtBusca.getText().trim() + "\":\n\n");
        int encontrados = 0;

        for (ConsultaPet c : consultas) {
            boolean bateId = String.valueOf(c.getId()).equals(termo);
            boolean batePet = c.getNomePet().toLowerCase().contains(termo);
            boolean bateTutor = c.getTutor().toLowerCase().contains(termo);

            if (bateId || batePet || bateTutor) {
                encontrados++;
                sb.append(c).append("\n");
                sb.append(c.emitirReceituario()).append("\n========================================\n\n");
            }
        }

        if (encontrados == 0) {
            atualizarAreaRelatorio("Nenhuma consulta encontrada para o filtro informado.");
        } else {
            atualizarAreaRelatorio(sb.toString());
        }
    }

    private void ordenarConsultas() {
        Collections.sort(consultas);
        exibirTodasConsultas("Consultas ordenadas por data/horário (Collections.sort):");
    }

    private void exibirTodasConsultas(String titulo) {
        if (consultas.isEmpty()) {
            atualizarAreaRelatorio(titulo + "\n\n(Lista vazia)");
            return;
        }

        StringBuilder sb = new StringBuilder(titulo + "\n\n");
        for (ConsultaPet c : consultas) {
            sb.append(c).append("\n");
            if (!c.getObservacoesClinicas().isBlank()) {
                sb.append("   Obs.: ").append(c.getObservacoesClinicas()).append("\n");
            }
        }
        atualizarAreaRelatorio(sb.toString());
    }

    private void atualizarAreaRelatorio(String texto) {
        areaRelatorio.setText(texto);
        areaRelatorio.setCaretPosition(0);
    }

    private int lerIdObrigatorio() {
        return Integer.parseInt(txtId.getText().trim());
    }

    private ConsultaPet buscarPorId(int id) {
        for (ConsultaPet c : consultas) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    private void limparCamposCadastro() {
        txtId.setText("");
        txtNomePet.setText("");
        txtEspecie.setText("");
        txtTutor.setText("");
        txtDataHora.setText("");
        txtObservacoes.setText("");
    }

    private void confirmarSaida() {
        int resposta = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente sair do sistema?",
                "Confirmar saída",
                JOptionPane.YES_NO_OPTION
        );
        if (resposta == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        confirmarSaida();
    }

    @Override
    public void windowOpened(WindowEvent e) { }

    @Override
    public void windowClosed(WindowEvent e) { }

    @Override
    public void windowIconified(WindowEvent e) { }

    @Override
    public void windowDeiconified(WindowEvent e) { }

    @Override
    public void windowActivated(WindowEvent e) { }

    @Override
    public void windowDeactivated(WindowEvent e) { }
}