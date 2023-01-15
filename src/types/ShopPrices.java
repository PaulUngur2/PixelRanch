package types;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public enum ShopPrices {
    Pork(10),
    Eggs(5),
    Chicken_Meat(8),
    Hide(20),
    Rabbit_Meat(12),
    Beef(15),
    Wool(10),
    Mutton(12),
    Milk(7);

    private final int value;

    ShopPrices(int value) {
        this.value = value;

    }

    public int getValue() {
        return value;
    }
}
