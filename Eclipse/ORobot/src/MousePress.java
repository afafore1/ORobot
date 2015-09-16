import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.sun.glass.events.KeyEvent;

/*
 * This class is used to perform all mouse and keyboard actions
 * 
 */
public class MousePress {
	Robot bot = new Robot();
	public static void click(int x, int y) throws AWTException, InterruptedException{
		Robot bot = new Robot();
		bot.mouseMove(x, y);
		bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		bot.setAutoDelay(4000);
		bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

	}

	public void moveMouse(int x, int y){
		bot.delay(500);
		bot.mouseMove(x, y);
		bot.delay(500);
	}

	public void lClick(){
		leftClick();
		bot.delay(500);		
	}

	public void rClick(){
		rightClick();
		bot.delay(500);
		//rightClick();
	}
	public void leftClick(){
		bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		bot.delay(500);
		bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		bot.delay(500);
	}

	public void rightClick(){
		bot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
		bot.delay(500);
		bot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
		bot.delay(500);
	}

	private void type(int i){ // type int, might remove this
		bot.delay(40);
		bot.keyPress(i);
		bot.keyRelease(i);
	}

	@SuppressWarnings("restriction")
	private void type(String s){ // type words here
		bot.delay(300);
		byte [] b = s.getBytes();
		for(byte i : b){
			int c = i;
			if(c == 69){
				type(KeyEvent.VK_ENTER); continue;
			}else if(c == 66){
				type(KeyEvent.VK_BACKSPACE);
				continue;
			}else if(c == 83){
				bot.keyPress(KeyEvent.VK_SHIFT);
				continue;
			}else if(c == 85){
				bot.keyRelease(KeyEvent.VK_SHIFT);
				continue;
			}else if(c == 84){
				bot.keyPress(KeyEvent.VK_TAB);
				continue;
			}else if(c == 67){
				bot.keyPress(KeyEvent.VK_CONTROL);
				continue;
			}else if(c == 68){
				bot.keyRelease(KeyEvent.VK_CONTROL);
				continue;
			}else if(c == 79){
				bot.keyPress(KeyEvent.VK_SPACE);
				continue;
			}else if(c == 65){
				bot.keyPress(KeyEvent.VK_SEMICOLON);
				continue;
			}else if(c == 76){
				bot.keyPress(KeyEvent.VK_LESS);
				continue;
			}else if(c == 62){
				bot.keyPress(KeyEvent.VK_GREATER);
				continue;
			}
			else if(c > 96 && c < 123){
				c -=32;
			}
			bot.delay(300);
			//System.out.println("typing "+c);
			bot.keyPress(c);
			bot.keyRelease(c);

		}

	}

	private static List<String> readAction(String username) throws FileNotFoundException{ // read action that will be performed
		List<String> words = new ArrayList<String>();
		Scanner sc = new Scanner(System.in);
		String direc = "c:/users/"+username+"/Desktop/commands.txt";
		List<String> tWords = Arrays.asList("MouseClick", "Sleep", "Send");
		try {
			sc = new Scanner(new File(direc)); // Creates a scanner object that scans the entered file
			while (sc.hasNextLine()){
				String word = sc.nextLine();
				word = word.replaceAll("\\{SPACE\\}", "O");
				word = word.replaceAll("\\{BACKSPACE\\}", "B");
				word = word.replaceAll("\\{ENTER\\}", "E");
				word = word.replaceAll("\\{SHIFTUP\\}", "U");
				word = word.replaceAll("\\{SHIFTDOWN\\}", "S");
				word = word.replaceAll("\\{TAB\\}", "T");
				word = word.replaceAll("\\{CTRLDOWN\\}", "C");
				word = word.replaceAll("\\{CTRLUP\\}", "D").replaceAll(";", "A").replaceAll("<", "L");
				String k = word.substring(0, word.indexOf(','));
				if(tWords.contains(k)){ // check if the words in the array contains the next word in file
					words.add(word.replaceAll("[,]", ""));
				}
			}
			sc.close(); // Closes the users input file
		}
		catch(FileNotFoundException e) // This handles if the file does not exist.
		{
			System.out.println("File name entered does not exist, please try again!");
			System.exit(0);
		}
		return words;

	}

	public MousePress() throws AWTException{
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		//System.out.println("Enter your username: "); // using this to get user's username
		String username = "ayo";
		bot.setAutoDelay(40);
		bot.setAutoWaitForIdle(true);
		List<String> commands = new ArrayList<String>();
		try {
			commands.addAll(readAction(username));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i = 0; i < commands.size(); i++){
			//bot.delay(200); // wait for 200 ms before doing anything

			String MouseClick = commands.get(i);
			if(commands.get(i).startsWith("M")){
				String sp = " ";
				int x = 0;
				int y = 0;
				if(MouseClick.charAt(MouseClick.indexOf(sp)+1) == 'r'){ // we want to right click
					x= Integer.parseInt(MouseClick.substring(MouseClick.indexOf(sp)+8,MouseClick.lastIndexOf(sp)-1));
					y = Integer.parseInt(MouseClick.substring(MouseClick.lastIndexOf(sp)+1));
					moveMouse(x,y);
					rClick();
				}else{ // left click
					x= Integer.parseInt(MouseClick.substring(MouseClick.indexOf(sp)+7,MouseClick.lastIndexOf(sp)-1));
					y = Integer.parseInt(MouseClick.substring(MouseClick.lastIndexOf(sp)+1));
					moveMouse(x,y);
					lClick();
				}
			}else if(commands.get(i).contains("Sl")){ // sleep action
				bot.delay(300);
			}else{
				String Tpthis = MouseClick.substring(MouseClick.indexOf(" ") + 1);
				type(Tpthis);
			}
		}
	}

	// check task manager to see if the app is running
	public static boolean isRunning(String name) throws IOException{
		String line;
		String pidInfo ="";

		Process p =Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe");

		BufferedReader input =  new BufferedReader(new InputStreamReader(p.getInputStream()));

		while ((line = input.readLine()) != null) {
			pidInfo+=line; 
		}
		input.close();
		if(pidInfo.contains(name))
		{
			return true;
		}
		return false;
	}
	// try to open an application, if the app is already opened tell user it is running already 
	public static void OpenApp(String location) throws AWTException{
		//Process p = null;
		try{
			Runtime r = Runtime.getRuntime();
			String Appname = location.substring(location.lastIndexOf("\\")+1);
			if(isRunning(Appname)){
				System.out.println("Program is already running!!\nTest will start now");
				Process p = r.exec(location);
				new MousePress();
			}else{
				System.out.println("Opening "+location+" ...");				
				Process p = r.exec(location);
				new MousePress();
				try{
					Thread.sleep(5000);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}


	public static void main(String[] args) throws AWTException, IOException {

		//new MousePress();
		OpenApp("C:\\Program Files\\Sublime Text 2\\sublime_text.exe");
		
	}

}
