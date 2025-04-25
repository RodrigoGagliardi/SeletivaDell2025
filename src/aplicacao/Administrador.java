package aplicacao;

import data.*;

import java.io.*;
import java.util.*;

public class Administrador {

    private Scoreboard scoreboard;
    private Scanner sc;
    private RepositorioStartUp repositorio;
    private Confrontos confronto;

    public Administrador(Scoreboard scoreboard, Scanner sc) {
        this.scoreboard = scoreboard;
        this.sc = sc;
        this.repositorio = scoreboard.getRepositorio();
    }

    public void menuAdministrador() {
        int opc = 0;

        while (opc != 5) {
            try {
                System.out.println("\nMENU ADMINISTRADOR");
                System.out.println("1. Exibir scoreboard");
                System.out.println("2. Ministrar batalha");
                System.out.println("3. Exportar scoreboard para CSV");
                System.out.println("4. importar scoreboard de um CSV");
                System.out.println("5. Voltar ao menu principal");
                System.out.print("Escolha uma opção: ");
                opc = sc.nextInt();
                sc.nextLine();

                switch (opc) {
                    case 1:
                        exibirScoreboard();
                        break;
                    case 2:
                        ministraBatalha();
                        break;
                    case 3:
                        exportarScoreboardCSV();
                        break;
                    case 4:
                        importarScoreboardCSV();
                    case 5:
                        System.out.println("Voltando ao menu principal...");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Digite um número.");
                sc.nextLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void exibirScoreboard() {
        scoreboard.mostrar();
    }

    public void exportarScoreboardCSV() {
        System.out.print("Digite o nome do arquivo para exportar (sem extensão): ");
        String nomeArquivo = sc.nextLine();
        boolean sucesso = scoreboard.exportarCSV(nomeArquivo + ".csv");
        if (sucesso) {
            System.out.println("O arquivo aparecerá assim que finalizar o sistema!");
        } else {
            System.out.println("Falha ao exportar o scoreboard.");
        }
    }

    public void importarScoreboardCSV() {
        System.out.println("AVISO! Caso persista nesta ação os dados atuais serão excluidos!");
        System.out.println("Deseja continuar? (y/n)");

        String continuar = sc.nextLine().trim().toLowerCase();

        if (continuar.toLowerCase().equals("y")) {

            if (!repositorio.getConfrontos().isEmpty() || !repositorio.getVencedores().isEmpty()) {// torneio em andamento?
                System.out.println("Não é possível importar dados com um torneio em andamento.");
                return;
            }

            System.out.print("Digite o nome do arquivo do qual deseja importar o scoreboard (sem extensão): ");

                String nomeArquivo = sc.nextLine();
                boolean sucesso = scoreboard.importarCSV(nomeArquivo + ".csv");
                if (sucesso) {
                    System.out.println("O arquivo foi implementado ao sistema!");
                } else {
                    System.out.println("Scoreboard Não encontrado.");
                }

        } else if(continuar.toLowerCase().equals("n")) {
            System.out.println("Retornando ao menu...");
        } else {
            System.out.println("opção inválida.");
        }
    }

    public void ministraBatalha() {
        System.out.println("\nMINISTRAR BATALHA");

        List<Confrontos> confrontos = repositorio.getConfrontos();//confrontos sorteados disponíveis?

        if (confrontos.isEmpty()) {
            List<StartUp> vencedores = repositorio.getVencedores();

            if (vencedores.isEmpty()) {
                System.out.println("Não há confrontos disponíveis, tente iniciar o torneio.");
                return;
            } else if (vencedores.size() == 1) {
                StartUp campea = vencedores.get(0);
                System.out.println("A StartUp vencedora do StartUp Rush é " + campea.getNome() + "!");
                System.out.println(campea.getSlogan());
                scoreboard.definirCampea(campea);
                scoreboard.mostrar();
                vencedores.clear();
                return;
            } else {
                System.out.println("Confrontos concluídos, iniciando nova fase do torneio!");


                if (vencedores.size() == 3) {//Caso especial para 6 equipes
                    System.out.println("Como há 3 StartUps vencedoras, uma delas será sorteada " +
                            "aleatóriamente para passar de fase e receber 30 pontos de vitória simples");

                    Random random = new Random();
                    int indiceAvancar = random.nextInt(vencedores.size());
                    StartUp avancaAutomatico = vencedores.get(indiceAvancar);
                    vencedores.remove(indiceAvancar);

                    System.out.println("A startup " + avancaAutomatico.getNome() + " avançou automaticamente e ganhou 30 pontos de vitória simples!");
                    avancaAutomatico.setScore(avancaAutomatico.getScore() + 30);

                    // Criar confronto entre as duas startups restantes
                    System.out.println("\nNovo confronto foi sorteado");
                    repositorio.adicionaConfronto(vencedores.get(0), vencedores.get(1));
                    Confrontos confronto = repositorio.getConfrontos().get(repositorio.getConfrontos().size() - 1);
                    System.out.println(confronto);

                    // Adicionar a startup que avançou automaticamente à lista de vencedores da próxima fase
                    List<StartUp> novaListaVencedores = new ArrayList<>();
                    novaListaVencedores.add(avancaAutomatico);

                    repositorio.getVencedores().clear();
                    repositorio.getVencedores().add(avancaAutomatico);

                } else if (vencedores.size() % 2 != 0) {
                    System.out.println("Como há um número ímpar de StartUps, uma delas será sorteada " +
                            "aleatóriamente para passar de fase e receber 30 pontos de vitória simples");

                    Random random = new Random();
                    int indiceAvancar = random.nextInt(vencedores.size());
                    StartUp avancaAutomatico = vencedores.get(indiceAvancar);
                    vencedores.remove(indiceAvancar);

                    System.out.println("A startup " + avancaAutomatico.getNome() + " avançou automaticamente e ganhou 30 pontos de vitória simples!");
                    avancaAutomatico.setScore(avancaAutomatico.getScore() + 30);

                    List<Integer> indices = new ArrayList<>();
                    for (int i = 0; i < vencedores.size(); i++) {
                        indices.add(i);
                    }

                    Collections.shuffle(indices);

                    System.out.println("\nNovos confrontos foram sorteados");
                    for (int i = 0; i < vencedores.size(); i += 2) {
                        if (i + 1 < vencedores.size()) {
                            StartUp s1 = vencedores.get(indices.get(i));
                            StartUp s2 = vencedores.get(indices.get(i + 1));

                            repositorio.adicionaConfronto(s1, s2);
                            Confrontos confronto = repositorio.getConfrontos().get(repositorio.getConfrontos().size() - 1);
                            System.out.println(confronto);
                        }
                    }

                    // Limpa confrontos anteriores e adiciona apenas a startup que avançou
                    List<StartUp> novaListaVencedores = new ArrayList<>();
                    novaListaVencedores.add(avancaAutomatico);

                    repositorio.getVencedores().clear();
                    repositorio.getVencedores().add(avancaAutomatico);

                } else {
                    List<Integer> indices = new ArrayList<>();
                    for (int i = 0; i < vencedores.size(); i++) {
                        indices.add(i);
                    }

                    Collections.shuffle(indices);
                    System.out.println("\nNovos confrontos foram sorteados");

                    // Corrigido: loop para criar confrontos com número par de equipes
                    for (int i = 0; i < vencedores.size(); i += 2) {
                        StartUp s1 = vencedores.get(indices.get(i));
                        StartUp s2 = vencedores.get(indices.get(i + 1));

                        repositorio.adicionaConfronto(s1, s2);
                        Confrontos confronto = repositorio.getConfrontos().get(repositorio.getConfrontos().size() - 1);
                        System.out.println(confronto);
                    }

                    repositorio.getVencedores().clear();
                }

                System.out.println("\nVolte ao menu de ministrar batalha para gerenciar os novos confrontos!");
                return;
            }
        }

        System.out.println("\nConfrontos disponíveis:");
        for (int i = 0; i < confrontos.size(); i++) {
            System.out.println((i + 1) + ". " + confrontos.get(i));
        }

        int selecao = 0;
        try {
            System.out.print("Selecione o número do confronto para ministrar: ");
            selecao = sc.nextInt();
            sc.nextLine();

            if (selecao < 1 || selecao > confrontos.size()) {
                System.out.println("Seleção inválida.");
                return;
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Digite um número.");
            sc.nextLine();
            return;
        }

        // Obter o confronto selecionado
        confronto = confrontos.get(selecao - 1);
        StartUp s1 = confronto.getStartup1();
        StartUp s2 = confronto.getStartup2();

        System.out.println("Confronto selecionado: " + s1.getNome() + " VS " + s2.getNome());

        // Avaliar startups
        int score1 = avaliarStartup(s1);
        int score2 = avaliarStartup(s2);

        System.out.println("\nResultado da batalha:");
        System.out.println(s1.getNome() + ": " + (s1.getScore() + score1) + " pontos");
        System.out.println(s2.getNome() + ": " + (s2.getScore() + score2) + " pontos");


        s1.setScore(s1.getScore() + score1);
        s2.setScore(s2.getScore() + score2);

        if (s1.getScore() > s2.getScore()) {
            System.out.println("Vencedora: " + s1.getNome());
            repositorio.adicionaVencedor(s1);
            scoreboard.marcarDerrotada(s2);
            s1.setScore(s1.getScore() + 30);
            s1.setVencedora();
            s2.setPerdedora();
        } else if (s2.getScore() > s1.getScore()) {
            System.out.println("Vencedora: " + s2.getNome());
            repositorio.adicionaVencedor(s2);
            scoreboard.marcarDerrotada(s1);
            s2.setScore(s2.getScore() + 30);
            s2.setVencedora();
            s1.setPerdedora();
        } else {
            System.out.println("Empate! Hora da SharkFight!");
            realizarSharkFight(s1, s2);
        }

        // Remover o confronto da lista após ser ministrado
        confrontos.remove(selecao - 1);

        if (confrontos.isEmpty()) {
            System.out.println("Parece que não há mais confrontos para serem ministrados nesta fase," +
                    " volte ao menu ministrar batalha para iniciar uma nova ou verificar a campeã!");
        }
    }

    private void realizarSharkFight(StartUp s1, StartUp s2) {
        Random r = new Random();
        int bonus1 = 0;
        int bonus2 = 0;


        if (r.nextInt(2) == 0) {
            bonus1 += 2;
        } else {
            bonus2 += 2;
        }

        s1.setScore(s1.getScore() + bonus1);
        s2.setScore(s2.getScore() + bonus2);

        System.out.println("SharkFight finalizada!");
        System.out.println(s1.getNome() + " recebeu bônus de " + bonus1 + " pontos. Total: " + s1.getScore());
        System.out.println(s2.getNome() + " recebeu bônus de " + bonus2 + " pontos. Total: " + s2.getScore());

        if (s1.getScore() > s2.getScore()) {
            System.out.println("Vencedora: " + s1.getNome());
            repositorio.adicionaVencedor(s1);
            scoreboard.marcarDerrotada(s2);
            s1.setScore(s1.getScore() + 30);
            s1.setVencedora();
            s2.setPerdedora();
        } else {
            System.out.println("Vencedora: " + s2.getNome());
            repositorio.adicionaVencedor(s2);
            scoreboard.marcarDerrotada(s1);
            s2.setScore(s2.getScore() + 30);
            s2.setVencedora();
            s1.setPerdedora();
        }
    }

    private int avaliarStartup(StartUp s) {
        int score = 0;

        if (s == null) return score;

        System.out.println("Avaliando: " + s.getNome());

        if (perguntarPontuacao("+6 pontos | O pitch da StartUp foi convincente? (y/n): ", 6) != 0) {
            score += 6;
            s.setPitchConvincenteQtd(s.getPitchConvincenteQtd() + 1);
        }

        if (perguntarPontuacao("-4 pontos | O produto apresentou algum tipo de bug? (y/n): ", -4) != 0) {
            score += -4;
            s.setBugsQtd(s.getBugsQtd() + 1);
        }

        if (perguntarPontuacao("+3 pontos | O produto apresentou uma boa tração de usuário? (y/n): ", 3) != 0) {
            score += 3;
            s.setTracaoDeUserQtd(s.getTracaoDeUserQtd() + 1);
        }

        if (perguntarPontuacao("-5 pontos | Os investidores ficaram irritados? (y/n): ", -5) != 0) {
            score += -5;
            s.setInvestidorIrritadoQtd(s.getInvestidorIrritadoQtd() + 1);
        }

        if (perguntarPontuacao("-8 pontos | O pitch apresentou alguma fake news? (y/n): ", -8) != 0) {
            score += -8;
            s.setFakeNewsQtd(s.getFakeNewsQtd() + 1);
        }

        return score;
    }

    private int perguntarPontuacao(String pergunta, int pontos) {
        while (true) {
            System.out.print(pergunta);
            String resposta = sc.nextLine().trim().toLowerCase();

            if (resposta.equals("y")) {
                return pontos;
            } else if (resposta.equals("n")) {
                return 0;
            } else {
                System.out.println("Resposta inválida. Digite apenas 'y' ou 'n'.");
            }
        }
    }
}