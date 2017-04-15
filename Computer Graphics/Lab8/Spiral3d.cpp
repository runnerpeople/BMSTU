#include "Spiral3d.hpp"

Spiral3d::Spiral3d(double radius_gyration,double radius_circle): radius_gyration(radius_gyration), radius_circle(radius_circle),
                                                               partition(25), animation(false),normalize(false),buffer_vbo(0),
                                                                light_flag(false), texture_flag(false),count(0) {
    for (int i=0;i<gyration;i++) {
        for (int j=0;j<partition;j++) {
            point_coords.push_back(std::vector<Point4D>(partition,(Point4D){0,0,0,0}));
            texture_coords.push_back(std::vector<Point2D>(partition,(Point2D){0,0}));
            normal_coords.push_back(std::vector<Point4D>(partition,(Point4D){0,0,0,0}));
        }
    }
    drawpoint_coords.reserve(partition*gyration*3);
}

Spiral3d::~Spiral3d() { }

glm::mat4 Spiral3d::frene(float i) {
    glm::mat4 frene_basis = {
        -std::cos(i),-std::sin(i),0.,0.,
        1/(sqrt(radius_gyration * radius_gyration + radius_circle * radius_circle)) * radius_circle * std::sin(i), 1/(sqrt(radius_gyration * radius_gyration + radius_circle * radius_circle)) * (-radius_circle) * std::cos(i), 1/(sqrt(radius_gyration * radius_gyration + radius_circle * radius_circle)) * radius_gyration, 0.,
        1/(sqrt(radius_gyration * radius_gyration + radius_circle * radius_circle)) * radius_gyration * (-1) * std::sin(i), 1/(sqrt(radius_gyration * radius_gyration + radius_circle * radius_circle)) * radius_gyration * std::cos(i), radius_circle /(sqrt(radius_gyration * radius_gyration + radius_circle * radius_circle)), 0.,
        radius_gyration * std::cos(i),radius_gyration * std::sin(i),radius_circle * i,1.
    };
    return glm::transpose(frene_basis);
}

void Spiral3d::get_min(glm::vec4 vector) {
    float y = vector[1];
    if (y_min==0.0) {
        y_min = y;
    }
    if (y_min > y)
        y_min = y;
}


void Spiral3d::generate_points(bool light_flag, bool texture_flag) {
    int count=0;
    for(float i=0;i<angle_gyrate;i+=step) {
        glm::mat4 matrix = frene(i);
        for (int j=0;j<partition;j++) {
            double alpha = 2 * j * M_PI / partition;
            glm::vec4 vector = {std::cos(alpha) * radius_circle,std::sin(alpha) * radius_circle,0,1};
            glm::vec4 new_vector = vector * matrix;
            get_min(new_vector);
            point_coords[count][j] = {new_vector[0],new_vector[1],new_vector[2],new_vector[3]};
        }
        count+=1;
    }
}

void Spiral3d::resize() {
    point_coords.clear();
    texture_coords.clear();
    normal_coords.clear();
    drawpoint_coords.clear();
    for (int i=0;i<gyration;i++) {
        for (int j=0;j<partition;j++) {
            point_coords.push_back(std::vector<Point4D>(partition,(Point4D){0,0,0,0}));
            texture_coords.push_back(std::vector<Point2D>(partition,(Point2D){0,0}));
            normal_coords.push_back(std::vector<Point4D>(partition,(Point4D){0,0,0,0}));
        }
    }
    drawpoint_coords.reserve(partition*gyration*3);
}

void Spiral3d::regenerate(bool flag) {
    if (flag) {
        if (partition + 1 > partition_MAX)
            return;
        partition += 1;
    }
    else {
        if (partition - 1 < 1)
            return;
        partition -= 1;
    }
    resize();
}

void Spiral3d::points_toDrawpoints() {
    int n = 0;
    for (int j = 0;j<gyration-1;j++)
        for (int i = 0; i < partition; i++) {
            drawpoint_coords[n++]=((Point3D) {point_coords[j][i].x, point_coords[j][i].y, point_coords[j][i].z});
            drawpoint_coords[n++]=((Point3D) {point_coords[j+1][i].x, point_coords[j+1][i].y, point_coords[j+1][i].z});
            if (i==partition-1) {
                drawpoint_coords[n++]=((Point3D) {point_coords[j][0].x, point_coords[j][0].y, point_coords[j][0].z});
                drawpoint_coords[n++]=((Point3D) {point_coords[j+1][0].x, point_coords[j+1][0].y, point_coords[j+1][0].z});
            }
        }
    count = n;
//    for (int j = 0;j<gyration-1;j++) {
//        for (int i = 0;i<partition;i++) {
//            element_array.push_back(partition*j+i);
//            element_array.push_back(partition*(j+1)+i);
//            if (i==partition-1) {
//                element_array.push_back(partition*j);
//                element_array.push_back(partition*(j+1));
//            }
//        }
//    }
}

void Spiral3d::loadVBO() {
    glGenBuffers(1,&buffer_vbo);
    glBindBuffer(GL_ARRAY_BUFFER, buffer_vbo);
    glBufferData(GL_ARRAY_BUFFER, count*12, &drawpoint_coords[0], GL_STATIC_DRAW);

//    glGenBuffers(1,&element_vbo);
//    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, element_vbo);
//    glBufferData(GL_ELEMENT_ARRAY_BUFFER, element_array.size(),element_array.data(), GL_STATIC_DRAW);


}

void Spiral3d::unloadVBO() {
    glBindBuffer(GL_ARRAY_BUFFER, 0);
//    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

    glDeleteBuffers(1,&buffer_vbo);
//    glDeleteBuffers(1,&element_vbo);
}