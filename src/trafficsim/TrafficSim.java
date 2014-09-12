/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficsim;

import java.awt.Point;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

/**
 * This is the main class for the project
 *
 * @author Magnus Hu
 */
public class TrafficSim {

	Terrain map = new Terrain();
	Robot[] prototype = new Robot[20];
	//Robot prototype = new Robot(400, 400, 0, map);
	static boolean redlight = false;
	int size = 6;
	
	
	public void start() throws InterruptedException {
		try {
			Display.setDisplayMode(new DisplayMode(800, 800));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		// init OpenGL here
		ArrayList<Point> track = map.getTrack();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 800, 0, 800, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		long lastTime = getTime();
		int prototypeX;
		int prototypeY;
		for (int i = 0; i < prototype.length; i++) {
			prototype[i] = new Robot(800 * Math.random(), 800 * Math.random(), 2 * Math.PI * Math.random() - Math.PI, map);

		}

		while (!Display.isCloseRequested()) {
			if(Keyboard.isKeyDown(Keyboard.KEY_X)){
				GL11.glColor3f(1f, 0.1f, 0.1f);
				redlight = true;
			}
			else{
				GL11.glColor3f(0.1f, 1f, 0.1f);
				redlight = false;
			}
			// render OpenGL here
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			
			//square
			GL11.glBegin(GL11.GL_QUADS);
	        GL11.glVertex2f(50,600);
	        GL11.glVertex2f(50+50,600);
	        GL11.glVertex2f(50+50,600+50);
	        GL11.glVertex2f(50,600+50);
	        GL11.glEnd();
	        
			for (int i = 0; i < track.size(); i++) {
				GL11.glBegin(GL11.GL_LINES);
				GL11.glVertex2d(track.get(i).getX(), track.get(i).getY());
				GL11.glVertex2d(track.get((i + 1) % track.size()).getX(), track.get((i + 1) % track.size()).getY());
				GL11.glEnd();

			}
			long delta = getTime() - lastTime;
			//Iterate over robots
//            for (int i = 0; i < track.size(); i++) {
//                Point point = track.get(i);
//                
//            }
			for (int i = 0; i < prototype.length; i++) {
				prototype[i].move(delta);
				prototypeX = (int) prototype[i].Xpos;
				prototypeY = (int) prototype[i].Ypos;
				double a = prototype[i].currentAngle;
				GL11.glColor3f(0.3f, 0.5f, 2f);
				GL11.glBegin(GL11.GL_QUADS);
				GL11.glVertex2d(prototypeX + size * Math.cos(a) - 1 * Math.sin(a),
						prototypeY + size * Math.sin(a) + 1 * Math.cos(a));
				GL11.glVertex2d(prototypeX + size * Math.cos(a) + 1 * Math.sin(a),
						prototypeY + size * Math.sin(a) - 1 * Math.cos(a));
				GL11.glVertex2d(prototypeX - size * Math.cos(a) + 3 * Math.sin(a),
						prototypeY - size * Math.sin(a) - size * Math.cos(a));
				GL11.glVertex2d(prototypeX - size * Math.cos(a) - size * Math.sin(a),
						prototypeY - size * Math.sin(a) + size * Math.cos(a));
				GL11.glEnd();
			}
			lastTime = getTime();
			Thread.sleep(16);
			Display.update();
			if(Keyboard.isKeyDown(Keyboard.KEY_Q))
				System.exit(0);
		}
		
		Display.destroy();
	}

	public long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	public static void main(String[] args) {
		// TODO code application logic here

		TrafficSim sim = new TrafficSim();
		try {
			sim.start();
		} catch (InterruptedException ex) {
			Logger.getLogger(TrafficSim.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}