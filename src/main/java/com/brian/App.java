package com.brian;

import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;

/**
 * Hello world!
 *
 */
public class App 
{
    static {
        GLProfile.initSingleton();
    }

    public void run() {
        GLProfile glp = GLProfile.getDefault();
//        GLProfile glp = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(glp);

        GLWindow window = GLWindow.create(caps);
        window.setSize(800, 600);
        window.setVisible(true);
        window.setTitle("NEWT Window Test");

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDestroyNotify(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

    }

    public static void main( String[] args )
    {
        new App().run();
    }
}
