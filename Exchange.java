//java how to handle exceptions

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

import javax.swing.JOptionPane;

public class Exchange {

	public static void main(String[] args) {
		
		// Give a path to the file of the rates
		HashMap<String, BigDecimal> rates = null;
		while(rates == null) {
			String pathToFile = JOptionPane.showInputDialog("Enter the path to the CSV file of the exchange rates \n"
					+ "For example C:\\Users\\Mantas\\eclipse-workspace\\Exchange\\data.csv");		
			rates = readCSV(pathToFile);
		}

		BigDecimal amount = null;	
		boolean number = false;
		while(!number) {
			try {
				amount = new BigDecimal(JOptionPane.showInputDialog("Amount of the currency you have: "));
				number = true;
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Enter the amount in a number format, please!");
			}
		}	
		
		String initialTick = "";
		boolean exists = false;
		while(exists != true) {
			initialTick = JOptionPane.showInputDialog("What is the currency you have? Provide 3 letters of the currecy: ");
			initialTick = initialTick.toUpperCase();
			if(!rates.containsKey(initialTick)) {
				JOptionPane.showMessageDialog(null, "The provided tick is not supported. The list of the supported currencies will be displayed in the terminal.");
				System.out.println("Supported ticks:");
				for(String i: rates.keySet()) {
					System.out.println(i);
				}
				JOptionPane.showMessageDialog(null, "Try to enter the tick of the currency again. ");
			}else {
				exists = true;
			}
		}
		
		String finalTick = "";
		exists = false;
		while(exists != true) {
			finalTick = JOptionPane.showInputDialog("What currency do you want to convert it into? Provide 3 letters of the currency: ");
			finalTick = finalTick.toUpperCase();
			if(!rates.containsKey(finalTick)) {
				JOptionPane.showMessageDialog(null, "The provided tick is not supported. The list of the supported currencies will be displayed in the terminal.");
				System.out.println("Supported ticks:");
				for(String i: rates.keySet()) {
					System.out.println(i);
				}
				JOptionPane.showMessageDialog(null, "Try to enter the tick of the currency again. ");
			}else {
				exists = true;
			}
		}
		
		BigDecimal finalAmount = amount.divide(rates.get(initialTick), 30, RoundingMode.HALF_UP);
		finalAmount = finalAmount.multiply(rates.get(finalTick));
		finalAmount = finalAmount.setScale(18, RoundingMode.HALF_UP).stripTrailingZeros();
		JOptionPane.showMessageDialog(null, amount + " " + initialTick + " is equivelent to " + finalAmount.toPlainString() + " " + finalTick);


	}
	
	public static HashMap<String, BigDecimal> readCSV(String pathToFile){
		/* Takes the CSV file with the currency tick in the first column and the actual rate in the second column.
		 * The actual rates start from the second row.
		 * The tick must be given in the upper-case format
		 * The rates must be provided in the correct format as agreed upon with Neringa:
		 * 		the symbol for the decimal separator must be "," with no more commas in the number formatting.
		 * 		Example: 0,809552722  
		 * 				 69770896569209
		 * 		Example of NOT allowed formating:
		 * 				 0.809552722
		 * 				 69,770,896,569,209
		 * 
		 * Argument: path to the CSV file
		 * Returns: HashMap with the tick-rate values
		 */
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(pathToFile));
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Either CSV file with rates is not existent or the given path is wrong");
			return null;
		}	
		
		
		String row;	
		HashMap<String, BigDecimal> rates = new HashMap<String, BigDecimal>();

		try {
			while( (row = reader.readLine()) != null ) {
				
				String[] data = row.split(",", 2);			
				data[1] = data[1].replace("\"", "");
				data[1] = data[1].replace(",", ".");
				BigDecimal rate = new BigDecimal(data[1]);	
				rates.put(data[0], rate);
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return rates;
		
	}


}
