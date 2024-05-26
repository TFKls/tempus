package dev.tfkls.tempus.core;

public class DrinkComponent {
    private final int thirst;
    private final boolean purified;

    public int getThirst() {
        return thirst;
    }

    public boolean isPurified() { return purified; }

    public DrinkComponent(int thirst) {
        this.thirst = thirst;
        this.purified = false;
    }

    public DrinkComponent(int thirst, boolean purified) {
        this.thirst = thirst;
        this.purified = purified;
    }


    public interface MixinAccessor {
        public DrinkComponent tempus$getDrinkComponent();
        public default boolean tempus$isDrinkable() {
            return tempus$getDrinkComponent() != null;
        }
    }
    public interface MutableMixinAccessor extends MixinAccessor {
        public DrinkComponent.MutableMixinAccessor tempus$setDrinkComponent(DrinkComponent newDrink);
    }
}
