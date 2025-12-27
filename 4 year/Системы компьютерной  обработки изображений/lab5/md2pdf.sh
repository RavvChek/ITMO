#!/bin/bash
cd "$(dirname "$0")"
eval "$(/usr/libexec/path_helper)" 2>/dev/null

pandoc README.md -o README.pdf \
    --pdf-engine=xelatex \
    -V geometry:margin=2.5cm \
    -V fontsize=11pt \
    -V colorlinks=true \
    -V mainfont="Arial" \
    -V monofont="Menlo"

[ $? -eq 0 ] && echo "Done: README.pdf" || echo "Error"
