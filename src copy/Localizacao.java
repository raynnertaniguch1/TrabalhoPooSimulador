/**
 * Representa uma localização em uma grade retangular.
 *
 * @author David J. Barnes e Michael Kolling (Traduzido por IA)
 * @version 2002-04-09
 */
public class Localizacao
{
    // Posições de linha e coluna.
    private int linha;
    private int coluna;

    /**
     * Representa uma linha e coluna.
     * @param linha A linha.
     * @param coluna A coluna.
     */
    public Localizacao(int linha, int coluna)
    {
        this.linha = linha;
        this.coluna = coluna;
    }

    /**
     * Implementa a igualdade de conteúdo.
     */
    public boolean equals(Object obj)
    {
        if(obj instanceof Localizacao) {
            Localizacao outro = (Localizacao) obj;
            return linha == outro.getLinha() && coluna == outro.getColuna();
        }
        else {
            return false;
        }
    }

    /**
     * Retorna uma string no formato linha,coluna
     * @return Uma representação em string da localização.
     */
    public String toString()
    {
        return linha + "," + coluna;
    }

    /**
     * Usa os 16 bits superiores para o valor da linha e os inferiores para
     * a coluna. Exceto por grades muito grandes, isso deve dar um
     * código hash exclusivo para cada par (linha, coluna).
     */
    public int hashCode()
    {
        return (linha << 16) + coluna;
    }

    /**
     * @return A linha.
     */
    public int getLinha()
    {
        return linha;
    }

    /**
     * @return A coluna.
     */
    public int getColuna()
    {
        return coluna;
    }
}