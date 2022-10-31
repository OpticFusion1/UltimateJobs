package de.warsteiner.jobs.api.plugins;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.xxmicloxx.NoteBlockAPI.NBSDecoder;
import com.xxmicloxx.NoteBlockAPI.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.Song;

import de.warsteiner.jobs.utils.objects.PluginColor;
import de.warsteiner.jobs.utils.objects.jobs.Job;

public class NoteBlockManager {
	
	public void playSong(Player player, Job job, String path) {
	
		File file = new File(path);
		
		if(!file.exists()) {
			Bukkit.getConsoleSender().sendMessage(PluginColor.WARNING.getPrefix() + "Cannot find NBS File for "+job.getConfigID()+" file is called; "+path);
			return;
		}
		
		Song song = NBSDecoder.parse(file);
		
		RadioSongPlayer rsp = new RadioSongPlayer(song);
		
		rsp.addPlayer(player);

		rsp.setPlaying(true);
		rsp.setAutoDestroy(true);
	}

}
