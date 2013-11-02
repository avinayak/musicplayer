/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package musicplayer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Iterator;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.ID3v1Tag;

/**
 *
 * @author gene
 */
public class Mp3TagReader {

    /**
     * @param args the command line arguments
     */
    static private MP3File f;

    public static void main(String[] args) throws IOException, CannotReadException, TagException, ReadOnlyFileException, InvalidAudioFrameException, SQLException {
        // TODO code application logic here
        Path startingDir = Paths.get("/media/gene/B836564536560532/music");
        DBEngine dbengine = new DBEngine();
        ID3v1Tag tag;
        MP3AudioHeader ah;
        int i = 0;
        String[] splt;
        String basename, title, album;
        Mp3Finder.Finder finder = new Mp3Finder.Finder();
        Files.walkFileTree(startingDir, finder);
        for (Iterator<String> it = finder.getMp3().iterator(); it.hasNext();) {
            String a = it.next();
            try {
                f = (MP3File) AudioFileIO.read(new File(a));
                tag = f.getID3v1Tag();

                ah = f.getMP3AudioHeader();
                 splt = a.split("/");
                 System.out.println("-->>>>>"+tag.getFirstArtist().split(" ")[0].toLowerCase());
                if (tag.getFirstTitle().trim().equals("") || tag.getFirstTitle().split(" ")[0].toLowerCase().equals("track")) {
                   
                    basename = splt[splt.length - 1];
                    title = basename;
                } else {
                    title = tag.getFirstTitle();
                }
                if(tag.getFirstAlbum().trim().equals("") || tag.getFirstAlbum().trim().equals("title"))
                {
                    album=splt[splt.length-2];
                }
                else
                {
                    album=tag.getFirstAlbum();
                }
                System.out.println(i++ + title + album + tag.getFirstArtist() + tag.getFirstGenre()+ ah.getTrackLengthAsString());
                dbengine.writeLibrary(i, title, album, tag.getFirstArtist(),tag.getFirstGenre() ,ah.getTrackLengthAsString(), a);
            } catch (NullPointerException | InvalidAudioFrameException e) {
            }


        }
    }
}
