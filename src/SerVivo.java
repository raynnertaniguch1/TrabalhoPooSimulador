import java.util.List;
import java.util.Random;

 

/**
 * Classe abstrata que representa um ser vivo no simulador.
 * Define o comportamento básico e o ciclo de vida comum a todos os seres biológicos.
 * 
 * Nessa refatoração, essa classe vai passar a ter apenas o comportamente comum
 * a todos seres, como idade,envelhecimento e o ciclo de vida básico
 * demais comportamentos específico de animais passa a ser da classe Animal
 * 
 * SubClasses devem implementar SOMENTE métodos abstratos, comportamentoEspecifico() e 
 * reproduzir()
 * 
 * 
 */
public abstract class SerVivo extends Ator {

    //  Atributos comuns a todos os seres vivos
         
    protected int idade;// Idade atual do ser vivo 
    protected int idadeMaxima; //Idade máxima permitida antes de morrer 

     
    protected static final Random aleatorio = new Random();     /** Gerador de números aleatórios compartilhado */
 
    public SerVivo() {
        super();
        this.idade = 0;
    }

      // 
    // definição do método Template, pra organizar o ciclo de vida comum a todos os seres(receita)
    // 

    /**
     * Template Method (final): define a ordem fixa do ciclo de vida:
     * 1) envelhecer
     * 2) comportamento específico (definido pela subclasse)
     * 3) reprodução (definida pela subclasse)
     *
     * Este método NÃO DEVE ser sobrescrito, garantindo consistência
     * entre espécies diferentes.
     */

    
     @Override
    public final void agir(Campo campoAtual, Campo campoNovo, List<Ator> novos) {
        // Passo 1 — Envelhece
        envelhecer();

        // Caso morra de velhice, para por aqui
        if (!estaVivo()) {
            return;
        }

        // Passo 2 — Ação específica da espécie
        comportamentoEspecifico(campoAtual, campoNovo, novos);

        // Caso tenha morrido pela ação, interrompe
        if (!estaVivo()) {
            return;
        }

        // Passo 3 — Reprodução (somente se ainda estiver vivo)
        reproduzir(campoAtual, campoNovo, novos);
    }


    // métodos comuns a todos os seres, COMPORTAMENTO 
    

    /**
     * Aumenta a idade e verifica se ultrapassou a idade máxima.
     * Se ultrapassar, o ser morre.
     */
    protected void envelhecer() {
        idade++;
        if (idade > idadeMaxima) {
            morrer();
        }
    }

    // MÉTODOS QUE SUBCLASSES DEVEM IMPLEMENTAR
    

    /**
     * Define o comportamento diário da espécie.
     * (Ex: Animais se movem e procuram comida; Plantas apenas existem)
     */
    protected abstract void comportamentoEspecifico(
        Campo campoAtual,
        Campo campoNovo,
        List<Ator> novos
    );

    /**
     * Define como ocorre a reprodução da espécie.
     * 
     * OBS: Para ANIMAIS, a lógica completa de reprodução,
     * envolvendo ninhadas, probabilidade, idade mínima etc.
     * FOI MOVIDA para a classe Animal.
     *
     * Aqui, a reprodução é apenas o "gatilho" abstrato —
     * a subclasse decide como criar novos indivíduos.
     */
    protected abstract void reproduzir(
        Campo campoAtual,
        Campo campoNovo,
        List<Ator> novos
    );
}
