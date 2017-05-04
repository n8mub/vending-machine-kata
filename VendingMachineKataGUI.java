/**
 * 
 */
package main.java.com.audition;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

/**
 * @author casey.richardson
 *
 */
public class VendingMachineKataGUI extends JFrame {

	private static final long serialVersionUID = 6981052387635999753L;
	
	private HashMap<String, Product> products = new HashMap<String, Product>();
	private JTable inventory = new JTable();
	private JFrame frame = this;
	private double balance = 0;
	
	public VendingMachineKataGUI() {
		super("Vending Machine");
		initGUI();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(300, 280);
        this.setLocation(100, 100);
        this.setResizable(false);
        this.setVisible(true);
	}
	
	/**
     * initialize GUI
     */
    private void initGUI() {
    	loadProducts();
    	this.setLayout(new BorderLayout());
    	JPanel panel = new JPanel();
    	panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
    	panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    	panel.add(new JScrollPane(inventory));
    	inventory.setColumnSelectionAllowed(false);
    	inventory.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	JPanel panel2 = new JPanel();
    	JButton select = new JButton("Select Products");
    	panel2.add(select);
    	panel.add(panel2);
    	this.add(panel,BorderLayout.CENTER);
    	select.setToolTipText("Select item from table");
    	JPanel panel3 = new JPanel();
    	JButton acceptCoins = new JButton("Accept Coins");
    	panel3.add(acceptCoins);
    	JTextField inputfield2 = new JTextField();
    	inputfield2.setColumns(10);
    	inputfield2.setToolTipText("Enter payment amount");
    	panel3.add(inputfield2);
    	panel.add(panel3);
    	JTextArea outputField = new JTextArea();
    	outputField.setEditable(false);
    	panel.add(outputField);
    	select.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				StringBuilder output = new StringBuilder();
				DecimalFormat formatter = new DecimalFormat("$###,###.##");
				try{
					inventory.getSelectedRow();
					if(inventory.getSelectedRow()<0){
						output.append("Please from Table");
					}else{
						output.append(products.get(inventory.getValueAt(inventory.getSelectedRow(), 0)).getName());
						output.append(System.lineSeparator());
						if(products.get(inventory.getValueAt(inventory.getSelectedRow(), 0)).getCount()<=0){
							output.append("Sold out");
						}else if(balance<=0){
							output.append("Price: ");
							output.append(formatter.format(products.get(inventory.getValueAt(inventory.getSelectedRow(), 0)).getPrice()));
						}else if(balance>=products.get(inventory.getValueAt(inventory.getSelectedRow(), 0)).getPrice()){
							//make change & return coins
							balance -= products.get(inventory.getValueAt(inventory.getSelectedRow(), 0)).getPrice();
							output.append("Balance: ").append(formatter.format(balance));
							int count = products.get(inventory.getValueAt(inventory.getSelectedRow(), 0)).getCount();
							products.get(inventory.getValueAt(inventory.getSelectedRow(), 0)).setCount(count-1);
							inventory.setValueAt(count-1, inventory.getSelectedRow(), 1);
						}else if(balance<products.get(inventory.getValueAt(inventory.getSelectedRow(), 0)).getPrice()){
							output.append("Due: ");
							output.append(formatter.format(-(balance - products.get(inventory.getValueAt(inventory.getSelectedRow(), 0)).getPrice())));
						}
					}
				}catch(Exception ex) {
					output.append(ex.getMessage());
					ex.printStackTrace(System.err);
				}
				outputField.setText(output.toString());
				frame.repaint();
			}
		});
    	acceptCoins.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String output = "";
				DecimalFormat formatter = new DecimalFormat("$###,###.##");
				DecimalFormat decimalFormat = new DecimalFormat("###.##");
				try {
					balance += Double.parseDouble(inputfield2.getText());
					balance = Double.parseDouble(decimalFormat.format(balance));
					output ="Balance: "+ formatter.format(balance);
				} catch (Exception e2) {
					output = e2.getMessage();
					e2.printStackTrace(System.err);
				}
				outputField.setText(output);
				frame.repaint();
			}
		});
    }
    
    /**
     * load initial values for products
     * (edit to change inventory in vending machine)
     */
	private void loadProducts(){
    	if(products.isEmpty()){
    		products.put("Pepsi",new Product("Pepsi", 0.65, 10));
        	products.put("Mountain Dew", new Product("Mountain Dew", 0.65, 10));
        	products.put("Dr Pepper", new Product("Dr Pepper", 0.65, 10));
        	products.put("Root Beer", new Product("Root Beer", 0.65, 10));
        	products.put("7up", new Product("7up", 0.65, 10));
    	}
    	String[] header = {"Name","Count","Price"};
    	Object[][] data = new Object[products.size()][3];
    	int i = 0;
    	for(String name : products.keySet()){
    		data[i][0] = products.get(name).getName();
    		data[i][1] = products.get(name).getCount();
    		data[i][2] = products.get(name).getPrice();
    		i++;
    	}
    	inventory = new JTable(data, header);
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		VendingMachineKataGUI vendingMachineKataGUI;
		vendingMachineKataGUI = new VendingMachineKataGUI();
	}
	
	private class Product{
		private String name;
		private double price;
		private int count;
		public Product(String name, double price, int count) {
			this.name = name;
			this.price = price;
			this.count = count;
		}
		public String getName() {
			return name;
		}
		public double getPrice() {
			return price;
		}
		public int getCount() {
			return count;
		}
		public void setCount(int count) {
			this.count = count;
		}
		@Override
		public String toString() {
			return "Product [name=" + name + ", price=" + price + ", count=" + count + "]";
		}
	}
}
