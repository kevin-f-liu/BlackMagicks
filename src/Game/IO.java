package Game;

import java.io.*;
import java.net.URISyntaxException;

public class IO {

		private PrintWriter fileOut;
		
		public  void createOutputFile(String fileName)
		{
			createOutputFile(fileName, false);
			try{
				fileOut = new PrintWriter(new BufferedWriter(new FileWriter(fileName)));
			}
			catch(IOException e){
				System.out.println("***Cannot create file: "+ fileName + " ***");
			}
		}
		
		public  void createOutputFile(String fileName, boolean append)
		{
			try{
				fileOut = new PrintWriter(new BufferedWriter(new FileWriter(fileName, append)));
			}
			catch(IOException e){
				System.out.println("***Cannot create file: "+ fileName + " ***");
			}
		}
		
		public  void print(String text)
		{
			fileOut.print(text);
		}
		
		public  void println(String text)
		{
			fileOut.println(text);
		}
		
		public  void closeOutputFile()
		{
			fileOut.close();
		}
		
		private static BufferedReader fileIn;
		
		public void openInputFile(String fileName)
		{
			
			try{
				fileIn = new BufferedReader(new FileReader(fileName));
			}
			catch(FileNotFoundException e)
			{
				System.out.println("*** Cannot open: " + fileName + " ***");
			}
		}
		
		public String readLine()
		{
			try{
				return fileIn.readLine();
			}
			catch(IOException e){}
			return null;
		}
		
		public void closeInputFile()
		{
			try{
				fileIn.close();
			}
			catch(IOException e){}
		}
		
}







