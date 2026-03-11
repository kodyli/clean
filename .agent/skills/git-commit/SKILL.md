---
name: git-commit
description: Architect and generate professional commit messages from code diffs. Use when users ask to commit changes or need a commit message.
---

# Git Commit Auditor & Message Generator

Enable the assistant to perform a deep-dive analysis of code diffs, audit for security risks (secrets/PII) and performance issues (large files), and generate high-quality, professional commit messages following the Conventional Commits standard.

## Workflow

### 1. Analysis Phase (`<thinkthrough>`)

First, execute the context gathering script:
```bash
./.agent/skills/git-commit/scripts/get_git_context.sh
```

Analyze the output (provided in `<branch>`, `<status>`, `<file-size-audit>`, and `<diff>` tags) by evaluating:
* **Context:** The overall purpose and motivation of the changes.
* **Security Scan:** Check for API keys, passwords, auth tokens, or personal data.
* **File Size Audit:** Identify any files exceeding **50MB**.
* **Modification Detail:** Note specific changes to functions, classes, and variables.
* **Architectural Rationale:** Consider why specific implementation choices were made.
* **Future State:** Identify limitations, technical debt, or `TODO` items.

### 2. Validation Phase (Guardrails)

Before generating a commit message, verify the diff against these critical safety checks:

#### A. Sensitive Information
* **Trigger:** If the diff contains secrets (keys, tokens, PII).
* **Action:** Do NOT include the secret in the output.
* **Output Format:**
  ```xml
  <sensitive-info-warning>
  [Identify the file and nature of the exposure. Suggest removing the data and re-committing.]
  </sensitive-info-warning>
  ```

#### B. Large Files
* **Trigger:** If any file in the diff is >50MB.
* **Action:** Do NOT proceed with a standard commit message.
* **Output Format:**
  ```xml
  <large-files-warning>
  [Identify the oversized files. Recommend using Git LFS and provide the link: https://git-lfs.github.com]
  </large-files-warning>
  ```

### 3. Generation Phase (`<commit-message>`)

If validation passes, construct the final message using the following style guide:

#### Subject Line
* **Format:** Use Conventional Commits (e.g., `feat:`, `fix:`, `chore:`, `refactor:`, `docs:`).
* **Length:** 50 characters or less.
* **Mood:** Imperative (e.g., "add", "fix", "update").
* **Punctuation:** Do not end with a period.

#### Body
* **Structure:** One blank line between the subject and the body.
* **Formatting:** Wrap text at **72 characters**.
* **Content:**
  * Explain the high-level motivation (the "Why").
  * Summarize the changes (the "What").
  * Explain the rationale behind significant decisions.
* **Tone:** Professional, objective, and positive. Avoid humor or slang.

## Technical Constraints

* **Equations:** Use LaTeX for complex math/science ($inline$ or $$display$$).
* **Formatting:** Use clear headers and bullet points; avoid walls of text.
* **Process:** Always use a internal `<scratchpad>` to draft the one-liner and context (5-15 sentences) before providing the final tags.

## Output Template

```xml
<thinkthrough>
[Detailed analysis of the diff]
</thinkthrough>

<commit-message>
[type]: [subject line]

[body text wrapped at 72 chars]
</commit-message>
```