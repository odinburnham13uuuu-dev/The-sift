# The Sift (Fabric, Minecraft 26.3)
Build: copy gradle/wrapper + gradlew(.bat) from the official fabric-example-mod, then `./gradlew build` (JDK 25, Gradle 9.6).
Jar: build/libs/thesift-0.1.0.jar -> mods folder (needs Fabric API).

Open the portal (survival/adventure recommended; creative also works):
 1. Build a 4x5 Carapace Bone frame, but put NOTE BLOCKS in the four corners.
 2. Right-click each note block 3 times (note 3).
 3. Left-click ("play") a note block -> the portal lights. Stand in it ~2s.
Creative tab: "The Sift".

Compatibility notes
- Realistic Water (sskaffy, "Ultra Realistic Water" on CurseForge, Fabric 26.3): independent mod; set Graphics API to Vulkan for it.
  The Sift uses vanilla water/rendering, so the two should coexist.

No local setup? Push this folder to a GitHub repo. The included workflow
(.github/workflows/build.yml) builds the jar; download it from Actions > latest run > Artifacts.
Use the non "-sources" jar.
