import java.util.List;

/**
 * Classe abstrata base para todos os atores do simulador.
 * Define o comportamento mínimo que todos devem ter,
 * como estar vivo, possuir uma localização e agir no campo.
 * 
 * Essa classe pode ser herdada por SerVivo (para seres biológicos)
 * ou por outros tipos de entidades (ex: Obstáculo, Caçador).
 * 
 *  
 */
public abstract class Ator {
    
    // Estado básico
    protected boolean vivo;
    protected Localizacao localizacao;

    /**
     * Construtor padrão.
     * Inicializa o ator como vivo.
     */
    public Ator() {
        this.vivo = true;
    }

    /**
     * Verifica se o ator ainda está vivo.
     * 
     * @return true se estiver vivo, false caso contrário.
     */
    public boolean estaVivo() {
        return vivo;
    }

    /**
     * Define que o ator morreu.
     */
    public void morrer() {
        vivo = false;
    }

    /**
     * Define a localização do ator.
     * 
     * @param localizacao A nova localização.
     */
    public void setLocalizacao(Localizacao localizacao) {
        this.localizacao = localizacao;
    }

    /**
     * Define a localização do ator a partir das coordenadas de linha e coluna.
     * 
     * @param linha  A coordenada vertical da localização.
     * @param coluna A coordenada horizontal da localização.
     */
    public void setLocalizacao(int linha, int coluna) {
        this.localizacao = new Localizacao(linha, coluna);
    }

    /**
     * Retorna a localização atual do ator.
     * 
     * @return A localização atual.
     */
    public Localizacao getLocalizacao() {
        return localizacao;
    }

    /**
     * Método abstrato — cada ator age de forma diferente.
     * 
     * @param campoAtual O campo atual.
     * @param campoNovo  O campo atualizado.
     * @param novos      Lista de novos atores criados.
     */
    public abstract void agir(Campo campoAtual, Campo campoNovo, List<Ator> novos);
}
