/**
 * Classe abstrata para animais herbívoros.
 * 
 * Herbívoros têm uma lógica simples de dieta: comem apenas plantas.
 * Isso evita repetição de código nas subclasses que seguem esse padrão.
 * 
 * Subclasses concretas (como Coelho) só precisam cuidar dos próprios
 * valores de energia, reprodução e criação de filhotes.
 */
public abstract class Herbivoro extends Animal {

    /**
     * Herbívoros só podem comer plantas.
     */
    @Override
    protected boolean podeComer(Ator ator) {
        return ator instanceof Planta;
    }

    /**
     * Energia recebida ao comer uma planta.
     * Cada espécie pode ajustar seu valor sobrescrevendo este método.
     */
    @Override
    protected int energiaPorPresa(Ator ator) {
        // Valor padrão. Subclasses podem personalizar.
        return 5;
    }
}
