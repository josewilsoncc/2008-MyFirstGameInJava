package newroten;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;

public class Maquina
{
    int Xinicial, Yinicial;
    
    Mapa Mundo1;
    
    int [] Animacion = {0,0,1,1,2,2,3,3};
    
    int Frame, Ancho, Alto, corX, corY;
    
    private int X,Y;
    
    Canvas refCanvas;
    
    boolean movProgreso;
    
    ImageIcon PC;
    
    public Maquina(Canvas canvas,Mapa Map, int xini, int yini)
    {
        refCanvas=canvas;
        movProgreso=false;
        Xinicial = xini;
        Yinicial = yini;
        corX=xini*32;
        corX=yini*32;
        Mundo1 = Map;
        PC = new ImageIcon(Toolkit.getDefaultToolkit().createImage("./Fantasma.png"));
        Alto = 32;//PC.getIconHeight();
        Ancho = Alto;
        Inicial();
    }
    
    public void paint(Graphics gr)
    {
        gr.setClip(corX, corY, Alto, Ancho);
        gr.drawImage(PC.getImage(), corX - (Animacion[Frame]* Ancho), corY, null);
        gr.setClip(0,0,320,320);
    }
    
    public void Inicial()
    {
        Frame = 0;
        X = Xinicial;
        Y = Yinicial;
        corX=Xinicial*32;
        corY=Yinicial*32;
    }
    
    public void AnimacionMaquina()
    {
        if(Frame < Animacion.length - 1)
        {
            Frame++;
        }
        else{Frame = 0;}
    }
    
    public void MovimientoDesconocido(int XJugador, int YJugador)
    {
        if((X >= XJugador-3 && X <= XJugador+3) && (Y >= YJugador-3 && Y <= YJugador+3))
        {
            if(XJugador > X){Mundo1.MoverMaquina(2, this);}
            if(XJugador < X){Mundo1.MoverMaquina(1, this);}
            if(YJugador > Y){Mundo1.MoverMaquina(4, this);}
            if(YJugador < Y){Mundo1.MoverMaquina(3, this);}
            if(XJugador == X && YJugador == Y){Mundo1.MoverMaquina(0, this);}
        }
        else
        {
            int Mov;
            Mov = (int) (Math.random() * 5);
            Mundo1.MoverMaquina(Mov, this);
        }
        
    }
    
    public int getX(){return X;}
    public int getY(){return Y;}
    
    public void movX(int mov)
    {
        this.X+=mov;
        int movPix;
        if(mov<0){movPix=-4;}else{movPix=4;}
        new Timer().schedule(new Deslizar(movPix,true),0,5);
    }
    
    public void movY(int mov)
    {
        this.Y+=mov;
        int movPix;
        if(mov<0){movPix=-4;}else{movPix=4;}
        new Timer().schedule(new Deslizar(movPix,false),0,5);
    }
    
    class Deslizar extends TimerTask
    {
        int movPix;
        boolean enX;
        public Deslizar(int movPix, boolean enX)
        {
            this.movPix=movPix;
            this.enX=enX;
            movProgreso=true;
        }
        
        public void run()
        {
            if(enX)
            {
                if(corX!=X*32)
                {
                    refCanvas.repaint();
                    corX+=movPix;
                }else{movProgreso=false;this.cancel();}
            }
            else
            {
                if(corY!=Y*32)
                {
                    refCanvas.repaint();
                    corY+=movPix;
                }else{movProgreso=false;this.cancel();}
            }
        }
    }
    
}
