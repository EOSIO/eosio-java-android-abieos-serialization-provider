# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.8.0)

set(CMAKE_CXX_FLAGS "-std=c++17 --target=x86_64-linux-android29 -march=x86-64")

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
        abieos-lib

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        abieos-lib.cpp
        ../../../../abieos/src/abieos.cpp
        ../../../../abieos/src/abi.cpp
        ../../../../abieos/src/crypto.cpp
        ../../../../abieos/src/fuzzer.cpp
        ../../../../abieos/include/eosio/fpconv.cpp)

#include(../../../../abieos/build/libabieos.so)

#set_target_properties(abieos-lib PROPERTIES COMPILE_FLAGS "-m32" LINK_FLAGS "-m32")

target_include_directories(abieos-lib PRIVATE include ../../../../abieos/include ../../../../abieos/external/rapidjson/include)

#target_include_directories(abieos-lib PRIVATE include ../../../../abieos/include ../../../../abieos/external/rapidjson/include)
#LINK_DIRECTORIES(../../../../abieos/build)
# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

#find_library(
#        TEST
#        libabieos
#)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        abieos-lib

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})
