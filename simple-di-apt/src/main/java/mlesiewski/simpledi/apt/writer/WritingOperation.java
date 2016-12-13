package mlesiewski.simpledi.apt.writer;

import java.io.IOException;
import java.io.Writer;

@FunctionalInterface
interface WritingOperation<T> {
    void apply(Writer writer, T object) throws IOException;
}
