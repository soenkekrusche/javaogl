package engine.graphics;

import engine.utils.FileUtils;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    private String vertexFile;
    private String fragmentFile;
    private int vertexID;
    private int fragmentID;
    private int programID;


    public Shader(String vertexPath, String fragmentPath) {
        vertexFile = FileUtils.loadAsString(vertexPath);
        fragmentFile = FileUtils.loadAsString(fragmentPath);
    }

    public void create() {
        programID = glCreateProgram();

        // ----------------- create VertexShader -----------------
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // vertexID and vertexPath as arguments
        glShaderSource(vertexID, vertexFile);
        //try to compile vertexShader
        glCompileShader(vertexID);
        //return if shaderinfo "compilestatus" of shader "vertexID" is equal to GL_False -> compiling failed
        if (glGetShaderi(vertexID, GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Vertex Shader: " + glGetShaderInfoLog(vertexID));
            return;
        }

        //---------------------create FragmentShader ----------- see above for detailed information
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentFile);
        glCompileShader(fragmentID);
        if (glGetShaderi(fragmentID, GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Fragment Shader: " + glGetShaderInfoLog(fragmentID));
            return;
        }

        glAttachShader(programID, vertexID);
        glAttachShader(programID, fragmentID);

        glLinkProgram(programID);
        // error checking to see if program linking worked
        if (glGetProgrami(programID, GL_LINK_STATUS) == GL11.GL_FALSE) {
            System.err.println("Program Linking: " + glGetProgramInfoLog(programID));
            return;
        }

        glValidateProgram(programID);
        if (glGetProgrami(programID, GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Program Validation: " + glGetProgramInfoLog(programID));
            return;
        }

        glDeleteShader(vertexID);
        glDeleteShader(fragmentID);
    }

    public void bind() {
        glUseProgram(programID);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void destroy() {
        glDeleteProgram(programID);
    }
}
