package org.example.trainear.backEnd;

import org.apache.commons.io.IOUtils;
import org.example.trainear.utilities.AudioUtilities;

import backEnd.Histogram;
import backEnd.PitchDetection;

public class AudioRead {
	public AudioRead(){
		
	}
	public static float[] read(String path) throws FileNotFoundException{
		float[] rawFloat = null;
		if(path.endsWith(".wav") || path.endsWith(".mp3") ){
			try {
				File file = null;
				if(path.endsWith(".wav")){
					file = new File(path);
				} else {
					file = AudioUtilities.convertMP3toWAV(path);
				}
				byte[] rawByte = IOUtils.toByteArray(new FileInputStream(file));
				rawFloat = AudioUtilities.byteToFloatArray(rawByte);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return rawFloat;
		} else {
			throw new FileNotFoundException();
		}
	}
	public static float[] analyze(String path) throws FileNotFoundException, IOException{
		return analyze(read(path));
	}
	public static float[] analyze(float[] rawFloat) throws IOException { 
		PitchDetection pd = new PitchDetection("file",rawFloat, 44100f);
		Histogram h = new Histogram(pd);
		return h.getPeaksCent();
//		return new float[]{4,2};
	}
}
