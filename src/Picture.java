import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Picture
{
    private String fileName;
    private BufferedImage bufferedImage;
    private String extension;

    public Picture(String fileName)
    {
        System.out.println("Trying to open thing");
        load(fileName);
    }

    public BufferedImage getBufferedImage() { return bufferedImage; }
    public String getFileName() { return fileName; }
    public int getWidth() { return bufferedImage.getWidth();}
    public int getHeight() { return bufferedImage.getHeight(); }

    /**
     * Method to return the pixel value as an int for the given x and y location
     * @param x the x coordinate of the pixel
     * @param y the y coordinate of the pixel
     * @return the pixel value as an integer (alpha, red, green, blue)
     */
    public int getBasicPixel(int x, int y)
    {
        return bufferedImage.getRGB(x,y);
    }

    /**
     * Method to return the pixel value as an int for the given x and y location
     * @param p the position of the pixel
     * @return the pixel value as an integer (alpha, red, green, blue)
     */
    public int getBasicPixel(Point p)
    {
        return bufferedImage.getRGB(p.getX(), p.getY());
    }

    /**
     * Method to set the value of a pixel in the picture from an int
     * @param x the x coordinate of the pixel
     * @param y the y coordinate of the pixel
     * @param rgb the new rgb value of the pixel (alpha, red, green, blue)
     */
    public void setBasicPixel(int x, int y, int rgb)
    {
        bufferedImage.setRGB(x,y,rgb);
    }

    /**
     * Method to set the value of a pixel in the picture from an int
     * @param p the position of the pixel
     * @param rgb the new rgb value of the pixel (alpha, red, green, blue)
     */
    public void setBasicPixel(Point p, int rgb)
    {
        bufferedImage.setRGB(p.getX(), p.getY(),rgb);
    }

    /**
     * Method to get a pixel object for the given x and y location
     * @param x  the x location of the pixel in the picture
     * @param y  the y location of the pixel in the picture
     * @return a Pixel object for this location
     */
    public Pixel getPixel(int x, int y)
    {
        // create the pixel object for this picture and the given x and y location
        Pixel pixel = new Pixel(this,x,y);
        return pixel;
    }

    /**
     * Method to get a pixel object for the given x and y location
     * @param p the position of the pixel in the picture
     * @return a Pixel object for this location
     */
    public Pixel getPixel(Point p)
    {
        // create the pixel object for this picture and the given x and y location
        Pixel pixel = new Pixel(this, p.getX(), p.getY());
        return pixel;
    }

    /**
     * Method to load the picture from the passed file name
     * @param fileName the file name to use to load the picture from
     * @throws IOException if the picture isn't found
     */
    public void loadOrFail(String fileName) throws IOException
    {
        // set the current picture's file name
        this.fileName = fileName;

        // set the extension
        int posDot = fileName.indexOf('.');
        if (posDot >= 0)
            this.extension = fileName.substring(posDot + 1);

        File file = new File(this.fileName);

        if (!file.canRead())
        {
            // try adding the media path
            String path = System.getProperty("user.dir") + "\\" + this.fileName;

            file = new File(path);
            if (!file.canRead())
            {
                throw new IOException(this.fileName +
                        " could not be opened. Check that you specified the path");
            }
        }

        bufferedImage = ImageIO.read(file);
    }
    /**
     * Method to read the contents of the picture from a filename
     * without throwing errors
     * @param fileName the name of the file to write the picture to
     * @return true if success else false
     */
    public boolean load(String fileName)
    {
        try {
            this.loadOrFail(fileName);
            return true;

        } catch (Exception ex) {
            System.out.println("There was an error trying to open " + fileName);
            Logger.addLog("There was an error trying to open " + fileName);
            bufferedImage = new BufferedImage(600,200,
                    BufferedImage.TYPE_INT_RGB);
            addMessage("Couldn't load " + fileName, new Font("Helvetica",Font.BOLD,16),5,100);
            return false;
        }
    }

    /**
     * Method to draw a message as a string on the buffered image
     * @param message the message to draw on the buffered image
     * @param font the font of the message to draw
     * @param xPos  the x coordinate of the leftmost point of the string
     * @param yPos  the y coordinate of the bottom of the string
     */
    public void addMessage(String message, Font font, int xPos, int yPos)
    {
        // get a graphics context to use to draw on the buffered image
        Graphics2D graphics2d = bufferedImage.createGraphics();

        graphics2d.setPaint(Options.color);

        graphics2d.setFont(font);

        // draw the message
        graphics2d.drawString(message,xPos,yPos);

    }

    /**
     * Method to draw a string at the given location on the picture
     * @param text the text to draw
     * @param xPos the center x for the text
     * @param yPos the center y for the text
     */
    public void drawString(String text, Font font, int xPos, int yPos)
    {
        int w = getDisplayWidth(text, font);
        addMessage(text,font, xPos-w/2,yPos + font.getSize()/2);
    }

    /**
     * Method to get the width of a string to be drawn
     * @param text the text to measure
     * @param font the font to measure
     */
    public int getDisplayWidth(String text, Font font)
    {
        Graphics2D graphics2d = bufferedImage.createGraphics();
        graphics2d.setFont(font);
        int width = graphics2d.getFontMetrics().stringWidth(text);
        return width;
    }

    /**
     * Method to write the contents of the picture to a file with
     * the passed name
     * @param fileName the name of the file to write the picture to
     */
    public void writeOrFail(String fileName) throws IOException
    {
        String extension = this.extension; // the default is current

        // create the file object
        File file = new File(fileName);
        File fileLoc = file.getParentFile(); // directory name

        // if there is no parent directory use the current media dir
        if (fileLoc == null)
        {
            // try adding the media path
            String path = System.getProperty("user.dir") + "\\" + fileName;
            file = new File(path);
            fileLoc = file.getParentFile();
        }

        // check that you can write to the directory
        if (!fileLoc.canWrite()) {
            throw new IOException(fileName +
                    " could not be opened. Check to see if you can write to the directory.");
        }

        // get the extension
        int posDot = fileName.indexOf('.');
        if (posDot >= 0)
            extension = fileName.substring(posDot + 1);

        // write the contents of the buffered image to the file
        ImageIO.write(bufferedImage, extension, file);

    }

    /**
     * Method to write the contents of the picture to a file with
     * the passed name without throwing errors
     * @param fileName the name of the file to write the picture to
     * @return true if success else false
     */
    public boolean write(String fileName)
    {
        try {
            this.writeOrFail(fileName);
            return true;
        } catch (Exception ex) {
            Logger.addLog("There was an error trying to write to" + fileName);
            System.out.println("There was an error trying to write to" + fileName);
            ex.printStackTrace();
            return false;
        }

    }

    /**
     * Method to return a string with information about this picture
     * @return a string with information about the picture
     */
    public String toString()
    {
        String output = "Simple Picture, filename " + fileName +
                " height " + getHeight() + " width " + getWidth();
        return output;
    }

    /**
     * Method to return the top left corner of the label position
     * @return a ScorePoint with top left corner of the label position and the score
     */
    public ScorePoint getBestBoxPosition(int w, int h) {
        ScoreBoxes scoreBoxes = new ScoreBoxes(this, w, h);
        return scoreBoxes.getBestBoxLocation();
    }

    public ScorePoint getBestBoxPosition(Dimension d) {
        return getBestBoxPosition(d.w, d.h);
    }

    public boolean writeLabel() {
        Font font = Options.font;
        String text = Options.text;
        String[] strings = new String[] {text};
        if (Options.newLine) {
            strings = text.split("\\s\\s+");
        }
        Dimension boxSize = getBoxSize(strings, font, Options.padXScale, Options.padYScale);
        if (!boxSize.fitsInside(getWidth(), getHeight())) {
            System.out.println("Label doesn't fit. Shrinking font size...");
            // TODO log?
            Options.shrinkFontSizeToFit(this, strings);
            font = Options.font;
            boxSize = getBoxSize(strings, font, Options.padXScale, Options.padYScale);
        }

        int heightOffset = 0;
        if (Options.useHeightOffset){
            FontMetrics fm = bufferedImage.getGraphics().getFontMetrics(font);
            heightOffset = font.getSize() - fm.getAscent() + fm.getDescent();
        }
        if (Options.centerLabel) {
            drawCenteredPaddedLabel(strings, font, boxSize, Options.padYScale);
            return true;
        }
        boxSize.h -= heightOffset;
        ScorePoint sp = getBestBoxPosition(boxSize);
        if (Options.debug) {
            drawBox(sp.getP().getX(), sp.getP().getY(), boxSize.w, boxSize.h);
            System.out.println(sp.getScore());
        }
        sp.getP().translate(0, -heightOffset);
        drawPaddedLabel(strings, font, sp.getP(), boxSize, Options.padYScale);
        return true;
   }

    private void drawBox(int x, int y, int w, int h) {
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                getPixel(x+i,y+j).setBlue(0);
                getPixel(x+i,y+j).setRed(255);
                getPixel(x+i,y+j).setGreen(0);
            }
        }
    }

    private void drawPaddedLabel(String[] strings, Font font, Point p, Dimension d, double padYScale){
        int origH = font.getSize();
        int drawX = p.getX() + d.w / 2;
        int drawTop = p.getY();
        int yPadding = (int) ((origH * padYScale) - origH);
        int drawFirstY = drawTop + yPadding/2 + origH/2;
        for (int i = 0; i < strings.length; i++) {
            drawString(strings[i], font, drawX, drawFirstY + i*origH);
        }
    }

    private void drawCenteredPaddedLabel(String[] strings, Font font, Dimension d, double padYScale){
        int origH = font.getSize();
        int drawX = this.getWidth()/2;
        int drawTop = this.getHeight()/2 - d.h/2;
        int yPadding = (int) ((origH * padYScale) - origH);
        int drawFirstY = drawTop + yPadding/2 + origH/2;
        for (int i = 0; i < strings.length; i++) {
            drawString(strings[i], font, drawX, drawFirstY + i*origH);
        }
    }

    public Dimension getBoxSize(String[] strings, Font font, double padXScale, double padYScale) {
        int maxW = 0;
        String longestStr = "";
        for (String s : strings){
            int w = getDisplayWidth(s, font);
            if (w > maxW){
                longestStr = s;
                maxW = w;
            }
        }
        int w = (int) (padXScale * getDisplayWidth(longestStr, font));
        int origH = font.getSize();
        int yPadding = (int) ((origH * padYScale) - origH);
        int h = origH * strings.length + yPadding;

        return new Dimension(w, h);
    }
}