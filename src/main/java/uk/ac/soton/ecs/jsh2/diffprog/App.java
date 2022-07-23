package uk.ac.soton.ecs.jsh2.diffprog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JSplitPane;

import org.openimaj.content.slideshow.PictureSlide;
import org.openimaj.content.slideshow.Slide;
import org.openimaj.content.slideshow.SlideshowApplication;
import org.openimaj.video.VideoDisplay.EndAction;

import uk.ac.soton.ecs.jsh2.diffprog.utils.JythonREPLConsoleSlide;
import uk.ac.soton.ecs.jsh2.diffprog.utils.ScaledVideoSlide;
import uk.ac.soton.ecs.jsh2.diffprog.utils.WrapperSlide;

public class App {
	private static BufferedImage background = null;
	static int SLIDE_WIDTH;
	static int SLIDE_HEIGHT;

	static {
		final GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		final Dimension size = device.getDefaultConfiguration().getBounds().getSize();

		// if (size.width >= 1680)
		// 	SLIDE_WIDTH = 1680;
		if (size.width >= 1920)
			SLIDE_WIDTH = 1920;
		else
			SLIDE_WIDTH = size.width;
		SLIDE_HEIGHT = SLIDE_WIDTH * 9 / 16;
	}

	/**
	 * Main method
	 *
	 * @param args
	 *            ignored
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		final List<Slide> slides = new ArrayList<Slide>();

		for (int i = 0; i <= 58; i++)
			slides.add(new PictureSlide(App.class.getResource(String.format("slides/%04d.png", i))));

		slides.set(30, new WrapperSlide(App.class.getResource(String.format("slides/%04d.png", 30)),
				new JythonREPLConsoleSlide(JSplitPane.HORIZONTAL_SPLIT, App.class.getResource("javelin.py"),
						"javelin(theta=0.1, gamma=0.001)")));

		slides.set(50, new WrapperSlide(App.class.getResource(String.format("slides/%04d.png", 50)),
				new ScaledVideoSlide(App.class.getResource("alphastar.mp4"), true, EndAction.CLOSE_AT_END)));

		try {
			// final FaceNetDemo fn = new FaceNetDemo("FaceTime");
			// slides.set(59, new WrapperSlide(App.class.getResource(String.format("slides/%04d.png", 59)), fn));

			final RetinaNetDemo rn = new RetinaNetDemo("FaceTime");
			slides.set(52, new WrapperSlide(App.class.getResource(String.format("slides/%04d.png", 52)), rn));
		} catch (final Exception e) {
			// ignore; probably haven't got python configured
		}

		slides.set(55, new WrapperSlide(App.class.getResource(String.format("slides/%04d.png", 55)),
				new ScaledVideoSlide(App.class.getResource("animation.mp4"), false, EndAction.CLOSE_AT_END)));

		new SlideshowApplication(slides, SLIDE_WIDTH, SLIDE_HEIGHT, getBackground());
	}

	/**
	 * @return the generic slide background
	 */
	public synchronized static BufferedImage getBackground() {
		if (background == null) {
			background = new BufferedImage(SLIDE_WIDTH, SLIDE_HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
			final Graphics2D g = background.createGraphics();
			g.setColor(Color.WHITE);
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g.fillRect(0, 0, background.getWidth(), background.getHeight());
		}
		return background;
	}

	public static int getVideoWidth(int remainder) {
		final int avail = SLIDE_WIDTH - remainder;
		if (avail >= 640)
			return 640;
		return 320;
	}

	public static int getVideoHeight(int remainder) {
		final int width = getVideoWidth(remainder);
		switch (width) {
		case 640:
			return 480;
		}
		return 240;
	}
}
