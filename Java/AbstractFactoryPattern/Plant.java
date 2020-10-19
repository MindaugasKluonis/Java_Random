public class Plant {
    private String name;
    private String soil;
    
    public Plant(String pname, String psoil) {
        name = pname;     //save name
        soil = psoil;
    }
    public String getName() {
        return name + " " + soil;
    }
    
    public String getSoil(){
    	
    	return soil;
    }
    
}

