package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileServiceImpl implements IUploadFileService{
	
	private final Logger log = LoggerFactory.getLogger(UploadFileServiceImpl.class);
	
	private final static String DIRECTORIO_UPLOAD = "uploads";

	@Override
	public Resource cargar(String nombreFoto) throws MalformedURLException {
		
		Path rutaArchivo = getPath(nombreFoto);
		log.info(rutaArchivo.toString());
		
		Resource recurso = new UrlResource(rutaArchivo.toUri());
		
		if(!recurso.exists() && !recurso.isReadable()) {
			rutaArchivo = Paths.get("src/main/resources/static/images").resolve("no-usuario.png").toAbsolutePath();
			
			recurso = new UrlResource(rutaArchivo.toUri());

			log.error("Error no se pudo cargar la imagen: " + nombreFoto);
			
		}
		return recurso;
	}

	@Override
	public String copiar(MultipartFile archivo) throws IOException {

		//concatenamos un nombre a;eatorio que sea unico para ese usuario asi sea el mismo nmbre del file que sube con UUID.randomUUID()
		//si e archivo tiene espacios en blanco, se reemplazan con nada
		String nombreArchivo = UUID.randomUUID().toString() + "_" +  archivo.getOriginalFilename().replace(" ", "");
		
		Path rutaArchivo = getPath(nombreArchivo);
		log.info(rutaArchivo.toString());

		/*aqui ya subimos l archivo a la ruta escojida*/
		Files.copy(archivo.getInputStream(), rutaArchivo);
		
		return nombreArchivo;
	}

	@Override
	public boolean eliminar(String nombreFoto) {
		
		if(nombreFoto !=null && nombreFoto.length() >0) {
			//obtenemos la foto con su ruta
			Path rutaFotoAnterior = Paths.get("uploads").resolve(nombreFoto).toAbsolutePath();
			/*convertimos a un archivo*/
			File archivoFotoAnterior = rutaFotoAnterior.toFile();
			if(archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
				archivoFotoAnterior.delete();
				return true;
			}
		}
		
		return false;
	}

	@Override
	public Path getPath(String nombreFoto) {
		//este directorio esta ajeno a nuestro proyecto, pero aca lo colocaron dentro y se busca
		//internnamente a traves de sta sintaxi
		//Como esta dentro de nuestro proecto, se define como una ruta relativa a nuestro proyecto "uploads"
		//Si es ruta externa, e debe colocar la ruta absoluta ejemplo: c:\\ruta_absoluta
		return Paths.get(DIRECTORIO_UPLOAD).resolve(nombreFoto).toAbsolutePath();
	}

}
