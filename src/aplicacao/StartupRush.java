package aplicacao;

import data.*;

import java.util.*;

public class StartupRush {

    Scanner sc = new Scanner(System.in);
    private RepositorioStartUp repositorio = new RepositorioStartUp();
    private Scoreboard scoreboard = new Scoreboard(repositorio);
    private boolean sorteio = false;

    public void StartupRush() {

        int op = 0;

        while (op != 8) {
            try {
                System.out.println("\nStartupRush! ");
                System.out.println("1. Cadastrar StartUp");
                System.out.println("2. Listar StartUps");
                System.out.println("3. Remover StartUp");
                System.out.println("4. Iniciar torneio");
                System.out.println("5. Exibir Ranking");
                System.out.println("6. Regras");
                System.out.println("7. Menu Administrador");
                System.out.println("8. Sair");
                System.out.print("Escolha uma opção: \n");
                op = sc.nextInt();
                sc.nextLine();

                switch (op) {
                    case 1:
                        cadastrarStartUp();
                        break;
                    case 2:
                        listarStartUps();
                        break;
                    case 3:
                        removeStartUp();
                        break;
                    case 4:
                        iniciaTorneio();
                        break;
                    case 5:
                        exibirRanking();
                        break;
                    case 6:
                        regras();
                        break;
                    case 7:
                        menuAdministrador();
                        break;
                    case 8:
                        System.out.println("Saindo do sistema...");
                        break;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (InputMismatchException e) {
                System.out.println("ERRO! Entrada inválida. Tente novamente.");
                sc.nextLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void cadastrarStartUp() {
        if (repositorio.getLista().size() > 7) {
            System.out.println("O número máximo de StartUps já foi atingido!\n" +
                    "Caso deseje incluir uma nova StartUp será necessário remover uma existente.");
        } else {
            try {
                System.out.print("Nome: ");
                String nome = sc.nextLine();

                System.out.print("Slogan: ");
                String slogan = sc.nextLine();

                System.out.print("Ano de Fundação: ");
                int ano = sc.nextInt();
                sc.nextLine();

                boolean existe = false;
                for (StartUp s : repositorio.getLista()) {
                    if (s.getNome().equalsIgnoreCase(nome.trim())) {
                        existe = true;
                        break;
                    }
                }

                if (existe) {
                    System.out.println("Já existe uma StartUp com este nome.");
                } else {
                    StartUp startUp = new StartUp(nome, slogan, ano, 70, 0,
                            0, 0, 0, 0);

                    repositorio.adicionar(startUp);
                    System.out.println("StartUp cadastrada com sucesso!");
                }

            } catch (InputMismatchException e) {
                System.out.println("Erro: Entrada inválida. Tente novamente.");
                sc.nextLine(); // limpa a entrada inválida
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void listarStartUps() {
        try {
            if (repositorio.getLista().isEmpty()) {
                System.out.println("Nenhuma StartUp cadastrada.");
            } else {
                System.out.println("\nLista de StartUps");
                for (StartUp s : repositorio.getLista()) {
                    System.out.println(s.toString() +
                            "\n---------------------");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void removeStartUp() {
        try {
            System.out.println("\nRemover StartUp");
            System.out.print("Digite o nome da startup que deseja remover: ");
            String nome = sc.nextLine();

            StartUp paraRemover = null;

            for (StartUp s : repositorio.getLista()) {
                if (s.getNome().equalsIgnoreCase(nome.trim())) {
                    paraRemover = s;
                    break;
                }
            }

            if (paraRemover != null) {
                repositorio.getLista().remove(paraRemover); // Remove direto da lista
                System.out.println("StartUp removida com sucesso!");
            } else {
                System.out.println("StartUp não encontrada.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void iniciaTorneio() {
        List<StartUp> startups = repositorio.getLista();
        int tamanho = startups.size();

        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < tamanho; i++) {
            indices.add(i);
        }

        if(sorteioRealizado() == true) {
            System.out.println("O torneio já está em andamento! Vá para o menu Administrador para ministrá-lo.");
            return;
        }

        if (tamanho < 4) {
            System.out.println("São necessários pelo menos 4 StartUps para iniciar o torneio.");
            return;
        }

        if (tamanho % 2 != 0) {
            System.out.println("Número ímpar de StartUps! Adicione ou remova uma para formar pares.");
            return;
        }

        if (tamanho == 6) {
            System.out.println("AVISO! Você está criando um torneio de 6 StartUps, portanto na próxima fase " +
                    "uma equipe aleatória genhará uma vitória simples e passará adiante na competição.");
        }

        repositorio.getConfrontos().clear();// Limpar confrontos anteriores se existirem

        Collections.shuffle(indices);

        System.out.println("Confrontos do Torneio:");
        for (int i = 0; i < tamanho; i += 2) {
            StartUp s1 = startups.get(indices.get(i));
            StartUp s2 = startups.get(indices.get(i + 1));


            repositorio.adicionaConfronto(s1, s2);

            Confrontos confronto = repositorio.getConfrontos().get(repositorio.getConfrontos().size() - 1);// Exibir o confronto
            System.out.println(confronto);
        }

        // Inicializar o status das startups no scoreboard
        scoreboard.inicializarStatus();
        sorteio = true;

        System.out.println("\nUtilize o Menu Administrador para ministrar as batalhas.");
    }

    public void exibirRanking() {
        List<StartUp> startups = new ArrayList<>(repositorio.getLista());

        if (startups.isEmpty()) {
            System.out.println("Nenhuma StartUp cadastrada para exibir no ranking.");
            return;
        }

        startups.sort((s1, s2) -> Integer.compare(s2.getScore(), s1.getScore()));

        System.out.println("\nRanking de StartUps");
        int posicao = 1;
        for (StartUp s : startups) {
            System.out.println(posicao + "º - " + s.getNome() + " | Score: " + s.getScore());
            posicao++;
        }
    }

    public void regras() {
        System.out.println("1. É necessário no mínimo 4 e no máximo 8 StartUps.");
        System.out.println("2. As batalhas são realizadas em formato mata-mata.");
        System.out.println("3. A pontuação inicial de cada startup é 70.");
        System.out.println("4. Em torneios com 6 startups, 2 sorteadas aleatóriamente avançam para a próxima fase e recebem um bônus de 30 pontos.");
        System.out.println("5. O torneio só pode ser sorteado uma vez.");
    }

    public void menuAdministrador(){
        Administrador administrador = new Administrador(scoreboard, sc);
        administrador.menuAdministrador();
    }

    public boolean sorteioRealizado() {
        return sorteio;
    }
}