package newroten;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import javax.swing.ImageIcon;


public class Dibujante extends Canvas implements KeyListener
{
    //Se crearan las variables que controlaran los flujos
    BufferedReader Leer;
    BufferedWriter Escribir;
    String CadenaFlujo;
    
    //Se declararan las variables de las pantallas
    final int MENU           = 1;
    final int JUEGO          = 2;
    final int CREDITOS       = 3;
    final int GAMEOVER       = 4;
    final int PAUSADO        = 5;
    final int CARGANDO       = 6;
    final int NIVEL          = 7;
    final int INSTRUCCIONES1 = 8;
    final int INSTRUCCIONES2 = 9;
    final int INSTRUCCIONES3 = 10;
    final int APODO          = 11;
    final int MUERTE         = 12;
    
    //Estas son unas minivariables que se usan en la pantalla cargando...
    boolean listo = false;
    int bar = 0;
    
    //Se creara el mapa, personaje y las maquinas
    Mapa Mundo1;
    Personaje Jugador1;
    Vector<Maquina> PCS;
    Vector<String> Alias=new Vector();
    //Se creara la imagen interna (Dibujo Interno)
    Image DI;
    
    //Se creara la imagen que puede ser usada como fondo
    Image fon;
    
    //Graficos Dibujo Interno, Los graficos de DI y GDI seran los mismos
    Graphics GDI;
    
    //Dibujo Interno Creado
    boolean DIC;
    
    //Estas son las variables que controlan las pantallas y la seleccion
    int xsel, xEstado, PuntajeMaximo;
    
    //Estas variables retendran los valores del ancho y alto del canvas
    int xWidth, xHeight;
    
    //Se creara el vector que retendra las opciones de la pantalla
    public Vector<String>Opciones = new Vector();
    
    //Esta variable es la que indica si la tecla presionada ya a realizado su accion
    boolean Reporte;
    //Estas son las variables limites que animaran a las maquinas una a una
    int nivel;
    
    //Con esta variable se personalizaran las cadenas
    Font letra;
    
    Canvas canvasEnviar;
    
    int temp=0;
    
