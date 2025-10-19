package li.yansan.clean.usecase.client;

public interface Client<UReq, URes> {
  URes send(UReq request);
}
