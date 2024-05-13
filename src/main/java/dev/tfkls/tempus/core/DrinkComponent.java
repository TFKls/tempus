package dev.tfkls.tempus.core;

public class DrinkComponent {
    private final int thirst;

    public int getThirst() {
        return thirst;
    }

    public DrinkComponent(int thirst) {
        this.thirst = thirst;
    }

    public interface MixinAccessor {
        public DrinkComponent tempus$getDrinkComponent();
        public default boolean tempus$isDrinkable() {
            return tempus$getDrinkComponent() != null;
        }
    }
    public interface MutableMixinAccessor extends MixinAccessor {
        public void tempus$setDrinkComponent(DrinkComponent newDrink);
    }
}
