package me.ci.sarica.terminal.history;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;

public class LineGraph
{
    private final LinkedList<Float> values = new LinkedList<Float>();
    private final JFrame frame;
    private final Renderer renderer;
    private float min;
    private float max;

	public LineGraph(String title)
	{
		this(title, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY);
	}

    public LineGraph(String title, float min, float max)
    {
		this.min = min;
		this.max = max;

        frame = new JFrame();
        frame.setTitle(title);
        frame.setResizable(true);
        frame.setSize(400, 300);
        frame.add(renderer = new Renderer());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void addValue(float val)
    {
        synchronized (values)
        {
            values.addFirst(val);
            min = Math.min(min, val);
            max = Math.max(max, val);
        }
        renderer.repaint();
    }

    private class Renderer extends JPanel
    {
		private static final long serialVersionUID = 32908450;

        @Override
        public void paint(Graphics g1)
        {
            Graphics2D g = (Graphics2D)g1;

            int width = getWidth();
            int height = getHeight();

            float min, max;
            int count;
            synchronized (values)
            {
                min = LineGraph.this.min;
                max = LineGraph.this.max;
                count = values.size();
            }

            g.setColor(Color.white);
            g.fillRect(0, 0, width, height);

            if (count > 1)
            {
                int sideBuffer = 80;
                int topBuffer = 20;
                int bottomBuffer = 40;

                // Render vertical values
                {
                    int vertCuts = 5;

                    float valY = height - bottomBuffer - topBuffer;
                    float space = valY / vertCuts;

                    g.setColor(Color.lightGray);
                    for (int i = 0; i <= vertCuts; i++)
                    {
                        int y = Math.round(space * i + topBuffer);
                        g.drawLine(0, y, width, y);
                    }

                    g.setColor(Color.black);
                    for (int i = 0; i <= vertCuts; i++)
                    {
                        int y = Math.round(space * (vertCuts - i) + topBuffer) + 12;
                        g.drawString(String.format("%.03f", (max - min) * (i / (float)vertCuts) + min), 4, y);
                    }
                }

                // Render horizontal values
                {
                    int c = count;
                    int iS = 1;
                    while (c > 20)
                    {
                        c /= 2;
                        iS *= 2;
                    }

                    float valX = width - sideBuffer;
                    float space = valX / c;

                    g.setColor(Color.lightGray);
                    for (int i = 0; i < c; i++)
                        g.drawLine((int)(space * i + sideBuffer), 0, (int)(space * i + sideBuffer), height);

                    g.setColor(Color.black);
                    for (int i = 0; i < c; i++)
                        g.drawString(String.valueOf(i * iS), space * i + sideBuffer + 2, height - 3);
                }

                // Render line graph
                {
                    g.setColor(Color.red);
                    synchronized (values)
                    {
                        int lX = 0;
                        int lY = 0;

                        int c = count;
                        int jump = 1;
                        while (c > 2000)
                        {
                            c /= 2;
                            jump *= 2;
                        }

                        Iterator<Float> itr = values.iterator();

                        float avg;
                        for (int i = 0; i < count; i += jump)
                        {
                            avg = 0f;
                            if (i + jump >= count)
                            {
                                int left = count - i;
                                for (int j = 0; j < left; j++)
                                    avg += itr.next();
                                avg /= left;
                            }
                            else
                            {
                                for (int j = 0; j < jump; j++)
                                    avg += itr.next();
                                avg /= jump;
                            }

                            int x = Math.round((1f - ((float)i / count)) * (width - sideBuffer) + sideBuffer - 1f / count * (width - sideBuffer));
                            int y = Math.round((1f - (avg - min) / (max - min)) * (height - bottomBuffer -
                                                                                          topBuffer) + topBuffer);
                            if (i > 0)
                                g.drawLine(lX, lY, x, y);

                            lX = x;
                            lY = y;
                        }
                    }
                }
            }

            g.dispose();
        }
    }
}
