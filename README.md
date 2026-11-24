# Sistema de Controle de Clínica Veterinária (POO em Memória)

Trabalho Prático da disciplina **Programação Orientada a Objetos** (Prof. Me. Marcos Roberto de Moraes — Maromo) — **Fatec Mogi Mirim**.

## 1. Identificação

- **Alunos:** 

Gabriel Filipini Tristão

Pedro Gabriel de Freita

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

### 1. Implementação da tela Swing

**Prompt utilizado:** "Me ajuda a implementar a tela principal desse projeto usando Swing"

**O que foi feito:** Auxílio na criação da tela principal, na configuração da janela e na organização dos componentes da interface gráfica.

**Arquivo:** `TelaClinicaVeterinaria.java`

```java
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
```

Também foi trabalhada a inicialização da tela principal.

**Arquivo:** `Main.java`

```java
public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        TelaClinicaVeterinaria tela = new TelaClinicaVeterinaria();
        tela.setVisible(true);
    });
}
```

### 2. Regra de negócio

**Prompt utilizado:** "Como faço pra não deixar cadastrar duas consultas no mesmo horário?"

**O que foi feito:** Validação que percorre a lista de consultas e verifica se já existe uma consulta com o mesmo ID ou horário. Consultas canceladas não bloqueiam novos agendamentos no mesmo horário.

**Arquivo:** `TelaClinicaVeterinaria.java`

```java
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
```

### 3. Ordenação das consultas

**Prompt utilizado:** "Como faço pra ordenar as consultas pelo horário usando Comparable?"

**O que foi feito:** Implementação do método `compareTo()` para organizar as consultas pela data e horário. Em caso de empate, a ordenação é feita pelo ID.

**Arquivo:** `ConsultaPet.java`

```java
@Override
public int compareTo(ConsultaPet outra) {
    int cmpData = this.dataHora.compareTo(outra.dataHora);
    if (cmpData != 0) {
        return cmpData;
    }
    return Integer.compare(this.id, outra.id);
}
```

Também foi utilizado o `Collections.sort()` para ordenar a lista.

**Arquivo:** `TelaClinicaVeterinaria.java`

```java
private void ordenarConsultas() {
    Collections.sort(consultas);
    exibirTodasConsultas("Consultas ordenadas por data/horário (Collections.sort):");
}
```

### 4. Revisão do projeto

**Prompt utilizado:** "Confere se meu projeto tem tudo que o professor pediu no PDF."
