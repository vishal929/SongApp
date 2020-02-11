//todo: create txt file with songs if there is none,write changes to the txt file

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;


public class SongController{


	@FXML ListView<String> listView;
	@FXML Button deleteSongButton;
	@FXML Button editDetailsButton;
	@FXML Button addSongButton;
	@FXML TextField songName;
	@FXML TextField artistName;
	@FXML TextField albumName;
	@FXML TextField songYear;

	//private list of songs for the app
	private ObservableList<Song> songList;

	//start has the purpose of initializing the listview with songList and selecting the first song automatically, if the song list is not empty
	public void start(){
		//populating the observable list from an array list obtained from reading the file
		songList = FXCollections.observableArrayList();
		File songData = new File("/SongList/mySongs.txt");
		//this will make the txt file if there is no such file at the specified path
		songData.createNewFile();
		BufferedReader songReader = new BufferedReader(new FileReader(songData));	
		String toAdd=songReader.readLine();
		while (toAdd!=null){
			//text is formatted "name;artist;album;year;" in our mySongs.txt
			//if no album : "name;artist;;year;"
			//if no year: "name;artist;album;;"
			//if no album or year: "name;artist;;;"
			//songs are in alphabetical order
			//removing white space at the beginning and end if any 
			toAdd=toAdd.trim();	
			Song inList = new Song();
			int lastSemicolon=0;
			int semicolonCount=0;
			for (int i=0;i<toAdd.length();i++){
				if (toAdd.charAt(i)==';'){
					if (semicolonCount==0){
						inList.name=toAdd.substring(0,i);	
						lastSemicolon=i;
						semicolonCount++;
					} else if (semicolonCount==1){
						inList.artist=toAdd.substring(lastSemicolon+1,i);
						lastSemicolon=i;
						semicolonCount++;
					} else if (semicolonCount==2){
						inList.album=toAdd.substring(lastSemicolon+1,i);
						lastSemicolon=i;
						semicolonCount++;
					} else {
						inList.year=toAdd.substring(lastSemicolon+1,i);
						lastSemicolon=i;
						semicolonCount++;
					}	
				}
			}
			songList.add(inList);
			//now our song object is populated with data in alphabetical order of the list


			
		}
		//closing the stream
		songReader.close();

		//feeding the data to our listview
		listView.setItems(songList);
		
	}

	//helper method to add songs to our observable list
	private boolean addSongHelper(Song toAdd){
		//need to iterate through the song observable list to see if the name and artist match case insensitive 		
		//NEED TO ADD IN ALPHABETICAL ORDER
		//IDEA: since our txt file is already sorted, we can just use binary search and figure out where to insert our item
		//We also need to edit the txt file after adding
				
		//BINARY SEARCH: 
		int low=0;
		int high=songList.size()-1;
		int mid;
		boolean low=false;
		while(low<=high){
			int mid=(high+low)/2;
			int comparison = toAdd.compareTo(songList.get(mid));
			if (comparison==0){
				//then we found a duplicate and we do not add
				return false;
			} else if (comparison<0){
				high=mid-1;
				low=false;
			} else {
				low=mid+1;
				low=true;
			}
		}
		//now the index high-1 is where we should add
		int addPosition = 0;
		if (low=true){
			addPosition=low-1;
		} else {
			addPosition=high+1;
		}

		if (songList.size()==0){
			addPosition=0;
		}
		//then we did not find a match and we can add the song to the observable list
		//we need to add the song in alphabetical order of names

		//this will insert the item at the right position, and shift elements in the list down one position
		songList.add(addPosition,toAdd);

		//updating the txt file 
		File songData = new File("/SongList/mySongs.txt");
		BufferedWriter songWriter = new BufferedWriter(new FileWriter(songData));
		//we know our txt file exists because we added it on our start 
		//we will overwrite all the data with our new list now
		for (int i=0;i<songList.size();i++){
			songWriter.write(songList.get(i).name+";"+songList.get(i).artist+";"+songList.get(i).album+";"+songList.get(i).year+";"+"\n"); 
		}
		songWriter.close();

		
		
		return true;
		
	}

	@FXML
	public void deleteSong(ActionEvent action){

	}

	@FXML
	public void editSong(ActionEvent action){

	}

	@FXML
	public void addSong(ActionEvent action){

	}






}
