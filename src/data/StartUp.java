package data;

public class StartUp {
    private String nome;
    private String slogan;
    private int anoFundacao;
    private int score;

    private int pitchConvincenteQtd;
    private int bugsQtd;
    private int tracaoDeUserQtd;
    private int investidorIrritadoQtd;
    private int fakeNewsQtd;

    private String status;

    public StartUp(String nome, String slogan, int anoFundacao, int score,
                   int pitchConvincenteQtd, int bugsQtd, int investidorIrritadoQtd,
                   int tracaoDeUserQtd, int fakeNewsQtd) {
        this.nome = nome;
        this.slogan = slogan;
        this.anoFundacao = anoFundacao;
        this.score = score;
        this.pitchConvincenteQtd = pitchConvincenteQtd;
        this.bugsQtd = bugsQtd;
        this.tracaoDeUserQtd = tracaoDeUserQtd;
        this.investidorIrritadoQtd = investidorIrritadoQtd;
        this.fakeNewsQtd = fakeNewsQtd;
        this.status = "Ativa";
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getSlogan() { return slogan; }
    public void setSlogan(String slogan) { this.slogan = slogan; }

    public int getAnoFundacao() { return anoFundacao; }
    public void setAnoFundacao(int anoFundacao) { this.anoFundacao = anoFundacao; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getPitchConvincenteQtd() { return pitchConvincenteQtd; }
    public void setPitchConvincenteQtd(int pitchConvincenteQtd) { this.pitchConvincenteQtd = pitchConvincenteQtd; }

    public int getBugsQtd() { return bugsQtd; }
    public void setBugsQtd(int bugsQtd) { this.bugsQtd = bugsQtd; }

    public int getTracaoDeUserQtd() { return tracaoDeUserQtd; }
    public void setTracaoDeUserQtd(int tracaoDeUserQtd) { this.tracaoDeUserQtd = tracaoDeUserQtd; }

    public int getInvestidorIrritadoQtd() {
        return investidorIrritadoQtd;
    }

    public void setInvestidorIrritadoQtd(int investidorIrritadoQtd) {
        this.investidorIrritadoQtd = investidorIrritadoQtd;
    }

    public int getFakeNewsQtd() { return fakeNewsQtd; }
    public void setFakeNewsQtd(int fakeNewsQtd) { this.fakeNewsQtd = fakeNewsQtd; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public void setVencedora() {
        this.status = "Campeã";
    }

    public void setPerdedora() {
        this.status = "Derrotada";
    }

    @Override
    public String toString() {
        return "StartUp: " + nome +
                "\nSlogan: " + slogan +
                "\nAno de Fundação: " + anoFundacao +
                "\nScore: " + score +
                "\nPitch Convincente (Qtd): " + pitchConvincenteQtd +
                "\nBugs Encontrados (Qtd): " + bugsQtd +
                "\nTração de Usuário (Qtd): " + tracaoDeUserQtd +
                "\nFake News (Qtd): " + fakeNewsQtd +
                "\nStatus: " + status;
    }
}
