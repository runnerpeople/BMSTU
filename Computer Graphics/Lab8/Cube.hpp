#ifndef OPENGL_CUBE_HPP
#define OPENGL_CUBE_HPP

#include <GL/glew.h>

#include <iostream>
#include <string>

#include "Point.hpp"

Point3D vertices[] = {{-0.8f, -0.8f, -0.8f},
                    {0.8f, -0.8f, -0.8f},
                    {0.8f,  0.8f, -0.8f},
                    {-0.8f, 0.8f, -0.8f},
                    {-0.8f, -0.8f,  0.8f},
                    {0.8f, -0.8f,  0.8f},
                    {0.8f,  0.8f,  0.8f},
                    {-0.8f,  0.8f,  0.8f}};

Point3D color_triangles[] = {{1.0f, 0.5f, 1.0f},
                           {1.0f, 0.5f, 0.5f},
                           {0.5f, 0.5f, 1.0f},
                           {0.0f, 1.0f, 1.0f},
                           {1.0f, 0.0f, 1.0f},
                           {1.0f, 1.0f, 0.0f},
                           {1.0f, 0.0f, 1.0f},
                           {0.0f, 1.0f, 1.0f}};
GLuint indices[] =  {
        0, 4, 5, 0, 5, 1,
        1, 5, 6, 1, 6, 2,
        2, 6, 7, 2, 7, 3,
        3, 7, 4, 3, 4, 0,
        4, 7, 6, 4, 6, 5,
        3, 0, 1, 3, 1, 2
};

enum CubeID {VERTICES, COLORS, INDICES};


class Cube {
    public:

        Cube() {};

        void loadVBO() {

            glGenBuffers(1, &buffer[CubeID::VERTICES]);
            glBindBuffer(GL_ARRAY_BUFFER, buffer[CubeID::VERTICES]);
            glBufferData(GL_ARRAY_BUFFER, sizeof(vertices), vertices, GL_STATIC_DRAW);

            glGenBuffers(1, &buffer[CubeID::COLORS]);
            glBindBuffer(GL_ARRAY_BUFFER, buffer[CubeID::COLORS]);
            glBufferData(GL_ARRAY_BUFFER, sizeof(color_triangles), color_triangles, GL_STATIC_DRAW);

            glGenBuffers(1, &buffer[CubeID::INDICES]);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffer[CubeID::INDICES]);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof(indices), indices, GL_STATIC_DRAW);

        }

        void unloadVBO() {
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

            glDeleteBuffers(1, &buffer[CubeID::VERTICES]);
            glDeleteBuffers(1, &buffer[CubeID::COLORS]);
            glDeleteBuffers(1, &buffer[CubeID::INDICES]);
        }

        GLuint buffer[3];

};


#endif //OPENGL_CUBE_HPP
