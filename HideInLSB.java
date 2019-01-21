import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class HideInLSB
{
    public static void main(String[] args) throws IOException
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
        
        String path="img1.jpg";
        BufferedImage original = ImageIO.read(new File(path));
        BufferedImage signified = new BufferedImage(original.getWidth(),original.getHeight(),BufferedImage.TYPE_INT_ARGB);
        
        for(int i=0;i<original.getHeight();i++)
            for(int j=0;j<original.getWidth();j++)
                signified.setRGB(i, j,original.getRGB(i,j));
        
        hideData("Yipee! message retrieved successfully!!Yipee! message retrieved successfully!!Yipee! message retrieved successfully!!Yipee! message retrieved successfully!!Yipee! message retrieved successfully!!Yipee! message retrieved successfully!!Yipee! message retrieved successfully!!Yipee! message retrieved successfully!!Yipee! message retrieved successfully!!Yipee! message retrieved successfully!!Yipee! message retrieved successfully!!Yipee! message retrieved successfully!!Yipee! message retrieved successfully!!Yipee! message retrieved successfully!!Yipee! message retrieved successfully!!Yipee! message retrieved successfully!!Yipee! message retrieved successfully!!Yipee! message retrieved successfully!!Yipee! message retrieved successfully!!Yipee! message retrieved successfully!!Yipee! message retrieved successfully!!Yipee! message retrieved successfully!!Yipee! message retrieved successfully!!Yipee! message retrieved successfully!!Yipee! message retrieved successfully!!Yipee! message retrieved successfully!!Yipee! message retrieved successfully!!Yipee! message retrieved successfully!!Yipee! message retrieved successfully!!Yipee! message retrieved successfully!!Yipee! message retrieved successfully!!Yipee! message retrieved successfully!!Yipee! message retrieved successfully!!",signified);
        getProperty(signified);
        String secret= retrieveData(signified);
        System.out.println("hidden msg:"+secret);

        System.out.println("MAX: "+getMaxStorableData(signified)+" Alpha: "+getMaxAlphaRequired(signified)+" GAP: "+getAlphaGap(signified));
    }
    
    private static boolean isSignified(BufferedImage signified)
    {
        String[] properties = getProperty(signified).split("!");
        String initialSign = "signify";
        String finalSign = "yfingis";

        return (properties[0].equals(initialSign) && properties[2].substring(0,finalSign.length()).equals(finalSign));
    } 

    /* to retrieve Data  from signified image */ 
    private static String retrieveData(BufferedImage signified)
    {
        System.out.println(isSignified(signified));
        if(!isSignified(signified))
        {
            System.out.println("Not Signified!");
            return "";
        }

        byte[] nibbles = new byte[getLength(signified)];
        String secret = null;
        long textLength=nibbles.length;
        long fullPixels = (textLength/3)*3;
        long remainingPixels = textLength%3;
        int currentNibble=0,currentPixel=0;

        try
        {
            while(currentNibble<fullPixels)
            {
                Color pixel1 = new Color(signified.getRGB( currentPixel/signified.getWidth() ,currentPixel%signified.getWidth()));
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
                Color pixel1 = new Color(signified.getRGB( currentPixel/signified.getWidth() ,currentPixel%signified.getWidth()));

                int red = pixel1.getRed();
                nibbles[currentNibble++] = (byte)(red & 15) ;

            }
            if( remainingPixels==2)
            {
                Color pixel1 = new Color(signified.getRGB( currentPixel/signified.getWidth() ,currentPixel%signified.getWidth()));

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
    public static void hideData(String text,BufferedImage signified)
    {
        
        byte[] nibbles = getNibbles(text);

        // System.out.println(nibbles.toString());
        int textLength=nibbles.length;
        //System.out.println(textLength);
        long fullPixels = (textLength/3)*3;
        long remainingPixels = textLength%3;

        int currentNibble=0,currentPixel=0;
        hideProperties(textLength,signified);
        try 
        {
            //img.getHeight()*img.getWidth()
            while(currentNibble < fullPixels)
            {
                //System.out.println(img.getHeight()+" "+img.getWidth());
                Color pixel1 = new Color(signified.getRGB( currentPixel/signified.getWidth() ,currentPixel%signified.getWidth()));
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
                signified.setRGB(currentPixel/signified.getWidth() ,currentPixel%signified.getWidth(),rgb);
                // Color temp = new Color(img.getRGB( currentPixel/img.getWidth() ,currentPixel%img.getWidth()));
                // System.out.println(temp.getRed()+" "+temp.getGreen()+" "+temp.getBlue()+" "+temp.getAlpha());
                 
                currentPixel++;

            }
            if(remainingPixels > 0)
            {
                Color pixel1 = new Color(signified.getRGB( currentPixel/signified.getWidth() ,currentPixel%signified.getWidth()));

                int red = pixel1.getRed();
                red = (red & 240) | nibbles[currentNibble++];

                int rgb = (red << 16 | pixel1.getGreen() << 8 | pixel1.getBlue());
                signified.setRGB(currentPixel/signified.getWidth() ,currentPixel%signified.getWidth(),rgb);
            }
            if( remainingPixels==2)
            {
                Color pixel1 = new Color(signified.getRGB( currentPixel/signified.getWidth() ,currentPixel%signified.getWidth()));

                int green = pixel1.getGreen();
                green = (green & 240) | nibbles[currentNibble++];

                int rgb = (pixel1.getRed() << 16 | green << 8 | pixel1.getBlue());
                signified.setRGB(currentPixel/signified.getWidth() ,currentPixel%signified.getWidth(),rgb);
            }

            hideProperties(textLength,signified);

            String finalPath = "answer_Signify.png"; //_Signify
            File outputfile = new File(finalPath);
            ImageIO.write(signified, "png", outputfile);
            
            getProperty(signified);

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
    public static void hideProperties(int length,BufferedImage signified)
    {
        String sign = "signify!";
        String reverse = new StringBuilder(sign).reverse().toString();

        String property = sign+String.valueOf(length)+reverse;
        byte[] nibbles = getNibbles(property);
        
        try 
        {
            int totalPixles = signified.getHeight()*signified.getWidth();
            int currentPixle;
            int pixleGap = getAlphaGap(signified);
            int currentNibble=0;

            for(currentPixle=0;currentPixle<totalPixles;currentPixle+=pixleGap)
            {
                int pixle = signified.getRGB( currentPixle/signified.getWidth() ,currentPixle%signified.getWidth());
                System.out.print(Integer.toBinaryString(pixle)+" (1--> ");

                pixle = pixle & 0xf0ffffff; //0x00ffffff
                System.out.print(Integer.toBinaryString(pixle)+" (2--> ");

                pixle |= nibbles[currentNibble++]<<24;
                System.out.print(Integer.toBinaryString(pixle)+" (3--> ");

                signified.setRGB(currentPixle/signified.getWidth() ,currentPixle%signified.getWidth(),pixle);
                pixle = (signified.getRGB( currentPixle/signified.getWidth() ,currentPixle%signified.getWidth()));
                System.out.print(Integer.toBinaryString(pixle)+" final \n");    
            }
        }
        catch(Exception e)
        {

        }
    }

    public static int getLength(BufferedImage signified)
    {
        String[] properties = getProperty(signified).split("!");
        
        System.out.println(getProperty(signified));
        return Integer.parseInt(properties[1]);

        //System.out.println("Here property "+getProperty(signified));
        //return 78; 
    }

    public static String getProperty(BufferedImage signified)
    {       
        String property="";

        try
        {
            int totalPixles = signified.getHeight()*signified.getWidth();
            int currentPixle;
            int pixleGap = getAlphaGap(signified);
            ArrayList<Byte> append = new ArrayList(); 

            for(currentPixle=0;currentPixle<totalPixles;currentPixle+=pixleGap)
            {
                System.out.println("current pixle: "+currentPixle+","+currentPixle/signified.getWidth()+","+currentPixle%signified.getWidth());
                int pixle = signified.getRGB( currentPixle/signified.getWidth() ,currentPixle%signified.getWidth());
                //byte alpha1 = (byte)((pixle & 0x0f000000)>>>16);
                byte alpha1 = (byte)((pixle & 251658240)>>>20);
                //System.out.print("pixle "+Integer.toBinaryString(pixle)+" ");
                //System.out.print(Integer.toBinaryString(alpha1)+" and ");              
                currentPixle+=pixleGap;

                if(currentPixle>=totalPixles) 
                break;

                pixle = signified.getRGB( currentPixle/signified.getWidth() ,currentPixle%signified.getWidth());
                //System.out.print("for pixle "+Integer.toBinaryString(pixle)+" ");
                //byte alpha2 = (byte)((pixle & 0x0f000000)>>>24);
                byte alpha2 = (byte)((pixle & 251658240)>>>24);
                //System.out.print(Integer.toBinaryString(alpha2)+" gives ");

                append.add(new Byte((byte)(alpha1|alpha2)));
                //System.out.println(append.get(append.size()-1));
            }
            

            byte[] bytes = new byte[append.size()];
            for(int i=0;i<append.size();i++)
                bytes[i]=append.get(i).byteValue();    

            property = new String(bytes);
            System.out.println("correct! "+property);
            System.out.flush();
        }
        catch(Exception e)
        {
            System.out.println("getProperty: "+e);
            System.out.println(signified.getHeight()+","+signified.getWidth());
        }

        return property;
    }
    //Max data that can be hidden in the image
    public static long getMaxStorableData(BufferedImage signified) throws IOException
    {
        long maxStorableData = (long)Math.floor((signified.getHeight()*signified.getWidth()*(12.0/32)));
        
        return maxStorableData;
    }

    //No of pixles alpha nibbles required to store length and signature of the data
    public static int getMaxAlphaRequired(BufferedImage signified) throws IOException
    {
        int signatureAlpha = 32; //signify! + !fyingis
        
        int maxAlpha = String.valueOf(getMaxStorableData(signified)).length()*2;
        maxAlpha+=signatureAlpha;

        return maxAlpha;
    }

    //How much gap can be kept between two aplha nibbles which shows length. i.e so that we don't have to store in contigous manner!
    public static int getAlphaGap(BufferedImage signified) throws IOException
    {
        //return (int)Math.floorDiv(getMaxStorableData(signified),getMaxAlphaRequired(signified));
        return Math.floorDiv(signified.getWidth()*signified.getHeight(),getMaxAlphaRequired(signified));
    }
}