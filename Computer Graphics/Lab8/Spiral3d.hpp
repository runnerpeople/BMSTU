#ifndef OPENGL_SPIRAL3D_HPP
#define OPENGL_SPIRAL3D_HPP

#define _USE_MATH_DEFINES

#include <GL/glew.h>

#include <iostream>
#include <string>
#include <cmath>
#include <vector>

#include <glm/glm.hpp>
#include <glm/ext.hpp>
#include <glm/gtc/type_ptr.hpp>
#include <glm/gtc/matrix_transform.hpp>

#include "Point.hpp"

// There is difference between 'drawpoints_coords' and 'point_coords'                     //
// 'drawpoints_coords' = array for rendering (VBO) using GL_TRIANGLE_STRIP (optimized)    //
// 'points_coords' = array for rendering without VBO using glBegin(GL_QUADS) .. glEnd()   //


class Spiral3d {
    public:
        double radius_gyration;
        double radius_circle;
        int partition;

        bool animation;
        bool normalize;

        bool light_flag;
        bool texture_flag;

        double y_min=0.0;
        int count;

        GLuint buffer_vbo;
        GLuint element_vbo;

        std::vector<Point3D> drawpoint_coords;
        std::vector<int> element_array;


        Spiral3d(double radius_gyration,double radius_circle);
        virtual ~Spiral3d();
        void generate_points(bool light_flag, bool texture_flag);
        void get_min(glm::vec4 vector);
        void points_toDrawpoints();
//        void animate(float dt);
        void regenerate(bool flag);
        glm::mat4 frene(float i);

        void loadVBO();
        void unloadVBO();


    private:

        void resize();

        // Const variable for helix //
        static constexpr int partition_MAX = 75;
        static constexpr int gyration = 80;
        static constexpr double angle_gyrate = 4 * M_PI;
        static constexpr double step = M_PI / 20;
        // ======================== //

        std::vector<std::vector<Point4D>> point_coords;
        std::vector<std::vector<Point2D>> texture_coords;
        std::vector<std::vector<Point4D>> normal_coords;

};


#endif //OPENGL_SPIRAL3D_HPP
