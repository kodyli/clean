package li.yansan.clean.platform;

import li.yansan.clean.commons.validation.Validator;
import li.yansan.clean.usecase.Actor;
import li.yansan.clean.usecase.messaging.AbstractMessenger;

public abstract class AbstractMessengerAdapter<TI, TO, UI, UO> extends AbstractMessenger<UI, UO> {

  @Override
  public UO doSend(Actor actor, UI message) {
    TI tReq = convertRequest(actor, message);
    validate(tReq);
    TO tRes = process(tReq);
    return convertResponse(tRes);
  }

  protected abstract TI convertRequest(Actor actor, UI message);

  private void validate(TI tReq) {
    Validator.validate(tReq);
  }

  protected abstract TO process(TI tReq);

  protected abstract UO convertResponse(TO tRes);
}
