package li.yansan.clean.skill.install.usecase.repository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import li.yansan.clean.usecase.repository.RepositoryRequest;
import li.yansan.clean.usecase.repository.RepositoryResponse;

public class FileSkillRepository implements SkillRepository {

  @Override
  public RepositoryResponse<ResponseBody> send(RepositoryRequest<RequestPayload> request) {
    Collection<String> requestedNames = request.payload().requestedNames();
    URL resource = getClass().getClassLoader().getResource("skills");
    if (resource == null || !"file".equals(resource.getProtocol())) {
      return new RepositoryResponse<>(new ResponseBody(List.of()));
    }

    try {
      return new RepositoryResponse<>(
          new ResponseBody(readFromFilesystem(Paths.get(resource.toURI()), requestedNames)));
    } catch (IOException | URISyntaxException e) {
      throw new RuntimeException("Failed to read skills from filesystem", e);
    }
  }

  private Collection<Skill> readFromFilesystem(Path src, Collection<String> skillNames)
      throws IOException {
    List<Skill> skills = new ArrayList<>();
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

                if (!Files.isDirectory(path)) {
                  skills.add(new Skill(skillName, relative.toString(), Files.readAllBytes(path)));
                }
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            });
    return skills;
  }
}
