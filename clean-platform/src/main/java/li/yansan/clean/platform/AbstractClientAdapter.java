package li.yansan.clean.platform;

import li.yansan.clean.commons.validation.Validator;
import li.yansan.clean.usecase.Actor;
import li.yansan.clean.usecase.client.AbstractClient;

public abstract class AbstractClientAdapter<TI, TO, UI, UO> extends AbstractClient<UI, UO> {

  @Override
  protected UO doSend(Actor actor, UI input) {
    TI tReq = convertRequest(actor, input);
    validate(tReq);
    TO tRes = process(tReq);
    return convertResponse(tRes);
  }

  protected abstract TI convertRequest(Actor actor, UI input);

  private void validate(TI tReq) {
    Validator.validate(tReq);
  }

  protected abstract TO process(TI tReq);

  protected abstract UO convertResponse(TO tRes);
}
