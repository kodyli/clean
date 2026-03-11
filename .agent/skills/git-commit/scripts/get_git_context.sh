#!/bin/bash
# get_git_context.sh
# Gathers branch, status, file sizes, and diff context for the AI agent.

echo "<branch>"
git branch --show-current
echo "</branch>"

echo "<status>"
git status --short
echo "</status>"

# Audit: Check for files larger than 50MB in the current diff
# We look at staged files if there are any, otherwise modified files
if ! git diff --cached --quiet; then
    # Staged changes exist
    files=$(git diff --cached --name-only)
else
    # No staged changes, look at working tree
    files=$(git diff --name-only)
fi

echo "<file-size-audit>"
if [ -n "$files" ]; then
    # Check each file size
    # Note: This is a simple check, handling spaces in filenames might require find -print0 or similar if needed, 
    # but for known project structures this simple loop is usually sufficient for a helper script.
    # formatting output for xml-like structure
    while IFS= read -r file; do
        if [ -f "$file" ]; then
            size=$(stat -f%z "$file" 2>/dev/null || stat -c%s "$file" 2>/dev/null)
            # 50MB = 52428800 bytes
            if [ "$size" -gt 52428800 ]; then
                echo "WARNING: $file is $(($size / 1024 / 1024))MB (exceeds 50MB limit)"
            fi
        fi
    done <<< "$files"
else
    echo "No files modified."
fi
echo "</file-size-audit>"

echo "<diff>"
# Prefer staged changes, fallback to all changes if nothing staged
if ! git diff --cached --quiet; then
    git diff --cached
else
    git diff
fi
echo "</diff>"
