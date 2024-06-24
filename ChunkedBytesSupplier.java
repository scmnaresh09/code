
package ai.djl.inference.streaming;

import ai.djl.ndarray.BytesSupplier;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/** A {link BytesSupplier} that supports chunked reading. */
public class ChunkedBytesSupplier implements BytesSupplier {

    private LinkedBlockingQueue<BytesSupplier> queue;
    private AtomicBoolean completed;

    /** Constructs a new {code ChunkedBytesSupplier} instance. */
    public ChunkedBytesSupplier() {
        queue = new LinkedBlockingQueue<>();
        completed = new AtomicBoolean();
    }

    /**
     * Appends content to the {@code BytesSupplier}.
     *
     * @param data bytes to append
     * @param lastChunk true if this is the last chunk
     */
    public void appendContent(byte[] data, boolean lastChunk) {
        appendContent(BytesSupplier.wrap(data), lastChunk);
    }

    /**
     * Appends content to the {@code BytesSupplier}.
     *
     * @param bytesSupplier BytesSupplier to append
     * @param lastChunk true if this is the last chunk
     */
    public void appendContent(BytesSupplier bytesSupplier, boolean lastChunk) {
        if (lastChunk) {
            completed.set(true);
        }
        queue.offer(bytesSupplier);
    }

    /**
     * Returns {@code true} if has more chunk.
     *
     * @return {@code true} if has more chunk
     */
    public boolean hasNext() {
        return !completed.get() || !queue.isEmpty();
    }

    /**
     * Returns the next chunk.
     *
     * @param timeout the maximum time to wait
     * @param unit the time unit of the timeout argument
     * @return the next chunk
     * @throws InterruptedException if the thread is interrupted
     */
    public BytesSupplier next(long timeout, TimeUnit unit) throws InterruptedException {
        BytesSupplier data = queue.poll(timeout, unit);
        if (data == null) {
            throw new IllegalStateException("Read chunk timeout.");
        }
        return data;
    }

    /**
     * Returns the next chunk.
     *
     * @param timeout the maximum time to wait
     * @param unit the time unit of the timeout argument
     * @return the next chunk
     * @throws InterruptedException if the thread is interrupted
     */
    public byte[] nextChunk(long timeout, TimeUnit unit) throws InterruptedException {
        return next(timeout, unit).getAsBytes();
    }

    /**
     * Retrieves and removes the head of chunk or returns {@code null} if data is not available.
     *
     * @return the head of chunk or returns {@code null} if data is not available
     */
    public BytesSupplier poll() {
        return queue.poll();
    }

    /**
     * Retrieves and removes the head of chunk or returns {@code null} if data is not available.
     *
     * @return the head of chunk or returns {@code null} if data is not available
     */
    public byte[] pollChunk() {
        BytesSupplier data = poll();
        return data == null ? null : data.getAsBytes();
    }

    /** {@inheritDoc} */
    @Override
    public byte[] getAsBytes() {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            while (hasNext()) {
                bos.write(nextChunk(1, TimeUnit.MINUTES));
            }
            return bos.toByteArray();
        } catch (IOException | InterruptedException e) {
            throw new AssertionError("Failed to read BytesSupplier", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public ByteBuffer toByteBuffer() {
        return ByteBuffer.wrap(getAsBytes());
    }
}
