package li.yansan.clean.skill.install.usecase;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import li.yansan.clean.usecase.UseCaseRequest;
import li.yansan.clean.usecase.UseCaseResponse;

public class DefaultInstallSkillUseCase implements InstallSkillUseCase {
  public static String DEFAULT_AGENT = "agent";
  public static String GITHUB_AGENT = "github";
  private final File projectDir;
  private final List<SkillSource> skillSources;

  public DefaultInstallSkillUseCase(File projectDir) {
    this(projectDir, List.of(new ClasspathSkillSource()));
  }

  public DefaultInstallSkillUseCase(File projectDir, List<SkillSource> skillSources) {
    this.projectDir = projectDir;
    this.skillSources = skillSources;
  }

  @Override
  public UseCaseResponse<ResponseBody> execute(UseCaseRequest<RequestPayload> request) {
    var requestPayload = request.payload();

    // ⭐ Only install in the root of a multi-module project
    if (!isRootProject(this.projectDir)) {
      return new UseCaseResponse<>(new ResponseBody(List.of()));
    }

    String agent = !requestPayload.agent().isBlank() ? requestPayload.agent() : DEFAULT_AGENT;
    return new UseCaseResponse<>(installSkills(requestPayload.skillNames(), agent));
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

  protected ResponseBody installSkills(Collection<String> skillNames, String agent) {
    Path target = this.projectDir.toPath().resolve("." + agent + "/skills");
    List<String> allInstalled = new ArrayList<>();

    for (SkillSource source : this.skillSources) {
      allInstalled.addAll(source.install(skillNames, target));
    }

    return new ResponseBody(allInstalled);
  }
}
