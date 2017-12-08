package be.looorent.flexmonster;

class StringUtils {

    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static void assertNotNull(String value, String propertyName) {
        if (isEmpty(value)) {
            throw new IllegalArgumentException(propertyName + " is required.");
        }
    }

    public static void assertNotNull(Integer value, String propertyName) {
        if (value == null) {
            throw new IllegalArgumentException(propertyName + " is required.");
        }
    }
}
