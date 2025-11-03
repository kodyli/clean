package li.yansan.clean.platform;

import java.security.Principal;
import li.yansan.clean.commons.validation.Validator;
import li.yansan.clean.usecase.Actor;
import li.yansan.clean.usecase.UseCase;
import li.yansan.clean.usecase.UseCaseRequest;
import li.yansan.clean.usecase.UseCaseResponse;

public abstract class AbstractUseCaseAdapter<TI, TO, UPayload, UBody> {
  public TO execute(Principal user, TI input) {
    Actor actor = convertToActor(user);
    UPayload payload = convertToPayload(input);
    UseCase<UPayload, UBody> delegate = getDelegate();
    if (actor == null || payload == null || delegate == null) {
      throw new IllegalArgumentException();
    }
    UseCaseResponse<UBody> uRes = delegate.execute(new UseCaseRequest<>(actor, payload));
    if (uRes == null) {
      throw new IllegalArgumentException("UseCaseResponse can not be null.");
    }
    TO output = convertBody(uRes.body());
    validate(output);
    return output;
  }

  protected void validate(TO output) {
    Validator.validate(output);
  }

  protected abstract UseCase<UPayload, UBody> getDelegate();

  protected abstract Actor convertToActor(Principal principal);

  protected abstract UPayload convertToPayload(TI input);

  protected abstract TO convertBody(UBody body);
}
