package bank.control;

import java.math.BigDecimal;

/** Small helper that turns text typed on a screen into a money amount. */
final class Amounts {

    private Amounts() {
    }

    /** @throws IllegalArgumentException if the text is not a number */
    static BigDecimal parse(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Please enter an amount");
        }
        try {
            return new BigDecimal(text.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Enter a valid number, for example 250.00");
        }
    }

    /** Like parse(), but blank text means "no amount" (returns null). */
    static BigDecimal parseOptional(String text) {
        return (text == null || text.isBlank()) ? null : parse(text);
    }
}
