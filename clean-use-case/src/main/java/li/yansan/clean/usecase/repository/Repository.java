package li.yansan.clean.usecase.repository;

public interface Repository<UReq, URes> {
  URes send(UReq uReq);
}
