package li.yansan.clean.usecase.handler;

import li.yansan.clean.usecase.UseCase;
import li.yansan.clean.usecase.UseCaseRequest;

public interface Handler<UPayload, UBody> extends UseCase<UPayload, UBody> {

	boolean support(UseCaseRequest<UPayload> request);

}
