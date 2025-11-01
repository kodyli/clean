/* (C)2025 */
package li.yansan.clean.usecase;

public interface UseCase<UPayload, UBody> {
  UseCaseResponse<UBody> execute(UseCaseRequest<UPayload> request);
}
