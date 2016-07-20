package it.unifi.ing.pc.downloader;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import javax.net.ssl.SSLHandshakeException;

import it.unifi.ing.pc.utilities.UserAgents;

public class ImageDownloader {

	private String destinationFolder;
	
	public ImageDownloader(String destinationFolder) {
		this.destinationFolder = destinationFolder;
	}
	
	public void saveImage(String imageUrl) throws IOException {
		String destinationFile = destinationFolder + imageUrl.substring(imageUrl.lastIndexOf("/"));
		URL url = new URL(imageUrl);

		try{
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestProperty("User-Agent", UserAgents.USER_AGENTS);
			con.setConnectTimeout(2000);
			InputStream is = con.getInputStream();
			OutputStream os = new FileOutputStream(destinationFile);

			byte[] b = new byte[2048];
			int length;
			while ((length = is.read(b)) != -1) {
				os.write(b, 0, length);
			}
			is.close();
			os.close();
		} catch (FileNotFoundException | SocketTimeoutException | SSLHandshakeException e) {
			System.out.println("Immagine non trovata");

		}
		
	}
	
}
