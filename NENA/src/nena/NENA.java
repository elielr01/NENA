
package nena;

import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;
//import java.applet.Applet;
//import java.applet.AudioClip;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NENA extends JFrame implements Runnable, KeyListener {
    
    private final int iMAXANCHO = 10; // maximo numero de personajes por ancho
    private final int iMAXALTO = 8;  // maxuimo numero de personajes por alto
    private Base basJuanito;         // Objeto Juanito
    private Base basNena;         // Objeto Nena
    private Base basFantasma;    // Objeto Fantasma
    private int iVidas;         //Vidas del juego
    private int iScore;         //Puntos del juego
    private boolean bPause;       //Pausa el juego
    private Base basGameOver;   //Imagen de Game Over
    private int iDireccion;     //Dirección con la que se mueve Nena
    private LinkedList <Base> lklJuanitos;  //Lista de Juanitos
    private LinkedList <Base> lklFantasmas;  //Lista de Fantasmas
    private int iVelJuanito;        //Velocidades de Juantio
    private int iChoques;       //Cuantas veces Juanito choca con Nena
    private int iRandomFantasmas;
    private int iRandomJuanito;
    
    /* objetos para manejar el buffer del Applet y este no parpadee */
    private Image    imaImagenApplet;   // Imagen a proyectar en Applet	
    private Graphics graGraficaApplet;  // Objeto grafico de la Imagen
    private SoundClip adcSonidoNena1;   // Objeto sonido de Nena 1
    private SoundClip adcSonidoNena2;   // Objeto sonido de Nena 2
    private String nombreArchivo;    //Nombre del archivo.
    private String[] arr;    //Arreglo del archivo divido.

    private boolean bGuardar;     //Variable G (Guardar)
    
    /** 
     * init
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se inizializan las variables o se crean los objetos
     * a usarse en el <code>Applet</code> y se definen funcionalidades.
     * 
     */
    public void init() {
    
        nombreArchivo = "Datos.txt";
        
        // Carga en falso
        bGuardar = false;
        
        // hago el applet de un tamaño 800,500
        setSize(800,500);
        
        //Pongo las vidas al zar de 3 a 5
        iVidas = (int) (Math.random() * 3) + 3;
        
        //Inicio en 0 puntos
        iScore = 0;
        
        //El juego no inicia en pausa
        bPause = false;
        
        //Inicializo la dirección en 0 para que no se mueva Nena al principio
        iDireccion = 0;
        
        //Inicializo la velocidad de Juanito en 1
        iVelJuanito = 1;
        
        //Inicializo los choques en 0 porque apenas empieza el juego
        iChoques = 0;
        
        //Inicializo las listas de Juanitos y Fantasmas
        lklJuanitos = new LinkedList();
        lklFantasmas = new LinkedList();
        
        // defino la imagen de Nena
	URL urlNena = this.getClass().getResource("chimpy.gif");
        
        // se crea el objeto para Nena 
        int iPosX = (iMAXANCHO - 1) * getWidth() / iMAXANCHO;
        int iPosY = (iMAXALTO - 1) * getHeight() / iMAXALTO;        
	basNena = new Base(iPosX,iPosY, getWidth() / iMAXANCHO,
                getHeight() / iMAXALTO,
                Toolkit.getDefaultToolkit().getImage(urlNena));
        
        //Se reposiciona a Nena en el centro del Aplet
        basNena.setX(getWidth() / 2);
        basNena.setY(getHeight() / 2);
        
        /* Inicio con la Creación de Juanitos*/
        //Crea un número al azar para hacer Juanitos
        iRandomJuanito = (int) (Math.random() * 6) + 10;
        int iPosRan;
        for (int iI = 0; iI < iRandomJuanito; iI++) {
            //Obtengo la url de Juanito
            URL urlJuanito = this.getClass().getResource("juanito.gif");
            //Creo a un Juanito
            basJuanito = new Base (0, 0, this.getWidth() / iMAXANCHO, 
                    this.getHeight() / iMAXALTO,
                    Toolkit.getDefaultToolkit().getImage(urlJuanito));
            //Reposiciono a Juanito arriba del applet
            iPosRan = (int) (Math.random() * 6) + 1;
            iPosX = (int) (Math.random() * this.getWidth());
            iPosY = 0 - basJuanito.getAlto()*iPosRan;
            basJuanito.setX(iPosX);
            basJuanito.setY (iPosY);
            //Lo añado a la lista
            lklJuanitos.add(basJuanito);
        }
        
        /*Inicio con la Creación de Fantasmas*/
        //Creo un número al azar para hacer Fantasmas
        iRandomFantasmas = (int) (Math.random() * 3) + 8;
        for (int iI = 0; iI < iRandomFantasmas; iI++) {
            //Obtengo la url de Fantasma
            URL urlFantasma = this.getClass().getResource("fantasmita.gif");
            //Creo a un Fantasma
            basFantasma = new Base (0, 0, this.getWidth() / iMAXANCHO,
                    this.getHeight() / iMAXALTO,
                    Toolkit.getDefaultToolkit().getImage(urlFantasma));
            //Reposiciono a Fantasma a la izquierda del applet
            iPosRan = (int) (Math.random() * 4) + 1;
            iPosX = 0 - basFantasma.getAncho()*iPosRan;
            iPosY = (int) (Math.random() * this.getHeight());
            basFantasma.setX(iPosX);
            basFantasma.setY(iPosY);
            //Lo añado a la lista
            lklFantasmas.add(basFantasma);
        }
        
        
        //Creo el sonido monkey1
        URL urlSonidoNena1 = this.getClass().getResource("monkey1.wav");
        adcSonidoNena1 = new SoundClip ("monkey1.wav");
        
        //Creo el sonido monkey2
        URL urlSonidoNena2 = this.getClass().getResource("monkey2.wav");
        adcSonidoNena2 = new SoundClip ("monkey2.wav");
        adcSonidoNena2.play();
        
        //Creo la URL de la imagen del Game Over
        URL urlGameOver = this.getClass().getResource("Game_Over.png");
        
        //Creo el objeto para el Game Over
        basGameOver = new Base (0, 0, this.getWidth(), this.getHeight(),
                Toolkit.getDefaultToolkit().getImage(urlGameOver)); 
        
        addKeyListener (this);
    }
    
    /** 
     * start
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se crea e inicializa el hilo
     * para la animacion este metodo es llamado despues del init o 
     * cuando el usuario visita otra pagina y luego regresa a la pagina
     * en donde esta este <code>Applet</code>
     * 
     */
    public void start () {
        // Declaras un hilo
        Thread th = new Thread (this);
        // Empieza el hilo
        th.start ();
    }
    
    // Constructor
    public NENA() {
        setTitle("Nena vs Los Fantasmas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        init();
        start();
        
    }
    
    /** 
     * run
     * 
     * Metodo sobrescrito de la clase <code>Thread</code>.<P>
     * En este metodo se ejecuta el hilo, que contendrá las instrucciones
     * de nuestro juego.
     * 
     */
    public void run () {
        /* mientras dure el juego, se actualizan posiciones de jugadores
           se checa si hubo colisiones para desaparecer jugadores o corregir
           movimientos y se vuelve a pintar todo
        */ 
        while (iVidas > 0) {
            //Verifico si está en pausa
            if (!bPause) {
                actualiza();
                checaColision();
            }
            repaint();
            try	{
                // El thread se duerme.
                Thread.sleep (20);
            }
            catch (InterruptedException iexError) {
                System.out.println("Hubo un error en el juego " + 
                        iexError.toString());
            }
	}
    }
    
    /** 
     * actualiza
     * 
     * Metodo que actualiza la posicion de los objetos 
     * 
     */
    public void actualiza(){
        
        //Esto mueve a Nena dependiendo de iDireccion
        switch(iDireccion) {
            case 1: { //se mueve hacia arriba
                basNena.setY(basNena.getY() - 7);
                break;    
            }
            case 2: { //se mueve hacia abajo
                basNena.setY(basNena.getY() + 7);
                break;    
            }
            case 3: { //se mueve hacia izquierda
                basNena.setX(basNena.getX() - 7);
                break;    
            }
            case 4: { //se mueve hacia derecha
                basNena.setX(basNena.getX() + 7);
                break;    	
            }
        }
        
        //Mueve a cada Fantasma
        int iVelFantasma;
        for (Base basFantasma : lklFantasmas) {
            iVelFantasma = (int) (Math.random() * 3) + 3; 
            basFantasma.setX(basFantasma.getX() + iVelFantasma);
        }
        
        //Mueve a cada Juanito
        for (Base basJuanito : lklJuanitos) {
            basJuanito.setY(basJuanito.getY() + iVelJuanito);
        }
        
        //Checo si ya choqué más de 5 veces con Juanito
        if (iChoques >= 5) {
            iVidas--;
            iVelJuanito = iVelJuanito + 1;
            iChoques = 0;
        }

    }
	
    /**
     * checaColision
     * 
     * Metodo usado para checar la colision entre objetos
     * 
     */
    public void checaColision(){
        
        //Esto valida que Nena se quede dentro del applet
        if (basNena.getX() < 0) {
            basNena.setX(0);
        }
        if (basNena.getY() < 22) {
            basNena.setY(22);
        }
        if (basNena.getX() + basNena.getAncho() > this.getWidth()) {
            basNena.setX(this.getWidth() - basNena.getAncho());
        }
        if (basNena.getY() + basNena.getAlto() > this.getHeight()) {
            basNena.setY(this.getHeight() - basNena.getAlto());
        }
        
        /* Checo a cada fantasma*/
        for (Base basFantasma : lklFantasmas) {
            //Verifica cuando ya llegó hasta la derecha del applet
            if (basFantasma.getX() + basFantasma.getAncho() > 
                    this.getWidth()) {
                //Reposiciono fuera del applet del lado izquierdo
                int iRanX = (int) (Math.random() * 4) + 1;
                basFantasma.setX(0 - basFantasma.getAncho() * iRanX);
                int iRanY = (int) (Math.random() * this.getHeight());
                basFantasma.setY(iRanY);
            }
            
            //Esto checa si cada fantasma choca con Nena
            if (basFantasma.intersecta(basNena)) {
                iScore++; //Sumo 1 al puntaje
                adcSonidoNena2.play();
                //Reposiciono el fantasma fuera del applet
                int iRanX = (int) (Math.random() * 4) + 1;
                basFantasma.setX(0 - basFantasma.getAncho() * iRanX);
                int iRanY = (int) (Math.random() * this.getHeight());
                basFantasma.setY(iRanY);
            }
            //Esto verifica que no se salga del applet por arriba o por abajo
            if (basFantasma.getY() < 0){
                basFantasma.setY(0);
            }
            if (basFantasma.getY() + basFantasma.getAlto() > this.getHeight()) {
                basFantasma.setY(this.getHeight() - basFantasma.getAlto());
            }            
        }
        
        /* Checo a cada Juanito */
        for (Base basJuanito : lklJuanitos) {
            
            //Verifica cuando ya llegó a la parte de abajo del applet
            if (basJuanito.getY() + basJuanito.getAlto() > 
                    this.getHeight()) {
                //Reposiciono arriba del applet
                int iRanX = (int) (Math.random() * this.getWidth()) ;
                basJuanito.setX(iRanX);
                int iRanY = (int) (Math.random() * 6) + 1;
                basJuanito.setY(0 - basJuanito.getAlto() * iRanY);
            }
            
            //Esto checa si cada fantasma choca con Nena
            if (basJuanito.intersecta(basNena)) {
                iChoques++; //Sumo 1 a los choques
                adcSonidoNena1.play();
                //Reposiciono arriba del applet
                int iRanX = (int) (Math.random() * this.getWidth()) ;
                basJuanito.setX(iRanX);
                int iRanY = (int) (Math.random() * 6) + 1;
                basJuanito.setY(0 - basJuanito.getAlto() * iRanY);
            }
            
            //Esto verifica que no se salga del applet por izq o por derecha
            if (basFantasma.getX() < 0){
                basFantasma.setX(0);
            }
            if (basFantasma.getX() + basFantasma.getAncho() > this.getWidth()) {
                basFantasma.setY(this.getWidth() - basFantasma.getAncho());
            }                
            
        }

    }
    
    
    /**
     * paint1
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo se dibuja la imagen con la posicion actualizada,
     * ademas que cuando la imagen es cargada te despliega una advertencia.
     * 
     * @param graDibujo es el objeto de <code>Graphics</code> usado para dibujar.
     * 
     */
    public void paint1(Graphics graDibujo) {
        
        //Checo si no se he perdido (vidas =0)
        if (iVidas > 0) {
            
            // si la imagen ya se cargo
            if (basNena != null && lklJuanitos != null && lklFantasmas != null) {
                
                //Dibuja a Nena en el Applet
                basNena.paint(graDibujo, this);
                
                //Dibujo los Juanitos
                for (Base basJuanito : lklJuanitos) {
                    basJuanito.paint(graDibujo, this);
                }
                
                //Dibujo los Fantasmas
                for (Base basFantasma : lklFantasmas) {
                    basFantasma.paint(graDibujo, this);
                }

            } // sino se ha cargado se dibuja un mensaje 
            else {
                    //Da un mensaje mientras se carga el dibujo	
                    graDibujo.drawString("No se cargo la imagen..", 20, 20);
            }
            
            if (bPause) {
                graDibujo.setColor(Color.red);
                graDibujo.drawString("Paused", this.getWidth() / 2,
                        this.getHeight() / 2);
            }
            
            graDibujo.setColor(Color.red);
            graDibujo.drawString("Puntos: " + iScore, 700, 40);
            graDibujo.setColor(Color.red);
            graDibujo.drawString ("Vidas: " + iVidas, 700, 60);
            
        }
        else {
            //Dibujo el Game Over cuando pierdo
            basGameOver.paint(graDibujo, this);
        }

    }
    
    public void paint(Graphics graGrafico){

        // Inicializan el DoubleBuffer
        if (imaImagenApplet == null){
                imaImagenApplet = createImage (this.getSize().width, 
                        this.getSize().height);
                graGraficaApplet = imaImagenApplet.getGraphics ();
        }

        // Actualiza la imagen de fondo.
        URL urlImagenFondo = this.getClass().getResource("Ciudad.png");
        Image imaImagenFondo = Toolkit.getDefaultToolkit().getImage(urlImagenFondo);
        graGraficaApplet.drawImage(imaImagenFondo, 0, 0, getWidth(), getHeight(), this);

        // Actualiza el Foreground.
        graGraficaApplet.setColor (getForeground());
        paint1(graGraficaApplet);

        // Dibuja la imagen actualizada
        graGrafico.drawImage (imaImagenApplet, 0, 0, this);
        
    }
   
    /**
     * Metodo que lee la informacion de un archivo.
     *
     * @throws IOException
     */
    private void leeArchivo() throws IOException{
        BufferedReader fileIn;
    	try{
    		fileIn = new BufferedReader(new FileReader(nombreArchivo));
    	} catch (FileNotFoundException e){
    		File datos = new File(nombreArchivo);
    		PrintWriter fileOut = new PrintWriter(datos);
    		fileOut.println("100,demo");
    		fileOut.close();
    		fileIn = new BufferedReader(new FileReader(nombreArchivo));
    	}
        
    	String dato = fileIn.readLine();
        // Lee la cantidad de vidas
        iVidas = (Integer.parseInt(dato));
        
        dato = fileIn.readLine();
        
        // Lee el score
        iScore = (Integer.parseInt(dato));
        
        dato = fileIn.readLine();
        
        // Lee la direccion
        iDireccion = (Integer.parseInt(dato));
 
        dato = fileIn.readLine();
        
        // Lee a Nena
        arr = dato.split(" ");
        basNena.setX(Integer.parseInt(arr[0]));
        basNena.setY(Integer.parseInt(arr[1]));
        
        String dato2 = new String();
        dato2 = fileIn.readLine();
         
        // Lee los fantasmas
        for (int iI = 0; iI < (Integer.parseInt(dato2)); iI++) {
            dato = fileIn.readLine();
            arr = dato.split(" ");
            lklFantasmas.get(iI).setX(Integer.parseInt(arr[0]));
            lklFantasmas.get(iI).setY(Integer.parseInt(arr[1]));
        }
        
         dato2 = fileIn.readLine();
         
        // Lee los juanitos
        for (int iI = 0; iI < (Integer.parseInt(dato2)); iI++) {
            dato = fileIn.readLine();
            arr = dato.split(" ");
            lklJuanitos.get(iI).setX(Integer.parseInt(arr[0]));
            lklJuanitos.get(iI).setY(Integer.parseInt(arr[1]));
        }
        
        // Lee si esta pausado o no
        dato = fileIn.readLine();
        bPause = (Boolean.parseBoolean(dato));
        
    
    	fileIn.close();
    }
    /**
     * Metodo graba información en un archivo.
     *
     * @throws IOException
     */
    private void grabaArchivo() throws IOException {
        PrintWriter fileOut = new PrintWriter(new FileWriter(nombreArchivo));
        
        // Guarda cantidad de vidas
        fileOut.println(iVidas);
        
        // Guarda el score
        fileOut.println(iScore);
        
        // Guarda la dirección
        fileOut.println(iDireccion);
        
        // Guarda a Nena
        fileOut.println(basNena.getX() + " " + basNena.getY());
        
        // Guarda la cantidad de Fantasmas
        fileOut.println(iRandomFantasmas);
        
        // Guarda cada Fantasma
        for (Base basFantasma : lklFantasmas) {
            fileOut.println(basFantasma.getX() + " " + basFantasma.getY());
        }
        
        // Guarda la cantidad de Juanitos
        fileOut.println(iRandomJuanito);
        
        // Guarda cada Juanito
        for (Base basJuanito : lklJuanitos) {
            fileOut.println(basJuanito.getX() + " " + basJuanito.getY());
        }
        
        // Guarda si está pausado o no
        fileOut.println(bPause);
        
    	fileOut.close();	        
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent keyEvento) {

        //Verifico que se haya presionado la tecla G
        if (keyEvento.getKeyCode() == KeyEvent.VK_G) {
            try {
                grabaArchivo();
            } catch (IOException ex) {
                Logger.getLogger(NENA.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       else if (keyEvento.getKeyCode() == KeyEvent.VK_C) {
            try {
                leeArchivo();
            } catch (IOException ex) {
                Logger.getLogger(NENA.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
        
        //Verifico que se haya presionado la tecla P
        if (keyEvento.getKeyCode() == KeyEvent.VK_P) {
            bPause = !bPause;
        }
        
        //Verifico que se haya presionado la tecla ESC
        if (keyEvento.getKeyCode() == KeyEvent.VK_ESCAPE) {
            iVidas = 0;
        }
        
        // La siguiente parte sirve para mover a Nena con W, A, S, D        
        // si presiono flecha para arriba
        if(keyEvento.getKeyCode() == KeyEvent.VK_W) {    
                iDireccion = 1;  // cambio la dirección arriba
        }
        // si presiono flecha para abajo
        else if(keyEvento.getKeyCode() == KeyEvent.VK_S) {    
                iDireccion = 2;   // cambio la direccion para abajo
        }
        // si presiono flecha a la izquierda
        else if(keyEvento.getKeyCode() == KeyEvent.VK_A) {    
                iDireccion = 3;   // cambio la direccion a la izquierda
        }
        // si presiono flecha a la derecha
        else if(keyEvento.getKeyCode() == KeyEvent.VK_D){    
                iDireccion = 4;   // cambio la direccion a la derecha
        }
        
    }


    public static void main(String[] args) {
        //Crea un nuevo objeto JFrameHolaMundo
        NENA juego = new NENA();
        //Despliega la ventana en pantalla al hacerla visible
        juego.setVisible(true);
        
        juego.setSize(800,500);
    }
    
}
