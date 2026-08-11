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
import dao.ConjuntoDAO;
import dao.DetalleVentaConjuntoDAO;
import dao.DetalleVentaPrendaDAO;
import dao.DevolucionConjuntoDAO;
import dao.DevolucionPrendaDAO;
import dao.InsumoDAO;
import dao.InsumoPrendaDAO;
import dao.PrendaConjuntoDAO;
import dao.PrendaDAO;
import dao.UsuarioDAO;
import dao.VentaDAO;
import model.Conjunto;
import model.ConjuntoVendido;
import model.DetalleVentaConjunto;
import model.DetalleVentaPrenda;
import model.DevolucionConjunto;
import model.DevolucionPrenda;
import model.DevolucionConjuntoVista;
import model.DevolucionVista;
import model.Insumo;
import model.InsumoPrenda;
import model.ItemVenta;
import model.MaterialPorPrenda;
import model.Prenda;
import model.PrendaConjunto;
import model.PrendaVendida;
import model.Usuario;
import model.Venta;
import model.VentaResumen;
import model.Tienda;
import dao.TiendaDAO;
import model.Ubicacion;
import dao.UbicacionDAO;
import database.Conexion;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import java.sql.Connection;

public class App extends Application {

    private Stage stage;
    private String rolActual;
    private Scene scenePrincipal;


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
    private java.util.Deque<Runnable> historialNavegacion = new java.util.ArrayDeque<>();
    private javafx.beans.property.SimpleBooleanProperty hayHistorial = 
    new javafx.beans.property.SimpleBooleanProperty(false);    private PrendaDAO prendaDAO = new PrendaDAO();
    private InsumoDAO insumoDAO = new InsumoDAO();
    private ConjuntoDAO conjuntoDAO = new ConjuntoDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private VentaDAO ventaDAO = new VentaDAO();
    private DetalleVentaPrendaDAO detallePrendaDAO = new DetalleVentaPrendaDAO();
    private DetalleVentaConjuntoDAO detalleConjuntoDAO = new DetalleVentaConjuntoDAO();
    private DevolucionPrendaDAO devolucionPrendaDAO = new DevolucionPrendaDAO();
    private DevolucionConjuntoDAO devolucionConjuntoDAO = new DevolucionConjuntoDAO();
    private PrendaConjuntoDAO prendaConjuntoDAO = new PrendaConjuntoDAO();
    private InsumoPrendaDAO insumoPrendaDAO = new InsumoPrendaDAO();

    private ObservableList<Prenda> listaPrendas = FXCollections.observableArrayList();
    private ObservableList<Insumo> listaMateriaPrima = FXCollections.observableArrayList();
    private ObservableList<Conjunto> listaConjuntos = FXCollections.observableArrayList();
    private ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();
    private ObservableList<MaterialPorPrenda> listaMaterialesPorPrenda = FXCollections.observableArrayList();
    private ObservableList<Tienda> listaTiendas = FXCollections.observableArrayList();
    private ObservableList<Ubicacion> listaUbicaciones = FXCollections.observableArrayList();

    private TiendaDAO tiendaDAO = new TiendaDAO();
    private UbicacionDAO ubicacionDAO = new UbicacionDAO();

    // ------------------LOGICA CONJUNTOS--------------- 
    private void verificarConjuntos() {
        listaConjuntos.removeIf(c -> calcularExistenciaConjunto(c) <= 0);
    }

    private int calcularExistenciaConjunto(Conjunto c) {
        try {
            List<PrendaConjunto> relaciones = prendaConjuntoDAO.getByConjunto(c.getId());
            int minExistencia = Integer.MAX_VALUE;
            for (PrendaConjunto pc : relaciones) {
                Prenda p = prendaDAO.getById(pc.getIdPrenda());
                if (p != null) {
                    minExistencia = Math.min(minExistencia, p.getExistencia());
                } else {
                    return 0;
                }
            }
            return minExistencia == Integer.MAX_VALUE ? 0 : minExistencia;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

private Double obtenerCantidadMaterial(String idPrenda, String idMateriaPrima) {
    return listaMaterialesPorPrenda.stream()
        .filter(m -> m.getIdPrenda().equals(idPrenda) && m.getIdMateriaPrima().equals(idMateriaPrima))
        .map(MaterialPorPrenda::getCantidad)
        .findFirst().orElse(null);
}

    private String nombrePrendaConTalla(String idPrenda) {
        return listaPrendas.stream()
            .filter(p -> String.valueOf(p.getId()).equals(idPrenda))
            .findFirst()
            .map(p -> p.getNombre() + " (" + p.getTalla() + ")")
            .orElse("ID " + idPrenda);
    }

    private double calcularPrecioConjunto(Conjunto c) {
        try {
            return prendaConjuntoDAO.getByConjunto(c.getId()).stream()
                .map(PrendaConjunto::getIdPrenda)
                .map(idPrenda -> {
                    try {
                        return prendaDAO.getById(idPrenda);
                    } catch (SQLException e) {
                        return null;
                    }
                })
                .filter(p -> p != null)
                .mapToDouble(Prenda::getPrecioMenudeo)
                .sum();
        } catch (SQLException e) {
            return 0.0;
        }
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setTitle("Punto Olayma");
        recargarDatos();
        mostrarBienvenida();
        stage.setMaximized(true);
        historialNavegacion.clear();
        stage.setMaximized(true);
        stage.show();
    }

    private void recargarDatos() {
        try {
            listaPrendas.clear();
            listaPrendas.addAll(prendaDAO.getAll());

            listaMateriaPrima.clear();
            listaMateriaPrima.addAll(insumoDAO.getAll());

            listaConjuntos.clear();
            listaConjuntos.addAll(conjuntoDAO.getAll());
            for (Conjunto conjunto : listaConjuntos) {
                List<String> idsPrendas = new ArrayList<>();
                for (PrendaConjunto pc : prendaConjuntoDAO.getByConjunto(conjunto.getId())) {
                    idsPrendas.add(String.valueOf(pc.getIdPrenda()));
                }
                conjunto.setIdPrendas(idsPrendas);
            }

            listaUsuarios.clear();
            listaUsuarios.addAll(usuarioDAO.getAll());
            listaTiendas.clear();
            listaTiendas.addAll(tiendaDAO.encontrarTodo());
            listaUbicaciones.clear();
            listaUbicaciones.addAll(ubicacionDAO.encontrarTodo());
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarError("Error al cargar datos: " + e.getMessage());
        }
    }

    private void recargarMaterialesPorPrendaDesdeBD() throws SQLException {
        listaMaterialesPorPrenda.clear();
        for (InsumoPrenda ip : insumoPrendaDAO.getAll()) {
            listaMaterialesPorPrenda.add(new MaterialPorPrenda(
                String.valueOf(ip.getIdPrenda()),
                ip.getIdInsumo(),
                ip.getCantidadInsumo()
            ));
        }
    }

    private ObservableList<PrendaVendida> cargarPrendasVendidasDesdeBD() throws SQLException {
        return ventaDAO.obtenerTodasLasVentas();
    }

    private ObservableList<ConjuntoVendido> cargarConjuntosVendidosDesdeBD() throws SQLException {
        ObservableList<ConjuntoVendido> ventas = FXCollections.observableArrayList();
        for (Venta venta : ventaDAO.getAll()) {
            List<DetalleVentaConjunto> detalles = detalleConjuntoDAO.getByVenta(venta.getFolio());
            for (DetalleVentaConjunto detalle : detalles) {
                Conjunto conjunto = conjuntoDAO.getById(detalle.getIdConjunto());
                if (conjunto == null) continue;

                List<String> nombresPrendas = new ArrayList<>();
                for (PrendaConjunto pc : prendaConjuntoDAO.getByConjunto(conjunto.getId())) {
                    Prenda p = prendaDAO.getById(pc.getIdPrenda());
                    if (p != null) {
                        nombresPrendas.add(p.getNombre() + " (Talla " + p.getTalla() + ")");
                    }
                }

                double precioUnitario = detalle.getCantidad() > 0 ? detalle.getTotal() / detalle.getCantidad() : 0.0;
                LocalDate fechaVenta = venta.getFecha();
                ventas.add(new ConjuntoVendido(
                    String.valueOf(venta.getFolio()),
                    conjunto.getNombre(),
                    detalle.getCantidad(),
                    "Menudeo",
                    precioUnitario,
                    fechaVenta,
                    fechaVenta.plusDays(DIAS_DEVOLUCION),
                    "Folio " + venta.getFolio(),
                    nombresPrendas
                ));
            }
        }
        return ventas;
    }

    private PrendaVendida buscarPrendaVendidaEnBD(String busqueda) throws SQLException {
        return cargarPrendasVendidasDesdeBD().stream()
            .filter(x -> x.getIdVenta().equalsIgnoreCase(busqueda) || x.getNombrePrenda().equalsIgnoreCase(busqueda))
            .findFirst().orElse(null);
    }

    private ConjuntoVendido buscarConjuntoVendidoEnBD(String busqueda) throws SQLException {
        return cargarConjuntosVendidosDesdeBD().stream()
            .filter(x -> x.getIdVenta().equalsIgnoreCase(busqueda) || x.getNombreConjunto().equalsIgnoreCase(busqueda))
            .findFirst().orElse(null);
    }

    //----------------------- BIENVENIDA ---------------------------
    private void mostrarBienvenida() {
        // Panel izquierdo — decorativo con gradiente oscuro
        limpiarHistorial();
        VBox panelIzquierdo = new VBox();
        panelIzquierdo.setPrefWidth(420);
        panelIzquierdo.setAlignment(Pos.CENTER);
        panelIzquierdo.setStyle("-fx-background-color: " + SECUNDARIO + ";");

        // Círculos decorativos de fondo
        javafx.scene.shape.Circle circulo1 = new javafx.scene.shape.Circle(180);
        circulo1.setFill(Color.web(PRINCIPAL, 0.08));
        circulo1.setTranslateY(-80);
        circulo1.setTranslateX(-60);

        javafx.scene.shape.Circle circulo2 = new javafx.scene.shape.Circle(120);
        circulo2.setFill(Color.web(PRINCIPAL, 0.12));
        circulo2.setTranslateY(100);
        circulo2.setTranslateX(80);

        javafx.scene.shape.Circle circulo3 = new javafx.scene.shape.Circle(60);
        circulo3.setFill(Color.web(PRINCIPAL_ALT, 0.15));
        circulo3.setTranslateY(-20);
        circulo3.setTranslateX(120);

        // Ícono central — letra T estilizada
        Label icono = new Label("T");
        icono.setFont(Font.font("System", FontWeight.BOLD, 72));
        icono.setTextFill(Color.WHITE);
        icono.setStyle(
            "-fx-background-color: " + PRINCIPAL + "; -fx-background-radius: 24;" +
            "-fx-padding: 10 28 10 28;");

        Label nombreSistema = new Label("Punto Olayma");
        nombreSistema.setFont(Font.font("System", FontWeight.BOLD, 28));
        nombreSistema.setTextFill(Color.WHITE);
        nombreSistema.setStyle("-fx-padding: 20 0 6 0;");

        Label versionLabel = new Label("v1.0  —  Gestión Integral");
        versionLabel.setFont(Font.font("System", 13));
        versionLabel.setTextFill(Color.web("#64748B"));

        // Línea decorativa
        Region linea = new Region();
        linea.setPrefHeight(3);
        linea.setPrefWidth(60);
        linea.setStyle("-fx-background-color: " + PRINCIPAL + "; -fx-background-radius: 2;");

        VBox contenidoIzq = new VBox(12, icono, nombreSistema, linea, versionLabel);
        contenidoIzq.setAlignment(Pos.CENTER);
        contenidoIzq.setStyle("-fx-padding: 40;");

        StackPane stackIzq = new StackPane(circulo1, circulo2, circulo3, contenidoIzq);
        panelIzquierdo.getChildren().add(stackIzq);
        VBox.setVgrow(stackIzq, Priority.ALWAYS);

        // Panel derecho — bienvenida
        VBox panelDerecho = new VBox();
        panelDerecho.setAlignment(Pos.CENTER);
        panelDerecho.setStyle("-fx-background-color: " + FONDO + "; -fx-padding: 60 50 60 50;");
        HBox.setHgrow(panelDerecho, Priority.ALWAYS);

        Label saludo = new Label("Bienvenido");
        saludo.setFont(Font.font("System", FontWeight.BOLD, 36));
        saludo.setTextFill(Color.web(SECUNDARIO));

        Label descripcion = new Label("Plataforma de inventario y punto de venta\npara la industria textil.");
        descripcion.setFont(Font.font("System", 14));
        descripcion.setTextFill(Color.web(TEXTO_SUAVE));
        descripcion.setStyle("-fx-text-alignment: center; -fx-padding: 10 0 30 0;");
        descripcion.setWrapText(true);

        // Tres tarjetas de características
        HBox tarjetas = new HBox(12,
            crearTarjetaBienvenida("📦", "Inventario", "Control de prendas,\nconjuntos y materias primas"),
            crearTarjetaBienvenida("🧾", "Ventas", "Punto de venta,\nrecibos y devoluciones"),
            crearTarjetaBienvenida("📊", "Reportes", "Alertas de stock\ny seguimiento de usuarios")
        );
        tarjetas.setAlignment(Pos.CENTER);
        tarjetas.setStyle("-fx-padding: 0 0 36 0;");

        Button btnEntrar = new Button("Entrar al sistema  →");
        btnEntrar.setStyle(
            "-fx-background-color: " + PRINCIPAL + "; -fx-text-fill: white; -fx-font-weight: bold;" +
            "-fx-font-size: 15px; -fx-padding: 14 36; -fx-background-radius: 8; -fx-cursor: hand;");
        btnEntrar.setOnMouseEntered(e -> btnEntrar.setStyle(
            "-fx-background-color: " + PRINCIPAL_ALT + "; -fx-text-fill: white; -fx-font-weight: bold;" +
            "-fx-font-size: 15px; -fx-padding: 14 36; -fx-background-radius: 8; -fx-cursor: hand;"));
        btnEntrar.setOnMouseExited(e -> btnEntrar.setStyle(
            "-fx-background-color: " + PRINCIPAL + "; -fx-text-fill: white; -fx-font-weight: bold;" +
            "-fx-font-size: 15px; -fx-padding: 14 36; -fx-background-radius: 8; -fx-cursor: hand;"));
        btnEntrar.setOnAction(e -> mostrarLogin());

        Label pie = new Label("© 2026 Punto Olayma  —  Todos los derechos reservados");
        pie.setFont(Font.font("System", 11));
        pie.setTextFill(Color.web("#CBD5E1"));
        pie.setStyle("-fx-padding: 30 0 0 0;");

        panelDerecho.getChildren().addAll(saludo, descripcion, tarjetas, btnEntrar, pie);

        HBox root = new HBox(panelIzquierdo, panelDerecho);
        HBox.setHgrow(panelDerecho, Priority.ALWAYS);
        cambiarEscena(root);
        stage.setMaximized(true);
    }

    private VBox crearTarjetaBienvenida(String emoji, String titulo, String descripcion) {
        Label lEmoji = new Label(emoji);
        lEmoji.setFont(Font.font("System", 26));

        Label lTitulo = new Label(titulo);
        lTitulo.setFont(Font.font("System", FontWeight.BOLD, 13));
        lTitulo.setTextFill(Color.web(SECUNDARIO));

        Label lDesc = new Label(descripcion);
        lDesc.setFont(Font.font("System", 11));
        lDesc.setTextFill(Color.web(TEXTO_SUAVE));
        lDesc.setWrapText(true);
        lDesc.setStyle("-fx-text-alignment: center;");

        VBox tarjeta = new VBox(8, lEmoji, lTitulo, lDesc);
        tarjeta.setAlignment(Pos.TOP_CENTER);
        tarjeta.setPrefWidth(150);
        tarjeta.setStyle(
            "-fx-background-color: " + PANEL + "; -fx-padding: 18 14; -fx-background-radius: 10;" +
            "-fx-border-color: #E5E7EB; -fx-border-radius: 10;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 10, 0, 0, 2);");
        return tarjeta;
    }


    // ----------------------- LOGIN ---------------------------
    private void mostrarLogin() {
        historialNavegacion.clear();
        VBox panelIzquierdo = new VBox();
        panelIzquierdo.setPrefWidth(260);
        panelIzquierdo.setStyle("-fx-background-color: " + SECUNDARIO + ";");
        panelIzquierdo.setAlignment(Pos.CENTER);

        Label marcaNombre = new Label("Punto\nOlayma");
        marcaNombre.setFont(Font.font("System", FontWeight.BOLD, 32));
        marcaNombre.setTextFill(Color.WHITE);
        marcaNombre.setStyle("-fx-padding: 0 0 12 0;");

        Label marcaDesc = new Label("Gestion de inventario\ny punto de venta");
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

        Label subtitulo = new Label("Inicia sesion para continuar");
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

        Label labelPass = new Label("Contrasena");
        labelPass.setFont(Font.font("System", FontWeight.BOLD, 12));
        labelPass.setTextFill(Color.web(TEXTO));

        PasswordField campoContrasena = new PasswordField();
        campoContrasena.setPromptText("Ingresa tu contrasena");
        campoContrasena.setMaxWidth(Double.MAX_VALUE);
        campoContrasena.setStyle(estiloInput());

        Label mensajeError = new Label("");
        mensajeError.setTextFill(Color.web(ERROR));
        mensajeError.setFont(Font.font("System", 12));

        Button btnEntrar = new Button("Iniciar Sesion");
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
                mensajeError.setText("Usuario o contrasena incorrectos");
                campoContrasena.clear();
            }
        });

        VBox form = new VBox(10, titulo, subtitulo, lineaBox,
            labelUsuario, campoUsuario, labelPass, campoContrasena, mensajeError, btnEntrar);
        form.setAlignment(Pos.CENTER_LEFT);
        form.setStyle("-fx-padding: 50 45 50 45; -fx-background-color: " + PANEL + ";");
        VBox.setVgrow(form, Priority.ALWAYS);

        HBox root = new HBox(panelIzquierdo, form);
        Button btnRegresar = new Button("← Volver");
        btnRegresar.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXTO_SUAVE + "; -fx-font-size: 12px; -fx-cursor: hand;");
        btnRegresar.setOnAction(e -> mostrarBienvenida());
        form.getChildren().add(btnRegresar);
        HBox.setHgrow(form, Priority.ALWAYS);
        root.setStyle("-fx-background-color: " + PANEL + ";");
        cambiarEscena(root);
