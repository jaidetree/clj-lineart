#!/usr/bin/env bash
fswatch -o . \
    | xargs -n1 -I{} rsync \
    -r \
    -a \
    -v \
    --exclude "README.org" \
    --exclude "/scripts" \
    --exclude "/test" \
    --exclude "/doc" \
    . "$REMOTE_PROJECT_PATH"
