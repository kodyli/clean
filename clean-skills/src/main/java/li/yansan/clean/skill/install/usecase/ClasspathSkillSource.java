package li.yansan.clean.skill.install.usecase;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/** SkillSource that retrieves skills from the current classpath. */
public class ClasspathSkillSource implements SkillSource {

  @Override
  public Collection<String> install(Collection<String> skillNames, Path target) {
    URL resource = getClass().getClassLoader().getResource("skills");
    if (resource == null) {
      return List.of();
    }

    try {
      if ("jar".equals(resource.getProtocol())) {
        return installFromJar(resource, target, skillNames);
      } else {
        return installFromFilesystem(Paths.get(resource.toURI()), target, skillNames);
      }
    } catch (IOException | URISyntaxException e) {
      throw new RuntimeException("Failed to install skills from classpath", e);
    }
  }

  private Collection<String> installFromFilesystem(
      Path src, Path dest, Collection<String> skillNames) throws IOException {
    List<String> installed = new ArrayList<>();
    Files.walk(src)
        .forEach(
            path -> {
              try {
                Path relative = src.relativize(path);
                if (relative.toString().isEmpty()) {
                  return;
                }

                String skillName = relative.getName(0).toString();
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
    List<String> installed = new ArrayList<>();
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
