/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algo;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class Signify_LSB_Basic implements ISignify
{
    public static void main(String[] args) throws IOException
    {

    }
    
    @Override
    public boolean isSignified(BufferedImage signified, String sign)
    {
        String[] properties = getProperty(signified).split("!");
        String initialSign = "signify";
        String finalSign = "yfingis";

        return (properties[0].equals(initialSign) && properties[2].substring(0,finalSign.length()).equals(finalSign));
    } 

    /* to retrieve Data  from signified image */ 
    @Override
    public String retrieveData(String imagePath, String sign) throws IOException
    {
        
        BufferedImage signified = ImageIO.read(new File(imagePath));

        //PrintWriter out   = new PrintWriter(new BufferedWriter(new FileWriter("/home/jay/Desktop/DesignifiedFiles/retrive.txt")));
        
        //System.out.println(isSignified(signified));
        if(!isSignified(signified,sign))
        {
            System.out.println("Not Signified!");
            return "No secret Data present in image";
        }

        String[] concatePath=imagePath.split("/");
        String fullFileName=concatePath[concatePath.length-1];
            
        String[] fileName=fullFileName.split(Pattern.quote("."));
        String designifiedPath="/home/jay/Desktop/DesignifiedFiles/"+fileName[0]+"_Signify."+ getExtension(signified);
        OutputStream targetStream = new FileOutputStream(new File(designifiedPath)); 

        int[] bytes = new int[getLength(signified)];
        
        int currentPixle=0;
        int currentByte=0;
        long bitPointer=-1;
        int totalBits = bytes.length*8;

        try
        {
            while(true)
            {
                Color pixle = new Color(signified.getRGB( currentPixle%signified.getWidth() ,currentPixle/signified.getWidth()));
                currentPixle++;

                bitPointer++;

                if(bitPointer%8 == 0 && bitPointer>0)
                {
                    
                    bytes[ (int)bitPointer/8 - 1] = (currentByte);//-1
                    //System.out.println(currentByte&0xff);
                    if(bitPointer == totalBits)break;
                    currentByte=0;
                    //System.out.println();
                }                // System.out.println(nibbles[currentNibble-1] +" "+ red);
                int red = (pixle.getRed() & 1);   //byte red
                currentByte=( currentByte | (red << 8) );//7
                currentByte=(currentByte>>1)&0xff;
                //System.out.println("Red currentByte: "+Integer.toBinaryString(currentByte)+" bitpointer"+bitPointer);
                //System.out.println("\t"+bitPointer+" "+Integer.toBinaryString(pixle.getRed()));

                
                bitPointer++;
                // System.out.println(nibbles[currentNibble-1] +" "+ green);
                if(bitPointer%8 == 0)
                {
                    bytes[ (int)bitPointer/8 - 1] = (currentByte);
                    //System.out.println(currentByte&0xff);
                    if(bitPointer == totalBits)break;
                    currentByte=0;                    
                    //System.out.println();

                }
                int green = (pixle.getGreen() & 1);
                currentByte=( currentByte | (green << 8) );
                currentByte=(currentByte>>1)&0xff;
                //System.out.println("Green currentByte: "+Integer.toBinaryString(currentByte)+" bitpointer"+bitPointer);
                //System.out.println("\t"+bitPointer+" "+Integer.toBinaryString(pixle.getGreen()));

                
                bitPointer++;
                // System.out.println(nibbles[currentNibble-1] +" "+ blue);
                if(bitPointer%8 == 0)
                {
                    bytes[ (int)bitPointer/8 - 1] = (currentByte);
                    //System.out.println(currentByte&0xff);
                    if(bitPointer == totalBits)break;
                    currentByte=0;
                    //System.out.println();
                }
                int blue = (pixle.getBlue() & 1);
                currentByte=( currentByte | (blue << 8) );
                currentByte=(currentByte>>1)&0xff;
                //System.out.println("Blue currentByte: "+Integer.toBinaryString(currentByte)+" bitpointer"+bitPointer);
                //System.out.println("\t"+bitPointer+" "+Integer.toBinaryString(pixle.getBlue()));

               
            }

            byte[] newByte=new byte[bytes.length];
            for(int i=0;i<bytes.length;i++)
            {
                //out.println(bytes[i]+"  "+Integer.toBinaryString(bytes[i]|0x80));
                newByte[i]=(byte)bytes[i];
            }
            //ByteArrayInputStream in =[ new ByteArrayInputStream(bytes);
            //IOUtils.copy(in, targetStream);
            //IOUtils.closeQuietly(in);
            //IOUtils.closeQuietly(targetStream);
            System.out.println("after");
            /*for(int i=0;i<bytes.length;i++)
            {
                //out.println(bytes[i]+"  "+Integer.toBinaryString(bytes[i]|0x80));
                targetStream.write(bytes[i]);            
            }
            targetStream.close();*/
            FileUtils.writeByteArrayToFile(new File(designifiedPath), newByte);
            
        //out.flush();
        //    out.close();
        }
        catch(IOException e)
        {
            System.out.println(e);
        }        
        return "your file "+fileName[0]+"_Signify."+ getExtension(signified)+" succesfully designified.";
    }

    /* To hide data into image*/
    @Override
    public void hideData(String filePath,String imagePath,String sign) throws IOException
    {
        File file = new File(filePath);
        
        //PrintWriter out   = new PrintWriter(new BufferedWriter(new FileWriter("/home/jay/Desktop/DesignifiedFiles/hide.txt")));
        System.out.println(file.length());
        FileInputStream reader=new FileInputStream(filePath);
        String extension = FilenameUtils.getExtension(filePath);
        long size = file.length();
        
        BufferedImage original = ImageIO.read(new File(imagePath));
        BufferedImage signified = new BufferedImage(original.getWidth(),original.getHeight(),BufferedImage.TYPE_INT_ARGB);

      
        for(int i=0;i<original.getWidth();i++)
            for(int j=0;j<original.getHeight();j++)
                signified.setRGB(i, j,original.getRGB(i,j));
            

        int currentPixle=0;
        int currentByte=0;
        long bitPointer=0;
        
        try 
        {
            //img.getHeight()*img.getWidth()
            while(true)
            {
                Color pixle = new Color(signified.getRGB( currentPixle%signified.getWidth() ,currentPixle/signified.getWidth()));
                //System.out.println(Integer.toBinaryString(pixle.getRGB()));
                int red=0,blue=0,green=0;
                if(bitPointer%8 == 0)
                {
                    if(bitPointer == size*8)break;
                    currentByte=reader.read();
                    //System.out.println(currentByte&0xff);
                    //out.println(Integer.toBinaryString(currentByte));
                }
                red = pixle.getRed();
                red = (red & 254) | (currentByte & 1);
                bitPointer++;
                currentByte=currentByte>>1;
                //System.out.println("red "+Integer.toBinaryString(red));

                if(bitPointer%8 == 0)
                {
                    if(bitPointer == size*8)
                    {
                        int rgb = (pixle.getAlpha()<<24 | red << 16 | pixle.getGreen() << 8 | pixle.getBlue());
                        signified.setRGB(currentPixle%signified.getWidth() ,currentPixle/signified.getWidth(),rgb);
                        break;
                    }                    currentByte=reader.read();
                    //System.out.println(currentByte&0xff);
                    //out.println(Integer.toBinaryString(currentByte));
                }
                green = pixle.getGreen();
                green = (green & 254) | (currentByte & 1);
                bitPointer++;
                currentByte=currentByte>>1;
                //System.out.println("green "+Integer.toBinaryString(green));
                // System.out.println(nibbles[currentNibble-1] +" "+ green);

                if(bitPointer%8 == 0)
                {
                    if(bitPointer == size*8)
                    {
                        int rgb = (pixle.getAlpha()<<24 | red << 16 | green << 8 | pixle.getBlue());
                        signified.setRGB(currentPixle%signified.getWidth() ,currentPixle/signified.getWidth(),rgb);
                        break;
                    }
                    currentByte=reader.read();
                    //System.out.println(currentByte&0xff);
                    //out.println(Integer.toBinaryString(currentByte));
                }
                blue = pixle.getBlue();
                blue = (blue & 254) | (currentByte & 1);
                bitPointer++;
                currentByte=currentByte>>1;
                //System.out.println("blue "+Integer.toBinaryString(blue));
                // System.out.println(nibbles[currentNibble-1] +" "+ blue);

                int rgb = (pixle.getAlpha()<<24 | red << 16 | green << 8 | blue);

                signified.setRGB(currentPixle%signified.getWidth() ,currentPixle/signified.getWidth(),rgb);
                //System.out.println(" after "+Integer.toBinaryString(signified.getRGB(currentPixle%signified.getWidth() ,currentPixle/signified.getWidth())));

                currentPixle++;

            }
            
            hideProperties(size,extension,signified,sign);

            String[] concatePath=imagePath.split("/");
            String fullFileName=concatePath[concatePath.length-1];
            
            String[] fileName=fullFileName.split(Pattern.quote("."));
            String finalPath = "/home/jay/Desktop/SignifiedImages/"+fileName[0]+"_Signify.png"; //_Signify
            File outputfile = new File(finalPath);
            ImageIO.write(signified, "png", outputfile);
            
            //getProperty(signified);
            reader.close();
            System.out.println("Done!");
            //out.flush();
            //out.close();
        } 
        catch (IOException e) 
        {
        }
    }
    
    //Hide Signature and Length
    @Override
    public void hideProperties(long length,String extension,BufferedImage signified,String _sign)
    {
        String sign = "signify!";
        String reverse = new StringBuilder(sign).reverse().toString();

        String property = sign+String.valueOf(length)+reverse+"!"+extension+"!";
        System.out.println(property);
        byte[] bytes = property.getBytes();
        
        int size = bytes.length;
        int currentPixle=0,currentByteIndex=0;
        int currentByte=0;
        long bitPointer=0;
        
        try 
        {
            int pixleGap = getAlphaGap(signified);

            while(true)
            {   
                if(bitPointer%8 == 0)
                {
                    if(bitPointer == size*8)break;
                    currentByte= bytes[currentByteIndex++];
                    
                }
                
                int pixle = signified.getRGB( currentPixle%signified.getWidth() ,currentPixle/signified.getWidth());
                //System.out.println(Integer.toBinaryString(pixle));
                pixle = pixle & 0xfeffffff; //0x00ffffff
                pixle |= (currentByte&1)<<24;
                
                currentByte=currentByte>>1;
                bitPointer++;
                //System.out.println(" after alpha"+Integer.toBinaryString(pixle));
                signified.setRGB(currentPixle%signified.getWidth() ,currentPixle/signified.getWidth(),pixle);
                
                currentPixle+=pixleGap;

            }

        }
        catch(IOException e)
        {
            System.out.println("*********"+e);
        }
    }

    @Override
    public int getLength(BufferedImage signified)
    {
        String[] properties = getProperty(signified).split("!");
        
        System.out.println(getProperty(signified));
        return Integer.parseInt(properties[1]);

        //System.out.println("Here property "+getProperty(signified));
        //return 78; 
    }
    
    @Override
    public String getExtension(BufferedImage signified)
    {
        String[] properties = getProperty(signified).split("!");
        return properties[3];
    }

    @Override
    public String getProperty(BufferedImage signified)
    {       
        String property="";

        try
        {
            int totalPixles = signified.getHeight()*signified.getWidth();
            int currentPixle;
            int currentByte=0;
            long bitPointer=0; 
            int pixleGap = getAlphaGap(signified);
            ArrayList<Byte> append = new ArrayList(); 

            for(currentPixle=0;currentPixle<totalPixles;currentPixle+=pixleGap)
            {
                
                bitPointer++;
                if(bitPointer%8==0)
                {
                    //System.out.println("\tadded: "+Integer.toBinaryString(currentByte));
                    append.add((byte)currentByte);
                    currentByte=0;
                }
                int pixle = signified.getRGB( currentPixle%signified.getWidth() ,currentPixle/signified.getWidth());
                byte alpha = (byte)(((pixle & 0x01000000)>>17));
                //System.out.println("currentbyte: "+Integer.toBinaryString(currentByte)+" alpha:"+Integer.toBinaryString(alpha));
                currentByte=(byte)(( currentByte )| alpha);

                currentByte=(byte)((currentByte>>1)&0x7f);//0111 1111
            }
            
            byte[] bytes = new byte[append.size()];
            for(int i=0;i<append.size();i++)
                bytes[i]=append.get(i);    

            property = new String(bytes);
            System.out.println("Designified correct! "+" "+property);
            System.out.flush();
        }
        catch(IOException e)
        {
            System.out.println("getProperty: "+e);
            System.out.println(signified.getHeight()+","+signified.getWidth());
        }

        return property;
    }
    //Max data that can be hidden in the image
    @Override
    public long getMaxStorableData(BufferedImage signified) throws IOException
    {
        long maxStorableData = ((long)signified.getHeight()*signified.getWidth()*3);
        System.out.println(maxStorableData+" "+(long)signified.getHeight()+" "+signified.getWidth());
        
        return maxStorableData;
    }

    //No of pixles alpha nibbles required to store length and signature of the data
    @Override
    public int getMaxAlphaRequired(BufferedImage signified) throws IOException
    {
        int longestFileExtension=12;
        int signatureAlpha = (16+longestFileExtension)*8; //signify! + !fyingis
        
        int maxAlpha = String.valueOf(getMaxStorableData(signified)).length()*8;
        maxAlpha+=signatureAlpha;

        return maxAlpha;
    }

    //How much gap can be kept between two aplha nibbles which shows length. i.e so that we don't have to store in contigous manner!
    @Override
    public int getAlphaGap(BufferedImage signified) throws IOException
    {
        //return (int)Math.floorDiv(getMaxStorableData(signified),getMaxAlphaRequired(signified));
        return Math.floorDiv(signified.getWidth()*signified.getHeight(),getMaxAlphaRequired(signified));
    }



    @Override
    public int getRGBRequired(BufferedImage signified, long secretFileSize) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getRGBGap(BufferedImage signified, long secretFileSize) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}