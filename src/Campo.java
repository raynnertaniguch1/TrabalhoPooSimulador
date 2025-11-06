import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * Representa uma grade retangular de posições no campo.
 * Cada posição pode armazenar um único animal.
 * 
 * @author David J. Barnes e Michael Kolling
 * @version 2002-04-09
 */
public class Campo
{
    private static final Random aleatorio = new Random();
    
    // A profundidade e a largura do campo.
    private int profundidade, largura;
    // Armazenamento para os animais.
    private Object[][] campo;

    /**
     * Representa um campo com as dimensões fornecidas.
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
     * Coloca um animal na posição indicada.
     * Se já existir um animal na posição, ele será substituído.
     * @param animal O animal a ser colocado.
     * @param linha A coordenada da linha.
     * @param coluna A coordenada da coluna.
     */
    public void colocar(Object animal, int linha, int coluna)
    {
        colocar(animal, new Localizacao(linha, coluna));
    }
    
    /**
     * Coloca um animal na localização indicada.
     * Se já existir um animal nessa localização, ele será substituído.
     * @param animal O animal a ser colocado.
     * @param localizacao Onde colocar o animal.
     */
    public void colocar(Object animal, Localizacao localizacao)
    {
        campo[localizacao.getLinha()][localizacao.getColuna()] = animal;
    }
    
    /**
     * Retorna o objeto na localização indicada, se houver.
     * @param localizacao A localização desejada no campo.
     * @return O objeto na localização indicada ou null se não houver.
     */
    public Object getObjetoEm(Localizacao localizacao)
    {
        return getObjetoEm(localizacao.getLinha(), localizacao.getColuna());
    }
    
    /**
     * Retorna o objeto na posição indicada, se houver.
     * @param linha A linha desejada.
     * @param coluna A coluna desejada.
     * @return O objeto na posição indicada ou null se não houver.
     */
    public Object getObjetoEm(int linha, int coluna)
    {
        return campo[linha][coluna];
    }
    
    /**
     * Gera uma localização aleatória que é adjacente ou igual à fornecida.
     * A localização retornada estará dentro dos limites válidos do campo.
     * @param localizacao A localização a partir da qual gerar a adjacência.
     * @return Uma localização válida dentro da grade.
     */
    public Localizacao localizacaoAdjacenteAleatoria(Localizacao localizacao)
    {
        int linha = localizacao.getLinha();
        int coluna = localizacao.getColuna();
        // Gera um deslocamento de -1, 0 ou +1 para linha e coluna.
        int proxLinha = linha + aleatorio.nextInt(3) - 1;
        int proxColuna = coluna + aleatorio.nextInt(3) - 1;
        // Verifica se a nova localização está dentro dos limites.
        if(proxLinha < 0 || proxLinha >= profundidade || proxColuna < 0 || proxColuna >= largura) {
            return localizacao;
        }
        else if(proxLinha != linha || proxColuna != coluna) {
            return new Localizacao(proxLinha, proxColuna);
        }
        else {
            return localizacao;
        }
    }
    
    /**
     * Tenta encontrar uma localização livre adjacente à fornecida.
     * Se não houver nenhuma, retorna a própria localização se estiver livre.
     * Caso contrário, retorna null.
     * A localização retornada estará dentro dos limites válidos do campo.
     * @param localizacao A localização a partir da qual gerar a adjacência.
     * @return Uma localização válida dentro da grade, ou null se estiver cheia.
     */
    public Localizacao localizacaoAdjacenteLivre(Localizacao localizacao)
    {
        Iterator adjacentes = localizacoesAdjacentes(localizacao);
        while(adjacentes.hasNext()) {
            Localizacao proxima = (Localizacao) adjacentes.next();
            if(campo[proxima.getLinha()][proxima.getColuna()] == null) {
                return proxima;
            }
        }
        // Verifica se a localização atual está livre.
        if(campo[localizacao.getLinha()][localizacao.getColuna()] == null) {
            return localizacao;
        } 
        else {
            return null;
        }
    }

    /**
     * Gera um iterador sobre uma lista embaralhada de localizações adjacentes
     * à fornecida. A lista não incluirá a própria localização.
     * Todas as localizações estarão dentro dos limites da grade.
     * @param localizacao A localização de origem.
     * @return Um iterador sobre as localizações adjacentes.
     */
    public Iterator localizacoesAdjacentes(Localizacao localizacao)
    {
        int linha = localizacao.getLinha();
        int coluna = localizacao.getColuna();
        LinkedList locais = new LinkedList();
        for(int dLinha = -1; dLinha <= 1; dLinha++) {
            int proxLinha = linha + dLinha;
            if(proxLinha >= 0 && proxLinha < profundidade) {
                for(int dColuna = -1; dColuna <= 1; dColuna++) {
                    int proxColuna = coluna + dColuna;
                    // Exclui localizações inválidas e a localização original.
                    if(proxColuna >= 0 && proxColuna < largura && (dLinha != 0 || dColuna != 0)) {
                        locais.add(new Localizacao(proxLinha, proxColuna));
                    }
                }
            }
        }
        Collections.shuffle(locais, aleatorio);
        return locais.iterator();
    }

    /**
     * @return A profundidade do campo.
     */
    public int getProfundidade()
    {
        return profundidade;
    }
    
    /**
     * @return A largura do campo.
     */
    public int getLargura()
    {
        return largura;
    }
    
    
    
}

