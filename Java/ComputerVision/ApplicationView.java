import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ApplicationView implements ActionListener, ListSelectionListener{
	
	private ApplicationController controller;
	
	private JFrame frame;
	private JScrollPane leftPanel;
	private JPanel rightPanel;
	private JScrollPane bottomPanel;
	private JPanel imagesPanel;
	private JSplitPane horizontalSplitPane;
	private JSplitPane verticalSplitPane;
	private JPanel imagePanel;
	
	private JLabel currentImage;
	private JLabel histogram;
	private ImageButton[] allImages;
	private ImageButton dilateButton;
	private ImageButton histogramButton;
	private ImageButton thresholdButton;
	private ImageButton erodeButton;
	private ImageButton componentButton;
	private ImageButton damageButton;
	private ImageButton startButton;
	private ImageButton stopButton;
	private JLabel conditionLabel;
	private JLabel numComponents;
	private JComboBox<String> comboBox;
	private JList<String> commandList;
	private DefaultListModel<String> listModel;
	private JLabel processingLabel;
	
	private String[] commandStrings = { "Threshold", "Dilation", "Erosion", "Show Histogram" ,"Component Counting", "Damage check", "Wait(Short)", "Wait(Long)" };
	
	public ApplicationView(ApplicationController controller) {
		
		this.controller = controller;
		
		InitializeView();
		
	}


	private void InitializeView() {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
				UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		frame = new JFrame();
		leftPanel = new JScrollPane();
		rightPanel = new JPanel();
		rightPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		bottomPanel = new JScrollPane();
		imagePanel = new JPanel(new GridBagLayout());
		imagesPanel = new JPanel();
		
		currentImage = new JLabel();
		histogram = new JLabel();
		processingLabel = new JLabel("Processing time:");
		
		comboBox = new JComboBox<String>(commandStrings);
		comboBox.setAlignmentX(Component.CENTER_ALIGNMENT);
		comboBox.addActionListener(this);
		
		commandList = new JList<String>();
		commandList.setAlignmentX(Component.CENTER_ALIGNMENT);
		commandList.addListSelectionListener(this);
		commandList.setPreferredSize(new Dimension(100,200));
		listModel = new DefaultListModel<String>();
		commandList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		commandList.setModel(listModel);
		
		startButton = new ImageButton(true,true,true,true);
		startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		startButton.setText("Start");
		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				controller.ExecuteCommandLoop();
				
			}
			
		});
		
		stopButton = new ImageButton(true,true,true,true);
		stopButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		stopButton.setText("Stop");
		stopButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				controller.StopCommandLoop();
				
			}
			
		});
		
		dilateButton = new ImageButton(true,true,true,true);
		dilateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		dilateButton.setText("Dilate");
		dilateButton.setCommand(new DilateCommand(controller, 0));
		dilateButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				dilateButton.getCommand().Execute();
				
			}
			
		});
		
		Font labelFont = new Font(Font.SANS_SERIF, Font.PLAIN, 20);
		
		numComponents = new JLabel();
		numComponents.setFont(labelFont);
		numComponents.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		conditionLabel = new JLabel();
		conditionLabel.setFont(labelFont);
		conditionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		componentButton = new ImageButton(true,true,true,true);
		componentButton.setText("Show image components");
		componentButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		componentButton.setCommand(new ImageComponentsCommand(controller, 0));
		componentButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				componentButton.getCommand().Execute();
				
			}
			
		});
		

		erodeButton = new ImageButton(true,true,true,true);
		erodeButton.setText("Erode");
		erodeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		erodeButton.setCommand(new ErodeCommand(controller, 0));
		erodeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				erodeButton.getCommand().Execute();
				
			}
			
		});
		
		histogramButton = new ImageButton(true,true,true,true);
		histogramButton.setText("Show histogram");
		histogramButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		histogramButton.setCommand(new HistogramCommand(controller, 0));
		histogramButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				histogramButton.getCommand().Execute();
				
			}
			
		});
		
		thresholdButton = new ImageButton(true,true,true,true);
		thresholdButton.setText("Threshold");
		thresholdButton.setCommand(new ThresholdCommand(controller, 0));
		thresholdButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		thresholdButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				thresholdButton.getCommand().Execute();
				
			}
			
		});
		

		damageButton = new ImageButton(true,true,true,true);
		damageButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		damageButton.setText("Check for damage");
		damageButton.setCommand(new DamageCommand(controller, 0));
		damageButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				damageButton.getCommand().Execute();
				
			}
			
		});
		
		
		horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                leftPanel, rightPanel);
		horizontalSplitPane.setOneTouchExpandable(true);
		
		verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				horizontalSplitPane,bottomPanel);
		verticalSplitPane.setOneTouchExpandable(true);
		
		Dimension minimumSize = new Dimension(600, 400);
		horizontalSplitPane.setDividerLocation((int)minimumSize.getWidth());
		
		leftPanel.setMinimumSize(minimumSize);
		rightPanel.setMinimumSize(minimumSize);
		
		imagePanel.add(currentImage);
		imagePanel.add(histogram);
		imagePanel.add(processingLabel);
		leftPanel.getViewport().add(imagePanel);
		bottomPanel.getViewport().add(imagesPanel);
		
		rightPanel.add(dilateButton);
		rightPanel.add(histogramButton);
		rightPanel.add(thresholdButton);
		rightPanel.add(erodeButton);
		rightPanel.add(componentButton);
		rightPanel.add(damageButton);
		rightPanel.add(comboBox);
		rightPanel.add(commandList);
		rightPanel.add(numComponents);
		rightPanel.add(conditionLabel);
		rightPanel.add(startButton);
		rightPanel.add(stopButton);
		
		frame.add(verticalSplitPane);
		frame.setMinimumSize(new Dimension(1300,800));
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}
	
	public void AddCurrentImageToView(BufferedImage image, BufferedImage histogram) {
		
		currentImage.setIcon(new ImageIcon(image));
		
	}
	
	public void AddCurrentImageToView(BufferedImage image) {
		
		currentImage.setIcon(new ImageIcon(image));
		
	}
	
	public void addHistogram(BufferedImage histogram) {
		
		this.histogram.setIcon(new ImageIcon(histogram));
		
	}
	
	public void CreateImageGallery(BufferedImage[] images) {
		
		int size = images.length;
		allImages = new ImageButton[size];
		
		for(int i = 0; i < size; i++) {
			
			ImageButton image = new ImageButton();
			image.setCommand(new EventCommand(controller, i));
			image.setIcon(new ImageIcon(images[i]));
			image.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					
					image.getCommand().Execute();
					
				}
				
			});
			imagesPanel.add(image);
			
		}
		
		frame.validate();
		
	}
	
	public void UpdateComponentLabel(int value) {
		
		numComponents.setText("Number of Components: " + value);
		
	}
	
	public void HideComponentLabel() {
		
		numComponents.setText("");
		
	}
	
