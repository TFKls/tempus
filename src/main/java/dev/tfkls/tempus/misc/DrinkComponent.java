package dev.tfkls.tempus.misc;

public class DrinkComponent {
    private final int thirst;
    private final boolean purified;

    public DrinkComponent(int thirst) {
        this.thirst = thirst;
        this.purified = false;
    }

    public DrinkComponent(int thirst, boolean purified) {
        this.thirst = thirst;
        this.purified = purified;
    }

    public int getThirst() {
        return thirst;
    }

    public boolean isPurified() {
        return purified;
    }

    public interface MixinAccessor {
        DrinkComponent tempus$getDrinkComponent();

        default boolean tempus$isDrinkable() {
            return tempus$getDrinkComponent() != null;
        }
    }

    public interface MutableMixinAccessor extends MixinAccessor {
        DrinkComponent.MutableMixinAccessor tempus$setDrinkComponent(DrinkComponent newDrink);
    }
}
