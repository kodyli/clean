package li.yansan.clean.skill.install.shell.plugin;

import java.io.File;
import java.util.List;
import li.yansan.clean.skill.install.usecase.DefaultInstallSkillUseCase;
import li.yansan.clean.skill.install.usecase.InstallSkillUseCase;
import li.yansan.clean.usecase.Actor;
import li.yansan.clean.usecase.UseCaseRequest;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "install")
public class InstallSkillMojo extends AbstractMojo {

  @Parameter(defaultValue = "${project}", readonly = true)
  private MavenProject project;

  @Parameter(defaultValue = "${project.basedir}", readonly = true)
  private File projectDir;

  @Parameter(property = "agent", defaultValue = "agent")
  private String agent;

  @Parameter(property = "skills")
  private List<String> skillNames;

  @Override
  public void execute() throws MojoExecutionException {
    if (!this.project.isExecutionRoot()) {
      getLog().info("Skipping non-execution root project.");
      return;
    }

    getLog().info("Installing skills...");

    try {
      List<String> names = this.skillNames != null ? this.skillNames : List.of();
      InstallSkillUseCase useCase = new DefaultInstallSkillUseCase(this.projectDir);
      var request =
          new UseCaseRequest<>(
              new Actor("system"), new InstallSkillUseCase.RequestPayload(names, this.agent));
      var response = useCase.execute(request);

      if (response.body().skillNames().isEmpty()) {
        getLog().info("No skills installed (either not root or no skills found).");
      } else {
        getLog().info("Skills installed successfully: " + response.body().skillNames());
      }
    } catch (Exception e) {
      throw new MojoExecutionException("Failed to install skills", e);
    }
  }
}
