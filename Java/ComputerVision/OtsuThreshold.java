
public class OtsuThreshold {

	public int calculateOtsu(byte[] srcData) {
		
		int[] histData = new int[256];
			
		int ptr = 0;
		
		while(ptr < srcData.length) {
			
			int h = 0xFF & srcData[ptr];
			histData[h]++;
			ptr++;		
		}
		
		int total = srcData.length;
		
		float sum = 0;
		for(int t = 0; t < 256 ; t++) sum += t * histData[t];
		
		float sumB = 0;
		int wB = 0;
		int wF = 0;
		
		float varMax = 0;
		int threshold = 0;
		
		for(int t = 0; t < 256; t++) {
			
			wB += histData[t];
			if(wB == 0) continue;
			
			wF = total - wB;
			if(wF == 0) break;
			
			sumB += (float)(t * histData[t]);
			
			float mB = sumB / wB;
			float mF = (sum -sumB) / wF;
			
			float varBetween = (float)wB * (float)wF *(mB - mF) * (mB - mF);
			
			if(varBetween > varMax) {
				
				varMax = varBetween;
				threshold = t;
				
			}
		}
		
		return threshold;
		
	}
	
}
