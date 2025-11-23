import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Classe abstrata, representa um ANIMAL no simulador.
 * 
 * Esta classe contém todo comportamento que é comum á todos os animais :
 * - energia / fome
 * - movimentação
 * - procura de comida
 * - comer presas
 * - procurar espaço livre
 * - reprodução baseada em probabilidade e ninhadas
 *
 * Ela complementa SerVivo, que contém APENAS o ciclo de vida básico.
 *
 * Subclasses específicas (Coelho, Raposa, Lobo, etc.) devem implementar:
 * - podeComer()  define dieta
 * - energiaPorPresa()  energia obtida ao comer
 * - criarFilho()  cria novo indivíduo da espécie
 *
 */
public abstract class Animal extends SerVivo {

    /** Energia atual do animal (diminui a cada passo, aumenta ao comer) */
    protected int energia;

    /** Energia inicial ao nascer */
    protected int energiaInicial;

    /** Energia perdida a cada passo do simulador */
    protected int perdaEnergiaPorPasso;

    // Atributos da reprodução

    /** Idade mínima para se reproduzir */
    protected int idadeReproducao;

    /** Probabilidade de reproduzir a cada passo */
    protected double probabilidadeReproducao;

    /** Número máximo de animais gerados por reprodução */
    protected int tamanhoMaximoNinhada;

    /** Gerador aleatório compartilhado */
    protected static final Random aleatorio = new Random();

    //
    // comportamento de cada animal
    //

    @Override
    protected void comportamentoEspecifico(Campo campoAtual, Campo campoNovo, List<Ator> novos) {
        // Pierde energia ao se mover / viver
        perderEnergia();

        if (!estaVivo()) return;

        // Primeiro tenta encontrar comida
        Localizacao novaLocalizacao = procurarComida(campoAtual);

        // Se não encontrou comida → tenta se mover para um espaço vazio
        if (novaLocalizacao == null) {
            novaLocalizacao = encontrarEspacoLivre(campoAtual);
        }

        // Se encontrou um lugar para ir (comida ou espaço vazio)
        if (novaLocalizacao != null) {
            moverPara(campoNovo, novaLocalizacao);

        } else {
            // Não conseguiu mover → morre por superlotação
            morrer();
        }
    }

    // questão da energia/comida

    /**
     * Reduz energia a cada ciclo; se chegar a zero, o animal morre.
     */
    protected void perderEnergia() {
        energia -= perdaEnergiaPorPasso;
        if (energia <= 0) morrer();
    }

    /**
      
     * Procura comida nas localizações adjacentes.
     * Se encontrar um ator que pode ser comida → come e move para lá.
     */
        protected Localizacao procurarComida(Campo campo) {

        Iterator<Localizacao> it = campo.localizacoesAdjacentes(getLocalizacao());
        while (it.hasNext()) {
            Localizacao onde = it.next();

            Object obj = campo.getObjetoEm(onde);  // vem como Object

            // Testa se é um SerVivo (animal ou planta)
            if (obj instanceof SerVivo) {
                SerVivo ser = (SerVivo) obj;

                // verifica dieta
                if (ser.estaVivo() && podeComer(ser)) {
                    comer(ser);
                    return onde;
                }
            }
        }

        return null;
    }


    /**
     * Come a presa e recupera energia.
     */
    protected void comer(SerVivo presa) {
        presa.morrer();
        energia += energiaPorPresa(presa);
    }

    // movimentação de cada animal

    /**
 * Procura um espaço vazio para se mover.
 */
    protected Localizacao encontrarEspacoLivre(Campo campo) {

        Iterator<Localizacao> it = campo.localizacoesAdjacentes(getLocalizacao());
        while (it.hasNext()) {
            Localizacao onde = it.next();
            if (campo.getObjetoEm(onde) == null) {
                return onde;
            }
        }

        return null;
    }


    /**
     * Move o animal para a nova localização no campo.
     */
    protected void moverPara(Campo campoNovo, Localizacao novaLoc) {
        setLocalizacao(novaLoc);
        campoNovo.colocar(this, novaLoc);
    }

    // reprodução

    /**
     * Lógica  de reprodução para todos os animais.
     * Baseia-se em probabilidade, idade mínima e tamanho da ninhada.
     */
    @Override
    protected void reproduzir(Campo campoAtual, Campo campoNovo, List<Ator> novos) {
        if (podeReproduzir()) {
            int numeroFilhotes = aleatorio.nextInt(tamanhoMaximoNinhada) + 1;

            for (int i = 0; i < numeroFilhotes; i++) {
                Localizacao loc = encontrarEspacoLivre(campoNovo);
                if (loc != null) {
                    Animal filhote = criarFilho();
                    filhote.setLocalizacao(loc);
                    novos.add(filhote);
                    campoNovo.colocar(filhote, loc); //  garante presença no próximo campo
                }
            }
        }
    }


    /**
     * Verifica se o animal pode se reproduzir.
     */
    protected boolean podeReproduzir() {
        return idade >= idadeReproducao &&
               aleatorio.nextDouble() <= probabilidadeReproducao;
    }

    // métodos à serem implementados pela subclasse

    /**
     * Define se o animal pode comer o ator especificado (dieta).
     */
    protected abstract boolean podeComer(Ator ator);

    /**
     * Retorna a quantidade de energia obtida ao comer determinada presa.
     */
    protected abstract int energiaPorPresa(Ator ator);

    /**
     * Cria um novo animal da mesma espécie.
     */
    protected abstract Animal criarFilho();
}
