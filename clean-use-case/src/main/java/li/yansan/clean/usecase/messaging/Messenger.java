package li.yansan.clean.usecase.messaging;

public interface Messenger<UReq, URes> {
  URes send(UReq uReq);
}
