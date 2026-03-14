package li.yansan.clean.commons.convert;

/**
 * Simple converter interface.
 *
 * @param <T> input type
 * @param <U> output type
 */
public interface Converter<T, U> {

  /**
   * Converts T to U.
   *
   * @param data input
   * @return output
   */
  U convert(T data);
}
