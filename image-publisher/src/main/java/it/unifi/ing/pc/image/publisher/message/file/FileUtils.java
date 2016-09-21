package it.unifi.ing.pc.image.publisher.message.file;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

	private String file;
	
	public FileUtils(String file) {
		this.file = file;
	}
	
	public void append(List<String> results) {
		try {
			Files.write(Paths.get(file), results, UTF_8, APPEND, CREATE);
			
		} catch(IOException e) {
			
		}
	}
	
	public List<String> read(){
		BufferedReader br = null;
		List<String> lines = new ArrayList<String>();
		
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(file));
			while ((sCurrentLine = br.readLine()) != null) {
				lines.add(sCurrentLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return lines;
	}
	
}
