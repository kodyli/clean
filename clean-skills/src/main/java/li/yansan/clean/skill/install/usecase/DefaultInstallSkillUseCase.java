package li.yansan.clean.skill.install.usecase;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import li.yansan.clean.skill.install.usecase.repository.Skill;
import li.yansan.clean.skill.install.usecase.repository.SkillRepository;
import li.yansan.clean.usecase.Actor;
import li.yansan.clean.usecase.UseCaseRequest;
import li.yansan.clean.usecase.UseCaseResponse;
import li.yansan.clean.usecase.repository.RepositoryRequest;

public class DefaultInstallSkillUseCase implements InstallSkillUseCase {
  public static String DEFAULT_AGENT = "agent";
  public static String GITHUB_AGENT = "github";
  private final File projectDir;
  private final List<SkillRepository> skillRepositories;

  public DefaultInstallSkillUseCase(File projectDir, SkillRepository skillRepository) {
    this(projectDir, List.of(skillRepository));
  }

  public DefaultInstallSkillUseCase(File projectDir, List<SkillRepository> skillRepositories) {
    this.projectDir = projectDir;
    this.skillRepositories = skillRepositories;
  }

  @Override
  public UseCaseResponse<ResponseBody> execute(UseCaseRequest<RequestPayload> request) {
    var requestPayload = request.payload();

    // ⭐ Only install in the root of a multi-module project
    if (!isRootProject(this.projectDir)) {
      return new UseCaseResponse<>(new ResponseBody(List.of()));
    }

    String agent = !requestPayload.agent().isBlank() ? requestPayload.agent() : DEFAULT_AGENT;
    return new UseCaseResponse<>(
        installSkills(request.actor(), requestPayload.skillNames(), agent));
  }

  private boolean isRootProject(File dir) {
    if (dir == null || !dir.exists() || !dir.isDirectory()) {
      return false;
    }
    File pomFile = new File(dir, "pom.xml");
    if (!pomFile.exists()) {
      return false;
    }
    return hasModules(pomFile);
  }

  private boolean hasModules(File pomFile) {
    try {
      String content = Files.readString(pomFile.toPath());
      return content.contains("<modules>");
    } catch (IOException e) {
      return false;
    }
  }

  protected ResponseBody installSkills(Actor actor, Collection<String> skillNames, String agent) {
    Path targetDir = this.projectDir.toPath().resolve("." + agent + "/skills");
    List<String> installed = new ArrayList<>();

    for (SkillRepository repository : this.skillRepositories) {
      var request = new RepositoryRequest<>(actor, new SkillRepository.RequestPayload(skillNames));
      var response = repository.send(request);

      for (Skill skill : response.body().skills()) {
        try {
          Path targetPath = targetDir.resolve(skill.relativePath());
          Files.createDirectories(targetPath.getParent());
          Files.write(targetPath, skill.content());

          if (!installed.contains(skill.name())) {
            installed.add(skill.name());
          }
        } catch (IOException e) {
          throw new RuntimeException("Failed to install skill: " + skill.name(), e);
        }
      }
    }

    return new ResponseBody(installed);
  }
}
