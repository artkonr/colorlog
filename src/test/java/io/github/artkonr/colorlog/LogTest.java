package io.github.artkonr.colorlog;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogTest {

    @Test
    void plain_text() {
        String expected = "log";
        String actual = new Log().text("log").collect();
        assertEquals(expected, actual);
    }

    @Test
    void plain_text_arg_check() {
        assertThrows(IllegalArgumentException.class, () -> new Log().text(null));
    }

    @Test
    void plain_text_several() {
        String expected = "simple log";
        String actual = new Log().text("simple").text("log").collect();
        assertEquals(expected, actual);
    }

    @Test
    void with_quoted() {
        String expected = "and I quote 'don't do this'";
        String actual = new Log()
                .text("and I quote")
                .quote("don't do this")
                .collect();
        assertEquals(expected, actual);
    }

    @Test
    void with_quoted_arg_check() {
        assertThrows(IllegalArgumentException.class, () -> new Log().quote(null));
    }

    @Test
    void with_attribute() {
        String expected = "surprising, but id=45";
        String actual = new Log()
                .text("surprising, but")
                .attribute("id", 45)
                .collect();
        assertEquals(expected, actual);
    }

    @Test
    void with_attribute_arg_check() {
        assertThrows(IllegalArgumentException.class, () -> new Log().attribute(null, null));
    }

    @Test
    void with_attribute_several() {
        String expected = "surprising, but id=45 name=Alice";
        String actual = new Log()
                .text("surprising, but")
                .attribute("id", 45)
                .attribute("name", "Alice")
                .collect();
        assertEquals(expected, actual);
    }

    @Test
    void with_explain() {
        String expected = "here is what we do: first, we need to light a fire";
        String actual = new Log()
                .text("here is what we do")
                .explain("first, we need to light a fire")
                .collect();
        assertEquals(expected, actual);
    }

    @Test
    void with_explain_arg_check() {
        assertThrows(IllegalArgumentException.class, () -> new Log().explain(null));
    }

    @Test
    void with_mdc() {
        String expected = "[debug] some info";
        String actual = new Log()
                .text("some info")
                .mdc("debug")
                .collect();
        assertEquals(expected, actual);
    }

    @Test
    void with_mdc_arg_check() {
        assertThrows(IllegalArgumentException.class, () -> new Log().mdc(null));
        assertThrows(IllegalArgumentException.class, () -> new Log().mdc((String) null, null));
    }

    @Test
    void with_mdc_several() {
        String expected = "[debug, concurrent, threadId=150] some info";
        String actual = new Log()
                .text("some info")
                .mdc("debug")
                .mdc("concurrent")
                .mdc("threadId", 150)
                .collect();
        assertEquals(expected, actual);
    }

    @Test
    void with_list_items() {
        String expected =
                """
                here is what we do
                  - light a fire
                  - call for help\
                """;
        String actual = new Log()
                .text("here is what we do")
                .listItem("light a fire")
                .listItem("call for help")
                .collect();
        assertEquals(expected, actual);
    }

    @Test
    void with_list_item_arg_check() {
        assertThrows(IllegalArgumentException.class, () -> new Log().listItem(null));
    }

    @Test
    void to_string() {
        String expected = "\u001B[0m\u001B[0msimple log";
        String actual = new Log().text("simple log").toString();

        assertEquals(expected, actual);
    }
}