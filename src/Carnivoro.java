/**
 * Classe abstrata para animais carnívoros.
 * 
 * Carnívoros se alimentam de outros animais.
 */
public abstract class Carnivoro extends Animal {

    /**
     * Carnívoros podem comer QUALQUER animal (exceto plantas).
     * Subclasses podem refinar isso.
     */
    @Override
    protected boolean podeComer(Ator ator) {
        return ator instanceof Animal;  // Planta NÃO é Animal → perfeito
    }

    /**
     * Energia obtida ao comer uma presa.
     * Cada espécie define o valor específico.
     */
    @Override
    protected abstract int energiaPorPresa(Ator ator);
}
