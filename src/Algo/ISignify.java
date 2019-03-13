/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algo;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *
 * @author jay
 */
public interface ISignify {
    
        public boolean isSignified(BufferedImage signified,String sign);
        public  String retrieveData(String imagePath,String sign) throws IOException;
        public void hideData(String filePath,String imagePath,String sign) throws IOException;
        public void hideProperties(long length,String extension,BufferedImage signified,String _sign);
        public int getLength(BufferedImage signified);
        public String getExtension(BufferedImage signified);
        public String getProperty(BufferedImage signified);
        public long getMaxStorableData(BufferedImage signified) throws IOException;
        public int getMaxAlphaRequired(BufferedImage signified) throws IOException;
        public int getRGBRequired(BufferedImage signified, long secretFileSize) throws IOException;
        public int getAlphaGap(BufferedImage signified) throws IOException;
        public int getRGBGap(BufferedImage signified,long secretFileSize) throws IOException;
            
}
