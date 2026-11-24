# Sistema de Controle de Clínica Veterinária (POO em Memória)

Trabalho Prático da disciplina **Programação Orientada a Objetos** (Prof. Me. Marcos Roberto de Moraes — Maromo) — **Fatec Mogi Mirim**.

## 1. Identificação

- **Aluno:** Andrews Morales Granadier

---

## 2. Tema Escolhido

**Tema 02 — Controle de Clínica Veterinária**

Sistema desktop desenvolvido em Java com interface gráfica (`javax.swing.*`) e persistência 100% em memória RAM (`List<ConsultaPet> = new ArrayList<>()`), permitindo o gerenciamento completo de consultas veterinárias: agendamento, validação de conflito de horários, pesquisa rápida, baixa de atendimento clínico com emissão de receituário, cancelamento e ordenação cronológica em memória.

---

## 3. Arquitetura e Organização das Classes

O projeto segue a arquitetura em pacotes proposta para separação de responsabilidades:

- **`br.edu.fatec.clinica.Main`:** ponto de entrada da aplicação na *Event Dispatch Thread* (`SwingUtilities.invokeLater`), responsável por exibir a janela principal.
- **`br.edu.fatec.clinica.interfaces.Prontuario` (Interface de Negócio):** define o contrato polimórfico `emitirReceituario()` e o método `default formatarCabecalhoReceita(...)` para padronizar o cabeçalho do receituário.
- **`br.edu.fatec.clinica.excecoes.HorarioIndisponivelException` (Checked Exception Customizada):** herda de `Exception` e é lançada quando há conflito de ID ou de data/horário entre consultas ativas.
- **`br.edu.fatec.clinica.modelo.ConsultaPet` (Entidade Principal):** encapsula os atributos da consulta (`id`, `nomePet`, `especie`, `tutor`, `dataHora`, `status`, `observacoesClinicas`), implementa `Comparable<ConsultaPet>` (ordenando por data/hora e desempate por `id`), implementa `Prontuario` e sobrescreve `equals()`, `hashCode()` e `toString()`.
- **`br.edu.fatec.clinica.visao.TelaClinicaVeterinaria` (Interface Gráfica Swing):** estende `JFrame`, implementa `ActionListener` e `WindowListener`, gerencia a coleção `List<ConsultaPet> consultas = new ArrayList<>()` em memória e exibe avisos e erros via `JOptionPane.showMessageDialog()`.

---

## 4. Instruções de Execução

### Pré-requisitos
- **JDK** 17 ou superior instalado.
- **IntelliJ IDEA** (ou terminal com `javac` e `java`).

### Pelo IntelliJ IDEA
1. Abra a pasta do projeto `clinica-veterinaria-poo`.
2. Abra `src/br/edu/fatec/clinica/Main.java`.
3. Clique no botão **Run** ao lado do método `main`.

### Pelo Terminal
1. Na raiz do projeto, compile os arquivos Java (`javac -d out` apontando para os arquivos em `src/br/edu/fatec/clinica/`).
2. Execute a classe principal com `java -cp out br.edu.fatec.clinica.Main`.

---

## 5. Registro de Prompts de IA Utilizados

- **Planejamento da estrutura e pacotes:** solicitado apoio para organizar o projeto do Tema 02 (Clínica Veterinária) em memória com `ArrayList`, Swing, exceção customizada, `Comparable` e interface, validado contra a estrutura de pacotes do enunciado.
- **Criação das classes base (`Prontuario`, `HorarioIndisponivelException` e `ConsultaPet`):** solicitado passo a passo para criar os pacotes e classes iniciais; o código foi revisado e integrado no IntelliJ IDEA.
- **Implementação da tela Swing (`TelaClinicaVeterinaria`) e `Main`:** gerada a interface gráfica com eventos, validações e `JOptionPane`, testada manualmente em todos os fluxos de cadastro, baixa, cancelamento, pesquisa, ordenação e tratamento de erros.