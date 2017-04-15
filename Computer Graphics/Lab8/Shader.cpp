#include "Shader.hpp"

Shader::Shader(): program(0) {
    shaders[VERTEX_SHADER] = 0;
    shaders[FRAGMENT_SHADER] = 0;
    shaders[GEOMETRY_SHADER] = 0;
}

Shader::~Shader() {
    this->unuse();
    if (shaders[VERTEX_SHADER]!=0)
        glDeleteShader(shaders[VERTEX_SHADER]);
    if (shaders[FRAGMENT_SHADER]!=0)
        glDeleteShader(shaders[FRAGMENT_SHADER]);
    if (shaders[GEOMETRY_SHADER]!=0)
        glDeleteShader(shaders[GEOMETRY_SHADER]);
    if (program != 0)
        glDeleteProgram(program);
}

void Shader::loadFromText(GLenum type, const std::string &src, ShaderType shaderType) {
    GLint status;
    GLuint shader = glCreateShader(type);
    const char* text = src.c_str();
    glShaderSource(shader,1,&text,NULL);
    glCompileShader(shader);
    glGetShaderiv(shader,GL_COMPILE_STATUS,&status);
    if (status == GL_FALSE) {
        std::cerr << "error compile shader: " << src << std::endl;
    }
    shaders[shaderType] = shader;
//    std::cout << "compile shader" << std::endl;
}

void Shader::loadFromFile(GLenum type, const std::string &file_name, ShaderType shaderType) {
    std::ifstream file(file_name);
    if (file.is_open()) {
        std::string shader((std::istreambuf_iterator<char>(file)), std::istreambuf_iterator<char>());
        this->loadFromText(type,shader,shaderType);
    }
    else {
        std::cerr << "error open file: " << file_name << std::endl;
    }
//    std::cout << "opened file: " << file_name << std::endl;
}

void Shader::link() {
    program = glCreateProgram();
    if (shaders[VERTEX_SHADER]!=0)
        glAttachShader(program,shaders[VERTEX_SHADER]);
    if (shaders[FRAGMENT_SHADER]!=0)
        glAttachShader(program,shaders[FRAGMENT_SHADER]);
    if (shaders[GEOMETRY_SHADER]!=0)
        glAttachShader(program,shaders[GEOMETRY_SHADER]);

    GLint link_ok;
    glLinkProgram(program);
    glGetProgramiv(program,GL_LINK_STATUS,&link_ok);
    if (link_ok == GL_FALSE) {
        std::cerr << "error link program" << std::endl;
    }
//    std::cout << "linked!" << std::endl;
}

void Shader::saveAttribute(const char *attrib) {
    attribList[attrib]=glGetAttribLocation(program,attrib);
}

void Shader::saveUniform(const char *uniform) {
    uniformlocationList[uniform]=glGetUniformLocation(program,uniform);
}

GLuint Shader::getAttributeLocation(const char *attrib) {
    return attribList[attrib];
}

GLuint Shader::getUniform(const char *uniform) {
    return uniformlocationList[uniform];
}

GLuint Shader::getIDProgram() const {
    return program;
}

void Shader::use() const {
    if (program!=0)
        glUseProgram(program);
    else
        std::cerr << "error in using program" << std::endl;
}

void Shader::unuse() const {
    glUseProgram(0);
}