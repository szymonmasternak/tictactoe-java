// Copyright 2020 Szymon Masternak

// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at

//     http://www.apache.org/licenses/LICENSE-2.0

// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class tictactoe {
	
	private JFrame frame;
	private JPanel panelbuttons;
	private JPanel panelmain;
	private JPanel panelscoreboard;
	private JButton button[][];
	private JLabel scoreboard;
	private JLabel texthuman;
	private JLabel textcomputer;
	private JLabel score_computer;
	private JLabel score_human;
	private JLabel message;
	private JButton playAgain;
	
	private int varscore_comp=0;
	private int varscore_human=0;
	private int gamenumber=0;
	
	
	//this function checks if a certain diagonal has 2 cells in any position in that diagonal that contain a certain string and one other string in any combination
	//this function is used to determine the optimum position that the computer is supposed to place its O
	private String checkoptdiag(String s, String dontcare) {
		if(s.equals(button[0][0].getText()) && s.equals(button[1][1].getText()) && dontcare.equals(button[2][2].getText())) {
			return "2,2";
		}	
		if(dontcare.equals(button[0][0].getText()) && s.equals(button[1][1].getText()) && s.equals(button[2][2].getText())) {
			return "0,0";
		}
		if(s.equals(button[0][0].getText()) && dontcare.equals(button[1][1].getText()) && s.equals(button[2][2].getText())) {
			return "1,1";
		}
		


		if(s.equals(button[0][2].getText()) && s.equals(button[1][1].getText()) && dontcare.equals(button[2][0].getText())) {
			return "2,0";
		}	
		if(dontcare.equals(button[0][2].getText()) && s.equals(button[1][1].getText()) && s.equals(button[2][0].getText())) {
			return "0,2";
		}
		if(s.equals(button[0][2].getText()) && dontcare.equals(button[1][1].getText()) && s.equals(button[2][0].getText())) {
			return "1,1";
		}
		return "";
	}
	
	//this function checks if a certain column has 2 cells in any position that contain a certain string and one other string on the third one in any order
	//this function is used to determine the optimum position that the computer is supposed to place its X
	private String checkoptrow(int number, String s, String dontcare) {
		int counter_match_empty = 0;
		int counter_match_o = 0;
		String position = "";
		
		for(int i=0; i<3;i++) {
				if(dontcare.equals(button[number][i].getText())) {
					counter_match_empty++;
					position = number+","+i;
				}
				if(s.equals(button[number][i].getText())) {
					counter_match_o++;
				}
		}
		
		if(counter_match_empty == 1 && counter_match_o == 2) {
			return position;
		}
		return "";
	}
	
	//this function checks if a certain column has 2 cells in any position that contain a certain string and one other string on the third one
	//this function is used to determine the optimum position that the computer is supposed to place its X
	private String checkoptcol(int number, String s, String dontcare) {
		int counter_match_empty = 0;
		int counter_match_o = 0;
		String position = "";
		
		for(int i=0; i<3;i++) {
				if(dontcare.equals(button[i][number].getText())) {
					counter_match_empty++;
					position = i+","+number;
				}
				if(s.equals(button[i][number].getText())) {
					counter_match_o++;
				}
		}
		
		if(counter_match_empty == 1 && counter_match_o == 2) {
			return position;
		}
		return "";
	}
	
	
	//this function determines the next optimum position that the computer plays in.
	//it prioritises getting a win over blocking the opponent.
	//it checks each row,column and diagonal if the computer has 2 o's and the 3rd is empty, if it does it returns the position of that empty cell
	//if it doesnt find 2 o's with an empty space it tries to look to block the opponent that has the same 2 x's and 1 empty cell
	private String checkoptpos() {		
		for(int i=0;i<3;i++) {
			if(!checkoptrow(i,"O","").equals("")) {
				return checkoptrow(i,"O","");
			}
			if(!checkoptcol(i,"O","").equals("")) {
				return checkoptcol(i,"O","");
			}	
		}
		
		if(!checkoptdiag("O","").equals("")) {
			return checkoptdiag("O","");
		}
		
		for(int i=0;i<3;i++) {
			if(!checkoptrow(i,"X","").equals("")) {
				return checkoptrow(i,"X","");
			}
			if(!checkoptcol(i,"X","").equals("")) {
				return checkoptcol(i,"X","");
			}	
		}
		
		if(!checkoptdiag("X","").equals("")) {
			return checkoptdiag("X","");
		}
		return "";
	}
	
	
	//this function turns all buttons clickable or not clickable
	private void setClickable(boolean bool) {
		for(int i=0; i<3; i++) {
			for(int j=0; j<3; j++) {
				button[i][j].setEnabled(bool);
			}
		}
	}
	
	//this function sets the score for the human and the computer
	private void setScore(int scorehuman, int scorecomp) {
		score_computer.setText(Integer.toString(scorecomp));
		score_human.setText(Integer.toString(scorehuman));
		System.out.println("The Score is Human: " + score_human.getText() + " Comp: " + score_computer.getText());
	}
	
	//this function checks the nth row for a certain string. returns true if three the same strings are present in a row. This is used to verify if 3 X's or 3 O's occurred in a row.
	private boolean checkRow(int number, String s){
		int counter_match=0;
		
		for(int i=0; i<3; i++) {
			if(s.equals(button[number][i].getText())){
				counter_match++;
			}
		}
		
		System.out.println("Counter Match for check row "+ number + " of " + s + " is " + counter_match);
		
		if(counter_match == 3) {
			return true;
		}
		else {
			return false;
		}
		
	}
	
	//this function checks a nth column for a certain string. returns true if 3 the same strings are present in a column. This is used to verify if 3 X's or 3 O's occured in a column
	private boolean checkCol(int number, String s){
		int counter_match=0;
		
		for(int i=0; i<3; i++) {
			if(s.equals(button[i][number].getText())){
				counter_match++;
			}
		}
		
		System.out.println("Counter Match for check Column "+ number +" of " + s + " is " + counter_match);

		if(counter_match == 3) {
			return true;
		}
		else {
			return false;
		}
	}
	
	//this function checks if a certain string occurs in a certain diagnal, this is used to verify if 3 X's or 3 O's occured in a diagnol to determine the winner
	private boolean checkDiag(String s){
		if(s.equals(button[0][0].getText()) && s.equals(button[1][1].getText()) && s.equals(button[2][2].getText())) {
			return true;
		}
		if(s.equals(button[0][2].getText()) && s.equals(button[1][1].getText()) && s.equals(button[2][0].getText())) {
			return true;
		}
		return false;
	}
	
	
	//this function determines if a winner exists in the current game
	private boolean detWinner() {
		boolean human_winner=false;
		boolean computer_winner=false;
		
		whileloop: while(computer_winner == false && human_winner == false) {
			for(int i=0; i<3; i++) {
				if(checkRow(i,"X") == true) {
					human_winner = true;
					break whileloop;
				}
				if(checkCol(i,"X") == true) {
					human_winner = true;
					break whileloop;
				}
			}
			human_winner = checkDiag("X");

			for(int i=0; i<3; i++) {
				if (checkRow(i, "O") == true) {
					computer_winner = true;
					break whileloop;
				}
				if (checkCol(i, "O") == true) {
					computer_winner = true;
					break whileloop;
				}
			}		
			computer_winner = checkDiag("O");
			break;
		}
		
		System.out.println("Computer_winner is " + computer_winner);
		System.out.println("Human_winner is " + human_winner);
		
		if(human_winner == false && computer_winner == false && emptyCellAvail() == true) {
			System.out.println("Draw but not over");
			return false;
		}
		
		if(human_winner == false && computer_winner == false && emptyCellAvail() == false) {
			System.out.println("Draw");
			setScore((varscore_human = varscore_human+1), (varscore_comp = varscore_comp+1));
			playAgain.setVisible(true);
			message.setVisible(true);
			message.setText("Draw");
			setClickable(false);
			return true;
		}
		
		if(human_winner == true && computer_winner == false) {
			System.out.println("Human Winner");
			setScore((varscore_human = varscore_human+2), (varscore_comp = varscore_comp+0));
			playAgain.setVisible(true);
			message.setVisible(true);
			message.setText("Human Wins");
			setClickable(false);			
			return true;
		}
		
		if(human_winner == false && computer_winner == true) {
			System.out.println("Computer Winner");
			setScore((varscore_human = varscore_human+0), (varscore_comp = varscore_comp+2));
			playAgain.setVisible(true);
			message.setVisible(true);
			message.setText("Computer Wins");
			setClickable(false);
			return true;
		}
		return false;
	}
	
	//resets all positions in the game
	private void clearTable() {
		for(int i=0; i<3; i++) {
			for(int j=0; j<3; j++) {
				button[i][j].setText("");
			}
		}
	}
	
	
	//checks if an empty cell in the table is available
	private boolean emptyCellAvail() {
		for(int i=0; i<3; i++) {
			for(int j=0; j<3; j++) {
				if("".equals(button[i][j].getText())){
					return true;
				}
			}
		}
		return false;	
	}
	
	//random function generator to generate a random number
	private int randomNum(int min, int max) {
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
	
	//computes the next computer move, if two in a row x's or o's are not found a random position is placed.
	private void compNextMove() {
		System.out.println("here");
		
		if(!emptyCellAvail()) {
			System.out.println("no cells available");
			return;
		}
		
		String optimumposition = checkoptpos();		
		
		if(!optimumposition.equals("")) {
		    String[] arrSplit = optimumposition.split(",");
		    
		    int number0 = Integer.parseInt(arrSplit[0]);
		    int number1 = Integer.parseInt(arrSplit[1]);

			System.out.println("Optimimum Position is " + optimumposition);
			button[number0][number1].setText("O");
			detWinner();
		}
		else {
			System.out.println("No Optimimum found, positioning random number");
			int num1 = randomNum(0,2);
			int num2 = randomNum(0,2);
			
			while("O".equals(button[num1][num2].getText()) || "X".equals(button[num1][num2].getText())) {
				num1 = randomNum(0,2);
				num2 = randomNum(0,2);
				System.out.println("here2");
			}
			button[num1][num2].setText("O");
			detWinner();
		}
		
		
	}
	
	// function that sets that sets the empty cell that the player clicked on and checks if it was legal
	private void setPlayerOpt(int i, int j) {
		if(!"X".equals(button[i][j].getText()) && !"O".equals(button[i][j].getText())){
			button[i][j].setText("X");
			if(detWinner() == false) {
				compNextMove();
			}
			else{
				System.out.println("Nice");
			}
		}
		else {
			System.out.println("Invalid Operation");
			JOptionPane.showMessageDialog(frame, "Invalid Operation!");
		}
	}
	
	// Initialisation function that inititialises the GUI and alongside with the action listener
	public void initGUI() {
        panelbuttons = new JPanel();
        panelbuttons.setLayout(new GridLayout(3,3,3,3));
		panelbuttons.setBounds(100, 100, 300, 300);
		panelbuttons.setVisible(true);
		panelbuttons.setBackground(Color.black);
		
		panelscoreboard = new JPanel();
		panelscoreboard.setLayout(new GridBagLayout());
		panelscoreboard.setBounds(500,100,400,130);
		panelscoreboard.setVisible(true);
		panelscoreboard.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(1.0f)));

		GridBagConstraints constraints = new GridBagConstraints();
		
		panelmain = new JPanel();
        panelmain.setLayout(null);
		panelmain.add(panelbuttons);
		panelmain.add(panelscoreboard);
		
		frame = new JFrame("Tic-Tac-Toe");
		frame.setSize(1000,600);
		frame.add(panelmain);
		
		button = new JButton[3][3];
		
		scoreboard = new JLabel();
		texthuman = new JLabel();
		textcomputer = new JLabel();
		score_computer = new JLabel();
		score_human = new JLabel();
		message = new JLabel();
		playAgain = new JButton();
				
		constraints.fill = GridBagConstraints.HORIZONTAL;
		
		scoreboard.setText("SCORE BOARD");
		scoreboard.setFont(new Font("Arial", Font.BOLD, 30));
		scoreboard.setHorizontalAlignment(JLabel.CENTER);
		scoreboard.setOpaque(true);
		scoreboard.setBackground(Color.LIGHT_GRAY);
		constraints.ipady = 20;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		panelscoreboard.add(scoreboard,constraints);
		
		texthuman.setText("Human");
		texthuman.setFont(new Font("Arial", Font.PLAIN, 30));
		texthuman.setHorizontalAlignment(JLabel.CENTER);
		texthuman.setOpaque(true);
		texthuman.setBackground(Color.white);
		constraints.ipady = 0;
		constraints.weightx = 0.5;
		constraints.weighty = 0.5;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		panelscoreboard.add(texthuman,constraints);

		textcomputer.setText("Computer");
		textcomputer.setFont(new Font("Arial", Font.PLAIN, 30));
		textcomputer.setHorizontalAlignment(JLabel.CENTER);
		textcomputer.setOpaque(true);
		textcomputer.setBackground(Color.white);
		constraints.weightx = 0.5;
		constraints.weighty = 0.5;
		constraints.gridx = 1;
		constraints.gridy = 1;
		panelscoreboard.add(textcomputer,constraints);

		score_human.setText("0");
		score_human.setFont(new Font("Arial", Font.PLAIN, 30));
		score_human.setHorizontalAlignment(JLabel.CENTER);
		score_human.setOpaque(true);
		score_human.setBackground(Color.white);
		constraints.weightx = 0.5;
		constraints.weighty = 0.5;
		constraints.gridx = 0;
		constraints.gridy = 2;
		panelscoreboard.add(score_human,constraints);


		score_computer.setText("0");
		score_computer.setFont(new Font("Arial", Font.PLAIN, 30));
		score_computer.setHorizontalAlignment(JLabel.CENTER);
		score_computer.setOpaque(true);
		score_computer.setBackground(Color.white);
		constraints.weightx = 0.5;
		constraints.weighty = 0.5;
		constraints.gridx = 1;
		constraints.gridy = 2;
		panelscoreboard.add(score_computer,constraints);

		
		message.setBounds(500,270,400,50);
		message.setText("You go first");
		message.setFont(new Font("Arial", Font.PLAIN, 30));
		message.setHorizontalAlignment(SwingConstants.CENTER);
		
		playAgain.setBounds(500,340,400,50);
		playAgain.setText("Play Again");
		playAgain.setFont(new Font("Arial", Font.PLAIN, 30));
		
		int counter = 0;
		for(int i=0; i<3; i++) {
			for(int j=0; j<3; j++) {
					button[i][j] = new JButton("" + counter);
					panelbuttons.add(button[i][j]);
					button[i][j].setFont(new Font("Comic Sans MS", Font.PLAIN, 50));
					button[i][j].setBackground(Color.white);
					button[i][j].setFocusPainted(false);
			        int finali = i;
			        int finalj = j;
					
			        //checks if the button has been clicked
					button[i][j].addActionListener(new ActionListener(){	
						@Override
						public void actionPerformed(ActionEvent e) {
								message.setVisible(false);
								System.out.println("x: " + finali + " y: " + finalj);
								setPlayerOpt(finali,finalj);
						}
					});
					
					System.out.println(counter);
					counter++;
			}
		}
		
		//checks if the playagain button has been clicked
		playAgain.addActionListener(new ActionListener(){	
			@Override
			public void actionPerformed(ActionEvent e) {
				playAgain.setVisible(false);
				clearTable();
				gamenumber++;
				
				//checks if the game is an even number if it is an even number it lets the human play first, if not then it lets computer play first
				if(gamenumber%2 == 0) {
					System.out.println("Player goes first");
					message.setText("You go First");
					setClickable(true);
				}else {
					System.out.println("Computer goes first");
					message.setText("Computer Goes First");
					compNextMove();
					setClickable(true);
				}
			}
		});
		
		panelmain.add(message);
		panelmain.add(playAgain);
		
		playAgain.setVisible(false);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		clearTable();
	}
	
	public tictactoe() {
		initGUI();
	}
	
	public static void main (String[] args) {
		new tictactoe();
	}
}
