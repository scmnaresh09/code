
public interface EngineProvider {

    /**
     * Returns the name of the {@link Engine}.
     *
     * @return the name of {@link Engine}
     */
    String getEngineName();

    /**
     * Returns the rank of the {@link Engine}.
     *
     * @return the rank of {@link Engine}
     */
    int getEngineRank();

    /**
     * Returns the instance of the {@link Engine} class EngineProvider should bind to.
     *
     * @return the instance of {@link Engine}
     */
    Engine getEngine();
}
