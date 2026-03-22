package li.yansan.clean.skill.install.usecase;

import java.nio.file.Path;
import java.util.Collection;

/** Strategy for retrieving and installing AI skills from a specific source. */
public interface SkillSource {
  /**
   * Installs selected skills to the target directory.
   *
   * @param skillNames the names of the skills to install (if empty, install all)
   * @param target the destination directory
   * @return a collection of the names of the skills that were successfully installed
   */
  Collection<String> install(Collection<String> skillNames, Path target);
}
