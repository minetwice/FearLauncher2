cmake_minimum_required(VERSION 3.22.1)
project("fearlauncher")

add_library(
        launcher-core
        SHARED
        launcher-core.cpp)

find_library(
        log-lib
        log)

target_link_libraries(
        launcher-core
        ${log-lib})
