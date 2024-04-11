package io.github.artkonr.colorlog;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FmtTest {

    @Test
    void wrap_simple_color() {
        String expected = "\u001B[34mtext\u001B[0m";
        String actual = Fmt.Builder.font(4).build().wrap("text");
        assertEquals(expected, actual);
    }

    @Test
    void wrap_simple_color_background() {
        String expected = "\u001B[44mtext\u001B[0m";
        String actual = Fmt.Builder.background(4).build().wrap("text");
        assertEquals(expected, actual);
    }

    @Test
    void wrap_extended_color() {
        String expected = "\u001B[38;5;123mtext\u001B[0m";
        String actual = Fmt.Builder.font(123).build().wrap("text");
        assertEquals(expected, actual);
    }

    @Test
    void wrap_extended_color_background() {
        String expected = "\u001B[48;5;123mtext\u001B[0m";
        String actual = Fmt.Builder.background(123).build().wrap("text");
        assertEquals(expected, actual);
    }

    @Test
    void wrap_arg_check() {
        assertThrows(IllegalArgumentException.class, () -> Fmt.Builder.font(10).build().wrap(null));
    }

    @Test
    void wrap_unrecognised_code() {
        assertThrows(Fmt.UnrecognisedColorException.class, () -> Fmt.Builder.font(500));
        assertThrows(Fmt.UnrecognisedColorException.class, () -> Fmt.Builder.font(-500));
        assertDoesNotThrow(() -> Fmt.Builder.font(-30));
        assertDoesNotThrow(() -> Fmt.Builder.font(255));
    }

    @Test
    void wrap_all() {
        String expected = "\u001B[38;5;123;1;4mtext\u001B[0m";
        String actual = Fmt.Builder
                .font(123)
                .bold()
                .underlined()
                .build()
                .wrap("text");
        assertEquals(expected, actual);
    }

    @Test
    void test_exception() {
        Fmt.UnrecognisedColorException ex = new Fmt.UnrecognisedColorException(900);
        String expected = "unrecognised color code: code=900";
        assertEquals(expected, ex.getMessage());
    }
}