package newroten;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Toolkit;
import javax.swing.ImageIcon;

public class Objetos
{
    
    ImageIcon Obj;
    
    int ix, iy;
    
    int[][] PosicionObjetos;
    
    Canvas UnCanvas;
    
    public Objetos(Canvas xcan)
    {
        UnCanvas = xcan;
        Obj = new ImageIcon(Toolkit.getDefaultToolkit().createImage("Objetos.png"));
        PosicionObjetos = new int[][]
        {
            {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
            {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
            {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
            {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
            {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
            {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
            {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
            {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
            {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
            {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1}
        };
    }
    
    public void paint(Graphics gr)
    {
        for(ix=0;ix<PosicionObjetos.length - 1;ix++)
        {
            for(iy=0;iy<PosicionObjetos[ix].length;iy++)
            {
                if(PosicionObjetos[ix][iy]!=-1)
                {
                    gr.setClip(ix * 32, iy * 32, 32, 32);
                    gr.drawImage(Obj.getImage(), ix*32-(PosicionObjetos[ix][iy]*32), iy*32, UnCanvas);
                    gr.setClip(0,0,320,320);
                }
            }
        }
    }
}
