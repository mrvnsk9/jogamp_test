package com.brian;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.Animator;

import javax.media.opengl.*;

/**
 * Created by brian on 10/3/14.
 */
public class SimpleScene implements GLEventListener {
    private double theta = 0;
    private double s = 0;
    private double c = 0;

    static {
        GLProfile.initSingleton();
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        glAutoDrawable.getGL().setSwapInterval(1);
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
        theta += 0.01;
        s = Math.sin(theta);
        c = Math.cos(theta);
    }

    private void render(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glBegin(GL.GL_TRIANGLES);
        gl.glColor3f(1, 0, 0);
        gl.glVertex2f(-(float)c, -(float)c);
        gl.glColor3f(0, 1, 0);
        gl.glVertex2f(0, (float)c);
        gl.glColor3f(0, 0, 1);
        gl.glVertex2f((float)s, -(float)s);
        gl.glEnd();
    }

    public static void main(String[] args) {
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);

        GLWindow window = GLWindow.create(caps);
        window.setTitle("Newt Window Test");
        window.setVisible(true);
        window.setSize(800, 600);

        window.addGLEventListener(new SimpleScene());
        window.addWindowListener(new QuitListener());

        GLAnimatorControl animator = new Animator();
        animator.add(window);
        animator.start();
    }
}
