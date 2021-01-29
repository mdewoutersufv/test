package org.dis;

import com.google.gson.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class Practica2DisApplication {

	private static final Logger log = LoggerFactory.getLogger(Practica2DisApplication.class);
	public static final String INICIAL_JSON = System.getProperty("user.dir") + "/src/main/java/org/dis/data_inicial.json";
	public static final String DOCUMENTO_JSON = System.getProperty("user.dir") + "/src/main/java/org/dis/data.json";
	public static Videoteca videoteca;

	public static void main(String[] args) {
		SpringApplication.run(Practica2DisApplication.class);
	}

	@Bean
	public CommandLineRunner loadData(PeliculaRepository peliculaRepository, ActorRepository actorRepository) {
		return (args) -> {
			//Cargamos las películas por defecto
			cargarPeliculasJSON(peliculaRepository, actorRepository, INICIAL_JSON);
		};
	}

	public static void cargarPeliculasJSON(PeliculaRepository repoPeli, ActorRepository repoActor, String docJson) throws IOException {
		//Leemos el json desde la dirección que nos pasan
		String doc_json1 = Files.readString(Paths.get(docJson), StandardCharsets.ISO_8859_1);

		JsonObject json1 = JsonParser.parseString(doc_json1).getAsJsonObject();
		//Aquí convertimos el json a un objeto videoteca
		Gson gson = new Gson();
		JsonObject videotecaObj = json1.getAsJsonObject("videoteca");

		//Guardamos los distinto atributos de la videoteca
		String nombre = videotecaObj.get("nombre").getAsString();
		String ubicacion = videotecaObj.get("ubicacion").getAsString();
		String fechaActualizacion = videotecaObj.get("fechaActualizacion").getAsString();
		List<Pelicula> peliculas = new ArrayList<Pelicula>();

		JsonArray peliculasObj = videotecaObj.getAsJsonObject("peliculas").getAsJsonArray("pelicula");

		//Cojemos cada película indicada en el json de la videoteca y guardamos sus atributos
		for (JsonElement elem1 : peliculasObj) {
			JsonObject peliculaObj = elem1.getAsJsonObject();
			String titulo = peliculaObj.get("titulo").getAsString();
			String sinopsis = peliculaObj.get("sinopsis").getAsString();
			String genero = peliculaObj.get("genero").getAsString();
			String enlace = peliculaObj.get("enlace").getAsString();
			int agno = peliculaObj.get("agno").getAsInt();
			int duracion = peliculaObj.get("duracion").getAsInt();
			List<Actor> reparto = new ArrayList<Actor>();

			JsonArray repartoObj = peliculaObj.getAsJsonObject("reparto").getAsJsonArray("actor");

			//Cojemos cada actor indicado en el json del reparto de una película, y lo añadimos a una lista de objetos Actor llamada reparto
			for (JsonElement elem2 : repartoObj) {
				JsonObject actorObj = elem2.getAsJsonObject();
				String nombreActor = actorObj.get("nombre").getAsString();
				String enlaceWikipedia = actorObj.get("enlaceWikipedia").getAsString();
				Actor actor = new Actor(nombreActor, enlaceWikipedia);
				reparto.add(actor);
			}
			//Guardamos la pekícula en un objeto de tipo Pelicula y la añadimos a la lista de películas de la videoteca
			Pelicula pelicula = new Pelicula(titulo, sinopsis, genero, enlace, agno, duracion, reparto);
			peliculas.add(pelicula);
		}

		//Creamos la videoteca a partir de todos los datos extraídos del json
		videoteca = new Videoteca(nombre, ubicacion, peliculas, fechaActualizacion);

		//Vacíamos la base de datos, para luego sobreescibir la lista de película y la de actores con lo que hemos leído del json
		repoPeli.deleteAll();
		repoActor.deleteAll();

		for(Pelicula pelicula : peliculas){
			for(Actor actor : pelicula.getReparto())
				repoActor.save(actor);
			repoPeli.save(pelicula);
		}
	}

	public static void guardarPeliculasJSON(PeliculaRepository repoPeli, String docJSON) throws IOException {
		String json = new String("");

		//Empezamo indicando los datos de la videoteca
		json += "{\"videoteca\":{\"ubicacion\":";
		json += videoteca.getUbicacion();

		//Ahora añadimos los datos de cada una de las películas
		json += ",\"peliculas\":{\"pelicula\":[";
		for(Pelicula p : repoPeli.findAll()){
			json += "{\"genero\":\"";
			json += p.getGenero() + "\",";
			json += "\"duracion\":";
			json += p.getDuracion() + ",";
			json += "\"titulo\":\"";
			json += p.getTitulo() + "\",";
			json += "\"agno\":";
			json += p.getAgno() + ",";
			json += "\"enlace\":\"";
			json += p.getEnlace() + "\",";
			//Ahora añadimos los datos de cada uno de los actores del reparto de cada película
			json += "\"reparto\":{\"actor\":[";
			for(Actor a : p.getReparto()){
				json += "{\"enlaceWikipedia\":\"";
				json += a.getEnlaceWikipedia()+ "\",";
				json += "\"nombre\":\"";
				json += a.getNombre() + "\"},";
			}
			//Nos sobra una coma
			json = json.substring(0, json.length() - 1);
			json += "]},\"sinopsis\":\"";
			json += p.getSinopsis() + "\"},";
		}
		//Nos sobra una coma
		json = json.substring(0, json.length() - 1);
		json += "]},";
		//Ponemos los últimos datos de la videoteca
		json += "\"fechaActualizacion\":\"";
		json += videoteca.getFechaActualizacion() + "\",";
		json += "\"nombre\":\"";
		json += videoteca.getNombre();
		json += "\"}}";

		//Pasamos el String a jsonObject para comprobar que esté correctamente estructurado
		JSONObject jsonObject = new JSONObject(json);
		//Guardamos el json de la videoteca en el archivo de destino
		String jsonPrettyPrintString = jsonObject.toString(4);
		Files.writeString(Paths.get(docJSON), jsonPrettyPrintString, StandardCharsets.ISO_8859_1);
	}

}