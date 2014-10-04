package com.brian;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.Animator;
import com.sun.jna.Memory;
import com.sun.jna.NativeLibrary;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.LibVlcFactory;
import uk.co.caprica.vlcj.logger.Logger;
import uk.co.caprica.vlcj.player.AudioOutput;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.RenderCallback;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.runtime.x.LibXUtil;

import javax.media.opengl.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Brian on 10/3/2014.
 */
public class MediaPlayer implements GLEventListener {
    /**
     * Log level, used only if the -Dvlcj.log= system property has not already been set.
     */
    private static final String VLCJ_LOG_LEVEL = "INFO";

    /**
     * Change this to point to your own vlc installation, or comment out the code if you want to use
     * your system default installation.
     * <p>
     * This is a bit more explicit than using the -Djna.library.path= system property.
     */
    private static final String NATIVE_LIBRARY_SEARCH_PATH = null;

    /**
     * Set to true to dump out native JNA memory structures.
     */
    private static final String DUMP_NATIVE_MEMORY = "false";

    static {
        GLProfile.initSingleton();

        if(null == System.getProperty("vlcj.log")) {
            System.setProperty("vlcj.log", VLCJ_LOG_LEVEL);
        }

        // Safely try to initialise LibX11 to reduce the opportunity for native
        // crashes - this will silently throw an Error on Windows (and maybe MacOS)
        // that can safely be ignored
        LibXUtil.initialise();

        if(null != NATIVE_LIBRARY_SEARCH_PATH) {
            Logger.info("Explicitly adding JNA native library search path: '{}'", NATIVE_LIBRARY_SEARCH_PATH);
            NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), NATIVE_LIBRARY_SEARCH_PATH);
        }

        System.setProperty("jna.dump_memory", DUMP_NATIVE_MEMORY);
    }

    private MediaPlayerFactory mediaPlayerFactory;
    private DirectMediaPlayer mediaPlayer;
    private ByteBuffer buffer;
    private int texture;
    private AtomicBoolean dirty = new AtomicBoolean(false);

    public MediaPlayer() {
        List<String> vlcArgs = new ArrayList<>();

        vlcArgs.add("--no-snapshot-preview");
        vlcArgs.add("--quiet");
        vlcArgs.add("--quiet-synchro");
        vlcArgs.add("--intf");
        vlcArgs.add("dummy");

        // Special case to help out users on Windows (supposedly this is not actually needed)...
        // if(RuntimeUtil.isWindows()) {
        // vlcArgs.add("--plugin-path=" + WindowsRuntimeUtil.getVlcInstallDir() + "\\plugins");
        // }
        // else {
        // vlcArgs.add("--plugin-path=/home/linux/vlc/lib");
        // }

        // vlcArgs.add("--plugin-path=" + System.getProperty("user.home") + "/.vlcj");

        Logger.debug("vlcArgs={}", vlcArgs);

        mediaPlayerFactory = new MediaPlayerFactory(vlcArgs.toArray(new String[vlcArgs.size()]));
        mediaPlayerFactory.setUserAgent("vlcj test player");

        List<AudioOutput> audioOutputs = mediaPlayerFactory.getAudioOutputs();
        Logger.debug("audioOutputs={}", audioOutputs);

        mediaPlayer = mediaPlayerFactory.newDirectMediaPlayer(new TestBufferFormatCallback(), new TestRenderCallback());
        String media = "D:\\big_buck_bunny_480p_h264.mov";
        mediaPlayer.startMedia(media);

    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.setSwapInterval(1);

        gl.glLoadIdentity();
        gl.glOrtho(0, 854, 480, 0, 1, -1);

        gl.glEnable(GL.GL_TEXTURE_2D);
        texture = getTexture(drawable);
        gl.glBindTexture(GL.GL_TEXTURE_2D, texture);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE);
        gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, 854, 480, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, null);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        render(drawable);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

    }

    private void render(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glViewport(0, 0, 854, 480);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glBindTexture(GL.GL_TEXTURE_2D, texture);
        if (buffer != null && dirty.get()) {
            gl.glTexSubImage2D(GL.GL_TEXTURE_2D, 0, 0, 0, 854, 480, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, buffer);
            dirty.set(false);
        }
        gl.glBegin(GL.GL_TRIANGLES);
        {
            /* 1st triangle */
            gl.glTexCoord2f(0, 0);
            gl.glVertex2f(0, 0);
            gl.glTexCoord2f(0, 1);
            gl.glVertex2f(0, 480);
            gl.glTexCoord2f(1, 1);
            gl.glVertex2f(854, 480);

            /* 2nd triangle */
            gl.glTexCoord2f(0, 0);
            gl.glVertex2f(0, 0);
            gl.glTexCoord2f(1, 0);
            gl.glVertex2f(854, 0);
            gl.glTexCoord2f(1, 1);
            gl.glVertex2f(854, 480);

        }
        gl.glEnd();
    }

    private int getTexture(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        int[] textures = new int[1];
        gl.glGenTextures(1, textures, 0);
        return textures[0];
    }

    private final class TestRenderCallback implements RenderCallback {

        @Override
        public void display(DirectMediaPlayer mediaPlayer, Memory[] nativeBuffers, BufferFormat bufferFormat) {
            buffer = nativeBuffers[0].getByteBuffer(0, (long) (bufferFormat.getHeight() * bufferFormat.getWidth() * 4));
            dirty.set(true);
        }

    }

    private final class TestBufferFormatCallback implements BufferFormatCallback {

        @Override
        public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
            return new BufferFormat("RGBA", sourceWidth, sourceHeight, new int[] { sourceWidth * 4 }, new int[] { sourceHeight });
        }

    }

    public static void main(String[] args) {
        LibVlc libVlc = LibVlcFactory.factory().create();

        Logger.info("version: {}", libVlc.libvlc_get_version());
        Logger.info("compiler: {}", libVlc.libvlc_get_compiler());
        Logger.info("changeset: {}", libVlc.libvlc_get_changeset());

        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        GLWindow window = GLWindow.create(caps);
        window.setTitle("MediaPlayer Test");
        window.setSize(854, 480);
        window.setVisible(true);

        window.addGLEventListener(new MediaPlayer());
        window.addWindowListener(new QuitListener());

        GLAnimatorControl animator = new Animator(window);
        animator.start();
    }
}
