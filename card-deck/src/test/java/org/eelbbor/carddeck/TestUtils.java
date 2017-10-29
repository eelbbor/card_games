package org.eelbbor.carddeck;

import java.util.Random;
import java.util.UUID;

public class TestUtils {
    private static final Random random = new Random();

    private TestUtils() {
    }

    public static int randomInteger() {
        return random.nextInt();
    }

    public static int randomInteger(int max) {
        return random.nextInt(max);
    }

    public static <E extends Enum<E>> E randomEnum(Class<E> enumData) {
        E[] enumConstants = enumData.getEnumConstants();
        return enumConstants[random.nextInt(enumConstants.length)];
    }

    public static String randomString() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
