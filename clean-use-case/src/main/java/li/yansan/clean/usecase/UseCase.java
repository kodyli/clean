/* (C)2025 */
package li.yansan.clean.usecase;

public interface UseCase<TReq, TRes> {
  TRes execute(TReq request);
}
