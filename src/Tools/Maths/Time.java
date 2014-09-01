package Tools.Maths;

public class Time {
	static final long GLOBAL_LaunchTime = System.currentTimeMillis();
	

	static long StartTime;
	private int Seconds, Minutes, Hours;
	
	public Time(){
		StartTime = System.currentTimeMillis();
	}
	
	public Time(int h, int m, int s){
		setTime(h, m ,s);
		StartTime = System.currentTimeMillis();
	}
	
	private Time(long LaunchTime, int h, int m, int s){
		setTime(h, m ,s);
		StartTime = LaunchTime;
	}

	public int getSeconds(){
		updateTime();
		return Seconds;
	}

	public int getMinutes(){
		updateTime();
		return Minutes;
	}

	public int getHours(){
		updateTime();
		return Hours;
	}
	
	
	public void setTime(int h, int m, int s){
		Seconds = s;
		Minutes = m;
		Hours = h;
	}
	

	public String getTimeAsString(){
		updateTime();
		return String.format("%02d:%02d:%02d", Hours, Minutes, Seconds);
	}
	
	public void updateTime(){
		int Dif = (int) (System.currentTimeMillis()-StartTime);
		int Seconds = (int) Math.round(Dif/1000);
		int Minutes = 0;
		int Hours = 0;
		while(Seconds >= 60){
			Minutes++;
			Seconds-=60;
		}
		while(Minutes >= 60){
			Hours++;
			Minutes-=60;
		}
		this.Seconds = Seconds;
		this.Minutes = Minutes;
		this.Hours = Hours;
	}
	
	public static String getGlobalTimeAsString(){
		int Dif = (int) (System.currentTimeMillis()-GLOBAL_LaunchTime);
		int Seconds = (int) Math.round(Dif/1000);
		int Minutes = 0;
		int Hours = 0;
		while(Seconds >= 60){
			Minutes++;
			Seconds-=60;
		}
		while(Minutes >= 60){
			Hours++;
			Minutes-=60;
		}
		return String.format("%02d:%02d:%02d", Hours, Minutes, Seconds);
	}

	public static Time getGlobalTimeAsTime(){
		int Dif = (int) (System.currentTimeMillis()-GLOBAL_LaunchTime);
		int Seconds = (int) Math.round(Dif/1000);
		int Minutes = 0;
		int Hours = 0;
		while(Seconds >= 60){
			Minutes++;
			Seconds-=60;
		}
		while(Minutes >= 60){
			Hours++;
			Minutes-=60;
		}
		return new Time(GLOBAL_LaunchTime, Hours, Minutes, Seconds);
	}
	
}
