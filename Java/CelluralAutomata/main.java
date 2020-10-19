
public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		int map[][] = new int[10000][10000];// 10 by 10 grid = 100
		int neighbors[];
		int total;
		
		
		for(int i = 0; i< 10;i++){
			for(int j = 0; j< 10;j++){
				
				map[i][j] = 0; // initialize to 0 = not infected

			}
			
			
		}
		
		map[4][5] = 1;
		map[6][5] = 1;
		map[5][6] = 1;
		map[5][4] = 1;
		map[5][5] = 1;
		total = 5;
		
		
		for(int g = 0;g < 400;g++){
			for(int i = 0; i<10000;i++){
				
				for(int j = 0; j<10000;j++){
					
					neighbors = new int[4];
					
					neighbors[0] = map[(i+10000-1) % 10000][j];
					neighbors[1] = map[(i+10000+1) % 10000][j];
					neighbors[2] = map[i][(j+10000-1) % 10000];
					neighbors[3] = map[i][(j+10000+1) % 10000];
					
					neighbors[4] = map[(i+10000-1) % 10000][(j+10000+1) % 10000];
					neighbors[5] = map[(i+10000+1) % 10000][(j+10000+1) % 10000];
					neighbors[6] = map[(i+10000-1) % 10000][(j+10000-1) % 10000];
					neighbors[7] = map[(i+10000+1) % 10000][(j+10000-1) % 10000];
					
					
					int infections = neighbors[0] + neighbors[1] + neighbors[2] + neighbors[3]
							+ neighbors[4] + neighbors[5] + neighbors[6] + neighbors[7];
				
					
					if(infections > 1 && map[i][j] == 0){
						
						total++;
						System.out.println("INFECTION" + total);
						map[i][j]=1;
						
					}
					
					
					
					
				}
			
			
			}
			
		}
	

	}

}
