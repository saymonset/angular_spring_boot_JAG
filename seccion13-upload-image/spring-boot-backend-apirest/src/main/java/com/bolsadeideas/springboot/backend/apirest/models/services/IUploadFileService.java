package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IUploadFileService {

	//reutilizar esta interface para subir imagenes en el sistemz operativo directamente
	public Resource cargar(String nombreFoto) throws MalformedURLException;
	public String copiar(MultipartFile archivo) throws IOException;
	public boolean eliminar(String nombreFoto);
	public Path getPath(String nombreFoto);
}
