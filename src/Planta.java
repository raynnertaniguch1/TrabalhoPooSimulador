import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Representa uma planta no simulador.
 * 
 * Plantas não se movem, não caçam e não têm energia como animais.
 * Elas apenas envelhecem e podem gerar novas plantas em espaços livres.
 */
public class Planta extends SerVivo
{
    private static final int IDADE_MAXIMA = 30;
    private static final double PROBABILIDADE_REPRODUCAO = 0.10;

    private static final Random aleatorio = new Random();

    /**
     * Cria uma planta.
     * 
     * @param idadeAleatoria Se true, a planta nasce com idade aleatória.
     */
    public Planta(boolean idadeAleatoria) {
        super();
        this.idadeMaxima = IDADE_MAXIMA;

        if (idadeAleatoria) {
            this.idade = aleatorio.nextInt(IDADE_MAXIMA);
        } else {
            this.idade = 0;
        }
    }

    /**
     * Plantas não se movem nem caçam.
     * O comportamento por ciclo é apenas “existir”.
     */
    @Override
    protected void comportamentoEspecifico(
        Campo campoAtual, Campo campoNovo, List<Ator> novos
    ) {
        // Planta permanece parada na mesma posição no próximo campo
        if (estaVivo() && getLocalizacao() != null) {
            campoNovo.colocar(this, getLocalizacao());
        }
    }


    /**
     * Reprodução simples: com certa probabilidade, planta gera outra
     * em uma localização adjacente livre.
     */
    @Override
    protected void reproduzir(
        Campo campoAtual, Campo campoNovo, List<Ator> novos
    ) {
        if (aleatorio.nextDouble() <= PROBABILIDADE_REPRODUCAO) {

            Iterator<Localizacao> it = campoNovo.localizacoesAdjacentes(getLocalizacao());
            while (it.hasNext()) {
                Localizacao adj = it.next();

                if (campoNovo.getObjetoEm(adj) == null) {
                    Planta filha = new Planta(false);
                    filha.setLocalizacao(adj);
                    novos.add(filha);
                    campoNovo.colocar(filha, adj);   //  IMPORTANTE
                    break;
                }
            }
        }
    }

}