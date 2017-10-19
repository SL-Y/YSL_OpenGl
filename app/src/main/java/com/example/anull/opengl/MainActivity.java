package com.example.anull.opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import Util.ShaderUtils;

import static android.R.attr.width;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GLSurfaceView glSurfaceView = (GLSurfaceView) findViewById(R.id.gl_view);

        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(new GLRenderer(this));
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        //4
    }

    public class GLRenderer implements GLSurfaceView.Renderer {
        private Context context;
        private int programId;
        private int aPositionHandle;
        private int uMatrixHandle;
        private final float[] projectionMatrix=new float[16];
        private final float[] vertexData = {
                0f,0f,0f,
                1f,-1f,0f,
                1f,1f,0f
        };
        private FloatBuffer vertexBuffer;
        public GLRenderer(Context context) {
            this.context = context ;
            vertexBuffer = ByteBuffer.allocateDirect(vertexData.length * 4)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer()
                    .put(vertexData);
            vertexBuffer.position(0);
        }

        /**
         * surfacec初始化，来回切换页面，也会调用
         * @param gl10
         * @param eglConfig
         */
        @Override
        public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
            String ver = ShaderUtils.readRawTextFile(context, R.raw.vertex_shader);
            String fra = ShaderUtils.readRawTextFile(context, R.raw.fragment_shader);
            programId = ShaderUtils.createProgram(ver, fra);
            aPositionHandle = GLES20.glGetAttribLocation(programId, "aPosition");
            uMatrixHandle=GLES20.glGetUniformLocation(programId,"uMatrix");
        }

        /**
         * 每当屏幕尺寸发生变化
         * @param gl10
         * @param i
         * @param i1
         */
        @Override
        public void onSurfaceChanged(GL10 gl10, int width, int height) {


            float ratio=width>height?
                    (float)width/height:
                    (float)height/width;
            if (width>height){
                Matrix.orthoM(projectionMatrix,0,-ratio,ratio,-1f,1f,-1f,1f);
            }else Matrix.orthoM(projectionMatrix,0,-1f,1f,-ratio,ratio,-1f,1f);

        }

        /**
         * 每次绘制时这个函数都会被调用
         * @param gl10
         */
        @Override
        public void onDrawFrame(GL10 gl10) {
           /* GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            GLES20.glUseProgram(programId);
            GLES20.glEnableVertexAttribArray(aPositionHandle);
            GLES20.glVertexAttribPointer(aPositionHandle, 3, GLES20.GL_FLOAT, false,
                    12, vertexBuffer);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);*/



            GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            GLES20.glUseProgram(programId);
            GLES20.glUniformMatrix4fv(uMatrixHandle,1,false,projectionMatrix,0);
            GLES20.glEnableVertexAttribArray(aPositionHandle);
            GLES20.glVertexAttribPointer(aPositionHandle, 3, GLES20.GL_FLOAT, false,
                    12, vertexBuffer);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
        }

    }
}
