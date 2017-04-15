#include <GL/glew.h>
#include <GLFW/glfw3.h>
#include <glm/glm.hpp>
#include <glm/gtc/type_ptr.hpp>
#include <glm/gtc/matrix_transform.hpp>

#include <iostream>
#include <map>

#include "Shader.hpp"
#include "Cube.hpp"
#include "Spiral3d.hpp"

/* Global params */
Shader shader;
GLenum fill_polygon = GL_FILL;
Cube cube;
Spiral3d spiral(1.6,0.4);
int window_width = 1600;
int window_heigth = 900;

typedef struct Matrix { ;
    glm::mat4 matrix_projection;
    glm::mat4 matrix_modelview;

    Matrix() {
        matrix_modelview = glm::mat4(1.0);
        matrix_projection = glm::mat4(1.0);
    };

    void translate(std::string axis,bool flag) {
        float params = 0.05;
        if (!flag)
            params *= -1;
        if (axis.compare("x")==0)
            matrix_modelview = glm::translate(matrix_modelview,glm::vec3(params,0.0f,0.0f));
        else if (axis.compare("y")==0)
            matrix_modelview = glm::translate(matrix_modelview,glm::vec3(0.0f,params,0.0f));
        else if (axis.compare("z")==0)
            matrix_modelview = glm::translate(matrix_modelview,glm::vec3(0.0f,0.0f,params));
    }

    void rotate(std::string axis,bool flag) {
        float params = 1.0;
        if (!flag)
            params *= -1;
        if (axis.compare("x")==0)
            matrix_modelview = glm::rotate(matrix_modelview,glm::radians(params),glm::vec3(1.0f,0.0f,0.0f));
        else if (axis.compare("y")==0)
            matrix_modelview = glm::rotate(matrix_modelview,glm::radians(params),glm::vec3(0.0f,1.0f,0.0f));
        else if (axis.compare("z")==0)
            matrix_modelview = glm::rotate(matrix_modelview,glm::radians(params),glm::vec3(0.0f,0.0f,1.0f));
    }

    void scale(std::string axis,bool flag) {
        float scale = 0.01;
        if (!flag)
            scale *= -1;
        matrix_modelview = glm::scale(matrix_modelview,glm::vec3(1.0+scale,1.0+scale,1.0+scale));
    }

} Matrix;

Matrix m;




static void error_callback(int error,const char* description) {
    std::cerr << "Error: " << description << std::endl;
}


void key_callback(GLFWwindow* window, int key, int scancode,int action,int mods) {
    if (key == GLFW_KEY_ENTER && action == GLFW_PRESS) {
        if (fill_polygon == GL_FILL) {
            fill_polygon = GL_LINE;
        }
        else {
            fill_polygon = GL_FILL;
        }
    }
    if (action == GLFW_PRESS) {
        switch (key) {
            case GLFW_KEY_U:
                m.translate("x",true);
                break;
            case GLFW_KEY_J:
                m.translate("x",false);
                break;
            case GLFW_KEY_I:
                m.translate("y",true);
                break;
            case GLFW_KEY_K:
                m.translate("y",false);
                break;
            case GLFW_KEY_O:
                m.translate("z",true);
                break;
            case GLFW_KEY_L:
                m.translate("z",false);
                break;
            case GLFW_KEY_Q:
                m.rotate("x",true);
                break;
            case GLFW_KEY_A:
                m.rotate("x",false);
                break;
            case GLFW_KEY_W:
                m.rotate("y",true);
                break;
            case GLFW_KEY_S:
                m.rotate("y",false);
                break;
            case GLFW_KEY_E:
                m.rotate("z",true);
                break;
            case GLFW_KEY_D:
                m.rotate("z",false);
                break;
            case GLFW_KEY_UP:
                m.scale("",true);
                break;
            case GLFW_KEY_DOWN:
                m.scale("",false);
                break;
            case GLFW_KEY_MINUS:
                spiral.regenerate(false);
                spiral.generate_points(false,false);
                spiral.points_toDrawpoints();
                break;
            case GLFW_KEY_EQUAL:
                spiral.regenerate(true);
                spiral.generate_points(false,false);
                spiral.points_toDrawpoints();
                break;

        }
    }
}




void framebuffer_size_callback(GLFWwindow* window, int width, int height) {
    glViewport(0, 0, width, height);
    height = height > 0 ? height : 1;
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();

//    glm::mat4 matrix = {-0.5555968880653381, 0.16962061822414398, -0.6942540407180786, -0.6941152215003967,
//                        0.0, 1.2340744733810425, 0.19084715843200684, 0.1908089965581894,
//                        -0.5555968880653381, -0.16962061822414398, 0.6942540407180786, 0.6941152215003967,
//                        -31.42930793762207, 0.0, 199.83998107910156, 200.0};
//    m.matrix_projection = glm::perspective(45.0f,(float)width/(float)height,1.0f,200.0f);
//    m.matrix_projection = glm::translate(m.matrix_projection,glm::vec3(0.0f,0.0f,-10.0f));
//    m.matrix_projection = glm::rotate(m.matrix_projection, 60.0f, glm::vec3(1.0f, 1.0f, 0.0f));
//    m.matrix_projection = glm::rotate(m.matrix_projection, -225.0f, glm::vec3(0.0f, 1.0f, 0.0f));
    //glMultMatrixf(glm::value_ptr(matrix));

}

