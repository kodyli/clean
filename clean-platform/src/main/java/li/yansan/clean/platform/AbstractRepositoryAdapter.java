package li.yansan.clean.platform;

import li.yansan.clean.commons.validation.Validator;
import li.yansan.clean.usecase.Actor;
import li.yansan.clean.usecase.repository.AbstractRepository;

public abstract class AbstractRepositoryAdapter<TI, TO, UPayload, UBody>
    extends AbstractRepository<UPayload, UBody> {

  @Override
  protected UBody doSend(Actor actor, UPayload payload) {
    TI input = convertRequest(actor, payload);
    validate(input);
    TO output = process(input);
    return convertResponse(output);
  }

  protected abstract TI convertRequest(Actor actor, UPayload payload);

  private void validate(TI input) {
    Validator.validate(input);
  }

  protected abstract TO process(TI input);

  protected abstract UBody convertResponse(TO output);
}
