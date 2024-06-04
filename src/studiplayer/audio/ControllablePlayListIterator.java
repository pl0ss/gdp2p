package studiplayer.audio;

import java.util.LinkedList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class ControllablePlayListIterator implements Iterator<AudioFile> {
	private List<AudioFile> files;
	private int pos = 0;
	
	public ControllablePlayListIterator(List<AudioFile> files) {
		this.files = files;
	}
	
	public ControllablePlayListIterator(List<AudioFile> files, String search, SortCriterion sortCriterion) {
		List<AudioFile> filteredFiles = getFilesIncludeSearch(files, search);
        this.files = getFilesSorted(filteredFiles, sortCriterion);
	}
	
	
	public boolean hasNext() {
		return pos < files.size();
	}

	public AudioFile next() {
		return files.get(pos++);
	}

	public AudioFile jumpToAudioFile(AudioFile file) {
		if(files.indexOf(file) >= 0) {
			pos = files.indexOf(file); 
			pos++;
			return file;
		}
		
		return null;
	}
	
	public List<AudioFile> getFilesIncludeSearch(List<AudioFile> files, String search) {
		Boolean caseSensitive = false; //* eine eigene Idee für die Suche in VA10
		
		if(search == null || search.equals("")) {
			return files;
		}
		
		List<AudioFile> filteredFiles = new LinkedList<>();

		if(!caseSensitive) {
			search = search.toLowerCase(); 
		}

		for(AudioFile a : files) {
			String autor = a.getAuthor();
			if(autor != null) {
				if(!caseSensitive) {
					autor = autor.toLowerCase();
				}
				
				if(autor.contains(search)) {
					filteredFiles.add(a);
					continue;
				}
			}

			
			String title = a.getTitle().toLowerCase();
			if(title != null) {
				if(!caseSensitive) {
					title = title.toLowerCase();
				}
				
				if(title.contains(search)) {
					filteredFiles.add(a);
					continue;
				}
			}

			
			String album = a.getAlbum();
			if(album != null) {
				if(!caseSensitive) {
					album = album.toLowerCase();
				}
				
				if(album != null && album.contains(search)) {
					filteredFiles.add(a);
					continue;
				}
			}
		}
		
		return filteredFiles;
	}
	
	public static List<AudioFile> getFilesSorted(List<AudioFile> files, SortCriterion sortCriterion) {
		List<AudioFile> filteredFiles = new LinkedList<>(files);
		
		if(sortCriterion.equals(SortCriterion.DEFAULT)) {
			
		} else if(sortCriterion.equals(SortCriterion.AUTHOR)) {
			filteredFiles.sort(new AuthorComparator());
		} else if(sortCriterion.equals(SortCriterion.TITLE)) {
			filteredFiles.sort(new TitleComparator());
		} else if(sortCriterion.equals(SortCriterion.ALBUM)) {
			filteredFiles.sort(new AlbumComparator());
		} else if(sortCriterion.equals(SortCriterion.DURATION)) {
			filteredFiles.sort(new DurationComparator());
		}
		
		return filteredFiles;
	}
	
	public List<AudioFile> getFiles() {
	    return this.files;
	}
}
