package data;

import java.util.*;

public class RepositorioStartUp {

    private List<StartUp> lista = new ArrayList<>();
    private List<StartUp> vencedores = new ArrayList<>();
    private List<Confrontos> confronto = new ArrayList<>();

    public void adicionar(StartUp s) {
        lista.add(s);
    }

    public List<StartUp> getLista() {
        return lista;
    }

    public void adicionaVencedor(StartUp s) {
        vencedores.add(s);
    }

    public List<StartUp> getVencedores() {
        return vencedores;
    }

    public void adicionaConfronto(StartUp s1, StartUp s2) {
        confronto.add(new Confrontos(s1, s2));
    }

    public List<Confrontos> getConfrontos() {
        return confronto;
    }

}
