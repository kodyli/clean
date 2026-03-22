package li.yansan.clean.skill.install.usecase.repository;

import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import li.yansan.clean.usecase.repository.Repository;

public interface SkillRepository
    extends Repository<SkillRepository.RequestPayload, SkillRepository.ResponseBody> {

  record RequestPayload(@NotNull Collection<String> requestedNames) {}

  record ResponseBody(@NotNull Collection<Skill> skills) {}
}
