package li.yansan.clean.skill.install.usecase;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import li.yansan.clean.usecase.UseCaseRequest;
import li.yansan.clean.usecase.UseCaseResponse;

public class DefaultInstallSkillUseCase implements InstallSkillUseCase {
  public static String DEFAULT_AGENT = "agent";
  public static String GITHUB_AGENT = "github";
  private final File projectDir;

  public DefaultInstallSkillUseCase(File projectDir) {
    this.projectDir = projectDir;
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
    // Simple heuristic for root project in this context:
    // It contains a pom.xml and either sub-modules or a .git directory.
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
    URL resource = getClass().getClassLoader().getResource("skills");
    if (resource == null) {
      return new ResponseBody(List.of());
    }

    Collection<String> installedSkills;
    try {
      if ("jar".equals(resource.getProtocol())) {
        installedSkills = installFromJar(resource, target, skillNames);
      } else {
        installedSkills = installFromFilesystem(Paths.get(resource.toURI()), target, skillNames);
      }
    } catch (IOException | URISyntaxException e) {
      throw new RuntimeException("Failed to install skills", e);
    }
    return new ResponseBody(installedSkills);
  }

  private Collection<String> installFromFilesystem(
      Path src, Path dest, Collection<String> skillNames) throws IOException {
    List<String> installed = new java.util.ArrayList<>();
    Files.walk(src)
        .forEach(
            path -> {
              try {
                Path relative = src.relativize(path);
                if (relative.toString().isEmpty()) {
                  return;
                }

                String skillName = relative.getName(0).toString();
                // If skillNames is not empty, only copy those skills
                if (!skillNames.isEmpty() && !skillNames.contains(skillName)) {
                  return;
                }

                Path target = dest.resolve(relative);
                if (Files.isDirectory(path)) {
                  Files.createDirectories(target);
                } else {
                  Files.createDirectories(target.getParent());
                  Files.copy(path, target, StandardCopyOption.REPLACE_EXISTING);
                  if (!installed.contains(skillName)) {
                    installed.add(skillName);
                  }
                }
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            });
    return installed;
  }

  private Collection<String> installFromJar(URL resource, Path dest, Collection<String> skillNames)
      throws IOException {
    List<String> installed = new java.util.ArrayList<>();
    String jarPath = resource.getPath().substring(5, resource.getPath().indexOf("!"));
    try (JarFile jar = new JarFile(jarPath)) {
      Enumeration<JarEntry> entries = jar.entries();

      while (entries.hasMoreElements()) {
        JarEntry entry = entries.nextElement();
        if (!entry.getName().startsWith("skills/") || entry.getName().equals("skills/")) {
          continue;
        }

        String relativeName = entry.getName().substring("skills/".length());
        String skillName = relativeName.split("/")[0];

        // If skillNames is not empty, only copy those skills
        if (!skillNames.isEmpty() && !skillNames.contains(skillName)) {
          continue;
        }

        Path target = dest.resolve(relativeName);
        if (entry.isDirectory()) {
          Files.createDirectories(target);
        } else {
          Files.createDirectories(target.getParent());
          try (InputStream in = jar.getInputStream(entry)) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            if (!installed.contains(skillName)) {
              installed.add(skillName);
            }
          }
        }
      }
    }
    return installed;
  }
}
