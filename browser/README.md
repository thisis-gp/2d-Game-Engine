# Browser version

The Java engine compiled to JavaScript with [TeaVM](https://teavm.org), so the game runs in a web page. This is play mode only: the ImGui editor does not run in a browser.

Live: https://thisis-gp.github.io/thisis_gp/play/

## What changed from the desktop code

- `src/` is a copy of the engine sources with the desktop-only parts swapped out: the window and input (GLFW) are a `requestAnimationFrame` loop and DOM key events, the OpenGL calls (LWJGL) go through a WebGL2 shim in `src/org/lwjgl`, textures come from preloaded images, sound uses Web Audio, and GSON level loading is a small hand-written loader.
- Patched copies of four JOML classes (no `Unsafe`) are in `src/org/joml`.
- `site/` holds the page, the asset loader, the pause overlay (Esc) and the player-sprite swap used by the character maker.

## Gameplay changes made in the browser copy

- The camera follows Mario both ways and stops at the level start, with an invisible wall there.
- Double jump: one extra jump in the air, refilled on landing or after stomping an enemy.
- Esc pauses and resumes, R restarts (the portfolio page wires that up).

## Build

```bash
bash build.sh            # downloads TeaVM and JOML, compiles, writes out/js/engine.js
bash pack.sh ../dist     # copies engine.js, the page, the level and the assets into ../dist
npx http-server ../dist  # then open http://localhost:8080
```

`pack.sh` copies sprites and sounds from `../assets` and the level from `../level.txt`, so run it from this folder inside a checkout of the repo.

## Known gaps

No editor, no mouse input, collider debug outlines are no-ops, reaching the flagpole restarts the level, and Safari may not decode Ogg audio.
