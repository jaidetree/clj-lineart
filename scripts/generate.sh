#!/usr/bin/env bash

bb -cp src test/lineart.clj "bg=363333&fg1=F3C581&fg2=FFFFFF" > test/lineart.svg
echo "Generated lineart.svg:"
cat test/lineart.svg
