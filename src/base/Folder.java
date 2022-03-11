package base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.List;

public class Folder implements Comparable<Folder>{

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
	
	public List<Note> searchNotes(String keywords){
		// keywords ¡§java or LAB attendance OR SESSION¡¨
		String[] a = keywords.split(" ");
		ArrayList<Note> output1 = new ArrayList<>();
		Boolean or = false;
		
		for(Note i: notes) {
			ArrayList<Boolean> con = new ArrayList<>();
			for(String j: a) {
				
				if(i.getTitle().toLowerCase().contains(j.toLowerCase())) {
					con.add(true);
				}else if(i instanceof TextNote) {
					if(((TextNote) i).content.toLowerCase().contains(j.toLowerCase())){
						con.add(true);
					}else
						con.add(false);
				}else
					con.add(false);
			}
			
			for(int j = 0 ; j < a.length; j++) {
				if(a[j].equalsIgnoreCase("or")) {
					if(con.get(j-1) || con.get(j+1)) {
						or = true;
						j++;
						continue;
					}else {
						or = false;
						break;
					}
				}
				or = con.get(j);
			}
			
			if(or)
				output1.add(i);
		}
		
		return output1;
	}
}
