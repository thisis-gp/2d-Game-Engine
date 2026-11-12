#!/bin/bash
# usage: pack.sh <outdir> -> static bundle (engine.js, loader, html, assets, minified level)
set -e
cd "$(dirname "$0")"
R=..        # original repo copy (assets, level.txt)
O="$1"; mkdir -p "$O/assets/images/spritesheets" "$O/assets/shaders" "$O/assets/sounds"
cp out/js/engine.js site/index.html site/engine-loader.js site/player-sprite.js site/pause.js "$O/"
node -e 'const fs=require("fs");fs.writeFileSync(process.argv[2],JSON.stringify(JSON.parse(fs.readFileSync(process.argv[1],"utf8"))))' $R/level.txt "$O/level.txt"
for f in bigSpritesheet items pipes spritesheet turtle spritesheets/decorationsAndBlocks; do cp "$R/assets/images/$f.png" "$O/assets/images/$f.png"; done
cp $R/assets/shaders/default.glsl "$O/assets/shaders/"
for s in break_block bump coin flagpole gameover invincible jump-small kick main-theme-overworld mario_die pipe powerup powerup_appears stage_clear stomp; do cp "$R/assets/sounds/$s.ogg" "$O/assets/sounds/"; done
