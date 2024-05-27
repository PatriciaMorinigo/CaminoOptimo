package forms;

import algoritmos.BusquedaIterativa;
import com.formdev.flatlaf.FlatDarculaLaf;
import db.ConexionDb;
import entidades.Config;
import entidades.DatosBusqueda;
import entidades.Nodo;
import entidades.Vecino;
import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import utils.ExportaRecorrido;
import utils.GeneradorMapa;
import utils.LecturaCSV;
import utils.OperacionesDb;

/**
 * Menu principal del programa
 *
 * @author Usuario
 */
public class Menu extends javax.swing.JFrame {

    private static String nombreArchivo;
    public static final Connection conn = ConexionDb.getConnection();
    private volatile boolean detenerEjecucion = false;

    public Menu() {
        initComponents();
        inicializarOrigen();
        cargaRegistros();
    }

    private void cargaRegistros() {
        try {
            DefaultTableModel tb_itinerario_uno = (DefaultTableModel) TB_Itinerario_Uno.getModel();
            DefaultTableModel tb_itinerario_dos = (DefaultTableModel) TB_Itinerario_Dos.getModel();
            DefaultTableModel tb_itinerario_tres = (DefaultTableModel) TB_Itinerario_Tres.getModel();
            DefaultTableModel tb_itinerario_centro = (DefaultTableModel) TB_Itinerario_Centro.getModel();
            
            tb_itinerario_uno.setRowCount(0);
            tb_itinerario_dos.setRowCount(0);
            tb_itinerario_tres.setRowCount(0);
            tb_itinerario_centro.setRowCount(0);

            ResultSet registros = OperacionesDb.obtieneRegistros(conn);
            while (registros.next()) {
                String calleInicio = OperacionesDb.obtieneCalleInicial(registros.getInt("COD_RECORRIDO_CAB"), conn);
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                Date date = new Date();
                
                //HORA DE INICIO
                date.setHours(registros.getInt("SEGUNDO_INICIO") / 3600);
                date.setMinutes((registros.getInt("SEGUNDO_INICIO") % 3600) / 60);
                date.setSeconds((registros.getInt("SEGUNDO_INICIO") % 3600) % 60);
                String horaInicio = dateFormat.format(date);

                //HORA DE FINALIZACIÓN
                date.setHours(registros.getInt("SEGUNDO_FINAL") / 3600);
                date.setMinutes((registros.getInt("SEGUNDO_FINAL") % 3600) / 60);
                date.setSeconds((registros.getInt("SEGUNDO_FINAL") % 3600) % 60);
                String horaFin = dateFormat.format(date);

                //DURACIÓN TOTAL
                int duracionTotal = 0;
                if (registros.getInt("SEGUNDO_INICIO") < registros.getInt("SEGUNDO_FINAL")) {
                    duracionTotal = registros.getInt("SEGUNDO_FINAL") - registros.getInt("SEGUNDO_INICIO");
                } else if (registros.getInt("SEGUNDO_INICIO") > registros.getInt("SEGUNDO_FINAL")) {
                    duracionTotal = (86400 - registros.getInt("SEGUNDO_INICIO")) + registros.getInt("SEGUNDO_FINAL");
                }
                
                date.setHours(duracionTotal / 3600);
                date.setMinutes((duracionTotal % 3600) / 60);
                date.setSeconds((duracionTotal % 3600) % 60);
                String duracion = dateFormat.format(date);
                
                Object[] fila = new Object[]{calleInicio, registros.getInt("COSTO"), registros.getInt("NODOS_RECORRIDOS"), horaInicio, horaFin, registros.getInt("COD_RECORRIDO_CAB"), duracion};
                
                if (registros.getString("NOMBRE_ARCHIVO").equals(Config.getArchivoIntinerarioUno())) {
                    tb_itinerario_uno.addRow(fila);
                } else if (registros.getString("NOMBRE_ARCHIVO").equals(Config.getArchivoIntinerarioDos())) {
                    tb_itinerario_dos.addRow(fila);
                } else if (registros.getString("NOMBRE_ARCHIVO").equals(Config.getArchivoIntinerarioTres())) {
                    tb_itinerario_tres.addRow(fila);
                } else {
                    tb_itinerario_centro.addRow(fila);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void inicializarOrigen() {
        ArrayList<String> listaCalles = LecturaCSV.obtienePuntosSalida();
        for (String calle : listaCalles) {
            CB_Origen.addItem(calle);
        }
    }

    private void bloquearCampos() {
        RB_Manual.setEnabled(false);
        RB_Automatico.setEnabled(false);
        CB_Origen.setEnabled(false);
        SP_Hora.setEnabled(false);
        SP_Minuto.setEnabled(false);
        BT_Buscar.setEnabled(false);
        RB_Intenerario_Uno.setEnabled(false);
        RB_Intenerario_Dos.setEnabled(false);
        RB_Intenerario_Tres.setEnabled(false);
        RB_Centro.setEnabled(false);
    }

    private void desbloquearCampos() {
        RB_Manual.setEnabled(true);
        RB_Automatico.setEnabled(true);
        if (!RB_Automatico.isSelected()) {
            CB_Origen.setEnabled(true);
        }
        SP_Hora.setEnabled(true);
        SP_Minuto.setEnabled(true);
        BT_Buscar.setEnabled(true);
        RB_Intenerario_Uno.setEnabled(true);
        RB_Intenerario_Dos.setEnabled(true);
        RB_Intenerario_Tres.setEnabled(true);
        RB_Centro.setEnabled(true);        
    }

    /**
     * Funcion utilizada para buscar un recorrido optimo apartir de un punto de
     * salida establecido por el usuario
     */
    public void buscarCaminoManual() {
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(() -> {
            int segundoInicial = ((Integer) SP_Hora.getValue() * 3600) + ((Integer) SP_Minuto.getValue() * 60);
            int segundoActual = segundoInicial;

            ArrayList<Nodo> Grafo = LecturaCSV.crearGrafo(nombreArchivo);
            ArrayList<Nodo> nodosRecorridos = new ArrayList();
            ArrayList<DatosBusqueda> listaResultados = new ArrayList();

            Nodo nodoOrigen = obtieneNodo(LecturaCSV.obtieneNodoSalida(CB_Origen.getSelectedItem().toString()), Grafo);

            SimpleDateFormat Df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            TA_Recorrido.setCaretPosition(TA_Recorrido.getDocument().getLength());
            try {
                while (!detenerEjecucion) {
                    boolean ind_salto = false;
                    
                    int costoActual = 0;
                    segundoActual = segundoInicial;
                    nodosRecorridos = new ArrayList();
                    nodosRecorridos.clear();
                    nodosRecorridos.add(nodoOrigen);
                    while (!verificaGrafo(nodosRecorridos, Grafo)) {
                        nodosRecorridos.add(BusquedaIterativa.BusquedaPorMenorCosto(nodosRecorridos, Grafo, segundoActual));
                        costoActual += obtieneCosto(nodosRecorridos, segundoActual);
                        segundoActual += convierteCosto(nodosRecorridos, segundoActual);
                        if (segundoActual >= 86400) {
                            segundoActual = segundoActual - 86400;
                        }
                        
                        if (nodosRecorridos.size() >= 5000) {
                            ind_salto = true;
                            ExportaRecorrido.generaHtml(nodosRecorridos, costoActual);
                            System.out.println("Html Generado");
                            break;
                        }
                    }

                    if (!ind_salto && (listaResultados.isEmpty() || costoActual < listaResultados.get(listaResultados.size() - 1).getCostoTotal())) {
                        TF_NodosRecorridos.setText(String.valueOf(nodosRecorridos.size()));
                        TF_CostoDeseado.setText(String.valueOf(costoActual));                        
                        TA_Recorrido.setText(TA_Recorrido.getText() + Df.format(new Date()) + " Nodos Recorridos -> " + nodosRecorridos.size() + "\n");
                        TA_Recorrido.setText(TA_Recorrido.getText() + Df.format(new Date()) + " Costo del Recorrido -> " + costoActual + "\n");
                        TA_Recorrido.setText(TA_Recorrido.getText() + "------------------------------------------------------------------\n");
                        TA_Recorrido.setCaretPosition(TA_Recorrido.getDocument().getLength());
                        DatosBusqueda db = new DatosBusqueda(nodosRecorridos, costoActual, segundoInicial, segundoActual);
                        listaResultados.add(db);
                    }
                }
            } catch (Exception ex) {
                System.out.println("Ocurrió un error al obtener el recorrido --> " + ex);
                System.out.println("Segundo Actual: " + segundoActual);
                System.out.println("Indice: " + (segundoActual * 48) / 86400);
                System.out.println("Ultimo Nodo:" + nodosRecorridos.get(nodosRecorridos.size() - 1).getNodo());
            }

            JOptionPane.showMessageDialog(null, "Busqueda de recorrido detenida.");
            TA_Recorrido.setText(null);
            TA_Recorrido.setText(TA_Recorrido.getText() + "------------------ RESUMEN DEL RECORRIDO ------------------\n");
            TA_Recorrido.setText(TA_Recorrido.getText() + "Resultados Procesados: " + listaResultados.size() + "\n");
            TA_Recorrido.setText(TA_Recorrido.getText() + "Resultado mas óptimo obtenido: " + listaResultados.get(listaResultados.size() - 1).getCostoTotal() + "\n");
            TA_Recorrido.setText(TA_Recorrido.getText() + "Hora de inicio del recorrido: " + (listaResultados.get(listaResultados.size() - 1).getSegundoInicio() / 3600) + ":" + ((listaResultados.get(listaResultados.size() - 1).getSegundoInicio() % 3600) / 60) + ":" + ((listaResultados.get(listaResultados.size() - 1).getSegundoInicio() % 3600) % 60) + "\n");
            TA_Recorrido.setText(TA_Recorrido.getText() + "Hora final del recorrido: " + (listaResultados.get(listaResultados.size() - 1).getSegundoFinal() / 3600) + ":" + ((listaResultados.get(listaResultados.size() - 1).getSegundoFinal() % 3600) / 60) + ":" + ((listaResultados.get(listaResultados.size() - 1).getSegundoFinal() % 3600) % 60) + "\n");
            int cod_recorrido_cab = OperacionesDb.insertaDb(listaResultados.get(listaResultados.size() - 1), conn, nombreArchivo);
            if (cod_recorrido_cab > 0) {
                GeneradorMapa.generar(listaResultados.get(listaResultados.size() - 1).getRecorrido(), cod_recorrido_cab);
            }
            cargaRegistros();
            desbloquearCampos();
        });
    }

    /**
     * Funcion utilizada para buscar un recorrido optimo desde todos los puntos
     * de salida disponibles
     *
     * @return void
     */
    private void buscarCaminoAutomatico() {
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(() -> {
            int segundoInicial = ((Integer) SP_Hora.getValue() * 3600) + ((Integer) SP_Minuto.getValue() * 60);
            int segundoActual = segundoInicial;

            ArrayList<Nodo> Grafo = LecturaCSV.crearGrafo(nombreArchivo);
            ArrayList<Nodo> nodosRecorridos = new ArrayList();
            ArrayList<DatosBusqueda> listaResultados = new ArrayList();

            SimpleDateFormat Df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            TA_Recorrido.setCaretPosition(TA_Recorrido.getDocument().getLength());
            try {
                while (!detenerEjecucion) {
                    for (String calle : LecturaCSV.obtienePuntosSalida()) {                        
                        boolean ind_salto = false;
                        
                        if (detenerEjecucion) {
                            break;
                        }
                        
                        int costoActual = 0;
                        segundoActual = segundoInicial;
                        Nodo nodoOrigen = obtieneNodo(LecturaCSV.obtieneNodoSalida(calle), Grafo);
                        TA_Recorrido.setText(TA_Recorrido.getText() + Df.format(new Date()) + " -> Calle: " + calle + "\n");
                        TA_Recorrido.setCaretPosition(TA_Recorrido.getDocument().getLength());
                        nodosRecorridos = new ArrayList();
                        nodosRecorridos.clear();
                        nodosRecorridos.add(nodoOrigen);
                        while (!verificaGrafo(nodosRecorridos, Grafo)) {
                            nodosRecorridos.add(BusquedaIterativa.BusquedaPorMenorCosto(nodosRecorridos, Grafo, segundoActual));
                            costoActual += obtieneCosto(nodosRecorridos, segundoActual);
                            segundoActual += convierteCosto(nodosRecorridos, segundoActual);
                            if (segundoActual >= 86400) {
                                segundoActual = segundoActual - 86400;
                            }
                            
                            if (nodosRecorridos.size() >= 5000) {
                                ind_salto = true;
                                ExportaRecorrido.generaHtml(nodosRecorridos, costoActual);
                                System.out.println("Html Generado");                                
                                break;
                            }
                        }

                        TA_Recorrido.setText(TA_Recorrido.getText() + Df.format(new Date()) + " Nodos Recorridos -> " + nodosRecorridos.size() + "\n");
                        TA_Recorrido.setText(TA_Recorrido.getText() + Df.format(new Date()) + " Costo del Recorrido -> " + costoActual + "\n");
                        TA_Recorrido.setText(TA_Recorrido.getText() + "------------------------------------------------------------------\n");

                        if (!ind_salto && (listaResultados.isEmpty() || costoActual < listaResultados.get(listaResultados.size() - 1).getCostoTotal())) {
                            TF_NodosRecorridos.setText(String.valueOf(nodosRecorridos.size()));
                            TF_CostoDeseado.setText(String.valueOf(costoActual));
                            CB_Origen.setSelectedIndex(indiceCalle(LecturaCSV.obtienePuntosSalida(), calle));
                            DatosBusqueda db = new DatosBusqueda(nodosRecorridos, costoActual, segundoInicial, segundoActual);
                            listaResultados.add(db);
                            ExportaRecorrido.generaHtml(nodosRecorridos, costoActual);
                        }
                        TA_Recorrido.setCaretPosition(TA_Recorrido.getDocument().getLength());
                        if (detenerEjecucion) {
                            break;
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println("Ocurrió un error al obtener el recorrido --> " + ex);
                System.out.println("Segundo Actual: " + segundoActual);
                System.out.println("Indice: " + (segundoActual * 48) / 86400);
                System.out.println("Ultimo Nodo:" + nodosRecorridos.get(nodosRecorridos.size() - 1).getNodo());
            }

            JOptionPane.showMessageDialog(null, "Busqueda de recorrido detenida.");
            TA_Recorrido.setText(TA_Recorrido.getText() + "------------------ RESUMEN DEL RECORRIDO ------------------\n");
            TA_Recorrido.setText(TA_Recorrido.getText() + "Resultados Procesados: " + listaResultados.size() + "\n");
            TA_Recorrido.setText(TA_Recorrido.getText() + "Resultado mas óptimo obtenido: " + listaResultados.get(listaResultados.size() - 1).getCostoTotal() + "\n");
            TA_Recorrido.setText(TA_Recorrido.getText() + "Hora de inicio del recorrido: " + (listaResultados.get(listaResultados.size() - 1).getSegundoInicio() / 3600) + ":" + ((listaResultados.get(listaResultados.size() - 1).getSegundoInicio() % 3600) / 60) + ":" + ((listaResultados.get(listaResultados.size() - 1).getSegundoInicio() % 3600) % 60) + "\n");
            TA_Recorrido.setText(TA_Recorrido.getText() + "Hora final del recorrido: " + (listaResultados.get(listaResultados.size() - 1).getSegundoFinal() / 3600) + ":" + ((listaResultados.get(listaResultados.size() - 1).getSegundoFinal() % 3600) / 60) + ":" + ((listaResultados.get(listaResultados.size() - 1).getSegundoFinal() % 3600) % 60) + "\n");
            TA_Recorrido.setCaretPosition(TA_Recorrido.getDocument().getLength());
            int cod_recorrido_cab = OperacionesDb.insertaDb(listaResultados.get(listaResultados.size() - 1), conn, nombreArchivo);
            if (cod_recorrido_cab > 0) {
                GeneradorMapa.generar(listaResultados.get(listaResultados.size() - 1).getRecorrido(), cod_recorrido_cab);
            }
            cargaRegistros();
            desbloquearCampos();
        });
    }

    /**
     * Obtiene nodo a partir de la etiqueta
     *
     * @param nodo
     * @param Grafo
     * @return
     */
    private Nodo obtieneNodo(String nodo, ArrayList<Nodo> Grafo) {
        for (Nodo N : Grafo) {
            if (N.getNodo().equals(nodo)) {
                return N;
            }
        }
        return null;
    }

    /**
     * Verifica que el grafo se haya recorrido en la totalidad
     *
     * @param nodosRecorridos
     * @param Grafo
     * @return
     */
    private boolean verificaGrafo(ArrayList<Nodo> nodosRecorridos, ArrayList<Nodo> Grafo) {
        for (Nodo N : Grafo) {
            if (!nodosRecorridos.contains(N) && N.getInd_obligatorio().equals("S")) {
                return false;
            }
        }
        return true;
    }

    /**
     * Se obtiene la equivalencia en segundos del costo
     *
     * @param nodosRecorridos
     * @param segundoActual
     * @return
     */
    private static int convierteCosto(ArrayList<Nodo> nodosRecorridos, int segundoActual) {
        Nodo nodoOrigen = nodosRecorridos.get(nodosRecorridos.size() - 2);
        Nodo nodoDestino = nodosRecorridos.get(nodosRecorridos.size() - 1);
        int indice = ((segundoActual * 48) / 86400);
        for (Vecino v : nodoOrigen.getVecinos()) {
            if (v.getNodo().equals(nodoDestino.getNodo())) {
                int costo = v.getCostos()[indice];
                switch (costo) {
                    case 1:
                        return 1;
                    case 2:
                        return 7;
                    case 3:
                        return 10;
                    case 4:
                        return 33;
                    case 5:
                        return 40;
                    case 6:
                        return 50;
                    case 7:
                        return 60;
                    case 8:
                        return 63;
                    case 9:
                        return 67;
                    case 10:
                        return 70;
                    case 11:
                        return 75;
                    case 12:
                        return 80;
                    case 13:
                        return 90;
                }
            }
        }
        return 0;
    }

    /**
     * Funcion que calcula el costo total desde una lista de nodos
     *
     * @param nodosRecorridos
     * @param archivo
     * @return int
     */
    private int obtieneCosto(ArrayList<Nodo> nodosRecorridos, int segundoActual) {
        Nodo nodoOrigen = nodosRecorridos.get(nodosRecorridos.size() - 2);
        Nodo nodoDestino = nodosRecorridos.get(nodosRecorridos.size() - 1);
        int indice = ((segundoActual * 48) / 86400);
        for (Vecino v : nodoOrigen.getVecinos()) {
            if (v.getNodo().equals(nodoDestino.getNodo())) {
                return v.getCostos()[indice];
            }
        }
        return 0;
    }

    /**
     * Función que devuelve el indice para la actualización de la lista de calles al encontrar un mejor recorrido desde otro punto de salida
     * 
     * @param calles
     * @param calleSeleccionada
     * @return 
     */
    private int indiceCalle(ArrayList<String> calles, String calleSeleccionada) {
        if (calles.isEmpty()) {
            return 0;
        }
        
        int indice = 0;
        for (String calle : calles) {
            if (calle.equals(calleSeleccionada)) {
                return indice;
            }
            indice ++;
        }
        return 0;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        BG_Busquedas = new javax.swing.ButtonGroup();
        BG_Intinerarios = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TA_Recorrido = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        RB_Automatico = new javax.swing.JRadioButton();
        RB_Manual = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        SP_Hora = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        SP_Minuto = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        CB_Origen = new javax.swing.JComboBox<>();
        BT_Buscar = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        TF_CostoDeseado = new javax.swing.JTextField();
        BT_Detener = new javax.swing.JButton();
        TF_NodosRecorridos = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TB_Itinerario_Uno = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        TB_Itinerario_Dos = new javax.swing.JTable();
        jScrollPane7 = new javax.swing.JScrollPane();
        TB_Itinerario_Tres = new javax.swing.JTable();
        jScrollPane8 = new javax.swing.JScrollPane();
        TB_Itinerario_Centro = new javax.swing.JTable();
        PN_Preview = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        RB_Intenerario_Tres = new javax.swing.JRadioButton();
        RB_Intenerario_Uno = new javax.swing.JRadioButton();
        RB_Intenerario_Dos = new javax.swing.JRadioButton();
        RB_Centro = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Recorrido Óptimo - Menú");

        jLabel1.setFont(new java.awt.Font("Noto Sans", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Búsqueda del recorrido óptimo");

        TA_Recorrido.setEditable(false);
        TA_Recorrido.setColumns(20);
        TA_Recorrido.setLineWrap(true);
        TA_Recorrido.setRows(5);
        TA_Recorrido.setWrapStyleWord(true);
        TA_Recorrido.setAutoscrolls(false);
        jScrollPane1.setViewportView(TA_Recorrido);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Tipo de Búsqueda"));

        BG_Busquedas.add(RB_Automatico);
        RB_Automatico.setText("Automático");
        RB_Automatico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RB_AutomaticoActionPerformed(evt);
            }
        });

        BG_Busquedas.add(RB_Manual);
        RB_Manual.setSelected(true);
        RB_Manual.setText("Manual");
        RB_Manual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RB_ManualActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(RB_Manual)
                .addGap(18, 18, 18)
                .addComponent(RB_Automatico)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RB_Manual)
                    .addComponent(RB_Automatico)))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos Iniciales"));

        jLabel2.setText("Calle inicial del recorrido");

        SpinnerNumberModel modeloSpinnerHoras = new SpinnerNumberModel();
        modeloSpinnerHoras.setMaximum(23);
        modeloSpinnerHoras.setMinimum(0);
        SP_Hora.setModel(modeloSpinnerHoras);
        SP_Hora.setModel(new javax.swing.SpinnerNumberModel(18, 0, 23, 1));

        jLabel3.setText(":");

        SpinnerNumberModel modeloSpinnerMinutos = new SpinnerNumberModel();
        modeloSpinnerMinutos.setMaximum(59);
        modeloSpinnerMinutos.setMinimum(0);
        SP_Minuto.setModel(modeloSpinnerMinutos);

        jLabel4.setText("Hora de inicio del recorrido");

        BT_Buscar.setText("Buscar");
        BT_Buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BT_BuscarActionPerformed(evt);
            }
        });

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("MC");

        TF_CostoDeseado.setEnabled(false);

        BT_Detener.setText("Detener");
        BT_Detener.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BT_DetenerActionPerformed(evt);
            }
        });

        TF_NodosRecorridos.setEnabled(false);

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("NR");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(CB_Origen, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(TF_CostoDeseado)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TF_NodosRecorridos, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(SP_Hora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(SP_Minuto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(BT_Buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BT_Detener, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CB_Origen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TF_CostoDeseado)
                            .addComponent(TF_NodosRecorridos))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SP_Hora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(SP_Minuto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BT_Buscar)
                    .addComponent(BT_Detener))
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Recorridos Registrados"));

        TB_Itinerario_Uno.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Calle Inicial", "Costo", "Nodos Recorridos", "Hora de Inicio", "Hora Finalización", "Código", "Duración Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TB_Itinerario_Uno.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        TB_Itinerario_Uno.setSurrendersFocusOnKeystroke(true);
        TB_Itinerario_Uno.getTableHeader().setReorderingAllowed(false);
        TB_Itinerario_Uno.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TB_Itinerario_UnoMouseClicked(evt);
            }
        });
        TB_Itinerario_Uno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TB_Itinerario_UnoKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(TB_Itinerario_Uno);
        if (TB_Itinerario_Uno.getColumnModel().getColumnCount() > 0) {
            TB_Itinerario_Uno.getColumnModel().getColumn(5).setMinWidth(0);
            TB_Itinerario_Uno.getColumnModel().getColumn(5).setPreferredWidth(0);
            TB_Itinerario_Uno.getColumnModel().getColumn(5).setMaxWidth(0);
        }

        TB_Itinerario_Dos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Calle Inicial", "Costo", "Nodos Recorridos", "Hora de Inicio", "Hora Finalización", "Código", "Duración Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TB_Itinerario_Dos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        TB_Itinerario_Dos.setSurrendersFocusOnKeystroke(true);
        TB_Itinerario_Dos.getTableHeader().setReorderingAllowed(false);
        TB_Itinerario_Dos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TB_Itinerario_DosMouseClicked(evt);
            }
        });
        TB_Itinerario_Dos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TB_Itinerario_DosKeyReleased(evt);
            }
        });
        jScrollPane6.setViewportView(TB_Itinerario_Dos);
        if (TB_Itinerario_Dos.getColumnModel().getColumnCount() > 0) {
            TB_Itinerario_Dos.getColumnModel().getColumn(5).setMinWidth(0);
            TB_Itinerario_Dos.getColumnModel().getColumn(5).setPreferredWidth(0);
            TB_Itinerario_Dos.getColumnModel().getColumn(5).setMaxWidth(0);
        }

        TB_Itinerario_Tres.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Calle Inicial", "Costo", "Nodos Recorridos", "Hora de Inicio", "Hora Finalización", "Código", "Duración Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TB_Itinerario_Tres.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        TB_Itinerario_Tres.setSurrendersFocusOnKeystroke(true);
        TB_Itinerario_Tres.getTableHeader().setReorderingAllowed(false);
        TB_Itinerario_Tres.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TB_Itinerario_TresMouseClicked(evt);
            }
        });
        TB_Itinerario_Tres.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TB_Itinerario_TresKeyReleased(evt);
            }
        });
        jScrollPane7.setViewportView(TB_Itinerario_Tres);
        if (TB_Itinerario_Tres.getColumnModel().getColumnCount() > 0) {
            TB_Itinerario_Tres.getColumnModel().getColumn(5).setMinWidth(0);
            TB_Itinerario_Tres.getColumnModel().getColumn(5).setPreferredWidth(0);
            TB_Itinerario_Tres.getColumnModel().getColumn(5).setMaxWidth(0);
        }

        TB_Itinerario_Centro.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Calle Inicial", "Costo", "Nodos Recorridos", "Hora de Inicio", "Hora Finalización", "Código", "Duración Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TB_Itinerario_Centro.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        TB_Itinerario_Centro.setSurrendersFocusOnKeystroke(true);
        TB_Itinerario_Centro.getTableHeader().setReorderingAllowed(false);
        TB_Itinerario_Centro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TB_Itinerario_CentroMouseClicked(evt);
            }
        });
        TB_Itinerario_Centro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                TB_Itinerario_CentroKeyReleased(evt);
            }
        });
        jScrollPane8.setViewportView(TB_Itinerario_Centro);
        if (TB_Itinerario_Centro.getColumnModel().getColumnCount() > 0) {
            TB_Itinerario_Centro.getColumnModel().getColumn(5).setMinWidth(0);
            TB_Itinerario_Centro.getColumnModel().getColumn(5).setPreferredWidth(0);
            TB_Itinerario_Centro.getColumnModel().getColumn(5).setMaxWidth(0);
        }

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 702, Short.MAX_VALUE)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 702, Short.MAX_VALUE)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 702, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PN_Preview.setBorder(javax.swing.BorderFactory.createTitledBorder("Vista Previa del Recorrido"));

        javax.swing.GroupLayout PN_PreviewLayout = new javax.swing.GroupLayout(PN_Preview);
        PN_Preview.setLayout(PN_PreviewLayout);
        PN_PreviewLayout.setHorizontalGroup(
            PN_PreviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        PN_PreviewLayout.setVerticalGroup(
            PN_PreviewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 265, Short.MAX_VALUE)
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Intinerario"));

        BG_Intinerarios.add(RB_Intenerario_Tres);
        RB_Intenerario_Tres.setText("Avenidas");

        BG_Intinerarios.add(RB_Intenerario_Uno);
        RB_Intenerario_Uno.setSelected(true);
        RB_Intenerario_Uno.setText("Antequera y paralelas");

        BG_Intinerarios.add(RB_Intenerario_Dos);
        RB_Intenerario_Dos.setText("Constitución y paralelas");

        BG_Intinerarios.add(RB_Centro);
        RB_Centro.setText("Zona Centro");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(RB_Intenerario_Uno)
                    .addComponent(RB_Intenerario_Tres))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(RB_Centro)
                    .addComponent(RB_Intenerario_Dos))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RB_Intenerario_Uno)
                    .addComponent(RB_Intenerario_Dos))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RB_Intenerario_Tres)
                    .addComponent(RB_Centro)))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PN_Preview, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PN_Preview, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void RB_ManualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RB_ManualActionPerformed
        CB_Origen.setEnabled(true);
    }//GEN-LAST:event_RB_ManualActionPerformed

    private void RB_AutomaticoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RB_AutomaticoActionPerformed
        CB_Origen.setEnabled(false);
    }//GEN-LAST:event_RB_AutomaticoActionPerformed

    private void BT_BuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BT_BuscarActionPerformed
        //Selección del archivo
        if (RB_Intenerario_Uno.isSelected()) {
            nombreArchivo = Config.getArchivoIntinerarioUno();
        }
        if (RB_Intenerario_Dos.isSelected()) {
            nombreArchivo = Config.getArchivoIntinerarioDos();
        }
        if (RB_Intenerario_Tres.isSelected()) {
            nombreArchivo = Config.getArchivoIntinerarioTres();
        }
        if (RB_Centro.isSelected()) {
            nombreArchivo = Config.getArchivoIntinerarioCompleto();
        }

        detenerEjecucion = false;
        bloquearCampos();
        TA_Recorrido.setText(null);
        if (RB_Manual.isSelected()) {
            buscarCaminoManual();
        } else {
            buscarCaminoAutomatico();
        }
    }//GEN-LAST:event_BT_BuscarActionPerformed

    private void BT_DetenerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BT_DetenerActionPerformed
        detenerEjecucion = true;
        desbloquearCampos();
    }//GEN-LAST:event_BT_DetenerActionPerformed

    private void TB_Itinerario_UnoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TB_Itinerario_UnoMouseClicked
        int cod_registro_cab = Integer.parseInt(String.valueOf(TB_Itinerario_Uno.getValueAt(TB_Itinerario_Uno.getSelectedRow(), 5)));
        
        if (cod_registro_cab > 0) {
            if (evt.getClickCount() == 1) {
                PN_Preview.removeAll();
                PN_Preview.setLayout(new BorderLayout());
                PN_Preview.add(GeneradorMapa.preview(OperacionesDb.obtieneRecorrido(cod_registro_cab, conn)), BorderLayout.CENTER);
                PN_Preview.revalidate();
                PN_Preview.repaint();
            }

            if (evt.getClickCount() == 2) {
                String[] opciones = {"Ver Recorrido", "Eliminar Recorrido", "Cancelar"};
                int respuesta = JOptionPane.showOptionDialog(null, "¿Qué acción desea realizar?", "Seleccione una opción", 0, 3, null, opciones, null);
                if (respuesta == 0) {
                    GeneradorMapa.generar(OperacionesDb.obtieneRecorrido(cod_registro_cab, conn), cod_registro_cab);
                } else if (respuesta == 1) {
                    boolean eliminado = OperacionesDb.eliminaRecorrido(cod_registro_cab, conn);
                    if (eliminado) {
                        JOptionPane.showMessageDialog(this, "Recorrido eliminado correctamente.");
                    }
                }
                cargaRegistros();
            }
        }
    }//GEN-LAST:event_TB_Itinerario_UnoMouseClicked

    private void TB_Itinerario_UnoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TB_Itinerario_UnoKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_UP || evt.getKeyCode() == KeyEvent.VK_DOWN) {
            int cod_registro_cab = Integer.parseInt(String.valueOf(TB_Itinerario_Uno.getValueAt(TB_Itinerario_Uno.getSelectedRow(), 5)));
            if (cod_registro_cab > 0) {
                PN_Preview.removeAll();
                PN_Preview.setLayout(new BorderLayout());
                PN_Preview.add(GeneradorMapa.preview(OperacionesDb.obtieneRecorrido(cod_registro_cab, conn)), BorderLayout.CENTER);
                PN_Preview.revalidate();
                PN_Preview.repaint();                
            }        
        }
    }//GEN-LAST:event_TB_Itinerario_UnoKeyReleased

    private void TB_Itinerario_DosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TB_Itinerario_DosMouseClicked
        int cod_registro_cab = Integer.parseInt(String.valueOf(TB_Itinerario_Dos.getValueAt(TB_Itinerario_Dos.getSelectedRow(), 5)));
        
        if (cod_registro_cab > 0) {
            if (evt.getClickCount() == 1) {
                PN_Preview.removeAll();
                PN_Preview.setLayout(new BorderLayout());
                PN_Preview.add(GeneradorMapa.preview(OperacionesDb.obtieneRecorrido(cod_registro_cab, conn)), BorderLayout.CENTER);
                PN_Preview.revalidate();
                PN_Preview.repaint();
            }

            if (evt.getClickCount() == 2) {
                String[] opciones = {"Ver Recorrido", "Eliminar Recorrido", "Cancelar"};
                int respuesta = JOptionPane.showOptionDialog(null, "¿Qué acción desea realizar?", "Seleccione una opción", 0, 3, null, opciones, null);
                if (respuesta == 0) {
                    GeneradorMapa.generar(OperacionesDb.obtieneRecorrido(cod_registro_cab, conn), cod_registro_cab);
                } else if (respuesta == 1) {
                    boolean eliminado = OperacionesDb.eliminaRecorrido(cod_registro_cab, conn);
                    if (eliminado) {
                        JOptionPane.showMessageDialog(this, "Recorrido eliminado correctamente.");
                    }
                }
                cargaRegistros();
            }
        }
    }//GEN-LAST:event_TB_Itinerario_DosMouseClicked

    private void TB_Itinerario_DosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TB_Itinerario_DosKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_UP || evt.getKeyCode() == KeyEvent.VK_DOWN) {
            int cod_registro_cab = Integer.parseInt(String.valueOf(TB_Itinerario_Dos.getValueAt(TB_Itinerario_Dos.getSelectedRow(), 5)));
            if (cod_registro_cab > 0) {
                PN_Preview.removeAll();
                PN_Preview.setLayout(new BorderLayout());
                PN_Preview.add(GeneradorMapa.preview(OperacionesDb.obtieneRecorrido(cod_registro_cab, conn)), BorderLayout.CENTER);
                PN_Preview.revalidate();
                PN_Preview.repaint();                
            }        
        }
    }//GEN-LAST:event_TB_Itinerario_DosKeyReleased

    private void TB_Itinerario_TresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TB_Itinerario_TresMouseClicked
        int cod_registro_cab = Integer.parseInt(String.valueOf(TB_Itinerario_Tres.getValueAt(TB_Itinerario_Tres.getSelectedRow(), 5)));
        
        if (cod_registro_cab > 0) {
            if (evt.getClickCount() == 1) {
                PN_Preview.removeAll();
                PN_Preview.setLayout(new BorderLayout());
                PN_Preview.add(GeneradorMapa.preview(OperacionesDb.obtieneRecorrido(cod_registro_cab, conn)), BorderLayout.CENTER);
                PN_Preview.revalidate();
                PN_Preview.repaint();
            }

            if (evt.getClickCount() == 2) {
                String[] opciones = {"Ver Recorrido", "Eliminar Recorrido", "Cancelar"};
                int respuesta = JOptionPane.showOptionDialog(null, "¿Qué acción desea realizar?", "Seleccione una opción", 0, 3, null, opciones, null);
                if (respuesta == 0) {
                    GeneradorMapa.generar(OperacionesDb.obtieneRecorrido(cod_registro_cab, conn), cod_registro_cab);
                } else if (respuesta == 1) {
                    boolean eliminado = OperacionesDb.eliminaRecorrido(cod_registro_cab, conn);
                    if (eliminado) {
                        JOptionPane.showMessageDialog(this, "Recorrido eliminado correctamente.");
                    }
                }
                cargaRegistros();
            }
        }
    }//GEN-LAST:event_TB_Itinerario_TresMouseClicked

    private void TB_Itinerario_TresKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TB_Itinerario_TresKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_UP || evt.getKeyCode() == KeyEvent.VK_DOWN) {
            int cod_registro_cab = Integer.parseInt(String.valueOf(TB_Itinerario_Tres.getValueAt(TB_Itinerario_Tres.getSelectedRow(), 5)));
            if (cod_registro_cab > 0) {
                PN_Preview.removeAll();
                PN_Preview.setLayout(new BorderLayout());
                PN_Preview.add(GeneradorMapa.preview(OperacionesDb.obtieneRecorrido(cod_registro_cab, conn)), BorderLayout.CENTER);
                PN_Preview.revalidate();
                PN_Preview.repaint();                
            }        
        }
    }//GEN-LAST:event_TB_Itinerario_TresKeyReleased

    private void TB_Itinerario_CentroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TB_Itinerario_CentroMouseClicked
        int cod_registro_cab = Integer.parseInt(String.valueOf(TB_Itinerario_Centro.getValueAt(TB_Itinerario_Centro.getSelectedRow(), 5)));
        
        if (cod_registro_cab > 0) {
            if (evt.getClickCount() == 1) {
                PN_Preview.removeAll();
                PN_Preview.setLayout(new BorderLayout());
                PN_Preview.add(GeneradorMapa.preview(OperacionesDb.obtieneRecorrido(cod_registro_cab, conn)), BorderLayout.CENTER);
                PN_Preview.revalidate();
                PN_Preview.repaint();
            }

            if (evt.getClickCount() == 2) {
                String[] opciones = {"Ver Recorrido", "Eliminar Recorrido", "Cancelar"};
                int respuesta = JOptionPane.showOptionDialog(null, "¿Qué acción desea realizar?", "Seleccione una opción", 0, 3, null, opciones, null);
                if (respuesta == 0) {
                    GeneradorMapa.generar(OperacionesDb.obtieneRecorrido(cod_registro_cab, conn), cod_registro_cab);
                } else if (respuesta == 1) {
                    boolean eliminado = OperacionesDb.eliminaRecorrido(cod_registro_cab, conn);
                    if (eliminado) {
                        JOptionPane.showMessageDialog(this, "Recorrido eliminado correctamente.");
                    }
                }
                cargaRegistros();
            }
        }
    }//GEN-LAST:event_TB_Itinerario_CentroMouseClicked

    private void TB_Itinerario_CentroKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TB_Itinerario_CentroKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_UP || evt.getKeyCode() == KeyEvent.VK_DOWN) {
            int cod_registro_cab = Integer.parseInt(String.valueOf(TB_Itinerario_Centro.getValueAt(TB_Itinerario_Centro.getSelectedRow(), 5)));
            if (cod_registro_cab > 0) {
                PN_Preview.removeAll();
                PN_Preview.setLayout(new BorderLayout());
                PN_Preview.add(GeneradorMapa.preview(OperacionesDb.obtieneRecorrido(cod_registro_cab, conn)), BorderLayout.CENTER);
                PN_Preview.revalidate();
                PN_Preview.repaint();                
            }        
        }
    }//GEN-LAST:event_TB_Itinerario_CentroKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup BG_Busquedas;
    private javax.swing.ButtonGroup BG_Intinerarios;
    private javax.swing.JButton BT_Buscar;
    private javax.swing.JButton BT_Detener;
    private javax.swing.JComboBox<String> CB_Origen;
    private javax.swing.JPanel PN_Preview;
    private javax.swing.JRadioButton RB_Automatico;
    private javax.swing.JRadioButton RB_Centro;
    private javax.swing.JRadioButton RB_Intenerario_Dos;
    private javax.swing.JRadioButton RB_Intenerario_Tres;
    private javax.swing.JRadioButton RB_Intenerario_Uno;
    private javax.swing.JRadioButton RB_Manual;
    private javax.swing.JSpinner SP_Hora;
    private javax.swing.JSpinner SP_Minuto;
    private javax.swing.JTextArea TA_Recorrido;
    private javax.swing.JTable TB_Itinerario_Centro;
    private javax.swing.JTable TB_Itinerario_Dos;
    private javax.swing.JTable TB_Itinerario_Tres;
    private javax.swing.JTable TB_Itinerario_Uno;
    private javax.swing.JTextField TF_CostoDeseado;
    private javax.swing.JTextField TF_NodosRecorridos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
