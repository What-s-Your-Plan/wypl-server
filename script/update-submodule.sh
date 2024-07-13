#!/bin/bash

echo "🚀 Start Submodule Update\n"

cd ..
git submodule update --remote --merge
git add ./src/main/resources/

echo '🎯 End Submodule Update'