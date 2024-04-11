package io.github.artkonr.colorlog;

import lombok.NonNull;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Fluent log message builder.
 */
public final class Log {

    private final Set<String> mdc = new LinkedHashSet<>();
    private final StringBuilder lines = new StringBuilder();
    private final AtomicBoolean space = new AtomicBoolean(false);

    /**
     * Appends simple text.
     * @param text text to append
     * @return this instance
     */
    public Log text(@NonNull String text) {
        addSpaceIfNeeded(text);

        lines.append(text);
        space.compareAndSet(false, true);
        return this;
    }

    /**
     * Applies the specified format to the
     *  given text and appends the text.
     * @param fmt format
     * @param text text to append
     * @return this instance
     */
    public Log text(@NonNull Fmt fmt, @NonNull String text) {
        addSpaceIfNeeded(text);

        lines.append(fmt.wrap(text));
        space.compareAndSet(false, true);
        return this;
    }

    public Log attribute(@NonNull String name, Object val) {
        String joined = "%s=%s".formatted(name, val);
        return text(joined);
    }

    public Log attribute(@NonNull Fmt fmt, @NonNull String name, Object val) {
        String joined = "%s=%s".formatted(fmt.wrap(name), val);
        return text(joined);
    }

    /**
     * Same as {@link Log#text(String)}, but
     *  encloses the appended text into single
     *  parentheses.
     * @param text text to append
     * @return this instance
     */
    public Log quote(@NonNull String text) {
        addSpaceIfNeeded(text);

        lines.append('\'').append(text).append('\'');
        space.compareAndSet(false, true);
        return this;
    }

    /**
     * Same as {@link Log#text(Fmt, String)}, but
     *  encloses the appended text into single
     *  parentheses.
     * @param text text to append
     * @return this instance
     */
    public Log quote(@NonNull Fmt fmt, @NonNull String text) {
        return quote(fmt.wrap(text));
    }

    /**
     * Appends a colon and provided text.
     * @param text text to append
     * @return this instance
     */
    public Log explain(@NonNull String text) {
        lines.append(": ").append(text);
        return this;
    }

    /**
     * Applies an MDC - diagnostic context;
     *  {@link Log} retains only unique MDCs
     *  and maintains the order of addition.
     * @param ctx tag
     * @return this instance
     */
    public Log mdc(@NonNull String ctx) {
        mdc.add(ctx);
        return this;
    }

    public Log mdc(@NonNull String name, Object val) {
        String joined = "%s=%s".formatted(name, val);
        return mdc(joined);
    }

    public Log mdc(@NonNull Fmt fmt, @NonNull String ctx) {
        mdc.add(fmt.wrap(ctx));
        return this;
    }

    public Log mdc(@NonNull Fmt fmt, @NonNull String name, Object val) {
        String joined = "%s=%s".formatted(fmt.wrap(name), val);
        return mdc(joined);
    }

    /**
     * Appends text formatted as a list item.
     * @param text text to append
     * @return this instance
     */
    public Log listItem(@NonNull String text) {
        lines
                .append('\n')
                .append("  - ")
                .append(text);
        return this;
    }

    public Log listItem(@NonNull Fmt fmt, @NonNull String text) {
        return listItem(fmt.wrap(text));
    }

    /**
     * Exports the collected message as string.
     * @return collected message
     */
    @Override
    public String toString() {
        String log = Fmt.NORM.wrap("");
        String collected = collect();
        return log + collected;
    }

    String collect() {
        String log = "";
        if (!mdc.isEmpty()) {
            log += '[' + String.join(", ", this.mdc) + ']';
            log += ' ';
        }

        log += lines.toString();

        return log;
    }

    void addSpaceIfNeeded(String text) {
        if (NO_SPACE_PREPEND.contains(text.charAt(0))) {
            return;
        }

        if (space.get()) {
            lines.append(' ');
        } else {
            space.set(true);
        }
    }

    private static final Set<Character> NO_SPACE_PREPEND = Set.of(
            ',', ':', ';'
    );

}
