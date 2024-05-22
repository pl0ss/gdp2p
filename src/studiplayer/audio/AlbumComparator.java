package studiplayer.audio;

import java.util.Comparator;

public class AlbumComparator implements Comparator<AudioFile> {

    public int compare(AudioFile a1, AudioFile a2) {
        if(!(a1 instanceof TaggedFile) && !(a2 instanceof TaggedFile)) {
        	 return 0;
        }
        if(!(a1 instanceof TaggedFile)) {
        	return -1;
        }
        if(!(a2 instanceof TaggedFile)) {
        	return 1;
        }
        
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
      
//      System.out.println("AlbumComparator: " + a1 + " - " + alb1 + " ||| " + a2 + " - " + alb2);

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
