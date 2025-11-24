// File: Ator.java
import java.util.List;

/**
 * Interface comum a todos os participantes do simulador.
 * Cada ator executa uma ação por passo e pode estar ativo (vivo) ou não.
 *
 * Versão traduzida/refatorada pelo grupo: Lucas, Raynner, Higor e Brunno.
 */
public interface Ator
{
    /**
     * Executa a ação do ator no passo atual.
     *
     * @param campoAtual campo do passo corrente (somente leitura)
     * @param campoAtualizado campo onde será montado o próximo passo
     * @param novosAtores coleção onde devem ser colocados os recém-criados
     */
    void agir(Campo campoAtual, Campo campoAtualizado, List<Ator> novosAtores);
    boolean estaAtivo();

    Localizacao getLocalizacao();
    public void definirLocalizacao(Localizacao nova);
}
