package data;

import java.util.*;

public class Confrontos {
    private StartUp startup1;
    private StartUp startup2;
    private StartUp vencedora;
    private StartUp perdedora;
    private RepositorioStartUp repositorio;

    public Confrontos(StartUp s1, StartUp s2) {
        this.startup1 = s1;
        this.startup2 = s2;
    }

    public StartUp getStartup1() {
        return startup1;
    }

    public StartUp getStartup2() {
        return startup2;
    }

    @Override
    public String toString() {
        return "---------------------\n" +
                "StartUp 1: " + startup1.getNome() + " (Score: " + startup1.getScore() + ")\n" +
                "StartUp 2: " + startup2.getNome() + " (Score: " + startup2.getScore() + ")\n" +
                (vencedora != null ? "Vencedora: " + vencedora.getNome() : "Confronto ainda não realizado");
    }
}
