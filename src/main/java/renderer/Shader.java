package renderer;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {
    private int shaderProgramID;
    private boolean beingUsed = false;
    private String vertexSource;
    private String fragmentSource;
    private String filepath;
    public Shader(String filepath){
        this.filepath = filepath;
        try {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
//            System.out.println(source);
            String[] splitString = source.split("(#type)(\\s+)([a-zA-Z]+)");

//            System.out.println(splitString);


            //Find the first pattern after #type pattern
            int index = source.indexOf("#type") + 6;
            System.out.println(index);
            int eol = source.indexOf("\r\n",index);
            System.out.println(eol);
            String firstPattern = source.substring(index,eol).trim();
            System.out.println(firstPattern);

            //Find the second pattern after #type pattern
            index = source.indexOf("#type",eol) + 6;
            eol = source.indexOf("\r\n",index);
            String secondPattern = source.substring(index,eol).trim();
//            System.out.println(secondPattern);
            if (firstPattern.equals("vertex")){
                vertexSource = splitString[1];
                System.out.println(vertexSource);
            } else if (firstPattern.equals("fragment")) {
                fragmentSource = splitString[1];
//                System.out.println(fragmentSource);
            } else {
                throw  new IOException("Unexpected token '" + firstPattern +  "'");

            }
            if (secondPattern.equals("vertex")){
                vertexSource = splitString[2];
                System.out.println(vertexSource);
            } else if (secondPattern.equals("fragment")) {
                fragmentSource = splitString[2];
//                System.out.println(fragmentSource);
            } else {
                throw  new IOException("Unexpected token '" + secondPattern +  "'");

            }
//            System.out.println(vertexSource);
//            System.out.println(fragmentSource);

        }catch (IOException e){
            e.printStackTrace();
            assert false: "Error: could not open file for shader:'" + filepath + "'";
        }
//        System.out.println(vertexSource);
//        System.out.println(fragmentSource);
    }


    public void compile(){
        int vertexID, fragmentID;
        //        Compile and link shaders
//        first load and compile the vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
//        pass the shader source to gpu
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);
//        check errors in compilation
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE){
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\tVertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }
        //        first load and compile the vertex shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
//        pass the shader source to gpu
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);
//        check errors in compilation
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE){
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\tFragment shader compilation failed.");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }
        // link shaders and check errors
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        // Check for linking errors
        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE){
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\tLinking shaders failed.");
            System.out.println(glGetProgramInfoLog(shaderProgramID, len));
            assert false : "";
        }
    }

    public void use(){
        if(!beingUsed){
            // Bind shader program
            glUseProgram(shaderProgramID);
            beingUsed = true;
        }

    }

    public void detach(){
        glUseProgram(0);
        beingUsed = false;
    }

    public void uploadMat4f(String varName, Matrix4f mat4){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);

    }
    public void uploadMat3f(String varName, Matrix3f mat3){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat3.get(matBuffer);
        glUniformMatrix3fv(varLocation, false, matBuffer);

    }

    public  void  uploadVec4f(String varName, Vector4f vec){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
    }

    public  void  uploadVec3f(String varName, Vector3f vec){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform3f(varLocation, vec.x, vec.y, vec.z);
    }

    public  void  uploadVec2f(String varName, Vector2f vec){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform2f(varLocation, vec.x, vec.y);
    }

    public void uploadFloat(String varName, float val){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1f(varLocation, val);
    }

    public  void uploadInt(String varName, int val){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1i(varLocation, val);
    }

    public void uploadTexture(String varName, int slot){
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1i(varLocation, slot);
    }
}
