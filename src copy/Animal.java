import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Superclasse para todos os animais do simulador.
 *
 * Centraliza comportamento comum:
 *  - ciclo de vida (idade, morte)
 *  - reprodução (regra padrão)
 *  - localização
 *  - busca genérica de comida adjacente
 *
 * Cada espécie concreta define:
 *  - dieta (podeComer)
 *  - efeito ao comer (comer)
 *  - como nasce filhote (criarFilhote)
 */
public abstract class Animal implements Ator
{
    // ===== Configurações fixas da espécie =====
    private final int idadeMaxima;
    private final int idadeReproducao;
    private final double probabilidadeReproducao;
    private final int tamanhoMaximoNinhada;

    // ===== Estado do indivíduo =====
    private int idade;
    private boolean vivo;
    private Localizacao localizacao;

    // Random compartilhado entre todos os animais
    private static final Random rand = new Random();

    /**
     * Construtor base para qualquer Animal.
     * A espécie passa os parâmetros fixos aqui.
     */
    public Animal(int idadeMaxima, int idadeReproducao,
                  double probabilidadeReproducao, int tamanhoMaximoNinhada,
                  boolean idadeAleatoria)
    {
        this.idadeMaxima = idadeMaxima;
        this.idadeReproducao = idadeReproducao;
        this.probabilidadeReproducao = probabilidadeReproducao;
        this.tamanhoMaximoNinhada = tamanhoMaximoNinhada;

        vivo = true;
        idade = 0;

        // Se solicitado, nasce com idade aleatória dentro do limite da espécie
        if (idadeAleatoria) {
            idade = rand.nextInt(idadeMaxima);
        }
    }

    /** Retorna se o animal está vivo/ativo no simulador. */
    @Override
    public boolean estaAtivo()
    {
        return vivo;
    }

    /** Retorna a localização atual do animal. */
    @Override
    public Localizacao getLocalizacao()
    {
        return localizacao;
    }

    /** Atualiza a localização do animal. */
    @Override
    public void definirLocalizacao(Localizacao nova)
    {
        localizacao = nova;
    }

    /** Desativa o animal (morte por fome, velhice, lotação etc.). */
    public void morrer()
    {
        vivo = false;
    }

    /** Incrementa idade e mata o animal se ultrapassar limite da espécie. */
    public void envelhecer()
    {
        idade++;
        if (idade > idadeMaxima) {
            morrer();
        }
    }

    /** Retorna true se o animal já tem idade mínima para reproduzir. */
    public boolean podeReproduzir()
    {
        return idade >= idadeReproducao;
    }

    /**
     * Calcula quantos filhotes nascem neste passo.
     * A reprodução ocorre só se o animal tiver idade e passar no sorteio.
     */
    public int gerarNascimentos()
    {
        if (podeReproduzir() && rand.nextDouble() <= probabilidadeReproducao) {
            return rand.nextInt(tamanhoMaximoNinhada) + 1;
        }
        return 0;
    }

    /**
     * Procura comida adjacente no campo atual (estado antigo).
     *
     * A dieta é definida por polimorfismo em podeComer().
     * Quando encontra uma presa válida:
     *  - aplica o efeito de comer (comer())
     *  - remove a presa do campo atual
     *  - retorna a localização da comida
     */
    public Localizacao encontrarComidaAdjacente(Campo campoAtual)
    {
        Iterator<Localizacao> it =
            campoAtual.localizacoesAdjacentes(getLocalizacao());

        while (it.hasNext()) {
            Localizacao onde = it.next();
            Object obj = campoAtual.getObjetoEm(onde);

            if (obj == null) continue;

            Ator a = (Ator) obj;

            if (a.estaAtivo() && podeComer(a)) {
                // aplica o efeito de comer definido na espécie
                comer(a);

                // presa sai do campo antigo imediatamente
                campoAtual.colocar(null, onde);

                return onde;
            }
        }
        return null;
    }

    /**
     * Reprodução padrão para qualquer animal.
     *
     * Gera os nascimentos e tenta colocar cada filhote
     * em uma casa livre adjacente no campo atualizado.
     */
    public void reproduzir(Campo campoAtualizado, List<Ator> novosAtores)
    {
        int nascimentos = gerarNascimentos();

        for(int b = 0; b < nascimentos; b++) {
            Localizacao livre =
                campoAtualizado.localizacaoAdjacenteLivre(getLocalizacao());

            if(livre != null) {
                Animal filhote = criarFilhote();
                filhote.definirLocalizacao(livre);

                campoAtualizado.colocar(filhote, livre);
                novosAtores.add(filhote);
            }
        }
    }

    // ===== Métodos que cada espécie deve implementar =====

    /** Define se esse animal pode comer o alvo encontrado. */
    public abstract boolean podeComer(Ator alvo);

    /** Define o efeito de comer o alvo (matar presa, ganhar energia etc.). */
    public abstract void comer(Ator alvo);

    /** Cria um filhote da própria espécie. */
    public abstract Animal criarFilhote();
}
