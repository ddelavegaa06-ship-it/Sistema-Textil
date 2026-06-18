import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.text.Text;

public class App extends Application {

    private Stage stage;
    private String rolActual;
    private int contadorIdVenta = 1;

    private static final String FONDO          = "#F5F7FA";
    private static final String PANEL          = "#FFFFFF";
    private static final String PRINCIPAL      = "#2563EB";
    private static final String SECUNDARIO     = "#1E293B";
    private static final String PRINCIPAL_ALT  = "#4F46E5";
    private static final String SECUNDARIO_ALT = "#312E81";
    private static final String NARANJA        = "#EA580C";
    private static final String CAFE           = "#7C2D12";
    private static final String AZUL_EDITAR    = "#0369A1";
    private static final String TEXTO          = "#111827";
    private static final String TEXTO_SUAVE    = "#6B7280";
    private static final String EXITO          = "#16A34A";
    private static final String ERROR          = "#DC2626";
    private static final String ADVERTENCIA    = "#EAB308";
    private static final int ALERTA_ROJO     = 5;   
    private static final int ALERTA_AMARILLO = 10;  


    private static final int DIAS_DEVOLUCION = 30;

    private ObservableList<MateriaPrima> listaMateriaPrima = FXCollections.observableArrayList(
        new MateriaPrima(1,  "BOT-001", 500,  "",        "4 hoyos nacarado, fácil costura",          "Botón para camisa",    "Blanco",       null,  null,  null,              null,             14,   null,  null,  "Plástico", "Botón"),
        new MateriaPrima(2,  "BOT-002", 50,   "",        "Largo canal central, acabado brillante",    "Botón para pantalón",  "Metálico oro", null,  null,  null,              null,             22,   null,  null,  "Latón",    "Botón"),
        new MateriaPrima(3,  "CRP-001", 1000, "",        "Cierre reforzado, deslizador metálico",     "Cierre pantalón",      "Negro",        null,  18.0,  null,              null,             null, null,  null,  "Latón",    "Cierre"),
        new MateriaPrima(4,  "CRP-002", 5,    "",        "Cierre delgado de bolsa lateral",           "Cierre bolsa",         "Rojo",         null,  15.0,  null,              null,             null, null,  null,  "Nylon",    "Cierre"),
        new MateriaPrima(5,  "TEL-001", 150,  "",        "Gabardina tejido plano, resistente",        "Gabardina",            "Azul marino",  null,  1.5,   "65% POL 35% ALG", "Tejido plano",   null, null,  null,  null,       "Tela plana"),
        new MateriaPrima(6,  "TEL-002", 200,  "",        "Mezclilla para jeans, peso medio",          "Mezclilla",            "Índigo",       null,  1.65,  "100% ALG",        "Tejido plano",   null, null,  null,  null,       "Tela plana"),
        new MateriaPrima(7,  "TELP-001", 80,  "",        "Tejido punto para playera, suave",          "Jersey",               "Rojo",         null,  1.5,   "100% ALG",        "Tejido de punto",null, null,  null,  null,       "Tela punto"),
        new MateriaPrima(8,  "HIL-001", 25,   "",        "Hilo para costura industrial resistente",   "Filamento",            "Marino 592King",null, null,  "100% Poliéster",  null,             null, "40/2",null,  null,       "Hilo")
    );

    private ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList(
        new Usuario("Administrador Principal", "admin", "1234", "administrador"),
        new Usuario("Encargado de Tienda",     "encargado", "5678", "encargado")
    );

    private ObservableList<Prenda> listaPrendas = FXCollections.observableArrayList(
        new Prenda("Falda escolar",      "1",  "Ch", "Uniforme", 25, 180.0, 220.0, "T1", "Falda escolar talla chica"),
        new Prenda("Falda escolar",      "2",  "M",  "Uniforme", 30, 180.0, 220.0, "T1", "Falda escolar talla mediana"),
        new Prenda("Falda escolar",      "3",  "G",  "Uniforme", 20, 180.0, 220.0, "T1", "Falda escolar talla grande"),
        new Prenda("Pantalón de vestir", "4",  "Ch", "Uniforme", 15, 250.0, 320.0, "T2", "Pantalón de vestir talla chica"),
        new Prenda("Pantalón de vestir", "5",  "M",  "Uniforme", 22, 250.0, 320.0, "T2", "Pantalón de vestir talla mediana"),
        new Prenda("Pantalón de vestir", "6",  "G",  "Uniforme", 18, 250.0, 320.0, "T2", "Pantalón de vestir talla grande"),
        new Prenda("Camisa blanca",      "7",  "Ch", "Uniforme", 40, 150.0, 190.0, "T1", "Camisa blanca talla chica"),
        new Prenda("Camisa blanca",      "8",  "M",  "Uniforme", 35, 150.0, 190.0, "T1", "Camisa blanca talla mediana"),
        new Prenda("Camisa blanca",      "9",  "G",  "Uniforme", 30, 150.0, 190.0, "T1", "Camisa blanca talla grande"),
        new Prenda("Playera polo",       "10", "Ch", "Casual",   50, 120.0, 160.0, "T3", "Playera polo talla chica"),
        new Prenda("Playera polo",       "11", "M",  "Casual",   60, 120.0, 160.0, "T3", "Playera polo talla mediana"),
        new Prenda("Playera polo",       "12", "G",  "Casual",   45, 120.0, 160.0, "T3", "Playera polo talla grande"),
        new Prenda("Playera polo",       "13", "XL", "Casual",   20, 120.0, 160.0, "T3", "Playera polo talla extra grande")
    
    );

    private ObservableList<Conjunto> listaConjuntos = FXCollections.observableArrayList(
        new Conjunto("1", "Uniforme escolar Ch", "Uniforme escolar completo talla chica",
            new ArrayList<>(List.of("1", "7")), 3),
        new Conjunto("2", "Uniforme escolar M", "Uniforme escolar completo talla mediana",
            new ArrayList<>(List.of("2", "8")), 3),
        new Conjunto("3", "Uniforme escolar G", "Uniforme escolar completo talla grande",
            new ArrayList<>(List.of("3", "9")), 3)
    );

    private ObservableList<PrendaVendida>   listaPrendasVendidas   = FXCollections.observableArrayList();
    private ObservableList<ConjuntoVendido> listaConjuntosVendidos = FXCollections.observableArrayList();

    // ── LÓGICA CONJUNTOS ─────────────────────────────────────────────
    private void verificarConjuntos() {
        listaConjuntos.removeIf(c -> {
            long tiposDisponibles = c.getIdPrendas().stream()
                .map(idP -> listaPrendas.stream().filter(p -> p.getId().equals(idP)).findFirst().orElse(null))
                .filter(p -> p != null && p.getExistencia() > 0)
                .map(Prenda::getNombre).distinct().count();
            return tiposDisponibles <= 1 || calcularExistenciaConjunto(c) <= 0;
        });
    }

    private int calcularExistenciaConjunto(Conjunto c) {
        return c.getIdPrendas().stream()
            .mapToInt(idP -> listaPrendas.stream().filter(p -> p.getId().equals(idP))
                .mapToInt(Prenda::getExistencia).findFirst().orElse(0))
            .min().orElse(0);
    }

    private double calcularPrecioConjunto(Conjunto c) {
        return c.getIdPrendas().stream()
            .mapToDouble(idP -> listaPrendas.stream().filter(p -> p.getId().equals(idP))
                .mapToDouble(Prenda::getPrecioMenudeo).findFirst().orElse(0.0))
            .sum();
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setTitle("Sistema Textil");
        mostrarLogin();
        stage.show();
    }

    // ── LOGIN ────────────────────────────────────────────────────────
    private void mostrarLogin() {
        VBox panelIzquierdo = new VBox();
        panelIzquierdo.setPrefWidth(260);
        panelIzquierdo.setStyle("-fx-background-color: " + SECUNDARIO + ";");
        panelIzquierdo.setAlignment(Pos.CENTER);

        Label marcaNombre = new Label("Sistema\nTextil");
        marcaNombre.setFont(Font.font("System", FontWeight.BOLD, 32));
        marcaNombre.setTextFill(Color.WHITE);
        marcaNombre.setStyle("-fx-padding: 0 0 12 0;");

        Label marcaDesc = new Label("Gestión de inventario\ny punto de venta");
        marcaDesc.setFont(Font.font("System", 13));
        marcaDesc.setTextFill(Color.web("#94A3B8"));
        marcaDesc.setStyle("-fx-text-alignment: center;");

        Region lineaDeco = new Region();
        lineaDeco.setPrefHeight(3); lineaDeco.setPrefWidth(60);
        lineaDeco.setStyle("-fx-background-color: " + PRINCIPAL + "; -fx-background-radius: 2;");

        VBox contenidoIzq = new VBox(16, lineaDeco, marcaNombre, marcaDesc);
        contenidoIzq.setAlignment(Pos.CENTER);
        contenidoIzq.setStyle("-fx-padding: 40;");
        panelIzquierdo.getChildren().add(contenidoIzq);

        Label titulo    = new Label("Bienvenido");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 24));
        titulo.setTextFill(Color.web(SECUNDARIO));

        Label subtitulo = new Label("Inicia sesión para continuar");
        subtitulo.setFont(Font.font("System", 13));
        subtitulo.setTextFill(Color.web(TEXTO_SUAVE));

        Region lineaTitulo = new Region();
        lineaTitulo.setPrefHeight(2); lineaTitulo.setPrefWidth(40);
        lineaTitulo.setStyle("-fx-background-color: " + PRINCIPAL + "; -fx-background-radius: 2;");
        HBox lineaBox = new HBox(lineaTitulo);

        Label labelUsuario = new Label("Usuario");
        labelUsuario.setFont(Font.font("System", FontWeight.BOLD, 12));
        labelUsuario.setTextFill(Color.web(TEXTO));

        TextField campoUsuario = crearTextField("Ingresa tu usuario");
        campoUsuario.setMaxWidth(Double.MAX_VALUE);

        Label labelPass = new Label("Contraseña");
        labelPass.setFont(Font.font("System", FontWeight.BOLD, 12));
        labelPass.setTextFill(Color.web(TEXTO));

        PasswordField campoContrasena = new PasswordField();
        campoContrasena.setPromptText("Ingresa tu contraseña");
        campoContrasena.setMaxWidth(Double.MAX_VALUE);
        campoContrasena.setStyle(estiloInput());

        Label mensajeError = new Label("");
        mensajeError.setTextFill(Color.web(ERROR));
        mensajeError.setFont(Font.font("System", 12));

        Button btnEntrar = new Button("Iniciar Sesión");
        btnEntrar.setMaxWidth(Double.MAX_VALUE);
        btnEntrar.setStyle(estiloBtnPrincipal());
        btnEntrar.setDefaultButton(true);
        btnEntrar.setOnAction(e -> {
            String rol = verificarLogin(campoUsuario.getText(), campoContrasena.getText());
            if (rol != null) {
                rolActual = rol;
                if (rol.equals("administrador")) mostrarMenuAdministrador();
                else mostrarMenuEncargado();
            } else {
                mensajeError.setText("Usuario o contraseña incorrectos");
                campoContrasena.clear();
            }
        });

        VBox form = new VBox(10, titulo, subtitulo, lineaBox,
            labelUsuario, campoUsuario, labelPass, campoContrasena, mensajeError, btnEntrar);
        form.setAlignment(Pos.CENTER_LEFT);
        form.setStyle("-fx-padding: 50 45 50 45; -fx-background-color: " + PANEL + ";");
        VBox.setVgrow(form, Priority.ALWAYS);

        HBox root = new HBox(panelIzquierdo, form);
        HBox.setHgrow(form, Priority.ALWAYS);
        root.setStyle("-fx-background-color: " + PANEL + ";");
        stage.setScene(new Scene(root, 700, 460));
    }

    // ── MENÚS ────────────────────────────────────────────────────────
    private void mostrarMenuAdministrador() { mostrarMenu("Administrador", true); }
    private void mostrarMenuEncargado()     { mostrarMenu("Encargado", false); }

    private void mostrarMenu(String rol, boolean esAdmin) {
    String colorSidebar = esAdmin ? SECUNDARIO     : SECUNDARIO_ALT;
    String colorLogo    = esAdmin ? PRINCIPAL      : PRINCIPAL_ALT;
    String colorHover   = esAdmin ? PRINCIPAL      : PRINCIPAL_ALT;

    VBox sidebar = new VBox(4);
    sidebar.setPrefWidth(230);
    sidebar.setStyle("-fx-background-color: " + colorSidebar + "; -fx-padding: 0;");

    Label logoLabel = new Label("Sistema Textil");
    logoLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
    logoLabel.setTextFill(Color.WHITE);
    logoLabel.setStyle("-fx-background-color: " + colorLogo + "; -fx-padding: 20 16 20 16; -fx-max-width: infinity;");
    logoLabel.setMaxWidth(Double.MAX_VALUE);

    // Puntito de alerta con color dinámico
    String colorAlerta = esAdmin ? calcularColorAlerta() : calcularColorAlertaEncargado();
    Label rolLabel = new Label("⬤ " + rol.toUpperCase());
    rolLabel.setFont(Font.font("System", 11));
    rolLabel.setTextFill(Color.web(colorAlerta));
    rolLabel.setStyle("-fx-padding: 12 16 8 16;");

    sidebar.getChildren().addAll(logoLabel, rolLabel);

    StackPane contenido = crearContenidoVacio();
    VBox.setVgrow(contenido, Priority.ALWAYS);

    if (esAdmin) {
        sidebar.getChildren().add(crearSeccionMenu("INVENTARIO"));
        Button btnMP  = crearBotonMenuColor("Materia Prima", colorHover);
        Button btnPr  = crearBotonMenuColor("Prendas Fabricadas", colorHover);
        Button btnCj  = crearBotonMenuColor("Conjuntos", colorHover);
        sidebar.getChildren().addAll(btnMP, btnPr, btnCj);
        btnMP.setOnAction(e -> mostrarModuloMateriaPrima(contenido, true));
        btnPr.setOnAction(e -> mostrarModuloPrendas(contenido, true));
        btnCj.setOnAction(e -> mostrarModuloConjuntos(contenido, true));

        sidebar.getChildren().add(crearSeccionMenu("VENTAS"));
        Button btnPV  = crearBotonMenuColor("Punto de Venta", colorHover);
        Button btnPVd = crearBotonMenuColor("Prendas Vendidas", colorHover);
        Button btnCVd = crearBotonMenuColor("Conjuntos Vendidos", colorHover);
        Button btnDev    = crearBotonMenuColor("Devoluciones", colorHover);
        Button btnRegDev = crearBotonMenuColor("Registro de Devoluciones", colorHover);
        sidebar.getChildren().addAll(btnPV, btnPVd, btnCVd, btnDev, btnRegDev);
        btnPV.setOnAction(e     -> mostrarPuntoDeVenta(contenido));
        btnPVd.setOnAction(e    -> mostrarModuloPrendasVendidas(contenido, true));
        btnCVd.setOnAction(e    -> mostrarModuloConjuntosVendidos(contenido, true));
        btnDev.setOnAction(e    -> mostrarDevoluciones(contenido));
        btnRegDev.setOnAction(e -> mostrarRegistroDevoluciones(contenido, true));

        sidebar.getChildren().add(crearSeccionMenu("ADMINISTRACIÓN"));
        Button btnUs  = crearBotonMenuColor("Usuarios", colorHover);
        Button btnAl  = crearBotonMenuColor("Alertas de Stock", colorHover);
        sidebar.getChildren().addAll(btnUs, btnAl);
        btnUs.setOnAction(e -> mostrarModuloUsuarios(contenido));
        btnAl.setOnAction(e -> mostrarAlertasStock(contenido, true));

    } else {
        sidebar.getChildren().add(crearSeccionMenu("INVENTARIO"));
        Button btnPr = crearBotonMenuColor("Prendas Fabricadas", colorHover);
        Button btnCj = crearBotonMenuColor("Conjuntos", colorHover);
        sidebar.getChildren().addAll(btnPr, btnCj);
        btnPr.setOnAction(e -> mostrarModuloPrendas(contenido, false));
        btnCj.setOnAction(e -> mostrarModuloConjuntos(contenido, false));

        sidebar.getChildren().add(crearSeccionMenu("VENTAS"));
        Button btnPV  = crearBotonMenuColor("Punto de Venta", colorHover);
        Button btnPVd = crearBotonMenuColor("Prendas Vendidas", colorHover);
        Button btnCVd = crearBotonMenuColor("Conjuntos Vendidos", colorHover);
        Button btnDev    = crearBotonMenuColor("Devoluciones", colorHover);
        Button btnRegDev = crearBotonMenuColor("Registro de Devoluciones", colorHover);
        sidebar.getChildren().addAll(btnPV, btnPVd, btnCVd, btnDev, btnRegDev);
        btnPV.setOnAction(e     -> mostrarPuntoDeVenta(contenido));
        btnPVd.setOnAction(e    -> mostrarModuloPrendasVendidas(contenido, false));
        btnCVd.setOnAction(e    -> mostrarModuloConjuntosVendidos(contenido, false));
        btnDev.setOnAction(e    -> mostrarDevoluciones(contenido));
        btnRegDev.setOnAction(e -> mostrarRegistroDevoluciones(contenido, false));

        // Encargado también tiene alertas pero solo de prendas y conjuntos
        sidebar.getChildren().add(crearSeccionMenu("AVISOS"));
        Button btnAl = crearBotonMenuColor("Alertas de Stock", colorHover);
        sidebar.getChildren().add(btnAl);
        btnAl.setOnAction(e -> mostrarAlertasStock(contenido, false));
    }

    Region spacer = new Region();
    VBox.setVgrow(spacer, Priority.ALWAYS);
    Button btnCerrar = new Button("Cerrar Sesión");
    btnCerrar.setMaxWidth(Double.MAX_VALUE);
    btnCerrar.setStyle(
        "-fx-background-color: transparent; -fx-text-fill: #F87171; -fx-font-size: 13px;" +
        "-fx-padding: 12 16; -fx-alignment: CENTER_LEFT; -fx-cursor: hand;" +
        "-fx-border-color: #374151; -fx-border-width: 1 0 0 0;");
    btnCerrar.setOnAction(e -> mostrarLogin());
    sidebar.getChildren().addAll(spacer, btnCerrar);

    ScrollPane sidebarScroll = new ScrollPane(sidebar);
    sidebarScroll.setFitToWidth(true);
    sidebarScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    sidebarScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    sidebarScroll.setPrefWidth(230);
    sidebarScroll.setMinWidth(230);
    sidebarScroll.setMaxWidth(230);
    sidebarScroll.setStyle("-fx-background-color: " + colorSidebar + "; -fx-border-color: transparent;");

    HBox cuerpo = new HBox(sidebarScroll, contenido);
    HBox.setHgrow(contenido, Priority.ALWAYS);
    VBox.setVgrow(cuerpo, Priority.ALWAYS);

    BorderPane root = new BorderPane();
    root.setCenter(cuerpo);
    root.setStyle("-fx-background-color: " + FONDO + ";");
    stage.setScene(new Scene(root, 960, 620));
}

    // ── MÓDULO ALERTAS DE STOCK ───────────────────────────────────────────────


