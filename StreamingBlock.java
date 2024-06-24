
package ai.djl.inference.streaming;

import ai.djl.ndarray.NDList;
import ai.djl.nn.Block;
import ai.djl.training.ParameterStore;
import ai.djl.util.PairList;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * A {@link Block} possessing the additional streaming forward capabilities used by {@link
 * ai.djl.inference.Predictor#streamingPredict(Object)}.
 */
public interface StreamingBlock extends Block {

    /**
     * Applies the operating function of the block once, but returns the result in chunks. This
     * method should only be called on blocks that are initialized.
     *
     * @param parameterStore the parameter store
     * @param inputs the input NDList
     * @param training true for a training forward pass (turn on dropout and layerNorm)
     * @return the output of the forward pass
     */
    default Stream<NDList> forwardStream(
            ParameterStore parameterStore, NDList inputs, boolean training) {
        return forwardStream(parameterStore, inputs, training, null);
    }

    /**
     * Applies the operating function of the block once, but returns the result in chunks. This
     * method should only be called on blocks that are initialized.
     *
     * @param parameterStore the parameter store
     * @param inputs the input NDList
     * @param training true for a training forward pass (turn on dropout and layerNorm)
     * @param params optional parameters
     * @return the output of the forward pass
     */
    default Stream<NDList> forwardStream(
            ParameterStore parameterStore,
            NDList inputs,
            boolean training,
            PairList<String, Object> params) {
        Iterator<NDList> itr = forwardStreamIter(parameterStore, inputs, training, params);
        Spliterator<NDList> spitr = Spliterators.spliteratorUnknownSize(itr, Spliterator.NONNULL);
        return StreamSupport.stream(spitr, false);
    }

    /**
     * Applies the operating function of the block once, but returns the result in chunks. This
     * method should only be called on blocks that are initialized.
     *
     * @param parameterStore the parameter store
     * @param inputs the input NDList
     * @param training true for a training forward pass (turn on dropout and layerNorm)
     * @param params optional parameters
     * @return the output of the forward pass
     */
    Iterator<NDList> forwardStreamIter(
            ParameterStore parameterStore,
            NDList inputs,
            boolean training,
            PairList<String, Object> params);
}
