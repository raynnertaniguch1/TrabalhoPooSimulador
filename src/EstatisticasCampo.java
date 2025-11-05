import java.util.HashMap;
import java.util.Iterator;

/**
 * Esta classe coleta e fornece alguns dados estatísticos sobre o estado
 * de um campo. É flexível: cria e mantém um contador para qualquer classe
 * de objeto encontrada dentro do campo.
 * 
 * @author David J. Barnes e Michael Kolling
 * @version 2002-04-23
 */
public class EstatisticasCampo
{
    // Contadores para cada tipo de entidade (raposa, coelho etc.) na simulação.
    private HashMap contadores;
    // Indica se os contadores estão atualizados.
    private boolean contagensValidas;

    /**
     * Constrói um objeto de estatísticas de campo.
     */
    public EstatisticasCampo()
    {
        // Cria uma coleção de contadores para cada tipo de animal que
        // possa ser encontrado.
        contadores = new HashMap();
        contagensValidas = true;
    }

    /**
     * @return Uma string descrevendo quais animais estão no campo.
     */
    public String getDetalhesPopulacao(Campo campo)
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
     * Invalida o conjunto atual de estatísticas; reinicia todas as
     * contagens para zero.
     */
    public void reiniciar()
    {
        contagensValidas = false;
        Iterator chaves = contadores.keySet().iterator();
        while(chaves.hasNext()) {
            Contador contador = (Contador) contadores.get(chaves.next());
            contador.reiniciar();
        }
    }

    /**
     * Incrementa a contagem para uma classe de animal.
     */
    public void incrementarContagem(Class classeAnimal)
    {
        Contador contador = (Contador) contadores.get(classeAnimal);
        if(contador == null) {
            // Ainda não temos um contador para esta espécie — cria um.
            contador = new Contador(classeAnimal.getName());
            contadores.put(classeAnimal, contador);
        }
        contador.incrementar();
    }

    /**
     * Indica que a contagem de animais foi concluída.
     */
    public void contagemFinalizada()
    {
        contagensValidas = true;
    }

    /**
     * Determina se a simulação ainda é viável.
     * Ou seja, se ela deve continuar a ser executada.
     * @return true se houver mais de uma espécie viva.
     */
    public boolean eViavel(Campo campo)
    {
        // Quantas contagens são diferentes de zero.
        int naoZeradas = 0;
        if(!contagensValidas) {
            gerarContagens(campo);
        }
        Iterator chaves = contadores.keySet().iterator();
        while(chaves.hasNext()) {
            Contador info = (Contador) contadores.get(chaves.next());
            if(info.getContagem() > 0) {
                naoZeradas++;
            }
        }
        return naoZeradas > 1;
    }
    
    /**
     * Gera as contagens do número de raposas e coelhos.
     * Essas contagens não são mantidas em tempo real à medida que
     * os animais são colocados no campo, mas apenas quando a informação
     * é solicitada.
     */
    private void gerarContagens(Campo campo)
    {
        reiniciar();
        for(int linha = 0; linha < campo.getProfundidade(); linha++) {
            for(int coluna = 0; coluna < campo.getLargura(); coluna++) {
                Object animal = campo.getObjetoEm(new Localizacao(linha, coluna));
                if(animal != null) {
                    incrementarContagem(animal.getClass());
                }
            }
        }
        contagensValidas = true;
    }
}
