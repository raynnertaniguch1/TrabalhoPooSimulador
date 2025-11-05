
/**
 * Fornece um contador para um participante da simulação.
 * Isso inclui uma string identificadora e uma contagem de quantos
 * participantes desse tipo existem atualmente dentro da simulação.
 * 
 * @author David J. Barnes e Michael Kolling
 * @version 2002-04-23
 */
public class Contador
{
    // Um nome para este tipo de participante da simulação.
    private String nome;
    // Quantos deste tipo existem na simulação.
    private int contagem;

    /**
     * Fornece um nome para um dos tipos de simulação.
     * @param nome Um nome, por exemplo "Raposa".
     */
    public Contador(String nome)
    {
        this.nome = nome;
        contagem = 0;
    }
    
    /**
     * @return A breve descrição deste tipo.
     */
    public String getNome()
    {
        return nome;
    }

    /**
     * @return A contagem atual deste tipo.
     */
    public int getContagem()
    {
        return contagem;
    }

    /**
     * Incrementa a contagem atual em uma unidade.
     */
    public void incrementar()
    {
        contagem++;
    }
    
    /**
     * Reinicia a contagem atual para zero.
     */
    public void reiniciar()
    {
        contagem = 0;
    }
}
