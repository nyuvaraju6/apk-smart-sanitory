# SmartSan Cleaner Mobile App

This project is configured with GitHub Actions for automated CI/CD. The workflows handle testing, building, and creating releases for the Flutter Android application.

## GitHub Actions Workflows

We have two main workflows configured:

1. **Flutter Build (`.github/workflows/flutter-build.yml`)**: Runs on every push or pull request to the `main` or `master` branches. It runs code analysis, tests, and builds the Debug APK, Release APK, and Release AAB. These are then uploaded as build artifacts.
2. **Flutter Release (`.github/workflows/release.yml`)**: Runs whenever a version tag (e.g., `v1.0.0`) is pushed to the repository. It builds the Release APK and AAB, creates a GitHub Release, and automatically attaches the built binaries to the release.

---

### 1. How to Enable GitHub Actions

GitHub Actions are enabled by default for most repositories. To ensure they are running:
1. Go to your repository on GitHub.
2. Click the **Actions** tab at the top.
3. If prompted, click **I understand my workflows, go ahead and enable them**.

### 2. How to Trigger Builds (Pushing Code)

The Build workflow is triggered automatically on every push to the main branch. 
```bash
git add .
git commit -m "Update dashboard UI"
git push origin main
```
You can monitor the progress of the build in the **Actions** tab of your repository.

### 3. How to Create a Release (Pushing Version Tags)

To create a new GitHub Release with the APK and AAB attached, you need to tag a commit with a version number starting with `v` (e.g., `v1.0.0`) and push the tag.

```bash
# Tag the current commit
git tag v1.0.0

# Push the tag to GitHub
git push origin v1.0.0
```
This will trigger the **Flutter Release** workflow. Once complete, a new release will appear in the **Releases** section on the right side of your repository homepage.

### 4. How to Download APKs from GitHub Actions

If you want to download an APK from a standard code push (not a tagged release):
1. Go to the **Actions** tab.
2. Click on the latest workflow run for **Flutter Build**.
3. Scroll down to the **Artifacts** section at the bottom of the summary page.
4. Click on `app-debug-apk` or `app-release-apk` to download a zip file containing the APK.

### 5. How to Download APKs from GitHub Releases

For official versioned builds:
1. Go to your repository homepage.
2. Look for the **Releases** section on the right sidebar and click on the latest release (e.g., `v1.0.0`).
3. Under the **Assets** dropdown at the bottom of the release notes, you will find `app-release.apk` and `app-release.aab`. Click to download them directly.

### 6. Building Locally

If you need to build the APK on your local machine, ensure you have Flutter and Java installed, then run:

```bash
# Install dependencies
flutter pub get

# Build Debug APK
flutter build apk --debug

# Build Release APK
flutter build apk --release

# Build Release App Bundle (for Play Store)
flutter build appbundle --release
```
The output files will be located in:
* APK: `build/app/outputs/flutter-apk/`
* AAB: `build/app/outputs/bundle/release/`
