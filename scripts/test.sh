#!/usr/bin/env bash
#
fswatch -o ./src \
    | xargs -n1 -I{} ./scripts/generate.sh
