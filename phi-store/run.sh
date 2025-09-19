set -e

BIN_DIR="bin"
SRC_DIR="src"

mkdir -p "$BIN_DIR"
rm -rf "$BIN_DIR/*"

# use better glob expansion if nested packages exist
# javac -d "$BIN_DIR" "$SRC_DIR"/*.java "$SRC_DIR"/**/*.java
javac -d "$BIN_DIR" "$SRC_DIR"/*.java "$SRC_DIR"/*.java
java -cp "$BIN_DIR" Main $@


sudo kill $(pgrep ollama)
