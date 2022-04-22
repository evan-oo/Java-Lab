package base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.List;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Folder implements Comparable<Folder>, Serializable{

	private ArrayList<Note> notes;
	private String name;
	
	public Folder(String name) {
		notes = new ArrayList<Note>() ;
		this.name = name ;
	}
	
	public void addNote(Note e) {
		notes.add(e);
	}
	
	public ArrayList<Note> getNotes(){
		return notes;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Folder))
			return false;
		Folder other = (Folder) obj;
		return Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		int nText = 0;
		int nImage = 0;
		for (Note n: notes) {
			if (n instanceof TextNote)
				nText++;
			else if(n instanceof ImageNote)
				nImage++;
		}
		return name + ":" + nText + ":" + nImage;
	}
	
	public int compareTo(Folder o) {
		return this.name.compareTo(o.name);
	}
	
	public void sortNotes() {
		Collections.sort(notes);
	}

	public ArrayList<Note> searchNotes(String keywords){
		String keywordsLower = keywords.toLowerCase();
		String[] strArr = keywordsLower.split(" or ");
		String strNew = String.join(",", strArr);
		String[] arrOfStr = strNew.split(" ");
		String[][] arrOfStrFinal = new String [arrOfStr.length][];
		boolean satisfy = false;
		int satisfyCount = 0;
		ArrayList<Note> keynote = new ArrayList<Note>();
		for(int i=0; i < arrOfStr.length; i++){
			arrOfStrFinal[i] = arrOfStr[i].split(",");
		}
		for (Note i: notes){
			if (i instanceof TextNote){
				satisfyCount = 0;
				TextNote textnote = (TextNote) i;
				String titleLower = textnote.getTitle().toLowerCase();
				String contentLower = textnote.getContent().toLowerCase();
				for(int j=0; j < arrOfStr.length; j++){
					satisfy = false;
					for(int k=0; k < arrOfStrFinal[j].length; k++){
						if (titleLower.contains(arrOfStrFinal[j][k]) || contentLower.contains(arrOfStrFinal[j][k])){
							// System.out.println(arrOfStrFinal[j][k]);
							satisfy = true;
						}
					}
					if (satisfy){
						satisfyCount++;
					}
				}
				if (satisfyCount == arrOfStr.length){
					keynote.add(i);
				}
			}
			else {
				satisfyCount = 0;
				String titleLower = i.getTitle().toLowerCase();
				for(int j=0; j < arrOfStr.length; j++){
					satisfy = false;
					for(int k=0; k < arrOfStrFinal[j].length; k++){
						if (titleLower.contains(arrOfStrFinal[j][k])){
							// System.out.println(arrOfStrFinal[j][k]);
							satisfy = true;
						}
					}
					if (satisfy){
						satisfyCount++;
					}
				}
				if (satisfyCount == arrOfStr.length){
					keynote.add(i);
				}
			}
		}
		return keynote;
	}
	
	/**
	* method to save the NoteBook instance to file
	*
	* @param file, the path of the file where to save the object serialization
	* @return true if save on file is successful, false otherwise
	*/
	public boolean save(String file) {
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		
		try {
			fos = new FileOutputStream(file);
			out = new ObjectOutputStream(fos);
			out.writeObject(this);
			out.close();
		}catch(Exception e) {	
			return false;
		}
		return true;
	}

	public boolean removeNotes(String title){
		for(int i = 0; i< notes.size(); i++){
			if(notes.get(i).getTitle().equals(title)){
				notes.remove(i);
				return true;
			}
		}
		return false;
	}
}