stage.setMaximized(true);
    }

    // ?"??"? MEN?sS ?"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"?
    private void mostrarMenuAdministrador() { mostrarMenu("Administrador", true); }
    private void mostrarMenuEncargado()     { mostrarMenu("Encargado", false); }

    private void mostrarMenu(String rol, boolean esAdmin) {
    limpiarHistorial(); 
    String colorSidebar = esAdmin ? SECUNDARIO     : SECUNDARIO_ALT;
    String colorLogo    = esAdmin ? PRINCIPAL      : PRINCIPAL_ALT;
    String colorHover   = esAdmin ? PRINCIPAL      : PRINCIPAL_ALT;

    VBox sidebar = new VBox(4);
    sidebar.setPrefWidth(230);
    sidebar.setStyle("-fx-background-color: " + colorSidebar + "; -fx-padding: 0;");

    Label logoLabel = new Label("Punto Olayma");
    logoLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
    logoLabel.setTextFill(Color.WHITE);
    logoLabel.setStyle("-fx-background-color: " + colorLogo + "; -fx-padding: 20 16 20 16; -fx-max-width: infinity;");
    logoLabel.setMaxWidth(Double.MAX_VALUE);

    // Puntito de alerta con color dinamico
    String colorAlerta = esAdmin ? calcularColorAlerta() : calcularColorAlertaEncargado();
    javafx.scene.shape.Circle puntoAlerta = new javafx.scene.shape.Circle(5);
    puntoAlerta.setFill(Color.web(colorAlerta));

    Label rolLabel = new Label(rol.toUpperCase());
    rolLabel.setFont(Font.font("System", 11));
    rolLabel.setTextFill(Color.web("#94A3B8"));

    HBox filaRol = new HBox(6, puntoAlerta, rolLabel);
    filaRol.setAlignment(Pos.CENTER_LEFT);
    filaRol.setStyle("-fx-padding: 12 16 8 16;");

    sidebar.getChildren().addAll(logoLabel, filaRol);

    StackPane contenido = crearContenidoVacio();
    VBox.setVgrow(contenido, Priority.ALWAYS);

    if (esAdmin) {
        sidebar.getChildren().add(crearSeccionMenu("INVENTARIO"));
        Button btnMP  = crearBotonMenuColor("Materia Prima", colorHover);
        Button btnPr  = crearBotonMenuColor("Prendas Fabricadas", colorHover);
        Button btnCj  = crearBotonMenuColor("Conjuntos", colorHover);
        sidebar.getChildren().addAll(btnMP, btnPr, btnCj);
        Button btnMxP = crearBotonMenuColor("Materiales por Prenda", colorHover);
        sidebar.getChildren().add(btnMxP);
        btnMxP.setOnAction(e -> { pushHistorial(() -> mostrarContenidoVacioEnMenu(contenido)); mostrarMaterialesPorPrenda(contenido); });
        btnMP.setOnAction(e  -> { pushHistorial(() -> mostrarContenidoVacioEnMenu(contenido)); mostrarModuloMateriaPrima(contenido, true); });
        btnPr.setOnAction(e  -> { pushHistorial(() -> mostrarContenidoVacioEnMenu(contenido)); mostrarModuloPrendas(contenido, true); });
        btnCj.setOnAction(e  -> { pushHistorial(() -> mostrarContenidoVacioEnMenu(contenido)); mostrarModuloConjuntos(contenido, true); });

        sidebar.getChildren().add(crearSeccionMenu("VENTAS"));
        Button btnPV  = crearBotonMenuColor("Punto de Venta", colorHover);
        Button btnPVd = crearBotonMenuColor("Prendas Vendidas", colorHover);
        Button btnCVd = crearBotonMenuColor("Conjuntos Vendidos", colorHover);
        Button btnDev    = crearBotonMenuColor("Devoluciones", colorHover);
        Button btnRegDev = crearBotonMenuColor("Registro de Devoluciones", colorHover);
        sidebar.getChildren().addAll(btnPV, btnPVd, btnCVd, btnDev, btnRegDev);
       btnPV.setOnAction(e     -> { pushHistorial(() -> mostrarContenidoVacioEnMenu(contenido)); mostrarPuntoDeVenta(contenido); });
        btnPVd.setOnAction(e    -> { pushHistorial(() -> mostrarContenidoVacioEnMenu(contenido)); mostrarModuloPrendasVendidas(contenido, true); });
        btnCVd.setOnAction(e    -> { pushHistorial(() -> mostrarContenidoVacioEnMenu(contenido)); mostrarModuloConjuntosVendidos(contenido, true); });
        btnDev.setOnAction(e    -> { pushHistorial(() -> mostrarContenidoVacioEnMenu(contenido)); mostrarDevoluciones(contenido); });
        btnRegDev.setOnAction(e -> { pushHistorial(() -> mostrarContenidoVacioEnMenu(contenido)); mostrarRegistroDevoluciones(contenido, true); });

        sidebar.getChildren().add(crearSeccionMenu("ADMINISTRACION"));
        Button btnUs  = crearBotonMenuColor("Usuarios", colorHover);
        Button btnAl  = crearBotonMenuColor("Alertas de Stock", colorHover);
        sidebar.getChildren().addAll(btnUs, btnAl);
       btnUs.setOnAction(e -> { pushHistorial(() -> mostrarContenidoVacioEnMenu(contenido)); mostrarModuloUsuarios(contenido); });
        btnAl.setOnAction(e -> { pushHistorial(() -> mostrarContenidoVacioEnMenu(contenido)); mostrarAlertasStock(contenido, true); });
        Button btnTU  = crearBotonMenuColor("Tiendas y Ubicaciones", colorHover);
        sidebar.getChildren().add(btnTU);
        btnTU.setOnAction(e -> { pushHistorial(() -> mostrarContenidoVacioEnMenu(contenido)); mostrarModuloTiendasUbicaciones(contenido, true); });
    } else {
        sidebar.getChildren().add(crearSeccionMenu("INVENTARIO"));
        Button btnPr = crearBotonMenuColor("Prendas Fabricadas", colorHover);
        Button btnCj = crearBotonMenuColor("Conjuntos", colorHover);
        sidebar.getChildren().addAll(btnPr, btnCj);
        btnPr.setOnAction(e -> { pushHistorial(() -> mostrarContenidoVacioEnMenu(contenido)); mostrarModuloPrendas(contenido, false); });
        btnCj.setOnAction(e -> { pushHistorial(() -> mostrarContenidoVacioEnMenu(contenido)); mostrarModuloConjuntos(contenido, false); });

        sidebar.getChildren().add(crearSeccionMenu("VENTAS"));
        Button btnPV  = crearBotonMenuColor("Punto de Venta", colorHover);
        Button btnPVd = crearBotonMenuColor("Prendas Vendidas", colorHover);
        Button btnCVd = crearBotonMenuColor("Conjuntos Vendidos", colorHover);
        Button btnDev    = crearBotonMenuColor("Devoluciones", colorHover);
        Button btnRegDev = crearBotonMenuColor("Registro de Devoluciones", colorHover);
        sidebar.getChildren().addAll(btnPV, btnPVd, btnCVd, btnDev, btnRegDev);
        btnPV.setOnAction(e     -> { pushHistorial(() -> mostrarContenidoVacioEnMenu(contenido)); mostrarPuntoDeVenta(contenido); });
        btnPVd.setOnAction(e    -> { pushHistorial(() -> mostrarContenidoVacioEnMenu(contenido)); mostrarModuloPrendasVendidas(contenido, false); });
        btnCVd.setOnAction(e    -> { pushHistorial(() -> mostrarContenidoVacioEnMenu(contenido)); mostrarModuloConjuntosVendidos(contenido, false); });
        btnDev.setOnAction(e    -> { pushHistorial(() -> mostrarContenidoVacioEnMenu(contenido)); mostrarDevoluciones(contenido); });
        btnRegDev.setOnAction(e -> { pushHistorial(() -> mostrarContenidoVacioEnMenu(contenido)); mostrarRegistroDevoluciones(contenido, false); });
        // Encargado tambien tiene alertas pero solo de prendas y conjuntos
        sidebar.getChildren().add(crearSeccionMenu("AVISOS"));
        Button btnAl = crearBotonMenuColor("Alertas de Stock", colorHover);
        sidebar.getChildren().add(btnAl);
       btnAl.setOnAction(e -> { pushHistorial(() -> mostrarContenidoVacioEnMenu(contenido)); mostrarAlertasStock(contenido, false); });
        Button btnTU2 = crearBotonMenuColor("Tiendas", colorHover);
        sidebar.getChildren().add(btnTU2);
        btnTU2.setOnAction(e -> { pushHistorial(() -> mostrarContenidoVacioEnMenu(contenido)); mostrarModuloTiendasUbicaciones(contenido, false); });  }

    Region spacer = new Region();
    VBox.setVgrow(spacer, Priority.ALWAYS);
    Button btnCerrar = new Button("Cerrar Sesion");
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

HBox barraAtras = crearBarraAtras();
        VBox areaContenido = new VBox(barraAtras, contenido);
        VBox.setVgrow(contenido, Priority.ALWAYS);
        HBox.setHgrow(areaContenido, Priority.ALWAYS);

        HBox cuerpo = new HBox(sidebarScroll, areaContenido);
        HBox.setHgrow(areaContenido, Priority.ALWAYS);
        VBox.setVgrow(cuerpo, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setCenter(cuerpo);
        root.setStyle("-fx-background-color: " + FONDO + ";");
        cambiarEscena(root);
        stage.setMaximized(true);
    }

    // ---------------MODULO ALERTAS DE STOCK -----------------------------


private void mostrarAlertasStock(StackPane contenido, boolean esAdmin) {
    contenido.getChildren().clear();

    Label titulo = new Label("Alertas de Stock");
    titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
    titulo.setTextFill(Color.web(SECUNDARIO));

    Label leyendaRojo     = new Label("Crítico: En o por debajo del mínimo");
Label leyendaAmarillo = new Label("Bajo: Hasta el doble del mínimo");
Label leyendaVerde    = new Label("Normal: Por encima del umbral bajo");
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

    // --------------TABLA PRENDAS -------------------------
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
    TableColumn<Prenda, String> colPMinimo   = new TableColumn<>("Minimo");
    TableColumn<Prenda, String> colPTipo     = new TableColumn<>("Tipo");

    colPNivel.setCellValueFactory(d -> {
        Integer min = d.getValue().getMinimoExistencia();
        if (min == null || min <= 0) return new SimpleStringProperty("Sin minimo");
        int ex = d.getValue().getExistencia();
return new SimpleStringProperty(ex <= min ? "\ud83d\udd34 Crítico" : "\ud83d\udfe1 Bajo");    });
    colPNombre.setCellValueFactory(d  -> new SimpleStringProperty(d.getValue().getNombre()));
    colPTalla.setCellValueFactory(d   -> new SimpleStringProperty(d.getValue().getTalla()));
    colPExist.setCellValueFactory(d   -> new SimpleStringProperty(String.valueOf(d.getValue().getExistencia())));
    colPMinimo.setCellValueFactory(d  -> new SimpleStringProperty(d.getValue().getMinimoExistencia() != null ? String.valueOf(d.getValue().getMinimoExistencia()) : "—"));
    colPTipo.setCellValueFactory(d    -> new SimpleStringProperty(d.getValue().getTipoPrenda()));

    tablaPrendas.getColumns().addAll(colPNivel, colPNombre, colPTalla, colPExist, colPMinimo, colPTipo);

    ObservableList<Prenda> prendasAlerta = FXCollections.observableArrayList(
        listaPrendas.stream()
            .filter(p -> p.getMinimoExistencia() != null && p.getMinimoExistencia() > 0)
            .filter(p -> p.getExistencia() <= p.getMinimoExistencia() * 2)
            .sorted((a, b) -> Integer.compare(a.getExistencia(), b.getExistencia()))
            .toList()
    );
    tablaPrendas.setItems(prendasAlerta);

    vista.getChildren().addAll(tituloPrendas, tablaPrendas);

    // ---------------TABLA CONJUNTOS -------------------------
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
    TableColumn<Conjunto, String> colCMinimo = new TableColumn<>("Minimo");
    TableColumn<Conjunto, String> colCPiezas = new TableColumn<>("Piezas");

    colCNivel.setCellValueFactory(d -> {
        Double min = d.getValue().getMinimoExistencia();
        if (min == null || min <= 0) return new SimpleStringProperty("Sin minimo");
        int ex = calcularExistenciaConjunto(d.getValue());
        return new SimpleStringProperty(ex <= min ? "🔴 Crítico" : "🟡 Bajo");
    });
    colCNombre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombre()));
    colCExist.setCellValueFactory(d  -> new SimpleStringProperty(String.valueOf(calcularExistenciaConjunto(d.getValue()))));
    colCMinimo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getMinimoExistencia() != null ? String.valueOf(d.getValue().getMinimoExistencia()) : "—"));
    colCPiezas.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getIdPrendas().size())));

    tablaConjuntos.getColumns().addAll(colCNivel, colCNombre, colCExist, colCMinimo, colCPiezas);

    ObservableList<Conjunto> conjuntosAlerta = FXCollections.observableArrayList(
        listaConjuntos.stream()
            .filter(c -> c.getMinimoExistencia() != null && c.getMinimoExistencia() > 0)
            .filter(c -> calcularExistenciaConjunto(c) <= c.getMinimoExistencia() * 2)
            .sorted((a, b) -> Integer.compare(calcularExistenciaConjunto(a), calcularExistenciaConjunto(b)))
            .toList()
    );
    tablaConjuntos.setItems(conjuntosAlerta);

    vista.getChildren().addAll(tituloConjuntos, tablaConjuntos);

    // ---------------TABLA MATERIA PRIMA (solo admin) -------------------------
    if (esAdmin) {
        Label tituloMP = new Label("Materia Prima");
        tituloMP.setFont(Font.font("System", FontWeight.BOLD, 15));
        tituloMP.setTextFill(Color.web(SECUNDARIO));

        TableView<Insumo> tablaMP = new TableView<>();
        tablaMP.setStyle("-fx-background-color: " + PANEL + "; -fx-border-color: #E5E7EB;");
        tablaMP.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tablaMP.setMinHeight(120);
        tablaMP.setMaxHeight(200);
        tablaMP.setPlaceholder(new Label("Sin alertas en materia prima"));

        TableColumn<Insumo, String> colMNivel    = new TableColumn<>("Nivel");
        TableColumn<Insumo, String> colMNombre   = new TableColumn<>("Nombre");
        TableColumn<Insumo, String> colMPartida  = new TableColumn<>("No. Partida");
        TableColumn<Insumo, String> colMExist    = new TableColumn<>("Existencia");
        TableColumn<Insumo, String> colMMinimo   = new TableColumn<>("Minimo");
        TableColumn<Insumo, String> colMTipo     = new TableColumn<>("Tipo Insumo");

        colMNivel.setCellValueFactory(d -> {
            Double min = d.getValue().getMinimoExistencia();
            if (min == null || min <= 0) return new SimpleStringProperty("Sin minimo");
            double ex = d.getValue().getExistencia();
            return new SimpleStringProperty(ex <= min ? "🔴 Crítico" : "🟡 Bajo");        });
        colMNombre.setCellValueFactory(d  -> new SimpleStringProperty(d.getValue().getNombre()));
        colMPartida.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNumeroPartida()));
        colMExist.setCellValueFactory(d   -> new SimpleStringProperty(String.valueOf(d.getValue().getExistencia())));
        colMMinimo.setCellValueFactory(d  -> new SimpleStringProperty(d.getValue().getMinimoExistencia() != null ? String.valueOf(d.getValue().getMinimoExistencia()) : "—"));
        colMTipo.setCellValueFactory(d    -> new SimpleStringProperty(d.getValue().getTipoInsumo()));

        tablaMP.getColumns().addAll(colMNivel, colMNombre, colMPartida, colMExist, colMMinimo, colMTipo);

        ObservableList<Insumo> mpAlerta = FXCollections.observableArrayList(
            listaMateriaPrima.stream()
                .filter(mp -> mp.getMinimoExistencia() != null && mp.getMinimoExistencia() > 0)
                .filter(mp -> mp.getExistencia() <= mp.getMinimoExistencia() * 2)
                .sorted((a, b) -> Double.compare(a.getExistencia(), b.getExistencia()))
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
        + (esAdmin ? listaMateriaPrima.stream()
            .filter(mp -> mp.getMinimoExistencia() != null && mp.getMinimoExistencia() > 0)
            .filter(mp -> mp.getExistencia() <= mp.getMinimoExistencia() * 2)
            .count() : 0);
    long criticas = prendasAlerta.stream()
        .filter(p -> p.getMinimoExistencia() != null && p.getExistencia() <= p.getMinimoExistencia())
        .count()
        + conjuntosAlerta.stream()
            .filter(c -> c.getMinimoExistencia() != null && calcularExistenciaConjunto(c) <= c.getMinimoExistencia())
            .count()
        + (esAdmin ? listaMateriaPrima.stream()
            .filter(mp -> mp.getMinimoExistencia() != null && mp.getExistencia() <= mp.getMinimoExistencia())
            .count() : 0);

    Label resumen = new Label(
        totalAlertas == 0
        ? "Sin alertas de stock activas"
        : totalAlertas + " alerta(s) activa(s) | " + criticas + " critica(s)"
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

// ---------------MODULO MATERIALES POR PRENDA -----------------------------
    private void mostrarMaterialesPorPrenda(StackPane contenido) {
        try {
            recargarMaterialesPorPrendaDesdeBD();
        } catch (SQLException ex) {
            ex.printStackTrace();
            mostrarError("Error al cargar materiales por prenda: " + ex.getMessage());
        }

        contenido.getChildren().clear();

        Label titulo = new Label("Materiales por Prenda");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        Label nota = new Label("Nota: la cantidad de tela esta dada en metros. Las demás cantidades son por pieza/unidad.");
        nota.setFont(Font.font("System", FontWeight.BOLD, 12));
        nota.setTextFill(Color.web(NARANJA));
        nota.setWrapText(true);
        nota.setStyle(
            "-fx-background-color: #FFF7ED; -fx-padding: 10 16; -fx-background-radius: 6;" +
            "-fx-border-color: " + NARANJA + "; -fx-border-radius: 6;");

        // ?"??"? Construir tabla dinmica: filas = prendas, columnas = tipos de materia prima ?"??"?
        TableView<String> tabla = new TableView<>();
        tabla.setStyle("-fx-background-color: " + PANEL + "; -fx-border-color: #E5E7EB;");
        tabla.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tabla.setPlaceholder(new Label("No hay prendas registradas"));

        // Columna fija: nombre de la prenda
        TableColumn<String, String> colPrenda = new TableColumn<>("Prenda");
        colPrenda.setPrefWidth(180);
        colPrenda.setCellValueFactory(d -> new SimpleStringProperty(nombrePrendaConTalla(d.getValue())));
        tabla.getColumns().add(colPrenda);

        // Una columna por cada insumo de materia prima registrado
        for (Insumo mp : listaMateriaPrima) {
            String idMp = mp.getNumeroPartida(); // usamos numeroPartida como referencia legible, pero buscamos por id
            String idNumerico = mp.getId();

            TableColumn<String, String> colMaterial = new TableColumn<>(mp.getNombre());
            colMaterial.setPrefWidth(140);
            colMaterial.setCellValueFactory(d -> {
                String idPrenda = d.getValue();
                Double cant = obtenerCantidadMaterial(idPrenda, String.valueOf(idNumerico));
                if (cant == null || cant == 0) return new SimpleStringProperty("N/A");
                String unidad = "Tela plana".equals(mp.getTipoInsumo()) || "Tela punto".equals(mp.getTipoInsumo()) ? " m" : "";
                return new SimpleStringProperty(formatearCantidad(cant) + unidad);
            });
            tabla.getColumns().add(colMaterial);
        }

        ObservableList<String> idsPrendas = FXCollections.<String>observableArrayList(
    listaPrendas.stream().map(p -> String.valueOf(p.getId())).toList());
        tabla.setItems(idsPrendas);
        tabla.setMinHeight(250);

        // Scroll vertical Y horizontal para la tabla, ambos pueden crecer dinamicamente
        ScrollPane scrollTabla = new ScrollPane(tabla);
        scrollTabla.setFitToHeight(false);
        scrollTabla.setFitToWidth(false);
        scrollTabla.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollTabla.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollTabla.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        scrollTabla.setPrefViewportHeight(320);
        VBox.setVgrow(scrollTabla, Priority.ALWAYS);

        Button btnAnadir = new Button("+ Dar de Alta Material por Prenda");
btnAnadir.setStyle("-fx-background-color: " + NARANJA + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
btnAnadir.setOnAction(e -> mostrarFormularioAltaMaterialPorPrenda(contenido));

Button btnEliminarMaterial = new Button("✕ Eliminar Material de Prenda");
btnEliminarMaterial.setStyle("-fx-background-color: " + ERROR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
btnEliminarMaterial.setOnAction(e -> mostrarFormularioEliminarMaterialPorPrenda(contenido));

HBox botonesMaterial = new HBox(12, btnAnadir, btnEliminarMaterial);

VBox vista = new VBox(16, titulo, nota, scrollTabla, botonesMaterial);
        vista.setStyle("-fx-padding: 30;");
        VBox.setVgrow(scrollTabla, Priority.ALWAYS);
        VBox.setVgrow(vista, Priority.ALWAYS);

        ScrollPane scrollExterno = new ScrollPane(vista);
        scrollExterno.setFitToWidth(true);
        scrollExterno.setFitToHeight(false);
        scrollExterno.setStyle("-fx-background-color: " + FONDO + "; -fx-background: " + FONDO + ";");
        VBox.setVgrow(scrollExterno, Priority.ALWAYS);

        StackPane wrapper = new StackPane(scrollExterno);
        wrapper.setStyle("-fx-background-color: " + FONDO + ";");
        contenido.getChildren().add(wrapper);
    }

    private String formatearCantidad(double valor) {
        if (valor == Math.floor(valor)) return String.valueOf((long) valor);
        return String.format("%.2f", valor);
    }

    // ---------------FORMULARIO TIPO CARRITO PARA DAR DE ALTA MATERIALES POR PRENDA -----------------------------
    private void mostrarFormularioAltaMaterialPorPrenda(StackPane contenido) {
        contenido.getChildren().clear();

        Label titulo = new Label("Dar de Alta Material por Prenda");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        Label nota = new Label("Selecciona la prenda, despues el material y la cantidad que se usa para producir 1 prenda.");
        nota.setFont(Font.font("System", 12));
        nota.setTextFill(Color.web(TEXTO_SUAVE));
        nota.setWrapText(true);

        // ---------------Selector de prenda -------------------------
        Label labelPrenda = new Label("Prenda:");
        labelPrenda.setFont(Font.font("System", 12));
        labelPrenda.setTextFill(Color.web(TEXTO_SUAVE));

        ComboBox<Prenda> selectorPrenda = new ComboBox<>();
        selectorPrenda.setItems(listaPrendas);
        selectorPrenda.setMaxWidth(360);
        selectorPrenda.setStyle(estiloInput());
        selectorPrenda.setConverter(new javafx.util.StringConverter<Prenda>() {
            @Override public String toString(Prenda p) { return p == null ? "" : p.getNombre() + " (" + p.getTalla() + ") ID " + p.getId(); }
            @Override public Prenda fromString(String s) { return null; }
        });

        // ---------------Selector de material (aparece tras elegir prenda) -------------------------
        Label labelMaterial = new Label("Material:");
        labelMaterial.setFont(Font.font("System", 12));
        labelMaterial.setTextFill(Color.web(TEXTO_SUAVE));
        labelMaterial.setVisible(false);
        labelMaterial.setManaged(false);

        ComboBox<Insumo> selectorMaterial = new ComboBox<>();
        selectorMaterial.setItems(listaMateriaPrima);
        selectorMaterial.setMaxWidth(360);
        selectorMaterial.setStyle(estiloInput());
        selectorMaterial.setVisible(false);
        selectorMaterial.setManaged(false);
        selectorMaterial.setConverter(new javafx.util.StringConverter<Insumo>() {
            @Override public String toString(Insumo mp) { return mp == null ? "" : mp.getNombre() + " (" + mp.getTipoInsumo() + ") ID " + mp.getNumeroPartida(); }
            @Override public Insumo fromString(String s) { return null; }
        });

        TextField campoCantidad = crearTextField("Cantidad usada por prenda (0 = N/A)");
        campoCantidad.setMaxWidth(360);
        campoCantidad.setVisible(false);
        campoCantidad.setManaged(false);

        Label mensajeAgregar = new Label("");
        mensajeAgregar.setFont(Font.font("System", 12));

        Button btnAgregarCarrito = new Button("+ Agregar a la lista");
        btnAgregarCarrito.setMaxWidth(360);
        btnAgregarCarrito.setStyle("-fx-background-color: " + CAFE + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 6; -fx-cursor: hand;");
        btnAgregarCarrito.setVisible(false);
        btnAgregarCarrito.setManaged(false);

        selectorPrenda.setOnAction(e -> {
            boolean haySeleccion = selectorPrenda.getValue() != null;
            labelMaterial.setVisible(haySeleccion);     labelMaterial.setManaged(haySeleccion);
            selectorMaterial.setVisible(haySeleccion);  selectorMaterial.setManaged(haySeleccion);
            campoCantidad.setVisible(haySeleccion);     campoCantidad.setManaged(haySeleccion);
            btnAgregarCarrito.setVisible(haySeleccion); btnAgregarCarrito.setManaged(haySeleccion);
        });

        // ---------------"Carrito" temporal de materiales a dar de alta para esta prenda -------------------------
        ObservableList<MaterialPorPrenda> carritoTemporal = FXCollections.observableArrayList();

        Label tituloCarrito = new Label("Materiales agregados");
        tituloCarrito.setFont(Font.font("System", FontWeight.BOLD, 14));
        tituloCarrito.setTextFill(Color.web(SECUNDARIO));

        TableView<MaterialPorPrenda> tablaCarrito = new TableView<>();
        tablaCarrito.setStyle("-fx-background-color: " + PANEL + "; -fx-border-color: #E5E7EB;");
        tablaCarrito.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tablaCarrito.setItems(carritoTemporal);
        tablaCarrito.setPlaceholder(new Label("Sin materiales agregados aun"));
        tablaCarrito.setMinHeight(140);

        TableColumn<MaterialPorPrenda, String> colCMaterial = new TableColumn<>("Material");
        TableColumn<MaterialPorPrenda, String> colCCantidad = new TableColumn<>("Cantidad");

        colCMaterial.setCellValueFactory(d -> {
            Insumo mp = listaMateriaPrima.stream()
                .filter(m -> String.valueOf(m.getId()).equals(d.getValue().getIdMateriaPrima()))
                .findFirst().orElse(null);
            return new SimpleStringProperty(mp != null ? mp.getNombre() : "??");
        });
        colCCantidad.setCellValueFactory(d -> {
            double c = d.getValue().getCantidad();
            Insumo mp = listaMateriaPrima.stream()
                .filter(m -> String.valueOf(m.getId()).equals(d.getValue().getIdMateriaPrima()))
                .findFirst().orElse(null);
            String unidad = mp != null && ("Tela plana".equals(mp.getTipoInsumo()) || "Tela punto".equals(mp.getTipoInsumo())) ? " m" : "";
            return new SimpleStringProperty(c == 0 ? "N/A" : formatearCantidad(c) + unidad);
        });

        tablaCarrito.getColumns().addAll(colCMaterial, colCCantidad);

        Button btnQuitar = new Button("Quitar seleccionado");
        btnQuitar.setStyle("-fx-background-color: transparent; -fx-text-fill: " + ERROR + "; -fx-font-size: 12px; -fx-cursor: hand; -fx-border-color: " + ERROR + "; -fx-border-radius: 4; -fx-padding: 6 14;");
        btnQuitar.setOnAction(e -> {
            MaterialPorPrenda sel = tablaCarrito.getSelectionModel().getSelectedItem();
            if (sel != null) carritoTemporal.remove(sel);
        });

        btnAgregarCarrito.setOnAction(e -> {
            Insumo matSel = selectorMaterial.getValue();
            if (matSel == null) {
                mensajeAgregar.setTextFill(Color.web(ERROR));
                mensajeAgregar.setText("Selecciona un material");
                return;
            }
            String cantStr = campoCantidad.getText().trim();
            double cantidad;
            try {
                cantidad = cantStr.isEmpty() ? 0 : Double.parseDouble(cantStr);
                if (cantidad < 0) {
                    mensajeAgregar.setTextFill(Color.web(ERROR));
                    mensajeAgregar.setText("La cantidad no puede ser negativa");
                    return;
                }
            } catch (NumberFormatException ex) {
                mensajeAgregar.setTextFill(Color.web(ERROR));
                mensajeAgregar.setText("Ingresa una cantidad valida (usa punto decimal)");
                return;
            }

            String idMatStr = String.valueOf(matSel.getId());
            boolean yaEnCarrito = carritoTemporal.stream().anyMatch(m -> m.getIdMateriaPrima().equals(idMatStr));
            if (yaEnCarrito) {
                mensajeAgregar.setTextFill(Color.web(ADVERTENCIA));
                mensajeAgregar.setText("Ese material ya esta en la lista. Quítalo si quieres cambiar la cantidad.");
                return;
            }

            carritoTemporal.add(new MaterialPorPrenda(
                selectorPrenda.getValue() != null ? String.valueOf(selectorPrenda.getValue().getId()) : "",
                idMatStr, cantidad
            ));
            mensajeAgregar.setTextFill(Color.web(EXITO));
            mensajeAgregar.setText("Material agregado a la lista");
            selectorMaterial.setValue(null);
            campoCantidad.clear();
        });

        Label mensajeEstado = new Label("");
        mensajeEstado.setFont(Font.font("System", 12));

        Button btnAceptar = new Button("Aceptar y Guardar");
        btnAceptar.setMaxWidth(360);
        btnAceptar.setStyle(
            "-fx-background-color: " + EXITO + "; -fx-text-fill: white; -fx-font-weight: bold;" +
            "-fx-font-size: 14px; -fx-padding: 12; -fx-background-radius: 6; -fx-cursor: hand;");
        btnAceptar.setOnAction(e -> {
            if (selectorPrenda.getValue() == null) {
                mensajeEstado.setTextFill(Color.web(ERROR));
                mensajeEstado.setText("Selecciona una prenda");
                return;
            }
            if (carritoTemporal.isEmpty()) {
                mensajeEstado.setTextFill(Color.web(ERROR));
                mensajeEstado.setText("Agrega al menos un material a la lista");
                return;
            }

            try {
                int idPrenda = selectorPrenda.getValue().getId();
                List<InsumoPrenda> registros = new ArrayList<>();

                for (MaterialPorPrenda mp : carritoTemporal) {
                    InsumoPrenda ip = new InsumoPrenda(mp.getIdMateriaPrima(), idPrenda, mp.getCantidad());
                    registros.add(ip);
                }

                int guardados = insumoPrendaDAO.insertAll(registros);
                recargarMaterialesPorPrendaDesdeBD();
                carritoTemporal.clear();

                mensajeEstado.setTextFill(Color.web(EXITO));
                mensajeEstado.setText("Se guardaron " + guardados + " material(es) correctamente");

                mostrarMaterialesPorPrenda(contenido);
            } catch (NumberFormatException ex) {
                mensajeEstado.setTextFill(Color.web(ERROR));
                mensajeEstado.setText("ID de material invalido. Debe ser numerico");
            } catch (SQLException ex) {
                ex.printStackTrace();
                mensajeEstado.setTextFill(Color.web(ERROR));
                mensajeEstado.setText("Error al guardar en BD: " + ex.getMessage());
            }
        });

        Button btnCancelar = new Button("Regresar sin guardar");
        btnCancelar.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXTO_SUAVE + "; -fx-font-size: 12px; -fx-cursor: hand;");
        btnCancelar.setOnAction(e -> mostrarMaterialesPorPrenda(contenido));

        VBox form = new VBox(12, titulo, nota,
            labelPrenda, selectorPrenda,
            labelMaterial, selectorMaterial, campoCantidad, mensajeAgregar, btnAgregarCarrito,
            tituloCarrito, tablaCarrito, btnQuitar,
            mensajeEstado, btnAceptar, btnCancelar);
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

    // -----------MODULO PRENDAS ---------------------
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
        TableColumn<Prenda, String> colMinimo  = new TableColumn<>("Minimo");

        colId.setCellValueFactory(d     -> new SimpleStringProperty(String.valueOf(d.getValue().getId())));
        colNombre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombre()));
        colTalla.setCellValueFactory(d  -> new SimpleStringProperty(d.getValue().getTalla()));
        colExist.setCellValueFactory(d  -> new SimpleStringProperty(String.valueOf(d.getValue().getExistencia())));
        colMayor.setCellValueFactory(d  -> new SimpleStringProperty("$" + d.getValue().getPrecioMayoreo()));
        colMenud.setCellValueFactory(d  -> new SimpleStringProperty("$" + d.getValue().getPrecioMenudeo()));
        colTienda.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getIdTienda())));
        colMinimo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getMinimoExistencia() != null ? String.valueOf(d.getValue().getMinimoExistencia()) : "—"));

        tabla.getColumns().addAll(colId, colNombre, colTalla, colExist, colMinimo, colMayor, colMenud, colTienda);
        tabla.setItems(listaPrendas);
        VBox.setVgrow(tabla, Priority.ALWAYS);

        Button btnDetalle = new Button("Ver Detalle");
        btnDetalle.setStyle("-fx-background-color: " + PRINCIPAL + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
        btnDetalle.setOnAction(e -> mostrarDetallePrenda(contenido, esAdmin));

        VBox vista = new VBox(16, titulo, tabla);
        vista.setStyle("-fx-padding: 30;");
        VBox.setVgrow(tabla, Priority.ALWAYS);
        VBox.setVgrow(vista, Priority.ALWAYS);

        if (esAdmin) {
            Button btnAnadir = new Button("+ Anadir Prenda");
            Button btnExist  = new Button("+ Anadir a Existente");
            Button btnEditar = new Button("✎ Editar Prenda");

            btnAnadir.setStyle("-fx-background-color: " + NARANJA + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
            btnExist.setStyle("-fx-background-color: " + CAFE + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
            btnEditar.setStyle("-fx-background-color: " + AZUL_EDITAR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");

            btnAnadir.setOnAction(e -> {
            pushHistorial(() -> mostrarModuloPrendas(contenido, true));
            mostrarFormularioNuevaPrenda(contenido);
       });
        btnExist.setOnAction(e  -> { pushHistorial(() -> mostrarModuloPrendas(contenido, true)); mostrarFormularioAnadirExistente(contenido); });
        btnEditar.setOnAction(e -> { pushHistorial(() -> mostrarModuloPrendas(contenido, true)); mostrarFormularioEditarPrenda(contenido); });

        Button btnEliminarPrenda = new Button("✕ Eliminar Prenda");
            btnEliminarPrenda.setStyle("-fx-background-color: " + ERROR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
            btnEliminarPrenda.setOnAction(e -> {
                Prenda sel = tabla.getSelectionModel().getSelectedItem();
                if (sel == null) { mostrarError("Selecciona una prenda de la tabla primero"); return; }
                Alert conf = new Alert(Alert.AlertType.CONFIRMATION);
                conf.setTitle("Eliminar prenda");
                conf.setHeaderText("¿Eliminar \"" + sel.getNombre() + "\" talla " + sel.getTalla() + "?");
                conf.setContentText("Esta acción no se puede deshacer.");
                conf.showAndWait().ifPresent(r -> {
                    if (r == ButtonType.OK) {
                        // TODO: acá va el código para eliminar de la base de datos
                        try{
                            prendaDAO.delete(sel.getId());
                            listaPrendas.remove(sel);
                        }
                        catch (SQLException ex) {
                            mostrarError("Error al eliminar prenda: " + ex.getMessage());
                        }
                    }
                });
            });
            HBox botones = new HBox(12, btnAnadir, btnExist, btnEditar, btnDetalle, btnEliminarPrenda);
            botones.setStyle("-fx-padding: 0 0 4 0;");
            vista.getChildren().add(botones);   

        } else {
            HBox botones = new HBox(12, btnDetalle);
            botones.setStyle("-fx-padding: 0 0 4 0;");
            vista.getChildren().add(botones);
        }

        contenido.getChildren().add(vista);
    }

    // ?"??"? VER DETALLE PRENDA ?"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"?
    private void mostrarDetallePrenda(StackPane contenido, boolean esAdmin) {
        contenido.getChildren().clear();

        Label titulo    = new Label("Detalle de Prenda");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        Label subtitulo = new Label("Busca por nombre o ID para ver toda la informacion");
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
            if (busqueda.isEmpty()) { mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("Ingresa un codigo de barras, nombre o ID"); return; }
            Prenda p = null;
            try {
                p = prendaDAO.getByCodigoBarras(busqueda);
                if (p == null) {
                    p = listaPrendas.stream()
                        .filter(x -> x.getNombre().equalsIgnoreCase(busqueda))
                        .findFirst().orElse(null);
                }
                if (p == null) {
                    try { p = prendaDAO.getById(Integer.parseInt(busqueda)); } catch (NumberFormatException ignored) {}
                }
            } catch (SQLException sqlEx) {
                mensajeBusqueda.setTextFill(Color.web(ERROR));
                mensajeBusqueda.setText("Error al buscar: " + sqlEx.getMessage());
                return;
            }
            if (p == null) {
                mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("No se encontro la prenda");
                tarjeta.setVisible(false); tarjeta.setManaged(false);
            } else {
                mensajeBusqueda.setTextFill(Color.web(EXITO)); mensajeBusqueda.setText("Prenda encontrada");
                tarjeta.getChildren().clear();
                Label lNombre = new Label(p.getNombre() + "  Talla " + p.getTalla());
                lNombre.setFont(Font.font("System", FontWeight.BOLD, 17));
                lNombre.setTextFill(Color.web(SECUNDARIO));
                Region sep = new Region(); sep.setPrefHeight(1); sep.setStyle("-fx-background-color: #E5E7EB;");
                tarjeta.getChildren().addAll(lNombre, sep,
                    filaDetalle("ID:", String.valueOf(p.getId())), filaDetalle("Tipo:", p.getTipoPrenda()),
                    filaDetalle("Talla:", p.getTalla()), filaDetalle("Existencia:", String.valueOf(p.getExistencia())),
                    filaDetalle("P. Mayoreo:", "$" + p.getPrecioMayoreo()), filaDetalle("P. Menudeo:", "$" + p.getPrecioMenudeo()),
                    filaDetalle("ID Tienda:", String.valueOf(p.getIdTienda())), filaDetalle("Descripcion:", p.getDescripcion()));
                tarjeta.setVisible(true); tarjeta.setManaged(true);
            }
        });

        Button btnRegresar = new Button("Regresar a lista");
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
        Label lValor = new Label(valor != null ? valor : "??");
        lValor.setFont(Font.font("System", 13));
        lValor.setTextFill(Color.web(TEXTO));
        lValor.setWrapText(true);
        HBox fila = new HBox(8, lEtiqueta, lValor);
        fila.setAlignment(Pos.CENTER_LEFT);
        return fila;
    }

  
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
        TableColumn<Conjunto, String> colMinimo  = new TableColumn<>("Minimo");

        colId.setCellValueFactory(d     -> new SimpleStringProperty(String.valueOf(d.getValue().getId())));
        colNombre.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombre()));
        colPiezas.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getIdPrendas().size())));
        colExist.setCellValueFactory(d  -> new SimpleStringProperty(String.valueOf(calcularExistenciaConjunto(d.getValue()))));
        colPrecio.setCellValueFactory(d -> new SimpleStringProperty("$" + String.format("%.2f", calcularPrecioConjunto(d.getValue()))));
        colMinimo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getMinimoExistencia() != null ? String.valueOf(d.getValue().getMinimoExistencia()) : "—"));

        tabla.getColumns().addAll(colId, colNombre, colPiezas, colExist, colMinimo, colPrecio);
        tabla.setItems(listaConjuntos);
        VBox.setVgrow(tabla, Priority.ALWAYS);

        Button btnDetalle = new Button("Ver Detalle");
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
            Button btnEliminarConj = new Button("✕ Eliminar Conjunto");
            btnEliminarConj.setStyle("-fx-background-color: " + ERROR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
            btnEliminarConj.setOnAction(e -> {
                Conjunto sel = tabla.getSelectionModel().getSelectedItem();
                if (sel == null) { mostrarError("Selecciona un conjunto de la tabla primero"); return; }
                Alert conf = new Alert(Alert.AlertType.CONFIRMATION);
                conf.setTitle("Eliminar conjunto");
                conf.setHeaderText("¿Eliminar conjunto \"" + sel.getNombre() + "\"?");
                conf.setContentText("Esta acción no se puede deshacer.");
                conf.showAndWait().ifPresent(r -> {
                    if (r == ButtonType.OK) {
                    
                        // TODO: acá va el código para eliminar de la base de datos
                        try{
                            conjuntoDAO.delete(sel.getId());
                            listaConjuntos.remove(sel);
                            
                        } catch (SQLException ex) {
                            mostrarError("Error al eliminar conjunto: " + ex.getMessage());
                        }
                    }
                });
            });
            HBox botones = new HBox(12, btnAnadir, btnEditar, btnDetalle, btnEliminarConj);
            vista.getChildren().add(botones);
        } else {
            HBox botones = new HBox(12, btnDetalle);
            vista.getChildren().add(botones);
        }

        contenido.getChildren().add(vista);
    }

    // ?"??"? VER DETALLE CONJUNTO ?"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"?
    private void mostrarDetalleConjunto(StackPane contenido, boolean esAdmin) {
        contenido.getChildren().clear();

        Label titulo    = new Label("Detalle de Conjunto");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        Label subtitulo = new Label("Busca por nombre o ID para ver toda la informacion");
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
                .filter(x -> x.getNombre().equalsIgnoreCase(busqueda) || String.valueOf(x.getId()).equals(busqueda))
                .findFirst().orElse(null);
            if (c == null) {
                mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("No se encontro el conjunto");
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
                    Prenda p = listaPrendas.stream().filter(x -> String.valueOf(x.getId()).equals(idP)).findFirst().orElse(null);
                    Label lPrenda = new Label(p != null
                        ? "- " + p.getNombre() + " (Talla " + p.getTalla() + ")" + "Exist: " + p.getExistencia()
                        : "- ID " + idP + " (no encontrada)");
                    lPrenda.setFont(Font.font("System", 13));
                    lPrenda.setTextFill(p != null && p.getExistencia() > 0 ? Color.web(TEXTO) : Color.web(ERROR));
                    lPrenda.setWrapText(true);
                    listaPrendasBox.getChildren().add(lPrenda);
                }
                HBox filaPrendas = new HBox(8, labelPrendasTitulo, listaPrendasBox);
                filaPrendas.setAlignment(Pos.TOP_LEFT);

                tarjeta.getChildren().addAll(lNombre, sep,
                    filaDetalle("ID:", String.valueOf(c.getId())),
                    filaDetalle("Piezas:", String.valueOf(c.getIdPrendas().size())),
                    filaDetalle("Existencia:", String.valueOf(calcularExistenciaConjunto(c))),
                    filaDetalle("Precio:", "$" + String.format("%.2f", calcularPrecioConjunto(c))),
                    filaDetalle("Descripcion:", c.getDescripcion()),
                    filaPrendas);
                tarjeta.setVisible(true); tarjeta.setManaged(true);
            }
        });

        Button btnRegresar = new Button("Regresar a lista");
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

    // ?"??"? M?"DULO PRENDAS VENDIDAS ?"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"?
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
        TableColumn<PrendaVendida, String> colFechaDev = new TableColumn<>("Limite Dev.");

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
        try {
            tabla.setItems(cargarPrendasVendidasDesdeBD());
        } catch (SQLException ex) {
            mostrarError("Error al cargar ventas de prendas: " + ex.getMessage());
            tabla.setItems(FXCollections.observableArrayList());
        }
        VBox.setVgrow(tabla, Priority.ALWAYS);

        Button btnDetalle = new Button("Ver Detalle");
        btnDetalle.setStyle("-fx-background-color: " + PRINCIPAL + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
        btnDetalle.setOnAction(e -> mostrarDetallePrendaVendida(contenido, esAdmin));

        VBox vista = new VBox(16, titulo, tabla);
        vista.setStyle("-fx-padding: 30;");
        VBox.setVgrow(tabla, Priority.ALWAYS);
        VBox.setVgrow(vista, Priority.ALWAYS);

        if (esAdmin) {
            Button btnAnadir = new Button("+ Registrar Venta");
            Button btnEditar = new Button("Editar Registro");
            btnAnadir.setStyle("-fx-background-color: " + NARANJA + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
            btnEditar.setStyle("-fx-background-color: " + AZUL_EDITAR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
            btnAnadir.setOnAction(e -> mostrarFormularioNuevaPrendaVendida(contenido));
            btnEditar.setOnAction(e -> mostrarFormularioEditarPrendaVendida(contenido));
            Button btnEliminarPV = new Button("✕ Eliminar Registro");
            btnEliminarPV.setStyle("-fx-background-color: " + ERROR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
            btnEliminarPV.setOnAction(e -> {
                PrendaVendida sel = tabla.getSelectionModel().getSelectedItem();
                if (sel == null) { mostrarError("Selecciona un registro de la tabla primero"); return; }
                Alert conf = new Alert(Alert.AlertType.CONFIRMATION);
                conf.setTitle("Eliminar registro");
                conf.setHeaderText("¿Eliminar venta \"" + sel.getIdVenta() + "\"?");
                conf.setContentText("Esta acción no se puede deshacer.");
                conf.showAndWait().ifPresent(r -> {
                    if (r == ButtonType.OK) {
                        // TODO: acá va el código para eliminar de la base de datos
                        try{
                            ventaDAO.deleteVenta(Integer.parseInt(sel.getIdVenta()));
                            tabla.getItems().remove(sel);
                        }catch(SQLException ex){
                            mostrarError("Error al eliminar registro: " + ex.getMessage());
                        }
                        
                    }
                });
            });
            HBox botones = new HBox(12, btnAnadir, btnEditar, btnDetalle, btnEliminarPV);
            vista.getChildren().add(botones);
        } else {
            HBox botones = new HBox(12, btnDetalle);
            vista.getChildren().add(botones);
        }

        contenido.getChildren().add(vista);
    }


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
            PrendaVendida pv;
            try {
                pv = buscarPrendaVendidaEnBD(busqueda);
            } catch (SQLException ex) {
                mensajeBusqueda.setTextFill(Color.web(ERROR));
                mensajeBusqueda.setText("Error al consultar ventas: " + ex.getMessage());
                tarjeta.setVisible(false); tarjeta.setManaged(false);
                return;
            }
            if (pv == null) {
                mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("No se encontro el registro");
                tarjeta.setVisible(false); tarjeta.setManaged(false);
            } else {
                mensajeBusqueda.setTextFill(Color.web(EXITO)); mensajeBusqueda.setText("Registro encontrado");
                tarjeta.getChildren().clear();
                Label lNombre = new Label(pv.getNombrePrenda() + " | Talla " + pv.getTalla());
                lNombre.setFont(Font.font("System", FontWeight.BOLD, 17));
                lNombre.setTextFill(Color.web(SECUNDARIO));
                Region sep = new Region(); sep.setPrefHeight(1); sep.setStyle("-fx-background-color: #E5E7EB;");

                boolean dentroDevolucion = !LocalDate.now().isAfter(pv.getFechaLimiteDevolucion());
                Label lEstado = new Label(dentroDevolucion ? "Dentro del periodo de devolucion" : "Periodo de devolucion vencido");
                lEstado.setFont(Font.font("System", FontWeight.BOLD, 12));
                lEstado.setTextFill(Color.web(dentroDevolucion ? EXITO : ERROR));

                tarjeta.getChildren().addAll(lNombre, sep,
                    filaDetalle("ID Venta:",         pv.getIdVenta()),
                    filaDetalle("Cantidad:",          String.valueOf(pv.getCantidad())),
                    filaDetalle("Tipo de venta:",     pv.getTipoVenta()),
                    filaDetalle("Precio unitario:",   "$" + String.format("%.2f", pv.getPrecioUnitario())),
                    filaDetalle("Total:",             "$" + String.format("%.2f", pv.getTotal())),
                    filaDetalle("Fecha de venta:",    pv.getFechaVenta().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))),
                    filaDetalle("Limite devolucion:", pv.getFechaLimiteDevolucion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))),
                    filaDetalle("Descripcion:",       pv.getDescripcion()),
                    lEstado);
                tarjeta.setVisible(true); tarjeta.setManaged(true);
            }
        });

        Button btnRegresar = new Button("Regresar a lista");
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

    // ?"??"? FORMULARIO NUEVA PRENDA VENDIDA ?"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"?
    private void mostrarFormularioNuevaPrendaVendida(StackPane contenido) {
        contenido.getChildren().clear();

        Label titulo    = new Label("Registrar Prenda Vendida");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        Label subtitulo = new Label("Selecciona la prenda y captura cantidad/precio unitario");
        subtitulo.setFont(Font.font("System", 12));
        subtitulo.setTextFill(Color.web(TEXTO_SUAVE));

        Label labelPrenda = new Label("Prenda:");
        labelPrenda.setTextFill(Color.web(TEXTO_SUAVE));
        labelPrenda.setFont(Font.font("System", 12));

        ComboBox<Prenda> selectorPrenda = new ComboBox<>();
        selectorPrenda.setItems(listaPrendas);
        selectorPrenda.setMaxWidth(320);
        selectorPrenda.setStyle(estiloInput());
        selectorPrenda.setConverter(new javafx.util.StringConverter<>() {
            @Override public String toString(Prenda p) {
                return p == null ? "" : p.getNombre() + " (Talla " + p.getTalla() + ")";
            }
            @Override public Prenda fromString(String s) { return null; }
        });

        TextField campoCantidad     = crearTextField("Cantidad");
        TextField campoPrecioUnit   = crearTextField("Precio unitario");

        Label mensajeEstado = new Label("");
        mensajeEstado.setFont(Font.font("System", 12));

        Button btnGuardar = new Button("Guardar Registro");
        btnGuardar.setMaxWidth(320);
        btnGuardar.setStyle("-fx-background-color: " + NARANJA + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 6; -fx-cursor: hand;");

        Button btnCancelar = new Button("Regresar a lista");
        btnCancelar.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXTO_SUAVE + "; -fx-font-size: 12px; -fx-cursor: hand;");
        btnCancelar.setOnAction(e -> mostrarModuloPrendasVendidas(contenido, true));

        btnGuardar.setOnAction(e -> {
            Prenda prendaSeleccionada = selectorPrenda.getValue();
            String cantidadStr  = campoCantidad.getText().trim();
            String precioStr    = campoPrecioUnit.getText().trim();

            if (prendaSeleccionada == null || cantidadStr.isEmpty() || precioStr.isEmpty()) {
                mensajeEstado.setTextFill(Color.web(ERROR));
                mensajeEstado.setText("Selecciona prenda y completa cantidad/precio");
                return;
            }

            try {
                int cantidad = Integer.parseInt(cantidadStr);
                double precioUnit = Double.parseDouble(precioStr);
                if (cantidad <= 0 || precioUnit < 0) {
                    mensajeEstado.setTextFill(Color.web(ERROR));
                    mensajeEstado.setText("Cantidad y precio deben ser validos");
                    return;
                }
                if (cantidad > prendaSeleccionada.getExistencia()) {
                    mensajeEstado.setTextFill(Color.web(ERROR));
                    mensajeEstado.setText("Stock insuficiente. Disponible: " + prendaSeleccionada.getExistencia());
                    return;
                }

                int folio = ventaDAO.crearVenta(LocalDate.now());
                DetalleVentaPrenda detalle = new DetalleVentaPrenda(
                    folio,
                    prendaSeleccionada.getId(),
                    cantidad,
                    cantidad * precioUnit
                );
                detallePrendaDAO.insert(detalle);

                prendaSeleccionada.setExistencia(prendaSeleccionada.getExistencia() - cantidad);
                prendaDAO.update(prendaSeleccionada);

                recargarDatos();
                verificarConjuntos();
                mostrarModuloPrendasVendidas(contenido, true);
            } catch (NumberFormatException ex) {
                mensajeEstado.setTextFill(Color.web(ERROR));
                mensajeEstado.setText("Cantidad y precio deben ser numericos");
            } catch (SQLException ex) {
                mensajeEstado.setTextFill(Color.web(ERROR));
                mensajeEstado.setText("Error al guardar venta: " + ex.getMessage());
            }
        });

        VBox form = new VBox(12, titulo, subtitulo, labelPrenda, selectorPrenda,
                campoCantidad, campoPrecioUnit, mensajeEstado, btnGuardar, btnCancelar);
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

    // ------------------ FORMULARIO EDITAR PRENDA VENDIDA -----------------------
    private void mostrarFormularioEditarPrendaVendida(StackPane contenido) {
        contenido.getChildren().clear();

        Label titulo    = new Label("Editar Registro de Venta");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        Label subtitulo = new Label("Consulta una venta registrada (edicion deshabilitada para datos transaccionales)");
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
        TextField campoDescripcion  = crearTextField("Descripcion");
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

        Button btnGuardar = new Button("Edicion no disponible");
        btnGuardar.setMaxWidth(320);
        btnGuardar.setStyle("-fx-background-color: " + AZUL_EDITAR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 6; -fx-cursor: hand;");
        btnGuardar.setDisable(true);

        panelEdicion.getChildren().addAll(campoNombrePrenda, campoTalla, campoCantidad, campoPrecioUnit,
                labelTipoVenta, selectorTipo, campoFechaVenta, campoDescripcion, mensajeEstado, btnGuardar);

        final PrendaVendida[] pvEncontrada = {null};

        btnBuscar.setOnAction(e -> {
            String busqueda = campoBusqueda.getText().trim();
            if (busqueda.isEmpty()) { mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("Ingresa un ID o nombre"); return; }
            PrendaVendida pv;
            try {
                pv = buscarPrendaVendidaEnBD(busqueda);
            } catch (SQLException ex) {
                mensajeBusqueda.setTextFill(Color.web(ERROR));
                mensajeBusqueda.setText("Error al consultar ventas: " + ex.getMessage());
                panelEdicion.setVisible(false); panelEdicion.setManaged(false);
                return;
            }
            if (pv == null) {
                mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("No se encontro el registro");
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
                mensajeEstado.setTextFill(Color.web(ADVERTENCIA));
                mensajeEstado.setText("Las ventas se administran directamente en BD y no se editan desde este formulario");
                panelEdicion.setVisible(true); panelEdicion.setManaged(true);
            }
        });

        Button btnCancelar = new Button("Regresar a lista");
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

// ?"??"? M?"DULO MATERIA PRIMA ?"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"?
    private void mostrarModuloMateriaPrima(StackPane contenido, boolean esAdmin) {
        contenido.getChildren().clear();

        Label titulo = new Label("Materia Prima");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        TableView<Insumo> tabla = new TableView<>();
        tabla.setStyle("-fx-background-color: " + PANEL + "; -fx-border-color: #E5E7EB;");
        tabla.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tabla.setPlaceholder(new Label("No hay materia prima registrada"));
        tabla.setMinHeight(200);

        TableColumn<Insumo, String> colId           = new TableColumn<>("ID");
        TableColumn<Insumo, String> colPartida      = new TableColumn<>("No. Partida");
        TableColumn<Insumo, String> colNombre       = new TableColumn<>("Nombre");
        TableColumn<Insumo, String> colExist        = new TableColumn<>("Existencia");
        TableColumn<Insumo, String> colMinimo       = new TableColumn<>("Minimo");
        TableColumn<Insumo, String> colTipoExist    = new TableColumn<>("Tipo Existencia");
        TableColumn<Insumo, String> colDescripcion  = new TableColumn<>("Descripcion");
        TableColumn<Insumo, String> colColor        = new TableColumn<>("Color");
        TableColumn<Insumo, String> colMedida       = new TableColumn<>("Medida");
        TableColumn<Insumo, String> colAncho        = new TableColumn<>("Ancho");
        TableColumn<Insumo, String> colComposicion  = new TableColumn<>("Composicion");
        TableColumn<Insumo, String> colTipo         = new TableColumn<>("Tipo");
        TableColumn<Insumo, String> colNo           = new TableColumn<>("No.");
        TableColumn<Insumo, String> colTamanio      = new TableColumn<>("Tamano");
        TableColumn<Insumo, String> colTalla2       = new TableColumn<>("Talla");
        TableColumn<Insumo, String> colMaterial     = new TableColumn<>("Material");
        TableColumn<Insumo, String> colTipoInsum    = new TableColumn<>("Tipo Insumo");

        colId.setCellValueFactory(d           -> new SimpleStringProperty(String.valueOf(d.getValue().getId())));
        colPartida.setCellValueFactory(d      -> new SimpleStringProperty(d.getValue().getNumeroPartida()));
        colNombre.setCellValueFactory(d       -> new SimpleStringProperty(d.getValue().getNombre()));
        colExist.setCellValueFactory(d        -> new SimpleStringProperty(String.valueOf(d.getValue().getExistencia())));
        colMinimo.setCellValueFactory(d       -> new SimpleStringProperty(d.getValue().getMinimoExistencia() != null ? String.valueOf(d.getValue().getMinimoExistencia()) : "—"));
        colTipoExist.setCellValueFactory(d    -> new SimpleStringProperty(d.getValue().getTipoExistencia() != null && !d.getValue().getTipoExistencia().isEmpty() ? d.getValue().getTipoExistencia() : "??"));
        colDescripcion.setCellValueFactory(d  -> new SimpleStringProperty(d.getValue().getDescripcion() != null ? d.getValue().getDescripcion() : "??"));
        colColor.setCellValueFactory(d        -> new SimpleStringProperty(d.getValue().getColor() != null ? d.getValue().getColor() : "??"));
        colMedida.setCellValueFactory(d       -> new SimpleStringProperty(String.valueOf(d.getValue().getMedida())));
        colAncho.setCellValueFactory(d        -> new SimpleStringProperty(String.valueOf(d.getValue().getAncho())));
        colComposicion.setCellValueFactory(d  -> new SimpleStringProperty(d.getValue().getComposicion() != null ? d.getValue().getComposicion() : "??"));
        colTipo.setCellValueFactory(d         -> new SimpleStringProperty(d.getValue().getTipo() != null ? d.getValue().getTipo() : "??"));
        colNo.setCellValueFactory(d           -> new SimpleStringProperty(String.valueOf(d.getValue().getNo())));
        colTamanio.setCellValueFactory(d      -> new SimpleStringProperty(d.getValue().getTamanio() != null ? d.getValue().getTamanio() : "??"));
        colTalla2.setCellValueFactory(d       -> new SimpleStringProperty(String.valueOf(d.getValue().getTalla())));
        colMaterial.setCellValueFactory(d     -> new SimpleStringProperty(d.getValue().getMaterial() != null ? d.getValue().getMaterial() : "??"));
        colTipoInsum.setCellValueFactory(d    -> new SimpleStringProperty(d.getValue().getTipoInsumo()));

        tabla.getColumns().addAll(colId, colPartida, colNombre, colExist, colMinimo,
                colTipoExist, colDescripcion, colColor, colMedida, colAncho,
                colComposicion, colTipo, colNo, colTamanio, colTalla2, colMaterial, colTipoInsum);
        tabla.setItems(listaMateriaPrima);

        // Ancho total = suma de los anchos preferidos de cada columna, así la tabla
        // no genera su propio scroll horizontal interno: el ScrollPane exterior controla todo.
        double anchoTotalColumnas = tabla.getColumns().stream()
            .mapToDouble(TableColumn::getPrefWidth)
            .sum();
        tabla.setPrefWidth(anchoTotalColumnas);
        tabla.setMinWidth(anchoTotalColumnas);

        ScrollPane scrollTablaMP = new ScrollPane(tabla);
        scrollTablaMP.setFitToHeight(false);
        scrollTablaMP.setFitToWidth(false);
        scrollTablaMP.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollTablaMP.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollTablaMP.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        scrollTablaMP.setPrefViewportHeight(400);
        VBox.setVgrow(scrollTablaMP, Priority.ALWAYS);

        Button btnDetalle = new Button("Ver Detalle");
        btnDetalle.setStyle("-fx-background-color: " + PRINCIPAL + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
        btnDetalle.setOnAction(e -> mostrarDetalleMateriaPrima(contenido, esAdmin));

        VBox vista = new VBox(16, titulo, scrollTablaMP);
        vista.setStyle("-fx-padding: 30;");
        VBox.setVgrow(scrollTablaMP, Priority.ALWAYS);
        VBox.setVgrow(vista, Priority.ALWAYS);

        if (esAdmin) {
            Button btnAnadir = new Button("+ Anadir Insumo");
            Button btnExist  = new Button("+ Anadir a Existente");
            Button btnEditar = new Button("✎ Editar Insumo");

            btnAnadir.setStyle("-fx-background-color: " + NARANJA + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
            btnExist.setStyle("-fx-background-color: " + CAFE + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
            btnEditar.setStyle("-fx-background-color: " + AZUL_EDITAR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");

            btnAnadir.setOnAction(e -> mostrarFormularioNuevaMateriaPrima(contenido));
            btnExist.setOnAction(e  -> mostrarFormularioAnadirExistenteMP(contenido));
            btnEditar.setOnAction(e -> mostrarFormularioEditarMateriaPrima(contenido));

            Button btnEliminarMP = new Button("✕ Eliminar Insumo");
            btnEliminarMP.setStyle("-fx-background-color: " + ERROR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
            btnEliminarMP.setOnAction(e -> {
                Insumo sel = tabla.getSelectionModel().getSelectedItem();
                if (sel == null) { mostrarError("Selecciona un insumo de la tabla primero"); return; }
                Alert conf = new Alert(Alert.AlertType.CONFIRMATION);
                conf.setTitle("Eliminar insumo");
                conf.setHeaderText("¿Eliminar \"" + sel.getNombre() + "\"?");
                conf.setContentText("Esta acción no se puede deshacer.");
                conf.showAndWait().ifPresent(r -> {
                    if (r == ButtonType.OK) {
                        // TODO: acá va el código para eliminar de la base de datos
                        try {
                            insumoDAO.delete(sel.getId());
                            listaMateriaPrima.remove(sel);
                        } catch (SQLException ex) {
                            mostrarError("Error al eliminar insumo: " + ex.getMessage());
                        }
                    }
                });
            });
            HBox botones = new HBox(12, btnAnadir, btnExist, btnEditar, btnDetalle, btnEliminarMP);
            vista.getChildren().add(botones);
        } else {
            HBox botones = new HBox(12, btnDetalle);
            vista.getChildren().add(botones);
        }

        contenido.getChildren().add(vista);
    }


    // ---------------VER DETALLE MATERIA PRIMA ---------------------
    private void mostrarDetalleMateriaPrima(StackPane contenido, boolean esAdmin) {
        contenido.getChildren().clear();

        Label titulo    = new Label("Detalle de Materia Prima");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        Label subtitulo = new Label("Busca por nombre o numero de partida para ver toda la informacion");
        subtitulo.setFont(Font.font("System", 12));
        subtitulo.setTextFill(Color.web(TEXTO_SUAVE));

        TextField campoBusqueda = crearTextField("Nombre o numero de partida");
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
            if (busqueda.isEmpty()) { mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("Ingresa un nombre o numero de partida"); return; }
            Insumo mp = listaMateriaPrima.stream()
                .filter(x -> x.getNombre().equalsIgnoreCase(busqueda) || x.getNumeroPartida().equalsIgnoreCase(busqueda))
                .findFirst().orElse(null);
            if (mp == null) {
                mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("No se encontro el insumo");
                tarjeta.setVisible(false); tarjeta.setManaged(false);
            } else {
                mensajeBusqueda.setTextFill(Color.web(EXITO)); mensajeBusqueda.setText("Insumo encontrado");
                tarjeta.getChildren().clear();
                Label lNombre = new Label(mp.getNombre() + " " + mp.getTipoInsumo());
                lNombre.setFont(Font.font("System", FontWeight.BOLD, 17));
                lNombre.setTextFill(Color.web(SECUNDARIO));
                Region sep = new Region(); sep.setPrefHeight(1); sep.setStyle("-fx-background-color: #E5E7EB;");
                tarjeta.getChildren().addAll(lNombre, sep,
                    filaDetalle("ID:",              String.valueOf(mp.getId())),
                    filaDetalle("No. Partida:",     mp.getNumeroPartida()),
                    filaDetalle("Existencia:",      String.valueOf(mp.getExistencia())),
                    filaDetalle("Tipo Existencia:", mp.getTipoExistencia() != null && !mp.getTipoExistencia().isEmpty() ? mp.getTipoExistencia() : "??"),
                    filaDetalle("Color:",           mp.getColor()        != null ? mp.getColor()        : "??"),
                    filaDetalle("Medida:",          String.valueOf(mp.getMedida())),
                    filaDetalle("Ancho:",           String.valueOf(mp.getAncho()) + " m"),
                    filaDetalle("Composicion:",     mp.getComposicion()  != null ? mp.getComposicion()   : "??"),
                    filaDetalle("Tipo:",            mp.getTipo()         != null ? mp.getTipo()          : "??"),
                    filaDetalle("No.:",             String.valueOf(mp.getNo())),
                    filaDetalle("Tamano:",          mp.getTamanio()      != null ? mp.getTamanio()       : "??"),
                    filaDetalle("Talla:",           String.valueOf(mp.getTalla())),
                    filaDetalle("Material:",        mp.getMaterial()     != null ? mp.getMaterial()      : "??"),
                    filaDetalle("Tipo Insumo:",     mp.getTipoInsumo()),
                    filaDetalle("Descripcion:",     mp.getDescripcion()));
                tarjeta.setVisible(true); tarjeta.setManaged(true);
            }
        });

        Button btnRegresar = new Button("Regresar a lista");
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

    // ------FORMULARIO NUEVO INSUMO --------------------
    private void mostrarFormularioNuevaMateriaPrima(StackPane contenido) {
        contenido.getChildren().clear();

        Label titulo    = new Label("Nuevo Insumo");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        Label subtitulo = new Label("Completa los campos para registrar el insumo");
        subtitulo.setFont(Font.font("System", 12));
        subtitulo.setTextFill(Color.web(TEXTO_SUAVE));

        TextField campoPartida      = crearTextField("Numero de partida  (ej: BOT-003)");
        TextField campoNombre       = crearTextField("Nombre del insumo");
        TextField campoExistencia   = crearTextField("Existencia inicial");
        TextField campoMinimo       = crearTextField("Minimo de existencia  (ej: 10)");
        TextField campoTipoExist    = crearTextField("Tipo de existencia  (opcional)");
        TextField campoDescripcion  = crearTextField("Descripcion  (opcional)");
        TextField campoColor        = crearTextField("Color  (opcional)");
        TextField campoMedida       = crearTextField("Medida  (opcional)");
        TextField campoAncho        = crearTextField("Ancho en metros  (opcional)");
        TextField campoComposicion  = crearTextField("Composicion  (opcional, ej: 100% ALG)");
        TextField campoTipo         = crearTextField("Tipo de tejido/estructura  (opcional)");
        TextField campoNo           = crearTextField("No.  (opcional, ej: 14)");
        TextField campoTamanio      = crearTextField("Tamano  (opcional)");
        TextField campoTalla        = crearTextField("Talla  (opcional)");
        TextField campoMaterial     = crearTextField("Material  (opcional, ej: Plastico)");
        TextField campoUbicacion     = crearTextField("Ubicacion");

        Label labelTipoInsumo = new Label("Tipo de insumo:");
        labelTipoInsumo.setTextFill(Color.web(TEXTO_SUAVE));
        labelTipoInsumo.setFont(Font.font("System", 12));

        ComboBox<String> selectorTipoInsumo = new ComboBox<>();
        selectorTipoInsumo.getItems().addAll("Boton", "Cierre", "Tela plana", "Tela punto", "Hilo", "Otro");
        selectorTipoInsumo.setValue("Boton");
        selectorTipoInsumo.setMaxWidth(320);
        selectorTipoInsumo.setStyle(estiloInput());

        Label mensajeEstado = new Label("");
        mensajeEstado.setFont(Font.font("System", 12));

        Button btnGuardar = new Button("Guardar Insumo");
        btnGuardar.setMaxWidth(320);
        btnGuardar.setStyle("-fx-background-color: " + NARANJA + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 6; -fx-cursor: hand;");

        Button btnCancelar = new Button("Regresar a lista");
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
            String talla     = campoTalla.getText().trim();
            String material     = campoMaterial.getText().trim();
            String tipoInsumo   = selectorTipoInsumo.getValue();
            String ubicacion    = campoUbicacion.getText().trim();

            String minimoStr    = campoMinimo.getText().trim();
            if (partida.isEmpty() || nombre.isEmpty() || existStr.isEmpty()) {
                mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("Partida, nombre y existencia son obligatorios"); return;
            }
            boolean yaExiste = listaMateriaPrima.stream().anyMatch(mp -> mp.getNumeroPartida() != null && mp.getNumeroPartida().equalsIgnoreCase(partida));
            if (yaExiste) { mensajeEstado.setTextFill(Color.web(ADVERTENCIA)); mensajeEstado.setText("Ya existe un insumo con ese numero de partida"); return; }

            try {
                double existencia = Double.parseDouble(existStr);
                Double minimo = minimoStr.isEmpty() ? null : Double.parseDouble(minimoStr);
                double ancho = anchoStr.isEmpty() ? 0.0 : Double.parseDouble(anchoStr);
                int no = noStr.isEmpty() ? 0 : Integer.parseInt(noStr);
                double medidaD = medida.isEmpty() ? 0.0 : Double.parseDouble(medida);
                double tallaD = talla.isEmpty() ? 0.0 : Double.parseDouble(talla);

                Insumo nuevoInsumo = new Insumo(
                    "I-" + String.valueOf(System.currentTimeMillis()),
                    partida,
                    existencia,
                    tipoExist.isEmpty() ? "" : tipoExist,
                    descripcion.isEmpty() ? "" : descripcion,
                    nombre,
                    color.isEmpty() ? "" : color,
                    medidaD,
                    ancho,
                    composicion.isEmpty() ? "" : composicion,
                    tipo.isEmpty() ? "" : tipo,
                    no,
                    tamanio.isEmpty() ? "" : tamanio,
                    tallaD,
                    material.isEmpty() ? "" : material,
                    tipoInsumo,
                    1
                );
                nuevoInsumo.setMinimoExistencia(minimo);
                insumoDAO.insert(nuevoInsumo);
                recargarDatos();
                mostrarModuloMateriaPrima(contenido, true);
            } catch (SQLException ex) {
                mensajeEstado.setTextFill(Color.web(ERROR));
                mensajeEstado.setText("Error al guardar: " + ex.getMessage());
            } catch (NumberFormatException ex) {
                mensajeEstado.setTextFill(Color.web(ERROR));
                mensajeEstado.setText("Existencia, minimo, medida, ancho y talla deben ser numeros validos");
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

    // ?"??"? FORMULARIO A?'ADIR A EXISTENTE (MP) ?"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"?
    private void mostrarFormularioAnadirExistenteMP(StackPane contenido) {
        contenido.getChildren().clear();

        Label titulo    = new Label("Anadir a Existente");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        Label subtitulo = new Label("Busca por nombre o numero de partida y añade unidades");
        subtitulo.setFont(Font.font("System", 12));
        subtitulo.setTextFill(Color.web(TEXTO_SUAVE));

        TextField campoBusqueda = crearTextField("Nombre o numero de partida");
        Label mensajeBusqueda   = new Label("");
        mensajeBusqueda.setFont(Font.font("System", 12));

        VBox panelResultado = new VBox(8);
        panelResultado.setStyle("-fx-background-color: #F0FDF4; -fx-border-color: " + EXITO + "; -fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 12;");
        panelResultado.setVisible(false);
        panelResultado.setManaged(false);

        Label labelResultado    = new Label("");
        TextField campoCantidad = crearTextField("Cantidad a anadir");
        Label mensajeEstado     = new Label("");
        mensajeEstado.setFont(Font.font("System", 12));

        Button btnAnadir = new Button("Anadir Unidades");
        btnAnadir.setMaxWidth(320);
        btnAnadir.setStyle("-fx-background-color: " + CAFE + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 6; -fx-cursor: hand;");
        panelResultado.getChildren().addAll(labelResultado, campoCantidad, mensajeEstado, btnAnadir);

        final Insumo[] mpEncontrada = {null};

        Button btnBuscar = new Button("Buscar");
        btnBuscar.setStyle(estiloBtnPrincipal());
        btnBuscar.setOnAction(e -> {
            String busqueda = campoBusqueda.getText().trim();
            if (busqueda.isEmpty()) { mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("Ingresa un nombre o numero de partida"); return; }
            Insumo encontrada = listaMateriaPrima.stream()
                .filter(mp -> mp.getNombre().equalsIgnoreCase(busqueda) || mp.getNumeroPartida().equalsIgnoreCase(busqueda))
                .findFirst().orElse(null);
            if (encontrada == null) {
                mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("No se encontro ningun insumo");
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
                insumoDAO.update(mpEncontrada[0]);
                recargarDatos();
                mensajeEstado.setTextFill(Color.web(EXITO));
                mensajeEstado.setText("Se añadieron " + cantidad + " unidades. Nueva existencia: " + mpEncontrada[0].getExistencia());
                labelResultado.setText("Insumo: " + mpEncontrada[0].getNombre() + " | Partida: " + mpEncontrada[0].getNumeroPartida() + " | Existencia: " + mpEncontrada[0].getExistencia());
                campoCantidad.clear();
            } catch (SQLException ex) {
                mensajeEstado.setTextFill(Color.web(ERROR));
                mensajeEstado.setText("Error al actualizar: " + ex.getMessage());
            } catch (NumberFormatException ex) { mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("Ingresa un numero valido"); }
        });

        Button btnCancelar = new Button("Regresar a lista");
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

    // ?"??"? FORMULARIO EDITAR INSUMO ?"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"?
    private void mostrarFormularioEditarMateriaPrima(StackPane contenido) {
        contenido.getChildren().clear();

        Label titulo    = new Label("Editar Insumo");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        Label subtitulo = new Label("Busca por nombre o numero de partida y modifica los campos");
        subtitulo.setFont(Font.font("System", 12));
        subtitulo.setTextFill(Color.web(TEXTO_SUAVE));

        TextField campoBusqueda = crearTextField("Nombre o numero de partida");
        Label mensajeBusqueda   = new Label("");
        mensajeBusqueda.setFont(Font.font("System", 12));

        VBox panelEdicion = new VBox(10);
        panelEdicion.setVisible(false);
        panelEdicion.setManaged(false);

        TextField campoNombre      = crearTextField("Nombre del insumo");
        TextField campoExistencia  = crearTextField("Existencia");
        TextField campoMinimo      = crearTextField("Minimo de existencia");
        TextField campoTipoExist   = crearTextField("Tipo de existencia");
        TextField campoDescripcion = crearTextField("Descripcion");
        TextField campoColor       = crearTextField("Color");
        TextField campoMedida      = crearTextField("Medida");
        TextField campoAncho       = crearTextField("Ancho en metros");
        TextField campoComposicion = crearTextField("Composicion");
        TextField campoTipo        = crearTextField("Tipo de tejido/estructura");
        TextField campoNo          = crearTextField("No.");
        TextField campoTamanio     = crearTextField("Tamano");
        TextField campoTalla       = crearTextField("Talla");
        TextField campoMaterial    = crearTextField("Material");

        Label labelTipoInsumo = new Label("Tipo de insumo:");
        labelTipoInsumo.setTextFill(Color.web(TEXTO_SUAVE));
        labelTipoInsumo.setFont(Font.font("System", 12));

        ComboBox<String> selectorTipoInsumo = new ComboBox<>();
        selectorTipoInsumo.getItems().addAll("Boton", "Cierre", "Tela plana", "Tela punto", "Hilo", "Otro");
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

        final Insumo[] mpEncontrada = {null};

        Button btnBuscar = new Button("Buscar");
        btnBuscar.setStyle(estiloBtnPrincipal());
        btnBuscar.setOnAction(e -> {
            String busqueda = campoBusqueda.getText().trim();
            if (busqueda.isEmpty()) { mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("Ingresa un nombre o numero de partida"); return; }
            Insumo encontrada = listaMateriaPrima.stream()
                .filter(mp -> mp.getNombre().equalsIgnoreCase(busqueda) || mp.getNumeroPartida().equalsIgnoreCase(busqueda))
                .findFirst().orElse(null);
            if (encontrada == null) {
                mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("No se encontro ningun insumo");
                panelEdicion.setVisible(false); panelEdicion.setManaged(false);
            } else {
                mpEncontrada[0] = encontrada;
                mensajeBusqueda.setTextFill(Color.web(EXITO)); mensajeBusqueda.setText("Insumo encontrado");
                campoNombre.setText(encontrada.getNombre());
                campoExistencia.setText(String.valueOf(encontrada.getExistencia()));
                campoMinimo.setText(encontrada.getMinimoExistencia() != null ? String.valueOf(encontrada.getMinimoExistencia()) : "");
                campoTipoExist.setText(encontrada.getTipoExistencia() != null ? encontrada.getTipoExistencia() : "");
                campoDescripcion.setText(encontrada.getDescripcion() != null ? encontrada.getDescripcion() : "");
                campoColor.setText(encontrada.getColor() != null ? encontrada.getColor() : "");
                campoMedida.setText(String.valueOf(encontrada.getMedida()));
                campoAncho.setText(String.valueOf(encontrada.getAncho()));
                campoComposicion.setText(encontrada.getComposicion() != null ? encontrada.getComposicion() : "");
                campoTipo.setText(encontrada.getTipo() != null ? encontrada.getTipo() : "");
                campoNo.setText(String.valueOf(encontrada.getNo()));
                campoTamanio.setText(encontrada.getTamanio() != null ? encontrada.getTamanio() : "");
                campoTalla.setText(String.valueOf(encontrada.getTalla()));
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
            double medidaDouble = medida.isEmpty() ? 0.0 : Double.parseDouble(medida);
            double TallaDouble = talla.isEmpty() ? 0.0 : Double.parseDouble(talla);

            if (nombre.isEmpty() || existStr.isEmpty()) {
                mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("Nombre y existencia son obligatorios"); return;
            }
            try {
                double existencia  = existStr.isEmpty() ? 0.0 : Double.parseDouble(existStr);
                Double minimo = minimoStr.isEmpty() ? null : Double.parseDouble(minimoStr);
                if (existencia < 0) { mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("La existencia no puede ser negativa"); return; }
                double ancho    = anchoStr.isEmpty() ? 0.0 : Double.parseDouble(anchoStr);
                int no      = noStr.isEmpty()    ? 0 : Integer.parseInt(noStr);

                Insumo mp = mpEncontrada[0];
                mp.setNombre(nombre);
                mp.setExistencia(existencia);
                mp.setTipoExistencia(tipoExist.isEmpty()   ? "" : tipoExist);
                mp.setDescripcion(descripcion.isEmpty()    ? "" : descripcion);
                mp.setColor(color.isEmpty()                ? null : color);
                mp.setMedida(medidaDouble);
                mp.setAncho(ancho);
                mp.setComposicion(composicion.isEmpty()    ? null : composicion);
                mp.setTipo(tipo.isEmpty()                  ? null : tipo);
                mp.setNo(no);
                mp.setTamanio(tamanio.isEmpty()            ? null : tamanio);
                mp.setTalla(TallaDouble);
                mp.setMaterial(material.isEmpty()          ? null : material);
                mp.setTipoInsumo(tipoInsumo);
                mp.setMinimoExistencia(minimo);
                insumoDAO.update(mp);
                recargarDatos();
                mensajeEstado.setTextFill(Color.web(EXITO)); mensajeEstado.setText("Insumo actualizado correctamente");
            } catch (SQLException ex) {
                mensajeEstado.setTextFill(Color.web(ERROR));
                mensajeEstado.setText("Error al actualizar: " + ex.getMessage());
            } catch (NumberFormatException ex) {
                mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("Existencia, ancho y No. deben ser numeros validos");
            }
        });

        Button btnCancelar = new Button("Regresar a lista");
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

    // ?"??"? M?"DULO CONJUNTOS VENDIDOS ?"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"?
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
        TableColumn<ConjuntoVendido, String> colFechaDev = new TableColumn<>("Limite Dev.");

        colId.setCellValueFactory(d       -> new SimpleStringProperty(d.getValue().getIdVenta()));
        colNombre.setCellValueFactory(d   -> new SimpleStringProperty(d.getValue().getNombreConjunto()));
        colCantidad.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getCantidad())));
        colTipo.setCellValueFactory(d     -> new SimpleStringProperty(d.getValue().getTipoVenta()));
        colPrecio.setCellValueFactory(d   -> new SimpleStringProperty("$" + String.format("%.2f", d.getValue().getPrecioUnitario())));
        colTotal.setCellValueFactory(d    -> new SimpleStringProperty("$" + String.format("%.2f", d.getValue().getTotal())));
        colFechaVta.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getFechaVenta().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        colFechaDev.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getFechaLimiteDevolucion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

        tabla.getColumns().addAll(colId, colNombre, colCantidad, colTipo, colPrecio, colTotal, colFechaVta, colFechaDev);
        try {
            tabla.setItems(cargarConjuntosVendidosDesdeBD());
        } catch (SQLException ex) {
            mostrarError("Error al cargar ventas de conjuntos: " + ex.getMessage());
            tabla.setItems(FXCollections.observableArrayList());
        }
        VBox.setVgrow(tabla, Priority.ALWAYS);

        Button btnDetalle = new Button("Ver Detalle");
        btnDetalle.setStyle("-fx-background-color: " + PRINCIPAL + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
        btnDetalle.setOnAction(e -> mostrarDetalleConjuntoVendido(contenido, esAdmin));

        VBox vista = new VBox(16, titulo, tabla);
        vista.setStyle("-fx-padding: 30;");
        VBox.setVgrow(tabla, Priority.ALWAYS);
        VBox.setVgrow(vista, Priority.ALWAYS);

        if (esAdmin) {
            Button btnAnadir = new Button("+ Registrar Venta");
            Button btnEditar = new Button("Editar Registro");
            btnAnadir.setStyle("-fx-background-color: " + NARANJA + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
            btnEditar.setStyle("-fx-background-color: " + AZUL_EDITAR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
            btnAnadir.setOnAction(e -> mostrarFormularioNuevoConjuntoVendido(contenido));
            btnEditar.setOnAction(e -> mostrarFormularioEditarConjuntoVendido(contenido));
            Button btnEliminarCV = new Button("✕ Eliminar Registro");
            btnEliminarCV.setStyle("-fx-background-color: " + ERROR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
            btnEliminarCV.setOnAction(e -> {
                ConjuntoVendido sel = tabla.getSelectionModel().getSelectedItem();
                if (sel == null) { mostrarError("Selecciona un registro de la tabla primero"); return; }
                Alert conf = new Alert(Alert.AlertType.CONFIRMATION);
                conf.setTitle("Eliminar registro");
                conf.setHeaderText("¿Eliminar venta \"" + sel.getIdVenta() + "\"?");
                conf.setContentText("Esta acción no se puede deshacer.");
                conf.showAndWait().ifPresent(r -> {
                    if (r == ButtonType.OK) {

                        // TODO: acá va el código para eliminar de la base de datos
                        try {
                           ventaDAO.deleteVenta(Integer.parseInt(sel.getIdVenta()));
                        } catch (SQLException ex) {
                            mostrarError("Error al eliminar registro: " + ex.getMessage());
                            return;
                        }
                        tabla.getItems().remove(sel);
                    }
                });
            });
            HBox botones = new HBox(12, btnAnadir, btnEditar, btnDetalle, btnEliminarCV);
            vista.getChildren().add(botones);
        } else {
            HBox botones = new HBox(12, btnDetalle);
            vista.getChildren().add(botones);
        }

        contenido.getChildren().add(vista);
    }

    // ?"??"? DETALLE CONJUNTO VENDIDO ?"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"?
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
            ConjuntoVendido cv;
            try {
                cv = buscarConjuntoVendidoEnBD(busqueda);
            } catch (SQLException ex) {
                mensajeBusqueda.setTextFill(Color.web(ERROR));
                mensajeBusqueda.setText("Error al consultar ventas: " + ex.getMessage());
                tarjeta.setVisible(false); tarjeta.setManaged(false);
                return;
            }
            if (cv == null) {
                mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("No se encontro el registro");
                tarjeta.setVisible(false); tarjeta.setManaged(false);
            } else {
                mensajeBusqueda.setTextFill(Color.web(EXITO)); mensajeBusqueda.setText("Registro encontrado");
                tarjeta.getChildren().clear();
                Label lNombre = new Label(cv.getNombreConjunto());
                lNombre.setFont(Font.font("System", FontWeight.BOLD, 17));
                lNombre.setTextFill(Color.web(SECUNDARIO));
                Region sep = new Region(); sep.setPrefHeight(1); sep.setStyle("-fx-background-color: #E5E7EB;");

                boolean dentroDevolucion = !LocalDate.now().isAfter(cv.getFechaLimiteDevolucion());
                Label lEstado = new Label(dentroDevolucion ? "Dentro del periodo de devolucion" : "Periodo de devolucion vencido");
                lEstado.setFont(Font.font("System", FontWeight.BOLD, 12));
                lEstado.setTextFill(Color.web(dentroDevolucion ? EXITO : ERROR));

                Label labelPrendasTitulo = new Label("Prendas del conjunto:");
                labelPrendasTitulo.setFont(Font.font("System", FontWeight.BOLD, 12));
                labelPrendasTitulo.setTextFill(Color.web(TEXTO_SUAVE));
                labelPrendasTitulo.setMinWidth(140);

                VBox listaPrendasBox = new VBox(4);
                for (String nombreP : cv.getNombresPrendas()) {
                    Label lP = new Label("- " + nombreP);
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
                    filaDetalle("Limite devolucion:", cv.getFechaLimiteDevolucion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))),
                    filaDetalle("Descripcion:",       cv.getDescripcion()),
                    filaPrendas, lEstado);
                tarjeta.setVisible(true); tarjeta.setManaged(true);
            }
        });

        Button btnRegresar = new Button("Regresar a lista");
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

    // ?"??"? FORMULARIO NUEVO CONJUNTO VENDIDO ?"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"?
    private void mostrarFormularioNuevoConjuntoVendido(StackPane contenido) {
        contenido.getChildren().clear();

        Label titulo    = new Label("Registrar Conjunto Vendido");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        Label subtitulo = new Label("Selecciona el conjunto y captura cantidad/precio unitario");
        subtitulo.setFont(Font.font("System", 12));
        subtitulo.setTextFill(Color.web(TEXTO_SUAVE));

        Label labelConjunto = new Label("Conjunto:");
        labelConjunto.setTextFill(Color.web(TEXTO_SUAVE));
        labelConjunto.setFont(Font.font("System", 12));

        ComboBox<Conjunto> selectorConjunto = new ComboBox<>();
        selectorConjunto.setItems(listaConjuntos);
        selectorConjunto.setMaxWidth(320);
        selectorConjunto.setStyle(estiloInput());
        selectorConjunto.setConverter(new javafx.util.StringConverter<>() {
            @Override public String toString(Conjunto c) {
                return c == null ? "" : c.getNombre();
            }
            @Override public Conjunto fromString(String s) { return null; }
        });

        TextField campoCantidad       = crearTextField("Cantidad");
        TextField campoPrecioUnit     = crearTextField("Precio unitario");

        Label mensajeEstado = new Label("");
        mensajeEstado.setFont(Font.font("System", 12));

        Button btnGuardar = new Button("Guardar Registro");
        btnGuardar.setMaxWidth(320);
        btnGuardar.setStyle("-fx-background-color: " + NARANJA + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 6; -fx-cursor: hand;");

        Button btnCancelar = new Button("Regresar a lista");
        btnCancelar.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXTO_SUAVE + "; -fx-font-size: 12px; -fx-cursor: hand;");
        btnCancelar.setOnAction(e -> mostrarModuloConjuntosVendidos(contenido, true));

        btnGuardar.setOnAction(e -> {
            Conjunto conjuntoSeleccionado = selectorConjunto.getValue();
            String cantStr = campoCantidad.getText().trim();
            String precioStr = campoPrecioUnit.getText().trim();

            if (conjuntoSeleccionado == null || cantStr.isEmpty() || precioStr.isEmpty()) {
                mensajeEstado.setTextFill(Color.web(ERROR));
                mensajeEstado.setText("Selecciona conjunto y completa cantidad/precio");
                return;
            }

            try {
                int cantidad = Integer.parseInt(cantStr);
                double precioUnit = Double.parseDouble(precioStr);
                if (cantidad <= 0 || precioUnit < 0) {
                    mensajeEstado.setTextFill(Color.web(ERROR));
                    mensajeEstado.setText("Cantidad y precio deben ser validos");
                    return;
                }

                List<DetalleVentaConjunto> detallesConjunto = new ArrayList<>();
                detallesConjunto.add(new DetalleVentaConjunto(
                    0,
                    conjuntoSeleccionado.getId(),
                    cantidad,
                    cantidad * precioUnit
                ));

                int folio = ventaDAO.registrarVenta(new ArrayList<>(), detallesConjunto);
                recargarDatos();
                verificarConjuntos();
                mensajeEstado.setTextFill(Color.web(EXITO));
                mensajeEstado.setText("Venta registrada con folio " + folio);
                mostrarModuloConjuntosVendidos(contenido, true);
            } catch (NumberFormatException ex) {
                mensajeEstado.setTextFill(Color.web(ERROR));
                mensajeEstado.setText("Cantidad y precio deben ser numericos");
            } catch (SQLException ex) {
                mensajeEstado.setTextFill(Color.web(ERROR));
                mensajeEstado.setText("Error al guardar venta: " + ex.getMessage());
            }
        });

        VBox form = new VBox(12, titulo, subtitulo, labelConjunto, selectorConjunto,
                campoCantidad, campoPrecioUnit, mensajeEstado, btnGuardar, btnCancelar);
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

    // ?"??"? FORMULARIO EDITAR CONJUNTO VENDIDO ?"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"?
    private void mostrarFormularioEditarConjuntoVendido(StackPane contenido) {
        contenido.getChildren().clear();

        Label titulo    = new Label("Editar Registro de Conjunto Vendido");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        Label subtitulo = new Label("Consulta una venta registrada (edicion deshabilitada para datos transaccionales)");
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
        TextField campoDescripcion    = crearTextField("Descripcion");
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

        Button btnGuardar = new Button("Edicion no disponible");
        btnGuardar.setMaxWidth(320);
        btnGuardar.setStyle("-fx-background-color: " + AZUL_EDITAR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 6; -fx-cursor: hand;");
        btnGuardar.setDisable(true);

        panelEdicion.getChildren().addAll(campoNombreConjunto, campoCantidad, campoPrecioUnit,
                labelTipoVenta, selectorTipo, campoFechaVenta, campoNombresPrendas,
                campoDescripcion, mensajeEstado, btnGuardar);

        final ConjuntoVendido[] cvEncontrado = {null};

        btnBuscar.setOnAction(e -> {
            String busqueda = campoBusqueda.getText().trim();
            if (busqueda.isEmpty()) { mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("Ingresa un ID o nombre"); return; }
            ConjuntoVendido cv;
            try {
                cv = buscarConjuntoVendidoEnBD(busqueda);
            } catch (SQLException ex) {
                mensajeBusqueda.setTextFill(Color.web(ERROR));
                mensajeBusqueda.setText("Error al consultar ventas: " + ex.getMessage());
                panelEdicion.setVisible(false); panelEdicion.setManaged(false);
                return;
            }
            if (cv == null) {
                mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("No se encontro el registro");
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
                mensajeEstado.setTextFill(Color.web(ADVERTENCIA));
                mensajeEstado.setText("Las ventas se administran directamente en BD y no se editan desde este formulario");
                panelEdicion.setVisible(true); panelEdicion.setManaged(true);
            }
        });

        Button btnCancelar = new Button("Regresar a lista");
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

    // ?"??"? FORMULARIO NUEVO CONJUNTO ?"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"?
    private void mostrarFormularioNuevoConjunto(StackPane contenido) {
        contenido.getChildren().clear();

        Label titulo = new Label("Nuevo Conjunto");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        TextField campoId          = crearTextField("ID del conjunto");
        TextField campoNombre      = crearTextField("Nombre del conjunto");
        TextField campoDescripcion = crearTextField("Descripcion");
        TextField campoPiezas      = crearTextField("Numero de piezas");
        TextField campoMinimo      = crearTextField("Minimo de existencia  (ej: 3)");
        TextField campoIdPrendas   = crearTextField("IDs de prendas separados por coma (ej: 1,7)");
        campoIdPrendas.setMaxWidth(400);

        Label mensajeEstado = new Label("");
        mensajeEstado.setFont(Font.font("System", 12));

        Button btnGuardar = new Button("Guardar Conjunto");
        btnGuardar.setMaxWidth(320);
        btnGuardar.setStyle("-fx-background-color: " + NARANJA + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 6; -fx-cursor: hand;");

        Button btnCancelar = new Button("Regresar a lista");
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
            try {
                int idConjunto = Integer.parseInt(id);
                int piezas = Integer.parseInt(piezasStr);
                List<String> ids = new ArrayList<>();
                for (String s : idPrendasStr.split(",")) {
                    String idLimpio = s.trim();
                    if (!listaPrendas.stream().anyMatch(p -> String.valueOf(p.getId()).equals(idLimpio))) {
                        mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("No se encontro la prenda con ID: " + idLimpio); return;
                    }
                    ids.add(idLimpio);
                }
                Double minimo = campoMinimo.getText().trim().isEmpty() ? null : Double.parseDouble(campoMinimo.getText().trim());
                Conjunto nuevoConj = new Conjunto(idConjunto, nombre, piezas, 0.0);
                nuevoConj.setDescripcion(descripcion);
                nuevoConj.setMinimoExistencia(minimo);
                conjuntoDAO.insert(nuevoConj);
                for (String idPrendaTxt : ids) {
                    PrendaConjunto pc = new PrendaConjunto(0, Integer.parseInt(idPrendaTxt), idConjunto);
                    prendaConjuntoDAO.insert(pc);
                }
                recargarDatos();
                mostrarModuloConjuntos(contenido, true);
            } catch (SQLException ex) {
                mensajeEstado.setTextFill(Color.web(ERROR));
                mensajeEstado.setText("Error al guardar: " + ex.getMessage());
            } catch (NumberFormatException ex) {
                mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("El numero de piezas debe ser un numero valido");
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

    // ?"??"? FORMULARIO EDITAR CONJUNTO ?"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"?
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
        TextField campoDescripcion = crearTextField("Descripcion");
        TextField campoPiezas      = crearTextField("Numero de piezas");
        TextField campoMinimo      = crearTextField("Minimo de existencia");
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
                .filter(x -> x.getNombre().equalsIgnoreCase(busqueda) || String.valueOf(x.getId()).equals(busqueda))
                .findFirst().orElse(null);
            if (c == null) {
                mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("No se encontro el conjunto");
                panelEdicion.setVisible(false); panelEdicion.setManaged(false);
            } else {
                conjuntoEncontrado[0] = c;
                mensajeBusqueda.setTextFill(Color.web(EXITO)); mensajeBusqueda.setText("Conjunto encontrado");
campoNombre.setText(c.getNombre()); campoDescripcion.setText(c.getDescripcion());
                campoPiezas.setText(String.valueOf(c.getPiezas())); campoMinimo.setText(c.getMinimoExistencia() != null ? String.valueOf(c.getMinimoExistencia()) : ""); campoIdPrendas.setText(String.join(",", c.getIdPrendas()));
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
                    if (!listaPrendas.stream().anyMatch(p -> String.valueOf(p.getId()).equals(idLimpio))) {
                        mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("No se encontro la prenda con ID: " + idLimpio); return;
                    }
                    ids.add(idLimpio);
                }
                Double minimo = campoMinimo.getText().trim().isEmpty() ? null : Double.parseDouble(campoMinimo.getText().trim());
                conjuntoEncontrado[0].setNombre(nombre); conjuntoEncontrado[0].setDescripcion(descripcion);
                conjuntoEncontrado[0].setPiezas(piezas); conjuntoEncontrado[0].setIdPrendas(ids);
                conjuntoEncontrado[0].setMinimoExistencia(minimo);

                double precioConjunto = ids.stream()
                    .map(id -> listaPrendas.stream()
                        .filter(p -> String.valueOf(p.getId()).equals(id))
                        .findFirst()
                        .orElse(null))
                    .filter(p -> p != null)
                    .mapToDouble(Prenda::getPrecioMayoreo)
                    .sum();
                conjuntoEncontrado[0].setPrecio(precioConjunto);

                conjuntoDAO.update(conjuntoEncontrado[0]);
                prendaConjuntoDAO.deleteByConjunto(conjuntoEncontrado[0].getId());
                for (String idPrenda : ids) {
                    prendaConjuntoDAO.insert(new PrendaConjunto(0, Integer.parseInt(idPrenda), conjuntoEncontrado[0].getId()));
                }

                recargarDatos();
                mostrarModuloConjuntos(contenido, true);
            } catch (SQLException ex) {
                mensajeEstado.setTextFill(Color.web(ERROR));
                mensajeEstado.setText("Error al actualizar: " + ex.getMessage());
            } catch (NumberFormatException ex) {
                mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("El numero de piezas debe ser un numero valido");
            }
        });

        Button btnCancelar = new Button("Regresar a lista");
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

    // ?"??"? FORMULARIO NUEVA PRENDA ?"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"?
    private void mostrarFormularioNuevaPrenda(StackPane contenido) {
        contenido.getChildren().clear();

        Label titulo    = new Label("Nueva Prenda");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        Label subtitulo = new Label("Completa los campos para registrar la prenda");
        subtitulo.setFont(Font.font("System", 12));
        subtitulo.setTextFill(Color.web(TEXTO_SUAVE));

        TextField campoNombre      = crearTextField("Nombre de la prenda");
        TextField campoCodigoBarras         = crearTextField("Codigo de barras");
        TextField campoTipoPrenda  = crearTextField("Tipo de prenda");
        TextField campoDescripcion = crearTextField("Descripcion");
        TextField campoExistencia  = crearTextField("Existencia inicial");
        TextField campoMinimo      = crearTextField("Minimo de existencia  (ej: 5)");
        TextField campoPMayoreo    = crearTextField("Precio mayoreo");
        TextField campoPMenudeo    = crearTextField("Precio menudeo");
Label labelTienda = new Label("Tienda:");
        labelTienda.setTextFill(Color.web(TEXTO_SUAVE));
        labelTienda.setFont(Font.font("System", 12));

        ComboBox<Tienda> selectorTienda = new ComboBox<>();
        selectorTienda.setItems(listaTiendas);
        selectorTienda.setMaxWidth(320);
        selectorTienda.setStyle(estiloInput());
        selectorTienda.setConverter(new javafx.util.StringConverter<Tienda>() {
            @Override public String toString(Tienda t) { return t == null ? "" : t.getNombre(); }
            @Override public Tienda fromString(String s) { return null; }
        });
        Label labelTalla = new Label("Talla:");
        labelTalla.setTextFill(Color.web(TEXTO_SUAVE));
        labelTalla.setFont(Font.font("System", 12));

        ComboBox<String> selectorTalla = new ComboBox<>();
        selectorTalla.getItems().addAll("2", "4", "6", "8", "10", "12", "14", "16", "18", "20", 
    "22", "24", "26", "28", "30", "32", "34", "36", "38", "40", 
    "42", "44", "46", "48", "50", "52", "54", "56", "58", "60", 
    "XS", "S", "M", "L", "XL", "XXL");
        selectorTalla.setValue("M");
        selectorTalla.setMaxWidth(320);
        selectorTalla.setStyle(estiloInput());

        Label mensajeEstado = new Label("");
        mensajeEstado.setFont(Font.font("System", 12));

        Button btnGuardar = new Button("Guardar Prenda");
        btnGuardar.setMaxWidth(320);
        btnGuardar.setStyle("-fx-background-color: " + NARANJA + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 6; -fx-cursor: hand;");

        Button btnCancelar = new Button("Regresar a lista");
        btnCancelar.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXTO_SUAVE + "; -fx-font-size: 12px; -fx-cursor: hand;");
        btnCancelar.setOnAction(e -> mostrarModuloPrendas(contenido, true));

        btnGuardar.setOnAction(e -> {
            String nombre      = campoNombre.getText().trim();
            String talla       = selectorTalla.getValue();

            if (nombre.isEmpty() || campoExistencia.getText().isEmpty()
                    || campoPMayoreo.getText().isEmpty() || campoPMenudeo.getText().isEmpty()) {
                mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("Todos los campos son obligatorios"); return;
            }
            if (listaPrendas.stream().anyMatch(p -> p.getNombre().equalsIgnoreCase(nombre) && p.getTalla().equalsIgnoreCase(talla))) {
                mensajeEstado.setTextFill(Color.web(ADVERTENCIA)); mensajeEstado.setText("Ya existe esa prenda con esa talla. Usa 'Anadir a Existente'."); return;
            }
            try {
                int existencia  = Integer.parseInt(campoExistencia.getText().trim());
                Integer minimo = campoMinimo.getText().trim().isEmpty() ? null : Integer.parseInt(campoMinimo.getText().trim());
                double pMayoreo = Double.parseDouble(campoPMayoreo.getText().trim());
                double pMenudeo = Double.parseDouble(campoPMenudeo.getText().trim());

                Integer idTiendaInt = selectorTienda.getValue() != null ? selectorTienda.getValue().getId() : null;

                String codigoBarras = campoCodigoBarras.getText().trim();
                Prenda nuevaPrenda = new Prenda(nombre, talla, existencia, pMayoreo, pMenudeo, idTiendaInt, codigoBarras);
                nuevaPrenda.setMinimoExistencia(minimo);
                int idGenerado = prendaDAO.insert(nuevaPrenda);
                if (idGenerado > 0) {
                    nuevaPrenda.setId(idGenerado);
                    recargarDatos();
                    mostrarModuloPrendas(contenido, true);
                }
            } catch (SQLException ex) {
                mensajeEstado.setTextFill(Color.web(ERROR));
                mensajeEstado.setText("Error al guardar: " + ex.getMessage());
            } catch (NumberFormatException ex) {
                mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("Existencia y precios deben ser numeros validos");
            }
        });

       VBox form = new VBox(10, titulo, subtitulo, campoNombre, campoCodigoBarras, campoTipoPrenda,
                campoDescripcion, labelTalla, selectorTalla, campoExistencia, campoMinimo , 
                campoPMayoreo, campoPMenudeo, labelTienda, selectorTienda, mensajeEstado, btnGuardar, btnCancelar);       form.setAlignment(Pos.TOP_LEFT); form.setMaxWidth(400);
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


// ---------------FORMULARIO ELIMINAR MATERIAL POR PRENDA -----------------------------
private void mostrarFormularioEliminarMaterialPorPrenda(StackPane contenido) {
    contenido.getChildren().clear();

    Label titulo = new Label("Eliminar Material de Prenda");
    titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
    titulo.setTextFill(Color.web(SECUNDARIO));

    Label nota = new Label("Selecciona la prenda y despues el material que deseas quitar de esa prenda.");
    nota.setFont(Font.font("System", 12));
    nota.setTextFill(Color.web(TEXTO_SUAVE));
    nota.setWrapText(true);

    Label labelPrenda = new Label("Prenda:");
    labelPrenda.setFont(Font.font("System", 12));
    labelPrenda.setTextFill(Color.web(TEXTO_SUAVE));

    ComboBox<Prenda> selectorPrenda = new ComboBox<>();
    selectorPrenda.setItems(listaPrendas);
    selectorPrenda.setMaxWidth(360);
    selectorPrenda.setStyle(estiloInput());
    selectorPrenda.setConverter(new javafx.util.StringConverter<Prenda>() {
        @Override public String toString(Prenda p) { return p == null ? "" : p.getNombre() + " (" + p.getTalla() + ") ID " + p.getId(); }
        @Override public Prenda fromString(String s) { return null; }
    });

    Label labelMaterial = new Label("Material asignado a esa prenda:");
    labelMaterial.setFont(Font.font("System", 12));
    labelMaterial.setTextFill(Color.web(TEXTO_SUAVE));
    labelMaterial.setVisible(false);
    labelMaterial.setManaged(false);

    ComboBox<Insumo> selectorMaterial = new ComboBox<>();
    selectorMaterial.setMaxWidth(360);
    selectorMaterial.setStyle(estiloInput());
    selectorMaterial.setVisible(false);
    selectorMaterial.setManaged(false);
    selectorMaterial.setConverter(new javafx.util.StringConverter<Insumo>() {
        @Override public String toString(Insumo mp) { return mp == null ? "" : mp.getNombre() + " (" + mp.getTipoInsumo() + ") ID " + mp.getNumeroPartida(); }
        @Override public Insumo fromString(String s) { return null; }
    });

    Label mensajeEstado = new Label("");
    mensajeEstado.setFont(Font.font("System", 12));

    Button btnEliminar = new Button("Eliminar Material");
    btnEliminar.setMaxWidth(360);
    btnEliminar.setStyle("-fx-background-color: " + ERROR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 6; -fx-cursor: hand;");
    btnEliminar.setVisible(false);
    btnEliminar.setManaged(false);

    selectorPrenda.setOnAction(e -> {
        Prenda prendaSel = selectorPrenda.getValue();
        boolean haySeleccion = prendaSel != null;
        labelMaterial.setVisible(haySeleccion);   labelMaterial.setManaged(haySeleccion);
        selectorMaterial.setVisible(haySeleccion); selectorMaterial.setManaged(haySeleccion);
        btnEliminar.setVisible(false); btnEliminar.setManaged(false);
        selectorMaterial.setValue(null);
        mensajeEstado.setText("");

        if (haySeleccion) {
            String idPrendaStr = String.valueOf(prendaSel.getId());
            ObservableList<Insumo> materialesDeEstaPrenda = FXCollections.observableArrayList(
                listaMateriaPrima.stream()
                    .filter(mp -> listaMaterialesPorPrenda.stream()
                        .anyMatch(mpp -> mpp.getIdPrenda().equals(idPrendaStr)
                            && mpp.getIdMateriaPrima().equals(String.valueOf(mp.getId()))))
                    .toList()
            );
            selectorMaterial.setItems(materialesDeEstaPrenda);
        }
    });

    selectorMaterial.setOnAction(e -> {
        boolean hayMaterial = selectorMaterial.getValue() != null;
        btnEliminar.setVisible(hayMaterial);
        btnEliminar.setManaged(hayMaterial);
    });

    btnEliminar.setOnAction(e -> {
        Prenda prendaSel = selectorPrenda.getValue();
        Insumo materialSel = selectorMaterial.getValue();
        if (prendaSel == null || materialSel == null) {
            mensajeEstado.setTextFill(Color.web(ERROR));
            mensajeEstado.setText("Selecciona prenda y material");
            return;
        }

        Alert conf = new Alert(Alert.AlertType.CONFIRMATION);
        conf.setTitle("Eliminar material");
        conf.setHeaderText("¿Quitar \"" + materialSel.getNombre() + "\" de \"" + prendaSel.getNombre() + " (" + prendaSel.getTalla() + ")\"?");
        conf.setContentText("Esta acción no se puede deshacer.");
        conf.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                try {

                    insumoPrendaDAO.deleteByInsumoYPrenda(materialSel.getId(), prendaSel.getId());
                    recargarMaterialesPorPrendaDesdeBD();
                    mensajeEstado.setTextFill(Color.web(EXITO));
                    mensajeEstado.setText("Material eliminado correctamente");
                    mostrarMaterialesPorPrenda(contenido);
                } catch (SQLException ex) {
                    mensajeEstado.setTextFill(Color.web(ERROR));
                    mensajeEstado.setText("Error al eliminar: " + ex.getMessage());
                }
            }
        });
    });

    Button btnCancelar = new Button("Regresar sin eliminar");
    btnCancelar.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXTO_SUAVE + "; -fx-font-size: 12px; -fx-cursor: hand;");
    btnCancelar.setOnAction(e -> mostrarMaterialesPorPrenda(contenido));

    VBox form = new VBox(12, titulo, nota,
        labelPrenda, selectorPrenda,
        labelMaterial, selectorMaterial,
        mensajeEstado, btnEliminar, btnCancelar);
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

    
    // ------------------- FORMULARIO AÑADIR A EXISTENTE ----------------------------
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
        TextField campoCantidad = crearTextField("Cantidad a anadir");
        Label mensajeEstado     = new Label("");
        mensajeEstado.setFont(Font.font("System", 12));

        Button btnAnadir = new Button("Anadir Unidades");
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
                .filter(p -> p.getNombre().equalsIgnoreCase(busqueda) || String.valueOf(p.getId()).equals(busqueda))
                .findFirst().orElse(null);
            if (encontrada == null) {
                mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("No se encontro ninguna prenda");
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
                prendaDAO.update(prendaEncontrada[0]);
                recargarDatos();
                mensajeEstado.setTextFill(Color.web(EXITO));
                mensajeEstado.setText("Se añadieron " + cantidad + " unidades. Nueva existencia: " + prendaEncontrada[0].getExistencia());
                labelResultado.setText("Prenda: " + prendaEncontrada[0].getNombre() + " | Talla: " + prendaEncontrada[0].getTalla() + " | Existencia: " + prendaEncontrada[0].getExistencia());
                campoCantidad.clear();
            } catch (SQLException ex) {
                mensajeEstado.setTextFill(Color.web(ERROR));
                mensajeEstado.setText("Error al actualizar: " + ex.getMessage());
            } catch (NumberFormatException ex) { mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("Ingresa un numero valido"); }
        });

        Button btnCancelar = new Button("Regresar a lista");
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

    // ?"??"? FORMULARIO EDITAR PRENDA ?"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"?
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
        TextField campoMinimo      = crearTextField("Minimo de existencia");
        TextField campoTipoPrenda  = crearTextField("Tipo de prenda");
        TextField campoDescripcion = crearTextField("Descripcion");
        Label labelTienda = new Label("Tienda:");
        labelTienda.setTextFill(Color.web(TEXTO_SUAVE));
        labelTienda.setFont(Font.font("System", 12));

        ComboBox<Tienda> selectorTienda = new ComboBox<>();
        selectorTienda.setItems(listaTiendas);
        selectorTienda.setMaxWidth(320);
        selectorTienda.setStyle(estiloInput());
        selectorTienda.setConverter(new javafx.util.StringConverter<Tienda>() {
            @Override public String toString(Tienda t) { return t == null ? "" : t.getNombre(); }
            @Override public Tienda fromString(String s) { return null; }
        });

        Label labelTalla = new Label("Talla:");
        labelTalla.setTextFill(Color.web(TEXTO_SUAVE));
        labelTalla.setFont(Font.font("System", 12));

        ComboBox<String> selectorTalla = new ComboBox<>();
        selectorTalla.getItems().addAll("2", "4", "6", "8", "10", "12", "14", "16", "18", "20", 
    "22", "24", "26", "28", "30", "32", "34", "36", "38", "40", 
    "42", "44", "46", "48", "50", "52", "54", "56", "58", "60", 
    "XS", "S", "M", "L", "XL", "XXL");
        selectorTalla.setMaxWidth(320);
        selectorTalla.setStyle(estiloInput());

        Label mensajeEstado = new Label("");
        mensajeEstado.setFont(Font.font("System", 12));

        Button btnGuardar = new Button("Guardar Cambios");
        btnGuardar.setMaxWidth(320);
        btnGuardar.setStyle("-fx-background-color: " + AZUL_EDITAR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 6; -fx-cursor: hand;");

        panelEdicion.getChildren().addAll(campoNombre, campoPMayoreo, campoPMenudeo, campoExistencia,
                campoMinimo, campoTipoPrenda, campoDescripcion, labelTienda, selectorTienda, labelTalla, selectorTalla, mensajeEstado, btnGuardar);
        final Prenda[] prendaEncontrada = {null};

        Button btnBuscar = new Button("Buscar");
        btnBuscar.setStyle(estiloBtnPrincipal());
        btnBuscar.setOnAction(e -> {
            String busqueda = campoBusqueda.getText().trim();
            if (busqueda.isEmpty()) { mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("Ingresa un nombre o ID"); return; }
            Prenda encontrada = listaPrendas.stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(busqueda) || String.valueOf(p.getId()).equals(busqueda))
                .findFirst().orElse(null);
            if (encontrada == null) {
                mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("No se encontro ninguna prenda");
                panelEdicion.setVisible(false); panelEdicion.setManaged(false);
            } else {
                prendaEncontrada[0] = encontrada;
                mensajeBusqueda.setTextFill(Color.web(EXITO)); mensajeBusqueda.setText("Prenda encontrada");
                campoNombre.setText(encontrada.getNombre()); campoPMayoreo.setText(String.valueOf(encontrada.getPrecioMayoreo()));
                campoPMenudeo.setText(String.valueOf(encontrada.getPrecioMenudeo())); campoExistencia.setText(String.valueOf(encontrada.getExistencia()));
                campoMinimo.setText(encontrada.getMinimoExistencia() != null ? String.valueOf(encontrada.getMinimoExistencia()) : "");               
                campoTipoPrenda.setText(encontrada.getTipoPrenda()); campoDescripcion.setText(encontrada.getDescripcion());
                Tienda tiendaActual = listaTiendas.stream()
                    .filter(t -> encontrada.getIdTienda() != null && t.getId() == encontrada.getIdTienda())
                    .findFirst().orElse(null);
                selectorTienda.setValue(tiendaActual);
                selectorTalla.setValue(encontrada.getTalla());
                mensajeEstado.setText(""); panelEdicion.setVisible(true); panelEdicion.setManaged(true);
            }
        });

        btnGuardar.setOnAction(e -> {
            if (prendaEncontrada[0] == null) return;
            String nombre      = campoNombre.getText().trim();
            String tipoPrenda  = campoTipoPrenda.getText().trim();
            String descripcion = campoDescripcion.getText().trim();
            String talla       = selectorTalla.getValue();
            if (nombre.isEmpty() || campoExistencia.getText().isEmpty() || campoPMayoreo.getText().isEmpty() || campoPMenudeo.getText().isEmpty()) {
                mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("Todos los campos son obligatorios"); return;
            }
            try {
                int existencia  = Integer.parseInt(campoExistencia.getText().trim());
                Integer minimo = campoMinimo.getText().trim().isEmpty() ? null : Integer.parseInt(campoMinimo.getText().trim());
                double pMayoreo = Double.parseDouble(campoPMayoreo.getText().trim());
                double pMenudeo = Double.parseDouble(campoPMenudeo.getText().trim());
                if (existencia < 0) { mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("La existencia no puede ser negativa"); return; }
                Prenda p = prendaEncontrada[0];
                if (existencia == 0) {
                    prendaDAO.delete(p.getId());
                    recargarDatos();
                    verificarConjuntos();
                    mostrarModuloPrendas(contenido, true);
                } else {
                  p.setNombre(nombre);
                    p.setTalla(talla);
                    p.setIdTienda(selectorTienda.getValue() != null ? selectorTienda.getValue().getId() : null);
                    p.setExistencia(existencia);
                    p.setPrecioMayoreo(pMayoreo);
                    p.setPrecioMenudeo(pMenudeo);
                    p.setMinimoExistencia(minimo);
                    prendaDAO.update(p);
                    recargarDatos();
                    verificarConjuntos();
                    mensajeEstado.setTextFill(Color.web(EXITO)); mensajeEstado.setText("Prenda actualizada correctamente");
                }
            } catch (SQLException ex) {
                mensajeEstado.setTextFill(Color.web(ERROR));
                mensajeEstado.setText("Error al actualizar: " + ex.getMessage());
            } catch (NumberFormatException ex) { mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("Existencia y precios deben ser numeros validos"); }
        });

        Button btnCancelar = new Button("Regresar a lista");
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

    // ------------------ PUNTO DE VENTA -------------
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
        selectorTipoBusqueda.getItems().addAll("Todos", "Prendas", "Conjuntos");
        selectorTipoBusqueda.setValue("Todos");
        selectorTipoBusqueda.setStyle(estiloInput());
        selectorTipoBusqueda.setMaxWidth(Double.MAX_VALUE);

        TextField campoBusqueda = new TextField();
        campoBusqueda.setPromptText("Nombre o ID");
        campoBusqueda.setStyle(estiloInput());

        // --------- Lista de autocompletado (no toca BD, solo filtra en memoria) ---------
ListView<String> listaSugerencias = new ListView<>();
listaSugerencias.setPrefHeight(130);
listaSugerencias.setMaxHeight(130);
listaSugerencias.setStyle("-fx-font-size: 12px; -fx-border-color: " + NARANJA + "; -fx-border-width: 1;");
listaSugerencias.setVisible(false);
listaSugerencias.setManaged(false);

campoBusqueda.textProperty().addListener((obs, oldVal, newVal) -> {
    String texto = newVal == null ? "" : newVal.trim().toLowerCase();
    if (texto.isEmpty()) {
        listaSugerencias.setVisible(false);
        listaSugerencias.setManaged(false);
        return;
    }

    List<String> coincidencias = new ArrayList<>();
    String tipoSel = selectorTipoBusqueda.getValue();

    if ("Prendas".equals(tipoSel) || "Todos".equals(tipoSel)) {
        for (Prenda p : listaPrendas) {
            if (p.getNombre().toLowerCase().contains(texto)) {
                coincidencias.add("[Prenda] " + p.getNombre() + " (Talla " + p.getTalla() + ")");
            }
        }
    }
    if ("Conjuntos".equals(tipoSel) || "Todos".equals(tipoSel)) {
        for (Conjunto c : listaConjuntos) {
            if (c.getNombre().toLowerCase().contains(texto)) {
                coincidencias.add("[Conjunto] " + c.getNombre());
            }
        }
    }

    listaSugerencias.setItems(FXCollections.observableArrayList(coincidencias));
    boolean haySugerencias = !coincidencias.isEmpty();
    listaSugerencias.setVisible(haySugerencias);
    listaSugerencias.setManaged(haySugerencias);
});

listaSugerencias.setOnMouseClicked(e -> {
    String seleccion = listaSugerencias.getSelectionModel().getSelectedItem();
    if (seleccion == null) return;

    boolean esPrenda = seleccion.startsWith("[Prenda]");
    String nombreLimpio = seleccion.replaceFirst("^\\[(Prenda|Conjunto)\\]\\s*", "");
    int idxTalla = nombreLimpio.indexOf(" (Talla");
    if (idxTalla > 0) nombreLimpio = nombreLimpio.substring(0, idxTalla);

    campoBusqueda.setText(nombreLimpio);
    selectorTipoBusqueda.setValue(esPrenda ? "Prendas" : "Conjuntos");
    listaSugerencias.setVisible(false);
    listaSugerencias.setManaged(false);
});

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
    listaSugerencias.setVisible(false); listaSugerencias.setManaged(false);

    boolean esConjunto = listaConjuntos.stream().anyMatch(c -> c.getNombre().equalsIgnoreCase(busqueda) || String.valueOf(c.getId()).equals(busqueda));
    boolean buscarComoPrenda = selectorTipoBusqueda.getValue().equals("Prendas")
        || (selectorTipoBusqueda.getValue().equals("Todos") && !esConjunto);

    if (buscarComoPrenda) {
                Prenda encontrada = null;
                try {
                    encontrada = prendaDAO.getByCodigoBarras(busqueda);
                    if (encontrada == null) {
                        encontrada = listaPrendas.stream()
                            .filter(p -> p.getNombre().equalsIgnoreCase(busqueda))
                            .findFirst().orElse(null);
                    }
                    if (encontrada == null) {
                        try { encontrada = prendaDAO.getById(Integer.parseInt(busqueda)); } catch (NumberFormatException ignored) {}
                    }
                } catch (SQLException sqlEx) {
                    mensajeBusqueda.setTextFill(Color.web(ERROR));
                    mensajeBusqueda.setText("Error al buscar: " + sqlEx.getMessage());
                    return;
                }
                if (encontrada == null) {
                    mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("Producto no encontrado");
                    panelResultado.setVisible(false); panelResultado.setManaged(false);
                } else if (encontrada.getExistencia() == 0) {
                    mensajeBusqueda.setTextFill(Color.web(ERROR)); mensajeBusqueda.setText("Sin existencias disponibles");
                    panelResultado.setVisible(false); panelResultado.setManaged(false);
                } else {
                    prendaSeleccionada[0] = encontrada;
                    mensajeBusqueda.setTextFill(Color.web(EXITO)); mensajeBusqueda.setText("Producto encontrado");
                    labelResultado.setText(encontrada.getNombre() + " | Talla: " + encontrada.getTalla());
                    labelStock.setText("Existencia disponible: " + encontrada.getExistencia());
                    labelPrecioInfo.setText("Menudeo: $" + encontrada.getPrecioMenudeo() + "   |   Mayoreo: $" + encontrada.getPrecioMayoreo());
                    mensajeAdd.setText(""); campoCantidad.clear();
                    panelResultado.setVisible(true); panelResultado.setManaged(true);
                }
            } else {
                Conjunto encontrado = listaConjuntos.stream()
                    .filter(c -> c.getNombre().equalsIgnoreCase(busqueda) || String.valueOf(c.getId()).equals(busqueda))
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
                    labelResultado.setText(encontrado.getNombre() + " | " + encontrado.getIdPrendas().size() + " piezas");
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
            } catch (NumberFormatException ex) { mensajeAdd.setTextFill(Color.web(ERROR)); mensajeAdd.setText("Ingresa una cantidad valida"); }
        });

        ScrollPane scrollBusqueda = new ScrollPane(new VBox(10, tituloBusqueda, labelTipoBusqueda, selectorTipoBusqueda, campoBusqueda, listaSugerencias, btnBuscar, mensajeBusqueda, panelResultado));        scrollBusqueda.setFitToWidth(true);
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

        Button btnQuitar = new Button("Quitar seleccionado");
        btnQuitar.setStyle("-fx-background-color: transparent; -fx-text-fill: " + ERROR + "; -fx-font-size: 12px; -fx-cursor: hand; -fx-border-color: " + ERROR + "; -fx-border-radius: 4; -fx-padding: 6 14;");
        btnQuitar.setOnAction(e -> { ItemVenta sel = tablaCarrito.getSelectionModel().getSelectedItem(); if (sel != null) carrito.remove(sel); });

        Label labelTotal = new Label("Total: $0.00");
        labelTotal.setFont(Font.font("System", FontWeight.BOLD, 18));
        labelTotal.setTextFill(Color.web(CAFE));

        carrito.addListener((javafx.collections.ListChangeListener<ItemVenta>) change -> {
            double total = carrito.stream().mapToDouble(ItemVenta::getSubtotal).sum();
            labelTotal.setText("Total: $" + String.format("%.2f", total));
        });

        Button btnCobrar = new Button("Cobrar y Generar Recibo");
        btnCobrar.setStyle("-fx-background-color: " + NARANJA + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 12 24; -fx-background-radius: 6; -fx-cursor: hand;");

        Button btnCancelarVenta = new Button("Cancelar venta");
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

    // -------------- RECIBO-----------------------
    private void mostrarRecibo(StackPane contenido, ObservableList<ItemVenta> carrito) {
        contenido.getChildren().clear();

        LocalDate fechaHoy       = LocalDate.now();
        LocalDate fechaDevolucion = fechaHoy.plusDays(DIAS_DEVOLUCION);
        int folioGenerado;
        try {
            List<DetalleVentaPrenda> detallesPrenda = new ArrayList<>();
            List<DetalleVentaConjunto> detallesConjunto = new ArrayList<>();

            for (ItemVenta item : carrito) {
                if (item.getPrenda() != null) {
                    DetalleVentaPrenda detalle = new DetalleVentaPrenda(
                        0,
                        item.getPrenda().getId(),
                        item.getCantidad(),
                        item.getSubtotal()
                    );
                    detallesPrenda.add(detalle);
                } else if (item.getConjunto() != null) {
                    DetalleVentaConjunto detalle = new DetalleVentaConjunto(
                        0,
                        item.getConjunto().getId(),
                        item.getCantidad(),
                        item.getSubtotal()
                    );
                    detallesConjunto.add(detalle);
                }
            }

            folioGenerado = ventaDAO.registrarVenta(detallesPrenda, detallesConjunto);
            recargarDatos();
            verificarConjuntos();
        } catch (SQLException ex) {
            mostrarError("Error al registrar venta: " + ex.getMessage());
            return;
        }

        double total = carrito.stream().mapToDouble(ItemVenta::getSubtotal).sum();
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        Label tituloRecibo    = new Label("Punto Olayma");
        tituloRecibo.setFont(Font.font("System", FontWeight.BOLD, 20));
        tituloRecibo.setTextFill(Color.web(CAFE));

        Label subtituloRecibo = new Label("RECIBO DE VENTA");
        subtituloRecibo.setFont(Font.font("System", FontWeight.BOLD, 13));
        subtituloRecibo.setTextFill(Color.web(TEXTO_SUAVE));

        Label labelFecha = new Label("Fecha: " + fecha + "   |   Venta #" + folioGenerado);
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

    // --------------- DEVOLUCIONES --------------
    private void mostrarDevoluciones(StackPane contenido) {
        contenido.getChildren().clear();

        Label titulo = new Label("Devoluciones");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        Label subtitulo = new Label("Busca por folio o nombre de prenda/conjunto y procesa devoluciones desde BD");
        subtitulo.setFont(Font.font("System", 12));
        subtitulo.setTextFill(Color.web(TEXTO_SUAVE));

        TextField campoBusqueda = crearTextField("Folio o nombre del producto");
        campoBusqueda.setMaxWidth(420);

        Label mensajeEstado = new Label("");
        mensajeEstado.setFont(Font.font("System", 12));

        TableView<VentaResumen> tabla = new TableView<>();
        tabla.setStyle("-fx-background-color: " + PANEL + "; -fx-border-color: #E5E7EB;");
        tabla.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tabla.setPlaceholder(new Label("Busca ventas para procesar devoluciones"));
        tabla.setMinHeight(260);

        TableColumn<VentaResumen, String> colFolio = new TableColumn<>("Folio");
        TableColumn<VentaResumen, String> colFecha = new TableColumn<>("Fecha Venta");
        TableColumn<VentaResumen, String> colTipo = new TableColumn<>("Tipo");
        TableColumn<VentaResumen, String> colProducto = new TableColumn<>("Producto");
        TableColumn<VentaResumen, String> colTalla = new TableColumn<>("Talla");
        TableColumn<VentaResumen, String> colCantidad = new TableColumn<>("Cant.");
        TableColumn<VentaResumen, String> colTotal = new TableColumn<>("Total");
        TableColumn<VentaResumen, String> colEstado = new TableColumn<>("Estado");

        colFolio.setPrefWidth(80);
        colFecha.setPrefWidth(110);
        colTipo.setPrefWidth(100);
        colProducto.setPrefWidth(220);
        colTalla.setPrefWidth(70);
        colCantidad.setPrefWidth(70);
        colTotal.setPrefWidth(100);
        colEstado.setPrefWidth(100);

        colFolio.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getFolioVenta())));
        colFecha.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getFechaVenta().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
colTipo.setCellValueFactory(d -> {
    String tipo = d.getValue().getTipoItem();
    String texto = "PRENDA".equalsIgnoreCase(tipo) ? "Prenda" : "Conjunto";
    return new SimpleStringProperty(texto);
});        
        colProducto.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombreItem()));
        colTalla.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTalla() == null || d.getValue().getTalla().isEmpty() ? "-" : d.getValue().getTalla()));
        colCantidad.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getCantidadVendida())));
        colTotal.setCellValueFactory(d -> new SimpleStringProperty("$" + String.format("%.2f", d.getValue().getTotal())));
colEstado.setCellValueFactory(d -> {
    String estado = d.getValue().getEstado();
    String texto = "DEVUELTO".equalsIgnoreCase(estado) ? "Devuelto" : "Activo";
    return new SimpleStringProperty(texto);
});
        tabla.getColumns().addAll(colFolio, colFecha, colTipo, colProducto, colTalla, colCantidad, colTotal, colEstado);

        Label labelCantidad = new Label("Cantidad a devolver:");
        labelCantidad.setTextFill(Color.web(TEXTO_SUAVE));
        labelCantidad.setFont(Font.font("System", 12));

        TextField campoCantidad = crearTextField("Cantidad");
        campoCantidad.setMaxWidth(180);

        Button btnBuscar = new Button("Buscar");
        btnBuscar.setStyle(estiloBtnPrincipal());

        Button btnProcesar = new Button("Procesar Devolucion");
        btnProcesar.setStyle("-fx-background-color: " + EXITO + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");

        tabla.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, selected) -> {
            if (selected != null) {
                campoCantidad.setText(String.valueOf(selected.getCantidadVendida()));
            }
        });

        btnBuscar.setOnAction(e -> {
            String busqueda = campoBusqueda.getText().trim();
            if (busqueda.isEmpty()) {
                mensajeEstado.setTextFill(Color.web(ERROR));
                mensajeEstado.setText("Ingresa un folio o nombre para buscar");
                return;
            }
            try {
                ObservableList<VentaResumen> resultados = ventaDAO.buscarVentasResumen(busqueda);
                tabla.setItems(resultados);
                mensajeEstado.setTextFill(resultados.isEmpty() ? Color.web(ADVERTENCIA) : Color.web(EXITO));
                mensajeEstado.setText(resultados.isEmpty() ? "Sin resultados" : "Se encontraron " + resultados.size() + " resultado(s)");
            } catch (SQLException ex) {
                mensajeEstado.setTextFill(Color.web(ERROR));
                mensajeEstado.setText("Error al buscar ventas: " + ex.getMessage());
            }
        });

        btnProcesar.setOnAction(e -> {
            VentaResumen seleccion = tabla.getSelectionModel().getSelectedItem();
            if (seleccion == null) {
                mensajeEstado.setTextFill(Color.web(ERROR));
                mensajeEstado.setText("Selecciona un registro para devolver");
                return;
            }
            if ("DEVUELTO".equalsIgnoreCase(seleccion.getEstado())) {
                mensajeEstado.setTextFill(Color.web(ADVERTENCIA));
                mensajeEstado.setText("Ese registro ya fue devuelto");
                return;
            }
            int cantidad;
            try {
                cantidad = Integer.parseInt(campoCantidad.getText().trim());
            } catch (NumberFormatException ex) {
                mensajeEstado.setTextFill(Color.web(ERROR));
                mensajeEstado.setText("Ingresa una cantidad valida");
                return;
            }

            if ("PRENDA".equalsIgnoreCase(seleccion.getTipoItem())) {
                mostrarFormularioDevolucion(contenido, seleccion.getFolioVenta(), seleccion.getIdItem());
            } else {
                procesarDevolucionConjunto(contenido, seleccion, cantidad);
            }
        });

        HBox acciones = new HBox(10, btnBuscar, labelCantidad, campoCantidad, btnProcesar);
        acciones.setAlignment(Pos.CENTER_LEFT);

        VBox vista = new VBox(14, titulo, subtitulo, campoBusqueda, acciones, mensajeEstado, tabla);
        vista.setStyle("-fx-padding: 30;");

        ScrollPane scroll = new ScrollPane(vista);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(false);
        scroll.setStyle("-fx-background-color: " + FONDO + "; -fx-background: " + FONDO + ";");

        StackPane wrapper = new StackPane(scroll);
        wrapper.setStyle("-fx-background-color: " + FONDO + ";");
        contenido.getChildren().add(wrapper);
    }

    private void mostrarFormularioDevolucion(StackPane contenido, int folioVenta, int idPrenda) {
        contenido.getChildren().clear();

        Label titulo = new Label("Procesar Devolucion de Prenda");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        Label subtitulo = new Label("Confirma la cantidad a devolver. La fecha se registra automaticamente.");
        subtitulo.setFont(Font.font("System", 12));
        subtitulo.setTextFill(Color.web(TEXTO_SUAVE));

        Label labelInfo = new Label("");
        labelInfo.setFont(Font.font("System", 12));
        labelInfo.setTextFill(Color.web(TEXTO));

        TextField campoCantidad = crearTextField("Cantidad a devolver");
        campoCantidad.setMaxWidth(220);

        Label mensajeEstado = new Label("");
        mensajeEstado.setFont(Font.font("System", 12));

        final VentaResumen[] ventaRef = {null};
        try {
            DetalleVentaPrenda detalle = detallePrendaDAO.obtenerPorFolioYPrenda(folioVenta, idPrenda);
            Venta venta = ventaDAO.getById(folioVenta);
            Prenda prenda = prendaDAO.getById(idPrenda);
            if (detalle == null || venta == null || prenda == null) {
                mostrarError("No se pudo cargar la venta/prenda para devolucion");
                mostrarDevoluciones(contenido);
                return;
            }
            ventaRef[0] = new VentaResumen(
                folioVenta,
                venta.getFecha(),
                "PRENDA",
                idPrenda,
                prenda.getNombre(),
                prenda.getTalla(),
                detalle.getCantidad(),
                detalle.getTotal(),
                "ACTIVO"
            );
            labelInfo.setText("Folio: " + folioVenta + " | Prenda: " + prenda.getNombre() + " (" + prenda.getTalla() + ") | Vendido: " + detalle.getCantidad());
            campoCantidad.setText(String.valueOf(detalle.getCantidad()));
        } catch (SQLException ex) {
            mostrarError("Error al cargar datos de devolucion: " + ex.getMessage());
            mostrarDevoluciones(contenido);
            return;
        }

        Button btnProcesar = new Button("Confirmar Devolucion");
        btnProcesar.setStyle("-fx-background-color: " + EXITO + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
        btnProcesar.setOnAction(e -> {
            int cantidad;
            try {
                cantidad = Integer.parseInt(campoCantidad.getText().trim());
            } catch (NumberFormatException ex) {
                mensajeEstado.setTextFill(Color.web(ERROR));
                mensajeEstado.setText("Cantidad invalida");
                return;
            }
            procesarDevolucionPrenda(contenido, ventaRef[0], cantidad);
        });

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXTO_SUAVE + "; -fx-font-size: 12px; -fx-cursor: hand;");
        btnCancelar.setOnAction(e -> mostrarDevoluciones(contenido));

        VBox form = new VBox(12, titulo, subtitulo, labelInfo, campoCantidad, mensajeEstado, btnProcesar, btnCancelar);
        form.setAlignment(Pos.TOP_LEFT);
        form.setMaxWidth(560);
        form.setStyle("-fx-background-color: " + PANEL + "; -fx-padding: 35; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3);");

        StackPane wrapper = new StackPane(form);
        wrapper.setStyle("-fx-background-color: " + FONDO + "; -fx-padding: 30;");
        wrapper.setAlignment(Pos.CENTER);
        contenido.getChildren().add(wrapper);
    }

    private void procesarDevolucionPrenda(StackPane contenido, VentaResumen venta, int cantidad) {
        Connection conn = Conexion.getConnection();
        try {
            boolean initialAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);

            if (!ventaDAO.existeVenta(venta.getFolioVenta())) {
                throw new SQLException("La venta no existe");
            }

            DetalleVentaPrenda detalle = detallePrendaDAO.obtenerPorFolioYPrenda(conn, venta.getFolioVenta(), venta.getIdItem());
            if (detalle == null) {
                throw new SQLException("La prenda no fue vendida en ese folio");
            }
            if (devolucionPrendaDAO.existeDevolucion(conn, venta.getFolioVenta(), venta.getIdItem())) {
                throw new SQLException("Esta prenda ya fue devuelta");
            }
            if (cantidad <= 0 || cantidad > detalle.getCantidad()) {
                throw new SQLException("Cantidad invalida. Maximo permitido: " + detalle.getCantidad());
            }
            if (cantidad != detalle.getCantidad()) {
                throw new SQLException("La estructura actual permite solo devolucion total de la linea vendida (" + detalle.getCantidad() + ")");
            }

            devolucionPrendaDAO.insertar(conn, new DevolucionPrenda(venta.getFolioVenta(), venta.getIdItem(), LocalDate.now()));

            Prenda prenda = prendaDAO.getById(venta.getIdItem());
            if (prenda == null) {
                throw new SQLException("No se encontro la prenda para actualizar stock");
            }
            prenda.setExistencia(prenda.getExistencia() + cantidad);
            prendaDAO.update(prenda);

            conn.commit();
            conn.setAutoCommit(initialAutoCommit);
            recargarDatos();
            mostrarDevoluciones(contenido);
        } catch (SQLException ex) {
            try { conn.rollback(); } catch (SQLException ignored) {}
            mostrarError("Error al procesar devolucion de prenda: " + ex.getMessage());
            try { conn.setAutoCommit(true); } catch (SQLException ignored) {}
        }
    }

    private void procesarDevolucionConjunto(StackPane contenido, VentaResumen venta, int cantidad) {
        Connection conn = Conexion.getConnection();
        try {
            boolean initialAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);

            if (!ventaDAO.existeVenta(venta.getFolioVenta())) {
                throw new SQLException("La venta no existe");
            }

            DetalleVentaConjunto detalle = detalleConjuntoDAO.obtenerPorFolioYConjunto(conn, venta.getFolioVenta(), venta.getIdItem());
            if (detalle == null) {
                throw new SQLException("El conjunto no fue vendido en ese folio");
            }
            if (devolucionConjuntoDAO.existeDevolucion(conn, venta.getFolioVenta(), venta.getIdItem())) {
                throw new SQLException("Este conjunto ya fue devuelto");
            }
            if (cantidad <= 0 || cantidad > detalle.getCantidad()) {
                throw new SQLException("Cantidad invalida. Maximo permitido: " + detalle.getCantidad());
            }
            if (cantidad != detalle.getCantidad()) {
                throw new SQLException("La estructura actual permite solo devolucion total de la linea vendida (" + detalle.getCantidad() + ")");
            }

            devolucionConjuntoDAO.insertar(conn, new DevolucionConjunto(venta.getFolioVenta(), venta.getIdItem(), LocalDate.now()));

            List<PrendaConjunto> prendasConjunto = prendaConjuntoDAO.getByConjunto(venta.getIdItem());
            for (PrendaConjunto pc : prendasConjunto) {
                Prenda prenda = prendaDAO.getById(pc.getIdPrenda());
                if (prenda != null) {
                    prenda.setExistencia(prenda.getExistencia() + cantidad);
                    prendaDAO.update(prenda);
                }
            }

            conn.commit();
            conn.setAutoCommit(initialAutoCommit);
            recargarDatos();
            mostrarDevoluciones(contenido);
        } catch (SQLException ex) {
            try { conn.rollback(); } catch (SQLException ignored) {}
            mostrarError("Error al procesar devolucion de conjunto: " + ex.getMessage());
            try { conn.setAutoCommit(true); } catch (SQLException ignored) {}
        }
    }
 
 

    // ----------- MÓDULO USUARIOS ------------------
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
        TableColumn<Usuario, String> colPass    = new TableColumn<>("Contraseña");
        TableColumn<Usuario, String> colRol     = new TableColumn<>("Rol");
        colNombre.setCellValueFactory(d  -> new SimpleStringProperty(d.getValue().getNombre()));
        colUsuario.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getUsuario()));
        colPass.setCellValueFactory(d    -> new SimpleStringProperty(d.getValue().getPassword()));
