package com.example.locationtrackscan;

import java.util.Calendar;

public class ConnInfo {
	private Calendar time;
	private String ip;
	private String MAC;
	private Quality quality;
	
	public ConnInfo(String ip, String MAC, int qLink, int qLevel, int qNoise) {
		this.time = Calendar.getInstance();
		this.ip = ip;
		this.MAC = MAC;
		this.quality = new Quality(qLink, qLevel, qNoise);
	}
	
	public Calendar getTime() {
		return time;
	}
	
    public String getIp() {
		return ip;
	}

	public String getMAC() {
		return MAC;
	}

	public Quality getQuality() {
		return quality;
	}

	public class Quality {
    	private int link;
    	private int level;
    	private int noise;
    	
    	public Quality(int link, int level, int noise) {
			this.link = link;
			this.level = level;
			this.noise = noise;
    	}

		public int getLink() {
			return link;
		}

		public int getLevel() {
			return level;
		}

		public int getNoise() {
			return noise;
		}
    }
}
