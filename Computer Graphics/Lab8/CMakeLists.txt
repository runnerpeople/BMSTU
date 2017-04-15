cmake_minimum_required(VERSION 3.3)
project(OpenGL)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -g")
set(SOURCE_FILES main.cpp Shader.cpp Shader.hpp Cube.hpp Spiral3d.cpp Spiral3d.hpp Point.hpp)
add_executable(OpenGL ${SOURCE_FILES})

find_package(PkgConfig REQUIRED)
find_package(OpenGL REQUIRED)
find_package(GLU REQUIRED)
find_package(glm REQUIRED)
find_package(GLEW REQUIRED)

pkg_search_module(GLFW REQUIRED glfw3)
include_directories(${GLFW_INCLUDE_DIRS} ${OPENGL_INCLUDE_DIRS} ${GLEW_INCLUDE_DIRS} ${GLM_INCLUDE_DIRS} ${GLU_INCLUDE_DIRS})
target_link_libraries(OpenGL ${OPENGL_gl_LIBRARY} ${GLFW_STATIC_LIBRARIES} ${OPENGL_STATIC_LIBRARIES} ${GLEW_LIBRARIES} ${GLM_STATIC_LIBRARIES} ${GLU_STATIC_LIBRARIES})






