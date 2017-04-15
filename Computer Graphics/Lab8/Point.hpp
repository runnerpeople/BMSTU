#ifndef OPENGL_POINT_HPP
#define OPENGL_POINT_HPP

#include <GL/glew.h>

#include <iostream>
#include <string>

struct Point2D {
    GLfloat x, y;
    Point2D(float x_,float y_): x(x_),y(y_) {};
    Point2D() {};
};

struct Point3D {
    GLfloat x, y, z;
    Point3D(float x_,float y_,float z_): x(x_),y(y_),z(z_) {};
    Point3D() {};
};

struct Point4D {
    GLfloat x,y,z;
    GLfloat w;
    Point4D(float x_,float y_,float z_,float w_): x(x_),y(y_),z(z_),w(w_) {};
    Point4D() {};
};

#endif //OPENGL_POINT_HPP
