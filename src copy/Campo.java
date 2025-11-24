import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * Representa uma grade retangular de posições de campo.
 * Cada posição é capaz de armazenar um único ator (animal, planta, etc.).
 *
 * @author David J. Barnes e Michael Kolling (Traduzido por IA)
 * @version 2002-04-09
 */
public class Campo
{
    private static final Random rand = new Random();

    // A profundidade e a largura do campo.
    private int profundidade, largura;
    // Armazenamento para os atores.
    private Object[][] campo;

    /**
     * Representa um campo das dimensões dadas.
     * @param profundidade A profundidade do campo.
     * @param largura A largura do campo.
     */
    public Campo(int profundidade, int largura)
    {
        this.profundidade = profundidade;
        this.largura = largura;
        campo = new Object[profundidade][largura];
    }

    /**
     * Esvazia o campo.
     */
    public void limpar()
    {
        for(int linha = 0; linha < profundidade; linha++) {
            for(int coluna = 0; coluna < largura; coluna++) {
                campo[linha][coluna] = null;
            }
        }
    }

    /**
     * Coloca um ator/objeto na localização dada.
     * Se já houver algo na localização, ele será perdido.
     * @param objeto O objeto a ser colocado.
     * @param linha Coordenada da linha da localização.
     * @param coluna Coordenada da coluna da localização.
     */
    public void colocar(Object objeto, int linha, int coluna)
    {
        colocar(objeto, new Localizacao(linha, coluna));
    }

    /**
     * Coloca um ator/objeto na localização dada.
     * Se já houver algo na localização, ele será perdido.
     * @param objeto O objeto a ser colocado.
     * @param localizacao Onde colocar o objeto.
     */
    public void colocar(Object objeto, Localizacao localizacao)
    {
        campo[localizacao.getLinha()][localizacao.getColuna()] = objeto;
    }

    /**
     * Retorna o objeto na localização dada, se houver.
     * @param localizacao Onde no campo.
     * @return O objeto na localização dada, ou null se não houver.
     */
    public Object getObjetoEm(Localizacao localizacao)
    {
        return getObjetoEm(localizacao.getLinha(), localizacao.getColuna());
    }

    /**
     * Retorna o objeto na localização dada, se houver.
     * @param linha A linha desejada.
     * @param coluna A coluna desejada.
     * @return O objeto na localização dada, ou null se não houver.
     */
    public Object getObjetoEm(int linha, int coluna)
    {
        return campo[linha][coluna];
    }

    /**
     * Gera uma localização aleatória adjacente à localização dada,
     * ou a mesma localização. Sempre dentro dos limites do campo.
     */
    public Localizacao localizacaoAdjacenteAleatoria(Localizacao localizacao)
    {
        int linha = localizacao.getLinha();
        int coluna = localizacao.getColuna();

        int proximaLinha = linha + rand.nextInt(3) - 1;
        int proximaColuna = coluna + rand.nextInt(3) - 1;

        if(proximaLinha < 0 || proximaLinha >= profundidade ||
           proximaColuna < 0 || proximaColuna >= largura) {
            return localizacao;
        }
        else if(proximaLinha != linha || proximaColuna != coluna) {
            return new Localizacao(proximaLinha, proximaColuna);
        }
        else {
            return localizacao;
        }
    }

    /**
     * Tenta encontrar uma localização livre adjacente à localização dada.
     * Se não houver, retorna a própria localização se ela estiver livre;
     * caso contrário, retorna null.
     */
    public Localizacao localizacaoAdjacenteLivre(Localizacao localizacao)
    {
        Iterator<Localizacao> adjacente = localizacoesAdjacentes(localizacao);

        while(adjacente.hasNext()) {
            Localizacao proxima = adjacente.next();
            if(campo[proxima.getLinha()][proxima.getColuna()] == null) {
                return proxima;
            }
        }

        if(campo[localizacao.getLinha()][localizacao.getColuna()] == null) {
            return localizacao;
        }
        else {
            return null;
        }
    }

    /**
     * Gera uma lista embaralhada de localizações adjacentes à localização dada.
     * A lista não inclui a própria localização.
     */
    public Iterator<Localizacao> localizacoesAdjacentes(Localizacao localizacao)
    {
        int linha = localizacao.getLinha();
        int coluna = localizacao.getColuna();

        LinkedList<Localizacao> localizacoes = new LinkedList<>();

        for(int deslocamentoLinha = -1; deslocamentoLinha <= 1; deslocamentoLinha++) {
            int proximaLinha = linha + deslocamentoLinha;

            if(proximaLinha >= 0 && proximaLinha < profundidade) {
                for(int deslocamentoColuna = -1; deslocamentoColuna <= 1; deslocamentoColuna++) {
                    int proximaColuna = coluna + deslocamentoColuna;

                    if(proximaColuna >= 0 && proximaColuna < largura &&
                       (deslocamentoLinha != 0 || deslocamentoColuna != 0)) {
                        localizacoes.add(new Localizacao(proximaLinha, proximaColuna));
                    }
                }
            }
        }

        Collections.shuffle(localizacoes, rand);
        return localizacoes.iterator();
    }

    public int getProfundidade() { return profundidade; }
    public int getLargura() { return largura; }

    /**
     * Retorna o ator na localização dada, ou null se não houver.
     * Atalho para evitar casts repetidos fora do Campo.
     */
    public Ator getAtorEm(Localizacao localizacao)
    {
        Object obj = getObjetoEm(localizacao);
        if(obj instanceof Ator) {
            return (Ator) obj;
        }
        return null;
    }
}
