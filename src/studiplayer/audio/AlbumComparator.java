package studiplayer.audio;

import java.util.Comparator;

public class AlbumComparator implements Comparator<AudioFile> {

    public int compare(AudioFile a1, AudioFile a2) {
    	
		//* Prüft ob eines (oder beide kein) AudioFile(s) kein TaggedFile ist
        if(!(a1 instanceof TaggedFile) && !(a2 instanceof TaggedFile)) {
        	 return 0;
        }
        if(!(a1 instanceof TaggedFile)) {
        	return -1;
        }
        if(!(a2 instanceof TaggedFile)) {
        	return 1;
        }
        
        
        //* Wenn beide TaggedFile sind
//      String alb1 = null; 
//      if(a1 instanceof TaggedFile) {
//          alb1 = ((TaggedFile) a1).getAlbum();
//      }
        String alb1 = a1.getAlbum();
      
//      String alb2 = null; 
//      if(a2 instanceof TaggedFile) {
//          alb2 = ((TaggedFile) a2).getAlbum();
//      }
        String alb2 = a2.getAlbum();


        //* Auch wenn es ein TaggedFile ist, kann album immer noch null sein
	    if(alb1 == null && alb2 == null) {
	        return 0;
	    }
	    if(alb1 == null) {
	        return -1;
	    }
	    if(alb2 == null) {
	        return 1;
	    }
	  
	    return alb1.compareTo(alb2);
    }
}