public void UpdateConditionLabel(String value) {
		
		conditionLabel.setText("Ring condition: " + value);
		
	}
	
	public void HideConditionLabel() {
		
		conditionLabel.setText("");
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == comboBox) {
			
			listModel.addElement(comboBox.getSelectedItem().toString());
			
			if(comboBox.getSelectedIndex() == 0) {
				
				controller.addCommandToTheList(new ThresholdCommand(controller, comboBox.getSelectedIndex()));
				
			}
			
			else if(comboBox.getSelectedIndex() == 1) {
				
				controller.addCommandToTheList(new DilateCommand(controller, comboBox.getSelectedIndex()));
				
			}
			
			else if(comboBox.getSelectedIndex() == 2) {
				
				controller.addCommandToTheList(new ErodeCommand(controller, comboBox.getSelectedIndex()));
				
			}
			
			else if(comboBox.getSelectedIndex() == 3) {
				
				controller.addCommandToTheList(new HistogramCommand(controller, comboBox.getSelectedIndex()));
				
			}
			
			else if(comboBox.getSelectedIndex() == 4) {
				
				controller.addCommandToTheList(new ImageComponentsCommand(controller, comboBox.getSelectedIndex()));
				
			}
			
			else if(comboBox.getSelectedIndex() == 5) {
				
				controller.addCommandToTheList(new DamageCommand(controller, comboBox.getSelectedIndex()));
				
			}
			
			else if(comboBox.getSelectedIndex() == 6) {
				
				controller.addCommandToTheList(new WaitCommand(1000));
				
			}
			
			else if(comboBox.getSelectedIndex() == 7) {
				
				controller.addCommandToTheList(new WaitCommand(3000));
				
			}
			
		}
		
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		
		if (e.getValueIsAdjusting() == false) {
			int index = commandList.getSelectedIndex();
		    if(index >= 0){ //Remove only if a particular item is selected
		        listModel.removeElementAt(index);
		        controller.removeCommandFromTheList(index);
		    }
		}
		
	}
	
	public void setEnabledButtons(boolean value) {
		
		dilateButton.setEnabled(value);
		histogramButton.setEnabled(value);
		thresholdButton.setEnabled(value);
		erodeButton.setEnabled(value);
		componentButton.setEnabled(value);
		damageButton.setEnabled(value);
		startButton.setEnabled(value);
		
		comboBox.setEnabled(value);
		commandList.setEnabled(value);
		
	}
	
	public void updateTime(String time) {
		
		processingLabel.setText("Processing time: " + time);
		
	}
	
}
