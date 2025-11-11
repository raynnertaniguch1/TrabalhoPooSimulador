import java.util.List;

public abstract class Animal {
    //  Atributos comuns a todos os animais
    protected int idade;
    protected boolean vivo;
    protected Localizacao localizacao;

    //  Construtor padrão
    public Animal() {
        this.idade = 0;
        this.vivo = true;
    }

    //  Métodos comuns
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

    //  Método abstrato — cada animal age de forma diferente
    public abstract void agir(Campo campoAtual, Campo campoNovo, List<Animal> novos);
}
