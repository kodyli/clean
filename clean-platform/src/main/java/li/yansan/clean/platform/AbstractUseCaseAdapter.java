package li.yansan.clean.platform;

import java.security.Principal;
import li.yansan.clean.commons.validation.Validator;
import li.yansan.clean.usecase.Actor;
import li.yansan.clean.usecase.UseCase;
import li.yansan.clean.usecase.UseCaseRequest;
import li.yansan.clean.usecase.UseCaseResponse;

public abstract class AbstractUseCaseAdapter<TI, TO, UI, UO> {
  public TO execute(Principal user, TI tReq) {
    Actor actor = getActor(user);
    UI uReq = convertRequest(tReq);
    UseCase<UseCaseRequest<UI>, UseCaseResponse<UO>> delegate = getDelegate();
    if (actor == null || uReq == null || delegate == null) {
      throw new IllegalArgumentException();
    }
    UseCaseResponse<UO> uRes = delegate.execute(new UseCaseRequest<>(actor, uReq));
    TO tRes = convertResponse(uRes.body());
    validate(tRes);
    return tRes;
  }

  private void validate(TO tRes) {
    Validator.validate(tRes);
  }

  protected abstract UseCase<UseCaseRequest<UI>, UseCaseResponse<UO>> getDelegate();

  protected abstract Actor getActor(Principal user);

  protected abstract UI convertRequest(TI tReq);

  protected abstract TO convertResponse(UO URes);
}
