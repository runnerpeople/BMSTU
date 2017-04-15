#ifndef OPENGL_SHADER_HPP
#define OPENGL_SHADER_HPP

#include <GL/glew.h>

#include <iostream>
#include <string>
#include <map>
#include <fstream>

using namespace std;

enum ShaderType {VERTEX_SHADER, FRAGMENT_SHADER, GEOMETRY_SHADER};

class Shader {
    public:
        Shader();
        virtual ~Shader();
        void loadFromText(GLenum type,const std::string& src,ShaderType shaderType);
        void loadFromFile(GLenum type,const std::string& file_name,ShaderType shaderType);

        void link();

        void saveAttribute(const char* attrib);
        void saveUniform(const char* uniform);
        GLuint getAttributeLocation(const char* attrib);
        GLuint getUniform(const char* uniform);

        GLuint getIDProgram() const;

        void use() const;
        void unuse() const;

    private:
        GLuint program;
        GLuint shaders[3];
        std::map<std::string,GLuint> attribList;
        std::map<std::string,GLuint> uniformlocationList;
};


#endif //OPENGL_SHADER_HPP
