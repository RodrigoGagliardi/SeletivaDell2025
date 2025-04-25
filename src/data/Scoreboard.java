package data;

import aplicacao.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Scoreboard {

    private Map<StartUp, String> statusStartups = new LinkedHashMap<>();
    private RepositorioStartUp repositorio;

    public Scoreboard(RepositorioStartUp repositorio) {
        this.repositorio = repositorio;
    }

    // Add this method to access the repository
    public RepositorioStartUp getRepositorio() {
        return this.repositorio;
    }

    // Inicializa status das startups registradas
    public void inicializarStatus() {
        for (StartUp s : repositorio.getLista()) {
            statusStartups.putIfAbsent(s, "Ativa");
        }
    }

    public List<StartUp> buscarAtivos() {
        List<StartUp> ativos = new ArrayList<>();
        for (Map.Entry<StartUp, String> entry : statusStartups.entrySet()) {
            if ("Ativa".equals(entry.getValue())) {
                ativos.add(entry.getKey());
            }
        }
        return ativos;
    }

    public void marcarDerrotada(StartUp s) {
        statusStartups.put(s, "Derrotada");
    }

    public void definirCampea(StartUp campea) {
        for (StartUp s : repositorio.getLista()) {
            if (s.equals(campea)) {
                statusStartups.put(s, "Campeã");
            } else if (!"Derrotada".equals(statusStartups.get(s))) {
                statusStartups.put(s, "Ativa");
            }
        }
    }

    public void mostrar() {
        System.out.println("\nSCOREBOARD");

        // Ordenar por pontuação em ordem decrescente
        Collections.sort(repositorio.getLista(), new Comparator<StartUp>() {
            @Override
            public int compare(StartUp s1, StartUp s2) {
                return Integer.compare(s2.getScore(), s1.getScore());
            }
        });

        // Exibir as startups ordenadas
        for (StartUp s : repositorio.getLista()) {
            String status = statusStartups.getOrDefault(s, "Ativa");
            System.out.printf("- %-20s | Score: %3d | Status: %s%n", s.getNome(), s.getScore(), status);
        }
    }

    public boolean exportarCSV(String caminho) {
        try (PrintWriter writer = new PrintWriter(new File(caminho))) {
            writer.println("Nome,Score,Status,pitches convincentes,bugs,tração de usuário,investidor irritado,fake news no pitch");
            for (StartUp s : repositorio.getLista()) {
                String status = statusStartups.getOrDefault(s, "Ativa");
                writer.println(s.getNome() + "," + s.getScore() + "," + status + "," + s.getPitchConvincenteQtd() + "," + s.getBugsQtd() + "," + s.getTracaoDeUserQtd()
                        + "," + s.getInvestidorIrritadoQtd() + "," + s.getFakeNewsQtd());
            }
            System.out.println("Scoreboard exportado com sucesso para: " + caminho);
            return true;
        } catch (IOException e) {
            System.out.println("Erro ao exportar: " + e.getMessage());
            return false;
        }
    }

    public boolean importarCSV(String caminho) {
        try {
            Path path = Paths.get(caminho);
            List<String> linhas = Files.readAllLines(path);

            repositorio.getLista().clear();
            statusStartups.clear();

            for (int i = 1; i < linhas.size(); i++) {
                String linha = linhas.get(i);
                String[] split = linha.split(",");

                if(split.length < 8) {
                    System.out.println("Erro ao importar: " + linha);
                    continue;
                }
                try {
                    String nome = split[0];
                    int score = Integer.parseInt(split[1]);
                    String status = split[2];
                    int pitchConvincenteQtd = Integer.parseInt(split[3]);
                    int bugsQtd = Integer.parseInt(split[4]);
                    int tracaoDeUserQtd = Integer.parseInt(split[5]);
                    int investidorIrritadoQtd = Integer.parseInt(split[6]);
                    int fakeNewsQtd = Integer.parseInt(split[7]);


                    StartUp startUp = new StartUp(nome, nome + " importado", 2025, score, pitchConvincenteQtd, bugsQtd, tracaoDeUserQtd, investidorIrritadoQtd, fakeNewsQtd);

                    repositorio.adicionar(startUp);

                    statusStartups.put(startUp, status);

                } catch (NumberFormatException e) {
                    System.out.println("Erro ao importar: " + linha);;
                    continue;
                }
            }
            System.out.println("Scoreboard importado com sucesso");
            return true;

        } catch (IOException e) {
            System.out.println("Erro ao importar: " + e.getMessage());
            return false;
        }
    }
}
