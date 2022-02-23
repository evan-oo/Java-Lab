package base;

import java.util.ArrayList;
import java.util.Objects;

public class Folder {

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
}
