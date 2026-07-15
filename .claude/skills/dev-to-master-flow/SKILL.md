---
name: dev-to-master-flow
description: This skill should be used when the user asks to "ship this to master", "create a branch and PR it to dev then master", "run the dev-to-master flow", "promote dev to master", or wants the full branch/PR/merge cycle for this repo (feature branch → PR into dev → merge → PR dev into master → merge → back on dev). Use whenever the user describes this multi-step branch/PR/merge sequence even without naming the skill.
---

# Dev-to-Master Flow

Ship a change through this repo's two-stage flow: a feature branch merges into `dev` via PR,
then `dev` merges into `master` via a second PR. Both merges use merge commits (not squash or
rebase) to match this repo's existing history style.

## Prerequisites

- `gh` must be authenticated (`gh auth status`). If not, stop and tell the user to run `gh auth login`.
- Confirm with the user: the sub-branch name, and whether any currently staged/uncommitted
  changes on `dev` should be swept into the new branch (this is the common case — work often
  starts as uncommitted changes on `dev` before being split into a proper branch).

## Steps

1. Check state before doing anything: `git status`, `git branch -a`, `git remote -v`. Confirm
   there are no unrelated in-progress changes that would get swept into the new branch by accident.

2. Create the sub-branch from `dev`:
   ```
   git checkout -b <branch-name>
   ```
   Running this while changes are staged/unstaged carries them onto the new branch — this is
   expected when the goal is to branch off in-progress work.

3. Commit with a message describing the actual change (not a generic placeholder), then push:
   ```
   git commit -m "<message>"
   git push -u origin <branch-name>
   ```

4. Open the PR into `dev`:
   ```
   gh pr create --base dev --head <branch-name> --title "<title>" --body "<summary + test plan>"
   ```

5. Merge with a merge commit:
   ```
   gh pr merge <PR#> --merge --delete-branch
   ```
   **Known failure mode:** if an untracked file in the working tree (e.g. `.idea/gradle.xml`,
   `.idea/firebender/chat_history.db*`) collides with a tracked file on the branch being checked
   out, `gh`'s post-merge branch switch aborts with "would be overwritten by checkout". The PR
   is still merged on GitHub at this point — verify with
   `gh pr view <PR#> --json state,mergedAt` before treating it as a failure. Resolve by removing
   or moving aside the untracked file, then proceed to step 6.

6. Sync local `dev`:
   ```
   git checkout dev
   git pull origin dev
   ```

7. Confirm what will move to `master`, then open the second PR:
   ```
   git log master..dev --oneline
   gh pr create --base master --head dev --title "<title>" --body "<summary + test plan>"
   ```

8. Merge with a merge commit (no `--delete-branch` here — `dev` is a permanent branch):
   ```
   gh pr merge <PR#> --merge
   ```

9. Finish on `dev`:
   ```
   git checkout dev
   git pull origin dev
   git status
   ```
   Confirm the working tree is clean before reporting completion.

## Notes

- Never use `--squash` or `--rebase` unless the user explicitly asks for a different merge
  method — this repo's history uses merge commits (visible via `git log --oneline`).
- Do not pass `--delete-branch` when merging the `dev` → `master` PR; only short-lived feature
  branches get deleted.
- If `git log master..dev --oneline` is empty after step 6, there is nothing to promote — say so
  instead of opening an empty PR.