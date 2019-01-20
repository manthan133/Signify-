public class BitFromString
{
    public static void main(String[] args)
    {
        String s = "SDP Project";
        
        byte[] nibbles = getNibbles(s);
        
        for(int i=0;i<nibbles.length;i++)
            System.out.print(nibbles[i]+" ");
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
            nibbles[k++]=(byte)(bytes[i]&15);
        }
        
        System.out.println(k);
        
        return nibbles;
    }
}