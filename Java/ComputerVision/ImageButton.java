import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ImageButton extends JButton{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Command command;

	public ImageButton() {
		
		setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(true);
		
	}
	
	public ImageButton(boolean areaFilled, boolean borderPainted, boolean focusPainted, boolean opaque) {
		
		setContentAreaFilled(areaFilled);
        setBorderPainted(borderPainted);
        setFocusPainted(focusPainted);
        setOpaque(opaque);
        
        setPreferredSize(new Dimension(200,50));
		
	}
	
	public void setIcon(ImageIcon icon) {
		
		super.setIcon(icon);
		
	}
	
	public void setCommand(Command command) {
		
		this.command = command;
		
	}
	
	public Command getCommand() {
		
		return command;
	}
}