void draw(GLFWwindow* window) {

    glClearColor(0.6,0.6,0.8,1.0);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();

    glEnable(GL_DEPTH_TEST);
    glDepthFunc(GL_LESS);
    glPolygonMode(GL_FRONT_AND_BACK, fill_polygon);

//    cube.loadVBO();
    shader.use();

//    spiral.unloadVBO();
//    spiral.loadVBO();

    GLuint uniformViewID = shader.getUniform("matrix_modelview");
    glUniformMatrix4fv(uniformViewID,1,GL_FALSE,glm::value_ptr(m.matrix_modelview));


    glEnableVertexAttribArray(shader.getAttributeLocation("coord"));
//    glBindBuffer(GL_ARRAY_BUFFER,cube.buffer[CubeID::VERTICES]);
    glBindBuffer(GL_ARRAY_BUFFER,spiral.buffer_vbo);
    glVertexAttribPointer(shader.getAttributeLocation("coord"),3,GL_FLOAT,GL_FALSE,0,0);

//    glEnableVertexAttribArray(shader.getAttributeLocation("color"));
//    glBindBuffer(GL_ARRAY_BUFFER,cube.buffer[CubeID::COLORS]);
//    glVertexAttribPointer(shader.getAttributeLocation("color"),3,GL_FLOAT,GL_FALSE,0,0);

//    int size;
//    glGetBufferParameteriv(GL_ELEMENT_ARRAY_BUFFER, GL_BUFFER_SIZE, &size);
//    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,cube.buffer[CubeID::INDICES]);
//    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,spiral.element_vbo);
//    glDrawElements(GL_TRIANGLE_STRIP,spiral.element_array.size(),GL_UNSIGNED_INT,0);
//    std::cout << spiral.count << std::endl;
    glDrawArrays(GL_TRIANGLE_STRIP,0,spiral.count);
//    glDrawElements(GL_TRIANGLES,size/sizeof(GLuint),GL_UNSIGNED_INT,0);
//    std::cout << spiral.count << std::endl;
//    glDrawArrays(GL_QUADS,0,spiral.count);

    glDisableVertexAttribArray(shader.getAttributeLocation("coord"));
//    glDisableVertexAttribArray(shader.getAttributeLocation("color"));

    shader.unuse();

//    cube.unloadVBO();
//    spiral.unloadVBO();

}



int main(void) {
    GLFWwindow* window;

    glfwSetErrorCallback(error_callback);


    if (!glfwInit())
        exit(EXIT_FAILURE);

    window = glfwCreateWindow(1600,900,"Lab2",NULL,NULL);
    if (!window) {
        glfwTerminate();
        exit(EXIT_FAILURE);
    }
    glfwMakeContextCurrent(window);
    glfwSetKeyCallback(window,key_callback);
    glfwSetFramebufferSizeCallback(window,framebuffer_size_callback);
    glfwSwapInterval(1);

    glewExperimental = GL_TRUE;
    glewInit();

    shader.loadFromFile(GL_VERTEX_SHADER,"/home/runnerpeople/ClionProjects/OpenGL/vertex_shader.glsl",ShaderType::VERTEX_SHADER);
    shader.loadFromFile(GL_FRAGMENT_SHADER,"/home/runnerpeople/ClionProjects/OpenGL/fragment_shader.glsl",ShaderType::FRAGMENT_SHADER);
    shader.link();
    shader.saveAttribute("coord");
    shader.saveAttribute("color");
    shader.saveUniform("matrix_projection");
    shader.saveUniform("matrix_modelview");
    shader.saveUniform("global_color");

    framebuffer_size_callback(window,1600,900);
//    cube.loadVBO();

    shader.use();

    m.matrix_projection = glm::perspective(76.5f,(float)window_width/(float)window_heigth,0.1f,1000.0f);
    m.matrix_projection = glm::translate(m.matrix_projection,glm::vec3(0.5f,0.0f,-5.0f));
    m.matrix_projection = glm::rotate(m.matrix_projection,glm::radians(215.0f),glm::vec3(0.0f,1.0f,0.0f));
    m.matrix_projection = glm::rotate(m.matrix_projection,glm::radians(4.5f), glm::vec3(1.0f,0.0f,0.0f));

    GLuint uniformProjectionID = shader.getUniform("matrix_projection");
    glUniformMatrix4fv(uniformProjectionID,1,GL_FALSE,glm::value_ptr(m.matrix_projection));

    GLuint uniformViewID = shader.getUniform("matrix_modelview");
    glUniformMatrix4fv(uniformViewID,1,GL_FALSE,glm::value_ptr(m.matrix_modelview));

    GLuint uniformColorID = shader.getUniform("global_color");
    glUniform1i(uniformColorID,true);

    /* For spiral functions */

    spiral.generate_points(false,false);
    spiral.points_toDrawpoints();

    spiral.loadVBO();

//    for (int i=0;i<spiral.count;i++) {
//        std::cout << spiral.drawpoint_coords[i].x << " " << spiral.drawpoint_coords[i].y << " " << spiral.drawpoint_coords[i].z << std::endl;
//    }
    shader.unuse();

    while(!glfwWindowShouldClose(window)) {
        draw(window);

        glfwPollEvents();
        glfwSwapBuffers(window);
    }

    glfwDestroyWindow(window);

//    cube.unloadVBO();
    spiral.unloadVBO();

    glfwTerminate();
    exit(EXIT_SUCCESS);
}