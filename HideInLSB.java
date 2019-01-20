import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class HideInLSB
{
    public static void main(String[] args)
    {
        try
        {
            /*System.out.println(getMaxStorableData("dslr.JPG"));
            System.out.println(getMaxAlphaRequired("dslr.JPG"));
            System.out.println(getAlphaGap("dslr.JPG"));*/
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
        hideData("Hii Jay!Hii manthan133", "img1.jpg");
        String secret= retrieveData("answer_Signify.png");
        System.out.println("hidden msg:"+secret);
    }
    
    /* to retrieve Data  from signified image */ 
    private static String retrieveData(String path)
    {
        BufferedImage img = null;
        byte[] nibbles = new byte[getLength(path)];
        String secret = null;
        long textLength=nibbles.length;
        long fullPixels = (textLength/3)*3;
        long remainingPixels = textLength%3;
        int currentNibble=0,currentPixel=0;

        try
        {
            img = ImageIO.read(new File(path));
            while(currentNibble<fullPixels)
            {
                Color pixel1 = new Color(img.getRGB( currentPixel/img.getWidth() ,currentPixel%img.getWidth()));
                //System.out.println(pixel1.getRed()+" "+pixel1.getGreen()+" "+pixel1.getBlue()+" "+pixel1.getAlpha());

                int red = pixel1.getRed();
                int green= pixel1.getGreen();
                int blue = pixel1.getBlue();

                nibbles[currentNibble++] = (byte)(red & 15) ;
                // System.out.print(nibbles[currentNibble-1]);

                nibbles[currentNibble++] = (byte)(green & 15);
                // System.out.print(nibbles[currentNibble-1]); 

                nibbles[currentNibble++] = (byte)(blue & 15);
                // System.out.print(nibbles[currentNibble-1]);

                currentPixel++;
            }
            if(remainingPixels > 0)
            {
                Color pixel1 = new Color(img.getRGB( currentPixel/img.getWidth() ,currentPixel%img.getWidth()));

                int red = pixel1.getRed();
                nibbles[currentNibble++] = (byte)(red & 15) ;

            }
            if( remainingPixels==2)
            {
                Color pixel1 = new Color(img.getRGB( currentPixel/img.getWidth() ,currentPixel%img.getWidth()));

                int green = pixel1.getGreen();
                nibbles[currentNibble++] = (byte)(green & 15);

            }

            secret=getStringFromNibbles(nibbles);

        }
        catch(Exception e)
        {

        }        
        return secret;
    }

    /* To hide data into image*/
    public static void hideData(String text, String path)
    {
        
        byte[] nibbles = getNibbles(text);

        // System.out.println(nibbles.toString());
        BufferedImage img = null;
        int textLength=nibbles.length;
        //System.out.println(textLength);
        long fullPixels = (textLength/3)*3;
        long remainingPixels = textLength%3;

        int currentNibble=0,currentPixel=0;
        hideProperties(textLength,path);
        try 
        {
            img = ImageIO.read(new File(path));
            //img.getHeight()*img.getWidth()
            while(currentNibble < fullPixels)
            {
                //System.out.println(img.getHeight()+" "+img.getWidth());
                Color pixel1 = new Color(img.getRGB( currentPixel/img.getWidth() ,currentPixel%img.getWidth()));
                //Color pixel2 = new Color(img.getRGB( p/img.getWidth() ,p%img.getWidth()));

                //System.out.println(pixel1.getRed()+" "+pixel1.getGreen()+" "+pixel1.getBlue()+" "+pixel1.getAlpha());

                int red = pixel1.getRed();
                int green= pixel1.getGreen();
                int blue = pixel1.getBlue();
                
                red = (red & 240) | nibbles[currentNibble++];
                // System.out.println(nibbles[currentNibble-1] +" "+ red);
                green = (green & 240) | nibbles[currentNibble++];
                // System.out.println(nibbles[currentNibble-1] +" "+ green);
                blue = (blue & 240) | nibbles[currentNibble++];
                // System.out.println(nibbles[currentNibble-1] +" "+ blue);

                int rgb = (pixel1.getAlpha()<<24 | red << 16 | green << 8 | blue);
                img.setRGB(currentPixel/img.getWidth() ,currentPixel%img.getWidth(),rgb);
                // Color temp = new Color(img.getRGB( currentPixel/img.getWidth() ,currentPixel%img.getWidth()));
                // System.out.println(temp.getRed()+" "+temp.getGreen()+" "+temp.getBlue()+" "+temp.getAlpha());
                 
                currentPixel++;

            }
            if(remainingPixels > 0)
            {
                Color pixel1 = new Color(img.getRGB( currentPixel/img.getWidth() ,currentPixel%img.getWidth()));

                int red = pixel1.getRed();
                red = (red & 240) | nibbles[currentNibble++];

                int rgb = (red << 16 | pixel1.getGreen() << 8 | pixel1.getBlue());
                img.setRGB(currentPixel/img.getWidth() ,currentPixel%img.getWidth(),rgb);
            }
            if( remainingPixels==2)
            {
                Color pixel1 = new Color(img.getRGB( currentPixel/img.getWidth() ,currentPixel%img.getWidth()));

                int green = pixel1.getGreen();
                green = (green & 240) | nibbles[currentNibble++];

                int rgb = (pixel1.getRed() << 16 | green << 8 | pixel1.getBlue());
                img.setRGB(currentPixel/img.getWidth() ,currentPixel%img.getWidth(),rgb);
            }

            //hideProperties(textLength,path);

            String finalPath = "answer_Signify.png"; //_Signify
            File outputfile = new File(finalPath);
            ImageIO.write(img, "png", outputfile);
            
            getProperty("new_answer_Signify.png");

            System.out.println("Done!");
        } 
        catch (IOException e) 
        {
        }
    }
    
    public static byte[] getNibbles(String text)
    {
        byte[] bytes = text.getBytes();
        byte[] nibbles = new byte[bytes.length*2];
        
        int len = bytes.length;
        int k=0;
        
        for(int i=0;i<len;i++)
        {
            nibbles[k++]=(byte)(bytes[i]>>4);
            //System.out.print(nibbles[k-1]);
            nibbles[k++]=(byte)(bytes[i]&15);
            //System.out.print(nibbles[k-1]);
        }
        
        //System.out.println(k);
        
        return nibbles;
    }

    private static String getStringFromNibbles(byte[] nibbles)
    {
        int len=nibbles.length;
        System.out.println(len);

        byte[] bytes=new byte[len/2];
        int k=0,p=0;
        // System.out.println(nibbles[k]);

        while(k<len)
        {
            bytes[p++]=(byte)(nibbles[k++]<<4 | nibbles[k++]);
            //System.out.print(bytes[p-1]);
        }
        String str=new String(bytes);
        // System.out.print(str);
        return str;
    }

    //Hide Signature and Length
    public static void hideProperties(int length,String path)
    {
        BufferedImage img = null;
        
        String sign = "signify!";
        String reverse = new StringBuilder(sign).reverse().toString();

        String property = sign+String.valueOf(length)+reverse;
        byte[] nibbles = getNibbles(property);
        
        System.out.println(property);

        try 
        {
            BufferedImage bi = ImageIO.read(new File(path));

            img = new BufferedImage(bi.getWidth(),bi.getHeight(),BufferedImage.TYPE_INT_ARGB);

            for(int i=0;i<bi.getHeight();i++)
            {
                for(int j=0;j<bi.getWidth();j++)
                {
                    img.setRGB(i, j,bi.getRGB(i,j));
                }
            }

            int totalPixles = img.getHeight()*img.getWidth();
            int currentPixle;
            int pixleGap = getAlphaGap(path);
            int currentNibble=0;

            for(currentPixle=0;currentPixle<totalPixles;currentPixle+=pixleGap)
            {
                int pixle = img.getRGB( currentPixle/img.getWidth() ,currentPixle%img.getWidth());
                System.out.print(Integer.toBinaryString(pixle)+" (1--> ");

                pixle = pixle & 0xf0ffffff; //0x00ffffff
                System.out.print(Integer.toBinaryString(pixle)+" (2--> ");

                pixle |= nibbles[currentNibble++]<<24;
                System.out.print(Integer.toBinaryString(pixle)+" (3--> ");

                img.setRGB(currentPixle/img.getWidth() ,currentPixle%img.getWidth(),pixle);
                pixle = (img.getRGB( currentPixle/img.getWidth() ,currentPixle%img.getWidth()));
                System.out.print(Integer.toBinaryString(pixle)+" final \n");    
            }

            String finalPath = "new_answer_Signify.png"; //_Signify
            File outputfile = new File(finalPath);
            ImageIO.write(img, "png", outputfile);
        }
        catch(Exception e)
        {

        }
    }

    public static int getLength(String path)
    {
        String[] properties = getProperty(path).split("!");
        
        System.out.println(getProperty(path)+" "+path);
        return Integer.parseInt(properties[1]);
    }

    public static String getProperty(String path)
    {       
        BufferedImage img = null;
        String property="";

        try
        {
            img = ImageIO.read(new File(path));
            int totalPixles = img.getHeight()*img.getWidth();
            int currentPixle;
            int pixleGap = getAlphaGap(path);
            ArrayList<Byte> append = new ArrayList(); 

            for(currentPixle=0;currentPixle<totalPixles;currentPixle+=pixleGap)
            {
                int pixle = img.getRGB( currentPixle/img.getWidth() ,currentPixle%img.getWidth());
                /*int alpha1 = pixle.getAlpha();
                System.out.print(alpha1+" "+ path+" " );

                currentPixle+=pixleGap;
                pixle = new Color(img.getRGB( currentPixle/img.getWidth(),currentPixle%img.getWidth()));
                int alpha2 = pixle.getAlpha();
                System.out.print(alpha2+" " );

                append.add(new Byte((byte)( (byte)( (alpha1&15)<<4) | (byte)(alpha2&15) ) ));*/
                System.out.print(Integer.toBinaryString(pixle)+" ");
            }

            byte[] bytes = new byte[append.size()];
            for(int i=0;i<append.size();i++)
                bytes[i]=append.get(i).byteValue();    

            property = new String(bytes);
            System.out.println("correct! "+property);
        }
        catch(Exception e)
        {
            //System.out.println("getProperty: "+e);
        }

        return property;
    }
    //Max data that can be hidden in the image
    public static long getMaxStorableData(String path) throws IOException
    {
        BufferedImage img = null;
        img = ImageIO.read(new File(path));
        
        long maxStorableData = (long)Math.floor((img.getHeight()*img.getWidth()*(12.0/32)));
        
        return maxStorableData;
    }

    //No of pixles alpha nibbles required to store length and signature of the data
    public static int getMaxAlphaRequired(String path) throws IOException
    {
        int signatureAlpha = 32; //signify! + !fyingis
        return (int)(Math.ceil(Math.log(getMaxStorableData(path))/(Math.log(2)*4)))+signatureAlpha;
    }

    //How much gap can be kept between two aplha nibbles which shows length. i.e so that we don't have to store in contigous manner!
    public static int getAlphaGap(String path) throws IOException
    {
	    BufferedImage img = null;
        img = ImageIO.read(new File(path));

        return Math.floorDiv(img.getWidth()*img.getHeight(),getMaxAlphaRequired(path));
    }
}