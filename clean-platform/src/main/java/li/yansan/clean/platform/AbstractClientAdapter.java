package li.yansan.clean.platform;

import li.yansan.clean.commons.validation.Validator;
import li.yansan.clean.usecase.Actor;
import li.yansan.clean.usecase.client.AbstractClient;

public abstract class AbstractClientAdapter<TI, TO, UPayload, UBody>
    extends AbstractClient<UPayload, UBody> {

  @Override
  protected UBody doSend(Actor actor, UPayload payload) {
    TI input = convertPayload(actor, payload);
    validate(input);
    TO output = process(input);
    return convertToBody(output);
  }

  protected abstract TI convertPayload(Actor actor, UPayload payload);

  private void validate(TI input) {
    Validator.validate(input);
  }

  protected abstract TO process(TI input);

  protected abstract UBody convertToBody(TO output);
}