private void mostrarAlertasStock(StackPane contenido, boolean esAdmin) {
    contenido.getChildren().clear();

    Label titulo = new Label("Alertas de Stock");
    titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
    titulo.setTextFill(Color.web(SECUNDARIO));

    Label leyendaRojo     = new Label("⬤ Crítico — en o por debajo del mínimo");
    Label leyendaAmarillo = new Label("⬤ Bajo — hasta el doble del mínimo");
    Label leyendaVerde    = new Label("⬤ Normal — por encima del umbral bajo");
    leyendaRojo.setTextFill(Color.web(ERROR));
    leyendaAmarillo.setTextFill(Color.web(ADVERTENCIA));
    leyendaVerde.setTextFill(Color.web(EXITO));
    for (Label l : new Label[]{leyendaRojo, leyendaAmarillo, leyendaVerde})
        l.setFont(Font.font("System", 12));

    HBox leyenda = new HBox(20, leyendaRojo, leyendaAmarillo, leyendaVerde);
    leyenda.setStyle(
        "-fx-background-color: " + PANEL + "; -fx-padding: 10 16; -fx-background-radius: 6;" +
        "-fx-border-color: #E5E7EB; -fx-border-radius: 6;");

    VBox vista = new VBox(16, titulo, leyenda);
    vista.setStyle("-fx-padding: 30;");
    VBox.setVgrow(vista, Priority.ALWAYS);

    // ── TABLA PRENDAS ──────────────────────────────────────────────
    Label tituloPrendas = new Label("Prendas Fabricadas");
    tituloPrendas.setFont(Font.font("System", FontWeight.BOLD, 15));
    tituloPrendas.setTextFill(Color.web(SECUNDARIO));

    TableView<Prenda> tablaPrendas = new TableView<>();
    tablaPrendas.setStyle("-fx-background-color: " + PANEL + "; -fx-border-color: #E5E7EB;");
    tablaPrendas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    tablaPrendas.setMinHeight(120);
    tablaPrendas.setMaxHeight(200);
    tablaPrendas.setPlaceholder(new Label("Sin alertas en prendas"));

    TableColumn<Prenda, String> colPNivel    = new TableColumn<>("Nivel");
    TableColumn<Prenda, String> colPNombre   = new TableColumn<>("Nombre");
    TableColumn<Prenda, String> colPTalla    = new TableColumn<>("Talla");
    TableColumn<Prenda, String> colPExist    = new TableColumn<>("Existencia");
    TableColumn<Prenda, String> colPMinimo   = new TableColumn<>("Mínimo");
    TableColumn<Prenda, String> colPTipo     = new TableColumn<>("Tipo");

    colPNivel.setCellValueFactory(d -> {
        int ex = d.getValue().getExistencia();
        int min = d.getValue().getMinimoExistencia();
        return new SimpleStringProperty(ex <= min ? "🔴 Crítico" : "🟡 Bajo");
    });
    colPNombre.setCellValueFactory(d  -> new SimpleStringProperty(d.getValue().getNombre()));
    colPTalla.setCellValueFactory(d   -> new SimpleStringProperty(d.getValue().getTalla()));
    colPExist.setCellValueFactory(d   -> new SimpleStringProperty(String.valueOf(d.getValue().getExistencia())));
    colPMinimo.setCellValueFactory(d  -> new SimpleStringProperty(String.valueOf(d.getValue().getMinimoExistencia())));
    colPTipo.setCellValueFactory(d    -> new SimpleStringProperty(d.getValue().getTipoPrenda()));

    tablaPrendas.getColumns().addAll(colPNivel, colPNombre, colPTalla, colPExist, colPMinimo, colPTipo);

    ObservableList<Prenda> prendasAlerta = FXCollections.observableArrayList(
        listaPrendas.stream()
            .filter(p -> p.getExistencia() <= p.getMinimoExistencia() * 2)
            .sorted((a, b) -> Integer.compare(a.getExistencia(), b.getExistencia()))
            .toList()
    );
    tablaPrendas.setItems(prendasAlerta);

    vista.getChildren().addAll(tituloPrendas, tablaPrendas);

    // ── TABLA CONJUNTOS ────────────────────────────────────────────
    Label tituloConjuntos = new Label("Conjuntos");
    tituloConjuntos.setFont(Font.font("System", FontWeight.BOLD, 15));
    tituloConjuntos.setTextFill(Color.web(SECUNDARIO));

    TableView<Conjunto> tablaConjuntos = new TableView<>();
    tablaConjuntos.setStyle("-fx-background-color: " + PANEL + "; -fx-border-color: #E5E7EB;");
    tablaConjuntos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    tablaConjuntos.setMinHeight(120);
    tablaConjuntos.setMaxHeight(200);
    tablaConjuntos.setPlaceholder(new Label("Sin alertas en conjuntos"));

    TableColumn<Conjunto, String> colCNivel  = new TableColumn<>("Nivel");
    TableColumn<Conjunto, String> colCNombre = new TableColumn<>("Nombre");
    TableColumn<Conjunto, String> colCExist  = new TableColumn<>("Existencia");
    TableColumn<Conjunto, String> colCMinimo = new TableColumn<>("Mínimo");
    TableColumn<Conjunto, String> colCPiezas = new TableColumn<>("Piezas");

    colCNivel.setCellValueFactory(d -> {
        int ex = calcularExistenciaConjunto(d.getValue());
        int min = d.getValue().getMinimoExistencia();
        return new SimpleStringProperty(ex <= min ? "🔴 Crítico" : "🟡 Bajo");
    });
    colCNombre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombre()));
    colCExist.setCellValueFactory(d  -> new SimpleStringProperty(String.valueOf(calcularExistenciaConjunto(d.getValue()))));
    colCMinimo.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getMinimoExistencia())));
    colCPiezas.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getIdPrendas().size())));

    tablaConjuntos.getColumns().addAll(colCNivel, colCNombre, colCExist, colCMinimo, colCPiezas);

    ObservableList<Conjunto> conjuntosAlerta = FXCollections.observableArrayList(
        listaConjuntos.stream()
            .filter(c -> calcularExistenciaConjunto(c) <= c.getMinimoExistencia() * 2)
            .sorted((a, b) -> Integer.compare(calcularExistenciaConjunto(a), calcularExistenciaConjunto(b)))
            .toList()
    );
    tablaConjuntos.setItems(conjuntosAlerta);

    vista.getChildren().addAll(tituloConjuntos, tablaConjuntos);

    // ── TABLA MATERIA PRIMA (solo admin) ──────────────────────────
    if (esAdmin) {
        Label tituloMP = new Label("Materia Prima");
        tituloMP.setFont(Font.font("System", FontWeight.BOLD, 15));
        tituloMP.setTextFill(Color.web(SECUNDARIO));

        TableView<MateriaPrima> tablaMP = new TableView<>();
        tablaMP.setStyle("-fx-background-color: " + PANEL + "; -fx-border-color: #E5E7EB;");
        tablaMP.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tablaMP.setMinHeight(120);
        tablaMP.setMaxHeight(200);
        tablaMP.setPlaceholder(new Label("Sin alertas en materia prima"));

        TableColumn<MateriaPrima, String> colMNivel    = new TableColumn<>("Nivel");
        TableColumn<MateriaPrima, String> colMNombre   = new TableColumn<>("Nombre");
        TableColumn<MateriaPrima, String> colMPartida  = new TableColumn<>("No. Partida");
        TableColumn<MateriaPrima, String> colMExist    = new TableColumn<>("Existencia");
        TableColumn<MateriaPrima, String> colMMinimo   = new TableColumn<>("Mínimo");
        TableColumn<MateriaPrima, String> colMTipo     = new TableColumn<>("Tipo Insumo");

        colMNivel.setCellValueFactory(d -> {
            int ex = d.getValue().getExistencia();
            int min = d.getValue().getMinimoExistencia();
            return new SimpleStringProperty(ex <= min ? "🔴 Crítico" : "🟡 Bajo");
        });
        colMNombre.setCellValueFactory(d  -> new SimpleStringProperty(d.getValue().getNombre()));
        colMPartida.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNumeroPartida()));
        colMExist.setCellValueFactory(d   -> new SimpleStringProperty(String.valueOf(d.getValue().getExistencia())));
        colMMinimo.setCellValueFactory(d  -> new SimpleStringProperty(String.valueOf(d.getValue().getMinimoExistencia())));
        colMTipo.setCellValueFactory(d    -> new SimpleStringProperty(d.getValue().getTipoInsumo()));

        tablaMP.getColumns().addAll(colMNivel, colMNombre, colMPartida, colMExist, colMMinimo, colMTipo);

        ObservableList<MateriaPrima> mpAlerta = FXCollections.observableArrayList(
            listaMateriaPrima.stream()
                .filter(mp -> mp.getExistencia() <= mp.getMinimoExistencia() * 2)
                .sorted((a, b) -> Integer.compare(a.getExistencia(), b.getExistencia()))
                .toList()
        );
        tablaMP.setItems(mpAlerta);

        ScrollPane scrollMP = new ScrollPane(tablaMP);
        scrollMP.setFitToHeight(true);
        scrollMP.setFitToWidth(false);
        scrollMP.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollMP.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollMP.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        vista.getChildren().addAll(tituloMP, scrollMP);
    }

    // Resumen
    long totalAlertas = prendasAlerta.size() + conjuntosAlerta.size()
        + (esAdmin ? listaMateriaPrima.stream().filter(mp -> mp.getExistencia() <= mp.getMinimoExistencia() * 2).count() : 0);
    long criticas = prendasAlerta.stream().filter(p -> p.getExistencia() <= p.getMinimoExistencia()).count()
        + conjuntosAlerta.stream().filter(c -> calcularExistenciaConjunto(c) <= c.getMinimoExistencia()).count()
        + (esAdmin ? listaMateriaPrima.stream().filter(mp -> mp.getExistencia() <= mp.getMinimoExistencia()).count() : 0);

    Label resumen = new Label(
        totalAlertas == 0
        ? "✔ Sin alertas de stock activas"
        : totalAlertas + " alerta(s) activa(s)  —  " + criticas + " crítica(s)"
    );
    resumen.setFont(Font.font("System", FontWeight.BOLD, 13));
    resumen.setTextFill(Color.web(totalAlertas == 0 ? EXITO : (criticas > 0 ? ERROR : ADVERTENCIA)));
    resumen.setStyle(
        "-fx-background-color: " + PANEL + "; -fx-padding: 10 16; -fx-background-radius: 6;" +
        "-fx-border-color: #E5E7EB; -fx-border-radius: 6;");

    vista.getChildren().add(2, resumen);

    ScrollPane scroll = new ScrollPane(vista);
    scroll.setFitToWidth(true);
    scroll.setFitToHeight(false);
    scroll.setStyle("-fx-background-color: " + FONDO + "; -fx-background: " + FONDO + ";");
    VBox.setVgrow(scroll, Priority.ALWAYS);

    StackPane wrapper = new StackPane(scroll);
    wrapper.setStyle("-fx-background-color: " + FONDO + ";");
    contenido.getChildren().add(wrapper);
}



    // ── MÓDULO PRENDAS ───────────────────────────────────────────────
    private void mostrarModuloPrendas(StackPane contenido, boolean esAdmin) {
        contenido.getChildren().clear();

        Label titulo = new Label("Prendas Fabricadas");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        TableView<Prenda> tabla = new TableView<>();
        tabla.setStyle("-fx-background-color: " + PANEL + "; -fx-border-color: #E5E7EB;");
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabla.setPlaceholder(new Label("No hay prendas registradas"));
        tabla.setMinHeight(200);

        TableColumn<Prenda, String> colId     = new TableColumn<>("ID");
        TableColumn<Prenda, String> colNombre = new TableColumn<>("Nombre");
        TableColumn<Prenda, String> colTalla  = new TableColumn<>("Talla");
        TableColumn<Prenda, String> colExist  = new TableColumn<>("Existencia");
        TableColumn<Prenda, String> colMayor  = new TableColumn<>("P. Mayoreo");
        TableColumn<Prenda, String> colMenud  = new TableColumn<>("P. Menudeo");
       TableColumn<Prenda, String> colTienda  = new TableColumn<>("ID Tienda");
        TableColumn<Prenda, String> colMinimo  = new TableColumn<>("Mínimo");

        colId.setCellValueFactory(d     -> new SimpleStringProperty(d.getValue().getId()));
        colNombre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombre()));
        colTalla.setCellValueFactory(d  -> new SimpleStringProperty(d.getValue().getTalla()));
        colExist.setCellValueFactory(d  -> new SimpleStringProperty(String.valueOf(d.getValue().getExistencia())));
        colMayor.setCellValueFactory(d  -> new SimpleStringProperty("$" + d.getValue().getPrecioMayoreo()));
        colMenud.setCellValueFactory(d  -> new SimpleStringProperty("$" + d.getValue().getPrecioMenudeo()));
        colTienda.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getIdTienda()));
        colMinimo.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getMinimoExistencia())));

        tabla.getColumns().addAll(colId, colNombre, colTalla, colExist, colMinimo, colMayor, colMenud, colTienda);
        tabla.setItems(listaPrendas);
        VBox.setVgrow(tabla, Priority.ALWAYS);

        Button btnDetalle = new Button("🔍 Ver Detalle");
        btnDetalle.setStyle("-fx-background-color: " + PRINCIPAL + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
        btnDetalle.setOnAction(e -> mostrarDetallePrenda(contenido, esAdmin));

        VBox vista = new VBox(16, titulo, tabla);
        vista.setStyle("-fx-padding: 30;");
        VBox.setVgrow(tabla, Priority.ALWAYS);
        VBox.setVgrow(vista, Priority.ALWAYS);

        if (esAdmin) {
            Button btnAnadir = new Button("+ Añadir Prenda");
            Button btnExist  = new Button("+ Añadir a Existente");
            Button btnEditar = new Button("✎ Editar Prenda");

            btnAnadir.setStyle("-fx-background-color: " + NARANJA + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
            btnExist.setStyle("-fx-background-color: " + CAFE + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
            btnEditar.setStyle("-fx-background-color: " + AZUL_EDITAR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");

            btnAnadir.setOnAction(e -> mostrarFormularioNuevaPrenda(contenido));
            btnExist.setOnAction(e  -> mostrarFormularioAnadirExistente(contenido));
            btnEditar.setOnAction(e -> mostrarFormularioEditarPrenda(contenido));

            HBox botones = new HBox(12, btnAnadir, btnExist, btnEditar, btnDetalle);
            botones.setStyle("-fx-padding: 0 0 4 0;");
            vista.getChildren().add(botones);
        } else {
            HBox botones = new HBox(12, btnDetalle);
            botones.setStyle("-fx-padding: 0 0 4 0;");
            vista.getChildren().add(botones);
        }

        contenido.getChildren().add(vista);
    }

    // ── VER DETALLE PRENDA ───────────────────────────────────────────
    private void mostrarDetallePrenda(StackPane contenido, boolean esAdmin) {
        contenido.getChildren().clear();

        Label titulo    = new Label("Detalle de Prenda");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        Label subtitulo = new Label("Busca por nombre o ID para ver toda la información");
        subtitulo.setFont(Font.font("System", 12));
        subtitulo.setTextFill(Color.web(TEXTO_SUAVE));

        TextField campoBusqueda = crearTextField("Nombre o ID de la prenda");
        Button btnBuscar = new Button("Buscar");
        btnBuscar.setStyle(estiloBtnPrincipal());

        Label mensajeBusqueda = new Label("");
        mensajeBusqueda.setFont(Font.font("System", 12));

        VBox tarjeta = new VBox(12);
        tarjeta.setStyle("-fx-background-color: " + PANEL + "; -fx-border-color: " + PRINCIPAL + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 24; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3);");
        tarjeta.setVisible(false);
        tarjeta.setManaged(false);
        tarjeta.setMaxWidth(460);

        btnBuscar.setOnAction(e -> {
            String busqueda = campoBusqueda.getText().trim();
            if (busqueda.isEmpty()) { mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("Ingresa un nombre o ID"); return; }
            Prenda p = listaPrendas.stream()
                .filter(x -> x.getNombre().equalsIgnoreCase(busqueda) || x.getId().equals(busqueda))
                .findFirst().orElse(null);
            if (p == null) {
                mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("No se encontró la prenda");
                tarjeta.setVisible(false); tarjeta.setManaged(false);
            } else {
                mensajeBusqueda.setTextFill(Color.web(EXITO)); mensajeBusqueda.setText("Prenda encontrada");
                tarjeta.getChildren().clear();
                Label lNombre = new Label(p.getNombre() + "  —  Talla " + p.getTalla());
                lNombre.setFont(Font.font("System", FontWeight.BOLD, 17));
                lNombre.setTextFill(Color.web(SECUNDARIO));
                Region sep = new Region(); sep.setPrefHeight(1); sep.setStyle("-fx-background-color: #E5E7EB;");
                tarjeta.getChildren().addAll(lNombre, sep,
                    filaDetalle("ID:", p.getId()), filaDetalle("Tipo:", p.getTipoPrenda()),
                    filaDetalle("Talla:", p.getTalla()), filaDetalle("Existencia:", String.valueOf(p.getExistencia())),
                    filaDetalle("P. Mayoreo:", "$" + p.getPrecioMayoreo()), filaDetalle("P. Menudeo:", "$" + p.getPrecioMenudeo()),
                    filaDetalle("ID Tienda:", p.getIdTienda()), filaDetalle("Descripción:", p.getDescripcion()));
                tarjeta.setVisible(true); tarjeta.setManaged(true);
            }
        });

        Button btnRegresar = new Button("← Regresar a lista");
        btnRegresar.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXTO_SUAVE + "; -fx-font-size: 12px; -fx-cursor: hand;");
        btnRegresar.setOnAction(e -> mostrarModuloPrendas(contenido, esAdmin));

        VBox form = new VBox(12, titulo, subtitulo, campoBusqueda, btnBuscar, mensajeBusqueda, tarjeta, btnRegresar);
        form.setAlignment(Pos.TOP_LEFT); form.setMaxWidth(500);
        form.setStyle("-fx-background-color: " + PANEL + "; -fx-padding: 35; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3);");

        ScrollPane scroll = new ScrollPane(form);
        scroll.setFitToWidth(false);
        scroll.setFitToHeight(false);
        scroll.setStyle("-fx-background-color: " + FONDO + "; -fx-background: " + FONDO + ";");

        StackPane wrapper = new StackPane(scroll);
        wrapper.setStyle("-fx-background-color: " + FONDO + "; -fx-padding: 30;");
        wrapper.setAlignment(Pos.CENTER);
        contenido.getChildren().add(wrapper);
    }

    private HBox filaDetalle(String etiqueta, String valor) {
        Label lEtiqueta = new Label(etiqueta);
        lEtiqueta.setFont(Font.font("System", FontWeight.BOLD, 12));
        lEtiqueta.setTextFill(Color.web(TEXTO_SUAVE));
        lEtiqueta.setMinWidth(120);
        Label lValor = new Label(valor != null ? valor : "—");
        lValor.setFont(Font.font("System", 13));
        lValor.setTextFill(Color.web(TEXTO));
        lValor.setWrapText(true);
        HBox fila = new HBox(8, lEtiqueta, lValor);
        fila.setAlignment(Pos.CENTER_LEFT);
        return fila;
    }

    // ── MÓDULO CONJUNTOS ─────────────────────────────────────────────
    private void mostrarModuloConjuntos(StackPane contenido, boolean esAdmin) {
        verificarConjuntos();
        contenido.getChildren().clear();

        Label titulo = new Label("Conjuntos");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        TableView<Conjunto> tabla = new TableView<>();
        tabla.setStyle("-fx-background-color: " + PANEL + "; -fx-border-color: #E5E7EB;");
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabla.setPlaceholder(new Label("No hay conjuntos registrados"));
        tabla.setMinHeight(200);

        TableColumn<Conjunto, String> colId     = new TableColumn<>("ID");
        TableColumn<Conjunto, String> colNombre = new TableColumn<>("Nombre");
        TableColumn<Conjunto, String> colPiezas = new TableColumn<>("Piezas");
        TableColumn<Conjunto, String> colExist  = new TableColumn<>("Existencia");
       TableColumn<Conjunto, String> colPrecio  = new TableColumn<>("Precio");
        TableColumn<Conjunto, String> colMinimo  = new TableColumn<>("Mínimo");

        colId.setCellValueFactory(d     -> new SimpleStringProperty(d.getValue().getId()));
        colNombre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombre()));
        colPiezas.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getIdPrendas().size())));
        colExist.setCellValueFactory(d  -> new SimpleStringProperty(String.valueOf(calcularExistenciaConjunto(d.getValue()))));
        colPrecio.setCellValueFactory(d -> new SimpleStringProperty("$" + String.format("%.2f", calcularPrecioConjunto(d.getValue()))));
        colMinimo.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getMinimoExistencia())));

        tabla.getColumns().addAll(colId, colNombre, colPiezas, colExist, colMinimo, colPrecio);
        tabla.setItems(listaConjuntos);
        VBox.setVgrow(tabla, Priority.ALWAYS);

        Button btnDetalle = new Button("🔍 Ver Detalle");
        btnDetalle.setStyle("-fx-background-color: " + PRINCIPAL + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
        btnDetalle.setOnAction(e -> mostrarDetalleConjunto(contenido, esAdmin));

        VBox vista = new VBox(16, titulo, tabla);
        vista.setStyle("-fx-padding: 30;");
        VBox.setVgrow(tabla, Priority.ALWAYS);
        VBox.setVgrow(vista, Priority.ALWAYS);

        if (esAdmin) {
            Button btnAnadir = new Button("+ Nuevo Conjunto");
            Button btnEditar = new Button("✎ Editar Conjunto");
            btnAnadir.setStyle("-fx-background-color: " + NARANJA + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
            btnEditar.setStyle("-fx-background-color: " + AZUL_EDITAR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
            btnAnadir.setOnAction(e -> mostrarFormularioNuevoConjunto(contenido));
            btnEditar.setOnAction(e -> mostrarFormularioEditarConjunto(contenido));
            HBox botones = new HBox(12, btnAnadir, btnEditar, btnDetalle);
            vista.getChildren().add(botones);
        } else {
            HBox botones = new HBox(12, btnDetalle);
            vista.getChildren().add(botones);
        }

        contenido.getChildren().add(vista);
    }

    // ── VER DETALLE CONJUNTO ─────────────────────────────────────────
    private void mostrarDetalleConjunto(StackPane contenido, boolean esAdmin) {
        contenido.getChildren().clear();

        Label titulo    = new Label("Detalle de Conjunto");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        Label subtitulo = new Label("Busca por nombre o ID para ver toda la información");
        subtitulo.setFont(Font.font("System", 12));
        subtitulo.setTextFill(Color.web(TEXTO_SUAVE));

        TextField campoBusqueda = crearTextField("Nombre o ID del conjunto");
        Button btnBuscar = new Button("Buscar");
        btnBuscar.setStyle(estiloBtnPrincipal());

        Label mensajeBusqueda = new Label("");
        mensajeBusqueda.setFont(Font.font("System", 12));

        VBox tarjeta = new VBox(12);
        tarjeta.setStyle("-fx-background-color: " + PANEL + "; -fx-border-color: " + PRINCIPAL_ALT + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 24; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3);");
        tarjeta.setVisible(false);
        tarjeta.setManaged(false);
        tarjeta.setMaxWidth(480);

        btnBuscar.setOnAction(e -> {
            String busqueda = campoBusqueda.getText().trim();
            if (busqueda.isEmpty()) { mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("Ingresa un nombre o ID"); return; }
            Conjunto c = listaConjuntos.stream()
                .filter(x -> x.getNombre().equalsIgnoreCase(busqueda) || x.getId().equals(busqueda))
                .findFirst().orElse(null);
            if (c == null) {
                mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("No se encontró el conjunto");
                tarjeta.setVisible(false); tarjeta.setManaged(false);
            } else {
                mensajeBusqueda.setTextFill(Color.web(EXITO)); mensajeBusqueda.setText("Conjunto encontrado");
                tarjeta.getChildren().clear();
                Label lNombre = new Label(c.getNombre());
                lNombre.setFont(Font.font("System", FontWeight.BOLD, 17));
                lNombre.setTextFill(Color.web(SECUNDARIO));
                Region sep = new Region(); sep.setPrefHeight(1); sep.setStyle("-fx-background-color: #E5E7EB;");

                Label labelPrendasTitulo = new Label("Prendas:");
                labelPrendasTitulo.setFont(Font.font("System", FontWeight.BOLD, 12));
                labelPrendasTitulo.setTextFill(Color.web(TEXTO_SUAVE));
                labelPrendasTitulo.setMinWidth(120);

                VBox listaPrendasBox = new VBox(4);
                for (String idP : c.getIdPrendas()) {
                    Prenda p = listaPrendas.stream().filter(x -> x.getId().equals(idP)).findFirst().orElse(null);
                    Label lPrenda = new Label(p != null
                        ? "• " + p.getNombre() + " (Talla " + p.getTalla() + ") — Exist: " + p.getExistencia()
                        : "• ID " + idP + " (no encontrada)");
                    lPrenda.setFont(Font.font("System", 13));
                    lPrenda.setTextFill(p != null && p.getExistencia() > 0 ? Color.web(TEXTO) : Color.web(ERROR));
                    lPrenda.setWrapText(true);
                    listaPrendasBox.getChildren().add(lPrenda);
                }
                HBox filaPrendas = new HBox(8, labelPrendasTitulo, listaPrendasBox);
                filaPrendas.setAlignment(Pos.TOP_LEFT);

                tarjeta.getChildren().addAll(lNombre, sep,
                    filaDetalle("ID:", c.getId()),
                    filaDetalle("Piezas:", String.valueOf(c.getIdPrendas().size())),
                    filaDetalle("Existencia:", String.valueOf(calcularExistenciaConjunto(c))),
                    filaDetalle("Precio:", "$" + String.format("%.2f", calcularPrecioConjunto(c))),
                    filaDetalle("Descripción:", c.getDescripcion()),
                    filaPrendas);
                tarjeta.setVisible(true); tarjeta.setManaged(true);
            }
        });

        Button btnRegresar = new Button("← Regresar a lista");
        btnRegresar.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXTO_SUAVE + "; -fx-font-size: 12px; -fx-cursor: hand;");
        btnRegresar.setOnAction(e -> mostrarModuloConjuntos(contenido, esAdmin));

        VBox form = new VBox(12, titulo, subtitulo, campoBusqueda, btnBuscar, mensajeBusqueda, tarjeta, btnRegresar);
        form.setAlignment(Pos.TOP_LEFT); form.setMaxWidth(520);
        form.setStyle("-fx-background-color: " + PANEL + "; -fx-padding: 35; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3);");

        ScrollPane scroll = new ScrollPane(form);
        scroll.setFitToWidth(false);
        scroll.setFitToHeight(false);
        scroll.setStyle("-fx-background-color: " + FONDO + "; -fx-background: " + FONDO + ";");

        StackPane wrapper = new StackPane(scroll);
        wrapper.setStyle("-fx-background-color: " + FONDO + "; -fx-padding: 30;");
        wrapper.setAlignment(Pos.CENTER);
        contenido.getChildren().add(wrapper);
    }

    // ── MÓDULO PRENDAS VENDIDAS ──────────────────────────────────────
    private void mostrarModuloPrendasVendidas(StackPane contenido, boolean esAdmin) {
        contenido.getChildren().clear();

        Label titulo = new Label("Prendas Vendidas");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        TableView<PrendaVendida> tabla = new TableView<>();
        tabla.setStyle("-fx-background-color: " + PANEL + "; -fx-border-color: #E5E7EB;");
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabla.setPlaceholder(new Label("No hay prendas vendidas registradas"));
        tabla.setMinHeight(200);

        TableColumn<PrendaVendida, String> colId       = new TableColumn<>("ID Venta");
        TableColumn<PrendaVendida, String> colNombre   = new TableColumn<>("Prenda");
        TableColumn<PrendaVendida, String> colTalla    = new TableColumn<>("Talla");
        TableColumn<PrendaVendida, String> colCantidad = new TableColumn<>("Cant.");
        TableColumn<PrendaVendida, String> colTipo     = new TableColumn<>("Tipo");
        TableColumn<PrendaVendida, String> colPrecio   = new TableColumn<>("Precio Unit.");
        TableColumn<PrendaVendida, String> colTotal    = new TableColumn<>("Total");
        TableColumn<PrendaVendida, String> colFechaVta = new TableColumn<>("Fecha Venta");
        TableColumn<PrendaVendida, String> colFechaDev = new TableColumn<>("Límite Dev.");

        colId.setCellValueFactory(d       -> new SimpleStringProperty(d.getValue().getIdVenta()));
        colNombre.setCellValueFactory(d   -> new SimpleStringProperty(d.getValue().getNombrePrenda()));
        colTalla.setCellValueFactory(d    -> new SimpleStringProperty(d.getValue().getTalla()));
        colCantidad.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getCantidad())));
        colTipo.setCellValueFactory(d     -> new SimpleStringProperty(d.getValue().getTipoVenta()));
        colPrecio.setCellValueFactory(d   -> new SimpleStringProperty("$" + String.format("%.2f", d.getValue().getPrecioUnitario())));
        colTotal.setCellValueFactory(d    -> new SimpleStringProperty("$" + String.format("%.2f", d.getValue().getTotal())));
        colFechaVta.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getFechaVenta().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        colFechaDev.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getFechaLimiteDevolucion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

        tabla.getColumns().addAll(colId, colNombre, colTalla, colCantidad, colTipo, colPrecio, colTotal, colFechaVta, colFechaDev);
        tabla.setItems(listaPrendasVendidas);
        VBox.setVgrow(tabla, Priority.ALWAYS);

        Button btnDetalle = new Button("🔍 Ver Detalle");
        btnDetalle.setStyle("-fx-background-color: " + PRINCIPAL + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
        btnDetalle.setOnAction(e -> mostrarDetallePrendaVendida(contenido, esAdmin));

        VBox vista = new VBox(16, titulo, tabla);
        vista.setStyle("-fx-padding: 30;");
        VBox.setVgrow(tabla, Priority.ALWAYS);
        VBox.setVgrow(vista, Priority.ALWAYS);

        if (esAdmin) {
            Button btnAnadir = new Button("+ Registrar Venta");
            Button btnEditar = new Button("✎ Editar Registro");
            btnAnadir.setStyle("-fx-background-color: " + NARANJA + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
            btnEditar.setStyle("-fx-background-color: " + AZUL_EDITAR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
            btnAnadir.setOnAction(e -> mostrarFormularioNuevaPrendaVendida(contenido));
            btnEditar.setOnAction(e -> mostrarFormularioEditarPrendaVendida(contenido));
            HBox botones = new HBox(12, btnAnadir, btnEditar, btnDetalle);
            vista.getChildren().add(botones);
        } else {
            HBox botones = new HBox(12, btnDetalle);
            vista.getChildren().add(botones);
        }

        contenido.getChildren().add(vista);
    }

    // ── DETALLE PRENDA VENDIDA ───────────────────────────────────────
    private void mostrarDetallePrendaVendida(StackPane contenido, boolean esAdmin) {
        contenido.getChildren().clear();

        Label titulo    = new Label("Detalle de Prenda Vendida");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        Label subtitulo = new Label("Busca por ID de venta o nombre de prenda");
        subtitulo.setFont(Font.font("System", 12));
        subtitulo.setTextFill(Color.web(TEXTO_SUAVE));

        TextField campoBusqueda = crearTextField("ID de venta o nombre de prenda");
        Button btnBuscar = new Button("Buscar");
        btnBuscar.setStyle(estiloBtnPrincipal());

        Label mensajeBusqueda = new Label("");
        mensajeBusqueda.setFont(Font.font("System", 12));

        VBox tarjeta = new VBox(12);
        tarjeta.setStyle("-fx-background-color: " + PANEL + "; -fx-border-color: " + NARANJA + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 24; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3);");
        tarjeta.setVisible(false);
        tarjeta.setManaged(false);
        tarjeta.setMaxWidth(480);

        btnBuscar.setOnAction(e -> {
            String busqueda = campoBusqueda.getText().trim();
            if (busqueda.isEmpty()) { mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("Ingresa un ID o nombre"); return; }
            PrendaVendida pv = listaPrendasVendidas.stream()
                .filter(x -> x.getIdVenta().equalsIgnoreCase(busqueda) || x.getNombrePrenda().equalsIgnoreCase(busqueda))
                .findFirst().orElse(null);
            if (pv == null) {
                mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("No se encontró el registro");
                tarjeta.setVisible(false); tarjeta.setManaged(false);
            } else {
                mensajeBusqueda.setTextFill(Color.web(EXITO)); mensajeBusqueda.setText("Registro encontrado");
                tarjeta.getChildren().clear();
                Label lNombre = new Label(pv.getNombrePrenda() + " — Talla " + pv.getTalla());
                lNombre.setFont(Font.font("System", FontWeight.BOLD, 17));
                lNombre.setTextFill(Color.web(SECUNDARIO));
                Region sep = new Region(); sep.setPrefHeight(1); sep.setStyle("-fx-background-color: #E5E7EB;");

                boolean dentroDevolucion = !LocalDate.now().isAfter(pv.getFechaLimiteDevolucion());
                Label lEstado = new Label(dentroDevolucion ? "✔ Dentro del periodo de devolución" : "✕ Periodo de devolución vencido");
                lEstado.setFont(Font.font("System", FontWeight.BOLD, 12));
                lEstado.setTextFill(Color.web(dentroDevolucion ? EXITO : ERROR));

                tarjeta.getChildren().addAll(lNombre, sep,
                    filaDetalle("ID Venta:",         pv.getIdVenta()),
                    filaDetalle("Cantidad:",          String.valueOf(pv.getCantidad())),
                    filaDetalle("Tipo de venta:",     pv.getTipoVenta()),
                    filaDetalle("Precio unitario:",   "$" + String.format("%.2f", pv.getPrecioUnitario())),
                    filaDetalle("Total:",             "$" + String.format("%.2f", pv.getTotal())),
                    filaDetalle("Fecha de venta:",    pv.getFechaVenta().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))),
                    filaDetalle("Límite devolución:", pv.getFechaLimiteDevolucion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))),
                    filaDetalle("Descripción:",       pv.getDescripcion()),
                    lEstado);
                tarjeta.setVisible(true); tarjeta.setManaged(true);
            }
        });

        Button btnRegresar = new Button("← Regresar a lista");
        btnRegresar.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXTO_SUAVE + "; -fx-font-size: 12px; -fx-cursor: hand;");
        btnRegresar.setOnAction(e -> mostrarModuloPrendasVendidas(contenido, esAdmin));

        VBox form = new VBox(12, titulo, subtitulo, campoBusqueda, btnBuscar, mensajeBusqueda, tarjeta, btnRegresar);
        form.setAlignment(Pos.TOP_LEFT); form.setMaxWidth(520);
        form.setStyle("-fx-background-color: " + PANEL + "; -fx-padding: 35; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3);");

        ScrollPane scroll = new ScrollPane(form);
        scroll.setFitToWidth(false);
        scroll.setFitToHeight(false);
        scroll.setStyle("-fx-background-color: " + FONDO + "; -fx-background: " + FONDO + ";");

        StackPane wrapper = new StackPane(scroll);
        wrapper.setStyle("-fx-background-color: " + FONDO + "; -fx-padding: 30;");
        wrapper.setAlignment(Pos.CENTER);
        contenido.getChildren().add(wrapper);
    }

    // ── FORMULARIO NUEVA PRENDA VENDIDA ──────────────────────────────
    private void mostrarFormularioNuevaPrendaVendida(StackPane contenido) {
        contenido.getChildren().clear();

        Label titulo    = new Label("Registrar Prenda Vendida");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        Label subtitulo = new Label("Completa los campos para registrar la venta");
        subtitulo.setFont(Font.font("System", 12));
        subtitulo.setTextFill(Color.web(TEXTO_SUAVE));

        TextField campoIdVenta      = crearTextField("ID de venta");
        TextField campoNombrePrenda = crearTextField("Nombre de la prenda");
        TextField campoTalla        = crearTextField("Talla");
        TextField campoCantidad     = crearTextField("Cantidad");
        TextField campoPrecioUnit   = crearTextField("Precio unitario");
        TextField campoDescripcion  = crearTextField("Descripción");

        Label labelTipoVenta = new Label("Tipo de venta:");
        labelTipoVenta.setTextFill(Color.web(TEXTO_SUAVE));
        labelTipoVenta.setFont(Font.font("System", 12));

        ComboBox<String> selectorTipo = new ComboBox<>();
        selectorTipo.getItems().addAll("Menudeo", "Mayoreo");
        selectorTipo.setValue("Menudeo");
        selectorTipo.setMaxWidth(320);
        selectorTipo.setStyle(estiloInput());

        Label labelFechaVenta = new Label("Fecha de venta (dd/MM/yyyy):");
        labelFechaVenta.setTextFill(Color.web(TEXTO_SUAVE));
        labelFechaVenta.setFont(Font.font("System", 12));

        TextField campoFechaVenta = crearTextField("dd/MM/yyyy  (vacío = hoy)");

        Label mensajeEstado = new Label("");
        mensajeEstado.setFont(Font.font("System", 12));

        Button btnGuardar = new Button("Guardar Registro");
        btnGuardar.setMaxWidth(320);
        btnGuardar.setStyle("-fx-background-color: " + NARANJA + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 6; -fx-cursor: hand;");

        Button btnCancelar = new Button("← Regresar a lista");
        btnCancelar.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXTO_SUAVE + "; -fx-font-size: 12px; -fx-cursor: hand;");
        btnCancelar.setOnAction(e -> mostrarModuloPrendasVendidas(contenido, true));

        btnGuardar.setOnAction(e -> {
            String idVenta      = campoIdVenta.getText().trim();
            String nombrePrenda = campoNombrePrenda.getText().trim();
            String talla        = campoTalla.getText().trim();
            String cantidadStr  = campoCantidad.getText().trim();
            String precioStr    = campoPrecioUnit.getText().trim();
            String descripcion  = campoDescripcion.getText().trim();
            String tipoVenta    = selectorTipo.getValue();
            String fechaStr     = campoFechaVenta.getText().trim();

            if (idVenta.isEmpty() || nombrePrenda.isEmpty() || talla.isEmpty() || cantidadStr.isEmpty() || precioStr.isEmpty()) {
                mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("Todos los campos obligatorios deben llenarse"); return;
            }
            boolean yaExiste = listaPrendasVendidas.stream().anyMatch(pv -> pv.getIdVenta().equals(idVenta));
            if (yaExiste) { mensajeEstado.setTextFill(Color.web(ADVERTENCIA)); mensajeEstado.setText("Ya existe un registro con ese ID de venta"); return; }

            try {
                int cantidad      = Integer.parseInt(cantidadStr);
                double precioUnit = Double.parseDouble(precioStr);
                LocalDate fechaVenta;
                if (fechaStr.isEmpty()) {
                    fechaVenta = LocalDate.now();
                } else {
                    fechaVenta = LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                }
                LocalDate fechaDevolucion = fechaVenta.plusDays(DIAS_DEVOLUCION);
                listaPrendasVendidas.add(new PrendaVendida(idVenta, nombrePrenda, talla, cantidad, tipoVenta, precioUnit, fechaVenta, fechaDevolucion, descripcion));
                mostrarModuloPrendasVendidas(contenido, true);
            } catch (Exception ex) {
                mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("Verifica que los datos sean válidos");
            }
        });

        VBox form = new VBox(12, titulo, subtitulo, campoIdVenta, campoNombrePrenda, campoTalla,
                campoCantidad, campoPrecioUnit, labelTipoVenta, selectorTipo,
                labelFechaVenta, campoFechaVenta, campoDescripcion,
                mensajeEstado, btnGuardar, btnCancelar);
        form.setAlignment(Pos.TOP_LEFT); form.setMaxWidth(420);
        form.setStyle("-fx-background-color: " + PANEL + "; -fx-padding: 35; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3);");

        ScrollPane scroll = new ScrollPane(form);
        scroll.setFitToWidth(false);
        scroll.setFitToHeight(false);
        scroll.setStyle("-fx-background-color: " + FONDO + "; -fx-background: " + FONDO + ";");

        StackPane wrapper = new StackPane(scroll);
        wrapper.setStyle("-fx-background-color: " + FONDO + "; -fx-padding: 30;");
        wrapper.setAlignment(Pos.CENTER);
        contenido.getChildren().add(wrapper);
    }

    // ── FORMULARIO EDITAR PRENDA VENDIDA ─────────────────────────────
    private void mostrarFormularioEditarPrendaVendida(StackPane contenido) {
        contenido.getChildren().clear();

        Label titulo    = new Label("Editar Registro de Venta");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        Label subtitulo = new Label("Busca por ID de venta o nombre de prenda");
        subtitulo.setFont(Font.font("System", 12));
        subtitulo.setTextFill(Color.web(TEXTO_SUAVE));

        TextField campoBusqueda = crearTextField("ID de venta o nombre de prenda");
        Button btnBuscar = new Button("Buscar");
        btnBuscar.setStyle(estiloBtnPrincipal());

        Label mensajeBusqueda = new Label("");
        mensajeBusqueda.setFont(Font.font("System", 12));

        VBox panelEdicion = new VBox(10);
        panelEdicion.setVisible(false);
        panelEdicion.setManaged(false);

        TextField campoNombrePrenda = crearTextField("Nombre de la prenda");
        TextField campoTalla        = crearTextField("Talla");
        TextField campoCantidad     = crearTextField("Cantidad");
        TextField campoPrecioUnit   = crearTextField("Precio unitario");
        TextField campoDescripcion  = crearTextField("Descripción");
        TextField campoFechaVenta   = crearTextField("Fecha venta dd/MM/yyyy");

        Label labelTipoVenta = new Label("Tipo de venta:");
        labelTipoVenta.setTextFill(Color.web(TEXTO_SUAVE));
        labelTipoVenta.setFont(Font.font("System", 12));

        ComboBox<String> selectorTipo = new ComboBox<>();
        selectorTipo.getItems().addAll("Menudeo", "Mayoreo");
        selectorTipo.setValue("Menudeo");
        selectorTipo.setMaxWidth(320);
        selectorTipo.setStyle(estiloInput());

        Label mensajeEstado = new Label("");
        mensajeEstado.setFont(Font.font("System", 12));

        Button btnGuardar = new Button("Guardar Cambios");
        btnGuardar.setMaxWidth(320);
        btnGuardar.setStyle("-fx-background-color: " + AZUL_EDITAR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 6; -fx-cursor: hand;");

        panelEdicion.getChildren().addAll(campoNombrePrenda, campoTalla, campoCantidad, campoPrecioUnit,
                labelTipoVenta, selectorTipo, campoFechaVenta, campoDescripcion, mensajeEstado, btnGuardar);

        final PrendaVendida[] pvEncontrada = {null};

        btnBuscar.setOnAction(e -> {
            String busqueda = campoBusqueda.getText().trim();
            if (busqueda.isEmpty()) { mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("Ingresa un ID o nombre"); return; }
            PrendaVendida pv = listaPrendasVendidas.stream()
                .filter(x -> x.getIdVenta().equalsIgnoreCase(busqueda) || x.getNombrePrenda().equalsIgnoreCase(busqueda))
                .findFirst().orElse(null);
            if (pv == null) {
                mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("No se encontró el registro");
                panelEdicion.setVisible(false); panelEdicion.setManaged(false);
            } else {
                pvEncontrada[0] = pv;
                mensajeBusqueda.setTextFill(Color.web(EXITO)); mensajeBusqueda.setText("Registro encontrado");
                campoNombrePrenda.setText(pv.getNombrePrenda());
                campoTalla.setText(pv.getTalla());
                campoCantidad.setText(String.valueOf(pv.getCantidad()));
                campoPrecioUnit.setText(String.valueOf(pv.getPrecioUnitario()));
                selectorTipo.setValue(pv.getTipoVenta());
                campoFechaVenta.setText(pv.getFechaVenta().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                campoDescripcion.setText(pv.getDescripcion());
                mensajeEstado.setText("");
                panelEdicion.setVisible(true); panelEdicion.setManaged(true);
            }
        });

        btnGuardar.setOnAction(e -> {
            if (pvEncontrada[0] == null) return;
            String nombrePrenda = campoNombrePrenda.getText().trim();
            String talla        = campoTalla.getText().trim();
            String cantStr      = campoCantidad.getText().trim();
            String precioStr    = campoPrecioUnit.getText().trim();
            String fechaStr     = campoFechaVenta.getText().trim();
            String descripcion  = campoDescripcion.getText().trim();
            String tipoVenta    = selectorTipo.getValue();

            if (nombrePrenda.isEmpty() || talla.isEmpty() || cantStr.isEmpty() || precioStr.isEmpty() || fechaStr.isEmpty()) {
                mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("Todos los campos son obligatorios"); return;
            }
            try {
                int cantidad      = Integer.parseInt(cantStr);
                double precioUnit = Double.parseDouble(precioStr);
                LocalDate fechaVenta = LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                LocalDate fechaDev   = fechaVenta.plusDays(DIAS_DEVOLUCION);
                PrendaVendida pv = pvEncontrada[0];
                pv.setNombrePrenda(nombrePrenda); pv.setTalla(talla); pv.setCantidad(cantidad);
                pv.setPrecioUnitario(precioUnit); pv.setTipoVenta(tipoVenta);
                pv.setFechaVenta(fechaVenta); pv.setFechaLimiteDevolucion(fechaDev);
                pv.setDescripcion(descripcion);
                mensajeEstado.setTextFill(Color.web(EXITO)); mensajeEstado.setText("Registro actualizado correctamente");
            } catch (Exception ex) {
                mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("Verifica que los datos sean válidos");
            }
        });

        Button btnCancelar = new Button("← Regresar a lista");
        btnCancelar.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXTO_SUAVE + "; -fx-font-size: 12px; -fx-cursor: hand;");
        btnCancelar.setOnAction(e -> mostrarModuloPrendasVendidas(contenido, true));

        VBox form = new VBox(12, titulo, subtitulo, campoBusqueda, btnBuscar, mensajeBusqueda, panelEdicion, btnCancelar);
        form.setAlignment(Pos.TOP_LEFT); form.setMaxWidth(440);
        form.setStyle("-fx-background-color: " + PANEL + "; -fx-padding: 35; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3);");

        ScrollPane scroll = new ScrollPane(form);
        scroll.setFitToWidth(false);
        scroll.setFitToHeight(false);
        scroll.setStyle("-fx-background-color: " + FONDO + "; -fx-background: " + FONDO + ";");

        StackPane wrapper = new StackPane(scroll);
        wrapper.setStyle("-fx-background-color: " + FONDO + "; -fx-padding: 30;");
        wrapper.setAlignment(Pos.CENTER);
        contenido.getChildren().add(wrapper);
    }

// ── MÓDULO MATERIA PRIMA ─────────────────────────────────────────
    private void mostrarModuloMateriaPrima(StackPane contenido, boolean esAdmin) {
        contenido.getChildren().clear();

        Label titulo = new Label("Materia Prima");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        TableView<MateriaPrima> tabla = new TableView<>();
        tabla.setStyle("-fx-background-color: " + PANEL + "; -fx-border-color: #E5E7EB;");
        tabla.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tabla.setPlaceholder(new Label("No hay materia prima registrada"));
        tabla.setMinHeight(200);

        TableColumn<MateriaPrima, String> colId           = new TableColumn<>("ID");
        TableColumn<MateriaPrima, String> colPartida      = new TableColumn<>("No. Partida");
        TableColumn<MateriaPrima, String> colNombre       = new TableColumn<>("Nombre");
        TableColumn<MateriaPrima, String> colExist        = new TableColumn<>("Existencia");
        TableColumn<MateriaPrima, String> colMinimo       = new TableColumn<>("Mínimo");
        TableColumn<MateriaPrima, String> colTipoExist    = new TableColumn<>("Tipo Existencia");
        TableColumn<MateriaPrima, String> colDescripcion  = new TableColumn<>("Descripción");
        TableColumn<MateriaPrima, String> colColor        = new TableColumn<>("Color");
        TableColumn<MateriaPrima, String> colMedida       = new TableColumn<>("Medida");
        TableColumn<MateriaPrima, String> colAncho        = new TableColumn<>("Ancho");
        TableColumn<MateriaPrima, String> colComposicion  = new TableColumn<>("Composición");
        TableColumn<MateriaPrima, String> colTipo         = new TableColumn<>("Tipo");
        TableColumn<MateriaPrima, String> colNo           = new TableColumn<>("No.");
        TableColumn<MateriaPrima, String> colTamanio      = new TableColumn<>("Tamaño");
        TableColumn<MateriaPrima, String> colTalla2       = new TableColumn<>("Talla");
        TableColumn<MateriaPrima, String> colMaterial     = new TableColumn<>("Material");
        TableColumn<MateriaPrima, String> colTipoInsum    = new TableColumn<>("Tipo Insumo");

        colId.setCellValueFactory(d           -> new SimpleStringProperty(String.valueOf(d.getValue().getId())));
        colPartida.setCellValueFactory(d      -> new SimpleStringProperty(d.getValue().getNumeroPartida()));
        colNombre.setCellValueFactory(d       -> new SimpleStringProperty(d.getValue().getNombre()));
        colExist.setCellValueFactory(d        -> new SimpleStringProperty(String.valueOf(d.getValue().getExistencia())));
        colMinimo.setCellValueFactory(d       -> new SimpleStringProperty(String.valueOf(d.getValue().getMinimoExistencia())));
        colTipoExist.setCellValueFactory(d    -> new SimpleStringProperty(d.getValue().getTipoExistencia() != null && !d.getValue().getTipoExistencia().isEmpty() ? d.getValue().getTipoExistencia() : "—"));
        colDescripcion.setCellValueFactory(d  -> new SimpleStringProperty(d.getValue().getDescripcion() != null ? d.getValue().getDescripcion() : "—"));
        colColor.setCellValueFactory(d        -> new SimpleStringProperty(d.getValue().getColor() != null ? d.getValue().getColor() : "—"));
        colMedida.setCellValueFactory(d       -> new SimpleStringProperty(d.getValue().getMedida() != null ? d.getValue().getMedida() : "—"));
        colAncho.setCellValueFactory(d        -> new SimpleStringProperty(d.getValue().getAncho() != null ? String.valueOf(d.getValue().getAncho()) : "—"));
        colComposicion.setCellValueFactory(d  -> new SimpleStringProperty(d.getValue().getComposicion() != null ? d.getValue().getComposicion() : "—"));
        colTipo.setCellValueFactory(d         -> new SimpleStringProperty(d.getValue().getTipo() != null ? d.getValue().getTipo() : "—"));
        colNo.setCellValueFactory(d           -> new SimpleStringProperty(d.getValue().getNo() != null ? String.valueOf(d.getValue().getNo()) : "—"));
        colTamanio.setCellValueFactory(d      -> new SimpleStringProperty(d.getValue().getTamanio() != null ? d.getValue().getTamanio() : "—"));
        colTalla2.setCellValueFactory(d       -> new SimpleStringProperty(d.getValue().getTalla() != null ? d.getValue().getTalla() : "—"));
        colMaterial.setCellValueFactory(d     -> new SimpleStringProperty(d.getValue().getMaterial() != null ? d.getValue().getMaterial() : "—"));
        colTipoInsum.setCellValueFactory(d    -> new SimpleStringProperty(d.getValue().getTipoInsumo()));

        tabla.getColumns().addAll(colId, colPartida, colNombre, colExist, colMinimo,
                colTipoExist, colDescripcion, colColor, colMedida, colAncho,
                colComposicion, colTipo, colNo, colTamanio, colTalla2, colMaterial, colTipoInsum);
        tabla.setItems(listaMateriaPrima);

        ScrollPane scrollTablaMP = new ScrollPane(tabla);
        scrollTablaMP.setFitToHeight(true);
        scrollTablaMP.setFitToWidth(false);
        scrollTablaMP.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollTablaMP.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollTablaMP.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        VBox.setVgrow(scrollTablaMP, Priority.ALWAYS);

        Button btnDetalle = new Button("🔍 Ver Detalle");
        btnDetalle.setStyle("-fx-background-color: " + PRINCIPAL + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
        btnDetalle.setOnAction(e -> mostrarDetalleMateriaPrima(contenido, esAdmin));

        VBox vista = new VBox(16, titulo, scrollTablaMP);
        vista.setStyle("-fx-padding: 30;");
        VBox.setVgrow(scrollTablaMP, Priority.ALWAYS);
        VBox.setVgrow(vista, Priority.ALWAYS);

        if (esAdmin) {
            Button btnAnadir = new Button("+ Añadir Insumo");
            Button btnExist  = new Button("+ Añadir a Existente");
            Button btnEditar = new Button("✎ Editar Insumo");

            btnAnadir.setStyle("-fx-background-color: " + NARANJA + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
            btnExist.setStyle("-fx-background-color: " + CAFE + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
            btnEditar.setStyle("-fx-background-color: " + AZUL_EDITAR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");

            btnAnadir.setOnAction(e -> mostrarFormularioNuevaMateriaPrima(contenido));
            btnExist.setOnAction(e  -> mostrarFormularioAnadirExistenteMP(contenido));
            btnEditar.setOnAction(e -> mostrarFormularioEditarMateriaPrima(contenido));

            HBox botones = new HBox(12, btnAnadir, btnExist, btnEditar, btnDetalle);
            vista.getChildren().add(botones);
        } else {
            HBox botones = new HBox(12, btnDetalle);
            vista.getChildren().add(botones);
        }

        contenido.getChildren().add(vista);
    }


    // ── VER DETALLE MATERIA PRIMA ────────────────────────────────────
    private void mostrarDetalleMateriaPrima(StackPane contenido, boolean esAdmin) {
        contenido.getChildren().clear();

        Label titulo    = new Label("Detalle de Materia Prima");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        Label subtitulo = new Label("Busca por nombre o número de partida para ver toda la información");
        subtitulo.setFont(Font.font("System", 12));
        subtitulo.setTextFill(Color.web(TEXTO_SUAVE));

        TextField campoBusqueda = crearTextField("Nombre o número de partida");
        Button btnBuscar = new Button("Buscar");
        btnBuscar.setStyle(estiloBtnPrincipal());

        Label mensajeBusqueda = new Label("");
        mensajeBusqueda.setFont(Font.font("System", 12));

        VBox tarjeta = new VBox(12);
        tarjeta.setStyle("-fx-background-color: " + PANEL + "; -fx-border-color: " + PRINCIPAL + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 24; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3);");
        tarjeta.setVisible(false);
        tarjeta.setManaged(false);
        tarjeta.setMaxWidth(480);

        btnBuscar.setOnAction(e -> {
            String busqueda = campoBusqueda.getText().trim();
            if (busqueda.isEmpty()) { mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("Ingresa un nombre o número de partida"); return; }
            MateriaPrima mp = listaMateriaPrima.stream()
                .filter(x -> x.getNombre().equalsIgnoreCase(busqueda) || x.getNumeroPartida().equalsIgnoreCase(busqueda))
                .findFirst().orElse(null);
            if (mp == null) {
                mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("No se encontró el insumo");
                tarjeta.setVisible(false); tarjeta.setManaged(false);
            } else {
                mensajeBusqueda.setTextFill(Color.web(EXITO)); mensajeBusqueda.setText("Insumo encontrado");
                tarjeta.getChildren().clear();
                Label lNombre = new Label(mp.getNombre() + "  —  " + mp.getTipoInsumo());
                lNombre.setFont(Font.font("System", FontWeight.BOLD, 17));
                lNombre.setTextFill(Color.web(SECUNDARIO));
                Region sep = new Region(); sep.setPrefHeight(1); sep.setStyle("-fx-background-color: #E5E7EB;");
                tarjeta.getChildren().addAll(lNombre, sep,
                    filaDetalle("ID:",              String.valueOf(mp.getId())),
                    filaDetalle("No. Partida:",     mp.getNumeroPartida()),
                    filaDetalle("Existencia:",      String.valueOf(mp.getExistencia())),
                    filaDetalle("Tipo Existencia:", mp.getTipoExistencia() != null && !mp.getTipoExistencia().isEmpty() ? mp.getTipoExistencia() : "—"),
                    filaDetalle("Color:",           mp.getColor()        != null ? mp.getColor()        : "—"),
                    filaDetalle("Medida:",          mp.getMedida()       != null ? mp.getMedida()        : "—"),
                    filaDetalle("Ancho:",           mp.getAncho()        != null ? mp.getAncho() + " m"  : "—"),
                    filaDetalle("Composición:",     mp.getComposicion()  != null ? mp.getComposicion()   : "—"),
                    filaDetalle("Tipo:",            mp.getTipo()         != null ? mp.getTipo()          : "—"),
                    filaDetalle("No.:",             mp.getNo()           != null ? String.valueOf(mp.getNo()) : "—"),
                    filaDetalle("Tamaño:",          mp.getTamanio()      != null ? mp.getTamanio()       : "—"),
                    filaDetalle("Talla:",           mp.getTalla()        != null ? mp.getTalla()         : "—"),
                    filaDetalle("Material:",        mp.getMaterial()     != null ? mp.getMaterial()      : "—"),
                    filaDetalle("Tipo Insumo:",     mp.getTipoInsumo()),
                    filaDetalle("Descripción:",     mp.getDescripcion()));
                tarjeta.setVisible(true); tarjeta.setManaged(true);
            }
        });

        Button btnRegresar = new Button("← Regresar a lista");
        btnRegresar.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXTO_SUAVE + "; -fx-font-size: 12px; -fx-cursor: hand;");
        btnRegresar.setOnAction(e -> mostrarModuloMateriaPrima(contenido, esAdmin));

        VBox form = new VBox(12, titulo, subtitulo, campoBusqueda, btnBuscar, mensajeBusqueda, tarjeta, btnRegresar);
        form.setAlignment(Pos.TOP_LEFT); form.setMaxWidth(520);
        form.setStyle("-fx-background-color: " + PANEL + "; -fx-padding: 35; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3);");

        ScrollPane scroll = new ScrollPane(form);
        scroll.setFitToWidth(false);
        scroll.setFitToHeight(false);
        scroll.setStyle("-fx-background-color: " + FONDO + "; -fx-background: " + FONDO + ";");

        StackPane wrapper = new StackPane(scroll);
        wrapper.setStyle("-fx-background-color: " + FONDO + "; -fx-padding: 30;");
        wrapper.setAlignment(Pos.CENTER);
        contenido.getChildren().add(wrapper);
    }

    // ── FORMULARIO NUEVO INSUMO ──────────────────────────────────────
    private void mostrarFormularioNuevaMateriaPrima(StackPane contenido) {
        contenido.getChildren().clear();

        Label titulo    = new Label("Nuevo Insumo");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        Label subtitulo = new Label("Completa los campos para registrar el insumo");
        subtitulo.setFont(Font.font("System", 12));
        subtitulo.setTextFill(Color.web(TEXTO_SUAVE));

        TextField campoPartida      = crearTextField("Número de partida  (ej: BOT-003)");
        TextField campoNombre       = crearTextField("Nombre del insumo");
        TextField campoExistencia   = crearTextField("Existencia inicial");
        TextField campoMinimo       = crearTextField("Mínimo de existencia  (ej: 10)");
        TextField campoTipoExist    = crearTextField("Tipo de existencia  (opcional)");
        TextField campoDescripcion  = crearTextField("Descripción  (opcional)");
        TextField campoColor        = crearTextField("Color  (opcional)");
        TextField campoMedida       = crearTextField("Medida  (opcional)");
        TextField campoAncho        = crearTextField("Ancho en metros  (opcional)");
        TextField campoComposicion  = crearTextField("Composición  (opcional, ej: 100% ALG)");
        TextField campoTipo         = crearTextField("Tipo de tejido/estructura  (opcional)");
        TextField campoNo           = crearTextField("No.  (opcional, ej: 14)");
        TextField campoTamanio      = crearTextField("Tamaño  (opcional)");
        TextField campoTalla        = crearTextField("Talla  (opcional)");
        TextField campoMaterial     = crearTextField("Material  (opcional, ej: Plástico)");

        Label labelTipoInsumo = new Label("Tipo de insumo:");
        labelTipoInsumo.setTextFill(Color.web(TEXTO_SUAVE));
        labelTipoInsumo.setFont(Font.font("System", 12));

        ComboBox<String> selectorTipoInsumo = new ComboBox<>();
        selectorTipoInsumo.getItems().addAll("Botón", "Cierre", "Tela plana", "Tela punto", "Hilo", "Otro");
        selectorTipoInsumo.setValue("Botón");
        selectorTipoInsumo.setMaxWidth(320);
        selectorTipoInsumo.setStyle(estiloInput());

        Label mensajeEstado = new Label("");
        mensajeEstado.setFont(Font.font("System", 12));

        Button btnGuardar = new Button("Guardar Insumo");
        btnGuardar.setMaxWidth(320);
        btnGuardar.setStyle("-fx-background-color: " + NARANJA + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 6; -fx-cursor: hand;");

        Button btnCancelar = new Button("← Regresar a lista");
        btnCancelar.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXTO_SUAVE + "; -fx-font-size: 12px; -fx-cursor: hand;");
        btnCancelar.setOnAction(e -> mostrarModuloMateriaPrima(contenido, true));

        btnGuardar.setOnAction(e -> {
            String partida      = campoPartida.getText().trim();
            String nombre       = campoNombre.getText().trim();
            String existStr     = campoExistencia.getText().trim();
            String tipoExist    = campoTipoExist.getText().trim();
            String descripcion  = campoDescripcion.getText().trim();
            String color        = campoColor.getText().trim();
            String medida       = campoMedida.getText().trim();
            String anchoStr     = campoAncho.getText().trim();
            String composicion  = campoComposicion.getText().trim();
            String tipo         = campoTipo.getText().trim();
            String noStr        = campoNo.getText().trim();
            String tamanio      = campoTamanio.getText().trim();
            String talla        = campoTalla.getText().trim();
            String material     = campoMaterial.getText().trim();
            String tipoInsumo   = selectorTipoInsumo.getValue();

            String minimoStr    = campoMinimo.getText().trim();
            if (partida.isEmpty() || nombre.isEmpty() || existStr.isEmpty() || minimoStr.isEmpty()) {
                mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("Partida, nombre, existencia y mínimo son obligatorios"); return;
            }
            boolean yaExiste = listaMateriaPrima.stream().anyMatch(mp -> mp.getNumeroPartida().equalsIgnoreCase(partida));
            if (yaExiste) { mensajeEstado.setTextFill(Color.web(ADVERTENCIA)); mensajeEstado.setText("Ya existe un insumo con ese número de partida"); return; }

        try {
                int existencia   = Integer.parseInt(existStr);
                int minimo       = Integer.parseInt(minimoStr);
                Double ancho     = anchoStr.isEmpty()  ? null : Double.parseDouble(anchoStr);
                Integer no       = noStr.isEmpty()     ? null : Integer.parseInt(noStr);
                int nuevoId      = listaMateriaPrima.isEmpty() ? 1 : listaMateriaPrima.stream().mapToInt(MateriaPrima::getId).max().orElse(0) + 1;

                MateriaPrima nuevaMP = new MateriaPrima(
                    nuevoId, partida, existencia,
                    tipoExist.isEmpty()   ? "" : tipoExist,
                    descripcion.isEmpty() ? "" : descripcion,
                    nombre,
                    color.isEmpty()       ? null : color,
                    medida.isEmpty()      ? null : medida,
                    ancho,
                    composicion.isEmpty() ? null : composicion,
                    tipo.isEmpty()        ? null : tipo,
                    no,
                    tamanio.isEmpty()     ? null : tamanio,
                    talla.isEmpty()       ? null : talla,
                    material.isEmpty()    ? null : material,
                    tipoInsumo
                );
                nuevaMP.setMinimoExistencia(minimo);
                listaMateriaPrima.add(nuevaMP);
                mostrarModuloMateriaPrima(contenido, true);
            } catch (NumberFormatException ex) {
                mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("Existencia, ancho y No. deben ser números válidos");
            }
        });

        VBox form = new VBox(10, titulo, subtitulo, campoPartida, campoNombre,
                campoExistencia, campoTipoExist, campoMinimo , labelTipoInsumo, selectorTipoInsumo,
                campoColor, campoMedida, campoAncho, campoComposicion,
                campoTipo, campoNo, campoTamanio, campoTalla, campoMaterial,
                campoDescripcion, mensajeEstado, btnGuardar, btnCancelar);
        form.setAlignment(Pos.TOP_LEFT); form.setMaxWidth(400);
        form.setStyle("-fx-background-color: " + PANEL + "; -fx-padding: 35; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3);");

        ScrollPane scroll = new ScrollPane(form);
        scroll.setFitToWidth(false);
        scroll.setFitToHeight(false);
        scroll.setStyle("-fx-background-color: " + FONDO + "; -fx-background: " + FONDO + ";");

        StackPane wrapper = new StackPane(scroll);
        wrapper.setStyle("-fx-background-color: " + FONDO + "; -fx-padding: 30;");
        wrapper.setAlignment(Pos.CENTER);
        contenido.getChildren().add(wrapper);
    }

    // ── FORMULARIO AÑADIR A EXISTENTE (MP) ──────────────────────────
    private void mostrarFormularioAnadirExistenteMP(StackPane contenido) {
        contenido.getChildren().clear();

        Label titulo    = new Label("Añadir a Existente");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        Label subtitulo = new Label("Busca por nombre o número de partida y añade unidades");
        subtitulo.setFont(Font.font("System", 12));
        subtitulo.setTextFill(Color.web(TEXTO_SUAVE));

        TextField campoBusqueda = crearTextField("Nombre o número de partida");
        Label mensajeBusqueda   = new Label("");
        mensajeBusqueda.setFont(Font.font("System", 12));

        VBox panelResultado = new VBox(8);
        panelResultado.setStyle("-fx-background-color: #F0FDF4; -fx-border-color: " + EXITO + "; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 12;");
        panelResultado.setVisible(false);
        panelResultado.setManaged(false);

        Label labelResultado    = new Label("");
        TextField campoCantidad = crearTextField("Cantidad a añadir");
        Label mensajeEstado     = new Label("");
        mensajeEstado.setFont(Font.font("System", 12));

        Button btnAnadir = new Button("Añadir Unidades");
        btnAnadir.setMaxWidth(320);
        btnAnadir.setStyle("-fx-background-color: " + CAFE + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 6; -fx-cursor: hand;");
        panelResultado.getChildren().addAll(labelResultado, campoCantidad, mensajeEstado, btnAnadir);

        final MateriaPrima[] mpEncontrada = {null};

        Button btnBuscar = new Button("Buscar");
        btnBuscar.setStyle(estiloBtnPrincipal());
        btnBuscar.setOnAction(e -> {
            String busqueda = campoBusqueda.getText().trim();
            if (busqueda.isEmpty()) { mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("Ingresa un nombre o número de partida"); return; }
            MateriaPrima encontrada = listaMateriaPrima.stream()
                .filter(mp -> mp.getNombre().equalsIgnoreCase(busqueda) || mp.getNumeroPartida().equalsIgnoreCase(busqueda))
                .findFirst().orElse(null);
            if (encontrada == null) {
                mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("No se encontró ningún insumo");
                panelResultado.setVisible(false); panelResultado.setManaged(false);
            } else {
                mpEncontrada[0] = encontrada;
                mensajeBusqueda.setTextFill(Color.web(EXITO)); mensajeBusqueda.setText("Insumo encontrado");
                labelResultado.setText("Insumo: " + encontrada.getNombre() + " | Partida: " + encontrada.getNumeroPartida() + " | Existencia: " + encontrada.getExistencia());
                labelResultado.setTextFill(Color.web(TEXTO));
                panelResultado.setVisible(true); panelResultado.setManaged(true);
                mensajeEstado.setText(""); campoCantidad.clear();
            }
        });

        btnAnadir.setOnAction(e -> {
            if (mpEncontrada[0] == null) return;
            try {
                int cantidad = Integer.parseInt(campoCantidad.getText().trim());
                if (cantidad <= 0) { mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("La cantidad debe ser mayor a 0"); return; }
                mpEncontrada[0].setExistencia(mpEncontrada[0].getExistencia() + cantidad);
                mensajeEstado.setTextFill(Color.web(EXITO));
                mensajeEstado.setText("Se añadieron " + cantidad + " unidades. Nueva existencia: " + mpEncontrada[0].getExistencia());
                labelResultado.setText("Insumo: " + mpEncontrada[0].getNombre() + " | Partida: " + mpEncontrada[0].getNumeroPartida() + " | Existencia: " + mpEncontrada[0].getExistencia());
                campoCantidad.clear();
            } catch (NumberFormatException ex) { mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("Ingresa un número válido"); }
        });

        Button btnCancelar = new Button("← Regresar a lista");
        btnCancelar.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXTO_SUAVE + "; -fx-font-size: 12px; -fx-cursor: hand;");
        btnCancelar.setOnAction(e -> mostrarModuloMateriaPrima(contenido, true));

        VBox form = new VBox(12, titulo, subtitulo, campoBusqueda, btnBuscar, mensajeBusqueda, panelResultado, btnCancelar);
        form.setAlignment(Pos.TOP_LEFT); form.setMaxWidth(420);
        form.setStyle("-fx-background-color: " + PANEL + "; -fx-padding: 35; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3);");

        ScrollPane scroll = new ScrollPane(form);
        scroll.setFitToWidth(false);
        scroll.setFitToHeight(false);
        scroll.setStyle("-fx-background-color: " + FONDO + "; -fx-background: " + FONDO + ";");

        StackPane wrapper = new StackPane(scroll);
        wrapper.setStyle("-fx-background-color: " + FONDO + "; -fx-padding: 30;");
        wrapper.setAlignment(Pos.CENTER);
        contenido.getChildren().add(wrapper);
    }

    // ── FORMULARIO EDITAR INSUMO ─────────────────────────────────────
    private void mostrarFormularioEditarMateriaPrima(StackPane contenido) {
        contenido.getChildren().clear();

        Label titulo    = new Label("Editar Insumo");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        Label subtitulo = new Label("Busca por nombre o número de partida y modifica los campos");
        subtitulo.setFont(Font.font("System", 12));
        subtitulo.setTextFill(Color.web(TEXTO_SUAVE));

        TextField campoBusqueda = crearTextField("Nombre o número de partida");
        Label mensajeBusqueda   = new Label("");
        mensajeBusqueda.setFont(Font.font("System", 12));

        VBox panelEdicion = new VBox(10);
        panelEdicion.setVisible(false);
        panelEdicion.setManaged(false);

        TextField campoNombre      = crearTextField("Nombre del insumo");
        TextField campoExistencia  = crearTextField("Existencia");
        TextField campoMinimo      = crearTextField("Mínimo de existencia");
        TextField campoTipoExist   = crearTextField("Tipo de existencia");
        TextField campoDescripcion = crearTextField("Descripción");
        TextField campoColor       = crearTextField("Color");
        TextField campoMedida      = crearTextField("Medida");
        TextField campoAncho       = crearTextField("Ancho en metros");
        TextField campoComposicion = crearTextField("Composición");
        TextField campoTipo        = crearTextField("Tipo de tejido/estructura");
        TextField campoNo          = crearTextField("No.");
        TextField campoTamanio     = crearTextField("Tamaño");
        TextField campoTalla       = crearTextField("Talla");
        TextField campoMaterial    = crearTextField("Material");

        Label labelTipoInsumo = new Label("Tipo de insumo:");
        labelTipoInsumo.setTextFill(Color.web(TEXTO_SUAVE));
        labelTipoInsumo.setFont(Font.font("System", 12));

        ComboBox<String> selectorTipoInsumo = new ComboBox<>();
        selectorTipoInsumo.getItems().addAll("Botón", "Cierre", "Tela plana", "Tela punto", "Hilo", "Otro");
        selectorTipoInsumo.setMaxWidth(320);
        selectorTipoInsumo.setStyle(estiloInput());

        Label mensajeEstado = new Label("");
        mensajeEstado.setFont(Font.font("System", 12));

        Button btnGuardar = new Button("Guardar Cambios");
        btnGuardar.setMaxWidth(320);
        btnGuardar.setStyle("-fx-background-color: " + AZUL_EDITAR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 6; -fx-cursor: hand;");

        panelEdicion.getChildren().addAll(campoNombre, campoExistencia,campoMinimo,  campoTipoExist,
                labelTipoInsumo, selectorTipoInsumo,
                campoColor, campoMedida, campoAncho, campoComposicion,
                campoTipo, campoNo, campoTamanio, campoTalla, campoMaterial,
                campoDescripcion, mensajeEstado, btnGuardar);

        final MateriaPrima[] mpEncontrada = {null};

        Button btnBuscar = new Button("Buscar");
        btnBuscar.setStyle(estiloBtnPrincipal());
        btnBuscar.setOnAction(e -> {
            String busqueda = campoBusqueda.getText().trim();
            if (busqueda.isEmpty()) { mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("Ingresa un nombre o número de partida"); return; }
            MateriaPrima encontrada = listaMateriaPrima.stream()
                .filter(mp -> mp.getNombre().equalsIgnoreCase(busqueda) || mp.getNumeroPartida().equalsIgnoreCase(busqueda))
                .findFirst().orElse(null);
            if (encontrada == null) {
                mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("No se encontró ningún insumo");
                panelEdicion.setVisible(false); panelEdicion.setManaged(false);
            } else {
                mpEncontrada[0] = encontrada;
                mensajeBusqueda.setTextFill(Color.web(EXITO)); mensajeBusqueda.setText("Insumo encontrado");
                campoNombre.setText(encontrada.getNombre());
                campoExistencia.setText(String.valueOf(encontrada.getExistencia()));
                campoMinimo.setText(String.valueOf(encontrada.getMinimoExistencia()));
                campoTipoExist.setText(encontrada.getTipoExistencia() != null ? encontrada.getTipoExistencia() : "");
                campoDescripcion.setText(encontrada.getDescripcion() != null ? encontrada.getDescripcion() : "");
                campoColor.setText(encontrada.getColor() != null ? encontrada.getColor() : "");
                campoMedida.setText(encontrada.getMedida() != null ? encontrada.getMedida() : "");
                campoAncho.setText(encontrada.getAncho() != null ? String.valueOf(encontrada.getAncho()) : "");
                campoComposicion.setText(encontrada.getComposicion() != null ? encontrada.getComposicion() : "");
                campoTipo.setText(encontrada.getTipo() != null ? encontrada.getTipo() : "");
                campoNo.setText(encontrada.getNo() != null ? String.valueOf(encontrada.getNo()) : "");
                campoTamanio.setText(encontrada.getTamanio() != null ? encontrada.getTamanio() : "");
                campoTalla.setText(encontrada.getTalla() != null ? encontrada.getTalla() : "");
                campoMaterial.setText(encontrada.getMaterial() != null ? encontrada.getMaterial() : "");
                selectorTipoInsumo.setValue(encontrada.getTipoInsumo());
                mensajeEstado.setText(""); panelEdicion.setVisible(true); panelEdicion.setManaged(true);
            }
        });

        btnGuardar.setOnAction(e -> {
            if (mpEncontrada[0] == null) return;
            String nombre      = campoNombre.getText().trim();
            String existStr    = campoExistencia.getText().trim();
            String minimoStr   = campoMinimo.getText().trim();
            String tipoExist   = campoTipoExist.getText().trim();
            String descripcion = campoDescripcion.getText().trim();
            String color       = campoColor.getText().trim();
            String medida      = campoMedida.getText().trim();
            String anchoStr    = campoAncho.getText().trim();
            String composicion = campoComposicion.getText().trim();
            String tipo        = campoTipo.getText().trim();
            String noStr       = campoNo.getText().trim();
            String tamanio     = campoTamanio.getText().trim();
            String talla       = campoTalla.getText().trim();
            String material    = campoMaterial.getText().trim();
            String tipoInsumo  = selectorTipoInsumo.getValue();

            if (nombre.isEmpty() || existStr.isEmpty() || minimoStr.isEmpty()) {
                mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("Nombre, existencia y mínimo son obligatorios"); return;
            }
            try {
                int existencia  = Integer.parseInt(existStr);
                int minimo      = Integer.parseInt(minimoStr);
                if (existencia < 0) { mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("La existencia no puede ser negativa"); return; }
                Double ancho    = anchoStr.isEmpty() ? null : Double.parseDouble(anchoStr);
                Integer no      = noStr.isEmpty()    ? null : Integer.parseInt(noStr);

                MateriaPrima mp = mpEncontrada[0];
                mp.setNombre(nombre);
                mp.setExistencia(existencia);
                mp.setTipoExistencia(tipoExist.isEmpty()   ? "" : tipoExist);
                mp.setDescripcion(descripcion.isEmpty()    ? "" : descripcion);
                mp.setColor(color.isEmpty()                ? null : color);
                mp.setMedida(medida.isEmpty()              ? null : medida);
                mp.setAncho(ancho);
                mp.setComposicion(composicion.isEmpty()    ? null : composicion);
                mp.setTipo(tipo.isEmpty()                  ? null : tipo);
                mp.setNo(no);
                mp.setTamanio(tamanio.isEmpty()            ? null : tamanio);
                mp.setTalla(talla.isEmpty()                ? null : talla);
                mp.setMaterial(material.isEmpty()          ? null : material);
                mp.setTipoInsumo(tipoInsumo);
                mp.setMinimoExistencia(minimo);
                mensajeEstado.setTextFill(Color.web(EXITO)); mensajeEstado.setText("Insumo actualizado correctamente");
            } catch (NumberFormatException ex) {
                mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("Existencia, ancho y No. deben ser números válidos");
            }
        });

        Button btnCancelar = new Button("← Regresar a lista");
        btnCancelar.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXTO_SUAVE + "; -fx-font-size: 12px; -fx-cursor: hand;");
        btnCancelar.setOnAction(e -> mostrarModuloMateriaPrima(contenido, true));

        VBox form = new VBox(12, titulo, subtitulo, campoBusqueda, btnBuscar, mensajeBusqueda, panelEdicion, btnCancelar);
        form.setAlignment(Pos.TOP_LEFT); form.setMaxWidth(420);
        form.setStyle("-fx-background-color: " + PANEL + "; -fx-padding: 35; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3);");

        ScrollPane scroll = new ScrollPane(form);
        scroll.setFitToWidth(false);
        scroll.setFitToHeight(false);
        scroll.setStyle("-fx-background-color: " + FONDO + "; -fx-background: " + FONDO + ";");

        StackPane wrapper = new StackPane(scroll);
        wrapper.setStyle("-fx-background-color: " + FONDO + "; -fx-padding: 30;");
        wrapper.setAlignment(Pos.CENTER);
        contenido.getChildren().add(wrapper);
    }

    // ── MÓDULO CONJUNTOS VENDIDOS ────────────────────────────────────
    private void mostrarModuloConjuntosVendidos(StackPane contenido, boolean esAdmin) {
        contenido.getChildren().clear();

        Label titulo = new Label("Conjuntos Vendidos");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        TableView<ConjuntoVendido> tabla = new TableView<>();
        tabla.setStyle("-fx-background-color: " + PANEL + "; -fx-border-color: #E5E7EB;");
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabla.setPlaceholder(new Label("No hay conjuntos vendidos registrados"));
        tabla.setMinHeight(200);

        TableColumn<ConjuntoVendido, String> colId       = new TableColumn<>("ID Venta");
        TableColumn<ConjuntoVendido, String> colNombre   = new TableColumn<>("Conjunto");
        TableColumn<ConjuntoVendido, String> colCantidad = new TableColumn<>("Cant.");
        TableColumn<ConjuntoVendido, String> colTipo     = new TableColumn<>("Tipo");
        TableColumn<ConjuntoVendido, String> colPrecio   = new TableColumn<>("Precio Unit.");
        TableColumn<ConjuntoVendido, String> colTotal    = new TableColumn<>("Total");
        TableColumn<ConjuntoVendido, String> colFechaVta = new TableColumn<>("Fecha Venta");
        TableColumn<ConjuntoVendido, String> colFechaDev = new TableColumn<>("Límite Dev.");

        colId.setCellValueFactory(d       -> new SimpleStringProperty(d.getValue().getIdVenta()));
        colNombre.setCellValueFactory(d   -> new SimpleStringProperty(d.getValue().getNombreConjunto()));
        colCantidad.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getCantidad())));
        colTipo.setCellValueFactory(d     -> new SimpleStringProperty(d.getValue().getTipoVenta()));
        colPrecio.setCellValueFactory(d   -> new SimpleStringProperty("$" + String.format("%.2f", d.getValue().getPrecioUnitario())));
        colTotal.setCellValueFactory(d    -> new SimpleStringProperty("$" + String.format("%.2f", d.getValue().getTotal())));
        colFechaVta.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getFechaVenta().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        colFechaDev.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getFechaLimiteDevolucion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

        tabla.getColumns().addAll(colId, colNombre, colCantidad, colTipo, colPrecio, colTotal, colFechaVta, colFechaDev);
        tabla.setItems(listaConjuntosVendidos);
        VBox.setVgrow(tabla, Priority.ALWAYS);

        Button btnDetalle = new Button("🔍 Ver Detalle");
        btnDetalle.setStyle("-fx-background-color: " + PRINCIPAL + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
        btnDetalle.setOnAction(e -> mostrarDetalleConjuntoVendido(contenido, esAdmin));

        VBox vista = new VBox(16, titulo, tabla);
        vista.setStyle("-fx-padding: 30;");
        VBox.setVgrow(tabla, Priority.ALWAYS);
        VBox.setVgrow(vista, Priority.ALWAYS);

        if (esAdmin) {
            Button btnAnadir = new Button("+ Registrar Venta");
            Button btnEditar = new Button("✎ Editar Registro");
            btnAnadir.setStyle("-fx-background-color: " + NARANJA + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
            btnEditar.setStyle("-fx-background-color: " + AZUL_EDITAR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
            btnAnadir.setOnAction(e -> mostrarFormularioNuevoConjuntoVendido(contenido));
            btnEditar.setOnAction(e -> mostrarFormularioEditarConjuntoVendido(contenido));
            HBox botones = new HBox(12, btnAnadir, btnEditar, btnDetalle);
            vista.getChildren().add(botones);
        } else {
            HBox botones = new HBox(12, btnDetalle);
            vista.getChildren().add(botones);
        }

        contenido.getChildren().add(vista);
    }

    // ── DETALLE CONJUNTO VENDIDO ─────────────────────────────────────
    private void mostrarDetalleConjuntoVendido(StackPane contenido, boolean esAdmin) {
        contenido.getChildren().clear();

        Label titulo    = new Label("Detalle de Conjunto Vendido");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        Label subtitulo = new Label("Busca por ID de venta o nombre del conjunto");
        subtitulo.setFont(Font.font("System", 12));
        subtitulo.setTextFill(Color.web(TEXTO_SUAVE));

        TextField campoBusqueda = crearTextField("ID de venta o nombre del conjunto");
        Button btnBuscar = new Button("Buscar");
        btnBuscar.setStyle(estiloBtnPrincipal());

        Label mensajeBusqueda = new Label("");
        mensajeBusqueda.setFont(Font.font("System", 12));

        VBox tarjeta = new VBox(12);
        tarjeta.setStyle("-fx-background-color: " + PANEL + "; -fx-border-color: " + PRINCIPAL_ALT + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 24; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3);");
        tarjeta.setVisible(false);
        tarjeta.setManaged(false);
        tarjeta.setMaxWidth(480);

        btnBuscar.setOnAction(e -> {
            String busqueda = campoBusqueda.getText().trim();
            if (busqueda.isEmpty()) { mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("Ingresa un ID o nombre"); return; }
            ConjuntoVendido cv = listaConjuntosVendidos.stream()
                .filter(x -> x.getIdVenta().equalsIgnoreCase(busqueda) || x.getNombreConjunto().equalsIgnoreCase(busqueda))
                .findFirst().orElse(null);
            if (cv == null) {
                mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("No se encontró el registro");
                tarjeta.setVisible(false); tarjeta.setManaged(false);
            } else {
                mensajeBusqueda.setTextFill(Color.web(EXITO)); mensajeBusqueda.setText("Registro encontrado");
                tarjeta.getChildren().clear();
                Label lNombre = new Label(cv.getNombreConjunto());
                lNombre.setFont(Font.font("System", FontWeight.BOLD, 17));
                lNombre.setTextFill(Color.web(SECUNDARIO));
                Region sep = new Region(); sep.setPrefHeight(1); sep.setStyle("-fx-background-color: #E5E7EB;");

                boolean dentroDevolucion = !LocalDate.now().isAfter(cv.getFechaLimiteDevolucion());
                Label lEstado = new Label(dentroDevolucion ? "✔ Dentro del periodo de devolución" : "✕ Periodo de devolución vencido");
                lEstado.setFont(Font.font("System", FontWeight.BOLD, 12));
                lEstado.setTextFill(Color.web(dentroDevolucion ? EXITO : ERROR));

                Label labelPrendasTitulo = new Label("Prendas del conjunto:");
                labelPrendasTitulo.setFont(Font.font("System", FontWeight.BOLD, 12));
                labelPrendasTitulo.setTextFill(Color.web(TEXTO_SUAVE));
                labelPrendasTitulo.setMinWidth(140);

                VBox listaPrendasBox = new VBox(4);
                for (String nombreP : cv.getNombresPrendas()) {
                    Label lP = new Label("• " + nombreP);
                    lP.setFont(Font.font("System", 13));
                    lP.setTextFill(Color.web(TEXTO));
                    listaPrendasBox.getChildren().add(lP);
                }
                HBox filaPrendas = new HBox(8, labelPrendasTitulo, listaPrendasBox);
                filaPrendas.setAlignment(Pos.TOP_LEFT);

                tarjeta.getChildren().addAll(lNombre, sep,
                    filaDetalle("ID Venta:",         cv.getIdVenta()),
                    filaDetalle("Cantidad:",          String.valueOf(cv.getCantidad())),
                    filaDetalle("Tipo de venta:",     cv.getTipoVenta()),
                    filaDetalle("Precio unitario:",   "$" + String.format("%.2f", cv.getPrecioUnitario())),
                    filaDetalle("Total:",             "$" + String.format("%.2f", cv.getTotal())),
                    filaDetalle("Fecha de venta:",    cv.getFechaVenta().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))),
                    filaDetalle("Límite devolución:", cv.getFechaLimiteDevolucion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))),
                    filaDetalle("Descripción:",       cv.getDescripcion()),
                    filaPrendas, lEstado);
                tarjeta.setVisible(true); tarjeta.setManaged(true);
            }
        });

        Button btnRegresar = new Button("← Regresar a lista");
        btnRegresar.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXTO_SUAVE + "; -fx-font-size: 12px; -fx-cursor: hand;");
        btnRegresar.setOnAction(e -> mostrarModuloConjuntosVendidos(contenido, esAdmin));

        VBox form = new VBox(12, titulo, subtitulo, campoBusqueda, btnBuscar, mensajeBusqueda, tarjeta, btnRegresar);
        form.setAlignment(Pos.TOP_LEFT); form.setMaxWidth(520);
        form.setStyle("-fx-background-color: " + PANEL + "; -fx-padding: 35; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3);");

        ScrollPane scroll = new ScrollPane(form);
        scroll.setFitToWidth(false);
        scroll.setFitToHeight(false);
        scroll.setStyle("-fx-background-color: " + FONDO + "; -fx-background: " + FONDO + ";");

        StackPane wrapper = new StackPane(scroll);
        wrapper.setStyle("-fx-background-color: " + FONDO + "; -fx-padding: 30;");
        wrapper.setAlignment(Pos.CENTER);
        contenido.getChildren().add(wrapper);
    }

    // ── FORMULARIO NUEVO CONJUNTO VENDIDO ────────────────────────────
    private void mostrarFormularioNuevoConjuntoVendido(StackPane contenido) {
        contenido.getChildren().clear();

        Label titulo    = new Label("Registrar Conjunto Vendido");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        Label subtitulo = new Label("Completa los campos para registrar la venta");
        subtitulo.setFont(Font.font("System", 12));
        subtitulo.setTextFill(Color.web(TEXTO_SUAVE));

        TextField campoIdVenta        = crearTextField("ID de venta");
        TextField campoNombreConjunto = crearTextField("Nombre del conjunto");
        TextField campoCantidad       = crearTextField("Cantidad");
        TextField campoPrecioUnit     = crearTextField("Precio unitario");
        TextField campoDescripcion    = crearTextField("Descripción");
        TextField campoNombresPrendas = crearTextField("Nombres de prendas separados por coma");
        campoNombresPrendas.setMaxWidth(400);

        Label labelTipoVenta = new Label("Tipo de venta:");
        labelTipoVenta.setTextFill(Color.web(TEXTO_SUAVE));
        labelTipoVenta.setFont(Font.font("System", 12));

        ComboBox<String> selectorTipo = new ComboBox<>();
        selectorTipo.getItems().addAll("Menudeo", "Mayoreo");
        selectorTipo.setValue("Menudeo");
        selectorTipo.setMaxWidth(320);
        selectorTipo.setStyle(estiloInput());

        Label labelFechaVenta = new Label("Fecha de venta (dd/MM/yyyy):");
        labelFechaVenta.setTextFill(Color.web(TEXTO_SUAVE));
        labelFechaVenta.setFont(Font.font("System", 12));

        TextField campoFechaVenta = crearTextField("dd/MM/yyyy  (vacío = hoy)");

        Label mensajeEstado = new Label("");
        mensajeEstado.setFont(Font.font("System", 12));

        Button btnGuardar = new Button("Guardar Registro");
        btnGuardar.setMaxWidth(320);
        btnGuardar.setStyle("-fx-background-color: " + NARANJA + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 6; -fx-cursor: hand;");

        Button btnCancelar = new Button("← Regresar a lista");
        btnCancelar.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXTO_SUAVE + "; -fx-font-size: 12px; -fx-cursor: hand;");
        btnCancelar.setOnAction(e -> mostrarModuloConjuntosVendidos(contenido, true));

        btnGuardar.setOnAction(e -> {
            String idVenta        = campoIdVenta.getText().trim();
            String nombreConjunto = campoNombreConjunto.getText().trim();
            String cantStr        = campoCantidad.getText().trim();
            String precioStr      = campoPrecioUnit.getText().trim();
            String descripcion    = campoDescripcion.getText().trim();
            String tipoVenta      = selectorTipo.getValue();
            String fechaStr       = campoFechaVenta.getText().trim();
            String prendasStr     = campoNombresPrendas.getText().trim();

            if (idVenta.isEmpty() || nombreConjunto.isEmpty() || cantStr.isEmpty() || precioStr.isEmpty()) {
                mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("Todos los campos obligatorios deben llenarse"); return;
            }
            boolean yaExiste = listaConjuntosVendidos.stream().anyMatch(cv -> cv.getIdVenta().equals(idVenta));
            if (yaExiste) { mensajeEstado.setTextFill(Color.web(ADVERTENCIA)); mensajeEstado.setText("Ya existe un registro con ese ID de venta"); return; }

            try {
                int cantidad      = Integer.parseInt(cantStr);
                double precioUnit = Double.parseDouble(precioStr);
                LocalDate fechaVenta;
                if (fechaStr.isEmpty()) {
                    fechaVenta = LocalDate.now();
                } else {
                    fechaVenta = LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                }
                LocalDate fechaDev = fechaVenta.plusDays(DIAS_DEVOLUCION);
                List<String> nombresPrendas = new ArrayList<>();
                if (!prendasStr.isEmpty()) {
                    for (String s : prendasStr.split(",")) nombresPrendas.add(s.trim());
                }
                listaConjuntosVendidos.add(new ConjuntoVendido(idVenta, nombreConjunto, cantidad, tipoVenta, precioUnit, fechaVenta, fechaDev, descripcion, nombresPrendas));
                mostrarModuloConjuntosVendidos(contenido, true);
            } catch (Exception ex) {
                mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("Verifica que los datos sean válidos");
            }
        });

        VBox form = new VBox(12, titulo, subtitulo, campoIdVenta, campoNombreConjunto,
                campoCantidad, campoPrecioUnit, labelTipoVenta, selectorTipo,
                labelFechaVenta, campoFechaVenta, campoNombresPrendas,
                campoDescripcion, mensajeEstado, btnGuardar, btnCancelar);
        form.setAlignment(Pos.TOP_LEFT); form.setMaxWidth(440);
        form.setStyle("-fx-background-color: " + PANEL + "; -fx-padding: 35; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3);");

        ScrollPane scroll = new ScrollPane(form);
        scroll.setFitToWidth(false);
        scroll.setFitToHeight(false);
        scroll.setStyle("-fx-background-color: " + FONDO + "; -fx-background: " + FONDO + ";");

        StackPane wrapper = new StackPane(scroll);
        wrapper.setStyle("-fx-background-color: " + FONDO + "; -fx-padding: 30;");
        wrapper.setAlignment(Pos.CENTER);
        contenido.getChildren().add(wrapper);
    }

    // ── FORMULARIO EDITAR CONJUNTO VENDIDO ───────────────────────────
    private void mostrarFormularioEditarConjuntoVendido(StackPane contenido) {
        contenido.getChildren().clear();

        Label titulo    = new Label("Editar Registro de Conjunto Vendido");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        Label subtitulo = new Label("Busca por ID de venta o nombre del conjunto");
        subtitulo.setFont(Font.font("System", 12));
        subtitulo.setTextFill(Color.web(TEXTO_SUAVE));

        TextField campoBusqueda = crearTextField("ID de venta o nombre del conjunto");
        Button btnBuscar = new Button("Buscar");
        btnBuscar.setStyle(estiloBtnPrincipal());

        Label mensajeBusqueda = new Label("");
        mensajeBusqueda.setFont(Font.font("System", 12));

        VBox panelEdicion = new VBox(10);
        panelEdicion.setVisible(false);
        panelEdicion.setManaged(false);

        TextField campoNombreConjunto = crearTextField("Nombre del conjunto");
        TextField campoCantidad       = crearTextField("Cantidad");
        TextField campoPrecioUnit     = crearTextField("Precio unitario");
        TextField campoDescripcion    = crearTextField("Descripción");
        TextField campoFechaVenta     = crearTextField("Fecha venta dd/MM/yyyy");
        TextField campoNombresPrendas = crearTextField("Nombres de prendas separados por coma");
        campoNombresPrendas.setMaxWidth(400);

        Label labelTipoVenta = new Label("Tipo de venta:");
        labelTipoVenta.setTextFill(Color.web(TEXTO_SUAVE));
        labelTipoVenta.setFont(Font.font("System", 12));

        ComboBox<String> selectorTipo = new ComboBox<>();
        selectorTipo.getItems().addAll("Menudeo", "Mayoreo");
        selectorTipo.setValue("Menudeo");
        selectorTipo.setMaxWidth(320);
        selectorTipo.setStyle(estiloInput());

        Label mensajeEstado = new Label("");
        mensajeEstado.setFont(Font.font("System", 12));

        Button btnGuardar = new Button("Guardar Cambios");
        btnGuardar.setMaxWidth(320);
        btnGuardar.setStyle("-fx-background-color: " + AZUL_EDITAR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 6; -fx-cursor: hand;");

        panelEdicion.getChildren().addAll(campoNombreConjunto, campoCantidad, campoPrecioUnit,
                labelTipoVenta, selectorTipo, campoFechaVenta, campoNombresPrendas,
                campoDescripcion, mensajeEstado, btnGuardar);

        final ConjuntoVendido[] cvEncontrado = {null};

        btnBuscar.setOnAction(e -> {
            String busqueda = campoBusqueda.getText().trim();
            if (busqueda.isEmpty()) { mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("Ingresa un ID o nombre"); return; }
            ConjuntoVendido cv = listaConjuntosVendidos.stream()
                .filter(x -> x.getIdVenta().equalsIgnoreCase(busqueda) || x.getNombreConjunto().equalsIgnoreCase(busqueda))
                .findFirst().orElse(null);
            if (cv == null) {
                mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("No se encontró el registro");
                panelEdicion.setVisible(false); panelEdicion.setManaged(false);
            } else {
                cvEncontrado[0] = cv;
                mensajeBusqueda.setTextFill(Color.web(EXITO)); mensajeBusqueda.setText("Registro encontrado");
                campoNombreConjunto.setText(cv.getNombreConjunto());
                campoCantidad.setText(String.valueOf(cv.getCantidad()));
                campoPrecioUnit.setText(String.valueOf(cv.getPrecioUnitario()));
                selectorTipo.setValue(cv.getTipoVenta());
                campoFechaVenta.setText(cv.getFechaVenta().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                campoNombresPrendas.setText(String.join(", ", cv.getNombresPrendas()));
                campoDescripcion.setText(cv.getDescripcion());
                mensajeEstado.setText("");
                panelEdicion.setVisible(true); panelEdicion.setManaged(true);
            }
        });

        btnGuardar.setOnAction(e -> {
            if (cvEncontrado[0] == null) return;
            String nombreConj  = campoNombreConjunto.getText().trim();
            String cantStr     = campoCantidad.getText().trim();
            String precioStr   = campoPrecioUnit.getText().trim();
            String fechaStr    = campoFechaVenta.getText().trim();
            String descripcion = campoDescripcion.getText().trim();
            String tipoVenta   = selectorTipo.getValue();
            String prendasStr  = campoNombresPrendas.getText().trim();

            if (nombreConj.isEmpty() || cantStr.isEmpty() || precioStr.isEmpty() || fechaStr.isEmpty()) {
                mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("Todos los campos son obligatorios"); return;
            }
            try {
                int cantidad      = Integer.parseInt(cantStr);
                double precioUnit = Double.parseDouble(precioStr);
                LocalDate fechaVenta = LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                LocalDate fechaDev   = fechaVenta.plusDays(DIAS_DEVOLUCION);
                List<String> nombresPrendas = new ArrayList<>();
                if (!prendasStr.isEmpty()) {
                    for (String s : prendasStr.split(",")) nombresPrendas.add(s.trim());
                }
                ConjuntoVendido cv = cvEncontrado[0];
                cv.setNombreConjunto(nombreConj); cv.setCantidad(cantidad);
                cv.setPrecioUnitario(precioUnit); cv.setTipoVenta(tipoVenta);
                cv.setFechaVenta(fechaVenta); cv.setFechaLimiteDevolucion(fechaDev);
                cv.setDescripcion(descripcion); cv.setNombresPrendas(nombresPrendas);
                mensajeEstado.setTextFill(Color.web(EXITO)); mensajeEstado.setText("Registro actualizado correctamente");
            } catch (Exception ex) {
                mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("Verifica que los datos sean válidos");
            }
        });

        Button btnCancelar = new Button("← Regresar a lista");
        btnCancelar.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXTO_SUAVE + "; -fx-font-size: 12px; -fx-cursor: hand;");
        btnCancelar.setOnAction(e -> mostrarModuloConjuntosVendidos(contenido, true));

        VBox form = new VBox(12, titulo, subtitulo, campoBusqueda, btnBuscar, mensajeBusqueda, panelEdicion, btnCancelar);
        form.setAlignment(Pos.TOP_LEFT); form.setMaxWidth(460);
        form.setStyle("-fx-background-color: " + PANEL + "; -fx-padding: 35; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3);");

        ScrollPane scroll = new ScrollPane(form);
        scroll.setFitToWidth(false);
        scroll.setFitToHeight(false);
        scroll.setStyle("-fx-background-color: " + FONDO + "; -fx-background: " + FONDO + ";");

        StackPane wrapper = new StackPane(scroll);
        wrapper.setStyle("-fx-background-color: " + FONDO + "; -fx-padding: 30;");
        wrapper.setAlignment(Pos.CENTER);
        contenido.getChildren().add(wrapper);
    }

    // ── FORMULARIO NUEVO CONJUNTO ────────────────────────────────────
    private void mostrarFormularioNuevoConjunto(StackPane contenido) {
        contenido.getChildren().clear();

        Label titulo = new Label("Nuevo Conjunto");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        TextField campoId          = crearTextField("ID del conjunto");
        TextField campoNombre      = crearTextField("Nombre del conjunto");
        TextField campoDescripcion = crearTextField("Descripción");
        TextField campoPiezas      = crearTextField("Número de piezas");
        TextField campoMinimo      = crearTextField("Mínimo de existencia  (ej: 3)");
        TextField campoIdPrendas   = crearTextField("IDs de prendas separados por coma (ej: 1,7)");
        campoIdPrendas.setMaxWidth(400);

        Label mensajeEstado = new Label("");
        mensajeEstado.setFont(Font.font("System", 12));

        Button btnGuardar = new Button("Guardar Conjunto");
        btnGuardar.setMaxWidth(320);
        btnGuardar.setStyle("-fx-background-color: " + NARANJA + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 6; -fx-cursor: hand;");

        Button btnCancelar = new Button("← Regresar a lista");
        btnCancelar.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXTO_SUAVE + "; -fx-font-size: 12px; -fx-cursor: hand;");
        btnCancelar.setOnAction(e -> mostrarModuloConjuntos(contenido, true));

        btnGuardar.setOnAction(e -> {
            String id           = campoId.getText().trim();
            String nombre       = campoNombre.getText().trim();
            String descripcion  = campoDescripcion.getText().trim();
            String piezasStr    = campoPiezas.getText().trim();
            String idPrendasStr = campoIdPrendas.getText().trim();

            if (id.isEmpty() || nombre.isEmpty() || piezasStr.isEmpty() || idPrendasStr.isEmpty()) {
                mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("Todos los campos son obligatorios"); return;
            }
            boolean yaExiste = listaConjuntos.stream().anyMatch(c -> c.getId().equals(id));
            if (yaExiste) { mensajeEstado.setTextFill(Color.web(ADVERTENCIA)); mensajeEstado.setText("Ya existe un conjunto con ese ID"); return; }

            try {
                int piezas = Integer.parseInt(piezasStr);
                List<String> ids = new ArrayList<>();
                for (String s : idPrendasStr.split(",")) {
                    String idLimpio = s.trim();
                    if (!listaPrendas.stream().anyMatch(p -> p.getId().equals(idLimpio))) {
                        mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("No se encontró la prenda con ID: " + idLimpio); return;
                    }
                    ids.add(idLimpio);
                }
                int minimo = campoMinimo.getText().trim().isEmpty() ? 3 : Integer.parseInt(campoMinimo.getText().trim());
                Conjunto nuevoConj = new Conjunto(id, nombre, descripcion, ids, piezas);
                nuevoConj.setMinimoExistencia(minimo);
                listaConjuntos.add(nuevoConj);                mostrarModuloConjuntos(contenido, true);
            } catch (NumberFormatException ex) {
                mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("El número de piezas debe ser un número válido");
            }
        });

        VBox form = new VBox(12, titulo, campoId, campoNombre, campoDescripcion,
                campoPiezas, campoMinimo, campoIdPrendas, mensajeEstado, btnGuardar, btnCancelar);
        form.setAlignment(Pos.TOP_LEFT); form.setMaxWidth(450);
        form.setStyle("-fx-background-color: " + PANEL + "; -fx-padding: 35; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3);");

        ScrollPane scroll = new ScrollPane(form);
        scroll.setFitToWidth(false);
        scroll.setFitToHeight(false);
        scroll.setStyle("-fx-background-color: " + FONDO + "; -fx-background: " + FONDO + ";");

        StackPane wrapper = new StackPane(scroll);
        wrapper.setStyle("-fx-background-color: " + FONDO + "; -fx-padding: 30;");
        wrapper.setAlignment(Pos.CENTER);
        contenido.getChildren().add(wrapper);
    }

    // ── FORMULARIO EDITAR CONJUNTO ───────────────────────────────────
    private void mostrarFormularioEditarConjunto(StackPane contenido) {
        contenido.getChildren().clear();

        Label titulo    = new Label("Editar Conjunto");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        Label subtitulo = new Label("Busca por nombre o ID y modifica los campos");
        subtitulo.setFont(Font.font("System", 12));
        subtitulo.setTextFill(Color.web(TEXTO_SUAVE));

        TextField campoBusqueda = crearTextField("Nombre o ID del conjunto");
        Button btnBuscar = new Button("Buscar");
        btnBuscar.setStyle(estiloBtnPrincipal());

        Label mensajeBusqueda = new Label("");
        mensajeBusqueda.setFont(Font.font("System", 12));

        VBox panelEdicion = new VBox(10);
        panelEdicion.setVisible(false);
        panelEdicion.setManaged(false);

        TextField campoNombre      = crearTextField("Nombre del conjunto");
        TextField campoDescripcion = crearTextField("Descripción");
        TextField campoPiezas      = crearTextField("Número de piezas");
        TextField campoMinimo      = crearTextField("Mínimo de existencia");
        TextField campoIdPrendas   = crearTextField("IDs de prendas separados por coma");
        campoIdPrendas.setMaxWidth(400);

        Label mensajeEstado = new Label("");
        mensajeEstado.setFont(Font.font("System", 12));

        Button btnGuardar = new Button("Guardar Cambios");
        btnGuardar.setMaxWidth(320);
        btnGuardar.setStyle("-fx-background-color: " + AZUL_EDITAR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 6; -fx-cursor: hand;");

panelEdicion.getChildren().addAll(campoNombre, campoDescripcion, campoPiezas, campoMinimo, campoIdPrendas, mensajeEstado, btnGuardar);
        final Conjunto[] conjuntoEncontrado = {null};

        btnBuscar.setOnAction(e -> {
            String busqueda = campoBusqueda.getText().trim();
            if (busqueda.isEmpty()) { mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("Ingresa un nombre o ID"); return; }
            Conjunto c = listaConjuntos.stream()
                .filter(x -> x.getNombre().equalsIgnoreCase(busqueda) || x.getId().equals(busqueda))
                .findFirst().orElse(null);
            if (c == null) {
                mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("No se encontró el conjunto");
                panelEdicion.setVisible(false); panelEdicion.setManaged(false);
            } else {
                conjuntoEncontrado[0] = c;
                mensajeBusqueda.setTextFill(Color.web(EXITO)); mensajeBusqueda.setText("Conjunto encontrado");
campoNombre.setText(c.getNombre()); campoDescripcion.setText(c.getDescripcion());
                campoPiezas.setText(String.valueOf(c.getPiezas())); campoMinimo.setText(String.valueOf(c.getMinimoExistencia())); campoIdPrendas.setText(String.join(",", c.getIdPrendas()));
                mensajeEstado.setText(""); panelEdicion.setVisible(true); panelEdicion.setManaged(true);
            }
        });

        btnGuardar.setOnAction(e -> {
            if (conjuntoEncontrado[0] == null) return;
            String nombre       = campoNombre.getText().trim();
            String descripcion  = campoDescripcion.getText().trim();
            String piezasStr    = campoPiezas.getText().trim();
            String idPrendasStr = campoIdPrendas.getText().trim();
            if (nombre.isEmpty() || piezasStr.isEmpty() || idPrendasStr.isEmpty()) {
                mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("Todos los campos son obligatorios"); return;
            }
            try {
                int piezas = Integer.parseInt(piezasStr);
                List<String> ids = new ArrayList<>();
                for (String s : idPrendasStr.split(",")) {
                    String idLimpio = s.trim();
                    if (!listaPrendas.stream().anyMatch(p -> p.getId().equals(idLimpio))) {
                        mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("No se encontró la prenda con ID: " + idLimpio); return;
                    }
                    ids.add(idLimpio);
                }
                int minimo = campoMinimo.getText().trim().isEmpty() ? 3 : Integer.parseInt(campoMinimo.getText().trim());
                conjuntoEncontrado[0].setNombre(nombre); conjuntoEncontrado[0].setDescripcion(descripcion);
                conjuntoEncontrado[0].setPiezas(piezas); conjuntoEncontrado[0].setIdPrendas(ids);
                conjuntoEncontrado[0].setMinimoExistencia(minimo);
                mensajeEstado.setTextFill(Color.web(EXITO)); mensajeEstado.setText("Conjunto actualizado correctamente");
            } catch (NumberFormatException ex) {
                mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("El número de piezas debe ser un número válido");
            }
        });

        Button btnCancelar = new Button("← Regresar a lista");
        btnCancelar.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXTO_SUAVE + "; -fx-font-size: 12px; -fx-cursor: hand;");
        btnCancelar.setOnAction(e -> mostrarModuloConjuntos(contenido, true));

        VBox form = new VBox(12, titulo, subtitulo, campoBusqueda, btnBuscar, mensajeBusqueda, panelEdicion, btnCancelar);
        form.setAlignment(Pos.TOP_LEFT); form.setMaxWidth(460);
        form.setStyle("-fx-background-color: " + PANEL + "; -fx-padding: 35; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3);");

        ScrollPane scroll = new ScrollPane(form);
        scroll.setFitToWidth(false);
        scroll.setFitToHeight(false);
        scroll.setStyle("-fx-background-color: " + FONDO + "; -fx-background: " + FONDO + ";");

        StackPane wrapper = new StackPane(scroll);
        wrapper.setStyle("-fx-background-color: " + FONDO + "; -fx-padding: 30;");
        wrapper.setAlignment(Pos.CENTER);
        contenido.getChildren().add(wrapper);
    }

    // ── FORMULARIO NUEVA PRENDA ──────────────────────────────────────
    private void mostrarFormularioNuevaPrenda(StackPane contenido) {
        contenido.getChildren().clear();

        Label titulo    = new Label("Nueva Prenda");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        Label subtitulo = new Label("Completa los campos para registrar la prenda");
        subtitulo.setFont(Font.font("System", 12));
        subtitulo.setTextFill(Color.web(TEXTO_SUAVE));

        TextField campoNombre      = crearTextField("Nombre de la prenda");
        TextField campoId          = crearTextField("ID");
        TextField campoTipoPrenda  = crearTextField("Tipo de prenda");
        TextField campoDescripcion = crearTextField("Descripción");
        TextField campoExistencia  = crearTextField("Existencia inicial");
        TextField campoMinimo      = crearTextField("Mínimo de existencia  (ej: 5)");
        TextField campoPMayoreo    = crearTextField("Precio mayoreo");
        TextField campoPMenudeo    = crearTextField("Precio menudeo");
        TextField campoIdTienda    = crearTextField("ID Tienda (pendiente)");

        Label labelTalla = new Label("Talla:");
        labelTalla.setTextFill(Color.web(TEXTO_SUAVE));
        labelTalla.setFont(Font.font("System", 12));

        ComboBox<String> selectorTalla = new ComboBox<>();
        selectorTalla.getItems().addAll("Ch", "M", "G", "XL", "XXL");
        selectorTalla.setValue("M");
        selectorTalla.setMaxWidth(320);
        selectorTalla.setStyle(estiloInput());

        Label mensajeEstado = new Label("");
        mensajeEstado.setFont(Font.font("System", 12));

        Button btnGuardar = new Button("Guardar Prenda");
        btnGuardar.setMaxWidth(320);
        btnGuardar.setStyle("-fx-background-color: " + NARANJA + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 6; -fx-cursor: hand;");

        Button btnCancelar = new Button("← Regresar a lista");
        btnCancelar.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXTO_SUAVE + "; -fx-font-size: 12px; -fx-cursor: hand;");
        btnCancelar.setOnAction(e -> mostrarModuloPrendas(contenido, true));

        btnGuardar.setOnAction(e -> {
            String nombre      = campoNombre.getText().trim();
            String id          = campoId.getText().trim();
            String talla       = selectorTalla.getValue();
            String tipoPrenda  = campoTipoPrenda.getText().trim();
            String descripcion = campoDescripcion.getText().trim();
            String idTienda    = campoIdTienda.getText().trim();

            if (nombre.isEmpty() || id.isEmpty() || campoExistencia.getText().isEmpty()
                    || campoPMayoreo.getText().isEmpty() || campoPMenudeo.getText().isEmpty()) {
                mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("Todos los campos son obligatorios"); return;
            }
            if (listaPrendas.stream().anyMatch(p -> p.getNombre().equalsIgnoreCase(nombre) && p.getTalla().equalsIgnoreCase(talla))) {
                mensajeEstado.setTextFill(Color.web(ADVERTENCIA)); mensajeEstado.setText("Ya existe esa prenda con esa talla. Usa 'Añadir a Existente'."); return;
            }
            try {
                int existencia  = Integer.parseInt(campoExistencia.getText().trim());
                int minimo      = campoMinimo.getText().trim().isEmpty() ? 5 : Integer.parseInt(campoMinimo.getText().trim());
                double pMayoreo = Double.parseDouble(campoPMayoreo.getText().trim());
                double pMenudeo = Double.parseDouble(campoPMenudeo.getText().trim());
                Prenda nuevaPrenda = new Prenda(nombre, id, talla, tipoPrenda, existencia, pMayoreo, pMenudeo, idTienda, descripcion);
                nuevaPrenda.setMinimoExistencia(minimo);
                listaPrendas.add(nuevaPrenda);                mostrarModuloPrendas(contenido, true);
            } catch (NumberFormatException ex) {
                mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("Existencia y precios deben ser números válidos");
            }
        });

        VBox form = new VBox(10, titulo, subtitulo, campoNombre, campoId, campoTipoPrenda,
                campoDescripcion, labelTalla, selectorTalla, campoExistencia, campoMinimo , 
                campoPMayoreo, campoPMenudeo, campoIdTienda, mensajeEstado, btnGuardar, btnCancelar);
        form.setAlignment(Pos.TOP_LEFT); form.setMaxWidth(400);
        form.setStyle("-fx-background-color: " + PANEL + "; -fx-padding: 35; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3);");

        ScrollPane scroll = new ScrollPane(form);
        scroll.setFitToWidth(false);
        scroll.setFitToHeight(false);
        scroll.setStyle("-fx-background-color: " + FONDO + "; -fx-background: " + FONDO + ";");

        StackPane wrapper = new StackPane(scroll);
        wrapper.setStyle("-fx-background-color: " + FONDO + "; -fx-padding: 30;");
        wrapper.setAlignment(Pos.CENTER);
        contenido.getChildren().add(wrapper);
    }

    // ── FORMULARIO AÑADIR A EXISTENTE ────────────────────────────────
    private void mostrarFormularioAnadirExistente(StackPane contenido) {
        contenido.getChildren().clear();

        Label titulo    = new Label("Añadir a Existente");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        Label subtitulo = new Label("Busca por nombre o ID y añade unidades a la existencia");
        subtitulo.setFont(Font.font("System", 12));
        subtitulo.setTextFill(Color.web(TEXTO_SUAVE));

        TextField campoBusqueda = crearTextField("Nombre o ID de la prenda");
        Label mensajeBusqueda   = new Label("");
        mensajeBusqueda.setFont(Font.font("System", 12));

        VBox panelResultado = new VBox(8);
        panelResultado.setStyle("-fx-background-color: #F0FDF4; -fx-border-color: " + EXITO + "; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 12;");
        panelResultado.setVisible(false);
        panelResultado.setManaged(false);

        Label labelResultado    = new Label("");
        TextField campoCantidad = crearTextField("Cantidad a añadir");
        Label mensajeEstado     = new Label("");
        mensajeEstado.setFont(Font.font("System", 12));

        Button btnAnadir = new Button("Añadir Unidades");
        btnAnadir.setMaxWidth(320);
        btnAnadir.setStyle("-fx-background-color: " + CAFE + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 6; -fx-cursor: hand;");
        panelResultado.getChildren().addAll(labelResultado, campoCantidad, mensajeEstado, btnAnadir);

        final Prenda[] prendaEncontrada = {null};

        Button btnBuscar = new Button("Buscar");
        btnBuscar.setStyle(estiloBtnPrincipal());
        btnBuscar.setOnAction(e -> {
            String busqueda = campoBusqueda.getText().trim();
            if (busqueda.isEmpty()) { mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("Ingresa un nombre o ID"); return; }
            Prenda encontrada = listaPrendas.stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(busqueda) || p.getId().equals(busqueda))
                .findFirst().orElse(null);
            if (encontrada == null) {
                mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("No se encontró ninguna prenda");
                panelResultado.setVisible(false); panelResultado.setManaged(false);
            } else {
                prendaEncontrada[0] = encontrada;
                mensajeBusqueda.setTextFill(Color.web(EXITO)); mensajeBusqueda.setText("Prenda encontrada");
                labelResultado.setText("Prenda: " + encontrada.getNombre() + " | Talla: " + encontrada.getTalla() + " | Existencia: " + encontrada.getExistencia());
                labelResultado.setTextFill(Color.web(TEXTO));
                panelResultado.setVisible(true); panelResultado.setManaged(true);
                mensajeEstado.setText(""); campoCantidad.clear();
            }
        });

        btnAnadir.setOnAction(e -> {
            if (prendaEncontrada[0] == null) return;
            try {
                int cantidad = Integer.parseInt(campoCantidad.getText().trim());
                if (cantidad <= 0) { mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("La cantidad debe ser mayor a 0"); return; }
                prendaEncontrada[0].setExistencia(prendaEncontrada[0].getExistencia() + cantidad);
                mensajeEstado.setTextFill(Color.web(EXITO));
                mensajeEstado.setText("Se añadieron " + cantidad + " unidades. Nueva existencia: " + prendaEncontrada[0].getExistencia());
                labelResultado.setText("Prenda: " + prendaEncontrada[0].getNombre() + " | Talla: " + prendaEncontrada[0].getTalla() + " | Existencia: " + prendaEncontrada[0].getExistencia());
                campoCantidad.clear();
            } catch (NumberFormatException ex) { mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("Ingresa un número válido"); }
        });

        Button btnCancelar = new Button("← Regresar a lista");
        btnCancelar.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXTO_SUAVE + "; -fx-font-size: 12px; -fx-cursor: hand;");
        btnCancelar.setOnAction(e -> mostrarModuloPrendas(contenido, true));

        VBox form = new VBox(12, titulo, subtitulo, campoBusqueda, btnBuscar, mensajeBusqueda, panelResultado, btnCancelar);
        form.setAlignment(Pos.TOP_LEFT); form.setMaxWidth(420);
        form.setStyle("-fx-background-color: " + PANEL + "; -fx-padding: 35; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3);");

        ScrollPane scroll = new ScrollPane(form);
        scroll.setFitToWidth(false);
        scroll.setFitToHeight(false);
        scroll.setStyle("-fx-background-color: " + FONDO + "; -fx-background: " + FONDO + ";");

        StackPane wrapper = new StackPane(scroll);
        wrapper.setStyle("-fx-background-color: " + FONDO + "; -fx-padding: 30;");
        wrapper.setAlignment(Pos.CENTER);
        contenido.getChildren().add(wrapper);
    }

    // ── FORMULARIO EDITAR PRENDA ─────────────────────────────────────
    private void mostrarFormularioEditarPrenda(StackPane contenido) {
        contenido.getChildren().clear();

        Label titulo    = new Label("Editar Prenda");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        Label subtitulo = new Label("Busca por nombre o ID y modifica los campos");
        subtitulo.setFont(Font.font("System", 12));
        subtitulo.setTextFill(Color.web(TEXTO_SUAVE));

        TextField campoBusqueda  = crearTextField("Nombre o ID de la prenda");
        Label mensajeBusqueda    = new Label("");
        mensajeBusqueda.setFont(Font.font("System", 12));

        VBox panelEdicion = new VBox(10);
        panelEdicion.setVisible(false);
        panelEdicion.setManaged(false);

        TextField campoNombre      = crearTextField("Nombre");
        TextField campoPMayoreo    = crearTextField("Precio mayoreo");
        TextField campoPMenudeo    = crearTextField("Precio menudeo");
        TextField campoExistencia  = crearTextField("Existencia");
        TextField campoMinimo      = crearTextField("Mínimo de existencia");
        TextField campoTipoPrenda  = crearTextField("Tipo de prenda");
        TextField campoDescripcion = crearTextField("Descripción");
        TextField campoIdTienda    = crearTextField("ID Tienda");

        Label labelTalla = new Label("Talla:");
        labelTalla.setTextFill(Color.web(TEXTO_SUAVE));
        labelTalla.setFont(Font.font("System", 12));

        ComboBox<String> selectorTalla = new ComboBox<>();
        selectorTalla.getItems().addAll("Ch", "M", "G", "XL", "XXL");
        selectorTalla.setMaxWidth(320);
        selectorTalla.setStyle(estiloInput());

        Label mensajeEstado = new Label("");
        mensajeEstado.setFont(Font.font("System", 12));

        Button btnGuardar = new Button("Guardar Cambios");
        btnGuardar.setMaxWidth(320);
        btnGuardar.setStyle("-fx-background-color: " + AZUL_EDITAR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 6; -fx-cursor: hand;");

        panelEdicion.getChildren().addAll(campoNombre, campoPMayoreo, campoPMenudeo, campoExistencia,
                campoMinimo, campoTipoPrenda, campoDescripcion, campoIdTienda, labelTalla, selectorTalla, mensajeEstado, btnGuardar);
        final Prenda[] prendaEncontrada = {null};

        Button btnBuscar = new Button("Buscar");
        btnBuscar.setStyle(estiloBtnPrincipal());
        btnBuscar.setOnAction(e -> {
            String busqueda = campoBusqueda.getText().trim();
            if (busqueda.isEmpty()) { mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("Ingresa un nombre o ID"); return; }
            Prenda encontrada = listaPrendas.stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(busqueda) || p.getId().equals(busqueda))
                .findFirst().orElse(null);
            if (encontrada == null) {
                mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("No se encontró ninguna prenda");
                panelEdicion.setVisible(false); panelEdicion.setManaged(false);
            } else {
                prendaEncontrada[0] = encontrada;
                mensajeBusqueda.setTextFill(Color.web(EXITO)); mensajeBusqueda.setText("Prenda encontrada");
                campoNombre.setText(encontrada.getNombre()); campoPMayoreo.setText(String.valueOf(encontrada.getPrecioMayoreo()));
                campoPMenudeo.setText(String.valueOf(encontrada.getPrecioMenudeo())); campoExistencia.setText(String.valueOf(encontrada.getExistencia()));
                campoMinimo.setText(String.valueOf(encontrada.getMinimoExistencia()));               
                campoTipoPrenda.setText(encontrada.getTipoPrenda()); campoDescripcion.setText(encontrada.getDescripcion());
                campoIdTienda.setText(encontrada.getIdTienda()); selectorTalla.setValue(encontrada.getTalla());
                mensajeEstado.setText(""); panelEdicion.setVisible(true); panelEdicion.setManaged(true);
            }
        });

        btnGuardar.setOnAction(e -> {
            if (prendaEncontrada[0] == null) return;
            String nombre      = campoNombre.getText().trim();
            String tipoPrenda  = campoTipoPrenda.getText().trim();
            String descripcion = campoDescripcion.getText().trim();
            String idTienda    = campoIdTienda.getText().trim();
            String talla       = selectorTalla.getValue();
            if (nombre.isEmpty() || campoExistencia.getText().isEmpty() || campoPMayoreo.getText().isEmpty() || campoPMenudeo.getText().isEmpty()) {
                mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("Todos los campos son obligatorios"); return;
            }
            try {
                int existencia  = Integer.parseInt(campoExistencia.getText().trim());
                int minimo      = campoMinimo.getText().trim().isEmpty() ? 5 : Integer.parseInt(campoMinimo.getText().trim());
                double pMayoreo = Double.parseDouble(campoPMayoreo.getText().trim());
                double pMenudeo = Double.parseDouble(campoPMenudeo.getText().trim());
                if (existencia < 0) { mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("La existencia no puede ser negativa"); return; }
                Prenda p = prendaEncontrada[0];
                if (existencia == 0) {
                    listaPrendas.remove(p); verificarConjuntos(); mostrarModuloPrendas(contenido, true);
                } else {
                    p.setNombre(nombre); p.setTalla(talla); p.setTipoPrenda(tipoPrenda);
                    p.setDescripcion(descripcion); p.setIdTienda(idTienda);
                    p.setExistencia(existencia); p.setPrecioMayoreo(pMayoreo); p.setPrecioMenudeo(pMenudeo);
                    p.setMinimoExistencia(minimo);
                    verificarConjuntos();
                    mensajeEstado.setTextFill(Color.web(EXITO)); mensajeEstado.setText("Prenda actualizada correctamente");
                }
            } catch (NumberFormatException ex) { mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("Existencia y precios deben ser números válidos"); }
        });

        Button btnCancelar = new Button("← Regresar a lista");
        btnCancelar.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXTO_SUAVE + "; -fx-font-size: 12px; -fx-cursor: hand;");
        btnCancelar.setOnAction(e -> mostrarModuloPrendas(contenido, true));

        VBox form = new VBox(12, titulo, subtitulo, campoBusqueda, btnBuscar, mensajeBusqueda, panelEdicion, btnCancelar);
        form.setAlignment(Pos.TOP_LEFT); form.setMaxWidth(420);
        form.setStyle("-fx-background-color: " + PANEL + "; -fx-padding: 35; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3);");

        ScrollPane scroll = new ScrollPane(form);
        scroll.setFitToWidth(false);
        scroll.setFitToHeight(false);
        scroll.setStyle("-fx-background-color: " + FONDO + "; -fx-background: " + FONDO + ";");

        StackPane wrapper = new StackPane(scroll);
        wrapper.setStyle("-fx-background-color: " + FONDO + "; -fx-padding: 30;");
        wrapper.setAlignment(Pos.CENTER);
        contenido.getChildren().add(wrapper);
    }

    // ── PUNTO DE VENTA ───────────────────────────────────────────────
    private void mostrarPuntoDeVenta(StackPane contenido) {
        verificarConjuntos();
        contenido.getChildren().clear();

        ObservableList<ItemVenta> carrito = FXCollections.observableArrayList();

        Label tituloBusqueda = new Label("Buscar Producto");
        tituloBusqueda.setFont(Font.font("System", FontWeight.BOLD, 15));
        tituloBusqueda.setTextFill(Color.web(SECUNDARIO));

        Label labelTipoBusqueda = new Label("Buscar en:");
        labelTipoBusqueda.setFont(Font.font("System", 12));
        labelTipoBusqueda.setTextFill(Color.web(TEXTO_SUAVE));
        ComboBox<String> selectorTipoBusqueda = new ComboBox<>();
        selectorTipoBusqueda.getItems().addAll("Prendas", "Conjuntos");
        selectorTipoBusqueda.setValue("Prendas");
        selectorTipoBusqueda.setStyle(estiloInput());
        selectorTipoBusqueda.setMaxWidth(Double.MAX_VALUE);

        TextField campoBusqueda = new TextField();
        campoBusqueda.setPromptText("Nombre o ID");
        campoBusqueda.setStyle(estiloInput());

        Label mensajeBusqueda = new Label("");
        mensajeBusqueda.setFont(Font.font("System", 12));

        VBox panelResultado = new VBox(8);
        panelResultado.setStyle("-fx-background-color: #FFF7ED; -fx-border-color: " + NARANJA + "; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 12;");
        panelResultado.setVisible(false);
        panelResultado.setManaged(false);

        Label labelResultado  = new Label("");
        labelResultado.setTextFill(Color.web(TEXTO));
        labelResultado.setFont(Font.font("System", 12));

        Label labelStock = new Label("");
        labelStock.setTextFill(Color.web(TEXTO_SUAVE));
        labelStock.setFont(Font.font("System", 11));

        Label labelPrecioInfo = new Label("");
        labelPrecioInfo.setTextFill(Color.web(CAFE));
        labelPrecioInfo.setFont(Font.font("System", FontWeight.BOLD, 12));

        TextField campoCantidad = new TextField();
        campoCantidad.setPromptText("Cantidad");
        campoCantidad.setStyle(estiloInput());

        Label labelTipoVenta = new Label("Tipo de venta:");
        labelTipoVenta.setFont(Font.font("System", 12));
        labelTipoVenta.setTextFill(Color.web(TEXTO_SUAVE));

        ComboBox<String> selectorTipoVenta = new ComboBox<>();
        selectorTipoVenta.getItems().addAll("Menudeo", "Mayoreo");
        selectorTipoVenta.setValue("Menudeo");
        selectorTipoVenta.setStyle(estiloInput());
        selectorTipoVenta.setMaxWidth(Double.MAX_VALUE);

        Label mensajeAdd = new Label("");
        mensajeAdd.setFont(Font.font("System", 12));

        Button btnAgregarCarrito = new Button("+ Agregar al carrito");
        btnAgregarCarrito.setMaxWidth(Double.MAX_VALUE);
        btnAgregarCarrito.setStyle("-fx-background-color: " + NARANJA + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 6; -fx-cursor: hand;");

        panelResultado.getChildren().addAll(labelResultado, labelStock, labelPrecioInfo,
                labelTipoVenta, selectorTipoVenta, campoCantidad, mensajeAdd, btnAgregarCarrito);

        final Prenda[]   prendaSeleccionada   = {null};
        final Conjunto[] conjuntoSeleccionado = {null};

        Button btnBuscar = new Button("Buscar");
        btnBuscar.setStyle("-fx-background-color: " + CAFE + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");

        btnBuscar.setOnAction(e -> {
            String busqueda = campoBusqueda.getText().trim();
            if (busqueda.isEmpty()) { mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("Ingresa un nombre o ID"); return; }
            prendaSeleccionada[0] = null; conjuntoSeleccionado[0] = null;

            if (selectorTipoBusqueda.getValue().equals("Prendas")) {
                Prenda encontrada = listaPrendas.stream()
                    .filter(p -> p.getNombre().equalsIgnoreCase(busqueda) || p.getId().equals(busqueda))
                    .findFirst().orElse(null);
                if (encontrada == null) {
                    mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("Producto no encontrado");
                    panelResultado.setVisible(false); panelResultado.setManaged(false);
                } else if (encontrada.getExistencia() == 0) {
                    mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("Sin existencias disponibles");
                    panelResultado.setVisible(false); panelResultado.setManaged(false);
                } else {
                    prendaSeleccionada[0] = encontrada;
                    mensajeBusqueda.setTextFill(Color.web(EXITO)); mensajeBusqueda.setText("Producto encontrado");
                    labelResultado.setText(encontrada.getNombre() + " — Talla " + encontrada.getTalla());
                    labelStock.setText("Existencia disponible: " + encontrada.getExistencia());
                    labelPrecioInfo.setText("Menudeo: $" + encontrada.getPrecioMenudeo() + "   |   Mayoreo: $" + encontrada.getPrecioMayoreo());
                    mensajeAdd.setText(""); campoCantidad.clear();
                    panelResultado.setVisible(true); panelResultado.setManaged(true);
                }
            } else {
                Conjunto encontrado = listaConjuntos.stream()
                    .filter(c -> c.getNombre().equalsIgnoreCase(busqueda) || c.getId().equals(busqueda))
                    .findFirst().orElse(null);
                int existenciaConj = encontrado != null ? calcularExistenciaConjunto(encontrado) : 0;
                if (encontrado == null) {
                    mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("Conjunto no encontrado");
                    panelResultado.setVisible(false); panelResultado.setManaged(false);
                } else if (existenciaConj == 0) {
                    mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("Sin existencias disponibles");
                    panelResultado.setVisible(false); panelResultado.setManaged(false);
                } else {
                    conjuntoSeleccionado[0] = encontrado;
                    double precio = calcularPrecioConjunto(encontrado);
                    mensajeBusqueda.setTextFill(Color.web(EXITO)); mensajeBusqueda.setText("Conjunto encontrado");
                    labelResultado.setText(encontrado.getNombre() + " — " + encontrado.getIdPrendas().size() + " piezas");
                    labelStock.setText("Existencia disponible: " + existenciaConj);
                    labelPrecioInfo.setText("Precio: $" + String.format("%.2f", precio));
                    mensajeAdd.setText(""); campoCantidad.clear();
                    panelResultado.setVisible(true); panelResultado.setManaged(true);
                }
            }
        });

        btnAgregarCarrito.setOnAction(e -> {
            try {
                int cantidad = Integer.parseInt(campoCantidad.getText().trim());
                if (cantidad <= 0) { mensajeAdd.setTextFill(Color.web(ERROR)); mensajeAdd.setText("La cantidad debe ser mayor a 0"); return; }
                String tipoVenta = selectorTipoVenta.getValue();

                if (prendaSeleccionada[0] != null) {
                    Prenda p = prendaSeleccionada[0];
                    if (cantidad > p.getExistencia()) { mensajeAdd.setTextFill(Color.web(ERROR)); mensajeAdd.setText("No hay suficiente existencia"); return; }
                    double precioU = tipoVenta.equals("Mayoreo") ? p.getPrecioMayoreo() : p.getPrecioMenudeo();
                    boolean yaEnCarrito = false;
                    for (ItemVenta item : carrito) {
                        if (item.getNombreProducto().equals(p.getNombre() + " T:" + p.getTalla()) && item.getTipoVenta().equals(tipoVenta)) {
                            int nuevaCant = item.getCantidad() + cantidad;
                            if (nuevaCant > p.getExistencia()) { mensajeAdd.setTextFill(Color.web(ERROR)); mensajeAdd.setText("Cantidad total supera existencia"); return; }
                            item.setCantidad(nuevaCant); yaEnCarrito = true; break;
                        }
                    }
                    if (!yaEnCarrito) carrito.add(new ItemVenta(p.getNombre() + " T:" + p.getTalla(), cantidad, precioU, tipoVenta, p, null));
                } else if (conjuntoSeleccionado[0] != null) {
                    Conjunto c = conjuntoSeleccionado[0];
                    int existConj = calcularExistenciaConjunto(c);
                    if (cantidad > existConj) { mensajeAdd.setTextFill(Color.web(ERROR)); mensajeAdd.setText("No hay suficiente existencia del conjunto"); return; }
                    double precioU = calcularPrecioConjunto(c);
                    carrito.add(new ItemVenta(c.getNombre() + " (Conjunto)", cantidad, precioU, tipoVenta, null, c));
                } else return;

                mensajeAdd.setTextFill(Color.web(EXITO)); mensajeAdd.setText("Agregado al carrito");
                campoBusqueda.clear(); campoCantidad.clear();
                panelResultado.setVisible(false); panelResultado.setManaged(false);
                mensajeBusqueda.setText(""); prendaSeleccionada[0] = null; conjuntoSeleccionado[0] = null;
            } catch (NumberFormatException ex) { mensajeAdd.setTextFill(Color.web(ERROR)); mensajeAdd.setText("Ingresa una cantidad válida"); }
        });

        ScrollPane scrollBusqueda = new ScrollPane(new VBox(10, tituloBusqueda, labelTipoBusqueda, selectorTipoBusqueda, campoBusqueda, btnBuscar, mensajeBusqueda, panelResultado));
        scrollBusqueda.setFitToWidth(true);
        scrollBusqueda.setStyle("-fx-background-color: " + PANEL + "; -fx-background: " + PANEL + "; -fx-border-color: #E5E7EB; -fx-border-radius: 8;");

        VBox panelBusqueda = new VBox(scrollBusqueda);
        panelBusqueda.setStyle("-fx-background-color: " + PANEL + "; -fx-padding: 0; -fx-background-radius: 8; -fx-border-color: #E5E7EB; -fx-border-radius: 8;");
        panelBusqueda.setPrefWidth(300); panelBusqueda.setMaxWidth(300);
        VBox.setVgrow(scrollBusqueda, Priority.ALWAYS);

        // Inyectar padding en el VBox dentro del scroll
        ((VBox) scrollBusqueda.getContent()).setStyle("-fx-padding: 20;");

        Label tituloCarrito = new Label("Carrito de Venta");
        tituloCarrito.setFont(Font.font("System", FontWeight.BOLD, 15));
        tituloCarrito.setTextFill(Color.web(SECUNDARIO));

        TableView<ItemVenta> tablaCarrito = new TableView<>();
        tablaCarrito.setStyle("-fx-background-color: " + PANEL + "; -fx-border-color: #E5E7EB;");
        tablaCarrito.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tablaCarrito.setItems(carrito);
        tablaCarrito.setPlaceholder(new Label("No hay productos en el carrito"));
        tablaCarrito.setMinHeight(150);

        TableColumn<ItemVenta, String> colNombre   = new TableColumn<>("Producto");
        TableColumn<ItemVenta, String> colTipo     = new TableColumn<>("Tipo");
        TableColumn<ItemVenta, String> colCantidad = new TableColumn<>("Cant.");
        TableColumn<ItemVenta, String> colPUnit    = new TableColumn<>("P. Unit.");
        TableColumn<ItemVenta, String> colSubtotal = new TableColumn<>("Subtotal");

        colNombre.setCellValueFactory(d   -> new SimpleStringProperty(d.getValue().getNombreProducto()));
        colTipo.setCellValueFactory(d     -> new SimpleStringProperty(d.getValue().getTipoVenta()));
        colCantidad.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getCantidad())));
        colPUnit.setCellValueFactory(d    -> new SimpleStringProperty("$" + String.format("%.2f", d.getValue().getPrecioUnitario())));
        colSubtotal.setCellValueFactory(d -> new SimpleStringProperty("$" + String.format("%.2f", d.getValue().getSubtotal())));

        tablaCarrito.getColumns().addAll(colNombre, colTipo, colCantidad, colPUnit, colSubtotal);
        VBox.setVgrow(tablaCarrito, Priority.ALWAYS);

        Button btnQuitar = new Button("✕ Quitar seleccionado");
        btnQuitar.setStyle("-fx-background-color: transparent; -fx-text-fill: " + ERROR + "; -fx-font-size: 12px; -fx-cursor: hand; -fx-border-color: " + ERROR + "; -fx-border-radius: 4; -fx-padding: 6 14;");
        btnQuitar.setOnAction(e -> { ItemVenta sel = tablaCarrito.getSelectionModel().getSelectedItem(); if (sel != null) carrito.remove(sel); });

        Label labelTotal = new Label("Total: $0.00");
        labelTotal.setFont(Font.font("System", FontWeight.BOLD, 18));
        labelTotal.setTextFill(Color.web(CAFE));

        carrito.addListener((javafx.collections.ListChangeListener<ItemVenta>) change -> {
            double total = carrito.stream().mapToDouble(ItemVenta::getSubtotal).sum();
            labelTotal.setText("Total: $" + String.format("%.2f", total));
        });

        Button btnCobrar = new Button("✔ Cobrar y Generar Recibo");
        btnCobrar.setStyle("-fx-background-color: " + NARANJA + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 12 24; -fx-background-radius: 6; -fx-cursor: hand;");

        Button btnCancelarVenta = new Button("✕ Cancelar venta");
        btnCancelarVenta.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXTO_SUAVE + "; -fx-font-size: 12px; -fx-cursor: hand;");
        btnCancelarVenta.setOnAction(e -> { carrito.clear(); campoBusqueda.clear(); mensajeBusqueda.setText(""); panelResultado.setVisible(false); panelResultado.setManaged(false); });

        btnCobrar.setOnAction(e -> { if (!carrito.isEmpty()) mostrarRecibo(contenido, carrito); });

        HBox filaBotones = new HBox(12, btnQuitar, btnCancelarVenta);
        filaBotones.setAlignment(Pos.CENTER_LEFT);

        HBox filaTotal = new HBox();
        filaTotal.setAlignment(Pos.CENTER_RIGHT);
        filaTotal.getChildren().add(labelTotal);

        VBox panelCarrito = new VBox(10, tituloCarrito, tablaCarrito, filaBotones, filaTotal, btnCobrar);
        panelCarrito.setStyle("-fx-background-color: " + PANEL + "; -fx-padding: 20; -fx-background-radius: 8; -fx-border-color: #E5E7EB; -fx-border-radius: 8;");
        VBox.setVgrow(tablaCarrito, Priority.ALWAYS);
        VBox.setVgrow(panelCarrito, Priority.ALWAYS);
        HBox.setHgrow(panelCarrito, Priority.ALWAYS);

        Label tituloPV = new Label("Punto de Venta");
        tituloPV.setFont(Font.font("System", FontWeight.BOLD, 20));
        tituloPV.setTextFill(Color.web(SECUNDARIO));

        HBox cuerpo = new HBox(16, panelBusqueda, panelCarrito);
        HBox.setHgrow(panelCarrito, Priority.ALWAYS);
        VBox.setVgrow(cuerpo, Priority.ALWAYS);

        VBox vista = new VBox(16, tituloPV, cuerpo);
        vista.setStyle("-fx-padding: 24;");
        VBox.setVgrow(cuerpo, Priority.ALWAYS);
        VBox.setVgrow(vista, Priority.ALWAYS);
        contenido.getChildren().add(vista);
    }

    // ── RECIBO ───────────────────────────────────────────────────────
    private void mostrarRecibo(StackPane contenido, ObservableList<ItemVenta> carrito) {
        contenido.getChildren().clear();

        List<Prenda> prendasAEliminar = new ArrayList<>();
        LocalDate fechaHoy       = LocalDate.now();
        LocalDate fechaDevolucion = fechaHoy.plusDays(DIAS_DEVOLUCION);
        String idVentaBase        = String.valueOf(contadorIdVenta++);

        int contadorItem = 0;
        for (ItemVenta item : carrito) {
            contadorItem++;
            String idVenta = idVentaBase + (carrito.size() > 1 ? "-" + contadorItem : "");

            if (item.getPrenda() != null) {
                Prenda p = item.getPrenda();
                int nuevaExist = p.getExistencia() - item.getCantidad();
                if (nuevaExist <= 0) prendasAEliminar.add(p);
                else p.setExistencia(nuevaExist);

                listaPrendasVendidas.add(new PrendaVendida(
                    idVenta, p.getNombre(), p.getTalla(), item.getCantidad(),
                    item.getTipoVenta(), item.getPrecioUnitario(),
                    fechaHoy, fechaDevolucion, ""
                ));

            } else if (item.getConjunto() != null) {
                Conjunto c = item.getConjunto();
                List<String> nombresPrendas = new ArrayList<>();

                for (String idP : c.getIdPrendas()) {
                    listaPrendas.stream().filter(p -> p.getId().equals(idP)).findFirst().ifPresent(p -> {
                        nombresPrendas.add(p.getNombre() + " (Talla " + p.getTalla() + ")");
                        int nuevaExist = p.getExistencia() - item.getCantidad();
                        if (nuevaExist <= 0) prendasAEliminar.add(p);
                        else p.setExistencia(nuevaExist);
                    });
                }

                listaConjuntosVendidos.add(new ConjuntoVendido(
                    idVenta, c.getNombre(), item.getCantidad(),
                    item.getTipoVenta(), item.getPrecioUnitario(),
                    fechaHoy, fechaDevolucion, "", nombresPrendas
                ));
            }
        }

        listaPrendas.removeAll(prendasAEliminar);
        verificarConjuntos();

        double total = carrito.stream().mapToDouble(ItemVenta::getSubtotal).sum();
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        Label tituloRecibo    = new Label("Sistema Textil");
        tituloRecibo.setFont(Font.font("System", FontWeight.BOLD, 20));
        tituloRecibo.setTextFill(Color.web(CAFE));

        Label subtituloRecibo = new Label("RECIBO DE VENTA");
        subtituloRecibo.setFont(Font.font("System", FontWeight.BOLD, 13));
        subtituloRecibo.setTextFill(Color.web(TEXTO_SUAVE));

        Label labelFecha = new Label("Fecha: " + fecha + "   |   Venta #" + idVentaBase);
        labelFecha.setFont(Font.font("System", 12));
        labelFecha.setTextFill(Color.web(TEXTO_SUAVE));

        Label labelDevolucion = new Label("Devoluciones hasta: " + fechaDevolucion.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        labelDevolucion.setFont(Font.font("System", FontWeight.BOLD, 12));
        labelDevolucion.setTextFill(Color.web(NARANJA));

        Region sep1 = new Region(); sep1.setPrefHeight(1); sep1.setStyle("-fx-background-color: #E5E7EB;");

        Label hProducto = new Label("Producto"); Label hTipo = new Label("Tipo");
        Label hCant = new Label("Cant."); Label hPUnit = new Label("P.Unit"); Label hSub = new Label("Subtotal");
        for (Label h : new Label[]{hProducto, hTipo, hCant, hPUnit, hSub}) {
            h.setFont(Font.font("System", FontWeight.BOLD, 11));
            h.setTextFill(Color.web(TEXTO_SUAVE));
        }

        GridPane encabezado = new GridPane(); encabezado.setHgap(12);
        ColumnConstraints c0 = new ColumnConstraints(); c0.setPrefWidth(200);
        ColumnConstraints c1 = new ColumnConstraints(); c1.setPrefWidth(70);
        ColumnConstraints c2 = new ColumnConstraints(); c2.setPrefWidth(45);
        ColumnConstraints c3 = new ColumnConstraints(); c3.setPrefWidth(80);
        ColumnConstraints c4 = new ColumnConstraints(); c4.setPrefWidth(90);
        encabezado.getColumnConstraints().addAll(c0, c1, c2, c3, c4);
        encabezado.add(hProducto, 0, 0); encabezado.add(hTipo, 1, 0);
        encabezado.add(hCant, 2, 0); encabezado.add(hPUnit, 3, 0); encabezado.add(hSub, 4, 0);

        VBox filas = new VBox(6);
        for (ItemVenta item : carrito) {
            Label lNombre   = new Label(item.getNombreProducto());
            Label lTipo     = new Label(item.getTipoVenta());
            Label lCant     = new Label(String.valueOf(item.getCantidad()));
            Label lPUnit    = new Label("$" + String.format("%.2f", item.getPrecioUnitario()));
            Label lSubtotal = new Label("$" + String.format("%.2f", item.getSubtotal()));

            for (Label l : new Label[]{lNombre, lTipo, lCant, lPUnit, lSubtotal}) { l.setFont(Font.font("System", 12)); l.setTextFill(Color.web(TEXTO)); }
            lSubtotal.setTextFill(Color.web(NARANJA)); lSubtotal.setFont(Font.font("System", FontWeight.BOLD, 12));

            GridPane fila = new GridPane(); fila.setHgap(12);
            fila.getColumnConstraints().addAll(c0, c1, c2, c3, c4);
            fila.add(lNombre, 0, 0); fila.add(lTipo, 1, 0);
            fila.add(lCant, 2, 0); fila.add(lPUnit, 3, 0); fila.add(lSubtotal, 4, 0);
            filas.getChildren().add(fila);
        }

        Region sep2 = new Region(); sep2.setPrefHeight(1); sep2.setStyle("-fx-background-color: #E5E7EB;");

        Label labelTotalFinal = new Label("TOTAL:   $" + String.format("%.2f", total));
        labelTotalFinal.setFont(Font.font("System", FontWeight.BOLD, 18));
        labelTotalFinal.setTextFill(Color.web(CAFE));

        Label labelGracias = new Label("¡Gracias por su compra!");
        labelGracias.setFont(Font.font("System", 12));
        labelGracias.setTextFill(Color.web(TEXTO_SUAVE));

        Button btnNuevaVenta = new Button("+ Nueva Venta");
        btnNuevaVenta.setStyle("-fx-background-color: " + NARANJA + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 24; -fx-background-radius: 6; -fx-cursor: hand;");
        btnNuevaVenta.setOnAction(e -> mostrarPuntoDeVenta(contenido));

        VBox recibo = new VBox(12, tituloRecibo, subtituloRecibo, labelFecha, labelDevolucion,
                sep1, encabezado, filas, sep2, labelTotalFinal, labelGracias, btnNuevaVenta);
        recibo.setAlignment(Pos.TOP_LEFT);
        recibo.setStyle("-fx-background-color: " + PANEL + "; -fx-padding: 32; -fx-background-radius: 8; -fx-border-color: " + NARANJA + "; -fx-border-width: 2; -fx-border-radius: 8;");
        recibo.setMaxWidth(560);

        ScrollPane scroll = new ScrollPane(recibo);
        scroll.setFitToWidth(false);
        scroll.setFitToHeight(false);
        scroll.setStyle("-fx-background-color: " + FONDO + "; -fx-background: " + FONDO + ";");

        StackPane wrapper = new StackPane(scroll);
        wrapper.setStyle("-fx-background-color: " + FONDO + "; -fx-padding: 30;");
        wrapper.setAlignment(Pos.CENTER);
        contenido.getChildren().add(wrapper);
    }

    // ── DEVOLUCIONES ─────────────────────────────────────────────────
    private void mostrarDevoluciones(StackPane contenido) {
        contenido.getChildren().clear();
 
        Label titulo = new Label("Devoluciones");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));
 
        Label subtitulo = new Label("Busca por ID de venta o nombre del producto para procesar la devolución");
        subtitulo.setFont(Font.font("System", 12));
        subtitulo.setTextFill(Color.web(TEXTO_SUAVE));
 
        TextField campoBusqueda = crearTextField("ID de venta o nombre de prenda/conjunto");
        campoBusqueda.setMaxWidth(400);
 
        Label mensajeBusqueda = new Label("");
        mensajeBusqueda.setFont(Font.font("System", 12));
 
        VBox panelResultado = new VBox(12);
        panelResultado.setStyle(
            "-fx-background-color: #FFF7ED; -fx-border-color: " + NARANJA + ";" +
            "-fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 16;");
        panelResultado.setVisible(false);
        panelResultado.setManaged(false);
        panelResultado.setMaxWidth(480);
 
        final PrendaVendida[]   pvRef = {null};
        final ConjuntoVendido[] cvRef = {null};
 
        Label labelNombre      = new Label("");
        Label labelFechaVenta  = new Label("");
        Label labelLimite      = new Label("");
        Label labelEstadoDev   = new Label("");
        Label labelCantVendida = new Label("");
 
        labelNombre.setFont(Font.font("System", FontWeight.BOLD, 15));
        labelNombre.setTextFill(Color.web(SECUNDARIO));
        for (Label l : new Label[]{labelFechaVenta, labelLimite, labelCantVendida}) {
            l.setFont(Font.font("System", 12));
            l.setTextFill(Color.web(TEXTO_SUAVE));
        }
        labelEstadoDev.setFont(Font.font("System", FontWeight.BOLD, 12));
 
        Label labelCantDev = new Label("Cantidad a devolver:");
        labelCantDev.setFont(Font.font("System", 12));
        labelCantDev.setTextFill(Color.web(TEXTO_SUAVE));
 
        TextField campoCantidad = crearTextField("Cantidad");
        campoCantidad.setMaxWidth(200);
 
        Label mensajeDevolucion = new Label("");
        mensajeDevolucion.setFont(Font.font("System", 12));
 
        Button btnConfirmar = new Button("✔ Confirmar Devolución");
        btnConfirmar.setStyle(
            "-fx-background-color: " + EXITO + "; -fx-text-fill: white; -fx-font-weight: bold;" +
            "-fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
 
        Region sepInterno = new Region();
        sepInterno.setPrefHeight(1);
        sepInterno.setStyle("-fx-background-color: #E5E7EB;");
 
        panelResultado.getChildren().addAll(
            labelNombre, sepInterno,
            labelFechaVenta, labelLimite, labelEstadoDev, labelCantVendida,
            labelCantDev, campoCantidad, mensajeDevolucion, btnConfirmar
        );
 
        Button btnBuscar = new Button("Buscar");
        btnBuscar.setStyle(estiloBtnPrincipal());
 
        btnBuscar.setOnAction(e -> {
            String busqueda = campoBusqueda.getText().trim();
            if (busqueda.isEmpty()) {
                mensajeBusqueda.setTextFill(Color.web(ERROR));
                mensajeBusqueda.setText("Ingresa un ID de venta o nombre");
                return;
            }
 
            pvRef[0] = null; cvRef[0] = null;
            mensajeDevolucion.setText(""); campoCantidad.clear();
 
            PrendaVendida pv = listaPrendasVendidas.stream()
                .filter(x -> x.getIdVenta().equalsIgnoreCase(busqueda) || x.getNombrePrenda().equalsIgnoreCase(busqueda))
                .findFirst().orElse(null);
 
            ConjuntoVendido cv = null;
            if (pv == null) {
                cv = listaConjuntosVendidos.stream()
                    .filter(x -> x.getIdVenta().equalsIgnoreCase(busqueda) || x.getNombreConjunto().equalsIgnoreCase(busqueda))
                    .findFirst().orElse(null);
            }
 
            if (pv == null && cv == null) {
                mensajeBusqueda.setTextFill(Color.web(ERROR));
                mensajeBusqueda.setText("No se encontró ninguna venta con ese dato");
                panelResultado.setVisible(false); panelResultado.setManaged(false);
                return;
            }
 
            boolean dentroDelPlazo;
            if (pv != null) {
                pvRef[0] = pv;
                dentroDelPlazo = !LocalDate.now().isAfter(pv.getFechaLimiteDevolucion());
                labelNombre.setText("Prenda: " + pv.getNombrePrenda() + " — Talla " + pv.getTalla());
                labelFechaVenta.setText("Fecha de venta: " + pv.getFechaVenta().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                labelLimite.setText("Límite de devolución: " + pv.getFechaLimiteDevolucion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                labelCantVendida.setText("Cantidad vendida: " + pv.getCantidad() + "   |   Tipo: " + pv.getTipoVenta() + "   |   Precio unit.: $" + String.format("%.2f", pv.getPrecioUnitario()));
            } else {
                cvRef[0] = cv;
                dentroDelPlazo = !LocalDate.now().isAfter(cv.getFechaLimiteDevolucion());
                labelNombre.setText("Conjunto: " + cv.getNombreConjunto());
                labelFechaVenta.setText("Fecha de venta: " + cv.getFechaVenta().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                labelLimite.setText("Límite de devolución: " + cv.getFechaLimiteDevolucion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                labelCantVendida.setText("Cantidad vendida: " + cv.getCantidad() + "   |   Tipo: " + cv.getTipoVenta() + "   |   Precio unit.: $" + String.format("%.2f", cv.getPrecioUnitario()));
            }
 
            labelEstadoDev.setText(dentroDelPlazo
                ? "✔ Dentro del periodo de devolución"
                : "⚠ Periodo de devolución vencido — se puede registrar igualmente");
            labelEstadoDev.setTextFill(Color.web(dentroDelPlazo ? EXITO : ADVERTENCIA));
 
            mensajeBusqueda.setTextFill(Color.web(EXITO));
            mensajeBusqueda.setText("Venta encontrada");
            panelResultado.setVisible(true); panelResultado.setManaged(true);
        });
 
        btnConfirmar.setOnAction(e -> {
            String cantStr = campoCantidad.getText().trim();
            if (cantStr.isEmpty()) {
                mensajeDevolucion.setTextFill(Color.web(ERROR));
                mensajeDevolucion.setText("Ingresa la cantidad a devolver");
                return;
            }
            try {
                int cantDevolver = Integer.parseInt(cantStr);
                if (cantDevolver <= 0) {
                    mensajeDevolucion.setTextFill(Color.web(ERROR));
                    mensajeDevolucion.setText("La cantidad debe ser mayor a 0");
                    return;
                }
 
                if (pvRef[0] != null) {
                    PrendaVendida pv = pvRef[0];
                    if (cantDevolver > pv.getCantidad()) {
                        mensajeDevolucion.setTextFill(Color.web(ERROR));
                        mensajeDevolucion.setText("No puedes devolver más de " + pv.getCantidad() + " unidades");
                        return;
                    }
 
                    Prenda prendaExistente = listaPrendas.stream()
                        .filter(p -> p.getNombre().equalsIgnoreCase(pv.getNombrePrenda())
                                  && p.getTalla().equalsIgnoreCase(pv.getTalla()))
                        .findFirst().orElse(null);
 
                    if (prendaExistente != null) {
                        prendaExistente.setExistencia(prendaExistente.getExistencia() + cantDevolver);
                    } else {
                        String idNuevo = "DEV-" + contadorIdVenta++;
                        listaPrendas.add(new Prenda(
                            pv.getNombrePrenda(), idNuevo, pv.getTalla(),
                            "Devuelto", cantDevolver,
                            pv.getPrecioUnitario(), pv.getPrecioUnitario(),
                            "—", "Prenda devuelta — ID venta: " + pv.getIdVenta()
                        ));
                    }
 
                    boolean eliminarRegistro = (cantDevolver == pv.getCantidad());
                    String idVentaOriginal = pv.getIdVenta();
                    String nombreProd = pv.getNombrePrenda();
                    String tallaProd  = pv.getTalla();
                    String tipoVentaProd = pv.getTipoVenta();
                    double precioUnitProd = pv.getPrecioUnitario();
 
                    if (eliminarRegistro) {
                        listaPrendasVendidas.remove(pv);
                        pvRef[0] = null;
                        panelResultado.setVisible(false); panelResultado.setManaged(false);
                        campoBusqueda.clear(); mensajeBusqueda.setText("");
                    } else {
                        pv.setCantidad(pv.getCantidad() - cantDevolver);
                        labelCantVendida.setText("Cantidad vendida: " + pv.getCantidad() + "   |   Tipo: " + pv.getTipoVenta() + "   |   Precio unit.: $" + String.format("%.2f", pv.getPrecioUnitario()));
                    }
 
                    // Abrir formulario para capturar el motivo y registrar la devolución
                    mostrarFormularioMotivoDevolucion(contenido, idVentaOriginal, nombreProd, tallaProd,
                        cantDevolver, tipoVentaProd, precioUnitProd, false);
                    return;
 
                } else if (cvRef[0] != null) {
                    ConjuntoVendido cv = cvRef[0];
                    if (cantDevolver > cv.getCantidad()) {
                        mensajeDevolucion.setTextFill(Color.web(ERROR));
                        mensajeDevolucion.setText("No puedes devolver más de " + cv.getCantidad() + " unidades");
                        return;
                    }
 
                    for (String nombrePrendaConj : cv.getNombresPrendas()) {
                        String nombreBase    = nombrePrendaConj;
                        String tallaExtraida = "";
                        int idxParen = nombrePrendaConj.lastIndexOf("(Talla ");
                        if (idxParen >= 0) {
                            nombreBase    = nombrePrendaConj.substring(0, idxParen).trim();
                            tallaExtraida = nombrePrendaConj.substring(idxParen + 7, nombrePrendaConj.lastIndexOf(")")).trim();
                        }
                        final String nBase = nombreBase;
                        final String talla = tallaExtraida;
 
                        Prenda prendaExistente = listaPrendas.stream()
                            .filter(p -> p.getNombre().equalsIgnoreCase(nBase)
                                      && (talla.isEmpty() || p.getTalla().equalsIgnoreCase(talla)))
                            .findFirst().orElse(null);
 
                        if (prendaExistente != null) {
                            prendaExistente.setExistencia(prendaExistente.getExistencia() + cantDevolver);
                        } else {
                            String idNuevo = "DEV-" + contadorIdVenta++;
                            listaPrendas.add(new Prenda(
                                nBase, idNuevo, talla.isEmpty() ? "?" : talla,
                                "Devuelto", cantDevolver, 0.0, 0.0, "—",
                                "Prenda devuelta de conjunto — ID venta: " + cv.getIdVenta()
                            ));
                        }
                    }
 
                    boolean eliminarRegistro = (cantDevolver == cv.getCantidad());
                    String idVentaOriginal = cv.getIdVenta();
                    String nombreProd = cv.getNombreConjunto();
                    String tipoVentaProd = cv.getTipoVenta();
                    double precioUnitProd = cv.getPrecioUnitario();
 
                    if (eliminarRegistro) {
                        listaConjuntosVendidos.remove(cv);
                        cvRef[0] = null;
                        panelResultado.setVisible(false); panelResultado.setManaged(false);
                        campoBusqueda.clear(); mensajeBusqueda.setText("");
                    } else {
                        cv.setCantidad(cv.getCantidad() - cantDevolver);
                        labelCantVendida.setText("Cantidad vendida: " + cv.getCantidad() + "   |   Tipo: " + cv.getTipoVenta() + "   |   Precio unit.: $" + String.format("%.2f", cv.getPrecioUnitario()));
                    }
 
                    // Abrir formulario para capturar el motivo y registrar la devolución
                    mostrarFormularioMotivoDevolucion(contenido, idVentaOriginal, nombreProd, "—",
                        cantDevolver, tipoVentaProd, precioUnitProd, true);
                    return;
                }
 
            } catch (NumberFormatException ex) {
                mensajeDevolucion.setTextFill(Color.web(ERROR));
                mensajeDevolucion.setText("Ingresa una cantidad válida");
            }
        });
 
        VBox form = new VBox(14, titulo, subtitulo, campoBusqueda, btnBuscar, mensajeBusqueda, panelResultado);
        form.setAlignment(Pos.TOP_LEFT);
        form.setMaxWidth(520);
        form.setStyle("-fx-background-color: " + PANEL + "; -fx-padding: 35; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3);");
 
        ScrollPane scroll = new ScrollPane(form);
        scroll.setFitToWidth(false);
        scroll.setFitToHeight(false);
        scroll.setStyle("-fx-background-color: " + FONDO + "; -fx-background: " + FONDO + ";");
 
        StackPane wrapper = new StackPane(scroll);
        wrapper.setStyle("-fx-background-color: " + FONDO + "; -fx-padding: 30;");
        wrapper.setAlignment(Pos.CENTER);
        contenido.getChildren().add(wrapper);
    }
 
    // ── FORMULARIO CAPTURA DE MOTIVO (tras confirmar devolución) ─────
    private void mostrarFormularioMotivoDevolucion(StackPane contenido, String idVentaOriginal,
            String nombreProducto, String talla, int cantidad, String tipoVenta,
            double precioUnitario, boolean esConjunto) {
        contenido.getChildren().clear();
 
        Label titulo = new Label("Confirmar Devolución");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));
 
        Label subtitulo = new Label("La devolución ya fue procesada en el inventario. Indica el motivo para completar el registro.");
        subtitulo.setFont(Font.font("System", 12));
        subtitulo.setTextFill(Color.web(TEXTO_SUAVE));
        subtitulo.setWrapText(true);
 
        VBox tarjetaInfo = new VBox(8);
        tarjetaInfo.setStyle("-fx-background-color: #F0FDF4; -fx-border-color: " + EXITO + "; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 16;");
 
        double total = cantidad * precioUnitario;
 
        tarjetaInfo.getChildren().addAll(
            filaDetalle("ID venta original:", idVentaOriginal),
            filaDetalle("Producto:", nombreProducto + (esConjunto ? " (Conjunto)" : "")),
            filaDetalle("Talla:", talla),
            filaDetalle("Cantidad devuelta:", String.valueOf(cantidad)),
            filaDetalle("Tipo de venta:", tipoVenta),
            filaDetalle("Precio unitario:", "$" + String.format("%.2f", precioUnitario)),
            filaDetalle("Reembolso:", "$" + String.format("%.2f", total))
        );
 
        Label labelMotivo = new Label("Motivo de la devolución:");
        labelMotivo.setFont(Font.font("System", 12));
        labelMotivo.setTextFill(Color.web(TEXTO_SUAVE));
 
        TextField campoMotivo = crearTextField("Ej: Talla incorrecta, defecto de fábrica...");
        campoMotivo.setMaxWidth(400);
 
        Label mensajeEstado = new Label("");
        mensajeEstado.setFont(Font.font("System", 12));
 
        Button btnGuardar = new Button("✔ Guardar Registro de Devolución");
        btnGuardar.setMaxWidth(320);
        btnGuardar.setStyle(
            "-fx-background-color: " + EXITO + "; -fx-text-fill: white; -fx-font-weight: bold;" +
            "-fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 6; -fx-cursor: hand;");
 
        btnGuardar.setOnAction(e -> {
    String motivo = campoMotivo.getText().trim();
    String idDevolucion = "DEVREG-" + contadorIdVenta++;
    listaDevolucionesRegistradas.add(new DevolucionRegistrada(
        idDevolucion, nombreProducto, talla, cantidad, tipoVenta, precioUnitario,
        LocalDate.now(), motivo.isEmpty() ? "Sin motivo especificado" : motivo
    ));
    mostrarDevoluciones(contenido);
});
 
        Button btnOmitir = new Button("Omitir y regresar sin guardar motivo");
        btnOmitir.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXTO_SUAVE + "; -fx-font-size: 12px; -fx-cursor: hand;");
        btnOmitir.setOnAction(e -> {
            String idDevolucion = "DEVREG-" + contadorIdVenta++;
            listaDevolucionesRegistradas.add(new DevolucionRegistrada(
                idDevolucion, nombreProducto, talla, cantidad, tipoVenta, precioUnitario,
                LocalDate.now(), "No especificado"
            ));
            mostrarDevoluciones(contenido);
        });
 
        VBox form = new VBox(14, titulo, subtitulo, tarjetaInfo, labelMotivo, campoMotivo,
                mensajeEstado, btnGuardar, btnOmitir);
        form.setAlignment(Pos.TOP_LEFT);
        form.setMaxWidth(520);
        form.setStyle("-fx-background-color: " + PANEL + "; -fx-padding: 35; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3);");
 
        ScrollPane scroll = new ScrollPane(form);
        scroll.setFitToWidth(false);
        scroll.setFitToHeight(false);
        scroll.setStyle("-fx-background-color: " + FONDO + "; -fx-background: " + FONDO + ";");
 
        StackPane wrapper = new StackPane(scroll);
        wrapper.setStyle("-fx-background-color: " + FONDO + "; -fx-padding: 30;");
        wrapper.setAlignment(Pos.CENTER);
        contenido.getChildren().add(wrapper);
    }
 
 

    // ── MÓDULO USUARIOS ──────────────────────────────────────────────
    private void mostrarModuloUsuarios(StackPane contenido) {
        contenido.getChildren().clear();

        Label titulo = new Label("Gestión de Usuarios");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        TableView<Usuario> tabla = new TableView<>();
        tabla.setStyle("-fx-background-color: " + PANEL + "; -fx-border-color: #E5E7EB;");
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabla.setPlaceholder(new Label("No hay usuarios registrados"));
        tabla.setMinHeight(200);

        TableColumn<Usuario, String> colNombre  = new TableColumn<>("Nombre");
        TableColumn<Usuario, String> colUsuario = new TableColumn<>("Usuario");
        TableColumn<Usuario, String> colRol     = new TableColumn<>("Rol");

        colNombre.setCellValueFactory(d  -> new SimpleStringProperty(d.getValue().getNombre()));
        colUsuario.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getUsuario()));
        colRol.setCellValueFactory(d     -> new SimpleStringProperty(d.getValue().getRol()));

        tabla.getColumns().addAll(colNombre, colUsuario, colRol);
        tabla.setItems(listaUsuarios);
        VBox.setVgrow(tabla, Priority.ALWAYS);

        Button btnNuevo = new Button("+ Nuevo Usuario");
        btnNuevo.setStyle(estiloBtnPrincipal());
        btnNuevo.setOnAction(e -> mostrarFormularioCrearUsuario(contenido));

        VBox vista = new VBox(16, titulo, tabla, btnNuevo);
        vista.setStyle("-fx-padding: 30;");
        VBox.setVgrow(tabla, Priority.ALWAYS);
        VBox.setVgrow(vista, Priority.ALWAYS);
        contenido.getChildren().add(vista);
    }

    // ── FORMULARIO CREAR USUARIO ─────────────────────────────────────
    private void mostrarFormularioCrearUsuario(StackPane contenido) {
        contenido.getChildren().clear();

        Label titulo    = new Label("Nuevo Usuario");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        Label subtitulo = new Label("Completa los campos para crear el usuario");
        subtitulo.setFont(Font.font("System", 12));
        subtitulo.setTextFill(Color.web(TEXTO_SUAVE));

        TextField campoNombre  = crearTextField("Nombre completo");
        TextField campoUsuario = crearTextField("Usuario");

        PasswordField campoPass = new PasswordField();
        campoPass.setPromptText("Contraseña"); campoPass.setMaxWidth(320); campoPass.setStyle(estiloInput());

        PasswordField campoPassConfirm = new PasswordField();
        campoPassConfirm.setPromptText("Confirmar contraseña"); campoPassConfirm.setMaxWidth(320); campoPassConfirm.setStyle(estiloInput());

        Label labelRol = new Label("Rol del usuario:");
        labelRol.setTextFill(Color.web(TEXTO_SUAVE)); labelRol.setFont(Font.font("System", 12));

        ComboBox<String> selectorRol = new ComboBox<>();
        selectorRol.getItems().addAll("administrador", "encargado");
        selectorRol.setValue("encargado"); selectorRol.setMaxWidth(320); selectorRol.setStyle(estiloInput());

        Label mensajeEstado = new Label("");
        mensajeEstado.setFont(Font.font("System", 12));

        Button btnGuardar = new Button("Crear Usuario");
        btnGuardar.setMaxWidth(320); btnGuardar.setStyle(estiloBtnPrincipal());

        Button btnCancelar = new Button("← Regresar a lista");
        btnCancelar.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXTO_SUAVE + "; -fx-font-size: 12px; -fx-cursor: hand;");
        btnCancelar.setOnAction(e -> mostrarModuloUsuarios(contenido));

        btnGuardar.setOnAction(e -> {
            String nombre  = campoNombre.getText().trim();
            String usuario = campoUsuario.getText().trim();
            String pass    = campoPass.getText().trim();
            String passC   = campoPassConfirm.getText().trim();
            if (nombre.isEmpty() || usuario.isEmpty() || pass.isEmpty()) {
                mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("Todos los campos son obligatorios");
            } else if (!pass.equals(passC)) {
                mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("Las contraseñas no coinciden");
            } else {
                listaUsuarios.add(new Usuario(nombre, usuario, pass, selectorRol.getValue()));
                mostrarModuloUsuarios(contenido);
            }
        });

        VBox form = new VBox(12, titulo, subtitulo, campoNombre, campoUsuario,
                campoPass, campoPassConfirm, labelRol, selectorRol, mensajeEstado, btnGuardar, btnCancelar);
        form.setAlignment(Pos.TOP_LEFT); form.setMaxWidth(400);
        form.setStyle("-fx-background-color: " + PANEL + "; -fx-padding: 35; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3);");

        ScrollPane scroll = new ScrollPane(form);
        scroll.setFitToWidth(false);
        scroll.setFitToHeight(false);
        scroll.setStyle("-fx-background-color: " + FONDO + "; -fx-background: " + FONDO + ";");

        StackPane wrapper = new StackPane(scroll);
        wrapper.setStyle("-fx-background-color: " + FONDO + "; -fx-padding: 30;");
        wrapper.setAlignment(Pos.CENTER);
        contenido.getChildren().add(wrapper);
    }

    // ── HELPERS ──────────────────────────────────────────────────────
    private Label crearSeccionMenu(String texto) {
        Label label = new Label(texto);
        label.setFont(Font.font("System", FontWeight.BOLD, 10));
        label.setTextFill(Color.web("#64748B"));
        label.setStyle("-fx-padding: 16 16 4 16;");
        label.setMaxWidth(Double.MAX_VALUE);
        return label;
    }

