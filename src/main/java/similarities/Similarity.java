package similarities;


/**
 * An abstract class for similarity metrics.
 *
 * @author vasgat
 */
public abstract class Similarity<T> {

    /**
     * calculates the similarity between two given objects.
     *
     * @param o1 input object 1
     * @param o2 input object 2
     * @return returns a value between 0 and 1
     */
    public abstract double calculate(T o1, T o2);

    /**
     * returns a small description of the metric
     *
     * @return the small description
     */
    public abstract String info();
}
