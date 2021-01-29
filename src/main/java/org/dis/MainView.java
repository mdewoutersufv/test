package org.dis;

import com.google.gson.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;

@CssImport("../src/main/style.css")
@Route
public class MainView extends VerticalLayout {
    private Grid<Pelicula> grid = new Grid<>(Pelicula.class);
    private final PeliculaRepository repo;
    private final ActorRepository actorRepo;
    private final PeliculaEditor editor;

    private TextField filterText = new TextField();
    private Dialog detalles =  new Dialog();
    private VerticalLayout detallesLayout = new VerticalLayout();
    private Button editarButton= new Button("Editar película");

    private HorizontalLayout detallesTitulo = new HorizontalLayout();
    private Paragraph tituloValue = new Paragraph("");
    private HorizontalLayout detallesSinopsis = new HorizontalLayout();
    private Paragraph sinopsisValue = new Paragraph("");
    private HorizontalLayout detallesGenero = new HorizontalLayout();
    private Paragraph generoValue = new Paragraph("");
    private HorizontalLayout detallesEnlace= new HorizontalLayout();
    private Paragraph enlaceValue = new Paragraph("");
    private HorizontalLayout detallesAgno = new HorizontalLayout();
    private Paragraph agnoValue = new Paragraph("");
    private HorizontalLayout detallesDuracion = new HorizontalLayout();
    private Paragraph duracionValue = new Paragraph("");
    private HorizontalLayout detallesActions = new HorizontalLayout();
    Dialog verReparto= new Dialog();
    Button cargarButton = new Button("Cargar películas");
    Button guardarButton = new Button("Guardar películas");
    Button verRepartoButton = new Button("Ver reparto",buttonClickEvent -> {verReparto.open();});
    VerticalLayout verRepartoLayout = new VerticalLayout();


    public MainView(PeliculaRepository repo, ActorRepository actorRepo, PeliculaEditor editor) {
        this.repo = repo;
        this.actorRepo = actorRepo;
        this.editor = editor;

        addClassName("list-view");
        setSizeFull();
        configureFilter();
        configureGrid();
        configureDetalles();
        configureEditor();
        configureGestionJSON();

        add(filterText,grid,detalles,editor,cargarButton,guardarButton);
        updateList(filterText);
    }


    private void configureGrid() {
        grid.addClassName("pelicula-grid");
        grid.setSizeFull();
        grid.setColumns( "peliculaId","titulo", "enlace", "agno", "duracion");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        //Configuramos lo que ocurre al hacer clic en alguna película
        grid.asSingleSelect().addValueChangeListener(e -> {
            if(e.getValue() != null)
            {
                Pelicula p = e.getValue();
                //Sobreescribimos los valores de los párrafos que muestran los detalles de una película con los valores de la película seleccionada
                tituloValue.setText(p.getTitulo());
                sinopsisValue.setText(p.getSinopsis());
                generoValue.setText(p.getGenero());
                enlaceValue.setText(p.getEnlace());
                agnoValue.setText(Integer.toString(p.getAgno()));
                duracionValue.setText(Integer.toString(p.getDuracion()));
                //Configuramos el layout de los detalles del reparto
                List<Actor> reparto = p.getReparto();
                configureVerActorLayout(reparto);
                //Configuramos el botón para abrir el editor de películas
                editarButton.addClickListener(ev -> {
                    detalles.close();
                    editor.editPelicula(p);
                });
                detalles.open();
            }
        });
    }

    private void updateList(TextField filterText) {
        if (StringUtils.isEmpty(filterText.getValue())) {
            grid.setItems(repo.findAll());
        }
        else {
            grid.setItems(repo.findByTituloStartsWithIgnoreCase(filterText.getValue()));
        }
    }

    private void configureFilter() {
        filterText.setPlaceholder("Filtrar por título...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList(filterText));
    }

    private void configureDetalles( ){

        detalles.setCloseOnOutsideClick(false);
        detalles.setCloseOnEsc(false);
        //Asociamos a cada atributo de la lase Pelicula un layout horizontal, al que añadimos el nombre del atibuto y su valor
        Paragraph tituloParagraph = new Paragraph("Titulo: ");
        tituloParagraph.setClassName("componente");
        detallesTitulo.add(tituloParagraph);
        detallesTitulo.add(tituloValue);
        Paragraph sinopsisParagraph = new Paragraph("Sinopsis: ");
        sinopsisParagraph.setClassName("componente");
        detallesSinopsis.add(sinopsisParagraph);
        detallesSinopsis.add(sinopsisValue);
        Paragraph generoParagraph = new Paragraph("Genero: ");
        generoParagraph.setClassName("componente");
        detallesGenero.add(generoParagraph);
        detallesGenero.add(generoValue);
        Paragraph enlaceParagraph = new Paragraph("Enlace: ");
        enlaceParagraph.setClassName("componente");
        detallesEnlace.add(enlaceParagraph);
        detallesEnlace.add(enlaceValue);
        Paragraph agnoParagraph = new Paragraph("Agno: ");
        agnoParagraph.setClassName("componente");
        detallesAgno.add(agnoParagraph);
        detallesAgno.add(agnoValue);
        Paragraph duracionParagraph = new Paragraph("Duracion: ");
        duracionParagraph.setClassName("componente");
        detallesDuracion.add(duracionParagraph);
        detallesDuracion.add(duracionValue);

        //Añadimos los layout horizontales previamnete definidos al layout de la ventana de detalles
        detallesLayout.add(detallesTitulo,detallesSinopsis,detallesGenero,detallesEnlace,detallesAgno,detallesDuracion,verRepartoButton);

        //Añadimos una función que haga que, al hacer clic en el botón "Cerrar" se cierre la ventana de detalles
        detalles.add(detallesLayout);
        detallesActions.add(new Button("Cerrar", event -> {
            detalles.close();
        }));
        detallesActions.add(editarButton);
        detalles.add(detallesActions);
    }

    private void configureVerActorLayout(List<Actor> reparto){
        verRepartoLayout.removeAll();
        //Añadimos a layout que permite ver un reparto los detalles de cada uno de sus actores
        for(int i = 0; i<reparto.size(); i++) {
            H3 head = new H3("Actor " + (i + 1));
            Actor a = reparto.get(i);
            Paragraph nombre = new Paragraph("Nombre: " + a.getNombre());
            Paragraph enlace = new Paragraph("Enlace a la wikipedia: " + a.getEnlaceWikipedia());
            verRepartoLayout.add(head, nombre, enlace);

        }
        verReparto.removeAll();
        verReparto.add(verRepartoLayout);
    }

    private void configureEditor(){
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            updateList(filterText);
        });
    }

    private void configureGestionJSON(){
        //Añadimos una función que haga que, al hacer clic en el botón "Cargarm películas" se carguen las películas desde un archivo JSON
        cargarButton.addClickListener(e->{
            try {
                Practica2DisApplication.cargarPeliculasJSON(repo,actorRepo,Practica2DisApplication.DOCUMENTO_JSON);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            updateList(filterText);
        });

        //Añadimos una función que haga que, al hacer clic en el botón "guardar películas" se guarden las películas desde un archivo JSON
        guardarButton.addClickListener(e->{
            try {
                Practica2DisApplication.guardarPeliculasJSON(repo,Practica2DisApplication.DOCUMENTO_JSON);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

}