package newroten;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;

public class Personaje
{
    final int NEUTRA = 0;
    final int IZQUIERDA = 1;
    final int DERECHA = 2;
    final int ARRIBA = 3;
    final int ABAJO = 4;

    final int VIVO = 5;
    final int MUERTO = 6;
    final int MURIO = 7;
    
    int [] DireccionIzq = {4,5,6,7,6,5};
    int [] DireccionDer = {8,9,10,11,10,9};
    int [] DireccionArr = {12,13,14,15,14,13};
    int [] DireccionAba = {0,1,2,3,2,1};
    int [] DireccionNeu = {0,0,0,8,8,8,12,12,12,4,4,4};
    int [] DireccionActiva = DireccionNeu;
    
    int Xinicial;
    int Yinicial;
    
    int corX, corY; // coordenadas en el plano del canvas
    
    ImageIcon Jugador;
    int IndiceFrame, Alto, Ancho, CantidadFrames, Direccion, PV, Vidas, Puntos;
    private int X, Y;
    
    boolean desProgreso;
    int Murio, Vive;
    
    Canvas refCanvas;
    
    public Personaje(Canvas canvasResivido, int xini,int yini)
    {
        Murio = VIVO;
        desProgreso=false;
        refCanvas=canvasResivido;
        Xinicial = xini;
        Yinicial = yini;
        corX=xini * 32;
        corY=yini * 32;
        Puntos = 0;
        PV = 100;
        Vidas = 3;
        Direccion = NEUTRA;
        Jugador = new ImageIcon(Toolkit.getDefaultToolkit().createImage("./Flow.png"));
        Alto = Jugador.getIconHeight();
        Ancho = Alto;
        CantidadFrames = Jugador.getIconWidth() / Alto;
        IndiceFrame = 0;
        CoordenadasIniciales();
    }
    
    public void paint(Graphics gr)
    {
        gr.setClip(corX, corY, Alto, Ancho);
        gr.drawImage(Jugador.getImage(), corX - (DireccionActiva[IndiceFrame] * Ancho), corY, null);
        gr.setClip(0, 0, 320, 320);
    }
    
    public void Direccion()
    {
        switch(Direccion)
        {
            case NEUTRA:
                DireccionActiva = DireccionNeu;
                if(IndiceFrame < DireccionActiva.length -1){IndiceFrame++;}else{IndiceFrame = 0;}
                break;
            case IZQUIERDA:
                DireccionActiva = DireccionIzq;
                if(IndiceFrame < DireccionActiva.length -1){IndiceFrame++;}else{IndiceFrame = 0;}
                break;
            case DERECHA:
                DireccionActiva = DireccionDer;
                if(IndiceFrame < DireccionActiva.length -1){IndiceFrame++;}else{IndiceFrame = 0;}
                break;
            case ARRIBA:
                DireccionActiva = DireccionArr;
                if(IndiceFrame < DireccionActiva.length -1){IndiceFrame++;}else{IndiceFrame = 0;}
                break;
            case ABAJO:
                DireccionActiva = DireccionAba;
                if(IndiceFrame < DireccionActiva.length -1){IndiceFrame++;}else{IndiceFrame = 0;}
                break;
        }
    }
    
    public int Live()
    {
        Vive = VIVO;
        if(PV > 0)
        {
            Vive = VIVO;
        }
        else
        {
            Murio=MURIO;
            if(Vidas > 0)
            {
                CoordenadasIniciales();
                PV = 100;
                Vidas--;
                Vive = VIVO;
            }
            else
            {
                Vive = MUERTO;
            }
        }
        return Vive;
    }
    
    public void Reiniciar()
    {
        Direccion = NEUTRA;
        Vidas = 3;
        PV = 100;
        CoordenadasIniciales();
        Puntos = 0;
    }
    
    public void CoordenadasIniciales()
    {
        Direccion = NEUTRA;
        X = Xinicial;
        Y = Yinicial;
        corX = Xinicial * 32;
        corY = Yinicial * 32;
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
            desProgreso=true;
        }
        
        public void run()
        {
            if(Vive == VIVO)
            {
                if(enX)
                {
                    if(corX!=X*32)
                    {
                        refCanvas.repaint();
                        corX+=movPix;
                    }else{desProgreso=false;this.cancel();}
                }
                else
                {
                    if(corY!=Y*32)
                    {
                        refCanvas.repaint();
                        corY+=movPix;
                    }else{desProgreso=false;this.cancel();}
                }
            }else{Vive=MUERTO;desProgreso=false;this.cancel();}
        }
    }
}
