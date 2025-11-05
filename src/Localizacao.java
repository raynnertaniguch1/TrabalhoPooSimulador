/**
 * Representa uma localização em uma grade retangular.
 * 
 * @author David J. Barnes e Michael Kolling
 * @version 2002-04-09
 */
public class Localizacao
{
    // Posições de linha e coluna.
    private int linha;
    private int coluna;

    /**
     * Representa uma linha e uma coluna.
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
            Localizacao outra = (Localizacao) obj;
            return linha == outra.getLinha() && coluna == outra.getColuna();
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
     * o valor da coluna. Exceto para grades muito grandes, isso deve gerar um
     * código hash único para cada par (linha, coluna).
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
