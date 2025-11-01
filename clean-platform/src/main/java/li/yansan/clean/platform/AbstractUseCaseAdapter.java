package li.yansan.clean.platform;

import java.security.Principal;
import li.yansan.clean.commons.validation.Validator;
import li.yansan.clean.usecase.Actor;
import li.yansan.clean.usecase.UseCase;
import li.yansan.clean.usecase.UseCaseRequest;
import li.yansan.clean.usecase.UseCaseResponse;

public abstract class AbstractUseCaseAdapter<TI, TO, UPayload, UBody> {
  public TO execute(Principal user, TI input) {
    Actor actor = convertActor(user);
    UPayload payload = convertRequest(input);
    UseCase<UPayload, UBody> delegate = getDelegate();
    if (actor == null || payload == null || delegate == null) {
      throw new IllegalArgumentException();
    }
    UseCaseResponse<UBody> uRes = delegate.execute(new UseCaseRequest<>(actor, payload));
    TO output = convertResponse(uRes.body());
    validate(output);
    return output;
  }

  private void validate(TO output) {
    Validator.validate(output);
  }

  protected abstract UseCase<UPayload, UBody> getDelegate();

  protected abstract Actor convertActor(Principal principal);

  protected abstract UPayload convertRequest(TI input);

  protected abstract TO convertResponse(UBody body);
}