    public Dibujante()
    {
        AderirApodos();
        canvasEnviar=this;
        try
        {
            Leer = new BufferedReader(new FileReader("WY.jw"));
            CadenaFlujo=Leer.readLine();
            Escribir = new BufferedWriter(new FileWriter("WY.jw", true));
        }catch(Exception e)
        {
            try{Escribir = new BufferedWriter(new FileWriter("WY.jw"));}catch(Exception ex){}
        }
        if(CadenaFlujo==null){CadenaFlujo="0";}
        PuntajeMaximo = Integer.parseInt(CadenaFlujo);
        Jugador1 = new Personaje(this,4,4);
        Mundo1 = new Mapa(this, Jugador1);
        PCS=new Vector();
        //PCS.add(new Maquina(Mundo1,1,1));
        xEstado = MENU;
        xsel = 0;
        DIC = false;
        Reporte = false;
        fon = Toolkit.getDefaultToolkit().createImage("./Reggaeton.png");
        nivel=1;
        this.addKeyListener(this);
        new Timer().schedule(new TareaMover(this), 200, 50);
        new Timer().schedule(new TareaMoverMaquina(), 2000, 400);
    }
    public void paint(Graphics gr)
    {
        update(gr);
    }
    public void update(Graphics gr)
    {
        if (!DIC)
        {
            xWidth = this.getWidth();
            xHeight = this.getHeight();
            //Dibujo interno creara una imagen deacuerdo al tamaño del canvas
            DI = this.createImage(320, 320);
            //GDI recibira por refencia los graficos de DI para poder manipular los graficos
            GDI = DI.getGraphics();
            //Se cambiara el color de fondo por negro
            GDI.setColor(new Color(0));
            //Se dibujara un rectangulo negro en toda la pantalla
            GDI.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1);
            //La variable DIC sera cambiada para no repetir este if
            DIC = true;
        }
        pan(GDI);
        //Se dibujara la imagen interna en el Graphics
        gr.drawImage(DI, 0, 0, 640, 640, this);
    }
    //El metodo que dibuja cada pantalla
    public void pan(Graphics gr)
    {
        Opciones.clear();
        letra=gr.getFont();
        switch(xEstado)
        {
            case MENU:
                gr.drawImage(fon, 0, 0, this);
                tamañoLetra(gr, 40);
                PaintString(gr, "DIAMONSTER", 25, 40);
                tamañoLetra(gr, 20);
                PaintString(gr, "JWCORPORACIÓN", 70, 65);
                PaintString(gr, "-Reggaetón-", 105, 85);
                tamañoLetra(gr);
                Opciones.add("Juego");
                Opciones.add("Creditos");
                Opciones.add("Instrucciones");
                PaintVector(gr);
                PaintString(gr, "Puntaje maximo: "+PuntajeMaximo, 100, 305);
                break;
            case JUEGO:
                Mundo1.paint(gr);
                Mundo1.obj.paint(gr);
                Jugador1.paint(gr);
                if(Jugador1.Puntos>nivel*2000){nivel++;xEstado=NIVEL;}
                for(int i=0;i<PCS.size();i++){PCS.elementAt(i).paint(gr);}
                Informante(gr);
                if(Jugador1.Live() == Jugador1.MUERTO)
                {
                    xEstado = GAMEOVER;
                    this.repaint();
                }
                else
                {
                    if(Jugador1.Murio == Jugador1.MURIO){xEstado=MUERTE; Jugador1.Murio = Jugador1.VIVO;}
                }
                break;
            case CREDITOS:
                gr.drawImage(fon, 0, 0, this);
                tamañoLetra(gr,15);
                PaintString(gr, "DIAMONSTER:", 20, 20);
                tamañoLetra(gr);
                PaintString(gr, "Creador:", 40, 55);
                PaintString(gr, "Jose Wilson Capera.", 60, 70);
                PaintString(gr, "Licencia:", 40, 90);
                PaintString(gr, "Solo Amigos", 60, 105);
                PaintString(gr, "Agradecimientos:", 40, 125);
                PaintString(gr, "Jose Jesus Piñeros.", 60, 140);
                PaintString(gr, "Ivan Giraldo Giraldo", 60, 155);
                PaintString(gr, "Diego Alberto Muñoz", 60, 170);
                PaintString(gr, "Alba Patricia Serna", 60, 185);
                PaintString(gr, "Edilberto Martines Nieto", 60, 200);
                PaintString(gr, "Integrantes Parquesoft", 60, 215);
                PaintString(gr, "Wisin & Yandel", 121, 230);
                tamañoLetra(gr,20);
                PaintString(gr,"Presiona Enter",98,255);
                tamañoLetra(gr,30);
                PaintString(gr, "JWCORPORACION", 25, 300);
                tamañoLetra(gr);
                break;
            case INSTRUCCIONES1:
                gr.drawImage(fon, 0, 0, this);
                tamañoLetra(gr,20);
                PaintString(gr, "Instrucciones:", 15, 30);
                tamañoLetra(gr,15);
                PaintString(gr, "Teclas:", 25, 50);
                tamañoLetra(gr);
                PaintString(gr,"Flechas:",35,65);
                PaintString(gr,"Controlan la seleccion del menú y al personaje",45,80);
                PaintString(gr,"Enter:",35,95);
                PaintString(gr,"Entra al menú indicado y pausan el juego",45,110);
                PaintString(gr,"Escape:",35,125);
                PaintString(gr,"Se sale de juego cantando juego perdido",45,140);
                PaintString(gr,"Suprimir-Delete:",35,155);
                PaintString(gr,"En el menú borra el puntaje maximo",45,170);
                PaintString(gr,"(Presiona Enter para continuar o Escape para salir)",20,260);
                break;
            case INSTRUCCIONES2:
                gr.drawImage(fon, 0, 0, this);
                tamañoLetra(gr,20);
                PaintString(gr, "Instrucciones:", 15, 30);
                tamañoLetra(gr,15);
                PaintString(gr, "Baldosas:", 25, 50);
                tamañoLetra(gr);
                PaintString(gr,"Azul:",35,65);
                PaintString(gr,"No afecta al personaje",45,80);
                PaintString(gr,"Verde:",35,95);
                PaintString(gr,"Aumenta los Puntos Vitales del personaje",45,110);
                PaintString(gr,"Rojo:",35,125);
                PaintString(gr,"Disminuye los Puntos Vitales del personaje",45,140);
                PaintString(gr,"Oscura:",35,155);
                PaintString(gr,"Resta una vida al personaje",45,170);
                PaintString(gr,"(Presiona Enter para continuar o Escape para salir)",20,260);
                break;
            case INSTRUCCIONES3:
                gr.drawImage(fon, 0, 0, this);
                tamañoLetra(gr,20);
                PaintString(gr, "Instrucciones:", 15, 30);
                tamañoLetra(gr,15);
                PaintString(gr, "Tabla de los valores de los diamantes", 25, 50);
                tamañoLetra(gr);
                gr.drawRect(25, 65, 270, 100);
                gr.drawLine(93, 65, 93, 165);
                gr.drawLine(160, 65, 160, 165);
                gr.drawLine(227, 65, 227, 165);
                gr.drawLine(25, 85, 295, 85);
                gr.drawLine(25, 105, 295, 105);
                gr.drawLine(25, 125, 295, 125);
                gr.drawLine(25, 145, 295, 145);
                PaintString(gr,"Color",43,80);
                PaintString(gr,"Grande",107,80);
                PaintString(gr,"Mediano",170,80);
                PaintString(gr,"Pequeño",237,80);
                PaintString(gr,"Rojo",47,100);
                PaintString(gr,"Azul",47,120);
                PaintString(gr,"Verde",43,140);
                PaintString(gr,"Amarillo",37,160);
                tamañoLetra(gr,20);
                gr.setColor(new Color(255,0,0));
                gr.drawString("500", 112, 103);
                gr.drawString("250", 175, 103);
                gr.drawString("125", 242, 103);
                gr.setColor(new Color(0,0,255));
                gr.drawString("100", 112, 123);
                gr.drawString("50", 182, 123);
                gr.drawString("25", 249, 123);
                gr.setColor(new Color(0,255,0));
                gr.drawString("20", 119, 143);
                gr.drawString("10", 182, 143);
                gr.drawString("5", 256, 143);
                gr.setColor(new Color(255,255,0));
                gr.drawString("4", 126, 163);
                gr.drawString("2", 189, 163);
                gr.drawString("1", 256, 163);
                tamañoLetra(gr);
                PaintString(gr,"(Presiona Enter o Escape para salir)",60,260);
                break;
            case GAMEOVER:
                gr.drawImage(fon, 0, 0, this);
                tamañoLetra(gr, 30);
                PaintString(gr, "JUEGO PERDIDO", 35, 80);
                if(Jugador1.Puntos > PuntajeMaximo){PaintString(gr, "¡Puntaje maximo!", 45, 115);}
                tamañoLetra(gr, 20);
                PaintString(gr, "Tu puntaje fue:", 95, 140);
                String textoTemp=""+Jugador1.Puntos;
                PaintString(gr, textoTemp,160-(textoTemp.length()*(gr.getFont().getSize()/4)),165);
                PaintString(gr, "Presiona Enter",95,220);
                tamañoLetra(gr);
                break;
            case PAUSADO:
                Mundo1.paint(gr);
                Mundo1.obj.paint(gr);
                Jugador1.paint(gr);
                for(int i=0;i<PCS.size();i++){PCS.elementAt(i).paint(gr);}
                Informante(gr);
                tamañoLetra(gr, 20);
                PaintString(gr, "Pausado", 120, 165);
                tamañoLetra(gr);
                break;
            case CARGANDO:
                gr.setColor(new Color(0));
                gr.fillRect(0, 0, 320, 320);
                if(listo==false)
                {
                    gr.drawImage(fon, 0, 0, this);
                    tamañoLetra(gr, 20);
                    PaintString(gr, "-Reggaetón-", 105, 150);
                    tamañoLetra(gr, 40);
                    switch(temp)
                    {
                        case 0:temp=1;break;
                        case 1:PaintString(gr, "Car...", 58, 245);temp=2;break;
                        case 2:PaintString(gr, "Cargan...", 58, 245);temp=3;break;
                        case 3:PaintString(gr, "Cargando...", 58, 245);temp=0;break;
                    }
                    tamañoLetra(gr);
                    gr.setColor(new Color(255,255,255));
                    gr.drawRect(60, 260, 200, 15);
                    if(bar<200)
                    {
                        gr.setColor(new Color(100,100,255));
                        gr.fillRect(61, 261, bar, 4);
                        gr.setColor(new Color(0,0,255));
                        gr.fillRect(61, 265, bar, 6);
                        gr.setColor(new Color(0,0,155));
                        gr.fillRect(61, 271, bar, 4);
                        PaintString(gr, (bar/2)+"%", 150, 272);
                    }
                    else
                    {
                        listo = true;
                        gr.fillRect(60, 260, 200, 200);
                        PaintString(gr, "100%", 150, 272);
                    }
                    bar+=(int)(Math.random()*40);
                    this.repaint();
                    JWpause(400);
                }
                else
                {
                    xEstado = JUEGO;
                    listo = false;
                }
                break;
            case NIVEL:
                gr.drawImage(fon, 0, 0, this);
                tamañoLetra(gr, 50);
                PaintString(gr, "Nivel "+nivel, 75, 150);
                tamañoLetra(gr, 20);
                PaintString(gr, "Presiona Enter", 98, 175);
                tamañoLetra(gr);
                break;
            case APODO:
                gr.drawImage(fon, 0, 0, this);
                tamañoLetra(gr, 25);
                PaintString(gr,"Deacuerdo con tus puntos",10,60);
                PaintString(gr,"te mereces el apodo:",40,90);
                tamañoLetra(gr);
                PaintString(gr,"Malo",50,220);
                PaintString(gr,"Bueno",235,220);
                gr.setColor(new Color(255,255,255));
                gr.fillRect(60, 230, 200, 5);
                gr.setColor(new Color(200,200,200));
                gr.fillRect(60, 235, 200, 5);
                gr.setColor(new Color(155,155,155));
                gr.fillRect(60, 240, 200, 5);
                gr.setColor(new Color(0));
                gr.fillOval(Calificacion(), 225, 5, 25);
                gr.setColor(new Color(255,255,255));
                gr.drawOval(Calificacion(), 225, 5, 25);
                tamañoLetra(gr,25);
                PaintString(gr,"> "+Apodar(),55,170);
                tamañoLetra(gr);
                break;
            case MUERTE:
                gr.drawImage(fon, 0, 0, this);
                tamañoLetra(gr,30);
                PaintString(gr,"¡Perdiste una vida!",35,100);
                tamañoLetra(gr,25);
                PaintString(gr,"Te quedan "+Jugador1.Vidas+" Vidas",55,130);
                tamañoLetra(gr,15);
                PaintString(gr,"Presiona Enter",110,170);
                tamañoLetra(gr);
                break;
        }
    }
    
    public int Calificacion()
    {
        int cordenadaX=60;
        if((Jugador1.Puntos/150)<30000)
        {
            cordenadaX+=(Jugador1.Puntos/150);
        }else{cordenadaX=260;}
        cordenadaX-=2;
        return cordenadaX;
    }
    
    public void tamañoLetra(Graphics gr)
    {
        gr.setFont(new Font(letra.getFontName(), letra.getStyle(),letra.getSize()));
    }
    
    public void tamañoLetra(Graphics gr, int x)
    {
        gr.setFont(new Font(letra.getFontName(), letra.getStyle(),x));
    }
    
    public void PaintString(Graphics gr, String str, int x,int y)
    {
        gr.setColor(new Color(255, 255, 255));
        gr.drawString(str,x-1,y-1);
        gr.drawString(str,x-1,y);
        gr.drawString(str,x-1,y+1);
        gr.drawString(str,x,y-1);
        gr.drawString(str,x,y+1);
        gr.drawString(str,x+1,y-1);
        gr.drawString(str,x+1,y);
        gr.drawString(str,x+1,y+1);
        gr.setColor(new Color(0));
        gr.drawString(str,x,y);
        gr.setColor(new Color(255, 255, 255));
    }
    
    public void PaintVector(Graphics gr)
    {
        int i, y;
        for(i=0, y=150; i<Opciones.size(); i++, y+=15)
        {
            if(i == xsel)
            {
                PaintString(gr, Opciones.get(i),115, y);
            }
            else
            {
                gr.setColor(new Color(255,255,255));
                gr.drawString(Opciones.get(i), 120, y);
                gr.setColor(new Color (0));
            }
        }
    }
    
    public class TareaMover extends TimerTask
    {
        Canvas can;
        public TareaMover(Canvas can)
        {
            this.can = can;
        }
        
        public void run()
        {
            if(xEstado == JUEGO)
            {
                Jugador1.Direccion();
                for(int i=0;i<PCS.size();i++){PCS.elementAt(i).AnimacionMaquina();}
                can.repaint();
                Mundo1.EnPiso();
                Mundo1.PonerObjeto();
            }
            if(xEstado == NIVEL)
            {
                can.repaint();
            }
            if(xEstado == MUERTE)
            {
                can.repaint();
            }
        }
    }
    
    public class TareaMoverMaquina extends TimerTask
    {
        public void run()
        {
            if(xEstado == JUEGO)
            {
                if(PCS.size()<nivel)
                {PCS.add(new Maquina(canvasEnviar,Mundo1,1,1));}
                for(int i=0;i<PCS.size();i++){PCS.elementAt(i).MovimientoDesconocido(Jugador1.getX(), Jugador1.getY());}
            }
        }
    }
    
    public void Informante(Graphics gr)
    {
        PaintString(gr, "- REGGAETÓN -", 30, 15);
        PaintString(gr, "Puntos:", 140, 15);
        PaintString(gr, ""+Jugador1.Puntos, 190, 15);
        PaintString(gr, "Nivel:",230,15);
        PaintString(gr, ""+nivel,268,15);
        PaintString(gr, "PV:", 60, 310);
        gr.setColor(new Color(255,100,100));
        gr.fillRect(82, 298, 100, 5);
        gr.setColor(new Color(255,0,0));
        gr.fillRect(82, 303, 100, 5);
        gr.setColor(new Color(155,0,0));
        gr.fillRect(82, 308, 100, 5);
        if(Jugador1.PV > 0)
        {
            gr.setColor(new Color(100,255,100));
            gr.fillRect(82, 298, Jugador1.PV, 5);
            gr.setColor(new Color(0,255,0));
            gr.fillRect(82, 303, Jugador1.PV, 5);
            gr.setColor(new Color(0,155,0));
            gr.fillRect(82, 308, Jugador1.PV, 5);
            PaintString(gr, Jugador1.PV+"%", 120, 310);
        }
        else{PaintString(gr, "0%", 120, 310);}
        gr.setColor(new Color(0,0,255));
        gr.drawRect(82, 298, 100, 15);
        PaintString(gr, "Vidas:", 210, 310);
        PaintString(gr, ""+Jugador1.Vidas, 250, 310);
    }
    
    public void JWpause(int x)
    {
        try{Thread.sleep(x);}catch(Exception e){}
    }
    
    public void AderirApodos()
    {
        Alias.add("HP");
        Alias.add("Punkera");
        Alias.add("Satanico");
        Alias.add("Garabito");
        Alias.add("Marigüano");
        Alias.add("Cabron");
        Alias.add("PingaChocha");
        Alias.add("Pelotudo");
        Alias.add("Parolin");
        Alias.add("Chochona");
        Alias.add("CareConcha");
        Alias.add("Verga");
        Alias.add("Bananero");
        Alias.add("CareAno");
        Alias.add("Burges");
        Alias.add("RataDeContainer");
        Alias.add("Gamba");
        Alias.add("CareKiosco");
        Alias.add("CareLampara");
        Alias.add("CarePlasta");
        Alias.add("Emo");
        Alias.add("Gay");
        Alias.add("Homosexual");
        Alias.add("Desechable");
        Alias.add("ChimpaVaca");
        Alias.add("Teton");
        Alias.add("Batichica");
        Alias.add("CareTetilla");
        Alias.add("BoquiSifon");
        Alias.add("OjiSapo");
        Alias.add("CareTajada");
        Alias.add("Rasguño");
        Alias.add("Pikachu");
        Alias.add("Vago");
        Alias.add("VagoSuperior");
        Alias.add("MasterVago");
        Alias.add("ElDuro");
        Alias.add("Player");
        Alias.add("Yankee");
        Alias.add("ReyDelJuego");
    }
    
    public String Apodar()
    {
        int retornarEste=0;
        //if(Jugador1.Puntos==0){return Alias.get(0);}
        for(int puntos=0;Jugador1.Puntos > puntos;retornarEste++)
        {
            puntos+=625;
        }
        return Alias.get(retornarEste);
    }
    
    public void keyTyped(KeyEvent e)
    {
        //throw new UnsupportedOperationException("Not supported yet.");
        
    }

    public void keyPressed(KeyEvent e)
    {
        //throw new UnsupportedOperationException("Not supported yet.");
        
        switch(xEstado)
        {
            case MENU:
                if(e.getKeyCode() == KeyEvent.VK_DOWN){if(xsel < Opciones.size()-1){xsel+=1;}else{xsel=0;}}
                if(e.getKeyCode() == KeyEvent.VK_UP){if(xsel > 0){xsel-=1;}else{xsel=Opciones.size()-1;}}
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    switch(xsel)
                    {
                        case 0: xEstado = CARGANDO; bar=0; listo=false; nivel=1; break;
                        case 1: xEstado = CREDITOS; break;
                        case 2: xEstado = INSTRUCCIONES1; break;
                    }
                    xsel = 0;
                }
                if(e.getKeyCode() == KeyEvent.VK_DELETE)
                {try{PuntajeMaximo=0;Escribir = new BufferedWriter(new FileWriter("WY.jw"));}catch(Exception ex){}}
                break;
            case JUEGO:
                if(e.getKeyCode() == KeyEvent.VK_LEFT){Jugador1.Direccion = Jugador1.IZQUIERDA; Mundo1.Mover(Jugador1.IZQUIERDA);}
                if(e.getKeyCode() == KeyEvent.VK_RIGHT){Jugador1.Direccion = Jugador1.DERECHA; Mundo1.Mover(Jugador1.DERECHA);}
                if(e.getKeyCode() == KeyEvent.VK_UP){Jugador1.Direccion = Jugador1.ARRIBA; Mundo1.Mover(Jugador1.ARRIBA);}
                if(e.getKeyCode() == KeyEvent.VK_DOWN){Jugador1.Direccion = Jugador1.ABAJO; Mundo1.Mover(Jugador1.ABAJO);}
                if(e.getKeyCode() == KeyEvent.VK_ENTER){xEstado = PAUSADO;}
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE){Jugador1.Vidas=0;Jugador1.PV=0;}
                break;
            case CREDITOS:
                if(e.getKeyCode() == KeyEvent.VK_ENTER){xEstado = MENU; xsel = 0;}
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE){xEstado = MENU; xsel = 0;}
                break;
            case GAMEOVER:
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    if(Jugador1.Puntos > PuntajeMaximo)
                    {
                        PuntajeMaximo = Jugador1.Puntos;
                        try{
                            Escribir = new BufferedWriter(new FileWriter("WY.jw"));
                            Escribir.write(""+PuntajeMaximo);
                            Escribir.flush();
                        }catch(Exception ex){}
                    }
                    xEstado = APODO;
                }
                break;
            case PAUSADO:
                if(e.getKeyCode() == KeyEvent.VK_ENTER){xEstado = JUEGO;}
                break;
            case NIVEL:
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    Jugador1.CoordenadasIniciales();
                    for(int i=0;i<PCS.size();i++){PCS.elementAt(i).Inicial();}
                    xEstado = JUEGO;
                }
                break;
            case INSTRUCCIONES1:
                if(e.getKeyCode() == KeyEvent.VK_ENTER){xEstado = INSTRUCCIONES2; xsel = 0;}
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE){xEstado = MENU; xsel = 0;}
                break;
            case INSTRUCCIONES2:
                if(e.getKeyCode() == KeyEvent.VK_ENTER){xEstado = INSTRUCCIONES3; xsel = 0;}
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE){xEstado = MENU; xsel = 0;}
                break;
            case INSTRUCCIONES3:
                if(e.getKeyCode() == KeyEvent.VK_ENTER){xEstado = MENU; xsel = 0;}
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE){xEstado = MENU; xsel = 0;}
                break;
            case APODO:
                if(e.getKeyCode() == KeyEvent.VK_ENTER){Jugador1.Reiniciar();nivel=1;PCS.clear();xEstado = MENU; xsel = 0;}
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE){Jugador1.Reiniciar();nivel=1;PCS.clear();xEstado = MENU; xsel = 0;}
                break;
            case MUERTE:
                if(e.getKeyCode() == KeyEvent.VK_ENTER){for(int i=0;i<PCS.size();i++){PCS.elementAt(i).Inicial();};xEstado = JUEGO;}
                break;
        }

        this.repaint();
    }

    public void keyReleased(KeyEvent e)
    {
    }
    
}
