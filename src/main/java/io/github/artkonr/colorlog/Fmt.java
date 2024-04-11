package io.github.artkonr.colorlog;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Fmt {

    public static final Fmt NORM = Fmt.Builder.font(-30).build();

    private final int color;
    private final boolean background;
    private final boolean underline;
    private final boolean bold;

    public String wrap(@NonNull String text) {
        return pref() + text + NORM.pref();
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder {
        private final int color;
        private final boolean background;
        private boolean underline = false;
        private boolean bold = false;

        public static Builder font(int value) {
            return new Builder(fixColor(value), false);
        }

        public static Builder background(int value) {
            return new Builder(fixColor(value), true);
        }

        public Builder underlined() {
            this.underline = true;
            return this;
        }

        public Builder bold() {
            this.bold = true;
            return this;
        }

        public Fmt build() {
            return new Fmt(color, background, underline, bold);
        }

        private static int fixColor(int value) {
            if (value > 0) {
                if (value > 255) {
                    throw new UnrecognisedColorException(value);
                } else {
                    return value;
                }
            } else {
                if (value == -30) {
                    return value;
                } else {
                    throw new UnrecognisedColorException(value);
                }
            }
        }

    }

    protected String pref() {
        String col = "\u001B[";

        if (color < 8) {
            col += Integer.toString(30 + color + (background ? 10 : 0));
        } else {
            col += (background ? '4' : '3') + "8;5;" + color;
        }

        if (bold) {
            col += ";1";
        }

        if (underline) {
            col += ";4";
        }

        col += "m";
        return col;
    }

    static class UnrecognisedColorException extends RuntimeException {
        public UnrecognisedColorException(int color) {
            super("unrecognised color code: code=%d".formatted(color));
        }
    }

}
