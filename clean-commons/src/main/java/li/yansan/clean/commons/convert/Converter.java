package li.yansan.clean.commons.convert;

public interface Converter<S, T> {

	T convert(S source);

}
