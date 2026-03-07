package li.yansan.clean.application;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import li.yansan.clean.commons.convert.Converter;
import li.yansan.clean.commons.convert.Convertible;
import li.yansan.clean.commons.validation.Validator;

/**
 * Represents a standardized request to a Use Case.
 *
 * <p>
 * This record encapsulates the necessary information to execute a business use case,
 * specifically the {@link Actor} initiating the request and the business payload.
 *
 * <p>
 * <b>Responsibilities:</b>
 *
 * <ul>
 * <li><b>Encapsulation:</b> Holds the {@code actor} and {@code payload} required for the
 * use case.
 * <li><b>Validation:</b> Enforces input validation upon instantiation. The
 * {@link #validate()} method is called in the constructor to ensure that both
 * {@code actor} and {@code payload} are not null and satisfy any constraints defined on
 * them.
 * <li><b>Transformation:</b> Provides flexible constructors to facilitate the conversion
 * of external data types into the required domain objects. It supports
 * {@link Convertible} interfaces and {@link Converter} functional interfaces to
 * streamline this process.
 * </ul>
 *
 * @param <UPayload> the type of the payload carrying the business data for the use case
 * @param actor the actor initiating the use case; must not be null
 * @param payload the business data payload; must not be null
 */
public record UseCaseRequest<UPayload>(@Valid @NotNull Actor actor, @Valid @NotNull UPayload payload) {
	public UseCaseRequest(Actor actor, UPayload payload) {
		this.actor = actor;
		this.payload = payload;
		validate();
	}

	public UseCaseRequest(Actor actor, Convertible<UPayload> payload) {
		this(actor, payload.convert());
	}

	public <T> UseCaseRequest(Converter<T, Actor> converter, T user, Convertible<UPayload> payload) {
		this(converter.convert(user), payload.convert());
	}

	public <T> UseCaseRequest(Converter<T, Actor> converter, T user, UPayload payload) {
		this(converter.convert(user), payload);
	}

	private void validate() {
		Validator.validate(this);
	}
}
