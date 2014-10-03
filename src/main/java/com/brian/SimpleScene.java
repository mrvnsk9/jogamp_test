package com.brian;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;

import javax.media.opengl.*;

/**
 * Created by brian on 10/3/14.
 */
public class SimpleScene implements GLEventListener {
    static {
        GLProfile.initSingleton();
    }

    public void run() {
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);

        GLWindow window = GLWindow.create(caps);
        window.setSize(800, 600);
        window.setVisible(true);
        window.setTitle("NEWT Window Test");
        window.addGLEventListener(this);

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDestroyNotify(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        update();
        render(glAutoDrawable);
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i2, int i3, int i4) {

    }

    private void update() {

    }

    private void render(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glBegin(GL.GL_TRIANGLES);
        gl.glColor3f(1, 0, 0);
        gl.glVertex2f(-1, -1);
        gl.glColor3f(0, 1, 0);
        gl.glVertex2f(0, 1);
        gl.glColor3f(0, 0, 1);
        gl.glVertex2f(1, -1);
        gl.glEnd();
    }

    public static void main(String[] args) {
        new SimpleScene().run();
    }
}
