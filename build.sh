#!/bin/bash
# SmartSort v1.1.0 Build Script

echo "================================"
echo "SmartSort v1.1.0 Builder"
echo "================================"
echo ""

# Check for Maven
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed!"
    echo "Please install Maven first:"
    echo "  Ubuntu/Debian: sudo apt install maven"
    echo "  macOS: brew install maven"
    echo "  Windows: Download from https://maven.apache.org/"
    exit 1
fi

# Check for Java
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed!"
    echo "Please install Java 8 or higher"
    exit 1
fi

echo "✅ Maven found: $(mvn --version | head -n 1)"
echo "✅ Java found: $(java -version 2>&1 | head -n 1)"
echo ""

# Navigate to script directory
cd "$(dirname "$0")"

# Clean previous builds
echo "🧹 Cleaning previous builds..."
mvn clean > /dev/null 2>&1

# Build the plugin
echo "🔨 Building SmartSort v1.1.0..."
echo "   This may take a minute on first run..."
echo ""

mvn package -q

# Check if build was successful
if [ -f "target/SmartSort-1.1.0.jar" ]; then
    echo ""
    echo "================================"
    echo "✅ BUILD SUCCESSFUL!"
    echo "================================"
    echo ""
    echo "📦 Output: target/SmartSort-1.1.0.jar"
    echo "📏 Size: $(ls -lh target/SmartSort-1.1.0.jar | awk '{print $5}')"
    echo ""
    echo "Next steps:"
    echo "1. Stop your Minecraft server"
    echo "2. Copy target/SmartSort-1.1.0.jar to your plugins folder"
    echo "3. Start your server"
    echo "4. Configure permissions (see PERMISSIONS.md)"
    echo ""
else
    echo ""
    echo "================================"
    echo "❌ BUILD FAILED!"
    echo "================================"
    echo ""
    echo "Please check the error messages above."
    echo "Common issues:"
    echo "- No internet connection (Maven needs to download dependencies)"
    echo "- Java version too old (needs Java 8+)"
    echo "- Corrupted Maven cache (run: mvn clean install -U)"
    echo ""
    exit 1
fi