colRol.setCellValueFactory(d -> {
    String rol = d.getValue().getRol();
    String capitalizado = (rol == null || rol.isEmpty()) ? rol
        : rol.substring(0, 1).toUpperCase() + rol.substring(1);
    return new SimpleStringProperty(capitalizado);
});        
        tabla.getColumns().addAll(colNombre, colUsuario, colPass, colRol);
        tabla.setItems(listaUsuarios);
        VBox.setVgrow(tabla, Priority.ALWAYS);
        Label mensajeEliminar = new Label("");
        mensajeEliminar.setFont(Font.font("System", 12));
        Button btnNuevo    = new Button("+ Nuevo Usuario");
        Button btnEliminar = new Button("✕ Eliminar Usuario");
        btnNuevo.setStyle(estiloBtnPrincipal());
        btnEliminar.setStyle(
            "-fx-background-color: " + ERROR + "; -fx-text-fill: white; -fx-font-weight: bold;" +
            "-fx-font-size: 13px; -fx-padding: 10 20; -fx-background-radius: 6; -fx-cursor: hand;");
        btnNuevo.setOnAction(e -> mostrarFormularioCrearUsuario(contenido));
        btnEliminar.setOnAction(e -> {
            Usuario seleccionado = tabla.getSelectionModel().getSelectedItem();
            if (seleccionado == null) {
                mensajeEliminar.setTextFill(Color.web(ADVERTENCIA));
                mensajeEliminar.setText("Selecciona un usuario de la tabla primero");
                return;
            }
            if (seleccionado.getUsuario().equals("admin")) {
                mensajeEliminar.setTextFill(Color.web(ERROR));
                mensajeEliminar.setText("No se puede eliminar el administrador principal");
                return;
            }
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("¿Eliminar usuario?");
            confirmacion.setContentText(
                "¿Estás seguro de que deseas eliminar al usuario \""
                + seleccionado.getUsuario() + "\" (" + seleccionado.getNombre() + ")?\n"
                + "Esta acción no se puede deshacer.");
            confirmacion.showAndWait().ifPresent(respuesta -> {
                if (respuesta == ButtonType.OK) {
                    //  acá va el código para eliminar el usuario de la base de datos
                    try{
                        
                        usuarioDAO.delete(seleccionado.getId());
                        listaUsuarios.remove(seleccionado);
                        mensajeEliminar.setTextFill(Color.web(EXITO));
                        mensajeEliminar.setText("Usuario eliminado correctamente");
                    } catch (SQLException ex) {
                        mensajeEliminar.setTextFill(Color.web(ERROR));
                        mensajeEliminar.setText("Error al eliminar usuario: " + ex.getMessage());
                    }
                }
            });
        });
        HBox botones = new HBox(12, btnNuevo, btnEliminar);
        VBox vista = new VBox(16, titulo, tabla, botones, mensajeEliminar);
        vista.setStyle("-fx-padding: 30;");
        VBox.setVgrow(tabla, Priority.ALWAYS);
        VBox.setVgrow(vista, Priority.ALWAYS);
        contenido.getChildren().add(vista);
    }

    // ?"??"? FORMULARIO CREAR USUARIO ?"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"??"?
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
        campoPass.setPromptText("Contrasena"); campoPass.setMaxWidth(320); campoPass.setStyle(estiloInput());

        PasswordField campoPassConfirm = new PasswordField();
        campoPassConfirm.setPromptText("Confirmar contrasena"); campoPassConfirm.setMaxWidth(320); campoPassConfirm.setStyle(estiloInput());

        Label labelRol = new Label("Rol del usuario:");
        labelRol.setTextFill(Color.web(TEXTO_SUAVE)); labelRol.setFont(Font.font("System", 12));

        ComboBox<String> selectorRol = new ComboBox<>();
