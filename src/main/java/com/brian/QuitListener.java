package com.brian;

import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.event.WindowListener;
import com.jogamp.newt.event.WindowUpdateEvent;

/**
 * Created by Brian on 10/3/2014.
 */
public class QuitListener implements WindowListener {
    @Override
    public void windowResized(WindowEvent e) {

    }

    @Override
    public void windowMoved(WindowEvent e) {

    }

    @Override
    public void windowDestroyNotify(WindowEvent e) {

    }

    @Override
    public void windowDestroyed(WindowEvent e) {
        System.exit(0);
    }

    @Override
    public void windowGainedFocus(WindowEvent e) {

    }

    @Override
    public void windowLostFocus(WindowEvent e) {

    }

    @Override
    public void windowRepaint(WindowUpdateEvent e) {

    }
}
