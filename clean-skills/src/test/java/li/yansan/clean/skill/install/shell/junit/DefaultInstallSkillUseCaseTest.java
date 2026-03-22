package li.yansan.clean.skill.install.shell.junit;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import li.yansan.clean.skill.install.usecase.DefaultInstallSkillUseCase;
import li.yansan.clean.skill.install.usecase.InstallSkillUseCase;
import li.yansan.clean.usecase.Actor;
import li.yansan.clean.usecase.UseCaseRequest;
import li.yansan.clean.usecase.UseCaseResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class DefaultInstallSkillUseCaseTest {

  @TempDir Path tempDir;

  @Test
  void shouldInstallSkillsInGithubFolderForGithubAgent() throws IOException, URISyntaxException {
    Path projectDir = this.tempDir.resolve("github-root");
    Files.createDirectories(projectDir);
    Files.writeString(projectDir.resolve("pom.xml"), "<modules></modules>");

    InstallSkillUseCase useCase = new DefaultInstallSkillUseCase(projectDir.toFile());
    UseCaseRequest<InstallSkillUseCase.RequestPayload> request =
        new UseCaseRequest<>(
            new Actor("test"), new InstallSkillUseCase.RequestPayload(List.of(), "github"));

    UseCaseResponse<InstallSkillUseCase.ResponseBody> response = useCase.execute(request);

    Assertions.assertFalse(response.body().skillNames().isEmpty(), "Skills should be installed");
    Assertions.assertTrue(
        Files.exists(projectDir.resolve(".github/skills")), ".github/skills should exist");
  }

  @Test
  void shouldInstallSkillsInAgentFolderForOtherAgents() throws IOException, URISyntaxException {
    Path projectDir = this.tempDir.resolve("agent-root");
    Files.createDirectories(projectDir);
    Files.writeString(projectDir.resolve("pom.xml"), "<modules></modules>");

    InstallSkillUseCase useCase = new DefaultInstallSkillUseCase(projectDir.toFile());
    UseCaseRequest<InstallSkillUseCase.RequestPayload> request =
        new UseCaseRequest<>(
            new Actor("test"), new InstallSkillUseCase.RequestPayload(List.of(), "some-agent"));

    UseCaseResponse<InstallSkillUseCase.ResponseBody> response = useCase.execute(request);

    Assertions.assertFalse(response.body().skillNames().isEmpty(), "Skills should be installed");
    Assertions.assertTrue(
        Files.exists(projectDir.resolve(".some-agent/skills")), ".some-agent/skills should exist");
  }

  @Test
  void shouldNotInstallSkillsOnNonRootProject() throws IOException, URISyntaxException {
    Path projectDir = this.tempDir.resolve("module");
    Files.createDirectories(projectDir);
    Files.writeString(projectDir.resolve("pom.xml"), "<project></project>");

    DefaultInstallSkillUseCase useCase = new DefaultInstallSkillUseCase(projectDir.toFile());
    UseCaseRequest<InstallSkillUseCase.RequestPayload> request =
        new UseCaseRequest<>(
            new Actor("test"), new InstallSkillUseCase.RequestPayload(List.of(), "test-agent"));

    UseCaseResponse<InstallSkillUseCase.ResponseBody> response = useCase.execute(request);

    Assertions.assertTrue(response.body().skillNames().isEmpty(), "Skills should not be installed");
  }
}
