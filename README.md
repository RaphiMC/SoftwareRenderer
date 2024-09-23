# SoftwareRenderer
Library for rendering simple 2D and 3D scenes in pure Java.  

This project is mostly for learning purposes due to Java's slow performance.

## Features
- 2D and 3D rendering
- Z-Buffering (Depth Testing)
- Perspective correct texture mapping
- Backface Culling
- Written in pure Java

## Releases
### Gradle/Maven
To use SoftwareRenderer with Gradle/Maven you can get it from [Lenni0451's Maven](https://maven.lenni0451.net/#/releases/net/raphimc/software-renderer) or [Jitpack](https://jitpack.io/#RaphiMC/SoftwareRenderer).
You can also find instructions how to implement it into your build script there.

### Jar File
If you just want the latest jar file you can download it from [GitHub Actions](https://github.com/RaphiMC/SoftwareRenderer/actions/workflows/build.yml) or [Lenni0451's Jenkins](https://build.lenni0451.net/job/SoftwareRenderer/).

## Usage
SoftwareRenderer provides most of its functionality through the ``SoftwareRenderer`` and ``PerspectiveSoftwareRenderer`` class.

## Examples
* [Simple 3D Scene](/src/test/java/TestMain.java)
* [ImGui (2D)](https://github.com/Lenni0451/imgui-swing)

## Contact
If you encounter any issues, please report them on the
[issue tracker](https://github.com/RaphiMC/SoftwareRenderer/issues).  
If you just want to talk or need help implementing SoftwareRenderer feel free to join my
[Discord](https://discord.gg/dCzT9XHEWu).
