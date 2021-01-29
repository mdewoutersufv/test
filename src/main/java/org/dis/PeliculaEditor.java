package org.dis;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


@SpringComponent
@UIScope
public class PeliculaEditor extends VerticalLayout implements KeyNotifier {

   private final PeliculaRepository repository;
   private final ActorRepository actorRepo;


    private Pelicula pelicula;

    /* Fields to edit properties in Pelicula entity */
    TextField titulo = new TextField("Titulo");
    TextField sinopsis = new TextField("Sinopsis");
    TextField genero = new TextField("Genero");
    TextField enlace = new TextField("Enlace");
    IntegerField agno = new IntegerField("Agno");
    IntegerField duracion = new IntegerField("Duracion");
    Paragraph reparto = new Paragraph("Reparto");

    /* Action buttons */
    Button save = new Button("Guardar cambios", VaadinIcon.CHECK.create());
    Button cancel = new Button("Cancelar edición");
    Button delete = new Button("Eliminar película", VaadinIcon.TRASH.create());
    Dialog editarReparto= new Dialog();
    Button editarRepartoButton = new Button("Editar reparto",buttonClickEvent -> {editarReparto.open();});
    VerticalLayout editarRepartoLayout = new VerticalLayout();
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<Pelicula> binder = new Binder<>(Pelicula.class);
    private ChangeHandler changeHandler;

    @Autowired
    public PeliculaEditor(PeliculaRepository repository, ActorRepository actorRepo) {
        this.repository = repository;
        this.actorRepo = actorRepo;

        add(titulo, sinopsis, genero, enlace, agno, duracion,reparto,editarReparto,editarRepartoButton, actions);

        // bind using naming convention
        binder.bindInstanceFields(this);

        // Configure and style components
        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        // wire action buttons to save, delete and reset
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> changeHandler.onChange());
        setVisible(false);


    }

    void delete() {
        repository.delete(pelicula);
        changeHandler.onChange();
    }

    void save() {
        repository.save(pelicula);
        changeHandler.onChange();
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editPelicula(Pelicula c) {
        if (c == null) {
            setVisible(false);
            return;
        }
        else {
            pelicula = c;
        }

        // Bind pelicula properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        // moving values from fields to entities before saving
        binder.setBean(pelicula);

        setVisible(true);

        // Pone el foco en el título
        titulo.focus();
        configureEditarReparto(pelicula);

    }

    public void configureEditarReparto(Pelicula pelicula){
        List<Actor> reparto = pelicula.getReparto();
        TextField nombreField = new TextField("Nombre");
        TextField enlaceField = new TextField("Enlace a la wikipedia");

        Button aniadirActorButton = new Button("Añadir actor");
        aniadirActorButton.addClickListener(e->{
            boolean existe = false;
            if(!nombreField.isEmpty() && !enlaceField.isEmpty()){
                String nombre = nombreField.getValue();
                for(Actor a : pelicula.getReparto()) {
                    if (a.getNombre().equals(nombre))
                        existe = true;
                }
                if(!existe) {
                    String enlace = enlaceField.getValue();
                    Actor a = new Actor(nombre, enlace);
                    actorRepo.save(a);
                    pelicula.aniadirActor(a);
                    //repository.save(pelicula);
                    aniadirActorALayout(reparto, reparto.size() - 1);
                    nombreField.clear();
                    enlaceField.clear();
                }
            }
        });

        HorizontalLayout aniadirActorLayout = new HorizontalLayout();
        aniadirActorLayout.add(aniadirActorButton);
        editarRepartoLayout.removeAll();
        editarRepartoLayout.add(new H3("Añadir actor:"),nombreField,enlaceField,aniadirActorLayout);

        for(int i=0;i<reparto.size();i++){
            aniadirActorALayout(reparto,i);
        }
        editarReparto.removeAll();
        editarReparto.add(editarRepartoLayout);
    }

    public void aniadirActorALayout(List<Actor> reparto ,int pos){
        H3 head = new H3("Actor " + (pos+1));
        Actor a = reparto.get(pos);
        Paragraph nombre= new Paragraph("Nombre: " + a.getNombre());
        Paragraph enlace= new Paragraph("Enlace a la wikipedia: " + a.getEnlaceWikipedia());
        Button b = new Button("Eliminar", VaadinIcon.TRASH.create());
        b.addClickListener(e->{
            b.getElement().setAttribute("theme","error");
            b.setText("Eliminado");
            pelicula.eliminarActor(a);
        });
        editarRepartoLayout.add(head,nombre,enlace,b);
    }

    public void setChangeHandler(ChangeHandler h) {
        // ChangeHandler is notified when either save or delete
        // is clicked
        changeHandler = h;
    }
}