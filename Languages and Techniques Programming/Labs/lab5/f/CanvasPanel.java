import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class CanvasPanel extends JPanel {
    private int radius = 3;
    private int width = 30;
    private int height = width / 2;
    private boolean color = false;

    public void setColor(boolean c) {
        if(color != c) {
            color = c;
            repaint();
        }
    }

    public void setRadius(int r) {
        radius = r;
        repaint();
    }


    public Polygon makeTopBrick(int leftPointX, int leftPointY) {
        int[] xPoints = new int[]{leftPointX, leftPointX + height, leftPointX + height + width, leftPointX + width};
        int[] yPoints = new int[]{leftPointY, leftPointY - height, leftPointY - height, leftPointY};
        return new Polygon(xPoints, yPoints, 4);
    }

    public Polygon makeSideBrick(int leftPointX, int leftPointY) {
        int[] xPoints = new int[]{leftPointX, leftPointX, leftPointX + height, leftPointX + height};
        int[] yPoints = new int[]{leftPointY, leftPointY - width, leftPointY - width - height, leftPointY - height};
        return new Polygon(xPoints, yPoints, 4);
    }

    public Polygon makeFrontBrick(int leftPointX, int leftPointY) {
        int[] xPoints = new int[]{leftPointX, leftPointX, leftPointX + width, leftPointX + width};
        int[] yPoints = new int[]{leftPointY - width, leftPointY, leftPointY, leftPointY - width};
        return new Polygon(xPoints, yPoints, 4);
    }

    @Override
    protected void paintComponent(Graphics g) {
        int spaceX = width;
        super.paintComponent(g);
        int spaceY = width * radius / 2 + width;
        g.setColor(Color.GREEN);
        Polygon brick;
        Random rand = new Random();
        Color[] colors = new Color[]{Color.YELLOW, Color.GREEN, Color.BLUE, Color.CYAN, Color.RED};
        if (color) {
            for (int i = 0; i < radius; ++i) {
                for (int j = 0; j < radius; ++j) {
                    brick = makeFrontBrick(spaceX + i * width, spaceY + width + j * width);
                    g.setColor(colors[rand.nextInt(colors.length)]);
                    g.fillPolygon(brick);
                    g.setColor(Color.BLACK);
                    g.drawPolygon(brick);
                }
            }
            for (int i = 0; i < radius; ++i) {
                for (int j = 0; j < radius; ++j) {
                    g.setColor(colors[rand.nextInt(colors.length)]);
                    brick = makeTopBrick(spaceX + i * width + j * height, spaceY - j * height);
                    g.fillPolygon(brick);
                    g.setColor(Color.BLACK);
                    g.drawPolygon(brick);
                }
            }
            spaceX += radius * width;
            spaceY += radius * width;
            for (int i = 0; i < radius; ++i) {
                for (int j = 0; j < radius; ++j) {
                    g.setColor(colors[rand.nextInt(colors.length)]);
                    brick = makeSideBrick(spaceX + i * height, spaceY - i * height - j * width);
                    g.fillPolygon(brick);
                    g.setColor(Color.BLACK);
                    g.drawPolygon(brick);
                }
            }
        } else {
            for (int i = 0; i < radius; ++i) {
                for (int j = 0; j < radius; ++j) {
                    brick = makeFrontBrick(spaceX + i * width, spaceY + width + j * width);
                    g.drawPolygon(brick);
                }
            }
            for (int i = 0; i < radius; ++i) {
                for (int j = 0; j < radius; ++j) {
                    brick = makeTopBrick(spaceX + i * width + j * height, spaceY - j * height);
                    g.drawPolygon(brick);
                }
            }
            spaceX += radius * width;
            spaceY += radius * width;
            for (int i = 0; i < radius; ++i) {
                for (int j = 0; j < radius; ++j) {
                    brick = makeSideBrick(spaceX + i * height, spaceY - i * height - j * width);
                    g.drawPolygon(brick);
                }

            }
        }
    }
}
