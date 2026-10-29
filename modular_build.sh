#!/bin/bash
# NovaOS Modular Build Script
# This script allows you to build specific parts of the OS to save time and system resources.
# Optimized for your Ryzen 5 5625U (6 Cores/12 Threads)

# 1. Setup environment
echo "Setting up build environment..."
source build/envsetup.sh
TARGET=${1:-nova_emu_x86_64-userdebug}
lunch $TARGET || { echo "Lunch failed. Please ensure your target is correct."; exit 1; }

# Safe threads to prevent system freeze
export BUILD_THREADS=6

echo "------------------------------------------------"
echo "NovaOS Modular Build Menu"
echo "Build individual components to verify your changes:"
echo "1) Build NovaSystemUI (Status Bar, QS, Lockscreen)"
echo "2) Build NovaSettings (Advanced Customization Center)"
echo "3) Build NovaLauncher (Home Screen)"
echo "4) Build Core Services (Framework logic, Reboot, Gestures)"
echo "5) Build NovaDialer"
echo "6) Build NovaAccount"
echo "------------------------------------------------"
echo "q) Exit"
echo "------------------------------------------------"
read -p "Select a component to build: " choice

case $choice in
    1)
        echo "Building NovaSystemUI..."
        m NovaSystemUI -j$BUILD_THREADS
        ;;
    2)
        echo "Building NovaSettings..."
        m NovaSettings -j$BUILD_THREADS
        ;;
    3)
        echo "Building NovaLauncher..."
        m NovaLauncher -j$BUILD_THREADS
        ;;
    4)
        echo "Building Framework Services..."
        m services -j$BUILD_THREADS
        ;;
    5)
        echo "Building NovaDialer..."
        m NovaDialer -j$BUILD_THREADS
        ;;
    6)
        echo "Building NovaAccount..."
        m NovaAccount -j$BUILD_THREADS
        ;;
    q)
        exit 0
        ;;
    *)
        echo "Invalid selection."
        ;;
esac

if [ $? -eq 0 ]; then
    echo "------------------------------------------------"
    echo "Module Build Successful!"
    echo "You can find the output APK in out/target/product/..."
    echo "------------------------------------------------"
else
    echo "------------------------------------------------"
    echo "Module Build Failed. Please check the logs above."
    echo "------------------------------------------------"
fi
