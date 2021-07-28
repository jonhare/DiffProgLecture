package uk.ac.soton.ecs.jsh2.diffprog.utils;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLayeredPane;
import javax.swing.JSplitPane;

import org.openimaj.content.slideshow.PictureSlide;
import org.openimaj.content.slideshow.Slide;
import org.openimaj.content.slideshow.SlideshowApplication;

import uk.ac.soton.ecs.jsh2.diffprog.App;

public class WrapperSlide extends PictureSlide {
	private Slide content;

	public WrapperSlide(URL image, Slide content) throws IOException {
		super(image);
		this.content = content;
	}

	@Override
	public Component getComponent(int width, int height) throws IOException {
		final JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setPreferredSize(new Dimension(width, height));
		layeredPane.setSize(new Dimension(width, height));

		final Component bg = super.getComponent(width, height);

		final int fgh = (int) (924. / 1080. * height);
		final int fgo = (int) (118. / 1080. * height);
		final Component fg = content.getComponent(width, fgh);

		layeredPane.add(bg);
		layeredPane.add(fg);
		layeredPane.setLayer(bg, 0);
		layeredPane.setLayer(fg, 1);
		fg.setBounds(0, fgo, width, fgh);
		bg.setBounds(0, 0, width, height);

		return layeredPane;
	}

	public static void main(String[] args) throws IOException {
		final List<Slide> slides = new ArrayList<Slide>();

		slides.add(new WrapperSlide(App.class.getResource(String.format("slides/%04d.png", 32)),
				new JythonREPLConsoleSlide(JSplitPane.HORIZONTAL_SPLIT, App.class.getResource("javelin.py"),
						"javelin(theta=0.1, gamma=0.001)")));

		final GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		final Dimension size = device.getDefaultConfiguration().getBounds().getSize();

		int width, height;
		if (size.width >= 1920)
			width = 1920;
		else
			width = size.width;
		height = width * 9 / 16;

		new SlideshowApplication(slides, width, height, App.getBackground());
	}
}
