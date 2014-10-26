package Control.Audio;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import Control.MainControl;
import Debug.ErrorPopup;

public class Sound{

	private static float volume = 0.7f;
	private static int IDTrack = 0;
	private static ArrayList<Sound> sounds = new ArrayList<Sound>();
	
	public static void setup(){
		new Thread(new Runnable(){
					
					@SuppressWarnings("unchecked")
					public void run(){
						
						while(!MainControl.CloseRequest){
							for(Sound s: (List<Sound>) sounds.clone()){
								try{
									s.gainControl.setValue(getActualGain(s.gain));
									
									if(s.playing && s.gain*volume != 0f){
										boolean songFinished = s.clip.getFrameLength() - s.clip.getFramePosition() <= 0;
										
										if(s.loop && songFinished){
											s.clip.setFramePosition(0);
											
										}else if(songFinished){
											s.stop();
										}
										
										s.clip.loop(-1);
									}else if(s.destroyOnFinish){
										s.cleanup();
									}
								}catch(NullPointerException e){
									
								}catch(IllegalArgumentException e){
									ErrorPopup.createMessage(e, true);
								}
							}
						}

						for(Sound s: sounds){
							s.cleanup();
						}
						
						try{
							TimeUnit.MILLISECONDS.sleep(100);
						}catch(InterruptedException e){
							ErrorPopup.createMessage(e, true);
						}
					}
				}).start();
	}
	
	public static float getActualGain(float gain){
		//Range -80, 6
		gain+=80;
		gain/=86;
		
		return (gain*volume*86)-80;
	}
	
	public static void setVolume(float vol){
		if(vol > 1f){
			vol = 1f;
		}if(vol < 0f){
			vol = 0f;
		}
		volume = vol;
	}
	
	public static float getVolume(){
		return volume;
	}
	
	private Clip clip;
	private String name = "";
	private float gain = -5f;
	private boolean loop = false;
	private boolean destroyOnFinish = true;
	private boolean playing = false;
	private int ID;
	private FloatControl gainControl;
	
	public Sound(final String file){
		name = file;
		try{
			clip = AudioSystem.getClip();
			AudioInputStream input = AudioSystem.getAudioInputStream(new File("Res/Sounds/" + file + ".wav"));
			clip.open(input);
			gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(getActualGain(gain));
			
		}catch(Exception e){
			ErrorPopup.createMessage(e, true);
		}
		ID = IDTrack++;
	}
	
	public int getID(){
		return ID;
	}
	
	public boolean isPlaying(){
		return playing;
	}
	
	private int lastFrame = 0;
	
	public void play(){
		if(!playing){
			gain = -5.0f;
			clip.start();
			clip.setFramePosition(lastFrame);
			playing = true;
			
			if(!sounds.contains(this)){
				sounds.add(this);
			}
		}
	}
	
	public boolean hasFinished(){
		return !loop && !playing;
	}
	
	public void doNotDestroyOnFinish(){
		destroyOnFinish = false;
	}
	
	public void pause(){
		if(playing){
			lastFrame = clip.getFramePosition();
			playing = false;
			clip.stop();
		}
	}
	
	public void stop(){
		if(playing){
			lastFrame = 0;
			playing = false;
			clip.stop();
		}
	}
	
	public void setLoop(boolean loop){
		this.loop = loop;
	}
	
	public float getGain(){
		return gain;
	}
	
	public void setGain(float gain){
		this.gain = gain;
	}
	
	public void destroy(){
		destroyOnFinish = true;
		playing = false;
	}
	
	private void cleanup(){
		sounds.remove(this);
		playing = false;
		clip.stop();
		clip.flush();
		clip.drain();
		if(!destroyOnFinish){
			System.out.println("Sound thread shutting down.. (" + name + ")");
		}
	}
}

