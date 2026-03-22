package li.yansan.clean.skill.install.usecase;

import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import li.yansan.clean.usecase.UseCase;

public interface InstallSkillUseCase
    extends UseCase<InstallSkillUseCase.RequestPayload, InstallSkillUseCase.ResponseBody> {
  record RequestPayload(@NotNull Collection<String> skillNames, @NotNull String agent) {}

  record ResponseBody(@NotNull Collection<String> skillNames) {}
}
