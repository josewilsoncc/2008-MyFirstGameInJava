package newroten;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.ImageIcon;

public class Mapa
{
    final int IZQUIERDA = 1;
    final int DERECHA = 2;
    final int ARRIBA = 3;
    final int ABAJO = 4;
    
    Objetos obj;
    int ObjPuestos = 0;
    
    Image Fondo;
    Canvas UnCanvas;
    ImageIcon TiraMapa;
    
    int i, i2;
    
    int [][] Tiles;
    
    int IndeiceFrame, Alto, Ancho, X, Y;
    
    Personaje Jugador;
    
    public Mapa(Canvas xCanvas, Personaje x)
    {
        obj = new Objetos(xCanvas);
        TiraMapa = new ImageIcon(Toolkit.getDefaultToolkit().createImage("./Mapa.png"));
        Tiles = new int [][]
        {
            {0,1,1,1,1,1,1,1,1,2},
            {3,10,4,9,4,4,9,4,10,5},
            {3,4,11,4,4,4,4,11,4,5},
            {3,9,4,4,4,4,4,4,9,5},
            {3,4,4,4,4,4,4,4,4,5},
            {3,4,4,4,4,4,4,4,4,5},
            {3,9,4,4,4,4,4,4,9,5},
            {3,4,11,4,4,4,4,11,4,5},
            {3,10,4,9,4,4,9,4,10,5},
            {6,7,7,7,7,7,7,7,7,8}
        };
        Jugador = x;
        UnCanvas = xCanvas;
        Fondo = Toolkit.getDefaultToolkit().createImage("./Reggaeton.png");
        Alto = 32;
        Ancho = 32;
    }
    
    public void paint(Graphics gr)
    {
        gr.drawImage(Fondo, 0, 0, UnCanvas);
        for(i = 0; i < Tiles.length; i++)
        {
            for(i2 = 0; i2 < Tiles[i].length; i2++)
            {
                gr.setClip(i * 32, i2 * 32, Ancho, Alto);
                gr.drawImage(TiraMapa.getImage(), i*Ancho-(Tiles[i2][i] * Ancho), i2 * 32, UnCanvas);
                gr.setClip(0, 0, 320, 320);
            }
        }
    }
    
    public void EnPiso()
    {
        switch(Tiles[Jugador.getX()][Jugador.getY()])
        {
            case 9:
                Jugador.PV--;
                Jugador.Direccion = Jugador.NEUTRA;
                break;
            case 10:
                if(Jugador.PV < 100){Jugador.PV++;}
                Jugador.Direccion = Jugador.NEUTRA;
                break;
            case 11:
                Jugador.PV = 0;
                Jugador.CoordenadasIniciales();
                break;
        }
    }
    
    public void PonerObjeto()
    {
        if(ObjPuestos<=5)
        {
            int nx, ny, poner;
            nx=(int)((Math.random()*10)-1);
            ny=(int)((Math.random()*10)-1);
            poner=(int)((Math.random()*12));
            if(obj.PosicionObjetos[nx][ny]==-1 && poner!=12)
            {
                if(Tiles[nx][ny]==4)
                {
                    obj.PosicionObjetos[nx][ny]=poner;
                    ObjPuestos++;
                }
            }
        }
    }
    
    public void Mover(int x)
    {
        if(x == IZQUIERDA)
        {
            switch(Tiles[Jugador.getX() -1][Jugador.getY()])
            {
                case 0:
                case 1:
                case 2:
                case 3:
                case 5:
                case 6:
                case 7:
                case 8:break;
                default:if(!Jugador.desProgreso){Jugador.movX(-1);}break;
            }
        }
        if(x == DERECHA)
        {
            switch(Tiles[Jugador.getX() +1][Jugador.getY()])
            {
                case 0:
                case 1:
                case 2:
                case 3:
                case 5:
                case 6:
                case 7:
                case 8:break;
                default:if(!Jugador.desProgreso){Jugador.movX(1);}break;
            }
        }
        if(x == ARRIBA)
        {
            switch(Tiles[Jugador.getX()][Jugador.getY() - 1])
            {
                case 0:
                case 1:
                case 2:
                case 3:
                case 5:
                case 6:
                case 7:
                case 8:break;
                default:if(!Jugador.desProgreso){Jugador.movY(-1);}break;
            }
        }
        if(x == ABAJO)
        {
            switch(Tiles[Jugador.getX()][Jugador.getY() + 1])
            {
                case 0:
                case 1:
                case 2:
                case 3:
                case 5:
                case 6:
                case 7:
                case 8:break;
                default:if(!Jugador.desProgreso){Jugador.movY(1);}break;
            }
        }
        switch(obj.PosicionObjetos[Jugador.getX()][Jugador.getY()])
        {
            case -1:break;
            case 0:Jugador.Puntos+=1;ObjPuestos--;break;
            case 1:Jugador.Puntos+=2;ObjPuestos--;break;
            case 2:Jugador.Puntos+=4;ObjPuestos--;break;
            case 3:Jugador.Puntos+=5;ObjPuestos--;break;
            case 4:Jugador.Puntos+=10;ObjPuestos--;break;
            case 5:Jugador.Puntos+=20;ObjPuestos--;break;
            case 6:Jugador.Puntos+=25;ObjPuestos--;break;
            case 7:Jugador.Puntos+=50;ObjPuestos--;break;
            case 8:Jugador.Puntos+=100;ObjPuestos--;break;
            case 9:Jugador.Puntos+=125;ObjPuestos--;break;
            case 10:Jugador.Puntos+=250;ObjPuestos--;break;
            case 11:Jugador.Puntos+=500;ObjPuestos--;break;
        }
        obj.PosicionObjetos[Jugador.getX()][Jugador.getY()]=-1;
    }
    public void MoverMaquina(int x, Maquina PCX)
    {
        if(x == IZQUIERDA)
        {
            switch(Tiles[PCX.getX() -1][PCX.getY()])
            {
                case 0:
                case 1:
                case 2:
                case 3:
                case 5:
                case 6:
                case 7:
                case 8:
                case 11:break;
                default:if(!PCX.movProgreso){PCX.movX(-1);}break;
            }
        }
        if(x == DERECHA)
        {
            switch(Tiles[PCX.getX() +1][PCX.getY()])
            {
                case 0:
                case 1:
                case 2:
                case 3:
                case 5:
                case 6:
                case 7:
                case 8:
                case 11:break;
                default:if(!PCX.movProgreso){PCX.movX(1);}break;
            }
        }
        if(x == ARRIBA)
        {
            switch(Tiles[PCX.getX()][PCX.getY() - 1])
            {
                case 0:
                case 1:
                case 2:
                case 3:
                case 5:
                case 6:
                case 7:
                case 8:
                case 11:break;
                default:if(!PCX.movProgreso){PCX.movY(-1);}break;
            }
        }
        if(x == ABAJO)
        {
            switch(Tiles[PCX.getX()][PCX.getY() + 1])
            {
                case 0:
                case 1:
                case 2:
                case 3:
                case 5:
                case 6:
                case 7:
                case 8:
                case 11:break;
                default:if(!PCX.movProgreso){PCX.movY(1);}break;
            }
        }
        
        if(PCX.getX() == Jugador.getX() && PCX.getY() == Jugador.getY())
        {
            Jugador.PV -= Math.random()*5;
        }
    }
}
