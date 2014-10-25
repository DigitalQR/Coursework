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

	private static float volume = 1f;
	private static ArrayList<Sound> sounds = new ArrayList<Sound>();
	
	public static void setup(){
		new Thread(new Runnable(){
					
					@SuppressWarnings("unchecked")
					public void run(){
						
						while(!MainControl.CloseRequest){
							for(Sound s: (List<Sound>) sounds.clone()){
								try{
									s.gainControl.setValue((s.gain+80)*volume-80);
									
									
									if(s.playing && s.gain*volume != 0f){
										boolean songFinished = s.clip.getFrameLength() - s.clip.getFramePosition() <= 0;
										
										if(s.loop && songFinished){
											s.clip.setFramePosition(0);
											
										}else if(songFinished){
											s.stop();
										}
										
										s.clip.loop(-1);
									}else if(s.destroyOnFinish){
										sounds.remove(s);
										s.cleanup();
									}
								}catch(NullPointerException e){}
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
	private float gain;
	private boolean loop = false;
	private boolean destroyOnFinish = true;
	private boolean playing = false;
	private FloatControl gainControl;
	
	public Sound(final String file){
		name = file;
		try{
			clip = AudioSystem.getClip();
			AudioInputStream input = AudioSystem.getAudioInputStream(new File("Res/Sounds/" + file + ".wav"));
			clip.open(input);
			gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			
		}catch(Exception e){
			ErrorPopup.createMessage(e, true);
		}
		sounds.add(this);
	}
	
	public boolean isPlaying(){
		return playing;
	}
	
	private int lastFrame;
	
	public void play(){
		gain = -5.0f;
		clip.start();
		clip.setFramePosition(lastFrame);
		playing = true;
	}
	
	public void doNotDestroyOnFinish(){
		destroyOnFinish = false;
	}
	
	public void pause(){
		lastFrame = clip.getFramePosition();
		playing = false;
		clip.stop();
	}
	
	public void stop(){
		lastFrame = 0;
		playing = false;
		clip.stop();
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
	
	public void cleanup(){
		clip.stop();
		clip.flush();
		clip.drain();
		if(!destroyOnFinish){
			System.out.println("Sound thread shutting down.. (" + name + ")");
		}
	}
	
}

