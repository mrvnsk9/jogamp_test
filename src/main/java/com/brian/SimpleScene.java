package com.brian;

import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.FPSAnimator;

import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import java.awt.*;

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
        theta += 0.01;
        s = Math.sin(theta);
        c = Math.cos(theta);
    }

    private void update() {
    }

    private void render(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
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
        GLCanvas canvas = new GLCanvas(caps);

        Frame frame = new Frame("AWT Window Test");
        frame.setSize(300, 300);
        frame.add(canvas);
        frame.setVisible(true);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });

        canvas.addGLEventListener(new SimpleScene());

        GLAnimatorControl animator = new Animator(canvas);
        animator.start();
    }
}
