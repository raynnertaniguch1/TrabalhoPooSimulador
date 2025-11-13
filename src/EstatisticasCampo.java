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
    // (adicionei generics porque o Java atual exige pra não dar aviso chato)
    private HashMap<Class, Contador> contadores;

    // Indica se os contadores estão atualizados.
    private boolean contagensValidas;

    /**
     * Constrói um objeto de estatísticas de campo.
     */
    public EstatisticasCampo()
    {
        // Cria uma coleção de contadores para cada tipo de animal que
        // possa ser encontrado.
        contadores = new HashMap<Class, Contador>();
        contagensValidas = true;
    }

    /**
     * @return Uma string descrevendo quais animais estão no campo.
     */
    public String getDetalhesPopulacao(Campo campo)
    {
        StringBuffer buffer = new StringBuffer();
        if(!contagensValidas) {
            gerarContinhas(campo); // mudei o nome sem querer, então deixei assim mesmo
        }
        Iterator<Class> chaves = contadores.keySet().iterator();
        while(chaves.hasNext()) {
            Contador info = contadores.get(chaves.next());
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
        Iterator<Class> chaves = contadores.keySet().iterator();
        while(chaves.hasNext()) {
            Contador contador = contadores.get(chaves.next());
            contador.reiniciar();
        }
    }

    /**
     * Incrementa a contagem para uma classe de animal.
     */
    public void incrementarContagem(Class classeAtor)
    {
        Contador contador = contadores.get(classeAtor);
        if(contador == null) {
            // Ainda não temos um contador para esta espécie — cria um.
            contador = new Contador(classeAtor.getSimpleName()); // usei getSimpleName pq é mais limpo
            contadores.put(classeAtor, contador);
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
        int naoZeradas = 0;
        if(!contagensValidas) {
            gerarContinhas(campo);
        }
        Iterator<Class> chaves = contadores.keySet().iterator();
        while(chaves.hasNext()) {
            Contador info = contadores.get(chaves.next());
            if(info.getContagem() > 0) {
                naoZeradas++;
            }
        }
        return naoZeradas > 1;
    }
    
    /**
     * Gera as contagens do número de raposas e coelhos.
     */
    private void gerarContinhas(Campo campo) // nome levemente errado mantido
    {
        reiniciar();
        for(int linha = 0; linha < campo.getProfundidade(); linha++) {
            for(int coluna = 0; coluna < campo.getLargura(); coluna++) {
                Object ator = campo.getObjetoEm(new Localizacao(linha, coluna));
                if(ator != null) {
                    incrementarContagem(ator.getClass());
                }
            }
        }
        contagensValidas = true;
    }
}