private String calcularColorAlerta() {
    boolean hayCritica = false, hayBaja = false;
    for (Prenda p : listaPrendas) {
        if (p.getExistencia() <= p.getMinimoExistencia())           hayCritica = true;
        else if (p.getExistencia() <= p.getMinimoExistencia() * 2)  hayBaja = true;
    }
    for (Conjunto c : listaConjuntos) {
        int ex = calcularExistenciaConjunto(c);
        if (ex <= c.getMinimoExistencia())           hayCritica = true;
        else if (ex <= c.getMinimoExistencia() * 2)  hayBaja = true;
    }
    for (MateriaPrima mp : listaMateriaPrima) {
        if (mp.getExistencia() <= mp.getMinimoExistencia())           hayCritica = true;
        else if (mp.getExistencia() <= mp.getMinimoExistencia() * 2)  hayBaja = true;
    }
    if (hayCritica) return ERROR;
    if (hayBaja)    return ADVERTENCIA;
    return EXITO;
}

private String calcularColorAlertaEncargado() {
    boolean hayCritica = false, hayBaja = false;
    for (Prenda p : listaPrendas) {
        if (p.getExistencia() <= p.getMinimoExistencia())           hayCritica = true;
        else if (p.getExistencia() <= p.getMinimoExistencia() * 2)  hayBaja = true;
    }
    for (Conjunto c : listaConjuntos) {
        int ex = calcularExistenciaConjunto(c);
        if (ex <= c.getMinimoExistencia())           hayCritica = true;
        else if (ex <= c.getMinimoExistencia() * 2)  hayBaja = true;
    }
    if (hayCritica) return ERROR;
    if (hayBaja)    return ADVERTENCIA;
    return EXITO;
}

    private StackPane crearContenidoVacio() {
        StackPane contenido = new StackPane();
        contenido.setStyle("-fx-background-color: " + FONDO + ";");
        Label placeholder = new Label("Selecciona un módulo del menú");
        placeholder.setTextFill(Color.web("#D1D5DB"));
        placeholder.setFont(Font.font("System", 16));
        contenido.getChildren().add(placeholder);
        return contenido;
    }

    private TextField crearTextField(String placeholder) {
        TextField field = new TextField();
        field.setPromptText(placeholder); field.setMaxWidth(320); field.setStyle(estiloInput());
        return field;
    }

    private String estiloInput() {
        return "-fx-background-color: " + PANEL + "; -fx-border-color: #D1D5DB; -fx-border-width: 1;" +
               "-fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 9 12; -fx-font-size: 13px; -fx-text-fill: " + TEXTO + ";";
    }

    private String estiloBtnPrincipal() {
        return "-fx-background-color: " + PRINCIPAL + "; -fx-text-fill: white; -fx-font-weight: bold;" +
               "-fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;";
    }

    private Button crearBotonMenuColor(String texto, String colorHover) {
        Button btn = new Button(texto);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #CBD5E1; -fx-font-size: 13px; -fx-padding: 10 16; -fx-cursor: hand;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: " + colorHover + "; -fx-text-fill: white; -fx-font-size: 13px; -fx-padding: 10 16; -fx-cursor: hand;"));
        btn.setOnMouseExited(e  -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #CBD5E1; -fx-font-size: 13px; -fx-padding: 10 16; -fx-cursor: hand;"));
        return btn;
    }

    private void mostrarPlaceholder(StackPane contenido, String modulo) {
        contenido.getChildren().clear();
        Label label = new Label(modulo);
        label.setTextFill(Color.web("#D1D5DB"));
        label.setFont(Font.font("System", FontWeight.BOLD, 20));
        contenido.getChildren().add(label);
    }

    private String verificarLogin(String usuario, String contrasena) {
        for (Usuario u : listaUsuarios) {
            if (u.getUsuario().equals(usuario) && u.getPassword().equals(contrasena)) return u.getRol();
        }
        return null;
    }

    public static void main(String[] args) { launch(); }

    // ── REGISTRO DE DEVOLUCIONES ─────────────────────────────────────
    private ObservableList<DevolucionRegistrada> listaDevolucionesRegistradas = FXCollections.observableArrayList();

    private void mostrarRegistroDevoluciones(StackPane contenido, boolean esAdmin) {
        contenido.getChildren().clear();

        Label titulo = new Label("Registro de Devoluciones");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        TableView<DevolucionRegistrada> tabla = new TableView<>();
        tabla.setStyle("-fx-background-color: " + PANEL + "; -fx-border-color: #E5E7EB;");
        tabla.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tabla.setPlaceholder(new Label("No hay devoluciones registradas"));
        tabla.setMinHeight(200);
        tabla.setRowFactory(tv -> {
            TableRow<DevolucionRegistrada> row = new TableRow<>();
            row.setPrefHeight(48);
            return row;
        });

        TableColumn<DevolucionRegistrada, String> colId       = new TableColumn<>("ID Dev.");
        TableColumn<DevolucionRegistrada, String> colProducto = new TableColumn<>("Producto");
        TableColumn<DevolucionRegistrada, String> colTalla    = new TableColumn<>("Talla");
        TableColumn<DevolucionRegistrada, String> colCantidad = new TableColumn<>("Cant.");
        TableColumn<DevolucionRegistrada, String> colTipo     = new TableColumn<>("Tipo");
        TableColumn<DevolucionRegistrada, String> colPrecio   = new TableColumn<>("Precio Unit.");
        TableColumn<DevolucionRegistrada, String> colTotal    = new TableColumn<>("Total Dev.");
        TableColumn<DevolucionRegistrada, String> colFecha    = new TableColumn<>("Fecha Dev.");
        TableColumn<DevolucionRegistrada, String> colMotivo   = new TableColumn<>("Motivo");

        colId.setPrefWidth(100);
        colProducto.setPrefWidth(160);
        colTalla.setPrefWidth(70);
        colCantidad.setPrefWidth(60);
        colTipo.setPrefWidth(90);
        colPrecio.setPrefWidth(100);
        colTotal.setPrefWidth(100);
        colFecha.setPrefWidth(100);
        colMotivo.setPrefWidth(320);

        colId.setCellValueFactory(d       -> new SimpleStringProperty(d.getValue().getIdDevolucion()));
        colProducto.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombreProducto()));
        colTalla.setCellValueFactory(d    -> new SimpleStringProperty(d.getValue().getTalla()));
        colCantidad.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getCantidad())));
        colTipo.setCellValueFactory(d     -> new SimpleStringProperty(d.getValue().getTipoVenta()));
        colPrecio.setCellValueFactory(d   -> new SimpleStringProperty("$" + String.format("%.2f", d.getValue().getPrecioUnitario())));
        colTotal.setCellValueFactory(d    -> new SimpleStringProperty("$" + String.format("%.2f", d.getValue().getTotal())));
        colFecha.setCellValueFactory(d    -> new SimpleStringProperty(d.getValue().getFechaDevolucion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        colMotivo.setCellValueFactory(d   -> new SimpleStringProperty(d.getValue().getMotivo()));

        // Celda con wrap-text para que el motivo se lea completo sin cortarse
        colMotivo.setCellFactory(col -> new TableCell<DevolucionRegistrada, String>() {
            private final Text text = new Text();
            {
                text.wrappingWidthProperty().bind(colMotivo.widthProperty().subtract(10));
                setGraphic(text);
                setPrefHeight(Region.USE_COMPUTED_SIZE);
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    text.setText(null);
                } else {
                    text.setText(item);
                }
            }
        });

        tabla.getColumns().addAll(colId, colProducto, colTalla, colCantidad, colTipo, colPrecio, colTotal, colFecha, colMotivo);
        tabla.setItems(listaDevolucionesRegistradas);

        ScrollPane scrollTabla = new ScrollPane(tabla);
        scrollTabla.setFitToHeight(true);
        scrollTabla.setFitToWidth(false);
        scrollTabla.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollTabla.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollTabla.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        VBox.setVgrow(scrollTabla, Priority.ALWAYS);

        VBox vista = new VBox(16, titulo, scrollTabla);
        vista.setStyle("-fx-padding: 30;");
        VBox.setVgrow(scrollTabla, Priority.ALWAYS);
        VBox.setVgrow(vista, Priority.ALWAYS);

        if (esAdmin) {
            Button btnNuevo = new Button("+ Registrar Devolución");
            Button btnEditar = new Button("✎ Editar Registro");
            btnNuevo.setStyle("-fx-background-color: " + NARANJA + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
            btnEditar.setStyle("-fx-background-color: " + AZUL_EDITAR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
            btnNuevo.setOnAction(e  -> mostrarFormularioNuevaDevolucion(contenido));
            btnEditar.setOnAction(e -> mostrarFormularioEditarDevolucion(contenido));
            HBox botones = new HBox(12, btnNuevo, btnEditar);
            vista.getChildren().add(botones);
        }

        ScrollPane scroll = new ScrollPane(vista);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(false);
        scroll.setStyle("-fx-background-color: " + FONDO + "; -fx-background: " + FONDO + ";");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        StackPane wrapper = new StackPane(scroll);
        wrapper.setStyle("-fx-background-color: " + FONDO + ";");
        contenido.getChildren().add(wrapper);
    }

    private void mostrarFormularioNuevaDevolucion(StackPane contenido) {
        contenido.getChildren().clear();

        Label titulo    = new Label("Registrar Devolución");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        Label subtitulo = new Label("Completa los campos para registrar la devolución");
        subtitulo.setFont(Font.font("System", 12));
        subtitulo.setTextFill(Color.web(TEXTO_SUAVE));

        TextField campoId         = crearTextField("ID de devolución");
        TextField campoProducto   = crearTextField("Nombre del producto");
        TextField campoTalla      = crearTextField("Talla");
        TextField campoCantidad   = crearTextField("Cantidad");
        TextField campoPrecioUnit = crearTextField("Precio unitario");
        TextField campoMotivo     = crearTextField("Motivo de la devolución");

        Label labelTipo = new Label("Tipo de venta original:");
        labelTipo.setTextFill(Color.web(TEXTO_SUAVE));
        labelTipo.setFont(Font.font("System", 12));

        ComboBox<String> selectorTipo = new ComboBox<>();
        selectorTipo.getItems().addAll("Menudeo", "Mayoreo");
        selectorTipo.setValue("Menudeo");
        selectorTipo.setMaxWidth(320);
        selectorTipo.setStyle(estiloInput());

        Label labelFecha = new Label("Fecha de devolución (dd/MM/yyyy):");
        labelFecha.setTextFill(Color.web(TEXTO_SUAVE));
        labelFecha.setFont(Font.font("System", 12));

        TextField campoFecha = crearTextField("dd/MM/yyyy  (vacío = hoy)");

        Label mensajeEstado = new Label("");
        mensajeEstado.setFont(Font.font("System", 12));

        Button btnGuardar = new Button("Guardar Devolución");
        btnGuardar.setMaxWidth(320);
        btnGuardar.setStyle("-fx-background-color: " + NARANJA + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 6; -fx-cursor: hand;");

        Button btnCancelar = new Button("← Regresar a lista");
        btnCancelar.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXTO_SUAVE + "; -fx-font-size: 12px; -fx-cursor: hand;");
        btnCancelar.setOnAction(e -> mostrarRegistroDevoluciones(contenido, true));

        btnGuardar.setOnAction(e -> {
            String id        = campoId.getText().trim();
            String producto  = campoProducto.getText().trim();
            String talla     = campoTalla.getText().trim();
            String cantStr   = campoCantidad.getText().trim();
            String precioStr = campoPrecioUnit.getText().trim();
            String motivo    = campoMotivo.getText().trim();
            String tipo      = selectorTipo.getValue();
            String fechaStr  = campoFecha.getText().trim();

            if (id.isEmpty() || producto.isEmpty() || cantStr.isEmpty() || precioStr.isEmpty()) {
                mensajeEstado.setTextFill(Color.web(ERROR));
                mensajeEstado.setText("ID, producto, cantidad y precio son obligatorios");
                return;
            }
            boolean yaExiste = listaDevolucionesRegistradas.stream().anyMatch(d -> d.getIdDevolucion().equals(id));
            if (yaExiste) {
                mensajeEstado.setTextFill(Color.web(ADVERTENCIA));
                mensajeEstado.setText("Ya existe una devolución con ese ID");
                return;
            }
            try {
                int cantidad      = Integer.parseInt(cantStr);
                double precioUnit = Double.parseDouble(precioStr);
                LocalDate fecha   = fechaStr.isEmpty() ? LocalDate.now() : LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                listaDevolucionesRegistradas.add(new DevolucionRegistrada(id, producto, talla, cantidad, tipo, precioUnit, fecha, motivo));
                mostrarRegistroDevoluciones(contenido, true);
            } catch (Exception ex) {
                mensajeEstado.setTextFill(Color.web(ERROR));
                mensajeEstado.setText("Verifica que los datos sean válidos");
            }
        });

        VBox form = new VBox(12, titulo, subtitulo, campoId, campoProducto, campoTalla,
                campoCantidad, campoPrecioUnit, labelTipo, selectorTipo,
                labelFecha, campoFecha, campoMotivo, mensajeEstado, btnGuardar, btnCancelar);
        form.setAlignment(Pos.TOP_LEFT);
        form.setMaxWidth(440);
        form.setStyle("-fx-background-color: " + PANEL + "; -fx-padding: 35; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3);");

        ScrollPane scroll = new ScrollPane(form);
        scroll.setFitToWidth(false);
        scroll.setFitToHeight(false);
        scroll.setStyle("-fx-background-color: " + FONDO + "; -fx-background: " + FONDO + ";");

        StackPane wrapper = new StackPane(scroll);
        wrapper.setStyle("-fx-background-color: " + FONDO + "; -fx-padding: 30;");
        wrapper.setAlignment(Pos.CENTER);
        contenido.getChildren().add(wrapper);
    }

    private void mostrarFormularioEditarDevolucion(StackPane contenido) {
        contenido.getChildren().clear();

        Label titulo    = new Label("Editar Registro de Devolución");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        Label subtitulo = new Label("Busca por ID de devolución o nombre del producto");
        subtitulo.setFont(Font.font("System", 12));
        subtitulo.setTextFill(Color.web(TEXTO_SUAVE));

        TextField campoBusqueda = crearTextField("ID de devolución o nombre del producto");
        Button btnBuscar = new Button("Buscar");
        btnBuscar.setStyle(estiloBtnPrincipal());

        Label mensajeBusqueda = new Label("");
        mensajeBusqueda.setFont(Font.font("System", 12));

        VBox panelEdicion = new VBox(10);
        panelEdicion.setVisible(false);
        panelEdicion.setManaged(false);

        TextField campoProducto   = crearTextField("Nombre del producto");
        TextField campoTalla      = crearTextField("Talla");
        TextField campoCantidad   = crearTextField("Cantidad");
        TextField campoPrecioUnit = crearTextField("Precio unitario");
        TextField campoFecha      = crearTextField("Fecha devolución dd/MM/yyyy");
        TextField campoMotivo     = crearTextField("Motivo");

        Label labelTipo = new Label("Tipo de venta original:");
        labelTipo.setTextFill(Color.web(TEXTO_SUAVE));
        labelTipo.setFont(Font.font("System", 12));

        ComboBox<String> selectorTipo = new ComboBox<>();
        selectorTipo.getItems().addAll("Menudeo", "Mayoreo");
        selectorTipo.setValue("Menudeo");
        selectorTipo.setMaxWidth(320);
        selectorTipo.setStyle(estiloInput());

        Label mensajeEstado = new Label("");
        mensajeEstado.setFont(Font.font("System", 12));

        Button btnGuardar = new Button("Guardar Cambios");
        btnGuardar.setMaxWidth(320);
        btnGuardar.setStyle("-fx-background-color: " + AZUL_EDITAR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 6; -fx-cursor: hand;");

        panelEdicion.getChildren().addAll(campoProducto, campoTalla, campoCantidad, campoPrecioUnit,
                labelTipo, selectorTipo, campoFecha, campoMotivo, mensajeEstado, btnGuardar);

        final DevolucionRegistrada[] devRef = {null};

        btnBuscar.setOnAction(e -> {
            String busqueda = campoBusqueda.getText().trim();
            if (busqueda.isEmpty()) {
                mensajeBusqueda.setTextFill(Color.web(ERROR));
                mensajeBusqueda.setText("Ingresa un ID o nombre");
                return;
            }
            DevolucionRegistrada dev = listaDevolucionesRegistradas.stream()
                .filter(d -> d.getIdDevolucion().equalsIgnoreCase(busqueda) || d.getNombreProducto().equalsIgnoreCase(busqueda))
                .findFirst().orElse(null);
            if (dev == null) {
                mensajeBusqueda.setTextFill(Color.web(ERROR));
                mensajeBusqueda.setText("No se encontró el registro");
                panelEdicion.setVisible(false); panelEdicion.setManaged(false);
            } else {
                devRef[0] = dev;
                mensajeBusqueda.setTextFill(Color.web(EXITO));
                mensajeBusqueda.setText("Registro encontrado");
                campoProducto.setText(dev.getNombreProducto());
                campoTalla.setText(dev.getTalla());
                campoCantidad.setText(String.valueOf(dev.getCantidad()));
                campoPrecioUnit.setText(String.valueOf(dev.getPrecioUnitario()));
                selectorTipo.setValue(dev.getTipoVenta());
                campoFecha.setText(dev.getFechaDevolucion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                campoMotivo.setText(dev.getMotivo());
                mensajeEstado.setText("");
                panelEdicion.setVisible(true); panelEdicion.setManaged(true);
            }
        });

        btnGuardar.setOnAction(e -> {
            if (devRef[0] == null) return;
            String producto  = campoProducto.getText().trim();
            String talla     = campoTalla.getText().trim();
            String cantStr   = campoCantidad.getText().trim();
            String precioStr = campoPrecioUnit.getText().trim();
            String fechaStr  = campoFecha.getText().trim();
            String motivo    = campoMotivo.getText().trim();
            String tipo      = selectorTipo.getValue();

            if (producto.isEmpty() || cantStr.isEmpty() || precioStr.isEmpty() || fechaStr.isEmpty()) {
                mensajeEstado.setTextFill(Color.web(ERROR));
                mensajeEstado.setText("Todos los campos son obligatorios");
                return;
            }
            try {
                int cantidad      = Integer.parseInt(cantStr);
                double precioUnit = Double.parseDouble(precioStr);
                LocalDate fecha   = LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                DevolucionRegistrada dev = devRef[0];
                dev.setNombreProducto(producto); dev.setTalla(talla);
                dev.setCantidad(cantidad); dev.setPrecioUnitario(precioUnit);
                dev.setTipoVenta(tipo); dev.setFechaDevolucion(fecha);
                dev.setMotivo(motivo);
                mensajeEstado.setTextFill(Color.web(EXITO));
                mensajeEstado.setText("Registro actualizado correctamente");
            } catch (Exception ex) {
                mensajeEstado.setTextFill(Color.web(ERROR));
                mensajeEstado.setText("Verifica que los datos sean válidos");
            }
        });

        Button btnCancelar = new Button("← Regresar a lista");
        btnCancelar.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXTO_SUAVE + "; -fx-font-size: 12px; -fx-cursor: hand;");
        btnCancelar.setOnAction(e -> mostrarRegistroDevoluciones(contenido, true));

        VBox form = new VBox(12, titulo, subtitulo, campoBusqueda, btnBuscar, mensajeBusqueda, panelEdicion, btnCancelar);
        form.setAlignment(Pos.TOP_LEFT);
        form.setMaxWidth(460);
        form.setStyle("-fx-background-color: " + PANEL + "; -fx-padding: 35; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3);");

        ScrollPane scroll = new ScrollPane(form);
        scroll.setFitToWidth(false);
        scroll.setFitToHeight(false);
        scroll.setStyle("-fx-background-color: " + FONDO + "; -fx-background: " + FONDO + ";");

        StackPane wrapper = new StackPane(scroll);
        wrapper.setStyle("-fx-background-color: " + FONDO + "; -fx-padding: 30;");
        wrapper.setAlignment(Pos.CENTER);
        contenido.getChildren().add(wrapper);
    }



    // ── CLASES MODELO ────────────────────────────────────────────────
    public static class Usuario {
        private String nombre, usuario, password, rol;
        public Usuario(String nombre, String usuario, String password, String rol) {
            this.nombre = nombre; this.usuario = usuario; this.password = password; this.rol = rol;
        }
        public String getNombre()   { return nombre; }
        public String getUsuario()  { return usuario; }
        public String getPassword() { return password; }
        public String getRol()      { return rol; }
    }

public static class DevolucionRegistrada {
        private String idDevolucion, nombreProducto, talla, tipoVenta, motivo;
        private int cantidad;
        private double precioUnitario;
        private LocalDate fechaDevolucion;

        public DevolucionRegistrada(String idDevolucion, String nombreProducto, String talla,
                                    int cantidad, String tipoVenta, double precioUnitario,
                                    LocalDate fechaDevolucion, String motivo) {
            this.idDevolucion  = idDevolucion;  this.nombreProducto = nombreProducto;
            this.talla         = talla;          this.cantidad       = cantidad;
            this.tipoVenta     = tipoVenta;      this.precioUnitario = precioUnitario;
            this.fechaDevolucion = fechaDevolucion; this.motivo       = motivo;
        }

        public String    getIdDevolucion()   { return idDevolucion; }
        public String    getNombreProducto() { return nombreProducto; }
        public String    getTalla()          { return talla; }
        public int       getCantidad()       { return cantidad; }
        public String    getTipoVenta()      { return tipoVenta; }
        public double    getPrecioUnitario() { return precioUnitario; }
        public double    getTotal()          { return cantidad * precioUnitario; }
        public LocalDate getFechaDevolucion(){ return fechaDevolucion; }
        public String    getMotivo()         { return motivo; }

        public void setNombreProducto(String n)    { this.nombreProducto  = n; }
        public void setTalla(String t)             { this.talla           = t; }
        public void setCantidad(int c)             { this.cantidad        = c; }
        public void setTipoVenta(String t)         { this.tipoVenta       = t; }
        public void setPrecioUnitario(double p)    { this.precioUnitario  = p; }
        public void setFechaDevolucion(LocalDate f){ this.fechaDevolucion = f; }
        public void setMotivo(String m)            { this.motivo          = m; }
    }

public static class Prenda {
        private String nombre, id, talla, tipoPrenda, idTienda, descripcion;
        private int existencia, minimoExistencia;
        private double precioMayoreo, precioMenudeo;

        public Prenda(String nombre, String id, String talla, String tipoPrenda,
                      int existencia, double precioMayoreo, double precioMenudeo,
                      String idTienda, String descripcion) {
            this.nombre = nombre; this.id = id; this.talla = talla;
            this.tipoPrenda = tipoPrenda; this.existencia = existencia;
            this.precioMayoreo = precioMayoreo; this.precioMenudeo = precioMenudeo;
            this.idTienda = idTienda; this.descripcion = descripcion;
            this.minimoExistencia = 5;
        }

        public String getNombre()        { return nombre; }
        public String getId()            { return id; }
        public String getTalla()         { return talla; }
        public String getTipoPrenda()    { return tipoPrenda; }
        public int    getExistencia()    { return existencia; }
        public int    getMinimoExistencia() { return minimoExistencia; }
        public double getPrecioMayoreo() { return precioMayoreo; }
        public double getPrecioMenudeo() { return precioMenudeo; }
        public String getIdTienda()      { return idTienda; }
        public String getDescripcion()   { return descripcion; }

        public void setNombre(String n)           { this.nombre = n; }
        public void setTalla(String t)            { this.talla = t; }
        public void setTipoPrenda(String t)       { this.tipoPrenda = t; }
        public void setIdTienda(String t)         { this.idTienda = t; }
        public void setDescripcion(String d)      { this.descripcion = d; }
        public void setExistencia(int e)          { this.existencia = e; }
        public void setMinimoExistencia(int m)    { this.minimoExistencia = m; }
        public void setPrecioMayoreo(double p)    { this.precioMayoreo = p; }
        public void setPrecioMenudeo(double p)    { this.precioMenudeo = p; }
    }

public static class Conjunto {
        private String id, nombre, descripcion;
        private List<String> idPrendas;
        private int piezas, minimoExistencia;

        public Conjunto(String id, String nombre, String descripcion, List<String> idPrendas, int piezas) {
            this.id = id; this.nombre = nombre; this.descripcion = descripcion;
            this.idPrendas = idPrendas; this.piezas = piezas;
            this.minimoExistencia = 3;
        }

        public String       getId()                 { return id; }
        public String       getNombre()             { return nombre; }
        public String       getDescripcion()        { return descripcion; }
        public List<String> getIdPrendas()          { return idPrendas; }
        public int          getPiezas()             { return piezas; }
        public int          getMinimoExistencia()   { return minimoExistencia; }

        public void setNombre(String n)             { this.nombre = n; }
        public void setDescripcion(String d)        { this.descripcion = d; }
        public void setIdPrendas(List<String> l)    { this.idPrendas = l; }
        public void setPiezas(int p)                { this.piezas = p; }
        public void setMinimoExistencia(int m)      { this.minimoExistencia = m; }
    }

    public static class PrendaVendida {
        private String idVenta, nombrePrenda, talla, tipoVenta, descripcion;
        private int cantidad;
        private double precioUnitario;
        private LocalDate fechaVenta, fechaLimiteDevolucion;

        public PrendaVendida(String idVenta, String nombrePrenda, String talla, int cantidad,
                             String tipoVenta, double precioUnitario,
                             LocalDate fechaVenta, LocalDate fechaLimiteDevolucion, String descripcion) {
            this.idVenta = idVenta; this.nombrePrenda = nombrePrenda; this.talla = talla;
            this.cantidad = cantidad; this.tipoVenta = tipoVenta; this.precioUnitario = precioUnitario;
            this.fechaVenta = fechaVenta; this.fechaLimiteDevolucion = fechaLimiteDevolucion;
            this.descripcion = descripcion;
        }

        public String    getIdVenta()               { return idVenta; }
        public String    getNombrePrenda()           { return nombrePrenda; }
        public String    getTalla()                  { return talla; }
        public int       getCantidad()               { return cantidad; }
        public String    getTipoVenta()              { return tipoVenta; }
        public double    getPrecioUnitario()         { return precioUnitario; }
        public double    getTotal()                  { return cantidad * precioUnitario; }
        public LocalDate getFechaVenta()             { return fechaVenta; }
        public LocalDate getFechaLimiteDevolucion()  { return fechaLimiteDevolucion; }
        public String    getDescripcion()            { return descripcion; }

        public void setNombrePrenda(String n)             { this.nombrePrenda = n; }
        public void setTalla(String t)                    { this.talla = t; }
        public void setCantidad(int c)                    { this.cantidad = c; }
        public void setTipoVenta(String t)                { this.tipoVenta = t; }
        public void setPrecioUnitario(double p)           { this.precioUnitario = p; }
        public void setFechaVenta(LocalDate f)            { this.fechaVenta = f; }
        public void setFechaLimiteDevolucion(LocalDate f) { this.fechaLimiteDevolucion = f; }
        public void setDescripcion(String d)              { this.descripcion = d; }
    }

    public static class ConjuntoVendido {
        private String idVenta, nombreConjunto, tipoVenta, descripcion;
        private int cantidad;
        private double precioUnitario;
        private LocalDate fechaVenta, fechaLimiteDevolucion;
        private List<String> nombresPrendas;

        public ConjuntoVendido(String idVenta, String nombreConjunto, int cantidad,
                               String tipoVenta, double precioUnitario,
                               LocalDate fechaVenta, LocalDate fechaLimiteDevolucion,
                               String descripcion, List<String> nombresPrendas) {
            this.idVenta = idVenta; this.nombreConjunto = nombreConjunto; this.cantidad = cantidad;
            this.tipoVenta = tipoVenta; this.precioUnitario = precioUnitario;
            this.fechaVenta = fechaVenta; this.fechaLimiteDevolucion = fechaLimiteDevolucion;
            this.descripcion = descripcion; this.nombresPrendas = nombresPrendas;
        }

        public String       getIdVenta()               { return idVenta; }
        public String       getNombreConjunto()        { return nombreConjunto; }
        public int          getCantidad()              { return cantidad; }
        public String       getTipoVenta()             { return tipoVenta; }
        public double       getPrecioUnitario()        { return precioUnitario; }
        public double       getTotal()                 { return cantidad * precioUnitario; }
        public LocalDate    getFechaVenta()            { return fechaVenta; }
        public LocalDate    getFechaLimiteDevolucion() { return fechaLimiteDevolucion; }
        public String       getDescripcion()           { return descripcion; }
        public List<String> getNombresPrendas()        { return nombresPrendas; }

        public void setNombreConjunto(String n)            { this.nombreConjunto = n; }
        public void setCantidad(int c)                     { this.cantidad = c; }
        public void setTipoVenta(String t)                 { this.tipoVenta = t; }
        public void setPrecioUnitario(double p)            { this.precioUnitario = p; }
        public void setFechaVenta(LocalDate f)             { this.fechaVenta = f; }
        public void setFechaLimiteDevolucion(LocalDate f)  { this.fechaLimiteDevolucion = f; }
        public void setDescripcion(String d)               { this.descripcion = d; }
        public void setNombresPrendas(List<String> l)      { this.nombresPrendas = l; }
    }

    public static class ItemVenta {
        private String nombreProducto, tipoVenta;
        private int cantidad;
        private double precioUnitario;
        private Prenda prenda;
        private Conjunto conjunto;

        public ItemVenta(String nombreProducto, int cantidad, double precioUnitario,
                         String tipoVenta, Prenda prenda, Conjunto conjunto) {
            this.nombreProducto = nombreProducto; this.cantidad = cantidad;
            this.precioUnitario = precioUnitario; this.tipoVenta = tipoVenta;
            this.prenda = prenda; this.conjunto = conjunto;
        }

        public String   getNombreProducto() { return nombreProducto; }
        public int      getCantidad()       { return cantidad; }
        public double   getPrecioUnitario() { return precioUnitario; }
        public String   getTipoVenta()      { return tipoVenta; }
        public double   getSubtotal()       { return cantidad * precioUnitario; }
        public Prenda   getPrenda()         { return prenda; }
        public Conjunto getConjunto()       { return conjunto; }
        public void     setCantidad(int c)  { this.cantidad = c; }
    }

    public static class MateriaPrima {
        private int id, existencia;
        private String numeroPartida, tipoExistencia, descripcion, nombre;
        private String color, medida, composicion, tipo, tamanio, talla, material, tipoInsumo;
        private Double ancho;
        private Integer no;

        private int minimoExistencia;

        public MateriaPrima(int id, String numeroPartida, int existencia, String tipoExistencia,
                            String descripcion, String nombre, String color, String medida,
                            Double ancho, String composicion, String tipo, Integer no,
                            String tamanio, String talla, String material, String tipoInsumo) {
            this.id = id; this.numeroPartida = numeroPartida; this.existencia = existencia;
            this.tipoExistencia = tipoExistencia; this.descripcion = descripcion;
            this.nombre = nombre; this.color = color; this.medida = medida;
            this.ancho = ancho; this.composicion = composicion; this.tipo = tipo;
            this.no = no; this.tamanio = tamanio; this.talla = talla;
            this.material = material; this.tipoInsumo = tipoInsumo;
            this.minimoExistencia = 10;
        }

        public int     getId()             { return id; }
        public String  getNumeroPartida()  { return numeroPartida; }
        public int     getExistencia()     { return existencia; }
        public String  getTipoExistencia() { return tipoExistencia; }
        public String  getDescripcion()    { return descripcion; }
        public String  getNombre()         { return nombre; }
        public String  getColor()          { return color; }
        public String  getMedida()         { return medida; }
        public Double  getAncho()          { return ancho; }
        public String  getComposicion()    { return composicion; }
        public String  getTipo()           { return tipo; }
        public Integer getNo()             { return no; }
        public String  getTamanio()        { return tamanio; }
        public String  getTalla()          { return talla; }
        public String  getMaterial()       { return material; }
        public String  getTipoInsumo()     { return tipoInsumo; }

        public void setNombre(String n)         { this.nombre = n; }
        public void setExistencia(int e)        { this.existencia = e; }
        public void setTipoExistencia(String t) { this.tipoExistencia = t; }
        public void setDescripcion(String d)    { this.descripcion = d; }
        public void setColor(String c)          { this.color = c; }
        public void setMedida(String m)         { this.medida = m; }
        public void setAncho(Double a)          { this.ancho = a; }
        public void setComposicion(String c)    { this.composicion = c; }
        public void setTipo(String t)           { this.tipo = t; }
        public void setNo(Integer n)            { this.no = n; }
        public void setTamanio(String t)        { this.tamanio = t; }
        public void setTalla(String t)          { this.talla = t; }
        public void setMaterial(String m)       { this.material = m; }
        public void setTipoInsumo(String t)     { this.tipoInsumo = t; }
        public int  getMinimoExistencia()   { return minimoExistencia; }
        public void setMinimoExistencia(int m) { this.minimoExistencia = m; }
    }
}