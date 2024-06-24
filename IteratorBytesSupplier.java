
package ai.djl.inference.streaming;

import ai.djl.ndarray.BytesSupplier;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;

/**
 * An {@link IteratorBytesSupplier} is a streaming {@link BytesSupplier} suitable for synchronous
 * usage.
 */
public class IteratorBytesSupplier implements BytesSupplier, Iterator<byte[]> {

    private Iterator<BytesSupplier> sources;

    /**
     * Constructs an {@link IteratorBytesSupplier}.
     *
     * @param sources the source suppliers
     */
    public IteratorBytesSupplier(Iterator<BytesSupplier> sources) {
        this.sources = sources;
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasNext() {
        return sources.hasNext();
    }

    /** {@inheritDoc} */
    @Override
    public byte[] next() {
        return sources.next().getAsBytes();
    }

    /** {@inheritDoc} */
    @Override
    public ByteBuffer toByteBuffer() {
        return ByteBuffer.wrap(getAsBytes());
    }

    /** {@inheritDoc} */
    @Override
    public byte[] getAsBytes() {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            while (hasNext()) {
                bos.write(next());
            }
            return bos.toByteArray();
        } catch (IOException e) {
            throw new AssertionError("Failed to read BytesSupplier", e);
        }
    }
}