selectorRol.getItems().addAll("administrador", "encargado");
selectorRol.setValue("encargado"); selectorRol.setMaxWidth(320); selectorRol.setStyle(estiloInput());
selectorRol.setConverter(new javafx.util.StringConverter<String>() {
    @Override public String toString(String rol) {
        if (rol == null) return "";
        return rol.substring(0, 1).toUpperCase() + rol.substring(1);
    }
    @Override public String fromString(String texto) { return texto; }
});
        Label mensajeEstado = new Label("");
        mensajeEstado.setFont(Font.font("System", 12));

        Button btnGuardar = new Button("Crear Usuario");
        btnGuardar.setMaxWidth(320); btnGuardar.setStyle(estiloBtnPrincipal());

        Button btnCancelar = new Button("Regresar a lista");
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
                mensajeEstado.setTextFill(Color.web(ERROR)); mensajeEstado.setText("Las contrasenas no coinciden");
            } else {
                try {
                    usuarioDAO.insert(new Usuario(nombre, usuario, pass, selectorRol.getValue()));
                    recargarDatos();
                    mostrarModuloUsuarios(contenido);
                } catch (SQLException ex) {
                    mensajeEstado.setTextFill(Color.web(ERROR));
                    mensajeEstado.setText("Error al crear usuario: " + ex.getMessage());
                }
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

    //  HELPERS 


private void cambiarEscena(javafx.scene.Parent root) {
    if (scenePrincipal == null) {
        scenePrincipal = new Scene(root);
        stage.setScene(scenePrincipal);
    } else {
        scenePrincipal.setRoot(root);
    }
}

    private Label crearSeccionMenu(String texto) {
        Label label = new Label(texto);
        label.setFont(Font.font("System", FontWeight.BOLD, 10));
        label.setTextFill(Color.web("#64748B"));
        label.setStyle("-fx-padding: 16 16 4 16;");
        label.setMaxWidth(Double.MAX_VALUE);
        return label;
    }

    private void mostrarContenidoVacioEnMenu(StackPane contenido) {
        contenido.getChildren().clear();
        Label placeholder = new Label("Selecciona un módulo del menú");
        placeholder.setTextFill(Color.web("#D1D5DB"));
        placeholder.setFont(Font.font("System", 16));
        contenido.getChildren().add(placeholder);
        limpiarHistorial();
    }



    private void pushHistorial(Runnable anterior) {
        historialNavegacion.push(anterior);
        hayHistorial.set(true);
    }

    private void regresarAtras() {
        if (!historialNavegacion.isEmpty()) {
            historialNavegacion.pop().run();
            hayHistorial.set(!historialNavegacion.isEmpty());
        }
    }

    private void limpiarHistorial() {
        historialNavegacion.clear();
        hayHistorial.set(false);
    }

private HBox crearBarraAtras() {
        Button btnAtras = new Button("← Atrás");
        btnAtras.setStyle(
            "-fx-background-color: transparent; -fx-text-fill: " + TEXTO_SUAVE + ";" +
            "-fx-font-size: 12px; -fx-cursor: hand; -fx-padding: 6 14;" +
            "-fx-border-color: #D1D5DB; -fx-border-radius: 6; -fx-background-radius: 6;");
        btnAtras.setOnMouseEntered(e -> {
            if (!historialNavegacion.isEmpty()) btnAtras.setStyle(
                "-fx-background-color: #F1F5F9; -fx-text-fill: " + SECUNDARIO + ";" +
                "-fx-font-size: 12px; -fx-cursor: hand; -fx-padding: 6 14;" +
                "-fx-border-color: #D1D5DB; -fx-border-radius: 6; -fx-background-radius: 6;");
        });
        btnAtras.setOnMouseExited(e -> btnAtras.setStyle(
            "-fx-background-color: transparent; -fx-text-fill: " + TEXTO_SUAVE + ";" +
            "-fx-font-size: 12px; -fx-cursor: hand; -fx-padding: 6 14;" +
            "-fx-border-color: #D1D5DB; -fx-border-radius: 6; -fx-background-radius: 6;"));
        btnAtras.setOnAction(e -> regresarAtras());
        btnAtras.disableProperty().bind(hayHistorial.not());

        HBox barra = new HBox(btnAtras);
        barra.setStyle(
            "-fx-background-color: " + PANEL + "; -fx-padding: 8 16;" +
            "-fx-border-color: #E5E7EB; -fx-border-width: 0 0 1 0;");
        barra.setAlignment(Pos.CENTER_LEFT);
        barra.visibleProperty().bind(hayHistorial);
        barra.managedProperty().bind(hayHistorial);
        return barra;
    }

private String calcularColorAlerta() {
    boolean hayCritica = false, hayBaja = false;
    for (Prenda p : listaPrendas) {
        if (p.getMinimoExistencia() != null && p.getMinimoExistencia() > 0) {
            if (p.getExistencia() <= p.getMinimoExistencia())          hayCritica = true;
            else if (p.getExistencia() <= p.getMinimoExistencia() * 2) hayBaja = true;
        }
    }
    for (Conjunto c : listaConjuntos) {
        int ex = calcularExistenciaConjunto(c);
        if (c.getMinimoExistencia() != null && c.getMinimoExistencia() > 0) {
            if (ex <= c.getMinimoExistencia())          hayCritica = true;
            else if (ex <= c.getMinimoExistencia() * 2) hayBaja = true;
        }
    }
    for (Insumo mp : listaMateriaPrima) {
        if (mp.getMinimoExistencia() != null && mp.getMinimoExistencia() > 0) {
            if (mp.getExistencia() <= mp.getMinimoExistencia())          hayCritica = true;
            else if (mp.getExistencia() <= mp.getMinimoExistencia() * 2) hayBaja = true;
        }
    }
    if (hayCritica) return ERROR;
    if (hayBaja)    return ADVERTENCIA;
    return EXITO;
}

private String calcularColorAlertaEncargado() {
    boolean hayCritica = false, hayBaja = false;
    for (Prenda p : listaPrendas) {
        if (p.getMinimoExistencia() != null && p.getMinimoExistencia() > 0) {
            if (p.getExistencia() <= p.getMinimoExistencia())          hayCritica = true;
            else if (p.getExistencia() <= p.getMinimoExistencia() * 2) hayBaja = true;
        }
    }
    for (Conjunto c : listaConjuntos) {
        int ex = calcularExistenciaConjunto(c);
        if (c.getMinimoExistencia() != null && c.getMinimoExistencia() > 0) {
            if (ex <= c.getMinimoExistencia())          hayCritica = true;
            else if (ex <= c.getMinimoExistencia() * 2) hayBaja = true;
        }
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

    private BorderPane crearLayoutConBtnAtras(StackPane contenido) {
        Button btnAtras = new Button("← Atrás");
        btnAtras.setStyle(
            "-fx-background-color: transparent; -fx-text-fill: " + TEXTO_SUAVE + ";" +
            "-fx-font-size: 12px; -fx-cursor: hand; -fx-padding: 6 14;" +
            "-fx-border-color: #D1D5DB; -fx-border-radius: 6; -fx-background-radius: 6;");
        btnAtras.setOnMouseEntered(e -> btnAtras.setStyle(
            "-fx-background-color: #F1F5F9; -fx-text-fill: " + SECUNDARIO + ";" +
            "-fx-font-size: 12px; -fx-cursor: hand; -fx-padding: 6 14;" +
            "-fx-border-color: #D1D5DB; -fx-border-radius: 6; -fx-background-radius: 6;"));
        btnAtras.setOnMouseExited(e -> btnAtras.setStyle(
            "-fx-background-color: transparent; -fx-text-fill: " + TEXTO_SUAVE + ";" +
            "-fx-font-size: 12px; -fx-cursor: hand; -fx-padding: 6 14;" +
            "-fx-border-color: #D1D5DB; -fx-border-radius: 6; -fx-background-radius: 6;"));
        btnAtras.setOnAction(e -> regresarAtras());

        HBox barraAtras = new HBox(btnAtras);
        barraAtras.setStyle(
            "-fx-background-color: " + PANEL + "; -fx-padding: 8 16;" +
            "-fx-border-color: #E5E7EB; -fx-border-width: 0 0 1 0;");
        barraAtras.setAlignment(Pos.CENTER_LEFT);

        

        BorderPane layout = new BorderPane();
        layout.setTop(barraAtras);
        layout.setCenter(contenido);
        return layout;
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

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Operacion no completada");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private String verificarLogin(String usuario, String contrasena) {
        try {
            Usuario u = usuarioDAO.getByUsuario(usuario);
            if (u != null && u.getPassword().equals(contrasena)) {
                return u.getRol();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) { launch(); }

    // ── MÓDULO TIENDAS Y UBICACIONES ────────────────────────────────────
private void mostrarModuloTiendasUbicaciones(StackPane contenido, boolean esAdmin) {
    contenido.getChildren().clear();

    Label titulo = new Label("Tiendas y Ubicaciones");
    titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
    titulo.setTextFill(Color.web(SECUNDARIO));

    Label tituloTiendas = new Label("Tiendas");
    tituloTiendas.setFont(Font.font("System", FontWeight.BOLD, 15));
    tituloTiendas.setTextFill(Color.web(SECUNDARIO));

    TableView<Tienda> tablaTiendas = new TableView<>();
    tablaTiendas.setStyle("-fx-background-color: " + PANEL + "; -fx-border-color: #E5E7EB;");
    tablaTiendas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    tablaTiendas.setPlaceholder(new Label("No hay tiendas registradas"));
    tablaTiendas.setMinHeight(160);
    tablaTiendas.setMaxHeight(220);
    tablaTiendas.setItems(listaTiendas);

    TableColumn<Tienda, String> colTId      = new TableColumn<>("ID");
    TableColumn<Tienda, String> colTTipo    = new TableColumn<>("Tipo");
    TableColumn<Tienda, String> colTNombre  = new TableColumn<>("Nombre");
    TableColumn<Tienda, String> colTIdPadre = new TableColumn<>("ID Padre");

    colTId.setCellValueFactory(d      -> new SimpleStringProperty(String.valueOf(d.getValue().getId())));
    colTTipo.setCellValueFactory(d    -> new SimpleStringProperty(d.getValue().getTipo()));
    colTNombre.setCellValueFactory(d  -> new SimpleStringProperty(d.getValue().getNombre()));
    colTIdPadre.setCellValueFactory(d -> new SimpleStringProperty(
        d.getValue().getIdPadre() != 0 ? String.valueOf(d.getValue().getIdPadre()) : "—"));

    tablaTiendas.getColumns().addAll(colTId, colTTipo, colTNombre, colTIdPadre);

    HBox botonesTiendas = new HBox(10);
Button btnNuevaTienda  = new Button("+ Nueva Tienda");
Button btnEditarTienda = new Button("✎ Editar Tienda");
btnNuevaTienda.setStyle("-fx-background-color: " + NARANJA + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 9 18; -fx-background-radius: 6; -fx-cursor: hand;");
btnEditarTienda.setStyle("-fx-background-color: " + AZUL_EDITAR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 9 18; -fx-background-radius: 6; -fx-cursor: hand;");
btnNuevaTienda.setOnAction(e  -> mostrarFormularioTienda(contenido, null, esAdmin));
btnEditarTienda.setOnAction(e -> {
    Tienda sel = tablaTiendas.getSelectionModel().getSelectedItem();
    if (sel != null) mostrarFormularioTienda(contenido, sel, esAdmin);
});
botonesTiendas.getChildren().addAll(btnNuevaTienda, btnEditarTienda);

if (esAdmin) {
    Button btnEliminarTienda = new Button("✕ Eliminar Tienda");
    btnEliminarTienda.setStyle("-fx-background-color: " + ERROR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 9 18; -fx-background-radius: 6; -fx-cursor: hand;");
    btnEliminarTienda.setOnAction(e -> {
        // TODO: acá va el código para eliminar la tienda de la base de datos
    });
    botonesTiendas.getChildren().add(btnEliminarTienda);
}

    VBox vista = new VBox(16, titulo, tituloTiendas, tablaTiendas, botonesTiendas);

    if (esAdmin) {
        Label tituloUbicaciones = new Label("Ubicaciones de Materia Prima");
        tituloUbicaciones.setFont(Font.font("System", FontWeight.BOLD, 15));
        tituloUbicaciones.setTextFill(Color.web(SECUNDARIO));

        Label notaUbicaciones = new Label("Indican dónde se almacena cada insumo dentro de una tienda.");
        notaUbicaciones.setFont(Font.font("System", 12));
        notaUbicaciones.setTextFill(Color.web(TEXTO_SUAVE));

        TableView<Ubicacion> tablaUbicaciones = new TableView<>();
        tablaUbicaciones.setStyle("-fx-background-color: " + PANEL + "; -fx-border-color: #E5E7EB;");
        tablaUbicaciones.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tablaUbicaciones.setPlaceholder(new Label("No hay ubicaciones registradas"));
        tablaUbicaciones.setMinHeight(160);
        tablaUbicaciones.setMaxHeight(220);
        tablaUbicaciones.setItems(listaUbicaciones);

        TableColumn<Ubicacion, String> colUId      = new TableColumn<>("ID");
        TableColumn<Ubicacion, String> colUTipo    = new TableColumn<>("Tipo");
        TableColumn<Ubicacion, String> colUNombre  = new TableColumn<>("Nombre");
        TableColumn<Ubicacion, String> colUIdPadre = new TableColumn<>("ID Tienda (Padre)");

        colUId.setCellValueFactory(d      -> new SimpleStringProperty(String.valueOf(d.getValue().getId())));
        colUTipo.setCellValueFactory(d    -> new SimpleStringProperty(d.getValue().getTipo()));
        colUNombre.setCellValueFactory(d  -> new SimpleStringProperty(d.getValue().getNombre()));
        colUIdPadre.setCellValueFactory(d -> new SimpleStringProperty(
            d.getValue().getIdPadre() != 0 ? String.valueOf(d.getValue().getIdPadre()) : "—"));

        tablaUbicaciones.getColumns().addAll(colUId, colUTipo, colUNombre, colUIdPadre);

        Button btnNuevaUbic  = new Button("+ Nueva Ubicación");
        Button btnEditarUbic = new Button("✎ Editar Ubicación");
        btnNuevaUbic.setStyle("-fx-background-color: " + NARANJA + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 9 18; -fx-background-radius: 6; -fx-cursor: hand;");
        btnEditarUbic.setStyle("-fx-background-color: " + AZUL_EDITAR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 9 18; -fx-background-radius: 6; -fx-cursor: hand;");
        btnNuevaUbic.setOnAction(e  -> mostrarFormularioUbicacion(contenido, null));
        btnEditarUbic.setOnAction(e -> {
            Ubicacion sel = tablaUbicaciones.getSelectionModel().getSelectedItem();
            if (sel != null) mostrarFormularioUbicacion(contenido, sel);
        });

        Button btnEliminarUbic = new Button("✕ Eliminar Ubicación");
        btnEliminarUbic.setStyle("-fx-background-color: " + ERROR + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 9 18; -fx-background-radius: 6; -fx-cursor: hand;");
        btnEliminarUbic.setOnAction(e -> {
            // TODO: acá va el código para eliminar la ubicación de la base de datos
        });

        HBox botonesUbic = new HBox(10, btnNuevaUbic, btnEditarUbic, btnEliminarUbic);
        vista.getChildren().addAll(tituloUbicaciones, notaUbicaciones, tablaUbicaciones, botonesUbic);
    }

    vista.setStyle("-fx-padding: 30;");

    ScrollPane scroll = new ScrollPane(vista);
    scroll.setFitToWidth(true);
    scroll.setFitToHeight(false);
    scroll.setStyle("-fx-background-color: " + FONDO + "; -fx-background: " + FONDO + ";");

    StackPane wrapper = new StackPane(scroll);
    wrapper.setStyle("-fx-background-color: " + FONDO + ";");
    contenido.getChildren().add(wrapper);
}

// ── FORMULARIO TIENDA ────────────────────────────────────────────
private void mostrarFormularioTienda(StackPane contenido, Tienda tiendaEditar, boolean esAdmin) {
    contenido.getChildren().clear();
    boolean esEdicion = tiendaEditar != null;

    Label titulo = new Label(esEdicion ? "Editar Tienda" : "Nueva Tienda");
    titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
    titulo.setTextFill(Color.web(SECUNDARIO));

    TextField campoTipo    = crearTextField("Tipo  (ej: Matriz, Sucursal)");
TextField campoNombre  = crearTextField("Nombre de la tienda");
TextField campoIdPadre = crearTextField("ID Padre  (opcional)");

campoTipo.setMaxWidth(360);
campoNombre.setMaxWidth(360); campoIdPadre.setMaxWidth(360);

if (esEdicion) {
    campoTipo.setText(tiendaEditar.getTipo());
    campoNombre.setText(tiendaEditar.getNombre());
    campoIdPadre.setText(tiendaEditar.getIdPadre() != 0 ? String.valueOf(tiendaEditar.getIdPadre()) : "");
}

    Label mensajeEstado = new Label("");
    mensajeEstado.setFont(Font.font("System", 12));

    Button btnGuardar = new Button(esEdicion ? "Guardar Cambios" : "Guardar Tienda");
    btnGuardar.setMaxWidth(360);
    btnGuardar.setStyle("-fx-background-color: " + (esEdicion ? AZUL_EDITAR : NARANJA) +
        "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 6; -fx-cursor: hand;");

    Button btnCancelar = new Button("← Regresar");
    btnCancelar.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXTO_SUAVE + "; -fx-font-size: 12px; -fx-cursor: hand;");
    btnCancelar.setOnAction(e -> mostrarModuloTiendasUbicaciones(contenido, esAdmin));

    btnGuardar.setOnAction(e -> {
        String tipo    = campoTipo.getText().trim();
        String nombre  = campoNombre.getText().trim();
        int idPadre = campoIdPadre.getText().trim().isEmpty() ? 0 : Integer.parseInt(campoIdPadre.getText().trim());

        if (tipo.isEmpty() || nombre.isEmpty()) {
            mensajeEstado.setTextFill(Color.web(ERROR));
            mensajeEstado.setText("Tipo y nombre son obligatorios");
            return;
        }
        if (!esEdicion) {
            int id = listaTiendas.stream().mapToInt(Tienda::getId).max().orElse(0) + 1;
            tiendaDAO.insert(new Tienda(id, tipo, nombre, idPadre));
            recargarDatos();
        } else {
            tiendaEditar.setTipo(tipo);
            tiendaEditar.setNombre(nombre);
            tiendaEditar.setIdPadre(idPadre);
            tiendaDAO.update(tiendaEditar);
            recargarDatos();
        }
        mostrarModuloTiendasUbicaciones(contenido, esAdmin);
    });

VBox form = new VBox(12, titulo, campoTipo, campoNombre, campoIdPadre,            mensajeEstado, btnGuardar, btnCancelar);
    form.setAlignment(Pos.TOP_LEFT); form.setMaxWidth(420);
    form.setStyle("-fx-background-color: " + PANEL + "; -fx-padding: 35; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3);");

    ScrollPane scroll = new ScrollPane(form);
    scroll.setFitToWidth(false); scroll.setFitToHeight(false);
    scroll.setStyle("-fx-background-color: " + FONDO + "; -fx-background: " + FONDO + ";");

    StackPane wrapper = new StackPane(scroll);
    wrapper.setStyle("-fx-background-color: " + FONDO + "; -fx-padding: 30;");
    wrapper.setAlignment(Pos.CENTER);
    contenido.getChildren().add(wrapper);
}

// ── FORMULARIO UBICACIÓN ─────────────────────────────────────────
private void mostrarFormularioUbicacion(StackPane contenido, Ubicacion ubicacionEditar) {
    contenido.getChildren().clear();
    boolean esEdicion = ubicacionEditar != null;

    Label titulo = new Label(esEdicion ? "Editar Ubicación" : "Nueva Ubicación");
    titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
    titulo.setTextFill(Color.web(SECUNDARIO));

    TextField campoId     = crearTextField("ID  (ej: U1)");
    TextField campoTipo   = crearTextField("Tipo  (ej: Estante, Bodega, Gaveta)");
    TextField campoNombre = crearTextField("Nombre  (ej: Estante A-3)");

    Label labelIdPadre = new Label("Tienda a la que pertenece:");
    labelIdPadre.setFont(Font.font("System", 12));
    labelIdPadre.setTextFill(Color.web(TEXTO_SUAVE));

    ComboBox<String> selectorTienda = new ComboBox<>();
    selectorTienda.setMaxWidth(360);
    selectorTienda.setStyle(estiloInput());
    selectorTienda.getItems().add("— Ninguna —");
    for (Tienda t : listaTiendas) selectorTienda.getItems().add(t.getId() + " — " + t.getNombre());
    selectorTienda.setValue("— Ninguna —");

    campoId.setMaxWidth(360); campoTipo.setMaxWidth(360); campoNombre.setMaxWidth(360);

    if (esEdicion) {
        campoId.setText(String.valueOf(ubicacionEditar.getId())); campoId.setDisable(true);
        campoTipo.setText(ubicacionEditar.getTipo());
        campoNombre.setText(ubicacionEditar.getNombre());
        if (ubicacionEditar.getIdPadre() != 0) {
            String selVal = listaTiendas.stream()
                .filter(t -> t.getId() == ubicacionEditar.getIdPadre())
                .findFirst()
                .map(t -> t.getId() + " — " + t.getNombre())
                .orElse("— Ninguna —");
            selectorTienda.setValue(selVal);
        }
    }

    Label mensajeEstado = new Label("");
    mensajeEstado.setFont(Font.font("System", 12));

    Button btnGuardar = new Button(esEdicion ? "Guardar Cambios" : "Guardar Ubicación");
    btnGuardar.setMaxWidth(360);
    btnGuardar.setStyle("-fx-background-color: " + (esEdicion ? AZUL_EDITAR : NARANJA) +
        "; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 10; -fx-background-radius: 6; -fx-cursor: hand;");

    Button btnCancelar = new Button("← Regresar");
    btnCancelar.setStyle("-fx-background-color: transparent; -fx-text-fill: " + TEXTO_SUAVE + "; -fx-font-size: 12px; -fx-cursor: hand;");
    btnCancelar.setOnAction(e -> mostrarModuloTiendasUbicaciones(contenido, true));

    btnGuardar.setOnAction(e -> {
        String idText = campoId.getText().trim();
        int id     = idText.isEmpty() ? 0 : Integer.parseInt(idText);
        String tipo   = campoTipo.getText().trim();
        String nombre = campoNombre.getText().trim();
        String selTienda = selectorTienda.getValue();
        int idPadre = (selTienda == null || selTienda.equals("— Ninguna —"))
            ? 0 : Integer.parseInt(selTienda.split(" — ")[0].trim());

        if (tipo.isEmpty() || nombre.isEmpty()) {
            mensajeEstado.setTextFill(Color.web(ERROR));
            mensajeEstado.setText("Tipo y nombre son obligatorios");
            return;
        }
        if (!esEdicion) {
            boolean yaExiste = listaUbicaciones.stream().anyMatch(u -> u.getId() == id);
            if (yaExiste) { mensajeEstado.setTextFill(Color.web(ADVERTENCIA)); mensajeEstado.setText("Ya existe una ubicación con ese ID"); return; }
            ubicacionDAO.insert(new Ubicacion(id, tipo, nombre, idPadre));
            recargarDatos();
        } else {
            ubicacionEditar.setTipo(tipo);
            ubicacionEditar.setNombre(nombre);
            ubicacionEditar.setIdPadre(idPadre);
            ubicacionDAO.update(ubicacionEditar);
            recargarDatos();
        }
        mostrarModuloTiendasUbicaciones(contenido, true);
    });

    VBox form = new VBox(12, titulo, campoId, campoTipo, campoNombre,
            labelIdPadre, selectorTienda, mensajeEstado, btnGuardar, btnCancelar);
    form.setAlignment(Pos.TOP_LEFT); form.setMaxWidth(420);
    form.setStyle("-fx-background-color: " + PANEL + "; -fx-padding: 35; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3);");

    ScrollPane scroll = new ScrollPane(form);
    scroll.setFitToWidth(false); scroll.setFitToHeight(false);
    scroll.setStyle("-fx-background-color: " + FONDO + "; -fx-background: " + FONDO + ";");

    StackPane wrapper = new StackPane(scroll);
    wrapper.setStyle("-fx-background-color: " + FONDO + "; -fx-padding: 30;");
    wrapper.setAlignment(Pos.CENTER);
    contenido.getChildren().add(wrapper);
}


    // REGISTRO DE DEVOLUCIONES
    private void mostrarRegistroDevoluciones(StackPane contenido, boolean esAdmin) {
        contenido.getChildren().clear();

        Label titulo = new Label("Registro de Devoluciones");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 20));
        titulo.setTextFill(Color.web(SECUNDARIO));

        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        TableView<DevolucionVista> tablaPrendas = new TableView<>();
        tablaPrendas.setStyle("-fx-background-color: " + PANEL + "; -fx-border-color: #E5E7EB;");
        tablaPrendas.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tablaPrendas.setPlaceholder(new Label("Sin devoluciones de prendas"));

        TableColumn<DevolucionVista, String> colFolioP = new TableColumn<>("Folio");
        TableColumn<DevolucionVista, String> colNombreP = new TableColumn<>("Prenda");
        TableColumn<DevolucionVista, String> colTallaP = new TableColumn<>("Talla");
        TableColumn<DevolucionVista, String> colFechaVentaP = new TableColumn<>("Fecha Venta");
        TableColumn<DevolucionVista, String> colFechaDevP = new TableColumn<>("Fecha Devolucion");

        colFolioP.setPrefWidth(90);
        colNombreP.setPrefWidth(220);
        colTallaP.setPrefWidth(90);
        colFechaVentaP.setPrefWidth(120);
        colFechaDevP.setPrefWidth(140);

        colFolioP.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getFolioVenta())));
        colNombreP.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombrePrenda()));
        colTallaP.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTalla()));
        colFechaVentaP.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getFechaVenta().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        colFechaDevP.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getFechaDevolucion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

        tablaPrendas.getColumns().addAll(colFolioP, colNombreP, colTallaP, colFechaVentaP, colFechaDevP);

        TableView<DevolucionConjuntoVista> tablaConjuntos = new TableView<>();
        tablaConjuntos.setStyle("-fx-background-color: " + PANEL + "; -fx-border-color: #E5E7EB;");
        tablaConjuntos.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tablaConjuntos.setPlaceholder(new Label("Sin devoluciones de conjuntos"));

        TableColumn<DevolucionConjuntoVista, String> colFolioC = new TableColumn<>("Folio");
        TableColumn<DevolucionConjuntoVista, String> colNombreC = new TableColumn<>("Conjunto");
        TableColumn<DevolucionConjuntoVista, String> colFechaVentaC = new TableColumn<>("Fecha Venta");
        TableColumn<DevolucionConjuntoVista, String> colFechaDevC = new TableColumn<>("Fecha Devolucion");

        colFolioC.setPrefWidth(90);
        colNombreC.setPrefWidth(260);
        colFechaVentaC.setPrefWidth(120);
        colFechaDevC.setPrefWidth(140);

        colFolioC.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().getFolioVenta())));
        colNombreC.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getNombreConjunto()));
        colFechaVentaC.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getFechaVenta().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        colFechaDevC.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getFechaDevolucion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

        tablaConjuntos.getColumns().addAll(colFolioC, colNombreC, colFechaVentaC, colFechaDevC);

        try {
            tablaPrendas.setItems(devolucionPrendaDAO.obtenerTodasConDetalles());
            tablaConjuntos.setItems(devolucionConjuntoDAO.obtenerTodasConDetalles());
        } catch (SQLException ex) {
            mostrarError("Error al cargar devoluciones: " + ex.getMessage());
        }

        Tab tabPrendas = new Tab("Prendas", tablaPrendas);
        Tab tabConjuntos = new Tab("Conjuntos", tablaConjuntos);
        tabs.getTabs().addAll(tabPrendas, tabConjuntos);

        Button btnRefrescar = new Button("Refrescar");
        btnRefrescar.setStyle(estiloBtnPrincipal());
        btnRefrescar.setOnAction(e -> mostrarRegistroDevoluciones(contenido, esAdmin));

        VBox vista = new VBox(14, titulo, tabs, btnRefrescar);
        vista.setStyle("-fx-padding: 30;");
        VBox.setVgrow(tabs, Priority.ALWAYS);

        ScrollPane scroll = new ScrollPane(vista);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(false);
        scroll.setStyle("-fx-background-color: " + FONDO + "; -fx-background: " + FONDO + ";");

        StackPane wrapper = new StackPane(scroll);
        wrapper.setStyle("-fx-background-color: " + FONDO + ";");
        contenido.getChildren().add(wrapper);
    }
}