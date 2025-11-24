/**
 * Fornece um contador para um participante na simulação.
 * Isso inclui uma string de identificação e uma contagem de quantos
 * participantes deste tipo existem atualmente na
 * simulação.
 *
 * @author David J. Barnes e Michael Kolling (Traduzido por IA)
 * @version 2002-04-23
 */
public class Contador
{
    // Um nome para este tipo de participante da simulação
    private String nome;
    // Quantos deste tipo existem na simulação.
    private int contagem;

    /**
     * Fornece um nome para um dos tipos de simulação.
     * @param nome Um nome, por exemplo, "Raposa".
     */
    public Contador(String nome)
    {
        this.nome = nome;
        contagem = 0;
    }

    /**
     * @return A descrição abreviada deste tipo.
     */
    public String getNome()
    {
        return nome;
    }

    /**
     * @return A contagem atual para este tipo.
     */
    public int getContagem()
    {
        return contagem;
    }

    /**
     * Incrementa a contagem atual em um.
     */
    public void incrementar()
    {
        contagem++;
    }

    /**
     * Redefine a contagem atual para zero.
     */
    public void resetar()
    {
        contagem = 0;
    }
}