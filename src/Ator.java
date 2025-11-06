import java.util.List;

public abstract class Ator {
    //  Atributos básicos (qualquer ator no campo)
    protected boolean vivo;
    protected Localizacao localizacao;

    public Ator() {
        this.vivo = true;
    }

    public boolean estaVivo() {
        return vivo;
    }

    public void morrer() {
        vivo = false;
    }

    public void setLocalizacao(int linha, int coluna) {
        this.localizacao = new Localizacao(linha, coluna);
    }

    public void setLocalizacao(Localizacao localizacao) {
        this.localizacao = localizacao;
    }

    public Localizacao getLocalizacao() {
        return localizacao;
    }

    //  Todo ator deve agir (animais, plantas, etc.)
    public abstract void agir(Campo campoAtual, Campo campoNovo, List<Ator> novos);
}
