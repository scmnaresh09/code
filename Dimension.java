
public class Dimension {

    @SerializedName("Name")
    private String name;

    @SerializedName("Value")
    private String value;

    /** Constructs a new {@code Dimension} instance. */
    public Dimension() {}

    /**
     * Constructs a new {@code Dimension} instance.
     *
     * @param name the dimension name
     * @param value the dimension value
     */
    public Dimension(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Returns the dimension name.
     *
     * @return the dimension name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the dimension value.
     *
     * @return the dimension value
     */
    public String getValue() {
        return value;
    }
}
