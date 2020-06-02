# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

set(ABIEOS_SUBMODULE_PATH ../../../../abieos)

if(CMAKE_SIZEOF_VOID_P EQUAL 8)
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++17")
elseif(CMAKE_SIZEOF_VOID_P EQUAL 4)
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++17 -DABIEOS_NO_INT128")
endif()

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
        ${ABIEOS_SUBMODULE_PATH}/src/abieos.cpp
        ${ABIEOS_SUBMODULE_PATH}/src/abi.cpp
        ${ABIEOS_SUBMODULE_PATH}/src/crypto.cpp
        ${ABIEOS_SUBMODULE_PATH}/src/fuzzer.cpp
        ${ABIEOS_SUBMODULE_PATH}/include/eosio/fpconv.c)


target_include_directories(abieos-lib PRIVATE include ${ABIEOS_SUBMODULE_PATH}/include ${ABIEOS_SUBMODULE_PATH}/external/rapidjson/include)

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

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        abieos-lib

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})
