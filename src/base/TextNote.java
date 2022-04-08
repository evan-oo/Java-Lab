package base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Serializable;

public class TextNote extends Note {

	public String content ;
	
	public TextNote(String title) {
		super(title) ;
	}
	
	public TextNote(String title, String content) {
		super(title) ;
		this.content = content;
	}
	
	/**
	* load a TextNote from File f
	*
	* the tile of the TextNote is the name of the file
	* the content of the TextNote is the content of the file
	*
	* @param File f
	*/
	public TextNote(File f) {
		super(f.getName());
		this.content = getTextFromFile(f.getAbsolutePath());
	}


	public String getContent(){
		return this.content;
	}
	/**
	* get the content of a file
	*
	* @param absolutePath of the file
	* @return the content of the file
	*/
	private String getTextFromFile(String absolutePath) {
		String result = "";
		FileInputStream file = null;
		InputStreamReader input = null;
		try {
			file = new FileInputStream(absolutePath);
			input = new InputStreamReader(file);
			
			int t;
			while((t = input.read()) != -1)
			{
				result = result + (char)t ;
			}
			
			input.close();
			file.close();
		} catch(FileNotFoundException e) {
			System.out.println("NO Such File Exists");
		} catch(IOException e) {
			System.out.println("IOException occurred");
		}
		// TODO
		return result;
	}
	
	/**
	* export text note to file
	*
	*
	* @param pathFolder path of the folder where to export the note
	* the file has to be named as the title of the note with extension ".txt"
	*
	* if the tile contains white spaces " " they has to be replaced with underscores "_"
	*
	*
	*/
	public void exportTextToFile(String pathFolder) {
		String title = this.getTitle().replaceAll(" ", "_");
		if(pathFolder == "")
			pathFolder = ".";
		
		try {
			File file = new File(pathFolder + File.separator + title +".txt");
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			writer.write(content);
			writer.close();
			
		}catch(IOException e) {
			System.out.println("IOException occurred");
		}
	}
	
	
}
