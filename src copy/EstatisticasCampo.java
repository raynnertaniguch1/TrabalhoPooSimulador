import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Esta classe coleta e fornece alguns dados estatísticos sobre o estado
 * de um campo. É flexível: criará e manterá um contador
 * para qualquer classe de objeto que for encontrada dentro do campo.
 *
 * @author David J. Barnes e Michael Kolling (Traduzido por IA)
 * @version 2002-04-23
 */
public class EstatisticasCampo
{
    // Contadores para cada tipo de entidade (raposa, coelho, etc.) na simulação.
    private HashMap contadores;
    // Indica se os contadores estão atualmente atualizados.
    private boolean contagensValidas;

    /**
     * Constrói um objeto de estatísticas de campo.
     */
    public EstatisticasCampo()
    {
        // Configura uma coleção para contadores de cada tipo de animal que
        // podemos encontrar.
        contadores = new HashMap();
        contagensValidas = true;
    }

    /**
     * @return Uma string descrevendo quais animais estão no campo.
     */
    public String obterDetalhesPopulacao(Campo campo)
    {
        StringBuffer buffer = new StringBuffer();
        if(!contagensValidas) {
            gerarContagens(campo);
        }
        Iterator chaves = contadores.keySet().iterator();
        while(chaves.hasNext()) {
            Contador info = (Contador) contadores.get(chaves.next());
            buffer.append(info.getNome());
            buffer.append(": ");
            buffer.append(info.getContagem());
            buffer.append(' ');
        }
        return buffer.toString();
    }

    /**
     * Invalida o conjunto atual de estatísticas; redefine todas
     * as contagens para zero.
     */
    public void resetar()
    {
        contagensValidas = false;
        Iterator chaves = contadores.keySet().iterator();
        while(chaves.hasNext()) {
            Contador cont = (Contador) contadores.get(chaves.next());
            cont.resetar();
        }
    }

    /**
     * Incrementa a contagem para uma classe de animal.
     */
    public void incrementarContagem(Class classeAnimal)
    {
        Contador cont = (Contador) contadores.get(classeAnimal);
        if(cont == null) {
            // ainda não temos um contador para esta espécie - criamos um
            cont = new Contador(classeAnimal.getName());
            contadores.put(classeAnimal, cont);
        }
        cont.incrementar();
    }

    /**
     * Indica que uma contagem de animais foi concluída.
     */
    public void contagemFinalizada()
    {
        contagensValidas = true;
    }

    /**
     * Determina se a simulação ainda é viável.
     * Ou seja, se deve continuar a rodar.
     * @return true Se houver mais de uma espécie viva.
     */
    public boolean ehViavel(Campo campo)
    {
        // Quantas contagens são diferentes de zero.
        int diferenteDeZero = 0;
        if(!contagensValidas) {
            gerarContagens(campo);
        }
        Iterator chaves = contadores.keySet().iterator();
        while(chaves.hasNext()) {
            Contador info = (Contador) contadores.get(chaves.next());
            if(info.getContagem() > 0) {
                diferenteDeZero++;
            }
        }
        return diferenteDeZero > 1;
    }

    /**
     * Gera contagens do número de raposas e coelhos.
     * Estas não são mantidas atualizadas à medida que raposas e coelhos
     * são colocados no campo, mas apenas quando uma solicitação
     * é feita para a informação.
     */
    private void gerarContagens(Campo campo)
    {
        resetar();
        for(int linha = 0; linha < campo.getProfundidade(); linha++) {
            for(int coluna = 0; coluna < campo.getLargura(); coluna++) {
                Object animal = campo.getObjetoEm(linha, coluna);
                if(animal != null) {
                    incrementarContagem(animal.getClass());
                }
            }
        }
        contagensValidas = true;
    }
}